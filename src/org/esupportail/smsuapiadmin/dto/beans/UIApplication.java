package org.esupportail.smsuapiadmin.dto.beans;

/**
 * UIApplication is the representation on the web side of the Application
 * persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIApplication {

	private String PASSWORD_PREFIX_IN_CERTIFCATE = "PASSWORD:";

	/** identifier (database) of the application. */
	private Integer id;

	private String name;
	private String password;
	private String institution;
	private String accountName;
	private Long quota;
	private java.lang.Long consumedSms;
	private byte[] certificate;
	private boolean deletable = true;

	/**
	 * Constructor.
	 */
	public UIApplication() {
	}

	public byte[] computeCertificateOrPassword() {
		if (password != null)
			return (PASSWORD_PREFIX_IN_CERTIFCATE + password).getBytes();
		else
			return certificate;
	}

	public void setCertificateOrPassword(byte[] certificate) {
		String password = null;
		if (certificate != null) 
			password = removePrefixOrNull(new String(certificate), PASSWORD_PREFIX_IN_CERTIFCATE);
		setCertificate(password == null ? certificate : null);
		setPassword(password);
	}

	/**
	 * Getter for 'name'.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Setter for 'name'.
	 * 
	 * @param name
	 */
	public void setName(final String name) {
		this.name = name.trim();
	}

	/**
	 * Getter for 'institution'.
	 * 
	 * @return
	 */
	public String getInstitution() {
		return institution;
	}

	/**
	 * Setter for 'institution'.
	 * 
	 * @param institution
	 */
	public void setInstitution(final String institution) {
		this.institution = institution;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(final String accountName) {
		this.accountName = accountName;
	}

	/**
	 * Getter for 'quota'.
	 * 
	 * @return
	 */
	public Long getQuota() {
		return quota;
	}

	/**
	 * Setter for 'quota'.
	 * 
	 * @param quota
	 */
	public void setQuota(final Long quota) {
		this.quota = quota;
	}

	public java.lang.Long getConsumedSms() {
		return consumedSms;
	}

	public void setConsumedSms(final java.lang.Long consumedSms) {
		this.consumedSms = consumedSms;
	}

	/**
	 * Setter for 'id'.
	 * 
	 * @param id
	 */
	public void setId(final Integer id) {
		this.id = id;
	}

	/**
	 * Getter for 'id'.
	 * 
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * Setter for 'certificate'.
	 * 
	 * @param certificate
	 */
	public void setCertificate(final byte[] certificate) {
		this.certificate = certificate;
	}

	/**
	 * Getter for 'certificate'.
	 * 
	 * @return
	 */
	public byte[] getCertificate() {
		return certificate;
	}

	/**
	 * Setter for 'password'.
	 * 
	 * @param password
	 */
	public void setPassword(final String password) {
		this.password = password;
	}

	/**
	 * Getter for 'password'.
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Setter for 'deletable'.
	 * 
	 * @param deletable
	 */
	public void setDeletable(final boolean deletable) {
		this.deletable = deletable;
	}

	/**
	 * Getter for 'deletable'.
	 * 
	 * @return
	 */
	public boolean isDeletable() {
		return deletable;
	}

	private String removePrefixOrNull(String s, String prefix) {
		return s.startsWith(prefix) ? s.substring(prefix.length()) : null;
	}

}
