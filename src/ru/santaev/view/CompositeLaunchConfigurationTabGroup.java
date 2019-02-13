package ru.santaev.view;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTabGroup;

public class CompositeLaunchConfigurationTabGroup implements ILaunchConfigurationTabGroup {

	private List<CompositeLaunchConfigurationTab> tabs = new ArrayList<>();
	
	@Override
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		tabs.add(new CompositeLaunchConfigurationTab(dialog));
	}

	@Override
	public ILaunchConfigurationTab[] getTabs() {
		return tabs.toArray(new ILaunchConfigurationTab[tabs.size()]);
	}

	@Override
	public void dispose() {
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		// TODO initializeFrom from did'n called in tabs
		for (CompositeLaunchConfigurationTab tab : tabs) {
			tab.initializeFrom(configuration);  
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void launched(ILaunch launch) {
	}

}
