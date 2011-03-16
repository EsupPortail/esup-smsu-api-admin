package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Institution;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UIInstitution;

/**
 * AccountManager is the business layer between the web and the database for
 * 'account' objects.
 * 
 * @author MZRL3760
 * 
 */
@SuppressWarnings("serial")
public class InstitutionManager extends AbstractApplicationAwareBean {

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
	public InstitutionManager() {
		super();
	}

	/**
	 * Setter for 'daoService'.
	 * 
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
	public List<UIInstitution> getAllUIInstitutions() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieves the institutions from the database");
		}
		List<UIInstitution> allUiInstitutions = new ArrayList<UIInstitution>();
		List<Institution> allInstitutions = daoService.getInstitutions();

		for (Institution inst : allInstitutions) {
			UIInstitution ui = dtoConverterService.convertToUI(inst);
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
