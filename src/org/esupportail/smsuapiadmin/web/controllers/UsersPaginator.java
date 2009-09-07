package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import org.esupportail.commons.web.beans.ListPaginator;
import org.esupportail.smsuapiadmin.domain.DomainService;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * UsersPaginator if the paginator of users.
 * 
 * @author MZRL3760
 * 
 */
public class UsersPaginator extends ListPaginator<UIUser> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5929348481008113376L;

	/**
	 * The domain service.
	 */
	private DomainService domainService;

	/**
	 * The currentUser parameter.
	 */
	private UIUser currentUser;
	
	// ////////////////////////////////////////////////////////////
	// Constructors
	// ////////////////////////////////////////////////////////////
	/**
	 * Constructor.
	 * 
	 * @param domainService
	 */
	@SuppressWarnings("deprecation")
	public UsersPaginator(final DomainService domainService) {
		super(null, 0);
		this.setDomainService(domainService);
	}

	/**
	 * Constructor.
	 * 
	 * @param domainService
	 * @param currentUser
	 */
	@SuppressWarnings("deprecation")
	public UsersPaginator(final DomainService domainService, final UIUser currentUser) {
		super(null, 0);
		this.setDomainService(domainService);
		this.currentUser = currentUser;
	}
	
	// ////////////////////////////////////////////////////////////
	// Principal method getData()
	// ////////////////////////////////////////////////////////////
	/**
	 * @see org.esupportail.commons.web.beans.ListPaginator#getData()
	 */
	@Override
	protected List<UIUser> getData() {
		return domainService.getUsers(this.currentUser);
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
	
	/**
	 * @return currentUser
	 */
	public UIUser getCurrentUser() {
		return currentUser;
	}

	/**
	 * @param currentUser
	 */
	public void setCurrentUser(final UIUser currentUser) {
		this.currentUser = currentUser;
	}
}
