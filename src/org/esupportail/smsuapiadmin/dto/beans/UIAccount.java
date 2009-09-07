package org.esupportail.smsuapiadmin.dto.beans;


/**
 * UIAccount is the representation on the web side of the Account persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIAccount extends UIObject {

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
	 * quota.
	 */
	private String quota;
	/**
	 * consumed_sms.
	 */
	private String consumedSms;

	/**
	 * Default constructor.
	 */
	public UIAccount() {
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
	 * Getter for 'quota'.
	 * 
	 * @return
	 */
	public String getQuota() {
		return quota;
	}

	/**
	 * Setter for 'quota'.
	 * 
	 * @param quota
	 */
	public void setQuota(final String quota) {
		this.quota = quota.trim();
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
	 * Setter for 'consumedSms'.
	 * 
	 * @param consumedSms
	 */
	public void setConsumedSms(final String consumedSms) {
		this.consumedSms = consumedSms;
	}

	/**
	 * Getter for 'consumedSms'.
	 * 
	 * @return
	 */
	public String getConsumedSms() {
		return consumedSms;
	}

}
