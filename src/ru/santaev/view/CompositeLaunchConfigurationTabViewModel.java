package ru.santaev.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableBooleanValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CompositeLaunchConfigurationTabViewModel {
	
	public ObservableList<Launch> allLaunchConfigurations = FXCollections.observableArrayList();
	public ObservableList<Launch> resultLaunchConfigurations = FXCollections.observableArrayList();
	public SimpleBooleanProperty isDirty = new SimpleBooleanProperty(true);

	private List<ILaunchConfiguration> rawLaunchConfigurations = new ArrayList<>();
	private List<ILaunchConfiguration> rawResultLaunchConfigurations = new ArrayList<>();
	
	public CompositeLaunchConfigurationTabViewModel() {
		fetchLaunchConfigurations();
	}
	
	public void addLaunchConfigurationToCompositeLaunch(int idx) {
		resultLaunchConfigurations.add(allLaunchConfigurations.get(idx));
		rawResultLaunchConfigurations.add(rawLaunchConfigurations.get(idx));
		isDirty.set(true);
	}
	
	public void removeLaunchConfigurationFromCompositeLaunch(int idx) {
		Launch itemToRemove = resultLaunchConfigurations.get(idx);
		resultLaunchConfigurations.remove(itemToRemove);
		isDirty.set(true);
	}
	
	public ObservableBooleanValue getIsDirtyObservable() {
		return isDirty;
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
	
	public StoreData getData() {
		List<String> launches = rawResultLaunchConfigurations
				.stream()
				.map(item -> getLaunchCOnfigurationStoreData(item))
				.collect(Collectors.toList());
		StoreData data = new StoreData();
		data.launches = launches;
		return data;
	}
	
	private String getLaunchCOnfigurationStoreData(ILaunchConfiguration launchConfiguration) {
		try {
			return launchConfiguration.getMemento();
		} catch (CoreException e) {
			return null;
		}
	}
	
	class StoreData {
		public List<String> launches;
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
