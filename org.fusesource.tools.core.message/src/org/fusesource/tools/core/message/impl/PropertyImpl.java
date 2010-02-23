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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.fusesource.tools.core.message.MessagePackage;
import org.fusesource.tools.core.message.Property;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Property</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.fusesource.tools.core.message.impl.PropertyImpl#isIsheader <em>Isheader</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.impl.PropertyImpl#getName <em>Name</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.impl.PropertyImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class PropertyImpl extends EObjectImpl implements Property {
    /**
     * The default value of the '{@link #isIsheader() <em>Isheader</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isIsheader()
     * @generated
     * @ordered
     */
    protected static final boolean ISHEADER_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isIsheader() <em>Isheader</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isIsheader()
     * @generated
     * @ordered
     */
    protected boolean isheader = ISHEADER_EDEFAULT;

    /**
     * This is true if the Isheader attribute has been set. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean isheaderESet;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final String VALUE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected String value = VALUE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected PropertyImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return MessagePackage.Literals.PROPERTY;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isIsheader() {
        return isheader;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setIsheader(boolean newIsheader) {
        boolean oldIsheader = isheader;
        isheader = newIsheader;
        boolean oldIsheaderESet = isheaderESet;
        isheaderESet = true;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.PROPERTY__ISHEADER, oldIsheader,
                    isheader, !oldIsheaderESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetIsheader() {
        boolean oldIsheader = isheader;
        boolean oldIsheaderESet = isheaderESet;
        isheader = ISHEADER_EDEFAULT;
        isheaderESet = false;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.UNSET, MessagePackage.PROPERTY__ISHEADER, oldIsheader,
                    ISHEADER_EDEFAULT, oldIsheaderESet));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetIsheader() {
        return isheaderESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.PROPERTY__NAME, oldName, name));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getValue() {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setValue(String newValue) {
        String oldValue = value;
        value = newValue;
        if (eNotificationRequired()) {
            eNotify(new ENotificationImpl(this, Notification.SET, MessagePackage.PROPERTY__VALUE, oldValue, value));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case MessagePackage.PROPERTY__ISHEADER:
                return isIsheader() ? Boolean.TRUE : Boolean.FALSE;
            case MessagePackage.PROPERTY__NAME:
                return getName();
            case MessagePackage.PROPERTY__VALUE:
                return getValue();
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
            case MessagePackage.PROPERTY__ISHEADER:
                setIsheader(((Boolean) newValue).booleanValue());
                return;
            case MessagePackage.PROPERTY__NAME:
                setName((String) newValue);
                return;
            case MessagePackage.PROPERTY__VALUE:
                setValue((String) newValue);
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
            case MessagePackage.PROPERTY__ISHEADER:
                unsetIsheader();
                return;
            case MessagePackage.PROPERTY__NAME:
                setName(NAME_EDEFAULT);
                return;
            case MessagePackage.PROPERTY__VALUE:
                setValue(VALUE_EDEFAULT);
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
            case MessagePackage.PROPERTY__ISHEADER:
                return isSetIsheader();
            case MessagePackage.PROPERTY__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
            case MessagePackage.PROPERTY__VALUE:
                return VALUE_EDEFAULT == null ? value != null : !VALUE_EDEFAULT.equals(value);
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
        result.append(" (isheader: ");
        if (isheaderESet) {
            result.append(isheader);
        } else {
            result.append("<unset>");
        }
        result.append(", name: ");
        result.append(name);
        result.append(", value: ");
        result.append(value);
        result.append(')');
        return result.toString();
    }

} // PropertyImpl
