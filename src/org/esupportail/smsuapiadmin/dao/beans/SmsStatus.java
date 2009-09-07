package org.esupportail.smsuapiadmin.dao.beans;

/**
 * Enumeration of all sms' status.
 * 
 * @author MZRL3760
 * 
 */
public enum SmsStatus {
	// Message saved in BD
	CREATED,
	// Message sent by the broker
	IN_PROGRESS,
	// Message successfully sent to the user mobile phone
	DELIVERED,
	// Message not sent due to quota error
	ERROR_QUOTA,
	// Message not sent because phone number is already in black list
	ERROR_PRE_BL,
	// Message not sent because phone number is invalid
	ERROR_POST_BL,
	// other error
	ERROR
}
