package ru.santaev.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CompositeLaunchConfigurationTabViewModel {
	
	public ObservableList<Launch> allLaunchConfigurations = FXCollections.observableArrayList();
	public ObservableList<Launch> resultLaunchConfigurations = FXCollections.observableArrayList();
	
	private List<ILaunchConfiguration> rawLaunchConfigurations = new ArrayList<>();
	
	public CompositeLaunchConfigurationTabViewModel() {
		fetchLaunchConfigurations();
	}
	
	public void add(long id) {
		if (0 > id || allLaunchConfigurations.size() <= id) {
			return;
		}
		resultLaunchConfigurations.add(allLaunchConfigurations.get((int) id));
	}
	
	public void remove(long id) {
		Optional<Launch> itemToRemove = allLaunchConfigurations.stream().filter(i -> i.getId() == id).findFirst();
		if (itemToRemove.isPresent()) {
			allLaunchConfigurations.remove(itemToRemove.get());
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
