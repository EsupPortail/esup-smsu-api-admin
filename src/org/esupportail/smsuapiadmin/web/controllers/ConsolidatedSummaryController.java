/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.io.IOException;
import java.util.List;

import net.sf.jasperreports.engine.JRException;

import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.smsuapiadmin.business.FormatReport;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.dto.beans.UIStatistic;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * AccountsQuotaController is the controller for all actions on pages about
 * consolidated summary.
 * 
 * @author MZRL3760
 * 
 */
public class ConsolidatedSummaryController extends
		AbstractContextAwareController {

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
	 * identifier of month.
	 */
	private String month = "-1";

	/**
	 * Id for summary to download.
	 */
	private Long downloadId;

	/**
	 * The consolidated summary paginator.
	 */
	private ConsolidatedSummaryPaginator paginator;

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
	public ConsolidatedSummaryController() {
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
		paginator = new ConsolidatedSummaryPaginator(getDomainService());
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
		return "consolidatedSummary";
	}

	/**
	 * JSF callback.
	 * 
	 * @return A list of summary.
	 */
	public String search() {
		List<UIStatistic> searchedStatistics = getDomainService()
				.searchStatistics(institutionId, accountId, applicationId,
						month);
		paginator.setData(searchedStatistics);
		setSearchDone(true);
		setResults(!searchedStatistics.isEmpty());
		return "consolidatedSummary";
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

	public void setDownloadId(final Long downloadId) {
		this.downloadId = downloadId;
	}

	/**
	 * Getter for 'downloadId'.
	 * 
	 * @return
	 */
	public Long getDownloadId() {
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
	 * Setter for 'month'.
	 * 
	 * @param month
	 */
	public void setMonth(final String month) {
		this.month = month;
	}

	/**
	 * Getter for 'monthId'.
	 * 
	 * @return
	 */
	public String getMonth() {
		return month;
	}

	public ConsolidatedSummaryPaginator getPaginator() {
		return paginator;
	}

	public void setResults(final boolean results) {
		this.results = results;
	}

	public boolean isResults() {
		return results;
	}

	public void setSearchDone(final boolean searchDone) {
		this.searchDone = searchDone;
	}

	public boolean isSearchDone() {
		return searchDone;
	}

	/**
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public String downloadPDFReport() throws JRException, IOException {

		// on recupere le fichier Pdf
		byte[] report = getDomainService().makeReportForConsolidatedSummaries(
				FormatReport.PDF, paginator.getData(), institutionId,
				applicationId, accountId, month);

		if (report == null) {
			logger.error("La generation du rapport a echouee");
		} else {
			logger.info("La generation du rapport a reussie");
		}

		try {
			downloadId = DownloadUtils.setDownload(report, "rapport.pdf",
					"application/octet-stream");
			logger
					.info("Le fichier 'rapport.pdf' est place en download avec downloadId="
							+ downloadId);
		} catch (DownloadException e) {
			logger.error("Le placement du rapport en download a echouee", e);
		}

		// on reste sur la meme page
		return null;
	}

	/**
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public String downloadXLSReport() throws JRException, IOException {

		// on recupere le fichier Pdf
		byte[] report = getDomainService().makeReportForConsolidatedSummaries(
				FormatReport.XLS, paginator.getData(), institutionId,
				applicationId, accountId, month);

		if (report == null) {
			logger.error("La generation du rapport a echouee");
		} else {
			logger.info("La generation du rapport a reussie");
		}

		try {
			downloadId = DownloadUtils.setDownload(report, "rapport.xls",
					"application/octet-stream");
			logger
					.info("Le fichier 'rapport.pdf' est place en download avec downloadId="
							+ downloadId);
		} catch (DownloadException e) {
			logger.error("Le placement du rapport en download a echouee", e);
		}

		// on reste sur la meme page
		return null;
	}
}
