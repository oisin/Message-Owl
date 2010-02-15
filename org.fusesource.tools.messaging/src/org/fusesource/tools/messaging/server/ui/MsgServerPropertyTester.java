package org.fusesource.tools.messaging.server.ui;

import org.eclipse.core.expressions.PropertyTester;

public class MsgServerPropertyTester extends PropertyTester {

	public MsgServerPropertyTester() {
	}

	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		return false;
	}

}
