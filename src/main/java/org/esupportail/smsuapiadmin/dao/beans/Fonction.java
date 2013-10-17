package org.esupportail.smsuapiadmin.dao.beans;

import java.io.Serializable;


/**
 * The class that represent functions (right access).
 */
public class Fonction  implements Serializable {

	/**
	 * Hibernate reference for function.
	 */
	public static final String REF = "Fonction";

	/**
	 * Hibernate property for the name.
	 */
	public static final String PROP_FTC_NAME = "Name";

	/**
	 * Hibernate property for the identifier.
	 */
	public static final String PROP_ID = "Id";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8521353039686407260L;


	/**
	 * Id of the function.
	 */
	private java.lang.Integer id;

	/**
	 * function name.
	 */
	private java.lang.String name;

	/**
	 * set of roles associated to the function.
	 */
	private java.util.Set<Role> roles;

	/**
	 * Bean constructor.
	 */
	public Fonction() {
		super();
	}

	/**
	 * Constructor for required fields.
	 */
	public Fonction(
		final java.lang.Integer id,
		final java.lang.String ftcName) {
		this.setId(id);
		this.setName(ftcName);
	}


	/**
	 * Return the unique identifier of this class.
     * @hibernate.id
     *  generator-class="native"
     *  column="FCT_ID"
     */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class.
	 * @param id the new ID
	 */
	public void setId(final java.lang.Integer id) {
		this.id = id;
	}




	/**
	 * Return the value associated with the column: FTC_NAME.
	 */
	public java.lang.String getName() {
		return name;
	}

	/**
	 * Set the value related to the column: FTC_NAME.
	 * @param ftcName the FTC_NAME value
	 */
	public void setName(final java.lang.String name) {
		this.name = name;
	}



	/**
	 * Return the value associated with the column: Roles.
	 */
	public java.util.Set<Role> getRoles() {
		return roles;
	}

	/**
	 * Set the value related to the column: Roles.
	 * @param roles the Roles value
	 */
	public void setRoles(final java.util.Set<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Add a role to the set associated to the function.
	 * @param role a role value
	 */
	public void addToRoles(final Role role) {
		if (null == getRoles()) {
			setRoles(new java.util.TreeSet<Role>());
		}
		getRoles().add(role);
	}



	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Fonction)) {
			return false;
		} else {
			Fonction fonction = (Fonction) obj;
			if (null == this.getId() || null == fonction.getId()) {
				return false;
			} else {
				return this.getId().equals(fonction.getId());
			}
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Function#" + hashCode() + "[id=[" + id + "], name=[" + name 
		+ "]]";
	}

}