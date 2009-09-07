package org.esupportail.smsuapiadmin.business;

/**
 * Enumeration of all format supported for report.
 * 
 * @author MZRL3760
 * 
 */
public enum FormatReport {
	PDF("PDF"), XLS("Excel");

	/**
	 * label.
	 */
	private String label;

	/**
	 * Default constructor.
	 * 
	 * @param label
	 */
	private FormatReport(final String label) {
		this.label = label;
	}

	/**
	 * Getter for 'label'.
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}

}
