package org.esupportail.smsuapiadmin.dto.beans;

import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;

/**
 * UIFonction is the representation on the web side of the Fonction persistent.
 * @author MZRL3760
 *
 */
public class UIFonction extends UIObject {
	/**
	 * identifier (database) of the account.
	 */
	private String id;
	/**
	 * Enumerated function.
	 */
	private EnumeratedFunction function;

	/**
	 * Default constructor.
	 */
	public UIFonction() {
		// nothing to do
	}

	/**
	 * Getter for 'function'.
	 * 
	 * @return
	 */
	public EnumeratedFunction getFunction() {
		return function;
	}

	/**
	 * Setter for 'function'.
	 * 
	 * @param name
	 */
	public void setFunction(final EnumeratedFunction function) {
		this.function = function;
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

}
