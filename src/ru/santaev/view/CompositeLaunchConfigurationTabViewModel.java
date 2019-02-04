package ru.santaev.view;

import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class CompositeLaunchConfigurationTabViewModel {
	
	public ObservableList<Launch> launches = FXCollections.observableArrayList();
	
	public CompositeLaunchConfigurationTabViewModel() {
		launches.addAll(new Launch(0, "Launch1"), new Launch(1, "Launch2")); 
	}
	
	public void remove(long id) {
		Optional<Launch> itemToRemove = launches.stream().filter(i -> i.getId() == id).findFirst();
		if (itemToRemove.isPresent()) {
			launches.remove(itemToRemove.get());
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
