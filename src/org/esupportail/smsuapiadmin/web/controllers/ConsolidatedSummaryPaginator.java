package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import org.esupportail.commons.web.beans.ListPaginator;
import org.esupportail.smsuapiadmin.domain.DomainService;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;

/**
 * 
 * @author MZRL3760
 * 
 */
public class ConsolidatedSummaryPaginator extends ListPaginator<UIStatistic> {

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
	private List<UIStatistic> data;

	// ////////////////////////////////////////////////////////////
	// Constructors
	// ////////////////////////////////////////////////////////////
	/**
	 * Constructor.
	 * 
	 * @param domainService
	 */
	@SuppressWarnings("deprecation")
	public ConsolidatedSummaryPaginator(final DomainService domainService) {
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
	protected List<UIStatistic> getData() {
		return data;
	}

	/**
	 * Setter for 'data'.
	 * 
	 * @param data
	 */
	public void setData(final List<UIStatistic> data) {
		this.data = data;
	}

	/**
	 * @param domainService
	 */
	public void setDomainService(final DomainService domainService) {
		this.domainService = domainService;
	}

	/**
	 * @return domainService
	 */
	public DomainService getDomainService() {
		return domainService;
	}

}
