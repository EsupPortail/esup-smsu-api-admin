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
import org.springframework.beans.factory.annotation.Autowired;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;


@Path("/users")
@RolesAllowed("FCTN_MANAGE_USERS")
public class UsersController {
	
	@Autowired
	private UserManager userManager;

        @SuppressWarnings("unused")
	private final Logger logger = new LoggerImpl(getClass());

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
		user.setId("" + id);
		validateLogin(user, user.getLogin());
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

		if (!login.equals(user.getLogin())) {
			boolean inUse = userManager.loginAlreadyUsed(login);
			if (inUse) throw new InvalidParameterException("USER.ERROR.LOGINUSED");
		}
	}

}
