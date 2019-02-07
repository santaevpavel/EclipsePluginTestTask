package ru.santaev.view;

import java.util.stream.Collectors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel.Launch;

public class CompositeLaunchConfigurationTab extends AbstractLaunchConfigurationTab {
	
	private CompositeLaunchConfigurationTabViewModel viewModel = new CompositeLaunchConfigurationTabViewModel();
	private ICompositeLaunchConfigurationTabControlCreator controlCreator = new CompositeLaunchConfigurationTabControlCreator();
	private Controls controls;
	
	public CompositeLaunchConfigurationTab(ILaunchConfigurationDialog dialog) {
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
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
		});
		viewModel.allLaunchConfigurations.addListener((ListChangeListener<Launch>) c -> onAllLaunchesListChanged());
		viewModel.resultLaunchConfigurations.addListener((ListChangeListener<Launch>) c -> onResultLaunchesListChanged());
		viewModel.getIsDirtyObservable().addListener((ChangeListener<Boolean>) (value, arg1, arg2) -> onIsDirtyChanged(value.getValue()));
		onAllLaunchesListChanged();
		onResultLaunchesListChanged();
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("setDefaults");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			System.out.println("initializeFrom " + configuration.getAttributes());
		} catch (CoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		//viewModel.applyConfiguration();
	}

	@Override
	public String getName() {
		return "Main";
	}
	
	private void onResultLaunchesListChanged() {
		java.util.List<String> names = viewModel.resultLaunchConfigurations.stream().map(t -> t.getName()).collect(Collectors.toList());
		controls.resultLaunchConfigurationsList.removeAll();
		controls.resultLaunchConfigurationsList.setItems(names.toArray(new String[names.size()]));
		scheduleUpdateJob();
	}
	
	private void onAllLaunchesListChanged() {
		java.util.List<String> names = viewModel.allLaunchConfigurations.stream().map(t -> t.getName()).collect(Collectors.toList());
		controls.allLaunchConfigurationsList.removeAll();
		controls.allLaunchConfigurationsList.setItems(names.toArray(new String[names.size()]));
	}
	
	private void onIsDirtyChanged(boolean value) {
		setDirty(value);
	}
	
	protected static class Controls {
		public List allLaunchConfigurationsList;
		public List resultLaunchConfigurationsList;
		public Composite root;
		public Button removeButton;
		public Button addButton;
	}
}
