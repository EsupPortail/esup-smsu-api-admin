/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.AccountManager;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AccountsQuotaController is the controller for all actions on pages about
 * accounts quota.
 * 
 * @author MZRL3760
 * 
 */
@Controller
@RequestMapping(value = "/accounts")
@RolesAllowed("FCTN_GESTION_CPT_IMPUT")
public class AccountsController {
	
	@Inject
	private AccountManager accountManager;

    @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	public List<UIAccount> getAccounts() {
		return accountManager.getAllUIAccounts();
	}

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
	public void create(@RequestBody UIAccount account) {
		checkMandatoryUIParameters(account);
		accountManager.addAccount(account);
	}

    @RequestMapping(method = RequestMethod.PUT, value = "/{id:\\d+}")
    @ResponseBody
	public void modify(@PathVariable("id") int id, @RequestBody UIAccount account) {
		account.id = id;
		checkMandatoryUIParameters(account);
		accountManager.updateAccount(account);
	}


	/**
	 * Check the values.
	 * 
	 * @return true if all mandatory parameters are filled 
	 */
	private void checkMandatoryUIParameters(UIAccount account) {
		String name = account.name;
		Long quota = account.quota;

		if (StringUtils.isBlank(name))
			throw new InvalidParameterException("ACCOUNT.ERROR.INVALIDNAME");
		if (quota == null || quota < 0)
			throw new InvalidParameterException("ACCOUNT.ERROR.INVALIDQUOTA");
	}

}
