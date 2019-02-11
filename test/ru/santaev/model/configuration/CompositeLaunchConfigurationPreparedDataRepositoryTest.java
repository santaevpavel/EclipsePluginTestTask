package ru.santaev.model.configuration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.util.List;

public class CompositeLaunchConfigurationPreparedDataRepositoryTest {

	private static final String ATTR_LAUNCHES = "launches";
	private static final String ATTR_DELAYS = "delays";

	private List<String> launches = List.of("Launch0", "Launch1", "Launch2");
	private List<String> delays = List.of("1", "20", "123");

	@Test
	public void testRestore() throws CoreException {
		CompositeLaunchConfigurationPreparedDataRepository repository = createRepository();

		CompositeLaunchConfigurationPreparedData configuration = repository
				.restoreConfiguration(createLaunchConfiguration());

		assertTrue(configuration.childLaunchConfigurations
				.contains(new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch0", 1)));
		assertTrue(configuration.childLaunchConfigurations
				.contains(new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch1", 20)));
		assertTrue(configuration.childLaunchConfigurations
				.contains(new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch2", 123)));
	}

	private ILaunchConfiguration createLaunchConfiguration() throws CoreException {
		ILaunchConfiguration configuration = mock(ILaunchConfiguration.class);
		when(configuration.getAttribute(eq(ATTR_LAUNCHES), anyList())).thenReturn(launches);
		when(configuration.getAttribute(eq(ATTR_DELAYS), anyList())).thenReturn(delays);

		return configuration;
	}

	private CompositeLaunchConfigurationPreparedDataRepository createRepository() {
		return new CompositeLaunchConfigurationPreparedDataRepository();
	}
}
