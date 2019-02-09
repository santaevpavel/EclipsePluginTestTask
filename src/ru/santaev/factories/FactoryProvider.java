package ru.santaev.factories;

public class FactoryProvider {

	private static FactoryProvider instance;
	
	public static FactoryProvider getInstance() {
		if (instance == null) {
			instance = new FactoryProvider();
		}
		return instance;
	}
	
	private IRepositoryFactory repositoryFactory;
	private IViewModelFactory viewModelFactory;
	
	private FactoryProvider() {
		setRepositoryFactory(new RepositoryFactory());
		setViewModelFactory(new ViewModelFactory());
	}
	
	public void setRepositoryFactory(IRepositoryFactory factory) {
		repositoryFactory = factory;
	}

	public IRepositoryFactory getRepositoryFactory() {
		return repositoryFactory;
	}

	public void setViewModelFactory(IViewModelFactory viewModelFactory) {
		this.viewModelFactory = viewModelFactory;
	}
	
	public IViewModelFactory getViewModelFactory() {
		return viewModelFactory;
	}
}
