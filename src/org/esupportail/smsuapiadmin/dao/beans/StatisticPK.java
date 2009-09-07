package org.esupportail.smsuapiadmin.dao.beans;

import java.io.Serializable;


/**
 * The class that represents the primary key of statistics.
 */
public class StatisticPK implements Serializable {

	/**
	 * The serialization id.
	 */
	private static final long serialVersionUID = 8478349437014681287L;


	/**
	 * application.
	 */
	private Application app;

	/**
	 * account.
	 */
	private Account acc;

	/**
	 * month.
	 */
	private java.util.Date month;


	/**
	 * Bean constructor.
	 */
	public StatisticPK() {
		super();
	}
	
	/**
	 * Constructor for required fields.
	 */
	public StatisticPK(
		final Application app,
		final Account acc,
		final java.util.Date month) {
		this.setApp(app);
		this.setAcc(acc);
		this.setMonth(month);
	}


	/**
	 * Return the value associated with the column: APP_ID.
	 */
	public Application getApp() {
		return app;
	}

	/**
	 * Set the value related to the column: APP_ID.
	 * @param app the APP_ID value
	 */
	public void setApp(final Application app) {
		this.app = app;
	}



	/**
	 * Return the value associated with the column: ACC_ID.
	 */
	public Account getAcc() {
		return acc;
	}

	/**
	 * Set the value related to the column: ACC_ID.
	 * @param acc the ACC_ID value
	 */
	public void setAcc(final Account acc) {
		this.acc = acc;
	}



	/**
	 * Return the value associated with the column: STAT_MONTH.
	 */
	public java.util.Date getMonth() {
		return month;
	}

	/**
	 * Set the value related to the column: STAT_MONTH.
	 * @param month the STAT_MONTH value
	 */
	public void setMonth(final java.util.Date month) {
		this.month = month;
	}


	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public boolean equals(final Object obj) {
		if (null == obj) {
			return false;
		}
		if (!(obj instanceof StatisticPK)) {
			return false;
		} else {
			StatisticPK mObj = (StatisticPK) obj;
			if (null != this.getApp() && null != mObj.getApp()) {
				if (!this.getApp().equals(mObj.getApp())) {
					return false;
				}
			} else {
				return false;
			}
			if (null != this.getAcc() && null != mObj.getAcc()) {
				if (!this.getAcc().equals(mObj.getAcc())) {
					return false;
				}
			} else {
				return false;
			}
			if (null != this.getMonth() && null != mObj.getMonth()) {
				if (!this.getMonth().equals(mObj.getMonth())) {
					return false;
				}
			} else {
				return false;
			}
			return true;
		}
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "StatisticPK#" + hashCode() + "[application=[" + app + "], account=[" + acc 
		+ "], month=[" + month +  "]]";
	}


}