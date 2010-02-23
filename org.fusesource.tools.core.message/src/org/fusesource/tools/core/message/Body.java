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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Body</b></em>'. <!--
 * end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.fusesource.tools.core.message.Body#getContent <em>Content</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.Body#getFileRef <em>File Ref</em>}</li>
 * <li>{@link org.fusesource.tools.core.message.Body#isUseFileRef <em>Use File Ref</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.fusesource.tools.core.message.MessagePackage#getBody()
 * @model extendedMetaData="name='body_._type' kind='elementOnly'"
 * @generated
 */
public interface Body extends EObject {
    /**
     * Returns the value of the '<em><b>Content</b></em>' containment reference. <!-- begin-user-doc
     * -->
     * <p>
     * If the meaning of the '<em>Content</em>' containment reference isn't clear, there really
     * should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Content</em>' containment reference.
     * @see #setContent(EObject)
     * @see org.fusesource.tools.core.message.MessagePackage#getBody_Content()
     * @model containment="true" required="true"
     *        extendedMetaData="kind='element' name='content' namespace='##targetNamespace'"
     * @generated
     */
    EObject getContent();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Body#getContent
     * <em>Content</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Content</em>' containment reference.
     * @see #getContent()
     * @generated
     */
    void setContent(EObject value);

    /**
     * Returns the value of the '<em><b>File Ref</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>File Ref</em>' attribute isn't clear, there really should be more
     * of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>File Ref</em>' attribute.
     * @see #setFileRef(String)
     * @see org.fusesource.tools.core.message.MessagePackage#getBody_FileRef()
     * @model dataType="org.eclipse.emf.ecore.xml.type.String"
     *        extendedMetaData="kind='attribute' name='file-ref'"
     * @generated
     */
    String getFileRef();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Body#getFileRef
     * <em>File Ref</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>File Ref</em>' attribute.
     * @see #getFileRef()
     * @generated
     */
    void setFileRef(String value);

    /**
     * Returns the value of the '<em><b>Use File Ref</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use File Ref</em>' attribute isn't clear, there really should be
     * more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Use File Ref</em>' attribute.
     * @see #isSetUseFileRef()
     * @see #unsetUseFileRef()
     * @see #setUseFileRef(boolean)
     * @see org.fusesource.tools.core.message.MessagePackage#getBody_UseFileRef()
     * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
     *        extendedMetaData="kind='attribute' name='use-file-ref'"
     * @generated
     */
    boolean isUseFileRef();

    /**
     * Sets the value of the '{@link org.fusesource.tools.core.message.Body#isUseFileRef
     * <em>Use File Ref</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Use File Ref</em>' attribute.
     * @see #isSetUseFileRef()
     * @see #unsetUseFileRef()
     * @see #isUseFileRef()
     * @generated
     */
    void setUseFileRef(boolean value);

    /**
     * Unsets the value of the '{@link org.fusesource.tools.core.message.Body#isUseFileRef
     * <em>Use File Ref</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetUseFileRef()
     * @see #isUseFileRef()
     * @see #setUseFileRef(boolean)
     * @generated
     */
    void unsetUseFileRef();

    /**
     * Returns whether the value of the '{@link org.fusesource.tools.core.message.Body#isUseFileRef
     * <em>Use File Ref</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Use File Ref</em>' attribute is set.
     * @see #unsetUseFileRef()
     * @see #isUseFileRef()
     * @see #setUseFileRef(boolean)
     * @generated
     */
    boolean isSetUseFileRef();

    String getMessageContent();

} // Body
