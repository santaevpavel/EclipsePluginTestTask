package ru.santaev.model.configuration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import ru.santaev.model.configuration.CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration;


public class CompositeLaunchConfigurationPreparedDataRepository implements ICompositeLaunchConfigurationPreparedDataRepository {
	
	private static final String ATTR_LAUNCHES = "launches";
	private static final String ATTR_DELAYS = "delays";
	
	@Override
	public void saveConfiguration(ILaunchConfigurationWorkingCopy configuration, CompositeLaunchConfigurationPreparedData data) {
		if (configuration == null) {
			throw new NullPointerException("Configuration should be set");
		}
		List<String> launches = new ArrayList<>();
		List<String> delays = new ArrayList<>();
		
		data.childLaunchConfigurations.forEach(c -> {
			launches.add(c.launchConfiguration);
			delays.add(String.valueOf(c.delayMillis));
		});
		configuration.setAttribute(ATTR_LAUNCHES, launches);
		configuration.setAttribute(ATTR_DELAYS, delays);
	}

	@Override
	public CompositeLaunchConfigurationPreparedData restoreConfiguration(ILaunchConfiguration configuration) {
		CompositeLaunchConfigurationPreparedData data = new CompositeLaunchConfigurationPreparedData();
		try {
			List<ChildLaunchConfiguration> childs = new ArrayList<>();
			List<String> launches = configuration.getAttribute(ATTR_LAUNCHES, List.of());
			List<String> delays = configuration.getAttribute(ATTR_DELAYS, List.of());
			
			for (int i = 0; i < launches.size(); i++) {
				childs.add(new ChildLaunchConfiguration(launches.get(i), Long.valueOf(delays.get(i))));
			}
			data.childLaunchConfigurations = childs;
		} catch (CoreException e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
			throw e;
		}
		return data;
	}

}
