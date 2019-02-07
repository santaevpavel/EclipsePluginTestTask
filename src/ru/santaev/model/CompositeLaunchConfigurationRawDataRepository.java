package ru.santaev.model;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;


public class CompositeLaunchConfigurationRawDataRepository implements ICompositeLaunchConfigurationRawDataRepository {

	private ICompositeLaunchConfigurationPreparedDataRepository repository;
	
	public CompositeLaunchConfigurationRawDataRepository(ICompositeLaunchConfigurationPreparedDataRepository repository) {
		this.repository = repository;
	}
	
	@Override
	public void saveConfiguration(ILaunchConfigurationWorkingCopy configuration,
			CompositeLaunchConfigurationRawData data) {
		repository.saveConfiguration(configuration, prepare(data));
	}

	@Override
	public CompositeLaunchConfigurationRawData restoreConfiguration(ILaunchConfiguration configuration) {
		CompositeLaunchConfigurationPreparedData data = repository.restoreConfiguration(configuration);
		return null;
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
}
