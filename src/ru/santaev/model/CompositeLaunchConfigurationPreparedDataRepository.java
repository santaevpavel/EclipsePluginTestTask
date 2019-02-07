package ru.santaev.model;

import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;


public class CompositeLaunchConfigurationPreparedDataRepository implements ICompositeLaunchConfigurationPreparedDataRepository {
	
	private static final String ATTR_LAUNCHES = "launches";
	
	@Override
	public void saveConfiguration(ILaunchConfigurationWorkingCopy configuration, CompositeLaunchConfigurationPreparedData data) {
		if (configuration == null) {
			throw new NullPointerException("Configuration should be set");
		}
		configuration.setAttribute(ATTR_LAUNCHES, data.launchDatas);
	}

	@Override
	public CompositeLaunchConfigurationPreparedData restoreConfiguration(ILaunchConfiguration configuration) {
		CompositeLaunchConfigurationPreparedData data = new CompositeLaunchConfigurationPreparedData();
		try {
			data.launchDatas = configuration.getAttribute(ATTR_LAUNCHES, List.of());
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return data;
	}
	
	
}
