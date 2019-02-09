package ru.santaev.model;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;


public class CompositeLaunchConfigurationRawDataRepository implements ICompositeLaunchConfigurationRawDataRepository {

	private ICompositeLaunchConfigurationPreparedDataRepository repository;
	private ILaunchManager launchManager;
	
	public CompositeLaunchConfigurationRawDataRepository(ICompositeLaunchConfigurationPreparedDataRepository repository,
			ILaunchManager launchManager) {
		this.repository = repository;
		this.launchManager = launchManager;
	}
	
	@Override
	public void saveConfiguration(ILaunchConfigurationWorkingCopy configuration,
			CompositeLaunchConfigurationRawData data) {
		repository.saveConfiguration(configuration, prepare(data));
	}

	@Override
	public CompositeLaunchConfigurationRawData restoreConfiguration(ILaunchConfiguration configuration) {
		CompositeLaunchConfigurationPreparedData data = repository.restoreConfiguration(configuration);
		List<ILaunchConfiguration> launchConfigurations = data.launchDatas
			.stream()
			.map(momento -> getLaunchConfiguration(momento))
			.collect(Collectors.toList());
		CompositeLaunchConfigurationRawData rawData = new CompositeLaunchConfigurationRawData();
		rawData.launchDatas = launchConfigurations;
		return rawData;
	}
	
	private CompositeLaunchConfigurationPreparedData prepare(CompositeLaunchConfigurationRawData rawData) {
		List<String> launches = rawData.launchDatas
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
