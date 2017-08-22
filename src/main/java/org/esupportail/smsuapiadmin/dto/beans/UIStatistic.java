package org.esupportail.smsuapiadmin.dto.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

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
	private final Logger logger = Logger.getLogger(getClass());

	private Date month;
	public long nbSendedSMS;
	public long nbSMSInError;
	public String accountName;
	public String appName;
	public String institution;

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
