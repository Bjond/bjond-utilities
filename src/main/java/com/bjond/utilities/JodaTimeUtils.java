package com.bjond.utilities;

import lombok.val;

import org.joda.time.DateTime;
import org.joda.time.MutablePeriod;
import org.joda.time.Period;

public class JodaTimeUtils {
    
    public final static int SECONDS = 0;
    public final static int MINUTES = 1;
    public final static int HOURS = 2;
    public final static int DAYS = 3;
    public final static int WEEKS = 4;
    public final static int MONTHS = 5;
    public final static int YEARS = 6;
    
    public static DateTime ago(DateTime anchor, int ... valueUnitPairs) throws Exception {
        val period = toPeriod(valueUnitPairs);
        return anchor.minus(period);
    }
    
    public static Period toPeriod(int ... valueUnitPairs) throws Exception {
        if (valueUnitPairs.length == 0)
            throw new Exception("empty unit and value pair");
        
        if ((valueUnitPairs.length % 2) != 0)
            throw new Exception("unit and value must be specified in a pair (first unit, second value)");
        
        val p = new MutablePeriod();
        for (int i = 0; i < valueUnitPairs.length; i = i + 2) {
            val value = valueUnitPairs[i];
            val unit = valueUnitPairs[i+1];
            if (value < 0)
                throw new Exception("Negative value: " + value);
            switch (unit) {
            case SECONDS: 
                p.addSeconds(value);
                break;
            case MINUTES:
                p.addMinutes(value);
                break;
            case HOURS:
                p.addHours(value);
                break;
            case DAYS:
                p.addDays(value);
                break;
            case WEEKS:
                p.addWeeks(value);
                break;
            case MONTHS:
                p.addMonths(value);
                break;
            case YEARS:
                p.addYears(value);
                break;
            default:
                throw new Exception("Invalid unit: " + unit);
            }
        }
        
        return p.toPeriod();
    }
    
    public static Period toPeriodFromFloat(float value, int unit) throws Exception {
        if (unit == WEEKS) 
            throw new Exception("Fractional weeks not supported");
        
        val ret = new MutablePeriod();
        do {
            int whole = wholePart(value);
            float fractional = fractionalPart(value);
            val part = toPeriod(whole, unit);
            ret.add(part);
            unit--;
            if (unit == WEEKS)
                unit--; // skip weeks.
            if (unit < 0)
                continue;
            value = range(unit) * fractional;
            
        } while (unit >= 0);
        
        return ret.toPeriod();
    }
    
    public static boolean inTimeRange(DateTime t, DateTime rangeStart, DateTime rangeEnd) {
        return (t.isAfter(rangeStart) && t.isBefore(rangeEnd)) || t.isEqual(rangeStart) || t.isEqual(rangeEnd);
    }
    
    public static boolean outsideTimeRange(DateTime t, DateTime rangeStart, DateTime rangeEnd) {
        return t.isBefore(rangeStart) || t.isAfter(rangeEnd);
    }
    
    /**
     * Note: all time zone must be UTC.
     * 
     * @param now
     * @param valueUnitPairs Length must be even number.  Non-empty.  E.g., [ 180, TimeUnit.SECONDS, 90, TimeUnit.DAYS ]    
     * @return
     */
    public static boolean afterTimeSpanAgo(DateTime t, DateTime now, int ... valueUnitPairs) throws Exception {
        val ago = ago(now, valueUnitPairs);
        return t.isEqual(ago) || t.isAfter(ago);
    }
    
    /**
     * Note: all time zone must be UTC.
     * 
     * @param now
     * @param valueUnitPairs Length must be even number.  Non-empty.  E.g., [ 180, TimeUnit.SECONDS, 90, TimeUnit.DAYS ]    
     * @return
     * @throws Exception 
     */
    public static boolean beforeTimeSpanAgo(DateTime t, DateTime now, int ... valueUnitPairs) throws Exception {
        val ago = ago(now, valueUnitPairs);
        return t.isBefore(ago);
    }
    
    static int wholePart(float num) {
        return (int) num;
    }
    
    static float fractionalPart(float fnum) {
        int wholePart = wholePart(fnum); 
        return fnum - wholePart;
    }

    /**
     * Don't get excited.  It's only an approximation 
     */
    static int range(int unit) throws Exception {
        switch (unit) {
        case YEARS:
            return Integer.MAX_VALUE;
        case MONTHS:
            return 12;
//        case WEEKS:
//            return 4;
        case DAYS:
            return 30;
        case HOURS:
            return 24;
        case MINUTES:
            return 60;
        case SECONDS:
            return 60;
        default: 
            throw new Exception("Invalid unit: " + unit);
        }
    }
}
