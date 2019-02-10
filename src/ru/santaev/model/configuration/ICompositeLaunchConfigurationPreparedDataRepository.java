package ru.santaev.model.configuration;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public interface ICompositeLaunchConfigurationPreparedDataRepository {
	
	public void saveConfiguration(ILaunchConfigurationWorkingCopy configuration, CompositeLaunchConfigurationPreparedData data);
	
	public CompositeLaunchConfigurationPreparedData restoreConfiguration(ILaunchConfiguration configuration);
}
