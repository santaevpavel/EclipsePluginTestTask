package ru.santaev.view;

import java.util.stream.Collectors;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import javafx.collections.ListChangeListener;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel.Launch;

public class CompositeLaunchConfigurationTab extends AbstractLaunchConfigurationTab {

	private CompositeLaunchConfigurationTabViewModel viewModel = new CompositeLaunchConfigurationTabViewModel();
	private ICompositeLaunchConfigurationTabControlCreator controlCreator = new CompositeLaunchConfigurationTabControlCreator();
	private Controls controls;
	
	@Override
	public void createControl(Composite parent) {
		controls = controlCreator.create(parent);
		setControl(controls.root);
		
		controls.removeButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewModel.remove(viewModel.launches.stream().findFirst().get().getId());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {	
			}
		});
		
		viewModel.launches.addListener((ListChangeListener<Launch>) c -> {
			java.util.List<String> names = c.getList().stream().map(t -> t.getName()).collect(Collectors.toList());
			controls.resultLaunchConfigurationsList.removeAll();
			controls.resultLaunchConfigurationsList.setItems(names.toArray(new String[names.size()])); 
		});
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("setDefaults");
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		System.out.println("initializeFrom");
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		System.out.println("performApply");
	}

	@Override
	public String getName() {
		return "Main";
	}
	
	protected static class Controls {
		public List allLaunchConfigurationsList;
		public List resultLaunchConfigurationsList;
		public Composite root;
		public Button removeButton;
		public Button addButton;
	}
}
