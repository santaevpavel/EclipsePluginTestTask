package ru.santaev.view;

import java.util.stream.Collectors;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import javafx.beans.value.ChangeListener;
import javafx.collections.ListChangeListener;
import ru.santaev.factories.FactoryProvider;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel.Launch;
import ru.santaev.view.utils.SelectionListenerAdapter;

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

		controls.removeButton.addSelectionListener(new SelectionListenerAdapter((e) -> onClickRemove()));
		controls.addButton.addSelectionListener(new SelectionListenerAdapter((e) -> onClickAdd()));
		controls.upButton.addSelectionListener(new SelectionListenerAdapter((e) -> onClickUp()));
		controls.downButton.addSelectionListener(new SelectionListenerAdapter((e) -> onClickDown()));
		
		viewModel.getAllLaunchConfigurations()
				.addListener((ListChangeListener<Launch>) c -> onAllLaunchesListChanged());
		viewModel.getResultLaunchConfigurations()
				.addListener((ListChangeListener<Launch>) c -> onResultLaunchesListChanged());
		viewModel.getIsBroken().addListener(
				(ChangeListener<Boolean>) (arg0, arg1, arg2) -> onIsBrokenChanged(viewModel.getIsBroken().get()));
		onAllLaunchesListChanged();
		onResultLaunchesListChanged();
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		viewModel.restoreConfiguration(configuration);
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		viewModel.applyConfiguration(configuration);
	}

	@Override
	public String getName() {
		return "Main";
	}

	private void onResultLaunchesListChanged() {
		java.util.List<String> names = viewModel.getResultLaunchConfigurations().stream().map(t -> t.getName())
				.collect(Collectors.toList());
		controls.resultLaunchConfigurationsList.removeAll();
		controls.resultLaunchConfigurationsList.setItems(names.toArray(new String[names.size()]));
	}

	private void onAllLaunchesListChanged() {
		java.util.List<String> names = viewModel.getAllLaunchConfigurations().stream().map(t -> t.getName())
				.collect(Collectors.toList());
		controls.allLaunchConfigurationsList.removeAll();
		controls.allLaunchConfigurationsList.setItems(names.toArray(new String[names.size()]));
	}

	private void onIsBrokenChanged(boolean isBroken) {
		if (isBroken) {
			getShell().getDisplay().asyncExec(() -> {
				MessageDialog.open(MessageDialog.INFORMATION, getShell(), Message.DIALOG_TITLE_WARNING,
						Message.MESSAGE_BROKEN_CONFIGURATION, SWT.NONE);
			});
		}
	}

	private void onClickRemove() {
		int selectionIdx = controls.resultLaunchConfigurationsList.getSelectionIndex();
		if (selectionIdx < 0) {
			return;
		}
		viewModel.removeLaunchConfigurationFromCompositeLaunch(selectionIdx);
		scheduleUpdateJob();
	}
	
	private void onClickAdd() {
		int selectionIdx = controls.allLaunchConfigurationsList.getSelectionIndex();
		if (selectionIdx < 0) {
			return;
		}
		viewModel.addLaunchConfigurationToCompositeLaunch(selectionIdx);
		scheduleUpdateJob();
	}
	
	private void onClickUp() {
		int selectionIdx = controls.resultLaunchConfigurationsList.getSelectionIndex();
		if (selectionIdx < 0) {
			return;
		}
		viewModel.moveUp(selectionIdx);
		scheduleUpdateJob();
	}

	private void onClickDown() {
		int selectionIdx = controls.resultLaunchConfigurationsList.getSelectionIndex();
		if (selectionIdx < 0) {
			return;
		}
		viewModel.moveDown(selectionIdx);
		scheduleUpdateJob();
	}

	protected static class Controls {
		public List allLaunchConfigurationsList;
		public List resultLaunchConfigurationsList;
		public Composite root;
		public Button removeButton;
		public Button upButton;
		public Button downButton;
		public Button addButton;
	}
}
