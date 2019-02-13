package ru.santaev.factories;

import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;
import ru.santaev.model.configuration.ILaunchConfigurationRepository;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel;

public class ViewModelFactory implements IViewModelFactory {

	public CompositeLaunchConfigurationTabViewModel getCompositeLaunchConfigurationTabViewModel() {
		ICompositeLaunchConfigurationRepository repository = FactoryProvider.getInstance().getRepositoryFactory()
				.getCompositeLaunchConfigurationRawDataRepository();
		ILaunchConfigurationRepository launchConfigurationRepository = FactoryProvider.getInstance()
				.getRepositoryFactory().getLaunchConfigurationRepository();
		return new CompositeLaunchConfigurationTabViewModel(repository, launchConfigurationRepository);
	}
}
