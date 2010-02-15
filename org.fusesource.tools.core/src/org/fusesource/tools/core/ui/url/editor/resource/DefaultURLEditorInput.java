package org.fusesource.tools.core.ui.url.editor.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.PlatformUI;

/*
 */

public class DefaultURLEditorInput implements URLEditorInput {

	private URL url;

	public DefaultURLEditorInput(URL url) {
		if (url == null) {
			throw new IllegalArgumentException();
		}
		this.url = url;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof URLEditorInput))
			return false;
		URLEditorInput other = (URLEditorInput) obj;
		return url.equals(other.getURL());
	}

	public int hashCode() {
		return url.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.sonicsw.tools.plugin.editor.resource.URLEditorInput#getURL()
	 */
	public URL getURL() {
		return url;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
	public boolean exists() {
		// return url.exists();
		// TODO
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
	public ImageDescriptor getImageDescriptor() {
		return PlatformUI.getWorkbench().getEditorRegistry()
				.getImageDescriptor(url.getFile());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
	public String getName() {
		return url.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
	public IPersistableElement getPersistable() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
	public String getToolTipText() {
		return url.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter) {
		if (adapter == URL.class)
			return url;
		return null;
	}

	public IStorage getStorage() throws CoreException {
		return new IStorage() {
			public InputStream getContents() throws CoreException {
				try {
					return url.openStream();
				} catch (IOException e) {
					throw new CoreException(new Status(IStatus.ERROR, "", 0,
							"Could not read URL: " + url, e));
				}
			}

			public IPath getFullPath() {
				return new Path(url.toString());
			}

			public String getName() {
				return url.getPath();
			}

			public boolean isReadOnly() {
				return true;
			}

			public Object getAdapter(Class aClass) {
				return DefaultURLEditorInput.this.getAdapter(aClass);
			}
		};
	}
}
