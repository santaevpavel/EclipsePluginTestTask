package ru.santaev;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

import ru.santaev.view.CompositeLaunchConfigurationTab;

public class CompositeLaunchConfigurationTabGroup implements ILaunchConfigurationTabGroup {

	private List<CompositeLaunchConfigurationTab> tabs = new ArrayList<>();
	
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		tabs.add(new CompositeLaunchConfigurationTab());
	}

	@Override
	public ILaunchConfigurationTab[] getTabs() {
		return tabs.toArray(new ILaunchConfigurationTab[tabs.size()]);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
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
	public void launched(ILaunch launch) {
		// TODO Auto-generated method stub
		
	}

}
