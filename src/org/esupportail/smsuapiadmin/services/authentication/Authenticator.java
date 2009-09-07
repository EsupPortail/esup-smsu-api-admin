package org.esupportail.smsuapiadmin.services.authentication;

import org.esupportail.smsuapiadmin.dto.beans.UIUser;


/**
 * The interface of authenticators.
 */
public interface Authenticator {

	/**
	 * @return the authenticated user.
	 */
	UIUser getUser();

}