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
package org.fusesource.tools.core.message;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Property</b></em>'. <!--
 * end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.fusesource.tools.core.message.Property#isIsheader <em>Isheader</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.Property#getName <em>Name</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.Property#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.fusesource.tools.core.message.MessagePackage#getProperty()
 * @model extendedMetaData="name='propertyType' kind='empty'"
 * @generated
 */
public interface Property extends EObject {
    /**
     * Returns the value of the '<em><b>Isheader</b></em>' attribute. The default value is
     * <code>"true"</code>. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Isheader</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Isheader</em>' attribute.
     * @see #isSetIsheader()
     * @see #unsetIsheader()
     * @see #setIsheader(boolean)
     * @see org.fusesource.tools.core.message.MessagePackage#getProperty_Isheader()
     * @model default="true" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='isheader'"
     * @generated
     */
    boolean isIsheader();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Property#isIsheader
     * <em>Isheader</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Isheader</em>' attribute.
     * @see #isSetIsheader()
     * @see #unsetIsheader()
     * @see #isIsheader()
     * @generated
     */
    void setIsheader(boolean value);

    /**
     * Unsets the value of the '{@link org.fusesource.tools.core.message.Property#isIsheader
     * <em>Isheader</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetIsheader()
     * @see #isIsheader()
     * @see #setIsheader(boolean)
     * @generated
     */
    void unsetIsheader();

    /**
     * Returns whether the value of the '
     * {@link org.fusesource.tools.core.message.Property#isIsheader <em>Isheader</em>}' attribute is
     * set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Isheader</em>' attribute is set.
     * @see #unsetIsheader()
     * @see #isIsheader()
     * @see #setIsheader(boolean)
     * @generated
     */
    boolean isSetIsheader();

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see org.fusesource.tools.core.message.MessagePackage#getProperty_Name()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='name'"
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Property#getName
     * <em>Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName(String value);

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear, there really should be more of
     * a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see org.fusesource.tools.core.message.MessagePackage#getProperty_Value()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='value'"
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Property#getValue
     * <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue(String value);

} // Property
