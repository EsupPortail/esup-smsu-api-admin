/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
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
	//@Produces({"application/json","application/pdf","application/vnd.ms-excel"})
	public Response search(
		@QueryParam("institution") String institution,
		@QueryParam("account") Long accountId,
		@QueryParam("application") Long applicationId,
		@QueryParam("month") String month) {

		if (month != null) {
			if (!month.matches("\\d{4}-\\d{2}"))
				throw new InvalidParameterException("invalid month parameter, expecting something like 2013-07");
		}

		List<UIStatistic> list = 
		    domainService.searchStatistics(institution, accountId, applicationId, month);

		String contentType = "application/json";
		Object result = list;

		return Response.status(200).entity(result).type(contentType).build();
	}    

	/**
	 * Returns a list for web containing all the months. The list is ordered
	 * (older -> most recent).
	 * 
	 * @return
	 */
    /*
	public List<SelectItem> getMonths() {
		List<SelectItem> result = new ArrayList<SelectItem>();

		SortedSet<Date> months = getDomainService().getMonthsOfStatistics();
		for (Date month : months) {
			String pattern = "MMM yyyy";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, getLocale());
			String formattedDate = sdf.format(month);
			SelectItem item = new SelectItem(month.getTime() + "", formattedDate);
			result.add(item);
		}

		String all = getI18nService().getString("SELECT.ALL", getLocale());
		result.add(0, new SelectItem("-1", all));

		return result;
	}
    */

}
