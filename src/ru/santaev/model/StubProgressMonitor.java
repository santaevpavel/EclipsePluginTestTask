package ru.santaev.model;

import org.eclipse.core.runtime.IProgressMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class StubProgressMonitor implements IProgressMonitor {

	private static final Logger LOG = LoggerFactory.getLogger(StubProgressMonitor.class);
	
	@Override
	public void beginTask(String name, int totalWork) {
		LOG.debug("Progress: " + "beginTask " + totalWork);
	}

	@Override
	public void done() {
		LOG.debug("Progress: " + "done");
	}

	@Override
	public void internalWorked(double work) {
	}

	@Override
	public boolean isCanceled() {
		return false;
	}

	@Override
	public void setCanceled(boolean value) {
	}

	@Override
	public void setTaskName(String name) {
	}

	@Override
	public void subTask(String name) {
	}

	@Override
	public void worked(int work) {
	}
}