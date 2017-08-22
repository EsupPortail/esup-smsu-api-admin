package org.esupportail.smsuapiadmin.dto.beans;

/**
 * UIApplication is the representation on the web side of the Application
 * persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIApplication {
	/** identifier (database) of the application. */
	public Integer id;

	public String name;
	public String password;
	public String institution;
	public String accountName;
	public Long quota;
	public Long consumedSms;
	public boolean deletable = true;

}
