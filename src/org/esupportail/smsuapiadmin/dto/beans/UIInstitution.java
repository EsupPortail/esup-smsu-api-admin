package org.esupportail.smsuapiadmin.dto.beans;

/**
 * UIInstitution is the representation on the web side of the Institution
 * persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIInstitution extends UIObject {

	/**
	 * The UID.
	 */
	private static final long serialVersionUID = -5312393467136910173L;

	/**
	 * identifier (database) of the account.
	 */
	private String id;
	/**
	 * name.
	 */
	private String name;

	/**
	 * Default constructor.
	 */
	public UIInstitution() {
		// nothing to do
	}

	/**
	 * Getter for 'name'.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for 'name'.
	 * 
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name.trim();
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
	 * Setter for 'id'.
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

}
