/**
 * generated by Xtext 2.19.0
 */
package org.xtext.example.mydsl.mml.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.xtext.example.mydsl.mml.AlgorithmVisitor;
import org.xtext.example.mydsl.mml.DT;
import org.xtext.example.mydsl.mml.MLAlgorithm;
import org.xtext.example.mydsl.mml.MmlPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>DT</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.xtext.example.mydsl.mml.impl.DTImpl#getMax_depth <em>Max
 * depth</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DTImpl extends MinimalEObjectImpl.Container implements DT,MLAlgorithm {
	/**
	 * The default value of the '{@link #getMax_depth() <em>Max depth</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMax_depth()
	 * @generated
	 * @ordered
	 */
	protected static final int MAX_DEPTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getMax_depth() <em>Max depth</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getMax_depth()
	 * @generated
	 * @ordered
	 */
	protected int max_depth = MAX_DEPTH_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DTImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MmlPackage.Literals.DT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public int getMax_depth() {
		return max_depth;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setMax_depth(int newMax_depth) {
		int oldMax_depth = max_depth;
		max_depth = newMax_depth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MmlPackage.DT__MAX_DEPTH, oldMax_depth, max_depth));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case MmlPackage.DT__MAX_DEPTH:
			return getMax_depth();
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
		case MmlPackage.DT__MAX_DEPTH:
			setMax_depth((Integer) newValue);
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
		case MmlPackage.DT__MAX_DEPTH:
			setMax_depth(MAX_DEPTH_EDEFAULT);
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
		case MmlPackage.DT__MAX_DEPTH:
			return max_depth != MAX_DEPTH_EDEFAULT;
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
		if (eIsProxy())
			return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (max_depth: ");
		result.append(max_depth);
		result.append(')');
		return result.toString();
	}

	@Override
	public String accept(AlgorithmVisitor<String> v) {
		return v.visit(this);
	}

} // DTImpl
