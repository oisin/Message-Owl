package org.fusesource.tools.core.ui.url.urlchooser;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;


/** 
 */
public class URLChooserDialog  extends Dialog{

	private String title;
	private URLChooser chooser;
	private URLChooserFilter filter;
	private URL primeURL;
	private List supportedProviderIds;
	private List tempFSProviders;
	
	String buttonLabel[];
	
	protected int chooserStyle = URLChooser.STYLE_NONE;
	
	public URLChooserDialog(Shell shell, 
                            String title, 
                            URLChooserFilter filter)
    {
		super(shell);		
		this.title = title;
		this.filter = filter;		
	}

	public URLChooserDialog(Shell shell, 
            String title, 
            URLChooserFilter filter,
            int chooserStyle)
	{
		super(shell);		
		this.title = title;
		this.filter = filter;	
		this.chooserStyle = chooserStyle;
	}
	
	public URLChooserDialog(Shell shell, 
            String title, 
            URLChooserFilter filter,
            List supportedProviderIds,
            List tempFSProviders,
            int chooserStyle)
	{
		super(shell);		
		this.title = title;
		this.filter = filter;
		this.supportedProviderIds = supportedProviderIds;
		this.tempFSProviders = tempFSProviders;
		this.chooserStyle = chooserStyle;
	}
	
	public URLChooserDialog(Shell shell,
	                        String title, 
                            URLChooserFilter filter,
                            String buttonLabel[]) 
    {
		this(shell, title, filter);
		this.buttonLabel = buttonLabel;
	}
	
	public URLChooserDialog(Shell shell,
            String title, 
            URLChooserFilter filter,
            String buttonLabel[], int chooserStyle) 
	{
		this(shell, title, filter);
		this.buttonLabel = buttonLabel;
		this.chooserStyle = chooserStyle;
	}	
	

	/**
	 * Construct a URLChooserDialog and pre-populate the text field with the passed in url 
	 * @param shell
	 * @param title
	 * @param filter
	 * @param buttonLabel
	 * @param url	URL to pre-populate the control with
	 */
	public URLChooserDialog(Shell shell,
            String title, 
            URLChooserFilter filter,
            String buttonLabel[],
            URL url) 
	{
		this(shell, title, filter);
		this.buttonLabel = buttonLabel;
		primeURL = url;
	}

	public URLChooserDialog(Shell shell,
            String title, 
            URLChooserFilter filter,
            String buttonLabel[],
            URL url,
            int chooserStyle) 
	{
		this(shell, title, filter);
		this.buttonLabel = buttonLabel;
		primeURL = url;
		this.chooserStyle = chooserStyle;
	}
	
	
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(title);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createDescriptionArea(composite);
		return composite;
	}		
		
	
	private void createDescriptionArea(Composite parent) {		
		Font font = parent.getFont();
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		Label label = new Label(composite, SWT.NONE);
		label.setText("File URL");
		label.setFont(font);
		GridData gd0 = new GridData(GridData.FILL_HORIZONTAL);
		gd0.widthHint = convertHorizontalDLUsToPixels(300);
		gd0.horizontalSpan = 2;
		label.setLayoutData(gd0);
		
        chooser = new URLChooser(composite, supportedProviderIds, tempFSProviders, chooserStyle);
        if (filter != null)
            chooser.setFilters(filter);
        
        // This is a passed in URL, if it is not null, use it to set the initial value of the chooser
        if (primeURL != null)
        	chooser.setSelectedValue(primeURL);
        
        chooser.setNewCustomizationProvider(new URLChooser.NewCustomizationProvider() {
            public Map getCustomizationMap() {
                try {
                    IEditorInput ei = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor().getEditorInput();
                    return URLChooser.getDefaultCustomizationMap(((IFile)ei.getAdapter(IFile.class)));
                }
                catch (Exception e) {
                }
                return null;
            }
        });
        
		chooser.getTextControl().addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				Button okButton = getButton(Dialog.OK);
				URL fileUrl = getURL();
				if (fileUrl != null) {
					File file = new File(fileUrl.getFile());
					if (file.exists()&& file.isFile()) {
						if (!(okButton.isEnabled()))
							okButton.setEnabled(true);
					} else {
						if (okButton.isEnabled())
							okButton.setEnabled(false);
					}
				}
			}
		});
		
        
        chooser.setBrowseButtonText("...");      
        Composite ui = chooser.getUI();
        GridData data = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
        ui.setLayoutData(data);
	}

	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		if (buttonLabel == null || buttonLabel.length < 2) {
			Button button = super
					.createButton(parent, id, label, defaultButton);
			if (id == Dialog.OK)
				button.setEnabled(false);
			return button;
		}
		
		if	(id == IDialogConstants.OK_ID)
			return super.createButton(parent, id, buttonLabel[0], defaultButton);
		if	(id == IDialogConstants.CANCEL_ID)
			return super.createButton(parent, id, buttonLabel[1], defaultButton);
		
		return super.createButton(parent, id, label, defaultButton);
	}
	
	public URL getURL() {		
		return chooser.getSelectedValueAsURL();
	}
	
}