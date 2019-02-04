package ru.santaev.view;

import java.util.stream.Collectors;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

import javafx.collections.ListChangeListener;
import ru.santaev.view.CompositeLaunchConfigurationTabViewModel.Launch;

public class CompositeLaunchConfigurationTab extends AbstractLaunchConfigurationTab {

	private CompositeLaunchConfigurationTabViewModel viewModel = new CompositeLaunchConfigurationTabViewModel();
	
	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setSize(1000, 1000);
	    GridLayout layout = new GridLayout(2, true);
	    composite.setLayout(layout);
	    
		
		setControl(composite);
		
		List launchConfigurationsList = new List(composite, SWT.PUSH);
		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 400;
		launchConfigurationsList.setLayoutData(gridData);
		
		Button removeButton = new Button(composite, SWT.PUSH);
		removeButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				viewModel.remove(viewModel.launches.stream().findFirst().get().getId());
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		viewModel.launches.addListener((ListChangeListener<Launch>) c -> {
			java.util.List<String> names = c.getList().stream().map(t -> t.getName()).collect(Collectors.toList());
			launchConfigurationsList.removeAll();
			launchConfigurationsList.setItems(names.toArray(new String[names.size()])); 
		});
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getName() {
		return "Main";
	}
}
