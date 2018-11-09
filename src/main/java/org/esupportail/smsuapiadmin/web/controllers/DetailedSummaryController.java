/**
 * SMS-U - Copyright (c) 2009-2014 Universite Paris 1 Pantheon-Sorbonne
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.Date;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.esupportail.smsuapiadmin.business.StatisticManager;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedCriteria;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping(value = "/summary/detailed")
@RolesAllowed("FCTN_API_EDITION_RAPPORT")
public class DetailedSummaryController {
		
	@Inject
	private StatisticManager statisticManager;

	private final Logger logger = Logger.getLogger(getClass());

	@RequestMapping(method = RequestMethod.GET)
    @ResponseBody
	public List<UIDetailedSummary> search(
		@RequestParam(value = "institution") String institution,
		@RequestParam(value = "account") String accountName,
		@RequestParam(value = "app") String applicationName,
		@RequestParam(value = "startDate") Long startDate,
		@RequestParam(value = "endDate") Long endDate,
		@RequestParam(value = "maxResults", defaultValue = "0" /* no limit */) int maxResults
		) throws Exception {

		Date startDate_ = startDate == null ? null : new Date(startDate);
		Date endDate_ = endDate == null ? null : new Date(endDate);

		logger.debug("institution=" + institution + ", account=" + accountName + ", application=" + 
			    applicationName + ", startDate=" + startDate + ", endDate=" + endDate);
		return statisticManager.searchDetailedSummaries(institution, accountName, applicationName, startDate_, endDate_, maxResults);
	}
	 
	@RequestMapping(method = RequestMethod.GET, value = "/criteria")
    @ResponseBody
	public List<UIDetailedCriteria> searchCriteria() {
		return statisticManager.getDetailedStatisticsCriteria();
	}
	
}
