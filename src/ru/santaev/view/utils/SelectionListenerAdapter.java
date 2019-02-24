package ru.santaev.view.utils;

import java.util.function.Consumer;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class SelectionListenerAdapter implements SelectionListener {

	private Consumer<SelectionEvent> proxy;
	
	public SelectionListenerAdapter(Consumer<SelectionEvent> proxy) {
		super();
		this.proxy = proxy;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (proxy != null) {
			proxy.accept(e);
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
	}
}
