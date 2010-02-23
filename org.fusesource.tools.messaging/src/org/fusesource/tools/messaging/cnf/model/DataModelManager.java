/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.cnf.model;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.ISender;

/**
 * Handles all the changes in the model file
 */
public class DataModelManager {
    private final Map<IFile, Object> cachedModelMap;

    private static DataModelManager modelManager;

    private DataModelManager() {
        cachedModelMap = new HashMap<IFile, Object>();
    }

    public static DataModelManager getInstance() {
        if (modelManager == null) {
            modelManager = new DataModelManager();
        }
        return modelManager;
    }

    public void addDestination(IFile file, ISender sender) {
        Senders senders = (Senders) getModel(file);
        if (senders == null) {
            senders = new Senders(file);
            addModel(file, senders);
        }
        senders.addSender(sender);
        saveSenders(senders);
    }

    public void addDestination(IFile file, IListener listener) {
        Listeners listeners = (Listeners) getModel(file);
        if (listeners == null) {
            listeners = new Listeners(file);
            addModel(file, listeners);
        }
        listeners.addListener(listener);
        saveListeners(listeners);
    }

    public void removeDestination(IFile file, ISender sender) {
        Senders senders = (Senders) getModel(file);
        if (senders != null) {
            senders.removeSender(sender);
            saveSenders(senders);
        }
    }

    public void removeDestination(IFile file, IListener listener) {
        Listeners listeners = (Listeners) getModel(file);
        if (listeners != null) {
            listeners.removeListener(listener);
            saveListeners(listeners);
        }
    }

    public void saveSenders(Senders senders) {
        save(senders, senders.getModelFile());
    }

    public void saveListeners(Listeners listeners) {
        save(listeners, listeners.getModelFile());
    }

    public void saveListeners(IFile modelFile) {
        saveListeners((Listeners) cachedModelMap.get(modelFile));
    }

    /**
     * writes the object to disk
     * 
     * @param senderList
     * @param file
     */
    private synchronized void save(final Object list, final IFile file) {
        ObjectOutputStream obj = null;
        try {
            FileOutputStream fileOutputStream = null;
            fileOutputStream = new FileOutputStream(file.getLocation().toOSString());
            obj = new ObjectOutputStream(fileOutputStream);
            obj.writeObject(list);
            obj.flush();
            file.getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        } finally {
            if (obj != null) {
                try {
                    obj.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Senders loadSenders(IFile modelFile) throws Exception {
        createFileIfRequired(modelFile);
        FileInputStream inputStream = null;
        ObjectInputStream objInputStream = null;
        Senders senderList = null;
        try {
            inputStream = new FileInputStream(modelFile.getLocation().toOSString());
            if (inputStream.available() == 0) {
                return null;
            }
            objInputStream = new ObjectInputStream(inputStream);
            senderList = (Senders) objInputStream.readObject();
            // Set the model File, since we did not serialize this member
            senderList.setModelFile(modelFile);
        } finally {
            if (objInputStream != null) {
                objInputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }

        return senderList;
    }

    public Listeners loadListeners(IFile modelFile) throws Exception {
        createFileIfRequired(modelFile);
        FileInputStream inputStream = null;
        ObjectInputStream objInputStream = null;
        Listeners listenerList = null;
        try {
            inputStream = new FileInputStream(modelFile.getLocation().toOSString());
            if (inputStream.available() == 0) {
                return null;
            }
            objInputStream = new ObjectInputStream(inputStream);
            listenerList = (Listeners) objInputStream.readObject();
            // Set the model File, since we did not serialize this member
            listenerList.setModelFile(modelFile);
        } finally {
            if (objInputStream != null) {
                objInputStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return listenerList;
    }

    private void createFileIfRequired(IFile modelFile) throws CoreException {
        if (!modelFile.exists()) {
            modelFile.create(new ByteArrayInputStream(new byte[] {}), true, null);
        }
    }

    /**
     * Load the model from the given file, if possible.
     * 
     * @param modelFile
     *            The IFile which contains the persisted model
     * @throws CoreException
     */
    private void updateModel(IFile modelFile) {
        Object object = null;
        try {
            createFileIfRequired(modelFile);
            if (IModelConstants.SENDERS_EXT.equals(modelFile.getFileExtension())) {
                object = loadSenders(modelFile);
            } else if (IModelConstants.LISTENERS_EXT.equals(modelFile.getFileExtension())) {
                object = loadListeners(modelFile);
            }
            if (object != null) {
                addModel(modelFile, object);
            }
        } catch (IOException e) {
            e.printStackTrace();// TODO log to problems view
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        cachedModelMap.clear();
    }

    private void addModel(IFile file, Object object) {
        cachedModelMap.put(file, object);
    }

    public Object getModel(IFile file) {
        if (cachedModelMap.get(file) == null) {
            updateModel(file);
        }
        return cachedModelMap.get(file);
    }
}
