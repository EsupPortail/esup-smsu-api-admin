package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import org.esupportail.commons.web.beans.ListPaginator;
import org.esupportail.smsuapiadmin.domain.DomainService;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
/**
 * @author xphp8691
 *
 */
public class ApplicationsPaginator extends ListPaginator<UIApplication> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5929348481008113376L;
	
	/**
	 * The domain service.
	 */
	private DomainService domainService;
	
	
	 //////////////////////////////////////////////////////////////
	 // Constructors
	 //////////////////////////////////////////////////////////////
	 /**
	 * Constructor.
	 * @param domainService 
	 */
	@SuppressWarnings("deprecation")
	public ApplicationsPaginator(final DomainService domainService) {
		super(null, 0);
		this.setDomainService(domainService);
	}

	//////////////////////////////////////////////////////////////
	// Principal method getData()
	//////////////////////////////////////////////////////////////
	/**
	 * @see org.esupportail.commons.web.beans.ListPaginator#getData()
	 */
	@Override
	protected List<UIApplication> getData() {
		return domainService.getApplications();
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
