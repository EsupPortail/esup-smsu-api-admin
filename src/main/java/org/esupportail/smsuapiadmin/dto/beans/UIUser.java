package org.esupportail.smsuapiadmin.dto.beans;

import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;

/**
 * UIUser is the representation on the web side of the User persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIUser {
	/**
	 * identifier (database) of the account.
	 */
	public String id;

    public String login;
	public EnumeratedRole role;
	public String sessionId;
	public String idpId;

    public UIUser() {        
    }
    
	public UIUser(String login) {
        this.login = login;
	}
}
