package org.esupportail.smsuapiadmin.dao.beans;
        
import java.io.Serializable;


/**
 * This is an object that contains data related to the role_composition table.
 *
 * @hibernate.class
 *  table="role_composition"
 */
public abstract class RoleComposition  implements Serializable {

	/**
	 * Hibernate reference for the association roleComposition.
	 */
	public static final String REF = "RoleComposition";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -6634898005768689305L;

	/**
	 * function identifier.
	 */
	private Fonction fct;

	/**
	 * role identifier.
	 */
	private Role rol;


	/**
	 * Bean constructor.
	 */
	public RoleComposition() {
		super();
	}

	/**
	 * Constructor for required fields.
	 */
	public RoleComposition(
		final Fonction fct,
		final Role rol) {

		this.setFct(fct);
		this.setRol(rol);
	}

	/**
     * @hibernate.property
     *  column=FCT_ID
	 * not-null=true
	 */
	public Fonction getFct() {
		return this.fct;
	}

	/**
	 * Set the value related to the column: FCT_ID.
	 * @param fct the FCT_ID value
	 */
	public void setFct(final Fonction fct) {
		this.fct = fct;
	}

	/**
     * @hibernate.property
     *  column=ROL_ID
	 * not-null=true
	 */
	public Role getRol() {
		return this.rol;
	}

	/**
	 * Set the value related to the column: ROL_ID.
	 * @param rol the ROL_ID value
	 */
	public void setRol(final Role rol) {
		this.rol = rol;
	}





	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof RoleComposition)) {
			return false;
		} else {
			RoleComposition roleComposition = (RoleComposition) obj;
			if (null != this.getFct() && null != roleComposition.getFct()) {
				if (!this.getFct().equals(roleComposition.getFct())) {
					return false;
				}
			} else {
				return false;
			}
			if (null != this.getRol() && null != roleComposition.getRol()) {
				if (!this.getRol().equals(roleComposition.getRol())) {
					return false;
				}
			} else {
				return false;
			}
			return true;
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
		return "RoleComposition#" + hashCode() + "[role id=[" + rol + "], function=[" + fct 
		+ "]]";
	}


}