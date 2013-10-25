package org.esupportail.smsuapiadmin.dto.beans;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.esupportail.commons.services.i18n.I18nService;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.beans.SmsStatus;


/**
 * UIDetailedSummary.
 * 
 * @author MZRL3760
 * 
 */
public class UIDetailedSummary implements Comparable<UIDetailedSummary> {

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	private I18nService i18nService;

	/**
	 * institution.
	 */
	private String institution;

	/**
	 * application.
	 */
	private String appName;

	/**
	 * account.
	 */
	private String accountName;

	/**
	 * date.
	 */
	private Date date;

	/**
	 * statistics.
	 */
	private Map<SmsStatus, Integer> statistics;

	/**
	 * Default constructor.
	 */
	public UIDetailedSummary(I18nService i18nService) {
		this.i18nService = i18nService;
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
	 * Setter for 'institution'.
	 * 
	 * @param institution
	 */
	public void setInstitution(final String institution) {
		this.institution = institution;
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
	 * Setter for 'appName'.
	 * 
	 * @param appName
	 */
	public void setAppName(final String appName) {
		this.appName = appName;
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
	 * Setter for 'accountName'.
	 * 
	 * @param accountName
	 */
	public void setAccountName(final String accountName) {
		this.accountName = accountName;
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
	 * Returns the month corresponding to format pattern.
	 * 
	 * @return
	 */
	public String formattedDate() {
		String pattern = "dd MMM yyyy HH:mm";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		} catch (IllegalArgumentException e) {
			logger.warn("Impossible de formater la date", e);
			return "N/A";
		}
	}

	/**
	 * Setter for 'date'.
	 * 
	 * @param date
	 */
	public void setDate(final Date date) {
		this.date = date;
	}

	/**
	 * Setter for 'statistics'.
	 * 
	 * @param statistics
	 */
	public void setStatistics(final Map<SmsStatus, Integer> statistics) {
		this.statistics = statistics;
	}

	public Integer getNbDelivered() {
		return statistics.get(SmsStatus.DELIVERED);
	}

	public String getErrors() {
		String detail = "";
		int total = 0;
		SmsStatus[] states = { SmsStatus.ERROR, SmsStatus.ERROR_POST_BL, SmsStatus.ERROR_PRE_BL, SmsStatus.ERROR_QUOTA, SmsStatus.CREATED };
		for (SmsStatus state : states) {
			int nb = statistics.get(state);
			total += nb;
			if (nb > 0 && state != SmsStatus.ERROR) {
				detail += (detail.equals("") ? "" : ", ") + nb + " " + i18nService.getString("SMS.STATUS." + state + ".NAME");
			}
		}
		return "" + total + (detail.equals("") ? "" : " (dont " + detail + ")");
	}

	public Integer getNbInProgress() {
		return statistics.get(SmsStatus.IN_PROGRESS);
	}

	public Integer getNbSms() {
		int result = 0;
		for (Integer cpt : statistics.values()) {
			result += cpt;
		}
		return result;
	}

	@Override
	public int compareTo(UIDetailedSummary arg0) {
		return date.compareTo(arg0.getDate());
	}
}
