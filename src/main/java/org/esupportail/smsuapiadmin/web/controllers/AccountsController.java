/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.springframework.beans.factory.annotation.Autowired;

import org.apache.commons.lang.StringUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.domain.DomainService;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;

/**
 * AccountsQuotaController is the controller for all actions on pages about
 * accounts quota.
 * 
 * @author MZRL3760
 * 
 */
@Path("/accounts")
@RolesAllowed("FCTN_GESTION_CPT_IMPUT")
public class AccountsController {
	
	@Autowired
	private DomainService domainService;

        @SuppressWarnings("unused")
	private final Logger logger = new LoggerImpl(getClass());

	@GET
	@Produces("application/json")
	public List<UIAccount> getAccounts() {
		return domainService.getAccounts();
	}

	@POST
	public void create(UIAccount account) {
		checkMandatoryUIParameters(account);
		domainService.addAccount(account);
	}

	@PUT
	@Path("/{id:\\d+}")
	public void modify(@PathParam("id") int id, UIAccount account) {
		account.setId(id);
		checkMandatoryUIParameters(account);
		domainService.updateAccount(account);
	}


	/**
	 * Check the values.
	 * 
	 * @return true if all mandatory parameters are filled 
	 */
	private void checkMandatoryUIParameters(UIAccount account) {
		String name = account.getName();
		Long quota = account.getQuota();

		if (StringUtils.isBlank(name))
			throw new InvalidParameterException("ACCOUNT.ERROR.INVALIDNAME");
		if (quota == null || quota < 0)
			throw new InvalidParameterException("ACCOUNT.ERROR.INVALIDQUOTA");
	}

}
