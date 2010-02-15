package org.fusesource.tools.messaging.ui;

import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Font;

public class FontBasedLabelProvider extends LabelProvider implements IFontProvider {

	private Font regularFont;

	public FontBasedLabelProvider() {
	}

	public Font getFont(Object element) {
		return regularFont;
	}

}
