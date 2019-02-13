package ru.santaev.model;

import java.text.MessageFormat;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ru.santaev.factories.FactoryProvider;
import ru.santaev.model.configuration.CompositeLaunchConfiguration;
import ru.santaev.model.configuration.CompositeLaunchConfiguration.ChildLaunchConfiguration;
import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;

public class CompositeLaunchConfigurationDelegate implements ILaunchConfigurationDelegate {

	private static final Logger LOG = LoggerFactory.getLogger(CompositeLaunchConfigurationDelegate.class);
	
	private ICompositeLaunchConfigurationRepository repository = FactoryProvider.getInstance().getRepositoryFactory()
			.getCompositeLaunchConfigurationRawDataRepository();
	private ConsolePlugin consolePlugin = ConsolePlugin.getDefault();
	private CompositeLaunchConfiguration configurationData;
	private MessageConsole console = new MessageConsole("Composite", null);
	private MessageConsoleStream consoleOutput = console.newMessageStream();

	public CompositeLaunchConfigurationDelegate() {
		LOG.debug("CompositeLaunchConfigurationDelegate constructor");
	}

	@Override
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		initConsole();

		LOG.debug("Mode " + mode);
		monitor.beginTask("Loading configuration", IProgressMonitor.UNKNOWN);
		loadConfiguration(configuration, launch, monitor);
		launch(configurationData, configuration, mode, launch, monitor);
	}

	private void initConsole() {
		IConsole[] consoles = new IConsole[] { console };
		consolePlugin.getConsoleManager().addConsoles(consoles);
		console.clearConsole();
	}

	private void loadConfiguration(ILaunchConfiguration configuration, ILaunch launch, IProgressMonitor monitor)
			throws DebugException {
		monitor.beginTask("Loading configuration", IProgressMonitor.UNKNOWN);

		configurationData = repository.restoreConfiguration(configuration);
		if (configurationData == null) {
			writeToConsole("Unable to load configuration data");
			launch.terminate();
		}
		configurationData.getChildLaunchConfigurations().forEach((child) -> {
			if (child == null) {
				writeToConsole(
						"Warning: can't load child configuration. One of launch configurations had been changed.");
			}
		});
	}

	private void launch(CompositeLaunchConfiguration configurationData, ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		int numberOfLaunches = configurationData.getChildLaunchConfigurations().size();
		long delay = 0;
		for (ChildLaunchConfiguration childLaunchConfiguration : configurationData.getChildLaunchConfigurations()) {
			if (childLaunchConfiguration == null) {
				continue;
			}
			Timer timer = new Timer();
			delay += childLaunchConfiguration.delayMillis;
			timer.schedule(new ExecuteLaunchConfigurationTask(launch, childLaunchConfiguration, monitor, mode, "Launch",
					numberOfLaunches), delay);
		}
	}

	private void writeToConsole(String message) {
		consoleOutput.println(message);
	}

	private class ExecuteLaunchConfigurationTask extends TimerTask {

		private ChildLaunchConfiguration childLaunchConfiguration;
		private IProgressMonitor monitor;
		private String mode;
		private String message;
		private int totalLaunches;
		private ILaunchConfiguration launchConfiguration;
		private ILaunch launch;

		public ExecuteLaunchConfigurationTask(ILaunch launch, ChildLaunchConfiguration childLaunchConfiguration,
				IProgressMonitor monitor, String mode, String message, int totalLaunches) {
			super();
			this.launch = launch;
			this.childLaunchConfiguration = childLaunchConfiguration;
			this.monitor = monitor;
			this.mode = mode;
			this.message = message;
			this.totalLaunches = totalLaunches;
			this.launchConfiguration = childLaunchConfiguration.launchConfiguration;
		}

		@Override
		public void run() {
			monitor.beginTask(message, totalLaunches);
			String startMessage = MessageFormat.format("Launching \"{0}\"...", launchConfiguration.getName());
			writeToConsole(startMessage);
			try {
				ILaunch childLaunch = childLaunchConfiguration.launchConfiguration.launch(mode,
						new StubProgressMonitor(), true, false);
				for (IProcess process : childLaunch.getProcesses()) {
					launch.addProcess(process);
				}
			} catch (CoreException e) {
				writeToConsole(MessageFormat.format("Unable to launch\"{0}\"...", launchConfiguration.getName()));
			}
		}
	}
}
