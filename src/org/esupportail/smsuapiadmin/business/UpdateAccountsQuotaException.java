package org.esupportail.smsuapiadmin.business;

/**
 * UpdateAccountsQuotaException is the exception due to incorrect import excel
 * file.
 * 
 * @author MZRL3760
 * 
 */
public class UpdateAccountsQuotaException extends Exception {

	/**
	 * UID.
	 */
	private static final long serialVersionUID = -4176723581659112620L;

	/**
	 * Constructor with a message and a throwable.
	 * 
	 * @param message
	 * @param th
	 */
	public UpdateAccountsQuotaException(final String message, final Throwable th) {
		super(message, th);
	}

	/**
	 * Constructor with a message.
	 * 
	 * @param message
	 */
	public UpdateAccountsQuotaException(final String message) {
		super(message);
	}
}
