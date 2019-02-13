package ru.santaev.factories;

import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;
import ru.santaev.model.configuration.ILaunchConfigurationRepository;

public interface IRepositoryFactory {

	public ICompositeLaunchConfigurationRepository getCompositeLaunchConfigurationRawDataRepository();
	
	public ILaunchConfigurationRepository getLaunchConfigurationRepository();
}
