package org.esupportail.smsuapiadmin.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.dao.DaoService;
import org.esupportail.smsuapiadmin.dao.beans.Role;
import org.esupportail.smsuapiadmin.dto.DTOConverterService;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import javax.inject.Inject;

/**
 * RoleManager is the business layer between the web and the database for
 * 'role' objects.
 * 
 */
public class RoleManager {

	private final Logger logger = Logger.getLogger(getClass());

	@Inject private DaoService daoService;
	@Inject private DTOConverterService dtoConverterService;

	/**
	 * Returns all institutions as UIObject.
	 * 
	 * @return
	 */
	public List<UIRole> getAllUIRoles() {
			logger.debug("Retrieves the roles from the database");
		
		List<UIRole> allUIRoles = new ArrayList<>();
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
