package org.esupportail.smsuapiadmin.dto.beans;

import java.io.Serializable;

import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;
import org.esupportail.smsuapiadmin.domain.beans.User;

/**
 * UIUser is the representation on the web side of the User persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIUser implements Serializable {

	/**
	 * The UID.
	 */
	private static final long serialVersionUID = -5312393467136910173L;
	/**
	 * identifier (database) of the account.
	 */
	private String id;
	/**
	 * login.
	 */
	private String login;
	/**
	 * role.
	 */
	private EnumeratedRole role;
	
	/**
	 * Boolean isDeletable.
	 */
	private Boolean isDeletable;
	
	/**
	 * Boolean isUpdateable.
	 */
	private Boolean isUpdateable;
	
	public String sessionId;
	public String idpId;

	/**
	 * Default constructor.
	 */
	public UIUser() {
	}

	public UIUser(String login) {
        this.login = login;
	}

	/**
	 * Getter for 'name'.
	 * 
	 * @return
	 */
	public String getLogin() {
		return login;
	}

	/**
	 * Setter for 'name'.
	 * 
	 * @param name
	 */
	public void setLogin(final String login) {
		this.login = login.trim();
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
	 * Setter for 'role'.
	 * 
	 * @param role
	 */
	public void setRole(final EnumeratedRole role) {
		this.role = role;
	}

	/**
	 * Getter for 'role'.
	 * 
	 * @return
	 */
	public EnumeratedRole getRole() {
		return role;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		return id.equals(((User) obj).getId());
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public Boolean getIsDeletable() {
		return isDeletable;
	}

	public void setIsDeletable(final Boolean isDeletable) {
		this.isDeletable = isDeletable;
	}

	public Boolean getIsUpdateable() {
		return isUpdateable;
	}

	public void setIsUpdateable(final Boolean isUpdateable) {
		this.isUpdateable = isUpdateable;
	}

}
