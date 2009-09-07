package org.esupportail.smsuapiadmin.business;

import java.util.List;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.esupportail.smsuapiadmin.dao.beans.Account;

/**
 * Class 'AccountDataSource' is the data source to fill accounts report.
 * 
 * @author MZRL3760
 * 
 */
public class AccountDataSource implements JRDataSource {

	/**
	 * accounts.
	 */
	private List<Account> accounts;

	/**
	 * index.
	 */
	private int index = -1;

	/**
	 * Default constructor.
	 * 
	 * @param allAccounts
	 */
	public AccountDataSource(final List<Account> allAccounts) {
		accounts = allAccounts;
	}

	public Object getFieldValue(final JRField field) throws JRException {
		Object result = null;

		Account account = accounts.get(index);
		if (field.getName().equals("LABEL")) {
			result = account.getLabel();
		} else if (field.getName().equals("QUOTA")) {
			result = account.getQuota();
		} else if (field.getName().equals("SMS")) {
			result = account.getConsumedSms();
		}

		return result;
	}

	public boolean next() throws JRException {
		index++;
		return index < accounts.size();
	}

}
