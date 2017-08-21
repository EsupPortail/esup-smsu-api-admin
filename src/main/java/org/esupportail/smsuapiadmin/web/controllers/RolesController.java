/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.RoleManager;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;


@Path("/roles")
public class RolesController {
	
	@Inject
	private RoleManager roleManager;

        @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

	@GET
	@Produces("application/json")
	public List<UIRole> getRoles() {
		return roleManager.getAllUIRoles();
	}

}
