/**
 * ESUP-Portail esup-smsu-api-admin - Copyright (c) 2006 ESUP-Portail consortium
 * http://sourcesup.cru.fr/projects/esup-smsu-api-admin
 */
package org.esupportail.smsuapiadmin.web.controllers;

import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;
import org.esupportail.smsuapiadmin.dto.beans.UIUser;

/**
 * A bean to manage files.
 */
public class AboutController extends AbstractContextAwareController {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = -522191275533736924L;

	/**
	 * Bean constructor.
	 */
	public AboutController() {
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
	 * @return true if the current user is allowed to view the page.
	 */
	public boolean isPageAuthorized() {
		return true;
	}
	
	/**
	 * @return true if the current user is allowed to test the exceptions.
	 */
	public boolean isExceptionAuthorized() {
		boolean result = false;

		UIUser currentUser = getCurrentUser();
		if (currentUser != null) {
			
			EnumeratedRole role = currentUser.getRole().getRole();
			result = role.equals(EnumeratedRole.ROLE_SUPER_ADMIN);
		} 

		return result;
	}
	
	/**
	 * JSF callback.
	 * @return A String.
	 */
	public String enter() {
		if (!isPageAuthorized()) {
			addUnauthorizedActionMessage();
			return null;
		}
		return "navigationAbout";
	}

}
