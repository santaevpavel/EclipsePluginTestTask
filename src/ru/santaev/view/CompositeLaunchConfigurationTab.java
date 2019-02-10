package ru.santaev.view;

import java.util.stream.Collectors;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import javafx.collections.ListChangeListener;
import ru.santaev.factories.FactoryProvider;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel.Launch;

public class CompositeLaunchConfigurationTab extends AbstractLaunchConfigurationTab {
	
	private CompositeLaunchConfigurationTabViewModel viewModel;
	private ICompositeLaunchConfigurationTabControlCreator controlCreator = new CompositeLaunchConfigurationTabControlCreator();
	private Controls controls;
	
	public CompositeLaunchConfigurationTab(ILaunchConfigurationDialog dialog) {
		viewModel = FactoryProvider.getInstance().getViewModelFactory().getCompositeLaunchConfigurationTabViewModel();
		setLaunchConfigurationDialog(dialog);
	}

	@Override
	public void createControl(Composite parent) {
		controls = controlCreator.create(parent);
		setControl(controls.root);
		
		controls.removeButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIdx = controls.resultLaunchConfigurationsList.getSelectionIndex();
				if (selectionIdx < 0) {
					return;
				}
				viewModel.removeLaunchConfigurationFromCompositeLaunch(selectionIdx);
				scheduleUpdateJob();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
		});
		controls.addButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIdx = controls.allLaunchConfigurationsList.getSelectionIndex();
				if (selectionIdx < 0) {
					return;
				}
				viewModel.addLaunchConfigurationToCompositeLaunch(selectionIdx);
				scheduleUpdateJob();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
		});
		viewModel.allLaunchConfigurations.addListener((ListChangeListener<Launch>) c -> onAllLaunchesListChanged());
		viewModel.resultLaunchConfigurations.addListener((ListChangeListener<Launch>) c -> onResultLaunchesListChanged());
		onAllLaunchesListChanged();
		onResultLaunchesListChanged();
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("setDefaults");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		System.out.println("initializeFrom");
		viewModel.restoreConfiguration(configuration);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("performApply ");
		viewModel.applyConfiguration(configuration);
	}

	@Override
	public String getName() {
		return "Main";
	}
	
	private void onResultLaunchesListChanged() {
		java.util.List<String> names = viewModel.resultLaunchConfigurations.stream().map(t -> t.getName()).collect(Collectors.toList());
		controls.resultLaunchConfigurationsList.removeAll();
		controls.resultLaunchConfigurationsList.setItems(names.toArray(new String[names.size()]));
	}
	
	private void onAllLaunchesListChanged() {
		java.util.List<String> names = viewModel.allLaunchConfigurations.stream().map(t -> t.getName()).collect(Collectors.toList());
		controls.allLaunchConfigurationsList.removeAll();
		controls.allLaunchConfigurationsList.setItems(names.toArray(new String[names.size()]));
	}
	
	protected static class Controls {
		public List allLaunchConfigurationsList;
		public List resultLaunchConfigurationsList;
		public Composite root;
		public Button removeButton;
		public Button addButton;
	}
}
