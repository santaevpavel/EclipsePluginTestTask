package ru.santaev.model.configuration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

public interface ILaunchConfigurationRepository {

	public ILaunchConfiguration[] getLaunchConfigurations() throws CoreException;
}
