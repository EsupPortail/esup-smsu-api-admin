package org.esupportail.smsuapiadmin.dto.beans;

import java.util.Date;

import org.esupportail.smsuapi.domain.beans.sms.SmsStatus;

/**
 * UISms is the representation on the web side of the Sms persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UISms {

	/**
	 * identifier (database) of the account.
	 */
    public String id;
    
	public UIApplication application;
	public UIAccount account;
	public String initialId;
	public String senderId;
	public SmsStatus state;
	public Date date;
	public String phone;

}
