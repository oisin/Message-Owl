/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.fusesource.tools.core.message.impl;

import java.io.File;
import java.io.IOException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap.Entry;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.fusesource.tools.core.message.Body;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.core.message.util.ReaderUtils;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Body</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.fusesource.tools.core.message.impl.BodyImpl#getContent <em>Content</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.impl.BodyImpl#getFileRef <em>File Ref</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.impl.BodyImpl#isUseFileRef <em>Use File Ref</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class BodyImpl extends EObjectImpl implements Body {
    /**
     * The cached value of the '{@link #getContent() <em>Content</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getContent()
     * @generated
     * @ordered
     */
    protected EObject content;

    /**
     * The default value of the '{@link #getFileRef() <em>File Ref</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getFileRef()
     * @generated
     * @ordered
     */
    protected static final String FILE_REF_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getFileRef() <em>File Ref</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getFileRef()
     * @generated
     * @ordered
     */
    protected String fileRef = FILE_REF_EDEFAULT;

    /**
     * The default value of the '{@link #isUseFileRef() <em>Use File Ref</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isUseFileRef()
     * @generated
     * @ordered
     */
    protected static final boolean USE_FILE_REF_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isUseFileRef() <em>Use File Ref</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isUseFileRef()
     * @generated
     * @ordered
     */
    protected boolean useFileRef = USE_FILE_REF_EDEFAULT;

    /**
     * This is true if the Use File Ref attribute has been set. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean useFileRefESet;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected BodyImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MessagePackage.Literals.BODY;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EObject getContent() {
        return content;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetContent(EObject newContent, NotificationChain msgs) {
        EObject oldContent = content;
        content = newContent;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    MessagePackage.BODY__CONTENT, oldContent, newContent);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setContent(EObject newContent) {
        if (newContent != content) {
            NotificationChain msgs = null;
            if (content != null) {
                msgs = ((InternalEObject) content).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - MessagePackage.BODY__CONTENT, null, msgs);
            }
            if (newContent != null) {
                msgs = ((InternalEObject) newContent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - MessagePackage.BODY__CONTENT, null, msgs);
            }
            msgs = basicSetContent(newContent, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.BODY__CONTENT, newContent, newContent));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getFileRef() {
        return fileRef;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setFileRef(String newFileRef) {
        String oldFileRef = fileRef;
        fileRef = newFileRef;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.BODY__FILE_REF, oldFileRef, fileRef));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isUseFileRef() {
        return useFileRef;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setUseFileRef(boolean newUseFileRef) {
        boolean oldUseFileRef = useFileRef;
        useFileRef = newUseFileRef;
        boolean oldUseFileRefESet = useFileRefESet;
        useFileRefESet = true;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.BODY__USE_FILE_REF, oldUseFileRef,
                    useFileRef, !oldUseFileRefESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetUseFileRef() {
        boolean oldUseFileRef = useFileRef;
        boolean oldUseFileRefESet = useFileRefESet;
        useFileRef = USE_FILE_REF_EDEFAULT;
        useFileRefESet = false;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.UNSET, MessagePackage.BODY__USE_FILE_REF, oldUseFileRef,
                    USE_FILE_REF_EDEFAULT, oldUseFileRefESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetUseFileRef() {
        return useFileRefESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case MessagePackage.BODY__CONTENT:
                return basicSetContent(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case MessagePackage.BODY__CONTENT:
                return getContent();
            case MessagePackage.BODY__FILE_REF:
                return getFileRef();
            case MessagePackage.BODY__USE_FILE_REF:
                return isUseFileRef() ? Boolean.TRUE : Boolean.FALSE;
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case MessagePackage.BODY__CONTENT:
                setContent((EObject) newValue);
                return;
            case MessagePackage.BODY__FILE_REF:
                setFileRef((String) newValue);
                return;
            case MessagePackage.BODY__USE_FILE_REF:
                setUseFileRef(((Boolean) newValue).booleanValue());
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case MessagePackage.BODY__CONTENT:
                setContent((EObject) null);
                return;
            case MessagePackage.BODY__FILE_REF:
                setFileRef(FILE_REF_EDEFAULT);
                return;
            case MessagePackage.BODY__USE_FILE_REF:
                unsetUseFileRef();
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case MessagePackage.BODY__CONTENT:
                return content != null;
            case MessagePackage.BODY__FILE_REF:
                return FILE_REF_EDEFAULT == null ? fileRef != null : !FILE_REF_EDEFAULT.equals(fileRef);
            case MessagePackage.BODY__USE_FILE_REF:
                return isSetUseFileRef();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) {
            return super.toString();
        }

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (fileRef: ");
        result.append(fileRef);
        result.append(", useFileRef: ");
        if (useFileRefESet) {
            result.append(useFileRef);
        } else {
            result.append("<unset>");
        }
        result.append(')');
        return result.toString();
    }

    /**
     * Returns the message content based on useFileRef attribute
     */
    public String getMessageContent() {
        if (isUseFileRef()) {
            try {
                if (getFileRef() != null) {
                    File file = new File(getFileRef());
                    if (file != null && file.exists()) {
                        return ReaderUtils.getContentAsString(file);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                // TODO handle exception
            }
        } else {
            if (content instanceof AnyTypeImpl) {
                AnyTypeImpl anyTypeImpl = (AnyTypeImpl) content;
                FeatureMap mixed = anyTypeImpl.getMixed();
                for (Entry entry : mixed) {
                    if (entry.getValue() instanceof String) {
                        return (String) entry.getValue();
                    } else if (entry.getValue() == null) {
                        return "";
                    }
                }
            }
        }
        return "";
    }
} // BodyImpl
