package org.esupportail.smsuapiadmin.business;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;

/**
 * Class 'DetailedSummaryDataSource' is the data source to fill detailed
 * summaries.
 * 
 * @author MZRL3760
 * 
 */
public class DetailedSummaryDataSource implements JRDataSource {

	/**
	 * statistics.
	 */
	private List<UIDetailedSummary> summaries;

	/**
	 * index.
	 */
	private int index = -1;

	/**
	 * Default constructor.
	 * 
	 * @param allStatistics
	 */
	public DetailedSummaryDataSource(final List<UIDetailedSummary> allSummaries) {
		summaries = allSummaries;
	}

	public Object getFieldValue(final JRField field) throws JRException {
		Object result = null;

		UIDetailedSummary summary = summaries.get(index);
		if (field.getName().equals("INSTITUTION")) {
			result = summary.getApplication().getInstitution().getName();
		} else if (field.getName().equals("APPLICATION")) {
			result = summary.getApplication().getName();
		} else if (field.getName().equals("ACCOUNT")) {
			result = summary.getAccount().getName();
		} else if (field.getName().equals("DATE")) {
			result = summary.getFormattedDate();
		} else if (field.getName().equals("IN_PROGRESS_COUNT")) {
			result = summary.getNbInProgress();
		} else if (field.getName().equals("DELIVERED_COUNT")) {
			result = summary.getNbDelivered();
		} else if (field.getName().equals("ERRORS")) {
			result = summary.getErrors();
		} else if (field.getName().equals("SMS_COUNT")) {
			result = summary.getSMSCount();
		}

		return result;
	}

	public boolean next() throws JRException {
		index++;
		return index < summaries.size();
	}

}
