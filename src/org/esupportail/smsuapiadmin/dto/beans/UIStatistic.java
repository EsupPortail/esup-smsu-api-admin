package org.esupportail.smsuapiadmin.dto.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;

/**
 * UIMonth is the representation on the web side of the Statistic persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIStatistic {

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * month.
	 */
	private Date month;

	/**
	 * nbSMS.
	 */
	private long nbSendedSMS;

	/**
	 * nbSMSInError.
	 */
	private long nbSMSInError;

	/**
	 * accountName.
	 */
	private String accountName;

	/**
	 * appName.
	 */
	private String appName;

	/**
	 * institution.
	 */
	private String institution;

	/**
	 * Setter for 'nbSMS'.
	 * 
	 * @param nbSMS
	 */
	public void setNbSendedSMS(final long nbSendedSMS) {
		this.nbSendedSMS = nbSendedSMS;
	}

	/**
	 * Getter for 'nbSMS'.
	 * 
	 * @return
	 */
	public long getNbSendedSMS() {
		return nbSendedSMS;
	}

	/**
	 * Setter for 'nbSMSInError'.
	 * 
	 * @param nbSMSInError
	 */
	public void setNbSMSInError(final long nbSMSInError) {
		this.nbSMSInError = nbSMSInError;
	}

	/**
	 * Getter for 'nbSMSInError'.
	 * 
	 * @return
	 */
	public long getNbSMSInError() {
		return nbSMSInError;
	}

	/**
	 * Setter for 'accountName'.
	 * 
	 * @return
	 */
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}

	/**
	 * Getter for 'accountName'.
	 * 
	 * @return
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * Setter for 'appName'.
	 * 
	 * @return
	 */
	public void setAppName(final String appName) {
		this.appName = appName;
	}

	/**
	 * Getter for 'appName'.
	 * 
	 * @return
	 */
	public String getAppName() {
		return appName;
	}

	/**
	 * Setter for 'institution'.
	 * 
	 * @return
	 */
	public void setInstitution(final String institution) {
		this.institution = institution;
	}

	/**
	 * Getter for 'institution'.
	 * 
	 * @return
	 */
	public String getInstitution() {
		return institution;
	}

	/**
	 * Setter for 'month'.
	 * 
	 * @return
	 */
	public void setMonth(final Date month) {
		this.month = month;
	}

	/**
	 * Getter for 'month'.
	 * 
	 * @return
	 */
	public String getMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		try {
			return sdf.format(month);
		} catch (IllegalArgumentException e) {
			logger.warn("Impossible de formater la date", e);
			return "N/A";
		}
	}

}
