package org.esupportail.smsuapiadmin.dao.beans;

import java.io.Serializable;

/**
 * The class that represents accounts.
 * 
 * @author MZRL3760
 * 
 */
public class Account implements Serializable {

	/**
	 * Hibernate reference for account.
	 */
	public static final String REF = "Account";

	/**
	 * Hibernate property for the quota.
	 */
	public static final String PROP_QUOTA = "Quota";

	/**
	 * Hibernate property for the number of consumed sms.
	 */
	public static final String PROP_CONSUMED_SMS = "ConsumedSms";

	/**
	 * Hibernate property for the label.
	 */
	public static final String PROP_LABEL = "Label";

	/**
	 * Hibernate property for the identifier.
	 */
	public static final String PROP_ID = "Id";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -1278768065587081098L;

	private static final long DEFAULT_QUOTA = 0;
	/**
	 * Account identifier.
	 */
	private java.lang.Integer id;

	/**
	 * Account label.
	 */
	private java.lang.String label;

	/**
	 * Account quota.
	 */
	private java.lang.Long quota;

	/**
	 * Number of consumed SMS.
	 */
	private java.lang.Long consumedSms;

	/**
	 * Collection of SMS associated to the account.
	 */
	private java.util.Set<Sms> sms;

	/**
	 * Number of consumed SMS.
	 */
	private java.util.Set<Application> applications;

	/**
	 * Bean constructor.
	 */
	public Account() {
		super();
		init();
	}

	/**
	 * Constructor for required fields.
	 */
	public Account(final java.lang.Integer id, final java.lang.String label,
			final java.lang.Long consumedSms) {

		this.setId(id);
		this.setLabel(label);
		this.setConsumedSms(consumedSms);
	}

	/**
	 * Initialize the object with the default values.
	 */
	private void init() {
		quota = DEFAULT_QUOTA;
		consumedSms = (long) 0;
	}

	/**
	 * Return the unique identifier of this class.
	 * 
	 * @hibernate.id generator-class="native" column="ACC_ID"
	 */
	public java.lang.Integer getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class.
	 * 
	 * @param id
	 *            the new ID
	 */
	public void setId(final java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * Return the value associated with the column: ACC_LABEL.
	 */
	public java.lang.String getLabel() {
		return label;
	}

	/**
	 * Set the value related to the column: ACC_LABEL.
	 * 
	 * @param label
	 *            the ACC_LABEL value
	 */
	public void setLabel(final java.lang.String label) {
		this.label = label;
	}

	/**
	 * Return the value associated with the column: ACC_QUOTA.
	 */
	public java.lang.Long getQuota() {
		return quota;
	}

	/**
	 * Set the value related to the column: ACC_QUOTA.
	 * 
	 * @param quota
	 *            the ACC_QUOTA value
	 */
	public void setQuota(final java.lang.Long quota) {
		this.quota = quota;
	}

	/**
	 * Return the value associated with the column: ACC_CONSUMED_SMS.
	 */
	public java.lang.Long getConsumedSms() {
		return consumedSms;
	}

	/**
	 * Set the value related to the column: ACC_CONSUMED_SMS.
	 * 
	 * @param consumedSms
	 *            the ACC_CONSUMED_SMS value
	 */
	public void setConsumedSms(final java.lang.Long consumedSms) {
		this.consumedSms = consumedSms;
	}

	/**
	 * Return the value associated with the column: Sms.
	 */
	public java.util.Set<Sms> getSms() {
		return sms;
	}

	/**
	 * Set the value related to the column: Sms.
	 * 
	 * @param sms
	 *            the Sms value
	 */
	public void setSms(final java.util.Set<Sms> sms) {
		this.sms = sms;
	}

	/**
	 * Add the sms to the collection of sms associated to the account.
	 * 
	 * @param sms
	 */
	public void addToSms(final Sms sms) {
		if (null == getSms()) {
			setSms(new java.util.TreeSet<Sms>());
		}
		getSms().add(sms);
	}

	/**
	 * Return the value associated with the column: Applications.
	 */
	public java.util.Set<Application> getApplications() {
		return applications;
	}

	/**
	 * Set the value related to the column: Applications.
	 * 
	 * @param applications
	 *            the Applications value
	 */
	public void setApplications(final java.util.Set<Application> applications) {
		this.applications = applications;
	}

	/**
	 * Add the application to the collection of applications associated to the
	 * account.
	 * 
	 * @param application
	 */
	public void addToApplications(final Application application) {
		if (null == getApplications()) {
			setApplications(new java.util.TreeSet<Application>());
		}
		getApplications().add(application);
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Account)) {
			return false;
		} else {
			Account account = (Account) obj;
			if (null == this.getId() || null == account.getId()) {
				return false;
			} else {
				return this.getId().equals(account.getId());
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
		return "Account#" + hashCode() + "[id=[" + id + "], label=[" + label
				+ "], quota=[" + quota + "], number of consumed sms =["
				+ consumedSms + "]]";
	}

}