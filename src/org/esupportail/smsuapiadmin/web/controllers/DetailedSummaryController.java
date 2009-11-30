/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.smsuapiadmin.business.FormatReport;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.dto.beans.UIDetailedSummary;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * DetailedSummaryController is the controller for all actions on pages about
 * detailed summary.
 * 
 * @author MZRL3760
 * 
 */
public class DetailedSummaryController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -239570715531002003L;

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * identifier of institution.
	 */
	private String institutionId = "-1";

	/**
	 * identifier of account.
	 */
	private String accountId = "-1";

	/**
	 * identifier of application.
	 */
	private String applicationId = "-1";

	/**
	 * identifier of startDate.
	 */
	private Date startDate;

	/**
	 * identifier of endDate.
	 */
	private Date endDate;

	/**
	 * Id for summary to download.
	 */
	private Long downloadId;

	/**
	 * The detailed summary paginator.
	 */
	private DetailedSummaryPaginator paginator;

	/**
	 * Boolean showing that a search has been done.
	 */
	private boolean searchDone;

	/**
	 * Boolean showing the existence of results.
	 */
	private boolean results;

	/**
	 * Bean constructor.
	 */
	public DetailedSummaryController() {
		super();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getClass().getSimpleName() + "#" + hashCode();
	}

	/**
	 * @see org.esupportail.smsuapiadmin.web.controllers.AbstractDomainAwareBean#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		enter();
	}

	/**
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		boolean result = false;

		UIUser currentUser = getCurrentUser();
		if (currentUser != null) {
			String roleName = currentUser.getRole().getRole().name();
			logger.info("L'utilisateur courant est : "
					+ currentUser.getLogin() + ", il a le role : " + roleName);
			result = currentUser
					.isAuthorizedForFonction(EnumeratedFunction.FCTN_API_EDITION_RAPPORT);
		} else {
			logger.error("L'utilisateur n'est pas connu en base");
		}

		return result;
	}

	/**
	 * initialize the page.
	 */
	private void init() {
		searchDone = false;
		results = false;
		paginator = new DetailedSummaryPaginator(getDomainService());
	}

	/**
	 * JSF callback.
	 * 
	 * @return A navigation rule.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			addUnauthorizedActionMessage();
			return null;
		}
		init();
		return "detailedSummary";
	}

	/**
	 * JSF callback.
	 * 
	 * @return A list of summary.
	 */
	public String search() {
logger.debug("institutionId : " + institutionId + ", accountId" + accountId + ", applicationId : " + 
						applicationId + ", startDate" + startDate + ", endDate" + endDate );
		List<UIDetailedSummary> searchDetailedSummaries = getDomainService()
				.searchDetailedSummaries(institutionId, accountId,
						applicationId, startDate, endDate);
		
		paginator.setData(searchDetailedSummaries);
		searchDone = true;
		results = !searchDetailedSummaries.isEmpty();

		return "detailedSummary";
	}

	/**
	 * JSF callback used to download the report in PDF format.
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public String downloadPDFReport() throws JRException, IOException {

		// on récupère le fichier Pdf
		byte[] report = getDomainService().makeReportForDetailedSummaries(
				FormatReport.PDF, paginator.getData(), institutionId,
				applicationId, accountId, startDate, endDate);

		if (report == null) {
			logger.error("La génération du rapport a échouée");
		} else {
			logger.info("La génération du rapport a réussie");
		}

		try {
			downloadId = DownloadUtils.setDownload(report, "rapport.pdf",
					"application/octet-stream");
			logger
					.info("Le fichier 'rapport.pdf' est placé en download avec downloadId="
							+ downloadId);
		} catch (DownloadException e) {
			logger.error("Le placement du rapport en download a échouée", e);
		}

		// on reste sur la même page
		return null;
	}

	/**
	 * JSF callback used to download the report in Excel format.
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public String downloadXLSReport() throws JRException, IOException {

		// on récupère le fichier Pdf
		byte[] report = getDomainService().makeReportForDetailedSummaries(
				FormatReport.XLS, paginator.getData(), institutionId,
				applicationId, accountId, startDate, endDate);

		if (report == null) {
			logger.error("La génération du rapport a échouée");
		} else {
			logger.info("La génération du rapport a réussie");
		}

		downloadId = DownloadUtils.setDownload(report, "rapport.xls",
				"application/octet-stream");

		logger
				.debug("Le fichier 'rapport.xls' est placé en download avec downloadId="
						+ downloadId);

		// on reste sur la même page
		return null;
	}

	/**
	 * Setter for 'institution'.
	 * 
	 * @param institutionId
	 */
	public void setInstitutionId(final String institutionId) {
		this.institutionId = institutionId;
	}

	/**
	 * Getter for 'institution'.
	 * 
	 * @return
	 */
	public String getInstitutionId() {
		return institutionId;
	}

	/**
	 * Getter for 'downloadId'.
	 * 
	 * @return
	 */
	public Long getDownloadId() {
		logger.debug("Demande de téléchargement d'un relevé (id=" + downloadId
				+ ")");
		Long id = this.downloadId;
		this.downloadId = null;
		return id;
	}

	/**
	 * Setter for 'accountId'.
	 * 
	 * @param accountId
	 */
	public void setAccountId(final String accountId) {
		this.accountId = accountId;
	}

	/**
	 * Getter for 'accountId'.
	 * 
	 * @return
	 */
	public String getAccountId() {
		return accountId;
	}

	/**
	 * Setter for 'applicationId'.
	 * 
	 * @param applicationId
	 */
	public void setApplicationId(final String applicationId) {
		this.applicationId = applicationId;
	}

	/**
	 * Getter for 'applicationId'.
	 * 
	 * @return
	 */
	public String getApplicationId() {
		return applicationId;
	}

	/**
	 * Getter for 'paginator'.
	 * 
	 * @return
	 */
	public DetailedSummaryPaginator getPaginator() {
		return paginator;
	}

	/**
	 * Getter for 'startDate'.
	 * 
	 * @return
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Setter for 'startDate'.
	 * 
	 * @param startDate
	 */
	public void setStartDate(final Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter for 'endDate'.
	 * 
	 * @return
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * 
	 * @param endDate
	 */
	public void setEndDate(final Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter for 'results'.
	 * 
	 * @return
	 */
	public boolean isResults() {
		return results;
	}

	/**
	 * Getter for 'searchDone'.
	 * 
	 * @return
	 */
	public boolean isSearchDone() {
		return searchDone;
	}

}
