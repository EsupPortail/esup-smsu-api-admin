package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.UserBoSmsu;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UserManager is the business layer between the web and the database for 'user'
 * objects.
 * 
 */
public class UserManager {

	private final Logger logger = Logger.getLogger(getClass());

	@Autowired private DaoService daoService;
	@Autowired private DTOConverterService dtoConverterService;

	/**
	 * Returns the user with the specified id.
	 * 
	 * @param id
	 * @return
	 */
	public UIUser getUserById(final Integer id) {
		logger.info("Recherche du user : id=" + id);
		UserBoSmsu user = daoService.getUserById(id);
		return dtoConverterService.convertToUI(user);
	}

	/**
	 * Returns the user with the specified login.
	 * 
	 * @param login
	 * @return
	 */
	public UIUser getUserByLogin(final String login) {
		logger.info("Recherche du user : login=" + login);
		UserBoSmsu user = daoService.getUserByLogin(login);
		if (user != null) {
			return dtoConverterService.convertToUI(user);
		} else {
			// do not return null user, otherwise null is returned which angularjs will take as a failure
			return new UIUser(login);
		}
	}

	/**
	 * Returns a list containing all users.
	 * 
	 * @return
	 */
	public List<UIUser> getUsers() {
			logger.debug("Retrieves the accounts from the database");
		
		List<UIUser> allUIUsers = new ArrayList<>();
		List<UserBoSmsu> allUsers = daoService.getUsers();

		for (UserBoSmsu user : allUsers) {
			UIUser ui = dtoConverterService.convertToUI(user);
			allUIUsers.add(ui);
		}
		return allUIUsers;
	}

	/**
	 * Returns a list containing all users.
	 * 
	 * @return
	 */
	public List<UIUser> getUsers(final UIUser currentUser) {
			logger.debug("Retrieves the accounts from the database");
		
		List<UIUser> allUIUsers = new ArrayList<>();

		for (UserBoSmsu user : daoService.getUsers()) {
			boolean isDeletable = true;
			boolean isUpdateable = true;
		
			UIUser ui = dtoConverterService.convertToUI(user);
			
			if (currentUser != null) {
				if (ui.getLogin().trim().equals(currentUser.getLogin().trim())) {
				isDeletable = isUpdateable = false;
				}
			}
			
			ui.setIsDeletable(isDeletable);
			ui.setIsUpdateable(isUpdateable);
		
			allUIUsers.add(ui);
		}
		return allUIUsers;
	}

	/**
	 * Updates the user.
	 * 
	 * @param uiUser
	 */
	public void updateUser(final UIUser uiUser) {

		UserBoSmsu user = dtoConverterService.convertFromUI(uiUser, false);

		Integer id = Integer.valueOf(uiUser.getId());

		UserBoSmsu userPersistent = daoService.getUserById(id);
		userPersistent.setLogin(uiUser.getLogin());
		userPersistent.setRole(user.getRole());

		daoService.updateUser(userPersistent);
	}

	/**
	 * Adds the user in database.
	 * 
	 * @param uiUser
	 */
	public void addUser(final UIUser uiUser) {
		UserBoSmsu user = dtoConverterService.convertFromUI(uiUser, true);
		daoService.addUser(user);
	}

	/**
	 * Deletes the user from the database.
	 * 
	 * @param uiUser
	 */
	public void delete(int id) {
		UserBoSmsu userPersistent = daoService.getUserById(id);
		daoService.deleteUser(userPersistent);
	}

	/**
	 * Returns true if the login is already used.
	 * 
	 * @param login
	 * @return
	 */
	public boolean loginAlreadyUsed(final String login) {
		UIUser user = getUserByLogin(login);
		return user != null;
	}

	public Set<String> getUserRights(String login) {
		UserBoSmsu user = daoService.getUserByLogin(login);
		if (user == null) return new java.util.TreeSet<String>();
		return dtoConverterService.convert(user.getRole().getFonctions());
	}

}
