package ru.santaev.factories;

import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel;

public class ViewModelFactory implements IViewModelFactory {

	public CompositeLaunchConfigurationTabViewModel getCompositeLaunchConfigurationTabViewModel() {
		ICompositeLaunchConfigurationRepository repository = FactoryProvider.getInstance()
				.getRepositoryFactory().getCompositeLaunchConfugurationRawDataRepository();
		return new CompositeLaunchConfigurationTabViewModel(repository);
	}
}
