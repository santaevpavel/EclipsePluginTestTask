package ru.santaev.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.santaev.model.configuration.CompositeLaunchConfiguration;
import ru.santaev.model.configuration.CompositeLaunchConfiguration.ChildLaunchConfiguration;
import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;
import ru.santaev.model.configuration.ILaunchConfigurationRepository;
import ru.santaev.utils.Check;

public class CompositeLaunchConfigurationTabViewModel {

	private ObservableList<Launch> allLaunchConfigurations = FXCollections.observableArrayList();
	private ObservableList<Launch> resultLaunchConfigurations = FXCollections.observableArrayList();
	private SimpleBooleanProperty isBroken = new SimpleBooleanProperty(false); 
	
	private List<ILaunchConfiguration> rawLaunchConfigurations = new ArrayList<>();
	private List<ILaunchConfiguration> rawResultLaunchConfigurations = new ArrayList<>();
	
	private CompositeLaunchConfiguration launchConfiguration = CompositeLaunchConfiguration.empty();
	private ICompositeLaunchConfigurationRepository repository;
	private ILaunchConfigurationRepository launchConfigurationsRepository;

	public CompositeLaunchConfigurationTabViewModel(ICompositeLaunchConfigurationRepository repository,
			ILaunchConfigurationRepository launchConfigurationsRepository) {
		Check.notNull("repository must be not null", repository);
		Check.notNull("launchConfigurationsRepository must be not null", launchConfigurationsRepository);
		this.repository = repository;
		this.launchConfigurationsRepository = launchConfigurationsRepository;
		
		fetchLaunchConfigurations();		
	}
	
	public ObservableList<Launch> getAllLaunchConfigurations() {
		return allLaunchConfigurations;
	}

	public ObservableList<Launch> getResultLaunchConfigurations() {
		return resultLaunchConfigurations;
	}

	public ObservableBooleanValue getIsBroken() {
		return isBroken;
	}

	public void addLaunchConfigurationToCompositeLaunch(int idx) {
		resultLaunchConfigurations.add(allLaunchConfigurations.get(idx));
		rawResultLaunchConfigurations.add(rawLaunchConfigurations.get(idx));
	}

	public void removeLaunchConfigurationFromCompositeLaunch(int idx) {
		resultLaunchConfigurations.remove(idx);
		rawResultLaunchConfigurations.remove(idx);
	}

	public void applyConfiguration(ILaunchConfigurationWorkingCopy configuration) {
		List<ChildLaunchConfiguration> childConfigurations = rawResultLaunchConfigurations.stream()
				.map(i -> new CompositeLaunchConfiguration.ChildLaunchConfiguration(i, 1000))
				.collect(Collectors.toList());
		launchConfiguration.setChildLaunchConfigurations(childConfigurations);
		repository.saveConfiguration(configuration, launchConfiguration);
	}

	public void restoreConfiguration(ILaunchConfiguration configuration) {
		CompositeLaunchConfiguration launchConfigurationData = repository.restoreConfiguration(configuration);
		if (launchConfigurationData != null) {
			this.launchConfiguration = launchConfigurationData;
			restoreFromLaunchConfigurationData(launchConfigurationData);
		}
	}

	private void restoreFromLaunchConfigurationData(CompositeLaunchConfiguration data) {
		rawResultLaunchConfigurations.clear();
		resultLaunchConfigurations.clear();

		boolean isAnyChildBroken = false;
		for (ChildLaunchConfiguration launchConfigurationData : data.getChildLaunchConfigurations()) {
			if (launchConfigurationData == null) {
				isAnyChildBroken = true;
				continue;
			}
			int idx = rawLaunchConfigurations.indexOf(launchConfigurationData.launchConfiguration);
			if (idx < 0) {
				isAnyChildBroken = true;
				continue;
			}
			addLaunchConfigurationToCompositeLaunch(idx);
		}
		if (isAnyChildBroken) {
			isBroken.setValue(true);
		}
	}

	private void fetchLaunchConfigurations() {
		try {
			rawLaunchConfigurations = Arrays.asList(launchConfigurationsRepository.getLaunchConfigurations());
			initLaunchConfigurationsListObservable(rawLaunchConfigurations);
		} catch (CoreException e) {
			rawLaunchConfigurations = List.of();
			e.printStackTrace();
		}
	}

	private void initLaunchConfigurationsListObservable(List<ILaunchConfiguration> rawLaunchConfigurations) {
		Iterator<Launch> launches = rawLaunchConfigurations.stream().map(i -> {
			try {
				return new Launch(0, i.getType().getName(), i.getName());
			} catch (CoreException e) {
				return new Launch(0, null, i.getName());
			}
		}).iterator();
		int idx = 0;
		while (launches.hasNext()) {
			Launch launch = launches.next();
			allLaunchConfigurations.add(new Launch(idx++, launch.type, launch.name));
		}
	}
	
	public static class Launch {

		private String name;
		private long id;
		private String type;

		public Launch(long id, String type, String name) {
			this.type = type;
			this.name = name;
			this.id = id;
		}

		public String getType() {
			return type;
		}
		
		public String getName() {
			return name;
		}

		public long getId() {
			return id;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (id ^ (id >>> 32));
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			result = prime * result + ((type == null) ? 0 : type.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Launch other = (Launch) obj;
			if (id != other.id)
				return false;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			if (type == null) {
				if (other.type != null)
					return false;
			} else if (!type.equals(other.type))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Launch [name=" + name + ", id=" + id + ", type=" + type + "]";
		}
		
		
	}
}
