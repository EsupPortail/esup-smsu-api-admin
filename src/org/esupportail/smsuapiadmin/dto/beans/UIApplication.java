package org.esupportail.smsuapiadmin.dto.beans;

import org.apache.myfaces.custom.fileupload.UploadedFile;

/**
 * UIApplication is the representation on the web side of the Application
 * persistent.
 * 
 * @author MZRL3760
 * 
 */
public class UIApplication extends UIObject {

	/**
	 * The UID.
	 */
	private static final long serialVersionUID = -5312393467136910173L;

	private String PASSWORD_PREFIX_IN_CERTIFCATE = "PASSWORD:";

	/**
	 * identifier (database) of the application.
	 */
	private String id;
	/**
	 * name - linked with the field of the JSF page.
	 */
	private String name;
	/**
	 * password - linked with the field of the JSF page.
	 */
	private String password;
	/**
	 * certificate - not linked.
	 */
	private byte[] certificate;
	/**
	 * institution - linked with the field of the JSF page.
	 */
	private UIInstitution institution;
	/**
	 * account - linked with the field of the JSF page.
	 */
	private UIAccount account;
	/**
	 * quota - linked with the field of the JSF page.
	 */
	private String quota;
	/**
	 * uploaded file - linked with the field of the JSF page.
	 */
	private UploadedFile certificateFile;
	/**
	 * deletable.
	 */
	private boolean deletable = true;

	/**
	 * Constructor.
	 */
	public UIApplication() {
		init();
	}

	/**
	 * Initialization.
	 */
	private void init() {
		institution = new UIInstitution();
		account = new UIAccount();
	}

	public byte[] getCertificateOrPassword() {
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
	public UIInstitution getInstitution() {
		return institution;
	}

	/**
	 * Setter for 'institution'.
	 * 
	 * @param institution
	 */
	public void setInstitution(final UIInstitution institution) {
		this.institution = institution;
	}

	/**
	 * Getter for 'account'.
	 * 
	 * @return
	 */
	public UIAccount getAccount() {
		return account;
	}

	/**
	 * Setter for 'account'.
	 * 
	 * @param account
	 */
	public void setAccount(final UIAccount account) {
		this.account = account;
	}

	/**
	 * Getter for 'quota'.
	 * 
	 * @return
	 */
	public String getQuota() {
		return quota;
	}

	/**
	 * Setter for 'quota'.
	 * 
	 * @param quota
	 */
	public void setQuota(final String quota) {
		this.quota = quota.trim();
	}

	/**
	 * Setter for 'certificateFile'.
	 * 
	 * @param certificateFile
	 */
	public void setCertificateFile(final UploadedFile certificateFile) {
		this.certificateFile = certificateFile;
	}

	/**
	 * Setter for 'certificateFile'.
	 * 
	 * @return
	 */
	public UploadedFile getCertificateFile() {
		return certificateFile;
	}

	/**
	 * Setter for 'id'.
	 * 
	 * @param id
	 */
	public void setId(final String id) {
		this.id = id;
	}

	/**
	 * Getter for 'id'.
	 * 
	 * @return
	 */
	public String getId() {
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
