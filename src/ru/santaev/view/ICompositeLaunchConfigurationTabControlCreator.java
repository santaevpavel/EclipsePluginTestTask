package ru.santaev.view;

import org.eclipse.swt.widgets.Composite;

import ru.santaev.view.CompositeLaunchConfigurationTab.Controls;

public interface ICompositeLaunchConfigurationTabControlCreator {

	public Controls create(Composite parent);
}
