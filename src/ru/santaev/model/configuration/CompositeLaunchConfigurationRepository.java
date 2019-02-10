package ru.santaev.model.configuration;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;


public class CompositeLaunchConfigurationRepository implements ICompositeLaunchConfigurationRepository {

	private ICompositeLaunchConfigurationPreparedDataRepository repository;
	private ILaunchManager launchManager;
	
	public CompositeLaunchConfigurationRepository(ICompositeLaunchConfigurationPreparedDataRepository repository,
			ILaunchManager launchManager) {
		this.repository = repository;
		this.launchManager = launchManager;
	}
	
	@Override
	public void saveConfiguration(ILaunchConfigurationWorkingCopy configuration,
			CompositeLaunchConfiguration data) {
		repository.saveConfiguration(configuration, prepare(data));
	}

	@Override
	public CompositeLaunchConfiguration restoreConfiguration(ILaunchConfiguration configuration) {
		CompositeLaunchConfigurationPreparedData data = repository.restoreConfiguration(configuration);
		List<ILaunchConfiguration> launchConfigurations = data.launchDatas
			.stream()
			.map(momento -> getLaunchConfiguration(momento))
			.collect(Collectors.toList());
		CompositeLaunchConfiguration rawData = new CompositeLaunchConfiguration(launchConfigurations);
		return rawData;
	}
	
	private CompositeLaunchConfigurationPreparedData prepare(CompositeLaunchConfiguration rawData) {
		List<String> launches = rawData.getChildLaunchConfigurations()
				.stream()
				.map(item -> getLaunchCOnfigurationStoreData(item))
				.collect(Collectors.toList());
		CompositeLaunchConfigurationPreparedData data = new CompositeLaunchConfigurationPreparedData();
		data.launchDatas = launches;
		return data; 
	}
	
	private String getLaunchCOnfigurationStoreData(ILaunchConfiguration launchConfiguration) {
		try {
			return launchConfiguration.getMemento();
		} catch (CoreException e) {
			return null;
		}
	}
	
	private ILaunchConfiguration getLaunchConfiguration(String momento) {
		try {
			return launchManager.getLaunchConfiguration(momento);
		} catch (CoreException e) {
			e.printStackTrace();
			return null;
		}
	}
}
