package ru.santaev.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;

import ru.santaev.view.CompositeLaunchConfigurationTab.Controls;

public class CompositeLaunchConfigurationTabControlCreator implements ICompositeLaunchConfigurationTabControlCreator {

	@Override
	public Controls create(Composite parent) {
		Controls result = new Controls();
		Composite composite = new Composite(parent, SWT.FILL);
		composite.setSize(1000, 1000);
	    GridLayout layout = new GridLayout(4, false);
	    composite.setLayout(layout);
		
		List allLaunchConfigurationsList = createAllLaunchConfigurationsList(composite);
		Button addButton = createAddButton(composite);
		List resultLaunchConfigurationsList = createResultLaunchConfigurationsList(composite);	
		Button removeButton = createRemoveButton(composite);
		
		result.allLaunchConfigurationsList = allLaunchConfigurationsList;
		result.resultLaunchConfigurationsList = resultLaunchConfigurationsList;
		result.root = composite;
		result.removeButton = removeButton;
		result.addButton = addButton;
		return result;
	}

	private List createAllLaunchConfigurationsList(Composite parent) {
		Group allLaunchConfigurationsGroup = new Group(parent, SWT.NONE);
		allLaunchConfigurationsGroup.setText("All launch configurations");
		allLaunchConfigurationsGroup.setLayout(new GridLayout());
		List allLaunchConfigurationsList = new List(allLaunchConfigurationsGroup, SWT.PUSH);
		allLaunchConfigurationsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		allLaunchConfigurationsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return allLaunchConfigurationsList;
	}
	
	private List createResultLaunchConfigurationsList(Composite parent) {
		Group resultLaunchConfigurationsGroup = new Group(parent, SWT.NONE);
		resultLaunchConfigurationsGroup.setText("Launch configurations");
		resultLaunchConfigurationsGroup.setLayout(new GridLayout());
		List resultLaunchConfigurationsList = new List(resultLaunchConfigurationsGroup, SWT.PUSH);
		resultLaunchConfigurationsList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		resultLaunchConfigurationsGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		return resultLaunchConfigurationsList;
	}
	
	private Button createRemoveButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText("Remove");
		return button;
	}
	
	private Button createAddButton(Composite parent) {
		Button button = new Button(parent, SWT.PUSH);
		button.setText(">>");
		return button;
	}
}
