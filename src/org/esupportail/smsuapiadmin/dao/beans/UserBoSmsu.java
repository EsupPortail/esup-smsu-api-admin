package org.esupportail.smsuapiadmin.dao.beans;

import java.io.Serializable;


/**
 * The class that represents Back Office users.
 */
public class UserBoSmsu implements Serializable {

	/**
	 * Hibernate reference for customized group.
	 */
	public static final String REF = "UserBoSmsu";

	/**
	 * Hibernate property for the identifier.
	 */
	public static final String PROP_ID = "Id";

	/**
	 * Hibernate property for the login.
	 */
	public static final String PROP_LOGIN = "Login";
	
	/**
	 * Hibernate property for the role.
	 */
	public static final String PROP_ROLE = "Role";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8032758484323704638L;


	/**
	 * User identifier.
	 */
	private java.lang.Integer id;

	/**
	 * User login.
	 */
	private java.lang.String login;

	/**
	 * User role.
	 */
	private Role role;

	/**
	 * Bean constructor.
	 */
	public UserBoSmsu() {
		super();
	}

	/**
	 * Constructor for required fields.
	 */
	public UserBoSmsu(
		final java.lang.Integer id,
		final java.lang.String login,
		final Role role) {

		this.setId(id);
		this.setLogin(login);
		this.setRole(role);
	}




	/**
	 * Return the unique identifier of this class.
     * @hibernate.id
     *  generator-class="native"
     *  column="USER_ID"
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
	 * Return the value associated with the column: USER_LOGIN.
	 */
	public java.lang.String getLogin() {
		return login;
	}

	/**
	 * Set the value related to the column: USER_LOGIN.
	 * @param login the USER_LOGIN value
	 */
	public void setLogin(final java.lang.String login) {
		this.login = login;
	}



	/**
	 * Return the value associated with the column: Role.
	 */
	public Role getRole() {
		return role;
	}

	/**
	 * Set the value related to the column: Role.
	 * @param roles the Roles value
	 */
	public void setRole(final Role role) {
		this.role = role;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof UserBoSmsu)) {
			return false;
		} else {
			UserBoSmsu userBoSmsu = (UserBoSmsu) obj;
			if (null == this.getId() || null == userBoSmsu.getId()) {
				return false;
			} else {
				return this.getId().equals(userBoSmsu.getId());
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
		return "UserBoSmsu#" + hashCode() + "[id=[" + id + "], login=[" + login 
		+  "]]";
	}


}