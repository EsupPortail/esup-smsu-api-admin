package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapi.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * AccountManager is the business layer between the web and the database for
 * 'account' objects.
 * 
 */
public class InstitutionManager {

	private final Logger logger = new LoggerImpl(getClass());

	@Autowired private DaoService daoService;
	@Autowired private DTOConverterService dtoConverterService;

	/**
	 * Retrieves all the institutions defined in database.
	 * 
	 * @return
	 */
	public List<Institution> getAllInstitutions() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve the institutions from the database");
		}
		List<Institution> allInstitutions = daoService.getInstitutions();
		return allInstitutions;
	}

	/**
	 * Returns all institutions as UIObject.
	 * 
	 * @return
	 */
	public List<String> getAllUIInstitutions() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieves the institutions from the database");
		}
		List<String> allUiInstitutions = new ArrayList<String>();
		List<Institution> allInstitutions = daoService.getInstitutions();

		for (Institution inst : allInstitutions) {
			String ui = dtoConverterService.convertToUI(inst);
			allUiInstitutions.add(ui);
		}
		return allUiInstitutions;
	}



	/**
	 * Returns the institution with the specified id.
	 * 
	 * @param institutionId
	 * @return
	 */
	public Institution getInstitutionById(final String institutionId) {
		Institution result = null;

		try {
			int id = Integer.parseInt(institutionId);
			result = daoService.getInstitutionById(id);
		} catch (NumberFormatException e) {
			logger.warn("L'identifiant d'une institution doit etre un entier",
					e);
		}

		return result;
	}

}
