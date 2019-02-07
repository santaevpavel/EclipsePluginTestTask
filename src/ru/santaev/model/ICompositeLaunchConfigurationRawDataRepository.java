package ru.santaev.model;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public interface ICompositeLaunchConfigurationRawDataRepository {
	
	public void saveConfiguration(ILaunchConfigurationWorkingCopy configuration, CompositeLaunchConfigurationRawData data);
	
	public CompositeLaunchConfigurationRawData restoreConfiguration(ILaunchConfiguration configuration);
}
