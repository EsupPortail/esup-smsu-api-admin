/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import net.sf.jasperreports.engine.JRException;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.smsuapiadmin.business.AccountManager;
import org.esupportail.smsuapiadmin.business.ApplicationManager;
import org.esupportail.smsuapiadmin.business.FormatReport;
import org.esupportail.smsuapiadmin.business.InstitutionManager;
import org.esupportail.smsuapiadmin.business.RoleManager;
import org.esupportail.smsuapiadmin.business.StatisticManager;
import org.esupportail.smsuapiadmin.business.UpdateAccountsQuotaException;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Account;
import org.esupportail.smsuapiadmin.dao.beans.Application;
import org.esupportail.smsuapiadmin.dao.beans.Institution;
import org.esupportail.smsuapiadmin.domain.beans.VersionManager;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;
import org.esupportail.smsuapiadmin.dto.beans.UIInstitution;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;
import org.springframework.beans.factory.InitializingBean;

/**
 * The basic implementation of DomainService.
 * 
 * See /properties/domain/domain-example.xml
 */
public class DomainServiceImpl implements DomainService, InitializingBean {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -8200845058340254019L;

	/**
	 * {@link DaoService}.
	 */
	private DaoService daoService;

	/**
	 * {@link SecurityManager}.
	 */
	private UserManager userManager;

	/**
	 * The application manager.
	 */
	private ApplicationManager applicationManager;

	/**
	 * The account manager.
	 */
	private AccountManager accountManager;

	/**
	 * The institution manager.
	 */
	private InstitutionManager institutionManager;

	/**
	 * The month manager.
	 */
	private StatisticManager statisticManager;

	/**
	 * The month manager.
	 */
	private RoleManager roleManager;

	/**
	 * Logger de classe.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * Bean constructor.
	 */
	public DomainServiceImpl() {
		super();
	}

	/**
	 * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
	 */
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(this.daoService, "property daoService of class "
				+ this.getClass().getName() + " can not be null");
	}

	// ////////////////////////////////////////////////////////////
	// User
	// ////////////////////////////////////////////////////////////

	public UIUser getUserById(final Integer id) throws UserNotFoundException {
		logger.info("Recherche du user : id=" + id);
		return userManager.getUserById(id);
	}

	public UIUser getUserByLogin(final String login) {
		logger.info("Recherche du user : login=" + login);
		return userManager.getUserByLogin(login);
	}

	/**
	 * @see org.esupportail.smsuapiadmin.domain.DomainService#getUsers()
	 */
	public List<UIUser> getUsers() {
		return userManager.getUsers();
	}

	/**
	 * @see org.esupportail.smsuapiadmin.domain.DomainService#getUsers(beans.UIUser)
	 */
	public List<UIUser> getUsers(final UIUser currentUser) {
		return userManager.getUsers(currentUser);
	}

	
	public void updateUser(final UIUser user) {
		userManager.updateUser(user);
	}

	public void addUser(final UIUser user) {
		userManager.addUser(user);
	}

	public void deleteUser(final UIUser user) {
		userManager.delete(user);
	}

	// ////////////////////////////////////////////////////////////
	// VersionManager
	// ////////////////////////////////////////////////////////////

	/**
	 * Setter for 'userManager'.
	 */
	public void setUserManager(final UserManager userManager) {
		this.userManager = userManager;
	}

	/**
	 * @see org.esupportail.smsuapiadmin.domain.DomainService#getDatabaseVersion()
	 */
	public Version getDatabaseVersion() throws ConfigException {
		VersionManager versionManager = daoService.getVersionManager();
		if (versionManager == null) {
			return null;
		}
		return new Version(versionManager.getVersion());
	}

	/**
	 * @see org.esupportail.smsuapiadmin.domain.DomainService#setDatabaseVersion(java.lang.String)
	 */
	public void setDatabaseVersion(final String version) {
		if (logger.isDebugEnabled()) {
			logger.debug("setting database version to '" + version + "'...");
		}
		VersionManager versionManager = daoService.getVersionManager();
		versionManager.setVersion(version);
		daoService.updateVersionManager(versionManager);
		if (logger.isDebugEnabled()) {
			logger.debug("database version set to '" + version + "'.");
		}
	}

	/**
	 * @see org.esupportail.smsuapiadmin.domain.DomainService
	 *     
	 *     
	 *      #setDatabaseVersion(org.esupportail.commons.services.application.Version)
	 */
	public void setDatabaseVersion(final Version version) {
		setDatabaseVersion(version.toString());
	}

	/**
	 * @param daoService
	 *            the daoService to set
	 */
	public void setDaoService(final DaoService daoService) {
		this.daoService = daoService;
	}

	public List<UIApplication> getApplications() {
		return applicationManager.getAllUIApplications();
	}

	/**
	 * 
	 * @param applicationManager
	 */
	public void setApplicationManager(
			final ApplicationManager applicationManager) {
		this.applicationManager = applicationManager;
	}

	public void addApplication(final UIApplication uiApplication) {
		applicationManager.addApplication(uiApplication);
	}

	public void updateApplication(final UIApplication uiApplication) {
		applicationManager.updateApplication(uiApplication);
	}

	public void deleteApplication(final UIApplication uiApplication) {
		applicationManager.deleteApplication(uiApplication);
	}

	/**
	 * Setter for 'accountManager'.
	 * 
	 * @param accountManager
	 */
	public void setAccountManager(final AccountManager accountManager) {
		this.accountManager = accountManager;
	}

	/**
	 * Setter for 'institutionManager'.
	 * 
	 * @param institutionManager
	 */
	public void setInstitutionManager(
			final InstitutionManager institutionManager) {
		this.institutionManager = institutionManager;
	}

	/**
	 * Getter for 'institutionManager'.
	 * 
	 * @return
	 */
	public InstitutionManager getInstitutionManager() {
		return institutionManager;
	}

	/**
	 * Setter for 'statisticManager'.
	 * 
	 * @param statisticManager
	 */
	public void setStatisticManager(final StatisticManager statisticManager) {
		this.statisticManager = statisticManager;
	}

	/**
	 * Getter for 'statisticManager'.
	 * 
	 * @return
	 */
	public StatisticManager getStatisticManager() {
		return statisticManager;
	}

	public void updateAccountsQuota(final InputStream importFile)
			throws UpdateAccountsQuotaException {
		accountManager.updateAccountsQuota(importFile);
	}

	public byte[] makePDFReportForAccounts() throws JRException, IOException {
		return accountManager.makeAccountPdfReport();
	}

	public byte[] makeXLSReportForAccounts() throws IOException, JRException {
		return accountManager.makeAccountInfosXlsFile();
	}

	public List<UIAccount> getAccounts() {
		return accountManager.getAllUIAccounts();
	}

	public List<UIInstitution> getInstitutions() {
		return institutionManager.getAllUIInstitutions();
	}

	public List<UIStatistic> getStatistics() {
		return statisticManager.getAllUIStatistics();
	}

	public SortedSet<Date> getMonthsOfStatistics() {
		return statisticManager.getMonthsOfStatistics();
	}

	public List<UIStatistic> searchStatistics(final String institutionId,
			final String accountId, final String applicationId,
			final String monthStr) {

		Institution inst = institutionManager.getInstitutionById(institutionId);
		Account acc = accountManager.getAccountById(accountId);
		Application app = applicationManager.getApplicationById(applicationId);
		Date month = null;
		try {
			long date = Long.valueOf(monthStr);
			if (date != -1) {
				month = new Date(date);
			}
		} catch (NumberFormatException e) {
			logger.debug("Impossible de parser la date choisie : "
					+ "on en deduit que l'item 'Tous' a ete selectionne.", e);
		}

		return statisticManager.searchStatistics(inst, acc, app, month);
	}

	public byte[] makeReportForConsolidatedSummaries(final FormatReport format,
			final List<UIStatistic> data, final String institutionId,
			final String applicationId, final String accountId,
			final String monthStr) throws IOException, JRException {

		// on recupere les objets persistents
		Institution inst = institutionManager.getInstitutionById(institutionId);
		Account acc = accountManager.getAccountById(accountId);
		Application app = applicationManager.getApplicationById(applicationId);
		Date month = null;
		try {
			long date = Long.valueOf(monthStr);
			if (date != -1) {
				month = new Date(date);
			}
		} catch (NumberFormatException e) {
			logger.debug("Impossible de parser la date choisie : "
					+ "on en deduit que l'item 'Tous' a ete selectionne.", e);
		}

		return statisticManager.makeReportForConsolidatedSummaries(format,
				data, inst, app, acc, month);
	}

	public byte[] makeReportForDetailedSummaries(final FormatReport format,
			final List<UIDetailedSummary> data, final String institutionId,
			final String applicationId, final String accountId,
			final Date startDate, final Date endDate) throws IOException,
			JRException {
		// on recupere les objets persistents
		Institution inst = institutionManager.getInstitutionById(institutionId);
		Account acc = accountManager.getAccountById(accountId);
		Application app = applicationManager.getApplicationById(applicationId);

		return statisticManager.makeReportForDetailedSummaries(format, data,
				inst, app, acc, startDate, endDate);
	}

	public List<UIDetailedSummary> searchDetailedSummaries(
			final String institutionId, final String accountId,
			final String applicationId, final Date startDate, final Date endDate) {
		// on recupere les objets persistents
		Institution inst = institutionManager.getInstitutionById(institutionId);
		Account acc = accountManager.getAccountById(accountId);
		Application app = applicationManager.getApplicationById(applicationId);

		return statisticManager.searchDetailedSummaries(inst, acc, app,
				startDate, endDate);
	}

	public List<UIRole> getAllRoles() {
		return roleManager.getAllUIRoles();
	}

	/**
	 * Setter for 'roleManager'.
	 * 
	 * @param roleManager
	 */
	public void setRoleManager(final RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	/**
	 * Returns true if the login is used.
	 */
	public boolean loginAlreadyUsed(final String login) {
		return userManager.loginAlreadyUsed(login);
	}

}
