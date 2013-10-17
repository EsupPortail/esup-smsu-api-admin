/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.domain.DomainService;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;


@Path("/summary/detailed")
@RolesAllowed("FCTN_API_EDITION_RAPPORT")
public class DetailedSummaryController {
	
	@Autowired
	private DomainService domainService;

	private final Logger logger = new LoggerImpl(getClass());

	@GET
	//@Produces({"application/json","application/pdf","application/vnd.ms-excel"})
	public Response search(
		@QueryParam("institution") String institution,
		@QueryParam("account") Long accountId,
		@QueryParam("application") Long applicationId,
		@QueryParam("startDate") Long startDate,
		@QueryParam("endDate") Long endDate,
		@QueryParam("maxResults") @DefaultValue("0" /* no limit */) int maxResults
		) throws Exception {

		Date startDate_ = startDate == null ? null : new Date(startDate);
		Date endDate_ = endDate == null ? null : new Date(endDate);

		logger.debug("institution=" + institution + ", accountId=" + accountId + ", applicationId=" + 
			    applicationId + ", startDate=" + startDate + ", endDate=" + endDate);
		List<UIDetailedSummary> list = 
			domainService.searchDetailedSummaries(institution, accountId, applicationId, startDate_, endDate_, maxResults);

		String contentType = "application/json";
		Object result = list;
		return Response.status(200).entity(result).type(contentType).build();	
	}

}
