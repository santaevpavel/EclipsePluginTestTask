package ru.santaev.factories;

import org.eclipse.debug.core.DebugPlugin;
import ru.santaev.model.CompositeLaunchConfigurationPreparedDataRepository;
import ru.santaev.model.CompositeLaunchConfigurationRawDataRepository;
import ru.santaev.model.ICompositeLaunchConfigurationPreparedDataRepository;
import ru.santaev.model.ICompositeLaunchConfigurationRawDataRepository;

public class RepositoryFactory implements IRepositoryFactory {

	public ICompositeLaunchConfigurationPreparedDataRepository getCompositeLaunchConfugurationPreparedDataRepository() {
		return new CompositeLaunchConfigurationPreparedDataRepository();
	}
	
	public ICompositeLaunchConfigurationRawDataRepository getCompositeLaunchConfugurationRawDataRepository() {
		ICompositeLaunchConfigurationPreparedDataRepository repository = getCompositeLaunchConfugurationPreparedDataRepository();
		return new CompositeLaunchConfigurationRawDataRepository(repository, DebugPlugin.getDefault().getLaunchManager());
	}
	
	public RepositoryFactory() {
	}
}
