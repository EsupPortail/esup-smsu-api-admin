/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
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

import org.apache.commons.lang.StringUtils;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.business.ApplicationManager;
import org.esupportail.smsuapiadmin.business.NotFoundException;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;


@Path("/applications")
@RolesAllowed("FCTN_API_CONFIG_APPLIS")
public class ApplicationsController {
	
	@Autowired
	private ApplicationManager applicationManager;

        @SuppressWarnings("unused")
	private final Logger logger = new LoggerImpl(getClass());

	@GET
	@Produces("application/json")
	public List<UIApplication> getApplications() {
		return applicationManager.getAllUIApplications();
	}

	@GET
	@Path("/{id:\\d+}")
	@Produces("application/json")
	public UIApplication getApplication(@PathParam("id") int id) {
		UIApplication app = applicationManager.getUIApplication(id);
		if (app == null) throw new NotFoundException("invalid application " + id); 
		return app;
	}

	@POST
	public void create(UIApplication application) {
		checkMandatoryUIParameters(application);
		applicationManager.addApplication(application);
	}

	@PUT
	@Path("/{id:\\d+}")
	public void modify(@PathParam("id") int id, UIApplication application) {
		application.setId(id);
		checkMandatoryUIParameters(application);
		applicationManager.updateApplication(application);
	}

	@DELETE
	@Path("/{id:\\d+}")
	public void delete(@PathParam("id") int id) {
		applicationManager.deleteApplication(id);
	}

	/**
	 * Check the values.
	 * 
	 * @return true if all mandatory parameters are filled 
	 */
	private void checkMandatoryUIParameters(UIApplication application) {
		String name = application.getName();
		String institution = application.getInstitution();
		Long quota = application.getQuota();

		if (StringUtils.isBlank(name))
			throw new InvalidParameterException("APPLICATION.ERROR.INVALIDNAME");
		if (StringUtils.isBlank(institution))
			throw new InvalidParameterException("APPLICATION.ERROR.INVALIDINSTITUTION");
		if (quota == null || quota < 0)
			throw new InvalidParameterException("APPLICATION.ERROR.INVALIDQUOTA");

		if (application.getPassword() == null) {		
			if (application.getCertificate() == null)
				throw new InvalidParameterException("APPLICATION.ERROR.INVALIDCERTIFICATE");

			application.setPassword(null); 
		} else {
			if (StringUtils.isBlank(application.getPassword())) {
				throw new InvalidParameterException("APPLICATION.ERROR.EMPTYPASSWORD");
			}
			application.setCertificate(null);
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
