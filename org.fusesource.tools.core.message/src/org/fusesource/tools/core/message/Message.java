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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Message</b></em>'. <!--
 * end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.fusesource.tools.core.message.Message#getProperties <em>Properties</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.Message#getBody <em>Body</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.Message#getProviderId <em>Provider Id</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.Message#getProviderName <em>Provider Name</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.Message#getType <em>Type</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.fusesource.tools.core.message.MessagePackage#getMessage()
 * @model extendedMetaData="name='message_._type' kind='elementOnly'"
 * @generated
 */
public interface Message extends EObject {
    /**
     * Returns the value of the '<em><b>Properties</b></em>' containment reference. <!--
     * begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Properties</em>' containment reference isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Properties</em>' containment reference.
     * @see #setProperties(Properties)
     * @see org.fusesource.tools.core.message.MessagePackage#getMessage_Properties()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='properties' namespace='##targetNamespace'"
     * @generated
     */
    Properties getProperties();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Message#getProperties
     * <em>Properties</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Properties</em>' containment reference.
     * @see #getProperties()
     * @generated
     */
    void setProperties(Properties value);

    /**
     * Returns the value of the '<em><b>Body</b></em>' containment reference. <!-- begin-user-doc
     * -->
     * <p>
     * If the meaning of the '<em>Body</em>' containment reference isn't clear, there really should
     * be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Body</em>' containment reference.
     * @see #setBody(Body)
     * @see org.fusesource.tools.core.message.MessagePackage#getMessage_Body()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='body' namespace='##targetNamespace'"
     * @generated
     */
    Body getBody();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Message#getBody
     * <em>Body</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Body</em>' containment reference.
     * @see #getBody()
     * @generated
     */
    void setBody(Body value);

    /**
     * Returns the value of the '<em><b>Provider Id</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Provider Id</em>' attribute isn't clear, there really should be
     * more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Provider Id</em>' attribute.
     * @see #setProviderId(String)
     * @see org.fusesource.tools.core.message.MessagePackage#getMessage_ProviderId()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='providerId'"
     * @generated
     */
    String getProviderId();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Message#getProviderId
     * <em>Provider Id</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Provider Id</em>' attribute.
     * @see #getProviderId()
     * @generated
     */
    void setProviderId(String value);

    /**
     * Returns the value of the '<em><b>Provider Name</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Provider Name</em>' attribute isn't clear, there really should be
     * more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Provider Name</em>' attribute.
     * @see #setProviderName(String)
     * @see org.fusesource.tools.core.message.MessagePackage#getMessage_ProviderName()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='providerName'"
     * @generated
     */
    String getProviderName();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Message#getProviderName
     * <em>Provider Name</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Provider Name</em>' attribute.
     * @see #getProviderName()
     * @generated
     */
    void setProviderName(String value);

    /**
     * Returns the value of the '<em><b>Type</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Type</em>' attribute isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Type</em>' attribute.
     * @see #setType(String)
     * @see org.fusesource.tools.core.message.MessagePackage#getMessage_Type()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     *        extendedMetaData="kind='attribute' name='type'"
     * @generated
     */
    String getType();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Message#getType
     * <em>Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Type</em>' attribute.
     * @see #getType()
     * @generated
     */
    void setType(String value);

} // Message
