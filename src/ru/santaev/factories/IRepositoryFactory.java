package ru.santaev.factories;

import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;

public interface IRepositoryFactory {

	public ICompositeLaunchConfigurationRepository getCompositeLaunchConfugurationRawDataRepository();
}
