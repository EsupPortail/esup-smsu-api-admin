package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * RoleManager is the business layer between the web and the database for
 * 'role' objects.
 * 
 */
public class RoleManager {

	private final Logger logger = new LoggerImpl(getClass());

	@Autowired private DaoService daoService;
	@Autowired private DTOConverterService dtoConverterService;

	/**
	 * Retrieves all the institutions defined in database.
	 * 
	 * @return
	 */
	public List<Role> getAllRoles() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve the roles from the database");
		}
		List<Role> allRoles = daoService.getRoles();
		return allRoles;
	}

	/**
	 * Returns all institutions as UIObject.
	 * 
	 * @return
	 */
	public List<UIRole> getAllUIRoles() {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieves the roles from the database");
		}
		List<UIRole> allUIRoles = new ArrayList<UIRole>();
		List<Role> allRoles = daoService.getRoles();
		
		logger.debug("Nombre de roles de la liste provenant du dao : "
				+ allRoles.size());

		for (Role role : allRoles) {
			UIRole uiRole = dtoConverterService.convertToUI(role);
			allUIRoles.add(uiRole);
		}
		
		logger.debug("Envoi de la liste de roles pour le domaine (nb de roles : "
				+ allUIRoles.size() + ")");
		
		return allUIRoles;
	}

}
