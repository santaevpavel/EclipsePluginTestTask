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
		List<CompositeLaunchConfiguration.ChildLaunchConfiguration> launchConfigurations = data.childLaunchConfigurations
			.stream()
			.map(child -> {
				return new CompositeLaunchConfiguration.ChildLaunchConfiguration(
						getLaunchConfiguration(child.launchConfiguration), child.delayMillis);	
			})
			.collect(Collectors.toList());
		CompositeLaunchConfiguration rawData = new CompositeLaunchConfiguration(launchConfigurations);
		return rawData;
	}
	
	private CompositeLaunchConfigurationPreparedData prepare(CompositeLaunchConfiguration rawData) {
		List<CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration> launches = rawData.getChildLaunchConfigurations()
				.stream()
				.map(item -> {
					String launch = getLaunchConfigurationStoreData(item.launchConfiguration);
					return new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration(launch, item.delayMillis);
				})
				.collect(Collectors.toList());
		CompositeLaunchConfigurationPreparedData data = new CompositeLaunchConfigurationPreparedData();
		data.childLaunchConfigurations = launches;
		return data; 
	}
	
	private String getLaunchConfigurationStoreData(ILaunchConfiguration launchConfiguration) {
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
