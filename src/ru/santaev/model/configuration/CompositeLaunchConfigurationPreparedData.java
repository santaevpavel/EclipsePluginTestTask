package ru.santaev.model.configuration;

import java.util.List;

public class CompositeLaunchConfigurationPreparedData {
	public List<ChildLaunchConfiguration> childLaunchConfigurations;
	
	public static class ChildLaunchConfiguration {
		public String launchConfiguration;
		public long delayMillis;
		
		public ChildLaunchConfiguration(String launchConfiguration, long delayMillis) {
			super();
			this.launchConfiguration = launchConfiguration;
			this.delayMillis = delayMillis;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + (int) (delayMillis ^ (delayMillis >>> 32));
			result = prime * result + ((launchConfiguration == null) ? 0 : launchConfiguration.hashCode());
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
			ChildLaunchConfiguration other = (ChildLaunchConfiguration) obj;
			if (delayMillis != other.delayMillis)
				return false;
			if (launchConfiguration == null) {
				if (other.launchConfiguration != null)
					return false;
			} else if (!launchConfiguration.equals(other.launchConfiguration))
				return false;
			return true;
		}
		
		
	}
}
