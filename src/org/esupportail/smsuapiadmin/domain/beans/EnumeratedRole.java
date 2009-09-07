package org.esupportail.smsuapiadmin.domain.beans;

/**
 * Enumeration of role.
 * 
 * @author MZRL3760
 * 
 */
public enum EnumeratedRole {
	// report
	ROLE_REPORT("ROLE.REPORT.NAME"),
	// Manage accounts and applications
	ROLE_MANAGE("ROLE.MANAGE.NAME"),
	// Super admin,
	ROLE_SUPER_ADMIN("ROLE.SUPERADMIN.NAME");

	/**
	 * I18n key role for display name.
	 */
	private String i18nKey;

	/**
	 * Constructor with i18n key.
	 * 
	 * @param key
	 */
	private EnumeratedRole(final String key) {
		i18nKey = key;
	}

	/**
	 * Getter for 'i18nKey'.
	 * 
	 * @return
	 */
	public String getI18nKey() {
		return i18nKey;
	}
}
