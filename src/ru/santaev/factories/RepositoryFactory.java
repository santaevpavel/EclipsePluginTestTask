package ru.santaev.factories;

import org.eclipse.debug.core.DebugPlugin;

import ru.santaev.model.configuration.CompositeLaunchConfigurationPreparedDataRepository;
import ru.santaev.model.configuration.CompositeLaunchConfigurationRepository;
import ru.santaev.model.configuration.ICompositeLaunchConfigurationPreparedDataRepository;
import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;
import ru.santaev.model.configuration.ILaunchConfigurationRepository;
import ru.santaev.model.configuration.LaunchConfigurationRepository;

public class RepositoryFactory implements IRepositoryFactory {

	public ICompositeLaunchConfigurationPreparedDataRepository getCompositeLaunchConfugurationPreparedDataRepository() {
		return new CompositeLaunchConfigurationPreparedDataRepository();
	}
	
	public ICompositeLaunchConfigurationRepository getCompositeLaunchConfigurationRawDataRepository() {
		ICompositeLaunchConfigurationPreparedDataRepository repository = getCompositeLaunchConfugurationPreparedDataRepository();
		return new CompositeLaunchConfigurationRepository(repository, DebugPlugin.getDefault().getLaunchManager());
	}
	
	public ILaunchConfigurationRepository getLaunchConfigurationRepository() {
		return new LaunchConfigurationRepository(DebugPlugin.getDefault().getLaunchManager());
	}
	
	public RepositoryFactory() {
	}
}
