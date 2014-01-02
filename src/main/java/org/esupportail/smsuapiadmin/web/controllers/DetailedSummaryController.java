/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006-2014 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.business.StatisticManager;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedCriteria;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;


@Path("/summary/detailed")
@RolesAllowed("FCTN_API_EDITION_RAPPORT")
public class DetailedSummaryController {
		
	@Autowired
	private StatisticManager statisticManager;

	private final Logger logger = new LoggerImpl(getClass());

	@GET
	@Produces("application/json")
	public List<UIDetailedSummary> search(
		@QueryParam("institution") String institution,
		@QueryParam("account") String accountName,
		@QueryParam("app") String applicationName,
		@QueryParam("startDate") Long startDate,
		@QueryParam("endDate") Long endDate,
		@QueryParam("maxResults") @DefaultValue("0" /* no limit */) int maxResults
		) throws Exception {

		Date startDate_ = startDate == null ? null : new Date(startDate);
		Date endDate_ = endDate == null ? null : new Date(endDate);

		logger.debug("institution=" + institution + ", account=" + accountName + ", application=" + 
			    applicationName + ", startDate=" + startDate + ", endDate=" + endDate);
		return statisticManager.searchDetailedSummaries(institution, accountName, applicationName, startDate_, endDate_, maxResults);
	}
	 
	@GET
	@Produces("application/json")
	@Path("/criteria")
	public List<UIDetailedCriteria> searchCriteria() {
		return statisticManager.getDetailedStatisticsCriteria();
	}
	
}
