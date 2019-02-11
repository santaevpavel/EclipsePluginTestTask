package ru.santaev.model.configuration;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.junit.Before;
import org.mockito.ArgumentCaptor;

import ru.santaev.model.configuration.CompositeLaunchConfiguration.ChildLaunchConfiguration;

import static org.mockito.Mockito.*;

import java.util.List;

public class CompositeLaunchConfigurationRepositoryTest {

	private static final int NUMBER_OF_LAUNCHES = 5;
	private ILaunchConfiguration[] allLaunchConfigurations;
	
	@Before
	public void setUp() throws Exception {
		allLaunchConfigurations = new ILaunchConfiguration[NUMBER_OF_LAUNCHES];
		for (int i = 0; i < NUMBER_OF_LAUNCHES; i++) {
			ILaunchConfiguration launchConfiguration = mock(ILaunchConfiguration.class);
			when(launchConfiguration.getMemento()).thenReturn("Launch" + i);
			allLaunchConfigurations[i] = launchConfiguration;
		}
	}

	@org.junit.Test
	public void testRestoreSuccess() throws CoreException {
		ICompositeLaunchConfigurationPreparedDataRepository innerRepository = createInnerRepository();
		CompositeLaunchConfigurationRepository repository = new CompositeLaunchConfigurationRepository(innerRepository, 
				createLaunchManager());
		
		CompositeLaunchConfiguration restoredConfiguration = repository.restoreConfiguration(any());
		List<ChildLaunchConfiguration> childs = restoredConfiguration.getChildLaunchConfigurations();
		
		assertEquals(2, childs.size());
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[1], 100)));
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[3], 2000)));
	}
	
	@org.junit.Test
	public void testStore() throws CoreException {
		ICompositeLaunchConfigurationPreparedDataRepository innerRepository = mock(ICompositeLaunchConfigurationPreparedDataRepository.class);
		CompositeLaunchConfigurationRepository repository = new CompositeLaunchConfigurationRepository(innerRepository, 
				mock(ILaunchManager.class));
		
		repository.saveConfiguration(mock(ILaunchConfigurationWorkingCopy.class), createLaunchConfiguration());
		
		CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration expectedConfiguration1 = new CompositeLaunchConfigurationPreparedData
				.ChildLaunchConfiguration("Launch1", 1000);
		CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration expectedConfiguration2 = new CompositeLaunchConfigurationPreparedData
				.ChildLaunchConfiguration("Launch2", 100);
	
		ArgumentCaptor<CompositeLaunchConfigurationPreparedData> captor = ArgumentCaptor.forClass(
				CompositeLaunchConfigurationPreparedData.class);		
		verify(innerRepository).saveConfiguration(any(), captor.capture());
		
		CompositeLaunchConfigurationPreparedData result = captor.getValue();
		
		assertEquals(2, result.childLaunchConfigurations.size());
		assertTrue(result.childLaunchConfigurations.contains(expectedConfiguration1));
		assertTrue(result.childLaunchConfigurations.contains(expectedConfiguration2));
	}
	
	private ICompositeLaunchConfigurationPreparedDataRepository createInnerRepository() {
		ICompositeLaunchConfigurationPreparedDataRepository repository = mock(ICompositeLaunchConfigurationPreparedDataRepository.class);
		
		CompositeLaunchConfigurationPreparedData preparedData = new CompositeLaunchConfigurationPreparedData();
		
		preparedData.childLaunchConfigurations = List.of(
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch1", 100),
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch3", 2000)
		);
		when(repository.restoreConfiguration(any())).thenReturn(preparedData);

		return repository;
	}
	
	private ILaunchManager createLaunchManager() throws CoreException {
		ILaunchManager launchManager = mock(ILaunchManager.class);
		for (int i = 0; i < NUMBER_OF_LAUNCHES; i++) {
			when(launchManager.getLaunchConfiguration("Launch" + i))
				.thenReturn(allLaunchConfigurations[i]);
		}
		return launchManager;
	}
	
	private CompositeLaunchConfiguration createLaunchConfiguration() {
		ChildLaunchConfiguration child1 = new ChildLaunchConfiguration(allLaunchConfigurations[1], 1000);
		ChildLaunchConfiguration child2 = new ChildLaunchConfiguration(allLaunchConfigurations[2], 100);
		CompositeLaunchConfiguration configuration = new CompositeLaunchConfiguration(List.of(child1, child2));
		return configuration;
	}
}
