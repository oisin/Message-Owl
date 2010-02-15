// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.core.ui;

import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextSourceViewerConfiguration;
import org.eclipse.wst.sse.core.StructuredModelManager;
import org.eclipse.wst.sse.ui.StructuredTextViewerConfiguration;
import org.eclipse.wst.sse.ui.internal.StructuredTextViewer;
import org.eclipse.wst.sse.ui.internal.provisional.style.LineStyleProvider;
import org.eclipse.wst.xml.core.internal.provisional.contenttype.ContentTypeIdForXML;
import org.eclipse.wst.xml.ui.StructuredTextViewerConfigurationXML;
import org.fusesource.tools.core.ui.viewer.actions.SourceViewerContextMenuProvider;


@SuppressWarnings("restriction")
public class TextViewerComponent {

	public static SourceViewer createTextViewer(Composite parent, int style) {
		return createTextViewer(parent, null, style);
	}

	public static StructuredTextViewer createXMLViewer(Composite parent, int style) {
		return createXMLViewer(parent, null, style);
	}

	public static SourceViewer createSourceViewer(Composite composite, Object data, int style) {

		SourceViewer sourceViewer = new SourceViewer(composite, null, null, false, style);
		sourceViewer.setEditable(false);
		sourceViewer.setDocument(new Document(""));
		sourceViewer.configure(new TextSourceViewerConfiguration());
		sourceViewer.getTextWidget().addFocusListener(new FocusListener() {
			private IContextActivation context;

			public void focusGained(FocusEvent e) {
				IContextService contextService = (IContextService) PlatformUI.getWorkbench().getService(
						IContextService.class);
				context = contextService.activateContext("org.eclipse.ui.textEditorScope");
			}

			public void focusLost(FocusEvent e) {
				IContextService contextService = (IContextService) PlatformUI.getWorkbench().getService(
						IContextService.class);
				contextService.deactivateContext(context);
			}

		});
		sourceViewer.getDocument().set(data.toString());
		sourceViewer.setEditable(true);
		addContextMenu(sourceViewer);
		return sourceViewer;
	}

	protected static void addContextMenu(SourceViewer sourceViewer) {
		new SourceViewerContextMenuProvider(sourceViewer);
	}

	public static SourceViewer createTextViewer(Composite parent, Object data, int style) {
		data = getData(data);
		return createSourceViewer(parent, data, style);
	}

	public static StructuredTextViewer createXMLViewer(Composite parent, Object data, int style) {
		data = getData(data);
		SourceViewerConfiguration sourceViewerConfiguration = new StructuredTextViewerConfiguration() {
			StructuredTextViewerConfiguration baseConfiguration = new StructuredTextViewerConfigurationXML();

			public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
				return baseConfiguration.getConfiguredContentTypes(sourceViewer);
			}

			public LineStyleProvider[] getLineStyleProviders(ISourceViewer sourceViewer, String partitionType) {
				return baseConfiguration.getLineStyleProviders(sourceViewer, partitionType);
			}
		};
		StructuredTextViewer structuredViewer = new StructuredTextViewer(parent, null, null, false, style);
		((StructuredTextViewer) structuredViewer).getTextWidget().setFont(
				JFaceResources.getFont("org.eclipse.wst.sse.ui.textfont"));
		org.eclipse.wst.sse.core.internal.provisional.IStructuredModel scratchModel = StructuredModelManager
				.getModelManager().createUnManagedStructuredModelFor(ContentTypeIdForXML.ContentTypeID_XML);
		IDocument document = scratchModel.getStructuredDocument();
		structuredViewer.configure(sourceViewerConfiguration);
		structuredViewer.setDocument(document);
		structuredViewer.getDocument().set(data.toString());
		new SourceViewerContextMenuProvider(structuredViewer);
		return structuredViewer;
	}

	private static Object getData(Object data) {
		if (data == null) {
			data = "";
		}
		if (data instanceof AnyType) {
			data = getData((AnyType) data);
		}
		
		data = ((String)data).trim();
		return data;
	}

	static String getData(AnyType object) {
		FeatureMap mixed = object.getMixed();
		for (Entry entry : mixed) {
			if (entry.getValue() instanceof String) {
				return ((String) entry.getValue()).trim();
			}
		}
		return "";
	}
}
