/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.domain.DomainService;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;

/**
 * AccountsQuotaController is the controller for all actions on pages about
 * consolidated summary.
 * 
 * @author MZRL3760
 * 
 */
@Path("/summary/consolidated")
@RolesAllowed("FCTN_API_EDITION_RAPPORT")
public class ConsolidatedSummaryController {
	
	@Autowired
	private DomainService domainService;

        @SuppressWarnings("unused")
	private final Logger logger = new LoggerImpl(getClass());

	@GET
	public List<UIStatistic> search() {
		return domainService.searchStatistics();
	}    
}
