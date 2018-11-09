/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.RoleManager;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/roles")
public class RolesController {
	
	@Inject
	private RoleManager roleManager;

        @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

   	@RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	public List<UIRole> getRoles() {
		return roleManager.getAllUIRoles();
	}

}
