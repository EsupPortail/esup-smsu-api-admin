package org.esupportail.smsuapiadmin.dao.beans;

import java.io.Serializable;


/**
 * The class that represents statistics.
 */
public class Statistic  implements Serializable {

	/**
	 * Hibernate reference for statistics.
	 */
	public static final String REF = "Statistic";

	/**
	 * Hibernate property for the number of sms in error.
	 */
	public static final String PROP_NB_SMS_IN_ERROR = "NbSmsInError";

	/**
	 * Hibernate property for the number of sms.
	 */
	public static final String PROP_NB_SMS = "NbSms";

	/**
	 * Hibernate property for the identifier.
	 */
	public static final String PROP_ID = "Id";

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8868222244245596801L;

	/**
	 * Statistic identifier.
	 */
	private StatisticPK id;

	/**
	 * number of SMS.
	 */
	private java.lang.Long nbSms;

	/**
	 * number of SMS in error.
	 */
	private java.lang.Long nbSmsInError;

	/**
	 * Bean constructor.
	 */
	public Statistic() {
		super();
	}

	/**
	 * Constructor for required fields.
	 */
	public Statistic(
			final StatisticPK id,
			final java.lang.Long nbSms,
			final java.lang.Long nbSmsInError) {

		this.setId(id);
		this.setNbSms(nbSms);
		this.setNbSmsInError(nbSmsInError);
	}


	/**
	 * Return the unique identifier of this class.
     * @hibernate.id
     */
	public StatisticPK getId() {
		return id;
	}

	/**
	 * Set the unique identifier of this class.
	 * @param id the new ID
	 */
	public void setId(final StatisticPK id) {
		this.id = id;
	}


	/**
	 * Return the value associated with the column: STAT_NB_SMS.
	 */
	public java.lang.Long getNbSms() {
		return nbSms;
	}

	/**
	 * Set the value related to the column: STAT_NB_SMS.
	 * @param nbSms the STAT_NB_SMS value
	 */
	public void setNbSms(final java.lang.Long nbSms) {
		this.nbSms = nbSms;
	}



	/**
	 * Return the value associated with the column: STAT_NB_SMS_IN_ERROR.
	 */
	public java.lang.Long getNbSmsInError() {
		return nbSmsInError;
	}

	/**
	 * Set the value related to the column: STAT_NB_SMS_IN_ERROR.
	 * @param nbSmsInError the STAT_NB_SMS_IN_ERROR value
	 */
	public void setNbSmsInError(final java.lang.Long nbSmsInError) {
		this.nbSmsInError = nbSmsInError;
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof Statistic)) {
			return false;
		} else {
			Statistic statistic = (Statistic) obj;
			if (null == this.getId() || null == statistic.getId()) {
				return false;
			} else {
				return this.getId().equals(statistic.getId());
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
		return "Statistic#" + hashCode() + "[id=[" + id + "], number of SMS=[" + nbSms 
		+ "], number of SMS in error=[" + nbSmsInError +  "]]";
	}


}