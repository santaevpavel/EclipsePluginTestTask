package ru.santaev.model.configuration;

import static org.junit.Assert.*;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.junit.Before;
import ru.santaev.model.configuration.CompositeLaunchConfiguration.ChildLaunchConfiguration;

import static org.mockito.Mockito.*;

import java.util.List;

public class CompositeLaunchConfigurationRepositoryRestoreTest {

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
		ICompositeLaunchConfigurationPreparedDataRepository innerRepository = createInnerRepositoryForSuccessTest();
		CompositeLaunchConfigurationRepository repository = new CompositeLaunchConfigurationRepository(innerRepository, 
				createLaunchManager());
		
		CompositeLaunchConfiguration restoredConfiguration = repository.restoreConfiguration(any());
		List<ChildLaunchConfiguration> childs = restoredConfiguration.getChildLaunchConfigurations();
		
		assertEquals(2, childs.size());
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[1], 100)));
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[3], 2000)));
	}
	
	@org.junit.Test
	public void testRestoreNotFound() throws CoreException {
		ICompositeLaunchConfigurationPreparedDataRepository innerRepository = createInnerRepositoryForFailedTest();
		CompositeLaunchConfigurationRepository repository = new CompositeLaunchConfigurationRepository(innerRepository, 
				createLaunchManager());
		
		CompositeLaunchConfiguration restoredConfiguration = repository.restoreConfiguration(any());
		List<ChildLaunchConfiguration> childs = restoredConfiguration.getChildLaunchConfigurations();
		
		assertEquals(3, childs.size());
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[0], 100)));
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[3], 2000)));
		assertTrue(childs.contains(null));
	}
	
	@org.junit.Test
	public void testRestoreThrowException() throws CoreException {
		ICompositeLaunchConfigurationPreparedDataRepository innerRepository = createInnerRepositoryForFailedTestThrowException();
		CompositeLaunchConfigurationRepository repository = new CompositeLaunchConfigurationRepository(innerRepository, 
				createLaunchManager());
		
		CompositeLaunchConfiguration restoredConfiguration = repository.restoreConfiguration(any());
		List<ChildLaunchConfiguration> childs = restoredConfiguration.getChildLaunchConfigurations();
		
		assertEquals(3, childs.size());
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[0], 100)));
		assertTrue(childs.contains(new CompositeLaunchConfiguration.ChildLaunchConfiguration(allLaunchConfigurations[3], 2000)));
		assertTrue(childs.contains(null));
	}
	
	private ICompositeLaunchConfigurationPreparedDataRepository createInnerRepositoryForSuccessTest() {
		ICompositeLaunchConfigurationPreparedDataRepository repository = mock(ICompositeLaunchConfigurationPreparedDataRepository.class);	
		CompositeLaunchConfigurationPreparedData preparedData = new CompositeLaunchConfigurationPreparedData();
		
		preparedData.childLaunchConfigurations = List.of(	
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch1", 100),
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch3", 2000)
		);
		when(repository.restoreConfiguration(any())).thenReturn(preparedData);

		return repository;
	}
	
	private ICompositeLaunchConfigurationPreparedDataRepository createInnerRepositoryForFailedTest() {
		ICompositeLaunchConfigurationPreparedDataRepository repository = mock(ICompositeLaunchConfigurationPreparedDataRepository.class);
		CompositeLaunchConfigurationPreparedData preparedData = new CompositeLaunchConfigurationPreparedData();
		
		preparedData.childLaunchConfigurations = List.of(
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch0", 100),
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("XXX", 1500),
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch3", 2000)
				
		);
		when(repository.restoreConfiguration(any())).thenReturn(preparedData);

		return repository;
	}
	
	private ICompositeLaunchConfigurationPreparedDataRepository createInnerRepositoryForFailedTestThrowException() {
		ICompositeLaunchConfigurationPreparedDataRepository repository = mock(ICompositeLaunchConfigurationPreparedDataRepository.class);
		CompositeLaunchConfigurationPreparedData preparedData = new CompositeLaunchConfigurationPreparedData();
		
		preparedData.childLaunchConfigurations = List.of(
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Launch0", 100),
				new CompositeLaunchConfigurationPreparedData.ChildLaunchConfiguration("Exception", 1500),
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
		Exception err = new CoreException(mock(IStatus.class));
		when(launchManager.getLaunchConfiguration(eq("Exception")))
			.thenThrow(err);
		return launchManager;
	}
}
