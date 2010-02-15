// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.message.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.ISetSelectionTarget;
import org.fusesource.tools.core.message.DocumentRoot;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessageFactory;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.message.MessageConstants;
import org.fusesource.tools.message.extensions.IMessageType;
import org.fusesource.tools.message.provider.MessageEditPlugin;
import org.fusesource.tools.message.utils.EMFUtil;
import org.fusesource.tools.message.utils.MessageExtensionsMgr;


/**
 * This is a simple wizard for creating a new model file. <!-- begin-user-doc
 * --> <!-- end-user-doc -->
 * 
 * @author sgupta
 * @generated
 */
public class MessageFileWizard extends Wizard implements INewWizard {
	/**
	 * The supported extensions for created files. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public static final List<String> FILE_EXTENSIONS = Collections.unmodifiableList(Arrays
			.asList(MessageEditorPlugin.INSTANCE.getString("_UI_MessageEditorFilenameExtensions").split("\\s*,\\s*")));

	/**
	 * A formatted list of supported file extensions, suitable for display. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static final String FORMATTED_FILE_EXTENSIONS = MessageEditorPlugin.INSTANCE.getString(
			"_UI_MessageEditorFilenameExtensions").replaceAll("\\s*,\\s*", ", ");

	/**
	 * This caches an instance of the model package. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MessagePackage messagePackage = MessagePackage.eINSTANCE;

	/**
	 * This caches an instance of the model factory. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MessageFactory messageFactory = messagePackage.getMessageFactory();

	/**
	 * This is the file creation page. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	protected MessageModelWizardNewFileCreationPage newFileCreationPage;

	/**
	 * This is the initial object creation page. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	protected MessageModelWizardInitialObjectCreationPage initialObjectCreationPage;

	/**
	 * Remember the selection during initialization for populating the default
	 * container. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IStructuredSelection selection;

	/**
	 * Remember the workbench during initialization. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected IWorkbench workbench;

	/**
	 * Caches the names of the features representing global elements. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected List<String> initialObjectNames;

	protected String selectedType = null;

	/**
	 * This just records the information. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.workbench = workbench;
		this.selection = selection;
		setWindowTitle(MessageEditorPlugin.INSTANCE.getString("_UI_Wizard_label"));
		setDefaultPageImageDescriptor(ExtendedImageRegistry.INSTANCE.getImageDescriptor(MessageEditorPlugin.INSTANCE
				.getImage("full/wizban/NewMessage")));
	}

	/**
	 * Returns the names of the features representing global elements. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Collection<String> getInitialObjectNames() {
		if (initialObjectNames == null) {
			initialObjectNames = new ArrayList<String>();
			for (EStructuralFeature eStructuralFeature : ExtendedMetaData.INSTANCE
					.getAllElements(ExtendedMetaData.INSTANCE.getDocumentRoot(messagePackage))) {
				if (eStructuralFeature.isChangeable()) {
					EClassifier eClassifier = eStructuralFeature.getEType();
					if (eClassifier instanceof EClass) {
						EClass eClass = (EClass) eClassifier;
						if (!eClass.isAbstract()) {
							initialObjectNames.add(eStructuralFeature.getName());
						}
					}
				}
			}
			Collections.sort(initialObjectNames, CommonPlugin.INSTANCE.getComparator());
		}
		return initialObjectNames;
	}

	/**
	 * Do the work after everything is specified. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean performFinish() {
		try {
			// Remember the file.
			//
			final IFile modelFile = getModelFile();

			// Do the work within an operation.
			//
			WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
				@Override
				protected void execute(IProgressMonitor progressMonitor) {
					try {
						saveMessageToDisk(modelFile);
					} catch (Exception exception) {
						exception.printStackTrace();
						MessageEditorPlugin.INSTANCE.log(exception);
					} finally {
						progressMonitor.done();
					}
				}

				private void saveMessageToDisk(final IFile modelFile) throws IOException {
					ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(
							ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
					BasicCommandStack commandStack = new BasicCommandStack();
					AdapterFactoryEditingDomain editingDomain = new AdapterFactoryEditingDomain(adapterFactory,
							commandStack, new HashMap<Resource, Boolean>());

					// Create a resource set
					ResourceSet resourceSet = new ResourceSetImpl();

					// Get the URI of the model file.
					URI fileURI = URI.createPlatformResourceURI(modelFile.getFullPath().toString(), true);

					// Create a resource for this file.
					Resource resource = resourceSet.createResource(fileURI);

					// Add the initial model object to the contents.
					EObject rootObject = EMFUtil.createInitialModel();
					if (rootObject != null) {
						resource.getContents().add(rootObject);
					}
					EList<EObject> contents = resource.getContents();
					if (contents != null && contents.size() > 0) {
						DocumentRoot object = (DocumentRoot) contents.get(0);
						Message messageModel = object.getMessage();

						ArrayList<Command> arrayList = loadMessageTypeAttributes(messageModel, editingDomain);
						CommandParameter commandParameter = new CommandParameter(messageModel, messagePackage
								.getMessage_Properties(), messageFactory.createProperties(), new StructuredSelection(
								messageModel).toList());
						arrayList.add(CreateChildCommand.create(editingDomain, messageModel, commandParameter,
								new StructuredSelection(messageModel).toList()));
						CompoundCommand command = new CompoundCommand(arrayList);
						editingDomain.getCommandStack().execute(command);
						saveProperties(selectedType, messageModel, editingDomain);

						if (!EMFUtil.getMessageType(selectedType).equalsIgnoreCase(MessageConstants.MESSAGE_TYPE)) {
							CommandParameter cmdParameter = new CommandParameter(messageModel, messagePackage
									.getMessage_Body(), messageFactory.createBody(), new StructuredSelection(
									messageModel).toList());
							Command cmd = CreateChildCommand.create(editingDomain, messageModel, cmdParameter,
									new StructuredSelection(messageModel).toList());
							editingDomain.getCommandStack().execute(cmd);
						}
					}
					// Save the contents of the resource to the file system.
					//
					Map<Object, Object> options = new HashMap<Object, Object>();
					options.put(XMLResource.OPTION_ENCODING, initialObjectCreationPage.getEncoding());
					resource.save(options);
				}

			};

			getContainer().run(false, false, operation);

			// Select the new file resource in the current view.
			//
			IWorkbenchWindow workbenchWindow = workbench.getActiveWorkbenchWindow();
			IWorkbenchPage page = workbenchWindow.getActivePage();
			final IWorkbenchPart activePart = page.getActivePart();
			if (activePart instanceof ISetSelectionTarget) {
				final ISelection targetSelection = new StructuredSelection(modelFile);
				getShell().getDisplay().asyncExec(new Runnable() {
					public void run() {
						((ISetSelectionTarget) activePart).selectReveal(targetSelection);
					}
				});
			}

			// Open an editor on the new file.
			//
			try {
				page.openEditor(new FileEditorInput(modelFile), workbench.getEditorRegistry().getDefaultEditor(
						modelFile.getFullPath().toString()).getId());
			} catch (PartInitException exception) {
				MessageDialog.openError(workbenchWindow.getShell(), MessageEditorPlugin.INSTANCE
						.getString("_UI_OpenEditorError_label"), exception.getMessage());
				return false;
			}

			return true;
		} catch (Exception exception) {
			MessageEditorPlugin.INSTANCE.log(exception);
			return false;
		}
	}

	/**
	 * This is the one page of the wizard. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @generated
	 */
	public class MessageModelWizardNewFileCreationPage extends WizardNewFileCreationPage {
		/**
		 * Pass in the selection. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public MessageModelWizardNewFileCreationPage(String pageId, IStructuredSelection selection) {
			super(pageId, selection);
		}

		/**
		 * The framework calls this to see if the file is correct. <!--
		 * begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		protected boolean validatePage() {
			if (super.validatePage()) {
				String extension = new Path(getFileName()).getFileExtension();
				if (extension == null || !FILE_EXTENSIONS.contains(extension)) {
					String key = FILE_EXTENSIONS.size() > 1 ? "_WARN_FilenameExtensions" : "_WARN_FilenameExtension";
					setErrorMessage(MessageEditorPlugin.INSTANCE.getString(key,
							new Object[] { FORMATTED_FILE_EXTENSIONS }));
					return false;
				}
				return true;
			}
			return false;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public IFile getModelFile() {
			return ResourcesPlugin.getWorkspace().getRoot().getFile(getContainerFullPath().append(getFileName()));
		}
	}

	/**
	 * This is the page where the type of object to create is selected. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public class MessageModelWizardInitialObjectCreationPage extends WizardPage {
		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected Combo initialObjectField;

		/**
		 * @generated <!-- begin-user-doc --> <!-- end-user-doc -->
		 */
		protected List<String> encodings;

		/**
		 * Pass in the selection. <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public MessageModelWizardInitialObjectCreationPage(String pageId) {
			super(pageId);
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NONE);
			{
				GridLayout layout = new GridLayout();
				layout.numColumns = 1;
				layout.verticalSpacing = 12;
				composite.setLayout(layout);

				GridData data = new GridData();
				data.verticalAlignment = GridData.FILL;
				data.grabExcessVerticalSpace = true;
				data.horizontalAlignment = GridData.FILL;
				composite.setLayoutData(data);
			}

			Label containerLabel = new Label(composite, SWT.LEFT);
			{
				// containerLabel.setText(MessageEditorPlugin.INSTANCE.getString("_UI_ModelObject"));
				containerLabel.setText("Message Type: ");
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				containerLabel.setLayoutData(data);
			}

			initialObjectField = new Combo(composite, SWT.BORDER | SWT.READ_ONLY);
			{
				GridData data = new GridData();
				data.horizontalAlignment = GridData.FILL;
				data.grabExcessHorizontalSpace = true;
				initialObjectField.setLayoutData(data);
			}

			for (String objectName : EMFUtil.getDisplayMessageTypes()) {
				initialObjectField.add(objectName);
			}

			setDefaultMessageType();
			initialObjectField.addModifyListener(validator);
			setPageComplete(validatePage());
			setControl(composite);
		}

		protected void setDefaultMessageType() {
			String[] items = initialObjectField.getItems();
			int defaultIndex = items.length > 0 ? 0 : -1;
			for (int i = 0; i < items.length; i++) {
				String type = EMFUtil.getMessageType(items[i]);
				String provider = EMFUtil.getMessageProvider(items[i]);
				if (MessageConstants.TEXT_MESSAGE_TYPE.equals(type)
						&& MessageConstants.DEFAULT_PROVIDER.equals(provider)) {
					defaultIndex = i;
					break;
				}
			}
			if (defaultIndex > -1)
				initialObjectField.select(defaultIndex);
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected ModifyListener validator = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				setPageComplete(validatePage());
			}
		};

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected boolean validatePage() {
			return getInitialObjectName() != null ;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		@Override
		public void setVisible(boolean visible) {
			super.setVisible(visible);
			if (visible) {
				if (initialObjectField.getItemCount() == 1) {
					initialObjectField.clearSelection();
				} else {
					initialObjectField.setFocus();
				}
			}
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public String getInitialObjectName() {
			String label = initialObjectField.getText();

			for (String name : EMFUtil.getDisplayMessageTypes()) {
				if (name.equals(label)) {
					selectedType = name;
					return name;
				}
			}
			return null;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		public String getEncoding() {
			return MessageConstants.DEFAULT_ENCODING;
		}

		/**
		 * Returns the label for the specified feature name. <!-- begin-user-doc
		 * --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected String getLabel(String featureName) {
			try {
				return MessageEditPlugin.INSTANCE.getString("_UI_DocumentRoot_" + featureName + "_feature");
			} catch (MissingResourceException mre) {
				MessageEditorPlugin.INSTANCE.log(mre);
			}
			return featureName;
		}

		/**
		 * <!-- begin-user-doc --> <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		protected Collection<String> getEncodings() {
			if (encodings == null) {
				encodings = new ArrayList<String>();
				for (StringTokenizer stringTokenizer = new StringTokenizer(MessageEditorPlugin.INSTANCE
						.getString("_UI_XMLEncodingChoices")); stringTokenizer.hasMoreTokens();) {
					encodings.add(stringTokenizer.nextToken());
				}
			}
			return encodings;
		}
	}

	/**
	 * The framework calls this to create the contents of the wizard. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void addPages() {
		// Create a page, set the title, and the initial model file name.
		//
		newFileCreationPage = new MessageModelWizardNewFileCreationPage("Whatever", selection);
		newFileCreationPage.setTitle(MessageEditorPlugin.INSTANCE.getString("_UI_MessageModelWizard_label"));
		newFileCreationPage
				.setDescription(MessageEditorPlugin.INSTANCE.getString("_UI_MessageModelWizard_description"));
		newFileCreationPage.setFileName(MessageEditorPlugin.INSTANCE.getString("_UI_MessageEditorFilenameDefaultBase")
				+ "." + FILE_EXTENSIONS.get(0));
		addPage(newFileCreationPage);

		// Try and get the resource selection to determine a current directory
		// for the file dialog.
		//
		if (selection != null && !selection.isEmpty()) {
			// Get the resource...
			//
			Object selectedElement = selection.iterator().next();
			if (selectedElement instanceof IResource) {
				// Get the resource parent, if its a file.
				//
				IResource selectedResource = (IResource) selectedElement;
				if (selectedResource.getType() == IResource.FILE) {
					selectedResource = selectedResource.getParent();
				}

				// This gives us a directory...
				//
				if (selectedResource instanceof IFolder || selectedResource instanceof IProject) {
					// Set this for the container.
					//
					newFileCreationPage.setContainerFullPath(selectedResource.getFullPath());

					// Make up a unique new name here.
					//
					String defaultModelBaseFilename = MessageEditorPlugin.INSTANCE
							.getString("_UI_MessageEditorFilenameDefaultBase");
					String defaultModelFilenameExtension = FILE_EXTENSIONS.get(0);
					String modelFilename = defaultModelBaseFilename + "." + defaultModelFilenameExtension;
					for (int i = 1; ((IContainer) selectedResource).findMember(modelFilename) != null; ++i) {
						modelFilename = defaultModelBaseFilename + i + "." + defaultModelFilenameExtension;
					}
					newFileCreationPage.setFileName(modelFilename);
				}
			}
		}
		initialObjectCreationPage = new MessageModelWizardInitialObjectCreationPage("PageName");
		initialObjectCreationPage.setTitle(MessageEditorPlugin.INSTANCE.getString("_UI_MessageModelWizard_label"));
		initialObjectCreationPage.setDescription(MessageEditorPlugin.INSTANCE
				.getString("_UI_Wizard_initial_object_description"));
		addPage(initialObjectCreationPage);
	}

	/**
	 * Get the file from the page. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IFile getModelFile() {
		return newFileCreationPage.getModelFile();
	}

	private void saveProperties(String displayType, Message messageModel, AdapterFactoryEditingDomain editingDomain) {
		String messageType = EMFUtil.getMessageType(displayType);
		String providerId = EMFUtil.getMessageProvider(displayType);
		try {

			IMessageType messageTypeExt = MessageExtensionsMgr.getInstance().getMessageTypeExtension(
					messageType, providerId);
			if (messageTypeExt != null) {
				Map<String, String> defaultHeaders = messageTypeExt.getHeaders();
				EMFUtil.createHeaderInModel(defaultHeaders, messageModel, editingDomain);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private ArrayList<Command> loadMessageTypeAttributes(Message messageModel, AdapterFactoryEditingDomain editingDomain) {
		ArrayList<Command> arrayList = new ArrayList<Command>();
		arrayList.add(SetCommand.create(editingDomain, messageModel, messagePackage.getMessage_ProviderId(), EMFUtil
				.getMessageProvider(selectedType)));
		arrayList.add(SetCommand.create(editingDomain, messageModel, messagePackage.getMessage_ProviderName(), EMFUtil
				.getMessageProvider(selectedType)));
		arrayList.add(SetCommand.create(editingDomain, messageModel, messagePackage.getMessage_Type(), EMFUtil
				.getMessageType(selectedType)));
		return arrayList;
	}
}
