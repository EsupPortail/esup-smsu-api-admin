package org.esupportail.smsuapiadmin.business;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;

/**
 * Class 'ConsolidatedSummaryDataSource' is the data source to fill consolidated
 * summaries.
 * 
 * @author MZRL3760
 * 
 */
public class ConsolidatedSummaryDataSource implements JRDataSource {

	/**
	 * statistics.
	 */
	private List<UIStatistic> statistics;

	/**
	 * index.
	 */
	private int index = -1;

	/**
	 * Default constructor.
	 * 
	 * @param allStatistics
	 */
	public ConsolidatedSummaryDataSource(final List<UIStatistic> allStatistics) {
		statistics = allStatistics;
	}

	public Object getFieldValue(final JRField field) throws JRException {
		Object result = null;

		UIStatistic stat = statistics.get(index);
		if (field.getName().equals("INSTITUTION")) {
			result = stat.getApplication().getInstitution().getName();
		} else if (field.getName().equals("APPLICATION")) {
			result = stat.getApplication().getName();
		} else if (field.getName().equals("ACCOUNT")) {
			result = stat.getAccount().getName();
		} else if (field.getName().equals("MONTH")) {
			result = stat.getFormattedMonth();
		} else if (field.getName().equals("SENDEDSMS")) {
			result = stat.getNbSendedSMS() + "";
		} else if (field.getName().equals("RECEIVEDSMS")) {
			result = stat.getNbReceivedSMS() + "";
		} else if (field.getName().equals("FAILRATE")) {
			result = stat.getFailRate();
		}

		return result;
	}

	public boolean next() throws JRException {
		index++;
		return index < statistics.size();
	}

}
