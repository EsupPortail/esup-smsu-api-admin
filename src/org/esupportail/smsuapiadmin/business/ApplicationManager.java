package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.esupportail.commons.beans.AbstractApplicationAwareBean;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Application;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;

/**
 * ApplicationManager is the business layer between the web and the database for
 * 'application' objects.
 * 
 * @author MZRL3760
 * 
 */
@SuppressWarnings("serial")
public class ApplicationManager extends AbstractApplicationAwareBean {

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
	public ApplicationManager() {
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
	 * Retrieves all the applications defined in database.
	 * 
	 * @return
	 */
	public List<Application> getAllApplications() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve the applications from the database");
		}
		List<Application> allApps = daoService.getApplications();
		return allApps;
	}

	/**
	 * Retrieves all the applications defined in database as UIObject.
	 * 
	 * @return
	 */
	public List<UIApplication> getAllUIApplications() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieves the applications from the database");
		}
		List<Application> allApps = daoService.getApplications();
		Map<Integer,Integer> idToCount = daoService.getNbSmsByApplication();

		List<UIApplication> allUiApps = new ArrayList<UIApplication>();
		for (Application app : allApps) {
			UIApplication ui = dtoConverterService.convertToUI(app);
			Integer count = idToCount.get(app.getId());
			ui.setName(ui.getName() + (count == null ? "" : " (" + count + " sms)"));
			allUiApps.add(ui);
		}
		return allUiApps;
	}

	/**
	 * Returns the application with the specified id.
	 * 
	 * @param idStr
	 *            Identifier of the application
	 * @return the application
	 */
	public Application getApplicationById(final String idStr) {
		Application result = null;

		try {
			int id = Integer.parseInt(idStr);
			result = daoService.getApplicationById(id);
		} catch (NumberFormatException e) {
			logger.warn("L'identifiant d'une application doit etre un entier",
					e);
		}

		return result;
	}

	/**
	 * Adds the application in the database.
	 * 
	 * @param uiApp
	 */
	public void addApplication(final UIApplication uiApp) {
		final Application app;
		// on convertit en application
		app = dtoConverterService.convertFromUI(uiApp);
		// on l'ajoute en base
		daoService.addApplication(app);
	}

	/**
	 * Updates the application.
	 * 
	 * @param uiApplication
	 */
	public void updateApplication(final UIApplication uiApplication) {
		final Application app;

		app = dtoConverterService.convertFromUI(uiApplication);

		String idStr = uiApplication.getId();
		Integer id = Integer.valueOf(idStr);

		Application appPersistent = daoService.getApplicationById(id);
		appPersistent.setName(app.getName());
		appPersistent.setCertificate(app.getCertificate());
		appPersistent.setAccount(app.getAccount());
		appPersistent.setInstitution(app.getInstitution());
		appPersistent.setQuota(app.getQuota());

		daoService.updateApplication(appPersistent);
	}

	/**
	 * Deletes the application.
	 * 
	 * @param uiApplication
	 */
	public void deleteApplication(final UIApplication uiApplication) {

		String idStr = uiApplication.getId();
		Integer id = Integer.valueOf(idStr);

		Application appPersistent = daoService.getApplicationById(id);
		daoService.deleteApplication(appPersistent);
	}
}
