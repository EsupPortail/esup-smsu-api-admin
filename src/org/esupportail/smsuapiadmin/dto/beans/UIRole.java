package org.esupportail.smsuapiadmin.dto.beans;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;

/**
 * UIRole is the representation on the web side of the Role persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIRole extends UIObject {

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
	private List<UIFonction> fonctions;

	/**
	 * Default constructor.
	 */
	public UIRole() {
		init();
	}

	/**
	 * Initializes the bean.
	 */
	private void init() {
		fonctions = new ArrayList<UIFonction>();
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
	public void setFonctions(final List<UIFonction> fonctions) {
		this.fonctions = fonctions;
	}

	/**
	 * Getter for 'fonctions'.
	 * 
	 * @return
	 */
	public List<UIFonction> getFonctions() {
		return fonctions;
	}

	/**
	 * Returns true if the role contains the function.
	 * 
	 * @param fct
	 * @return
	 */
	public boolean isAuthorizedForFonction(final EnumeratedFunction fct) {
		for (UIFonction uiFct : fonctions) {
			if (fct == uiFct.getFunction()) return true;
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
