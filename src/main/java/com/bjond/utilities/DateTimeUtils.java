/*  Copyright (c) 2015
 *  by Bjönd, Inc., Boston, MA
 *
 *  This software is furnished under a license and may be used only in
 *  accordance with the terms of such license.  This software may not be
 *  provided or otherwise made available to any other party.  No title to
 *  nor ownership of the software is hereby transferred.
 *
 *  This software is the intellectual property of Bjönd, Inc.,
 *  and is protected by the copyright laws of the United States of America.
 *  All rights reserved internationally.
 *
 */

package com.bjond.utilities;


import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;



/** <p> Bjond DateTime Utils </p>

 *
 * <a href="mailto:Stephen.Agneta@bjondinc.com">Steve 'Crash' Agneta</a>
 * @author Stephen 'Crash' Agneta
 *
 */

final public class DateTimeUtils {

	/**
	 * Returns 11:59:59.999 PM of date.
	 *
	 * @param date Valid non-null java.util.Date object.
	 * @return New date. Original is not altered.
	 */
    public static Date getEndOfDay(final Date date) {
        return DateUtils.addMilliseconds(DateUtils.ceiling(date, Calendar.DATE), -1);
    }

	/**
	 * Returns 00:01:01.000 in the morning of Date.
	 *
	 * @param date Valid non-null java.util.Date object
	 * @return New date. Original is not altered.
	 */
    public static Date getStartOfDay(final Date date) {
        return DateUtils.truncate(date, Calendar.DATE);
    }

	/**
	 * Returns true if now greater than date
	 *
	 * @param date Valid non-null java.util.Date object
	 * @return New date. Original is not altered.
	 */
    public static boolean isNowAfter(final Date date){
        return (date != null && System.currentTimeMillis() > date.getTime());
    }

	/**
	 * Returns true if NOW greater than or equal to date
	 *
	 * @param date Valid non-null java.util.Date object
	 * @return New date. Original is not altered.
	 */
    public static boolean isNowEqualToOrGreaterThan(final Date date){
        return (date != null && System.currentTimeMillis() >= date.getTime());
    }

	/**
	 * return true if firstDate greater than or equal to secondDate
	 *
	 * @param firstDate Valid non-null java.util.Date object
	 * @param secondDate Valid non-null java.util.Date object
	 * @return New date. Original is not altered.
	 */
    public static boolean isEqualToOrGreaterThan(final Date firstDate, final Date secondDate){
        return (firstDate != null && secondDate != null && firstDate.getTime() >= secondDate.getTime());
    }

	/**
	 * Returns 11:59:59.999 PM tomorrow of date.
	 *
	 * @param date Valid non-null java.util.Date object
	 * @return New date. Original is not altered.
	 */
    public static Date tomorrow(final Date date) {
        return getEndOfDay(DateUtils.addDays(date, 1));
    } 

	/**
	 * Returns 00:01:01.000 yesterday.
	 *
	 * @param date Valid non-null java.util.Date object
	 * @return New date. Original is not altered.
	 */
    public static Date yesterday(final Date date) {
        return getStartOfDay(DateUtils.addDays(date, -1));
    } 

    
	/**
	 * Returns 11:59:59.999 PM today of date.
	 *
	 * @param date Valid non-null java.util.Date object
	 * @return New date. Original is not altered.
	 */
    public static Date today(final Date date) {
        return getEndOfDay(date);
    } 


	/**
	 * Returns 11:59:59.999 PM tomorrow.
	 *
	 * @return New date. Original is not altered.
	 */
    public static Date tomorrow() {
        return getEndOfDay(DateUtils.addDays(new Date(), 1));
    } 


	/**
	 * Returns 00:01:01.000 yesterday.
	 *
	 * @return New date. Original is not altered.
	 */
    public static Date yesterday() {
        return getStartOfDay(DateUtils.addDays(new Date(), -1));
    } 


	/**
	 * Returns 11:59:59.999 PM today.
	 *
	 * @return New date. Original is not altered.
	 */
    public static Date today() {
        return getEndOfDay(new Date());
    }



    /**
	 * Clones a date passed as a parameter.
	 * 
     * NOTE: if date is null then null is returned.
     *
	 * @param date Valid non-null java.util.Date object
	 * @return New date. Original is not altered.
	 */
    public static Date clone(final Date date) {
        return (null != date) ? (Date)date.clone() : null;
    }

}
