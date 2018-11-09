/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.ApplicationManager;
import org.esupportail.smsuapiadmin.business.NotFoundException;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@RequestMapping(value = "/applications")
@RolesAllowed("FCTN_API_CONFIG_APPLIS")
public class ApplicationsController {
	
	@Inject
	private ApplicationManager applicationManager;

        @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	public List<UIApplication> getApplications() {
		return applicationManager.getAllUIApplications();
	}

    @RequestMapping(method = RequestMethod.GET, value = "/{id:\\d+}")
    @ResponseBody
	public UIApplication getApplication(@PathVariable("id") int id) {
		UIApplication app = applicationManager.getUIApplication(id);
		if (app == null) throw new NotFoundException("invalid application " + id); 
		return app;
	}

    @RequestMapping(method = RequestMethod.POST)
	public void create(UIApplication application) {
		checkMandatoryUIParameters(application);
		applicationManager.addApplication(application);
	}

    @RequestMapping(method = RequestMethod.PUT, value = "/{id:\\d+}")
	public void modify(@PathVariable("id") int id, UIApplication application) {
		application.id = id;
		checkMandatoryUIParameters(application);
		applicationManager.updateApplication(application);
	}

    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:\\d+}")
	public void delete(@PathVariable("id") int id) {
		applicationManager.deleteApplication(id);
	}

	/**
	 * Check the values.
	 * 
	 * @return true if all mandatory parameters are filled 
	 */
	private void checkMandatoryUIParameters(UIApplication application) {
		if (StringUtils.isBlank(application.name))
			throw new InvalidParameterException("APPLICATION.ERROR.INVALIDNAME");
		if (StringUtils.isBlank(application.institution))
			throw new InvalidParameterException("APPLICATION.ERROR.INVALIDINSTITUTION");
		if (application.quota == null || application.quota < 0)
			throw new InvalidParameterException("APPLICATION.ERROR.INVALIDQUOTA");
		if (StringUtils.isBlank(application.password)) {
			throw new InvalidParameterException("APPLICATION.ERROR.EMPTYPASSWORD");
		}
	}

	static boolean isPositiveInteger(String strValue) {
		try {
			// le quota doit etre positif ou nul
			return Integer.parseInt(strValue) >= 0;
		} catch (NumberFormatException e) {
			return false;
		}
	}
	
}
