/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.io.IOException;
import java.io.InputStream;

import net.sf.jasperreports.engine.JRException;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.esupportail.commons.exceptions.DownloadException;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.commons.utils.DownloadUtils;
import org.esupportail.smsuapiadmin.business.UpdateAccountsQuotaException;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * AccountsQuotaController is the controller for all actions on pages about
 * accounts quota.
 * 
 * @author MZRL3760
 * 
 */
public class AccountsController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -239570715531002003L;

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * uploaded file - linked with the field of the JSF page.
	 */
	private UploadedFile xlsFile;

	/**
	 * The client applications paginator.
	 */
	private AccountsPaginator paginator;

	/**
	 * 
	 */
	private Long downloadId;

	/**
	 * Bean constructor.
	 */
	public AccountsController() {
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
					.isAuthorizedForFonction(EnumeratedFunction.FCTN_GESTION_CPT_IMPUT);
		} else {
			logger.error("L'utilisateur n'est pas connu en base");
		}

		return result;
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
		return "accounts";
	}

	/**
	 * initialize the page.
	 */
	private void init() {
		// initialize the paginator
		paginator = new AccountsPaginator(getDomainService());
	}

	/**
	 * JSF callback.
	 * 
	 * @return A navigation rule.
	 */
	public String importFile() {
		boolean error = false;
		try {
			InputStream inputStream = xlsFile.getInputStream();
			getDomainService().updateAccountsQuota(inputStream);
		} catch (IOException e) {
			error = true;
			logger.error("Impossible de lire dans le fichier uploade", e);
			addErrorMessage("import:importFile",
					"ACCOUNT.IMPORT.XLSFILE.ERROR.GETIMPORT");
		} catch (UpdateAccountsQuotaException e) {
			error = true;
			logger.warn("Impossible d'importer le fichier", e);
			addErrorMessage("import:importFile",
					"ACCOUNT.IMPORT.XLSFILE.ERROR", e.getMessage());
		}

		if (!error) {
			logger.info("L'import du ficher Excel a reussi");
			addInfoMessage("import:importFile", "ACCOUNT.IMPORT.XLSFILE.OK");
		}
		return "accounts";
	}

	/**
	 * JSF callback. Downloads the report in Excel format.
	 * 
	 * @return A navigation rule.
	 * @throws JRException
	 * @throws IOException
	 */
	public String downloadXLSReport() throws IOException, JRException {

		// on recupere le fichier Excel
		byte[] report = getDomainService().makeXLSReportForAccounts();

		downloadId = DownloadUtils.setDownload(report, "rapport.xls",
				"application/octet-stream");

		logger
				.debug("Le fichier 'rapport.xls' est place en download avec downloadId="
						+ downloadId);

		// on reste sur la meme page
		return null;
	}

	/**
	 * JSF callback. Downloads the report in PDF format.
	 * 
	 * @return
	 * @throws JRException
	 * @throws IOException
	 */
	public String downloadPDFReport() throws JRException, IOException {

		// on recupere le fichier Pdf
		byte[] report = getDomainService().makePDFReportForAccounts();

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
	 * Setter for 'xlsFile'.
	 * 
	 * @param xlsFile
	 */
	public void setXlsFile(final UploadedFile xlsFile) {
		this.xlsFile = xlsFile;
	}

	/**
	 * Getter for 'xlsFile'.
	 * 
	 * @return
	 */
	public UploadedFile getXlsFile() {
		return xlsFile;
	}

	/**
	 * Getter for 'paginator'.
	 * 
	 * @return
	 */
	public AccountsPaginator getPaginator() {
		return paginator;
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

}
