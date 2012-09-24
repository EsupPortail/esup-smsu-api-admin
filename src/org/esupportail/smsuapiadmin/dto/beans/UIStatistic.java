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
public class UIStatistic extends UIObject {

	/**
	 * The UID.
	 */
	private static final long serialVersionUID = -5312393467136910173L;

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
	 * account.
	 */
	private UIAccount account;

	/**
	 * application.
	 */
	private UIApplication application;

	/**
	 * Default constructor.
	 */
	public UIStatistic() {
		// nothing to do
	}

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
	public String getNbSendedSMS() {
		return nbSendedSMS + "";
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
	public String getNbSMSInError() {
		return nbSMSInError + "";
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
	 * Getter for 'account'.
	 * 
	 * @return
	 */
	public UIAccount getAccount() {
		return account;
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
	 * Getter for 'application'.
	 * 
	 * @return
	 */
	public UIApplication getApplication() {
		return application;
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
	public Date getMonth() {
		return month;
	}

	/**
	 * Compute the fail rate.
	 * 
	 * @return
	 */
	public String getFailRate() {
		String result = "N/A";

				if (nbSendedSMS != 0) {
					final double cent = 100;
					double rate = ((double) nbSMSInError / nbSendedSMS) * cent;
					result = (int) rate + "%";
				}

		return result;
	}

	/**
	 * Compute number of received SMS.
	 * 
	 * @return
	 */
	public String getNbReceivedSMS() {
		long nbReceivedSMS = nbSendedSMS - nbSMSInError;
		return nbReceivedSMS + "";
	}

	/**
	 * Returns the month corresponding to format pattern.
	 * 
	 * @return
	 */
	public String getFormattedMonth() {
		String pattern = "MMM yyyy";
		String result = "N/A";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			result = sdf.format(month);
		} catch (IllegalArgumentException e) {
			logger.warn("Impossible de formater la date", e);
		}

		return result;
	}
}
