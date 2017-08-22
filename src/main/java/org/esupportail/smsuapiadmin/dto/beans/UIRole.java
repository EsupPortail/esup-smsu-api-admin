package org.esupportail.smsuapiadmin.dto.beans;

import java.util.Set;

import org.esupportail.smsuapiadmin.domain.beans.EnumeratedFunction;
import org.esupportail.smsuapiadmin.domain.beans.EnumeratedRole;

/**
 * UIRole is the representation on the web side of the Role persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIRole {
	/**
	 * identifier (database) of the account.
	 */
    public String id;
    
	public EnumeratedRole role;
	public Set<EnumeratedFunction> fonctions;   
}
