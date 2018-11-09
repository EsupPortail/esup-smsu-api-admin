/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.StatisticManager;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * AccountsQuotaController is the controller for all actions on pages about
 * consolidated summary.
 */
@RequestMapping(value = "/summary/consolidated")
@RolesAllowed("FCTN_API_EDITION_RAPPORT")
public class ConsolidatedSummaryController {
	
	@Inject
	private StatisticManager statisticManager;

        @SuppressWarnings("unused")
	private final Logger logger = Logger.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	public List<UIStatistic> search() {
		return statisticManager.getStatisticsSorted();
	}    
}
