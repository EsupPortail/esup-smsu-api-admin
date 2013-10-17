package org.esupportail.smsuapiadmin.dao.beans;

import java.io.Serializable;


/**
 * The class that represents phones that are in the blacklist.
 */
public class Blacklist  implements Serializable {

	/**
	 * Hibernate reference for blacklist.
	 */
	public static final String REF = "Blacklist";

	/**
	 * Hibernate property for the application source.
	 */
	public static final String PROP_APP = "App";

	/**
	 * Hibernate property for the phone.
	 */
	public static final String PROP_BLA_PHONE = "BlaPhone";

	/**
	 * Hibernate property for the date.
	 */
	public static final String PROP_BLA_DATE = "BlaDate";

	/**
	 * Hibernate property for the identifier.
	 */
	public static final String PROP_ID = "Id";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -4890358065129207527L;

	/**
	 * blacklist item identifier.
	 */
	private java.lang.Integer id;

	/**
	 * Date when the phone has been put in the blacklist.
	 */
	private java.util.Date date;

	/**
	 * Phone.
	 */
	private String phone;

	/**
	 * Application from which the invalid phone number has been detected.
	 */
	private Application app;

	/**
	 * Bean constructor.
	 */
	public Blacklist() {
		super();
	}

	/**
	 * Constructor for required fields.
	 */
	public Blacklist(
			final java.lang.Integer id,
			final Application app,
			final java.util.Date blaDate,
			final String blaPhone) {

		this.setId(id);
		this.setApp(app);
		this.setDate(blaDate);
		this.setPhone(blaPhone);
	}





	/**
	 * Return the unique identifier of this class.
     * @hibernate.id
     *  generator-class="native"
     *  column="BLA_ID"
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
	 * Return the value associated with the column: BLA_DATE.
	 */
	public java.util.Date getDate() {
		return date;
	}

	/**
	 * Set the value related to the column: BLA_DATE.
	 * @param date the BLA_DATE value
	 */
	public void setDate(final java.util.Date date) {
		this.date = date;
	}

	/**
	 * Return the value associated with the column: BLA_PHONE.
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Set the value related to the column: BLA_PHONE.
	 * @param phone the BLA_PHONE value
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

	/**
	 * Return the value associated with the column: APP_ID.
	 */
	public Application getApp() {
		return app;
	}

	/**
	 * Set the value related to the column: APP_ID.
	 * @param app the APP_ID value
	 */
	public void setApp(final Application app) {
		this.app = app;
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Blacklist)) {
			return false;
		} else {
			Blacklist blacklist = (Blacklist) obj;
			if (null == this.getId() || null == blacklist.getId()) {
				return false;
			} else {
				return this.getId().equals(blacklist.getId());
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
		return "Blacklist#" + hashCode() + "[id=[" + id + "], phone=[" + phone 
		+ "], date=[" + date +  "]]";
	}


}