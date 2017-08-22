package org.esupportail.smsuapiadmin.dto.beans;

import java.util.Date;
import java.util.Map;

import org.esupportail.smsuapi.domain.beans.sms.SmsStatus;


/**
 * UIDetailedSummary.
 * 
 * @author MZRL3760
 * 
 */
public class UIDetailedSummary implements Comparable<UIDetailedSummary> {

	public String institution;
	/**
	 * application.
	 */
	public String appName;
	public String accountName;
	public Date date;

	private Map<SmsStatus, Integer> statistics;

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
				detail += (detail.equals("") ? "" : ", ") + nb + " " + ("SMS.STATUS." + state + ".NAME");
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
		return date.compareTo(arg0.date);
	}
}
