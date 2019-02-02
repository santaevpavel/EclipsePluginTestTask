package ru.santaev;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IPersistableSourceLocator;
import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IStackFrame;

public class SourceLocator implements ISourceLocator {

	@Override
	public Object getSourceElement(IStackFrame stackFrame) {
		System.out.println("getSourceElement");
		return null;
	}

}
