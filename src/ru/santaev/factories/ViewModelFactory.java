package ru.santaev.factories;

import ru.santaev.model.ICompositeLaunchConfigurationRawDataRepository;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel;

public class ViewModelFactory implements IViewModelFactory {

	public CompositeLaunchConfigurationTabViewModel getCompositeLaunchConfigurationTabViewModel() {
		ICompositeLaunchConfigurationRawDataRepository repository = FactoryProvider.getInstance()
				.getRepositoryFactory().getCompositeLaunchConfugurationRawDataRepository();
		return new CompositeLaunchConfigurationTabViewModel(repository);
	}
}
