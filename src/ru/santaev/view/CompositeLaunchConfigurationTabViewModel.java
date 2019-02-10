package ru.santaev.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import ru.santaev.model.configuration.CompositeLaunchConfiguration;
import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;

public class CompositeLaunchConfigurationTabViewModel {
	
	public ObservableList<Launch> allLaunchConfigurations = FXCollections.observableArrayList();
	public ObservableList<Launch> resultLaunchConfigurations = FXCollections.observableArrayList();

	private List<ILaunchConfiguration> rawLaunchConfigurations = new ArrayList<>();
	private List<ILaunchConfiguration> rawResultLaunchConfigurations = new ArrayList<>();

	private CompositeLaunchConfiguration launchConfigurationData = CompositeLaunchConfiguration.empty();
	private ICompositeLaunchConfigurationRepository repository;
	
	public CompositeLaunchConfigurationTabViewModel(ICompositeLaunchConfigurationRepository repository) {
		this.repository = repository;
		fetchLaunchConfigurations();
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
		launchConfigurationData.setChildLaunchConfigurations(new ArrayList<>(rawResultLaunchConfigurations));
		repository.saveConfiguration(configuration, launchConfigurationData);
	}
	
	public void restoreConfiguration(ILaunchConfiguration configuration) {
		CompositeLaunchConfiguration launchConfigurationData = repository.restoreConfiguration(configuration);
		if (launchConfigurationData != null) {
			this.launchConfigurationData = launchConfigurationData;
			restoreFromLaunchConfigurationData(launchConfigurationData);
		}
	}
	
	private void restoreFromLaunchConfigurationData(CompositeLaunchConfiguration data) {
		rawResultLaunchConfigurations.clear();
		resultLaunchConfigurations.clear();
		
		for (ILaunchConfiguration launchConfigurationData: data.getChildLaunchConfigurations()) {
			int idx = rawLaunchConfigurations.indexOf(launchConfigurationData);
			if (idx < 0) {
				continue;
			}
			addLaunchConfigurationToCompositeLaunch(idx);
		}
	}
	
	private void fetchLaunchConfigurations() {
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		try {
			rawLaunchConfigurations = Arrays.asList(manager.getLaunchConfigurations());
			Iterator<String > names = rawLaunchConfigurations
				.stream()
				.map(i -> {
					try {
						return i.getType().getName() + ": "+ i.getName();
					} catch (CoreException e) {
						return "Unknown: " + i.getName();
					}
				})
				.iterator();
			int idx = 0;
			while (names.hasNext()) {
				allLaunchConfigurations.add(new Launch(idx++, names.next()));
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}
	
	class Launch {
		
		private String name;
		private long id; 
		
		public Launch(long id, String name) {
			this.name = name;
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public long getId() {
			return id;
		}
	}
}
