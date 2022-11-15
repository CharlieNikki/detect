package com.example.detect.utils;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    public static String dateFormat() {

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.CHINA);
        DateFormat timeFormat = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.CHINA);
        return dateFormat.format(new Date()) + " " + timeFormat.format(new Date());
    }
}
