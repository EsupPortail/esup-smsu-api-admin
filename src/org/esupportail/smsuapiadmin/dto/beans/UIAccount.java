package org.esupportail.smsuapiadmin.dto.beans;



/**
 * UIAccount is the representation on the web side of the Account persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIAccount {

	/**
	 * identifier (database) of the account.
	 */
	private Integer id;
	/**
	 * name.
	 */
	private String name;
	/**
	 * quota.
	 */
	private Long quota;
	/**
	 * consumed_sms.
	 */
	private Long consumedSms;

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
	public Long getQuota() {
		return quota;
	}

	/**
	 * Setter for 'quota'.
	 * 
	 * @param quota
	 */
	public void setQuota(final Long quota) {
		this.quota = quota;
	}

	/**
	 * Setter for 'id'.
	 * 
	 * @param id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Getter for 'id'.
	 * 
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Setter for 'consumedSms'.
	 * 
	 * @param consumedSms
	 */
	public void setConsumedSms(final Long consumedSms) {
		this.consumedSms = consumedSms;
	}

	/**
	 * Getter for 'consumedSms'.
	 * 
	 * @return
	 */
	public Long getConsumedSms() {
		return consumedSms;
	}

}
