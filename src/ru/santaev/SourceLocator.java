package ru.santaev;

import org.eclipse.debug.core.model.ISourceLocator;
import org.eclipse.debug.core.model.IStackFrame;

public class SourceLocator implements ISourceLocator {

	@Override
	public Object getSourceElement(IStackFrame stackFrame) {
		System.out.println("getSourceElement");
		return null;
	}

}
