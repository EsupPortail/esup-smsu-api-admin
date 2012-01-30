/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.validator.ValidatorException;

import org.esupportail.commons.services.logging.Logger;
import org.esupportail.commons.services.logging.LoggerImpl;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.dto.beans.UIRole;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * UsersController is the controller for all actions on pages about user.
 * 
 * @author MZRL3760
 * 
 */
public class UsersController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -239570715531002003L;

	/**
	 * Log4j logger.
	 */
	private final Logger logger = new LoggerImpl(getClass());

	/**
	 * The user.
	 */
	private UIUser user;

	/**
	 * The client users paginator.
	 */
	private UsersPaginator paginator;

	/**
	 * Bean constructor.
	 */
	public UsersController() {
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
		// initialize the paginator
		UIUser currentUser = getCurrentUser();
		if (currentUser != null) {
		paginator = new UsersPaginator(getDomainService(), currentUser);
		} else {
		paginator = new UsersPaginator(getDomainService());	
		}
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
					.isAuthorizedForFonction(EnumeratedFunction.FCTN_MANAGE_USERS);
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
		return "users";
	}

	/**
	 * initialize the page.
	 */
	private void init() {
		// initialize the paginator
		UIUser currentUser = getCurrentUser();
		if (currentUser != null) {
		paginator = new UsersPaginator(getDomainService(), currentUser);
		} else {
		paginator = new UsersPaginator(getDomainService());	
		}
	}

	/**
	 * JSF callback.
	 * 
	 * @return A navigation rule.
	 */
	public String create() {
		// on cree un nouvel utilisateur
		setUser(new UIUser());
		return "editUser";
	}

	/**
	 * JSF callback.
	 * 
	 * @return A navigation rule.
	 */
	public String modify() {
		// on met a jour le mode d'edition
		user.setAddMode(false);
		return "editUser";
	}

	/**
	 * Validates the login field.
	 * 
	 * @param context
	 * @param componentToValidate
	 * @param value
	 * @throws ValidatorException
	 */
	public void validateLogin(final FacesContext context,
			final UIComponent componentToValidate, final Object value)
			throws ValidatorException {
		String login = (String) value;

		if (!login.equals(user.getLogin())) {
			boolean inUse = getDomainService().loginAlreadyUsed(login);
			if (inUse) {
				throw new ValidatorException(
						getFacesErrorMessage("USER.ERROR.LOGINUSED"));
			}
		}
	}

	/**
	 * Getter for 'paginator'.
	 * 
	 * @return
	 */
	public UsersPaginator getPaginator() {
		return paginator;
	}

	/**
	 * Setter for 'user'.
	 * 
	 * @param user
	 */
	public void setUser(final UIUser user) {
		this.user = user;
	}

	/**
	 * Getter for 'user'.
	 * 
	 * @return
	 */
	public UIUser getUser() {
		return user;
	}

	/**
	 * JSF callback. Saves a user in database (add or update).
	 * 
	 * @return A navigation rule.
	 */
	public String save() {
		String result = "users";

		if (user.isAddMode()) {
			getDomainService().addUser(user);
		} else {
			getDomainService().updateUser(user);
		}

		// on revient a la liste des utilisateurs
		return result;
	}

	/**
	 * JSF callback. Deletes a user in database (add or update).
	 * 
	 * @return A navigation rule.
	 */
	public String delete() {
		getDomainService().deleteUser(user);
		// on revient a la liste des utilisateurs
		reset();
		return "users";
	}

	/**
	 * Returns a list of roles for the page.
	 * 
	 * @return
	 */
	public List<SelectItem> getRoles() {
		List<SelectItem> result = new ArrayList<SelectItem>();

		List<UIRole> roles = getDomainService().getAllRoles();

		logger.debug("Nombre de roles de la liste provenant du domaine : "
				+ roles.size());

		for (UIRole role : roles) {
			String key = role.getI18nKey();
			String nameRole = getI18nService().getString(key, getLocale());
			SelectItem item = new SelectItem(role.getId(), nameRole);
			result.add(item);
		}

		logger.debug("Envoi de la liste de roles pour le web (nb de roles : "
				+ result.size() + ")");

		return result;
	}

}
