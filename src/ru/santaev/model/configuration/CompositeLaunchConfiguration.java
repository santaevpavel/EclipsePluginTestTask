package ru.santaev.model.configuration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunchConfiguration;

public class CompositeLaunchConfiguration {
	private List<ILaunchConfiguration> childLaunchConfigurations;
	
	public CompositeLaunchConfiguration(List<ILaunchConfiguration> childLaunchConfigurations) {
		this.setChildLaunchConfigurations(childLaunchConfigurations);
	}
	
	public List<ILaunchConfiguration> getChildLaunchConfigurations() {
		return childLaunchConfigurations;
	}

	public void setChildLaunchConfigurations(List<ILaunchConfiguration> childLaunchConfigurations) {
		this.childLaunchConfigurations = childLaunchConfigurations;
	}

	class ChildLaunchConfiguration {
		public ILaunchConfiguration launchConfiguration;
		public long delayMillis;
		
		public ChildLaunchConfiguration(ILaunchConfiguration launchConfiguration, long delayMillis) {
			this.launchConfiguration = launchConfiguration;
			this.delayMillis = delayMillis;
		}
	}
	
	public static CompositeLaunchConfiguration empty() {
		return new CompositeLaunchConfiguration(new ArrayList<>());
	}
}
