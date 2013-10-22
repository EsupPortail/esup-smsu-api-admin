package org.esupportail.smsuapiadmin.dto.beans;

public class UIDetailedCriteria {
	String accountName;
	String appName;
	String institution;

	public UIDetailedCriteria(String accountName, String appName, String institution) {
		this.accountName = accountName;
		this.appName = appName;
		this.institution = institution;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getAppName() {
		return appName;
	}

	public String getInstitution() {
		return institution;
	}
}
