package org.esupportail.smsuapiadmin.dto.beans;

import java.util.Set;

import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;

/**
 * UIRole is the representation on the web side of the Role persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIRole {

	/**
	 * identifier (database) of the account.
	 */
	private String id;
	/**
	 * Enumerated role.
	 */
	private EnumeratedRole role;
	/**
	 * fonctions.
	 */
	private Set<EnumeratedFunction> fonctions;

	/**
	 * Default constructor.
	 */
	public UIRole() {
	}

	/**
	 * Setter for 'id'.
	 * 
	 * @param id
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Getter for 'id'.
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Setter for 'fonctions'.
	 * 
	 * @param fonctions
	 */
	public void setFonctions(final Set<EnumeratedFunction> fonctions) {
		this.fonctions = fonctions;
	}

	/**
	 * Getter for 'fonctions'.
	 * 
	 * @return
	 */
	public Set<EnumeratedFunction> getFonctions() {
		return fonctions;
	}

	/**
	 * Returns true if the role contains the function.
	 * 
	 * @param fct
	 * @return
	 */
	public boolean isAuthorizedForFonction(final EnumeratedFunction fct) {
		for (EnumeratedFunction uiFct : fonctions) {
			if (fct == uiFct) return true;
		}
		return false;
	}

	/**
	 * Setter for 'role'.
	 * 
	 * @param role
	 */
	public void setRole(final EnumeratedRole role) {
		this.role = role;
	}

	/**
	 * Getter for 'role'.
	 * @return
	 */
	public EnumeratedRole getRole() {
		return role;
	}
	
	public String getI18nKey() {
		return role.getI18nKey();
	}
}
