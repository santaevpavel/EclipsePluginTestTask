package ru.santaev.model.view;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import ru.santaev.model.configuration.CompositeLaunchConfiguration;
import ru.santaev.model.configuration.CompositeLaunchConfiguration.ChildLaunchConfiguration;
import ru.santaev.model.configuration.ICompositeLaunchConfigurationRepository;
import ru.santaev.model.configuration.ILaunchConfigurationRepository;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel.Launch;

public class CompositeLaunchConfigurationTabViewModelTest {

	private ILaunchConfiguration[] allLaunchConfigurations;
	private List<Launch> allLaunch;
	private Matcher<Launch>[] allLaunchMatchers;
	private CompositeLaunchConfiguration launchConfiguration;

	@Before
	public void setup() throws CoreException {
		setupLaunchConfigurations();
		setupCompositeLaunchConfiguration();
	}

	@Test
	public void testListOfLaunchConfigurations() throws CoreException {
		CompositeLaunchConfigurationTabViewModel viewModel = createViewModel();

		assertThat(viewModel.getAllLaunchConfigurations(), containsInAnyOrder(allLaunchMatchers));
	}

	@Test
	public void testRestoreConfiguration() throws CoreException {
		CompositeLaunchConfigurationTabViewModel viewModel = createViewModel();
		viewModel.restoreConfiguration(mock(ILaunchConfiguration.class));

		Assert.assertTrue(viewModel.getIsBroken().get());

		assertThat(viewModel.getResultLaunchConfigurations(), Matchers.hasSize(1));
		assertThat(viewModel.getResultLaunchConfigurations(),
				containsInAnyOrder(Matchers.equalTo(new Launch(0, "LaunchType" + 0, "Launch" + 0))));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddConfiguration() throws CoreException {
		CompositeLaunchConfigurationTabViewModel viewModel = createViewModel();
		viewModel.restoreConfiguration(mock(ILaunchConfiguration.class));
		
		assertThat(viewModel.getResultLaunchConfigurations(),
				containsInAnyOrder(Matchers.equalTo(new Launch(0, "LaunchType" + 0, "Launch" + 0))));
		
		viewModel.addLaunchConfigurationToCompositeLaunch(1);
		
		Matcher<Launch> expectedLaunches[] = new Matcher[] {
			Matchers.equalTo(new Launch(0, "LaunchType" + 0, "Launch" + 0)),
			Matchers.equalTo(new Launch(1, "LaunchType" + 1, "Launch" + 1))
		};
		assertThat(viewModel.getResultLaunchConfigurations(), containsInAnyOrder(expectedLaunches));
	}
	
	@SuppressWarnings("unchecked")
	private void setupLaunchConfigurations() throws CoreException {
		int size = 5;
		allLaunchConfigurations = new ILaunchConfiguration[size];
		allLaunch = new ArrayList<>();
		allLaunchMatchers = new Matcher[size];
		for (int i = 0; i < size; i++) {
			ILaunchConfiguration launch = mock(ILaunchConfiguration.class);
			ILaunchConfigurationType type = mock(ILaunchConfigurationType.class);
			when(launch.getName()).thenReturn("Launch" + i);
			when(launch.getType()).thenReturn(type);
			when(type.getName()).thenReturn("LaunchType" + i);

			allLaunchConfigurations[i] = launch;
			allLaunch.add(new Launch(i, "LaunchType" + i, "Launch" + i));
			allLaunchMatchers[i] = Matchers.equalTo(new Launch(i, "LaunchType" + i, "Launch" + i));
		}
	}

	private void setupCompositeLaunchConfiguration() {
		List<ChildLaunchConfiguration> childs = new ArrayList<>();
		childs.add(new ChildLaunchConfiguration(allLaunchConfigurations[0], 100));
		childs.add(null);
		launchConfiguration = new CompositeLaunchConfiguration(childs);
	}

	private CompositeLaunchConfigurationTabViewModel createViewModel() throws CoreException {
		ICompositeLaunchConfigurationRepository launchConfigurationRepository = mock(
				ICompositeLaunchConfigurationRepository.class);
		ILaunchConfigurationRepository repository = mock(ILaunchConfigurationRepository.class);

		when(repository.getLaunchConfigurations()).thenReturn(allLaunchConfigurations);

		when(launchConfigurationRepository.restoreConfiguration(any())).thenReturn(launchConfiguration);
		CompositeLaunchConfigurationTabViewModel viewModel = new CompositeLaunchConfigurationTabViewModel(
				launchConfigurationRepository, repository);
		return viewModel;
	}
}
