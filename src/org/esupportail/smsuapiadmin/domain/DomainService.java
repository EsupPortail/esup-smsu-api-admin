/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.domain;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

import net.sf.jasperreports.engine.JRException;

import org.esupportail.commons.exceptions.ConfigException;
import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.commons.services.application.Version;
import org.esupportail.smsuapiadmin.business.FormatReport;
import org.esupportail.smsuapiadmin.business.UpdateAccountsQuotaException;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;
import org.esupportail.smsuapiadmin.dto.beans.UIInstitution;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * The domain service interface.
 */
public interface DomainService extends Serializable {

	// ////////////////////////////////////////////////////////////
	// User
	// ////////////////////////////////////////////////////////////

	/**
	 * @param id
	 * @return the User instance that corresponds to an id.
	 * @throws UserNotFoundException
	 */
	UIUser getUserById(Integer id) throws UserNotFoundException;

	/**
	 * Returns the user with the specified 'login'.
	 * 
	 * @param id
	 * @return
	 */
	UIUser getUserByLogin(String id);

	/**
	 * @return the list of all the users.
	 */
	List<UIUser> getUsers();

	/**
	 * @return the list of all the users.
	 */
	List<UIUser> getUsers(UIUser currentUser);
	
	/**
	 * Creates a new user.
	 * 
	 * @param user
	 */
	void addUser(UIUser user);

	/**
	 * Update a user.
	 * 
	 * @param user
	 */
	void updateUser(UIUser user);

	/**
	 * Deletes a user.
	 * 
	 * @param user
	 */
	void deleteUser(UIUser user);

	// ////////////////////////////////////////////////////////////
	// VersionManager
	// ////////////////////////////////////////////////////////////

	/**
	 * @return the database version.
	 * @throws ConfigException
	 *             when the database is not initialized
	 */
	Version getDatabaseVersion() throws ConfigException;

	/**
	 * Set the database version.
	 * 
	 * @param version
	 */
	void setDatabaseVersion(Version version);

	/**
	 * Set the database version.
	 * 
	 * @param version
	 */
	void setDatabaseVersion(String version);

	// ////////////////////////////////////////////////////////////
	// Authorizations
	// ////////////////////////////////////////////////////////////

	/**
	 * 
	 * @return a list containing all the applications
	 */
	List<UIApplication> getApplications();

	/**
	 * Adds the application in the database.
	 * 
	 * @param uiApplication
	 */
	void addApplication(UIApplication uiApplication);

	/**
	 * Updates the application in the database.
	 * 
	 * @param application
	 */
	void updateApplication(UIApplication application);

	/**
	 * Deletes the application from the database.
	 * 
	 * @param application
	 */
	void deleteApplication(UIApplication application);

	/**
	 * Makes an Excel file that contains informations about accounts.
	 * 
	 * @throws JRException
	 * @throws IOException
	 */
	byte[] makeXLSReportForAccounts() throws IOException, JRException;

	/**
	 * Updates accounts quota with informations of the XML file.
	 * 
	 * @param importFile
	 * @throws UpdateAccountsQuotaException
	 */
	void updateAccountsQuota(InputStream importFile)
			throws UpdateAccountsQuotaException;

	/**
	 * Makes a PDF report about quotas.
	 * 
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	byte[] makePDFReportForAccounts() throws JRException, IOException;

	/**
	 * 
	 * @return a list containing all the accounts
	 */
	List<UIAccount> getAccounts();

	/**
	 * 
	 * @return a list containing all the institutions
	 */
	List<UIInstitution> getInstitutions();

	/**
	 * 
	 * @return a list containing all the statistics
	 */
	List<UIStatistic> getStatistics();

	/**
	 * 
	 * @return a set containing all the months of statistics
	 */
	SortedSet<Date> getMonthsOfStatistics();

	/**
	 * Searches statistics matching criterias.
	 * 
	 * @param institutionId
	 * @param accountId
	 * @param applicationId
	 * @param month
	 * @return list containing all statistics matching criterias
	 */
	List<UIStatistic> searchStatistics(String institutionId, String accountId,
			String applicationId, String month);

	/**
	 * Returns a report for consolidated summaries.
	 * 
	 * @param format
	 * @param data
	 * @param institutionId
	 * @param applicationId
	 * @param accountId
	 * @param monthStr
	 * @return
	 * @throws IOException
	 * @throws JRException
	 */
	byte[] makeReportForConsolidatedSummaries(FormatReport format,
			List<UIStatistic> data, String institutionId, String applicationId,
			String accountId, String monthStr) throws IOException, JRException;

	/**
	 * Searches detailed summaries matching criterias.
	 * 
	 * @param institutionId
	 * @param accountId
	 * @param applicationId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<UIDetailedSummary> searchDetailedSummaries(String institutionId,
			String accountId, String applicationId, Date startDate, Date endDate);

	/**
	 * Returns a report for detailed summaries.
	 * 
	 * @param xls
	 * @param data
	 * @param institutionId
	 * @param applicationId
	 * @param accountId
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	byte[] makeReportForDetailedSummaries(FormatReport xls,
			List<UIDetailedSummary> data, String institutionId,
			String applicationId, String accountId, Date startDate, Date endDate)
			throws IOException, JRException;

	/**
	 * Returns all the roles.
	 * 
	 * @return
	 */
	List<UIRole> getAllRoles();

	/**
	 * Returns true if the login is already used.
	 * 
	 * @param strValue
	 * @return
	 */
	boolean loginAlreadyUsed(String login);

}
