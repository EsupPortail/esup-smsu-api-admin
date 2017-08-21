/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.StatisticManager;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;

/**
 * AccountsQuotaController is the controller for all actions on pages about
 * consolidated summary.
 */
@Path("/summary/consolidated")
@RolesAllowed("FCTN_API_EDITION_RAPPORT")
public class ConsolidatedSummaryController {
	
	@Inject
	private StatisticManager statisticManager;

        @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

	@GET
	public List<UIStatistic> search() {
		return statisticManager.getStatisticsSorted();
	}    
}
