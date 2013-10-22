/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.domain;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.Assert;
import org.esupportail.smsuapiadmin.business.AccountManager;
import org.esupportail.smsuapiadmin.business.ApplicationManager;
import org.esupportail.smsuapiadmin.business.InstitutionManager;
import org.esupportail.smsuapiadmin.business.NotFoundException;
import org.esupportail.smsuapiadmin.business.RoleManager;
import org.esupportail.smsuapiadmin.business.StatisticManager;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Account;
import org.esupportail.smsuapiadmin.dao.beans.Application;
import org.esupportail.smsuapiadmin.dao.beans.Institution;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedCriteria;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;
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

	public void deleteUser(int id) {
		userManager.delete(id);
	}

	public Set<EnumeratedFunction> getUserFunctions(String login) {
		return userManager.getUserFunctions(login);
	}

	/**
	 * Setter for 'userManager'.
	 */
	public void setUserManager(final UserManager userManager) {
		this.userManager = userManager;
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

	public UIApplication getApplication(int id) {
		return applicationManager.getUIApplication(id);
	}

	public void addApplication(final UIApplication uiApplication) {
		applicationManager.addApplication(uiApplication);
	}

	public void updateApplication(final UIApplication uiApplication) {
		applicationManager.updateApplication(uiApplication);
	}

	public void deleteApplication(int id) {
		applicationManager.deleteApplication(id);
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

	public List<UIAccount> getAccounts() {
		return accountManager.getAllUIAccounts();
	}

	public void addAccount(final UIAccount uiAccount) {
		accountManager.addAccount(uiAccount);
	}

	public void updateAccount(final UIAccount uiAccount) {
		accountManager.updateAccount(uiAccount);
	}

	public List<String> getInstitutions() {
		return institutionManager.getAllUIInstitutions();
	}

	public List<UIStatistic> getStatistics() {
		return statisticManager.getAllUIStatistics();
	}

	public List<UIStatistic> getStatisticsSorted() {
		return statisticManager.getStatisticsSorted();
	}

	public List<UIDetailedCriteria> getDetailedStatisticsCriteria() {
		return statisticManager.getDetailedStatisticsCriteria();
	}

	public List<UIDetailedSummary> searchDetailedSummaries(
			final String institution, final String accountLabel,
			final String applicationName, final Date startDate, final Date endDate, int maxResults) throws Exception {
		Institution inst = null;
		if (institution != null) {
			inst = institutionManager.getInstitutionByName(institution);
			if (inst == null) throw new NotFoundException("invalid institution " + institution);
		}
		Account acc = null;
		if (accountLabel != null) {
			acc = daoService.getAccountByName(accountLabel);
			if (acc == null) throw new NotFoundException("invalid account " + accountLabel);
		}
		Application app = null;
		if (applicationName != null) {
			app = daoService.getApplicationByName(applicationName);
			if (app == null) throw new NotFoundException("invalid application " + applicationName);
		}
		return statisticManager.searchDetailedSummaries(inst, acc, app, startDate, endDate, maxResults);
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
