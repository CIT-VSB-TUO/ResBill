package cz.vsb.resbill.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.CompareToBuilder;

@Embeddable
public class Period implements Comparable<Period>, Serializable {

	private static final long serialVersionUID = 6273411581118602454L;

	/** Beginning of the period */
	@Temporal(TemporalType.DATE)
	@Column(name = "begin_date")
	@NotNull
	private Date beginDate;

	/** End of the period */
	@Temporal(TemporalType.DATE)
	@Column(name = "end_date")
	private Date endDate;

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Checks if the specified date is covered by this period (is between its bounds, inclusively).
	 * 
	 * @param date
	 *          a date to check
	 * @return <code>true</code> if the date is between this bounds, <code>false</code> otherwise
	 * @throws IllegalArgumentException
	 *           if the date parameter is <code>null</code>
	 */
	public boolean covers(Date date) {
		return isDateInPeriod(date, this);
	}

	/**
	 * Checks if the specified period is covered by this period (is between its bounds, inclusively).
	 * 
	 * @param period
	 *          a period to check
	 * @return <code>true</code> if the period is between this bounds, <code>false</code> otherwise
	 * @throws IllegalArgumentException
	 *           if the period parameter is <code>null</code>
	 */
	public boolean covers(Period period) {
		return isPeriodInPeriod(period, this);
	}

	/**
	 * Checks if the specified period partially or fully overlaps this period (bounds included).
	 * 
	 * @param period
	 *          a period to check
	 * @return <code>true</code> if there is an intersection of the periods, <code>false</code> otherwise
	 * @throws IllegalArgumentException
	 *           if the period parameter is <code>null</code>
	 */
	public boolean overlaps(Period period) {
		if (period == null) {
			throw new IllegalArgumentException("Given parameters cannot be null.");
		}
		return (period.getEndDate() == null || this.getBeginDate().compareTo(period.getEndDate()) <= 0) && (this.getEndDate() == null || period.getBeginDate().compareTo(this.getEndDate()) <= 0);
	}

	/**
	 * Checks if the specified period is valid - from is not null and to is null or not before from.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public static boolean isValid(Date from, Date to) {
		Period p = new Period();
		p.setBeginDate(from);
		p.setEndDate(to);
		return isValid(p);
	}

	/**
	 * Checks if the specified period is valid - beginning is not null and end is null or not before beginning.
	 * 
	 * @param p
	 * @return
	 */
	public static boolean isValid(Period p) {
		if (p == null) throw new IllegalArgumentException("Given parameter cannot be null.");
		return p.getBeginDate() != null && (p.getEndDate() == null || p.getBeginDate().compareTo(p.getEndDate()) <= 0);
	}

	/**
	 * Checks if the specified date is between bounds of the specified period (inclusively).
	 * 
	 * @param date
	 *          a date to check
	 * @param period
	 *          inclusive bounds
	 * @return <code>true</code> if the date is between the bounds, <code>false</code> otherwise
	 * @throws IllegalArgumentException
	 *           if any parameter is <code>null</code>
	 */
	public static boolean isDateInPeriod(Date date, Period period) {
		if (date == null || period == null) {
			throw new IllegalArgumentException("Given parameters cannot be null.");
		}
		return (period.getBeginDate().compareTo(date) <= 0) && (period.getEndDate() == null || period.getEndDate().compareTo(date) >= 0);
	}

	/**
	 * Checks if the first specified period is between bounds of the specified second period (inclusively).
	 * 
	 * @param p1
	 * @param p2
	 * @return <code>true</code> if the p1 is between the bounds od p2, <code>false</code> otherwise
	 * @throws IllegalArgumentException
	 *           if any parameter is <code>null</code>
	 */
	public static boolean isPeriodInPeriod(Period p1, Period p2) {
		if (p1 == null || p2 == null) {
			throw new IllegalArgumentException("Given parameters cannot be null.");
		}
		return isDateInPeriod(p1.getBeginDate(), p2) && (p2.getEndDate() == null || (p1.getEndDate() != null && isDateInPeriod(p1.getEndDate(), p2)));
	}

	/**
	 * Checks if the specified periods are partially or fully overlapped (bounds included).
	 * 
	 * @param period1
	 *          first period
	 * @param period2
	 *          second period
	 * @return <code>true</code> if there is an intersection of the periods, <code>false</code> otherwise
	 * @throws IllegalArgumentException
	 *           if any parameter is <code>null</code>
	 */
	public static boolean arePeriodsOverlapped(Period period1, Period period2) {
		if (period1 == null || period2 == null) {
			throw new IllegalArgumentException("Given parameters cannot be null.");
		}
		return (period2.getEndDate() == null || period1.getBeginDate().compareTo(period2.getEndDate()) <= 0)
				&& (period1.getEndDate() == null || period2.getBeginDate().compareTo(period1.getEndDate()) <= 0);
	}

	@Override
	public int compareTo(Period o) {
		CompareToBuilder cmpBuilder = new CompareToBuilder();
		cmpBuilder.append(this.getBeginDate(), o.getBeginDate());
		cmpBuilder.append(this.getEndDate(), o.getEndDate());
		return cmpBuilder.toComparison();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Period [");
		builder.append(super.toString());
		builder.append(", beginDate=");
		builder.append(beginDate);
		builder.append(", endDate=");
		builder.append(endDate);
		builder.append("]");
		return builder.toString();
	}

}
