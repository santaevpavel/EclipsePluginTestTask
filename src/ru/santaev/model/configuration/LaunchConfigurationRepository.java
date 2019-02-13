package ru.santaev.model.configuration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

public class LaunchConfigurationRepository implements ILaunchConfigurationRepository {

	private ILaunchManager launchManager;
	
	public LaunchConfigurationRepository(ILaunchManager launchManager) {
		this.launchManager = launchManager;
	}

	@Override
	public ILaunchConfiguration[] getLaunchConfigurations() throws CoreException {
		return launchManager.getLaunchConfigurations();
	}

}
