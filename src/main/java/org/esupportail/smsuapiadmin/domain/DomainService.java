/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.esupportail.commons.exceptions.UserNotFoundException;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.dto.beans.UIAccount;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;
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
	void deleteUser(int id);

        Set<EnumeratedFunction> getUserFunctions(String login);

	// ////////////////////////////////////////////////////////////
	// Authorizations
	// ////////////////////////////////////////////////////////////

	/**
	 * 
	 * @return a list containing all the applications
	 */
	List<UIApplication> getApplications();

	UIApplication getApplication(int id);

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
	void deleteApplication(int id);

	/**
	 * 
	 * @return a list containing all the accounts
	 */
	List<UIAccount> getAccounts();

	/**
	 * Adds the account in the database.
	 * 
	 * @param uiAccount
	 */
	void addAccount(UIAccount uiAccount);

	/**
	 * Updates the account in the database.
	 * 
	 * @param account
	 */
	void updateAccount(UIAccount account);

	/**
	 * 
	 * @return a list containing all the institutions
	 */
	List<String> getInstitutions();

	/**
	 * 
	 * @return a sorted list containing all the statistics
	 */
	List<UIStatistic> getStatisticsSorted();

	/**
	 * Searches detailed summaries matching criterias.
	 * 
	 * @param institution
	 * @param accountId
	 * @param applicationId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<UIDetailedSummary> searchDetailedSummaries(String institution,
							Long accountId, Long applicationId, Date startDate, Date endDate, int maxResults) throws Exception;

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
