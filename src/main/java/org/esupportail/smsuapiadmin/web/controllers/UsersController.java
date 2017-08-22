/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;


@Path("/users")
@RolesAllowed("FCTN_MANAGE_USERS")
public class UsersController {
	
	@Inject
	private UserManager userManager;

        @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

	@GET
	@Produces("application/json")
	public List<UIUser> getUsers() {
		return userManager.getUsers();
	}

	@POST
	public void create(UIUser user) {
		userManager.addUser(user);
	}

	@PUT
	@Path("/{id:\\d+}")
	public void modify(@PathParam("id") int id, UIUser user) {
        user.id = "" + id;
        user.login = user.login.trim();
		validateLogin(user, user.login);
		userManager.updateUser(user);
	}

	@DELETE
	@Path("/{id:\\d+}")
	public void delete(@PathParam("id") int id) {
		userManager.delete(id);
	}

	/**
	 * Validates the login field.
	 */
        public void validateLogin(UIUser user, String login) {

		if (!login.equals(user.login)) {
			boolean inUse = userManager.loginAlreadyUsed(login);
			if (inUse) throw new InvalidParameterException("USER.ERROR.LOGINUSED");
		}
	}

}
