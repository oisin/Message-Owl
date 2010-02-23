/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.ui;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MessagesHistoryDialog extends Dialog {

    private String value;

    public MessagesHistoryDialog(Shell parentShell, int value) {
        super(parentShell);
        this.value = value + "";
    }

    @Override
    public void create() {
        super.create();
        getShell().setText("Messages History Count");
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Control control = super.createDialogArea(parent);
        Composite panel = new Composite((Composite) control, SWT.NULL);
        GridLayout layout = new GridLayout(2, false);
        layout.marginWidth = 6;
        layout.marginLeft = 6;
        layout.marginRight = 6;
        panel.setLayout(layout);
        panel.setLayoutData(new GridData(GridData.FILL_BOTH));
        createLabel(panel, "Messages history count: ");
        createTextBox(panel, value, 1, SWT.NONE);
        return panel;
    }

    private void createTextBox(Composite parent, String value, int span, int sytle) {
        final Text text = new Text(parent, SWT.SINGLE | SWT.BORDER | sytle);
        text.setText(value);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = span;
        gd.minimumWidth = 50;
        text.setLayoutData(gd);
        text.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                String countValue = text.getText().trim();
                if (!isValid(countValue)) {
                    getButton(IDialogConstants.OK_ID).setEnabled(false);
                    return;
                }
                getButton(IDialogConstants.OK_ID).setEnabled(true);
                updateValue(countValue);
            }

            private boolean isValid(String countValue) {
                try {
                    int value = Integer.parseInt(countValue);
                    if (value <= 0) {
                        return false;
                    }
                } catch (NumberFormatException exp) {
                    return false;
                }
                return true;
            }

        });
    }

    protected void updateValue(String count) {
        value = count;
    }

    private Label createLabel(Composite parent, String labelName) {
        Label label = new Label(parent, SWT.NONE);
        label.setText(labelName);
        label.setLayoutData(new GridData());
        return label;
    }

    public int getHistoryCount() {
        int count = -1;
        try {
            count = Integer.parseInt(value);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return count;
    }
}
