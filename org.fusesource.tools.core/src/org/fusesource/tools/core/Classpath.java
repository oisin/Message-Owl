package org.fusesource.tools.core;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathContainer;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class Classpath {
	IJavaProject jp;

	public Classpath(IProject project) {
		jp = JavaCore.create(project);
		if(jp == null)
			throw new IllegalArgumentException(project.getName() + "is not a java project");
	}

	public Collection<IPath> getLibs() {
		return getPaths(IClasspathEntry.CPE_LIBRARY);
	}

	public Collection<IPath> getSrc() {
		return getPaths(IClasspathEntry.CPE_SOURCE);
	}

	public IPath getOutputPath() {
		IPath outPath = null;

		try {
			outPath = jp.getOutputLocation();
			String prjName = jp.getProject().getName();
			IPath path = jp.getProject().getLocation();

			if (outPath.segment(0).startsWith(prjName)) {
				return path.append(outPath.removeFirstSegments(1));
			}
		} catch (JavaModelException e) {
		}

		return outPath;
	}

	public Collection<IPath> getContainers() {
		Collection<IPath> names = new ArrayList<IPath>();
		Collection<IClasspathEntry> entries = getEntries(IClasspathEntry.CPE_CONTAINER);
		for (IClasspathEntry entry : entries) {
			try {
				IClasspathContainer c = JavaCore.getClasspathContainer(entry
						.getPath(), jp);
				if(c.getKind() != IClasspathContainer.K_DEFAULT_SYSTEM){
					names.add(entry.getPath());	
				}
			} catch (JavaModelException e) {
				System.out.println(e.getMessage());
			}
		}
		return names;
	}

	public Collection<IPath> getContainerPaths(IPath containerPath) {
		Collection<IPath> paths = new ArrayList<IPath>();
		IClasspathContainer container = getContainer(containerPath);
		if (container != null) {
			IClasspathEntry[] entries = container.getClasspathEntries();
			for (IClasspathEntry entry : entries) {
				paths.add(getAbsolutePath(entry.getPath()));
			}
		}
		return paths;
	}

	protected IClasspathContainer getContainer(IPath containerPath) {
		Collection<IClasspathEntry> entries = getEntries(IClasspathEntry.CPE_CONTAINER);
		for (IClasspathEntry entry : entries) {
			try {
				if (containerPath.equals(entry.getPath())) {
					IClasspathContainer c = JavaCore.getClasspathContainer(entry .getPath(), jp);
					return c;
				}
			} catch (JavaModelException e) {
				System.out.println(e.getMessage());
			}
		}
		return null;

	}

	private Collection<IClasspathEntry> getEntries(int kind) {
		Collection<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		try {
			IClasspathEntry[] entriesArray = jp.getRawClasspath();
			for (IClasspathEntry e : entriesArray) {
				if (e.getEntryKind() == kind) {
					entries.add(e);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return entries;
	}

	private Collection<IPath> getPaths(int kind) {
		Collection<IPath> paths = new ArrayList<IPath>();

		Collection<IClasspathEntry> entries = getEntries(kind);
		for (IClasspathEntry e : entries) {
			paths.add(getAbsolutePath(e.getPath()));
		}
		return paths;
	}
	
	private IPath getAbsolutePath(IPath relativePath){
		String prjName = jp.getProject().getName();
		if(relativePath.segment(0).startsWith(prjName)){
			IPath retPath = jp.getProject().getLocation();
			return retPath.removeLastSegments(1).append(relativePath);
		}
		return relativePath;
	}
}
