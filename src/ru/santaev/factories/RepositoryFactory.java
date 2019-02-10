package ru.santaev.factories;

import org.eclipse.debug.core.DebugPlugin;

import ru.santaev.model.configuration.CompositeLaunchConfigurationPreparedDataRepository;
import ru.santaev.model.configuration.CompositeLaunchConfigurationRepository;
import ru.santaev.model.configuration.ICompositeLaunchConfigurationPreparedDataRepository;
import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;

public class RepositoryFactory implements IRepositoryFactory {

	public ICompositeLaunchConfigurationPreparedDataRepository getCompositeLaunchConfugurationPreparedDataRepository() {
		return new CompositeLaunchConfigurationPreparedDataRepository();
	}
	
	public ICompositeLaunchConfigurationRepository getCompositeLaunchConfugurationRawDataRepository() {
		ICompositeLaunchConfigurationPreparedDataRepository repository = getCompositeLaunchConfugurationPreparedDataRepository();
		return new CompositeLaunchConfigurationRepository(repository, DebugPlugin.getDefault().getLaunchManager());
	}
	
	public RepositoryFactory() {
	}
}
