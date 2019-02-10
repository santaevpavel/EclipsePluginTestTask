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

	private ILaunchConfiguration[] allLaunchConfigurations;
	
	@Before
	public void setUp() throws Exception {
		ILaunchConfiguration launchConfiguration1 = mock(ILaunchConfiguration.class);
		when(launchConfiguration1.getMemento()).thenReturn("Launch1");
		ILaunchConfiguration launchConfiguration2 = mock(ILaunchConfiguration.class);
		when(launchConfiguration2.getMemento()).thenReturn("Launch2");
		ILaunchConfiguration launchConfiguration3 = mock(ILaunchConfiguration.class);
		when(launchConfiguration3.getMemento()).thenReturn("Launch3");
		
		allLaunchConfigurations = new ILaunchConfiguration[] {
				launchConfiguration1, launchConfiguration2, launchConfiguration3
		};
	}

	@org.junit.Test
	public void testRestoreSuccess() throws CoreException {
		ICompositeLaunchConfigurationPreparedDataRepository innerRepository = createInnerRepository();
		ILaunchManager launchManager = createLaunchManager();
		CompositeLaunchConfigurationRepository repository = new CompositeLaunchConfigurationRepository(innerRepository, launchManager);
		
		CompositeLaunchConfiguration restoredConfiguration = repository.restoreConfiguration(any());
		List<ChildLaunchConfiguration> childs = restoredConfiguration.getChildLaunchConfigurations();
		
		assertEquals(2, childs.size());
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[0], 100)));
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[2], 2000)));
	}
	
	@org.junit.Test
	public void testStore() throws CoreException {
		ICompositeLaunchConfigurationPreparedDataRepository innerRepository = mock(ICompositeLaunchConfigurationPreparedDataRepository.class);
		CompositeLaunchConfigurationRepository repository = new CompositeLaunchConfigurationRepository(innerRepository, 
				mock(ILaunchManager.class));
		
		CompositeLaunchConfiguration launchConfiguration = createLaunchConfiguration();
		repository.saveConfiguration(mock(ILaunchConfigurationWorkingCopy.class), launchConfiguration);
		
		CompositeLaunchConfigurationPreparedData configuration = new CompositeLaunchConfigurationPreparedData();
		configuration.childLaunchConfigurations = List.of(
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch2", 1000),
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch3", 100)
		);
		ArgumentCaptor<CompositeLaunchConfigurationPreparedData> captor = ArgumentCaptor.forClass(
				CompositeLaunchConfigurationPreparedData.class);		
		verify(innerRepository).saveConfiguration(any(), captor.capture());
		
		CompositeLaunchConfigurationPreparedData result = captor.getValue();
		
		assertEquals(2, result.childLaunchConfigurations.size());
		assertTrue(result.childLaunchConfigurations.contains(configuration.childLaunchConfigurations.get(0)));
		assertTrue(result.childLaunchConfigurations.contains(configuration.childLaunchConfigurations.get(1)));
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
		when(launchManager.getLaunchConfiguration("Launch1")).thenReturn(allLaunchConfigurations[0]);
		when(launchManager.getLaunchConfiguration("Launch2")).thenReturn(allLaunchConfigurations[1]);
		when(launchManager.getLaunchConfiguration("Launch3")).thenReturn(allLaunchConfigurations[2]);
		return launchManager;
	}
	
	private CompositeLaunchConfiguration createLaunchConfiguration() {
		ChildLaunchConfiguration child1 = new ChildLaunchConfiguration(allLaunchConfigurations[1], 1000);
		ChildLaunchConfiguration child2 = new ChildLaunchConfiguration(allLaunchConfigurations[2], 100);
		CompositeLaunchConfiguration configuration = new CompositeLaunchConfiguration(List.of(child1, child2));
		return configuration;
	}
}
