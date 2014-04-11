package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapi.dao.beans.Account;
import org.esupportail.smsuapi.dao.beans.Sms;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UISms;

/**
 * SmsManager is the business layer between the web and the database for 'sms'
 * objects.
 * 
 * @author MZRL3760
 * 
 */
@SuppressWarnings("serial")
public class SmsManager extends AbstractApplicationAwareBean {


	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * {@link DTOConverterService}.
	 */
	private DTOConverterService dtoConverterService;

	/**
	 * constructor.
	 */
	public SmsManager() {
		super();
	}

	/**
	 * @param daoService
	 *            the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	/**
	 * Setter for 'dtoConverterService'.
	 * 
	 * @param dtoConverterService
	 */
	public void setDtoConverterService(
			final DTOConverterService dtoConverterService) {
		this.dtoConverterService = dtoConverterService;
	}

	/**
	 * Retrieves all the accounts defined in database.
	 * 
	 * @return
	 */
	public List<Sms> getAllSms() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve the sms from the database");
		}
		List<Sms> allSms = daoService.getSms();
		return allSms;
	}

	/**
	 * Returns all applications as UIObject.
	 * 
	 * @return
	 */
	public List<UISms> getAllUISms() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieves the sms from the database");
		}
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
