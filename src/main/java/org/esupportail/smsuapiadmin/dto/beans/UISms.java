package org.esupportail.smsuapiadmin.dto.beans;

import java.util.Date;

import org.esupportail.smsuapiadmin.dao.beans.SmsStatus;

/**
 * UISms is the representation on the web side of the Sms persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UISms extends UIObject {

	/**
	 * identifier (database) of the account.
	 */
	private String id;
	/**
	 * application.
	 */
	private UIApplication application;
	/**
	 * account.
	 */
	private UIAccount account;
	/**
	 * initialId.
	 */
	private String initialId;
	/**
	 * senderId.
	 */
	private String senderId;
	/**
	 * state.
	 */
	private SmsStatus state;
	/**
	 * date.
	 */
	private Date date;
	/**
	 * phone.
	 */
	private String phone;

	/**
	 * Default constructor.
	 */
	public UISms() {
		// nothing to do
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
	 * Getter for 'application'.
	 * 
	 * @return
	 */
	public UIApplication getApplication() {
		return application;
	}

	/**
	 * Setter for 'application'.
	 * 
	 * @return
	 */
	public void setApplication(final UIApplication application) {
		this.application = application;
	}

	/**
	 * Getter for 'account'.
	 * 
	 * @return
	 */
	public UIAccount getAccount() {
		return account;
	}

	/**
	 * Setter for 'account'.
	 * 
	 * @return
	 */
	public void setAccount(final UIAccount account) {
		this.account = account;
	}

	/**
	 * Getter for 'initialId'.
	 * 
	 * @return
	 */
	public String getInitialId() {
		return initialId;
	}

	/**
	 * Setter for 'initialId'.
	 * 
	 * @return
	 */
	public void setInitialId(final String initialId) {
		this.initialId = initialId;
	}

	/**
	 * Getter for 'senderId'.
	 * 
	 * @return
	 */
	public String getSenderId() {
		return senderId;
	}

	/**
	 * Setter for 'senderId'.
	 * 
	 * @return
	 */
	public void setSenderId(final String senderId) {
		this.senderId = senderId;
	}

	/**
	 * Getter for 'state'.
	 * 
	 * @return
	 */
	public SmsStatus getState() {
		return state;
	}

	/**
	 * Setter for 'state'.
	 * 
	 * @return
	 */
	public void setState(final SmsStatus state) {
		this.state = state;
	}

	/**
	 * Getter for 'date'.
	 * 
	 * @return
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Setter for 'date'.
	 * 
	 * @return
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Getter for 'phone'.
	 * 
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Setter for 'phone'.
	 * 
	 * @return
	 */
	public void setPhone(final String phone) {
		this.phone = phone;
	}

}
