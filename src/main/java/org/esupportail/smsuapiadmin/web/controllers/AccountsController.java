/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
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
import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.AccountManager;
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
	private AccountManager accountManager;

        @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

	@GET
	@Produces("application/json")
	public List<UIAccount> getAccounts() {
		return accountManager.getAllUIAccounts();
	}

	@POST
	public void create(UIAccount account) {
		checkMandatoryUIParameters(account);
		accountManager.addAccount(account);
	}

	@PUT
	@Path("/{id:\\d+}")
	public void modify(@PathParam("id") int id, UIAccount account) {
		account.setId(id);
		checkMandatoryUIParameters(account);
		accountManager.updateAccount(account);
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
