/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.fusesource.tools.core.message;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Properties</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link org.fusesource.tools.core.message.Properties#getProperty <em>Property</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.fusesource.tools.core.message.MessagePackage#getProperties()
 * @model extendedMetaData="name='propertiesType' kind='elementOnly'"
 * @generated
 */
public interface Properties extends EObject {
	/**
	 * Returns the value of the '<em><b>Property</b></em>' containment reference list.
	 * The list contents are of type {@link org.fusesource.tools.core.message.Property}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property</em>' containment reference list.
	 * @see org.fusesource.tools.core.message.MessagePackage#getProperties_Property()
	 * @model containment="true"
	 *        extendedMetaData="kind='element' name='property' namespace='##targetNamespace'"
	 * @generated
	 */
	EList<Property> getProperty();

} // Properties
