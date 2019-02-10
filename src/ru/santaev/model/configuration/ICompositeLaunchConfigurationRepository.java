package ru.santaev.model.configuration;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

public interface ICompositeLaunchConfigurationRepository {
	
	public void saveConfiguration(ILaunchConfigurationWorkingCopy configuration, CompositeLaunchConfiguration data);
	
	public CompositeLaunchConfiguration restoreConfiguration(ILaunchConfiguration configuration);
}
