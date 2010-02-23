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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.fusesource.tools.core.message.Body;
import org.fusesource.tools.core.message.Message;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.core.message.Properties;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Message</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.fusesource.tools.core.message.impl.MessageImpl#getProperties <em>Properties</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.impl.MessageImpl#getBody <em>Body</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.impl.MessageImpl#getProviderId <em>Provider Id</em>}
 * </li>
 * <li>{@link org.fusesource.tools.core.message.impl.MessageImpl#getProviderName <em>Provider Name
 * </em>}</li>
 * <li>{@link org.fusesource.tools.core.message.impl.MessageImpl#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class MessageImpl extends EObjectImpl implements Message {
    /**
     * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getProperties()
     * @generated
     * @ordered
     */
    protected Properties properties;

    /**
     * The cached value of the '{@link #getBody() <em>Body</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getBody()
     * @generated
     * @ordered
     */
    protected Body body;

    /**
     * The default value of the '{@link #getProviderId() <em>Provider Id</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getProviderId()
     * @generated
     * @ordered
     */
    protected static final String PROVIDER_ID_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProviderId() <em>Provider Id</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getProviderId()
     * @generated
     * @ordered
     */
    protected String providerId = PROVIDER_ID_EDEFAULT;

    /**
     * The default value of the '{@link #getProviderName() <em>Provider Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getProviderName()
     * @generated
     * @ordered
     */
    protected static final String PROVIDER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getProviderName() <em>Provider Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getProviderName()
     * @generated
     * @ordered
     */
    protected String providerName = PROVIDER_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getType()
     * @generated
     * @ordered
     */
    protected static final String TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getType() <em>Type</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getType()
     * @generated
     * @ordered
     */
    protected String type = TYPE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected MessageImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MessagePackage.Literals.MESSAGE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetProperties(Properties newProperties, NotificationChain msgs) {
        Properties oldProperties = properties;
        properties = newProperties;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    MessagePackage.MESSAGE__PROPERTIES, oldProperties, newProperties);
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
    public void setProperties(Properties newProperties) {
        if (newProperties != properties) {
            NotificationChain msgs = null;
            if (properties != null) {
                msgs = ((InternalEObject) properties).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - MessagePackage.MESSAGE__PROPERTIES, null, msgs);
            }
            if (newProperties != null) {
                msgs = ((InternalEObject) newProperties).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - MessagePackage.MESSAGE__PROPERTIES, null, msgs);
            }
            msgs = basicSetProperties(newProperties, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.MESSAGE__PROPERTIES, newProperties,
                    newProperties));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Body getBody() {
        return body;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetBody(Body newBody, NotificationChain msgs) {
        Body oldBody = body;
        body = newBody;
        if (eNotificationRequired()) {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    MessagePackage.MESSAGE__BODY, oldBody, newBody);
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
    public void setBody(Body newBody) {
        if (newBody != body) {
            NotificationChain msgs = null;
            if (body != null) {
                msgs = ((InternalEObject) body).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                        - MessagePackage.MESSAGE__BODY, null, msgs);
            }
            if (newBody != null) {
                msgs = ((InternalEObject) newBody).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                        - MessagePackage.MESSAGE__BODY, null, msgs);
            }
            msgs = basicSetBody(newBody, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.MESSAGE__BODY, newBody, newBody));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setProviderId(String newProviderId) {
        String oldProviderId = providerId;
        providerId = newProviderId;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.MESSAGE__PROVIDER_ID, oldProviderId,
                    providerId));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getProviderName() {
        return providerName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setProviderName(String newProviderName) {
        String oldProviderName = providerName;
        providerName = newProviderName;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.MESSAGE__PROVIDER_NAME,
                    oldProviderName, providerName));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getType() {
        return type;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setType(String newType) {
        String oldType = type;
        type = newType;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.MESSAGE__TYPE, oldType, type));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case MessagePackage.MESSAGE__PROPERTIES:
                return basicSetProperties(null, msgs);
            case MessagePackage.MESSAGE__BODY:
                return basicSetBody(null, msgs);
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
            case MessagePackage.MESSAGE__PROPERTIES:
                return getProperties();
            case MessagePackage.MESSAGE__BODY:
                return getBody();
            case MessagePackage.MESSAGE__PROVIDER_ID:
                return getProviderId();
            case MessagePackage.MESSAGE__PROVIDER_NAME:
                return getProviderName();
            case MessagePackage.MESSAGE__TYPE:
                return getType();
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
            case MessagePackage.MESSAGE__PROPERTIES:
                setProperties((Properties) newValue);
                return;
            case MessagePackage.MESSAGE__BODY:
                setBody((Body) newValue);
                return;
            case MessagePackage.MESSAGE__PROVIDER_ID:
                setProviderId((String) newValue);
                return;
            case MessagePackage.MESSAGE__PROVIDER_NAME:
                setProviderName((String) newValue);
                return;
            case MessagePackage.MESSAGE__TYPE:
                setType((String) newValue);
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
            case MessagePackage.MESSAGE__PROPERTIES:
                setProperties((Properties) null);
                return;
            case MessagePackage.MESSAGE__BODY:
                setBody((Body) null);
                return;
            case MessagePackage.MESSAGE__PROVIDER_ID:
                setProviderId(PROVIDER_ID_EDEFAULT);
                return;
            case MessagePackage.MESSAGE__PROVIDER_NAME:
                setProviderName(PROVIDER_NAME_EDEFAULT);
                return;
            case MessagePackage.MESSAGE__TYPE:
                setType(TYPE_EDEFAULT);
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
            case MessagePackage.MESSAGE__PROPERTIES:
                return properties != null;
            case MessagePackage.MESSAGE__BODY:
                return body != null;
            case MessagePackage.MESSAGE__PROVIDER_ID:
                return PROVIDER_ID_EDEFAULT == null ? providerId != null : !PROVIDER_ID_EDEFAULT.equals(providerId);
            case MessagePackage.MESSAGE__PROVIDER_NAME:
                return PROVIDER_NAME_EDEFAULT == null ? providerName != null : !PROVIDER_NAME_EDEFAULT
                        .equals(providerName);
            case MessagePackage.MESSAGE__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
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
        result.append(" (providerId: ");
        result.append(providerId);
        result.append(", providerName: ");
        result.append(providerName);
        result.append(", type: ");
        result.append(type);
        result.append(')');
        return result.toString();
    }

} // MessageImpl
