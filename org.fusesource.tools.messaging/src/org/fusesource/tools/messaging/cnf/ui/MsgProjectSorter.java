package org.fusesource.tools.messaging.cnf.ui;

import java.text.Collator;

import org.eclipse.jface.viewers.ViewerSorter;
import org.fusesource.tools.messaging.cnf.model.ListenersRootComponent;
import org.fusesource.tools.messaging.cnf.model.SendersRootComponent;


/**
 * Sorter for Messaging Project. Sticks the root components as immediate
 * children for the Messaging Project
 * 
 * @author kiranb
 * 
 */
public class MsgProjectSorter extends ViewerSorter {
	public static final int SENDERS_ROOT_POSITION = -2;
	public static final int LISTENERS_ROOT_POSITION = -1;

	public MsgProjectSorter() {
	}

	public MsgProjectSorter(Collator collator) {
		super(collator);
	}

	@Override
	public int category(Object element) {
		if (element instanceof SendersRootComponent)
			return SENDERS_ROOT_POSITION;
		else if (element instanceof ListenersRootComponent)
			return LISTENERS_ROOT_POSITION;
		return super.category(element);
	}
}