package org.esupportail.smsuapiadmin.dao.beans;

import java.io.Serializable;


/**
 * The class that represents sms.
 */
public class Sms  implements Serializable {

	/**
	 * Hibernate reference for sms.
	 */
	public static final String REF = "Sms";

	/**
	 * Hibernate property for the identifier.
	 */

	/**
	 * Hibernate property for the sender identifier.
	 */
	public static final String PROP_SENDER_ID = "SenderId";

	/**
	 * Hibernate property for the account identifier.
	 */
	public static final String PROP_ACC = "Acc";

	/**
	 * Hibernate property for the state.
	 */
	public static final String PROP_STATE = "State";

	/**
	 * Hibernate property for the application identifier.
	 */
	public static final String PROP_APP = "App";

	/**
	 * Hibernate property for the date.
	 */
	public static final String PROP_DATE = "Date";

	/**
	 * Hibernate property for the initial identifier (in the source application).
	 */
	public static final String PROP_INITIAL_ID = "InitialId";

	/**
	 * Hibernate property for the identifier.
	 */
	public static final String PROP_ID = "Id";

	/**
	 * Hibernate property for the phone.
	 */
	public static final String PROP_PHONE = "Phone";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -583019571723546904L;

	/**
	 * Sms identifier.
	 */
	private java.lang.Integer id;

	/**
	 * Sms initial identifier (in the application source).
	 */
	private java.lang.Integer initialId;

	/**
	 * Sender identifier of the sms (in the application source).
	 */
	private java.lang.Integer senderId;

	/**
	 * Sms state.
	 */
	private java.lang.String state;

	/**
	 * Sms date.
	 */
	private java.util.Date date;

	/**
	 * Sms phone.
	 */
	private java.lang.String phone;

	/**
	 * Application associated to the SMS.
	 */
	private Application app;

	/**
	 * Account associated to the application.
	 */
	private Account acc;

	/**
	 * Bean constructor.
	 */
	public Sms() {
		super();
	}

	/**
	 * Constructor for required fields.
	 */
	public Sms(
			final java.lang.Integer id,
			final Application app,
			final Account acc,
			final java.lang.String state,
			final java.util.Date date) {

		this.setId(id);
		this.setApp(app);
		this.setAcc(acc);
		this.setState(state);
		this.setDate(date);
	}

	/**
	 * Return the unique identifier of this class.
     * @hibernate.id
     *  generator-class="native"
     *  column="SMS_ID"
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
	 * Return the value associated with the column: SMS_INITIAL_ID.
	 */
	public java.lang.Integer getInitialId() {
		return initialId;
	}

	/**
	 * Set the value related to the column: SMS_INITIAL_ID.
	 * @param initialId the SMS_INITIAL_ID value
	 */
	public void setInitialId(final java.lang.Integer initialId) {
		this.initialId = initialId;
	}



	/**
	 * Return the value associated with the column: SMS_SENDER_ID.
	 */
	public java.lang.Integer getSenderId() {
		return senderId;
	}

	/**
	 * Set the value related to the column: SMS_SENDER_ID.
	 * @param senderId the SMS_SENDER_ID value
	 */
	public void setSenderId(final java.lang.Integer senderId) {
		this.senderId = senderId;
	}

	/**
	 * Return the value associated with the column: SMS_STATE.
	 */
	public java.lang.String getState() {
		return state;
	}

	/**
	 * Set the value related to the column: SMS_STATE.
	 * @param state the SMS_STATE value
	 */
	public void setState(final java.lang.String state) {
		this.state = state;
	}



	/**
	 * Return the value associated with the column: SMS_DATE.
	 */
	public java.util.Date getDate() {
		return date;
	}

	/**
	 * Set the value related to the column: SMS_DATE.
	 * @param date the SMS_DATE value
	 */
	public void setDate(final java.util.Date date) {
		this.date = date;
	}



	/**
	 * Return the value associated with the column: SMS_PHONE.
	 */
	public java.lang.String getPhone() {
		return phone;
	}

	/**
	 * Set the value related to the column: SMS_PHONE.
	 * @param phone the SMS_PHONE value
	 */
	public void setPhone(final java.lang.String phone) {
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
	 * Return the value associated with the column: ACC_ID.
	 */
	public Account getAcc() {
		return acc;
	}

	/**
	 * Set the value related to the column: ACC_ID.
	 * @param acc the ACC_ID value
	 */
	public void setAcc(final Account acc) {
		this.acc = acc;
	}




	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Sms)) {
			return false;
		} else {
			Sms sms = (Sms) obj;
			if (null == this.getId() || null == sms.getId()) {
				return false;
			} else {
				return this.getId().equals(sms.getId());
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
		return "Sms#" + hashCode() + "[id=[" + id + "], initial id=[" + initialId 
		+ "], sender id=[" + senderId + "], phone=[" + phone 
		+ "], state=[" + state + "], date=[" + date + "]]";
	}

}