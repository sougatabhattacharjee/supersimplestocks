package com.jpmorgan.util;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sougata Bhattacharjee
 * On 17.03.18
 */
public class DateTimeUtil {

    /**
     * Return a date specific minutes before
     * @param minutes - how many minutes before
     * @return the time before X minutes
     */
    public static Date getMinutesBefore(final int minutes) {
        final Calendar now = Calendar.getInstance();
        now.add(Calendar.MINUTE, -minutes);
        return now.getTime();
    }
}
