package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import org.esupportail.commons.web.beans.ListPaginator;
import org.esupportail.smsuapiadmin.domain.DomainService;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;

/**
 * 
 * @author MZRL3760
 * 
 */
public class DetailedSummaryPaginator extends ListPaginator<UIDetailedSummary> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5929348481008113376L;

	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * Data to display.
	 */
	private List<UIDetailedSummary> data;

	// ////////////////////////////////////////////////////////////
	// Constructors
	// ////////////////////////////////////////////////////////////
	/**
	 * Constructor.
	 * 
	 * @param domainService
	 */
	@SuppressWarnings("deprecation")
	public DetailedSummaryPaginator(final DomainService domainService) {
		super(null, 0);
		this.setDomainService(domainService);
	}

	// ////////////////////////////////////////////////////////////
	// Principal method getData()
	// ////////////////////////////////////////////////////////////
	/**
	 * @see org.esupportail.commons.web.beans.ListPaginator#getData()
	 */
	@Override
	protected List<UIDetailedSummary> getData() {
		return data;
	}

	/**
	 * Setter for 'data'.
	 * 
	 * @param data
	 */
	public void setData(final List<UIDetailedSummary> data) {
		this.data = data;
	}

	/**
	 * Setter for 'domainService'.
	 * 
	 * @param domainService
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

}
