package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapi.dao.beans.Account;
import org.esupportail.smsuapi.dao.beans.Sms;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UISms;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * SmsManager is the business layer between the web and the database for 'sms'
 * objects.
 * 
 */
public class SmsManager {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired private DaoService daoService;
	@Autowired private DTOConverterService dtoConverterService;

	/**
	 * Retrieves all the accounts defined in database.
	 * 
	 * @return
	 */
	public List<Sms> getAllSms() {
			logger.debug("Retrieve the sms from the database");
		
		List<Sms> allSms = daoService.getSms();
		return allSms;
	}

	/**
	 * Returns all applications as UIObject.
	 * 
	 * @return
	 */
	public List<UISms> getAllUISms() {
			logger.debug("Retrieves the sms from the database");
		
		List<UISms> allUISms = new ArrayList<UISms>();
		List<Sms> allSms = daoService.getSms();

		for (Sms sms : allSms) {
			UISms uiSms = dtoConverterService.convertToUI(sms);
			allUISms.add(uiSms);
		}
		return allUISms;
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

}
