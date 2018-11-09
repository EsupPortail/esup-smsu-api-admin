/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.UserManager;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping(value = "/users")
@RolesAllowed("FCTN_MANAGE_USERS")
public class UsersController {
	
	@Inject
	private UserManager userManager;

        @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	public List<UIUser> getUsers() {
		return userManager.getUsers();
	}

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
	public void create(@RequestBody UIUser user) {
		userManager.addUser(user);
	}

    @RequestMapping(method = RequestMethod.PUT, value = "/{id:\\d+}")
    @ResponseBody
	public void modify(@PathVariable("id") int id, @RequestBody UIUser user) {
        user.id = "" + id;
        user.login = user.login.trim();
		validateLogin(user, user.login);
		userManager.updateUser(user);
	}

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:\\d+}")
    @ResponseBody
	public void delete(@PathVariable("id") int id) {
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
