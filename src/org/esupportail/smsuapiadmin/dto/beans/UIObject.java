package org.esupportail.smsuapiadmin.dto.beans;

/**
 * Base object for representbusinnes object on web side.
 * 
 * @author MZRL3760
 * 
 */
public class UIObject {

	/**
	 * Edit mode : Add (true) or Modify (false).
	 */
	private boolean addMode = true;

	/**
	 * Default constructor.
	 */
	public UIObject() {
		// do nothing
	}

	/**
	 * Sets the edit mode.
	 * 
	 * @param addMode
	 */
	public void setAddMode(final boolean addMode) {
		this.addMode = addMode;
	}

	/**
	 * Gets the edit mode.
	 * 
	 * @return
	 */
	public boolean isAddMode() {
		return addMode;
	}

}
