package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapi.dao.beans.Account;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import javax.inject.Inject;

/**
 * AccountManager is the business layer between the web and the database for
 * 'account' objects.
 * 
 */
public class AccountManager {

	private final Logger logger = Logger.getLogger(getClass());

	@Inject private DaoService daoService;
	@Inject private DTOConverterService dtoConverterService;

	/**
	 * Returns all applications as UIObject.
	 * 
	 * @return
	 */
	public List<UIAccount> getAllUIAccounts() {
			logger.debug("Retrieves the accounts from the database");
		List<UIAccount> allUiAccounts = new ArrayList<>();
		List<Account> allAccounts = daoService.getAccounts();

		for (Account acc : allAccounts) {
			UIAccount ui = dtoConverterService.convertToUI(acc);
			allUiAccounts.add(ui);
		}
		return allUiAccounts;
	}

	/**
	 * Return the account with the specified id.
	 * 
	 * @param accountId
	 * @return
	 */
	public Account getAccountById(final String accountId) {
		Account result = null;

		try {
			int id = Integer.parseInt(accountId);
			result = daoService.getAccountById(id);
		} catch (NumberFormatException e) {
			logger.warn("L'identifiant d'un compte doit etre un entier", e);
		}

		return result;
	}

	/**
	 * Adds the account in the database.
	 * 
	 * @param uiAccount
	 */
	public void addAccount(final UIAccount uiAccount) {
		logger.info("creating account " + uiAccount.name);
		Account account = dtoConverterService.convertFromUI(uiAccount, true);
		daoService.addAccount(account);
	}

	/**
	 * Updates the account.
	 * 
	 * @param uiAccount
	 */
	public void updateAccount(final UIAccount uiAccount) {
		final Account account = dtoConverterService.convertFromUI(uiAccount, false);
		logger.info("modify account " + account.getId() + " " + account.getLabel());

		Account accountPersistent = daoService.getAccountById(account.getId());
		accountPersistent.setLabel(account.getLabel());
		accountPersistent.setQuota(account.getQuota());
		daoService.updateAccount(accountPersistent);
	}

}
