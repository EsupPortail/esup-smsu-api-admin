/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;

import org.apache.myfaces.custom.fileupload.UploadedFile;
import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.dto.beans.UIApplication;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * ApplicationController is the controller for all actions on pages about
 * application.
 * 
 * @author MZRL3760
 * 
 */
public class ApplicationsController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -239570715531002003L;

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The application.
	 */
	private UIApplication application;

	/**
	 * The client applications paginator.
	 */
	private ApplicationsPaginator paginator;

	/**
	 * Bean constructor.
	 */
	public ApplicationsController() {
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
		paginator = new ApplicationsPaginator(getDomainService());
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
					.isAuthorizedForFonction(EnumeratedFunction.FCTN_API_CONFIG_APPLIS);
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
		return "applications";
	}

	/**
	 * initialize the page.
	 */
	private void init() {
		// initialize the paginator
		paginator = new ApplicationsPaginator(getDomainService());
	}

	/**
	 * JSF callback.
	 * 
	 * @return A navigation rule.
	 */
	public String create() {
		// on crée une nouvelle application
		setApplication(new UIApplication());
		return "editApplication";
	}

	/**
	 * JSF callback.
	 * 
	 * @return A navigation rule.
	 */
	public String modify() {
		// on met à jour le mode d'édition
		application.setAddMode(false);
		return "editApplication";
	}

	/**
	 * Validates the name field.
	 * 
	 * @param context
	 * @param componentToValidate
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateName(final FacesContext context,
			final UIComponent componentToValidate, final Object value)
			throws ValidatorException {
		String strValue = (String) value;
		// on enleve les espaces
		strValue = strValue.trim();

		if (strValue.equals("")) {
			throw new ValidatorException(
					getFacesErrorMessage("APPLICATION.ERROR.INVALIDNAME"));
		}
	}

	/**
	 * Validates the certificate field.
	 * 
	 * @param context
	 * @param componentToValidate
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateCertificate(final FacesContext context,
			final UIComponent componentToValidate, final Object value)
			throws ValidatorException {
		if (value == null) {
			throw new ValidatorException(
					getFacesErrorMessage("APPLICATION.ERROR.INVALIDCERTIFICATE"));
		}
	}

	public String uploadCertificate() {
		// le certificat
		// on regarde d'abord si un fichier a été uploadé
		UploadedFile certificateFile = application.getCertificateFile();
		if (certificateFile != null) {
			try {
				application.setCertificate(certificateFile.getBytes());
			} catch (IOException e) {
				logger.warn("Impossible de récupérer les bits du certificat.",
						e);
			}
		}
		return "editApplication";
	}

	/**
	 * Validates the institution field.
	 * 
	 * @param context
	 * @param componentToValidate
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateInstitution(final FacesContext context,
			final UIComponent componentToValidate, final Object value)
			throws ValidatorException {
		String strValue = (String) value;
		// on enleve les espaces
		strValue = strValue.trim();

		if (strValue.equals("")) {
			throw new ValidatorException(
					getFacesErrorMessage("APPLICATION.ERROR.INVALIDINSTITUTION"));
		}
	}

	/**
	 * Validates the account field.
	 * 
	 * @param context
	 * @param componentToValidate
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateAccount(final FacesContext context,
			final UIComponent componentToValidate, final Object value)
			throws ValidatorException {
		String strValue = (String) value;
		// on enleve les espaces
		strValue = strValue.trim();

		if (strValue.equals("")) {
			throw new ValidatorException(
					getFacesErrorMessage("APPLICATION.ERROR.INVALIDACCOUNT"));
		}
	}

	/**
	 * Validates the quota field. Quota has to be a positive or null integer.
	 * 
	 * @param context
	 * @param componentToValidate
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateQuota(final FacesContext context,
			final UIComponent componentToValidate, final Object value)
			throws ValidatorException {
		String strValue = (String) value;
		// on enleve les espaces
		strValue = strValue.trim();

		String messageInvalidQuota = "APPLICATION.ERROR.INVALIDQUOTA";
		if (strValue.equals("")) {
			// le champ est vide
			throw new ValidatorException(
					getFacesErrorMessage(messageInvalidQuota));
		} else {
			// le champ n'est pas vide
			try {
				// on essaye de le parser en int
				Integer intValue = Integer.parseInt(strValue);
				// le quota doit être positif ou nul
				if (intValue < 0) {
					throw new ValidatorException(
							getFacesErrorMessage(messageInvalidQuota));
				}
			} catch (NumberFormatException e) {
				throw new ValidatorException(
						getFacesErrorMessage(messageInvalidQuota));
			}
		}
	}

	/**
	 * Getter for 'paginator'.
	 * 
	 * @return
	 */
	public ApplicationsPaginator getPaginator() {
		return paginator;
	}

	/**
	 * Setter for 'application'.
	 * 
	 * @param application
	 */
	public void setApplication(final UIApplication application) {
		this.application = application;
	}

	/**
	 * Getter for 'application'.
	 * 
	 * @return
	 */
	public UIApplication getApplication() {
		return application;
	}

	/**
	 * JSF callback. Saves an application in database (add or update).
	 * 
	 * @return A navigation rule.
	 */
	public String save() {
		String result = "applications";

		if (checkMandatoryUIParameters()) {
			if (application.isAddMode()) {
				getDomainService().addApplication(application);
			} else {
				getDomainService().updateApplication(application);
			}
			// on revient à la liste des applications
		} else {
			// on revient à la page d'édition
			result = "editApplication";
		}

		return result;
	}

	/**
	 * JSF callback. Deletes an application in database (add or update).
	 * 
	 * @return A navigation rule.
	 */
	public String delete() {
		getDomainService().deleteApplication(application);
		// on revient à la liste des application
		reset();
		return "applications";
	}

	/**
	 * Check the values.
	 * 
	 * @return
	 */
	private Boolean checkMandatoryUIParameters() {
		Boolean result = true;

		uploadCertificate();

		String name = application.getName();
		String account = application.getAccount().getName();
		String institution = application.getInstitution().getName();
		String quota = application.getQuota();
		byte[] certificate = application.getCertificate();
		UploadedFile certificateFile = application.getCertificateFile();

		String messageInvalidQuota = "APPLICATION.ERROR.INVALIDQUOTA";

		if ((name == null) || name.equals("")) {
			addErrorMessage("editApplication:name",
					"APPLICATION.ERROR.INVALIDNAME");
			result = false;
		}
		if ((account == null) || account.equals("")) {
			addErrorMessage("editApplication:account",
					"APPLICATION.ERROR.INVALIDACCOUNT");
			result = false;
		}
		if ((institution == null) || institution.equals("")) {
			addErrorMessage("editApplication:institution",
					"APPLICATION.ERROR.INVALIDINSTITUTION");
			result = false;
		}

		if (quota == null || quota.equals("")) {
			addErrorMessage("editApplication:quota", messageInvalidQuota);
			result = false;
		} else {
			try {
				// on essaye de le parser en int
				Integer intValue = Integer.parseInt(quota);
				// le quota doit être positif ou nul
				if (intValue < 0) {
					addErrorMessage("editApplication:quota",
							messageInvalidQuota);
					result = false;
				}
			} catch (NumberFormatException e) {
				addErrorMessage("editApplication:quota", messageInvalidQuota);
				result = false;
			}
		}
		if (certificate == null && certificateFile == null) {
			addErrorMessage("editApplication:certificate",
					"APPLICATION.ERROR.INVALIDCERTIFICATE");
			result = false;
		}

		return result;
	}

}
