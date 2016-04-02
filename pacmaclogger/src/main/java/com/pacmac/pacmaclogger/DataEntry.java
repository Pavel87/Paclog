package com.pacmac.pacmaclogger;

import java.util.Calendar;

/**
 * Created by pacmac on 24/03/16.
 */
public class DataEntry {

    private String message = null;
    private Calendar calendar = null;


    public DataEntry(String m) {
        this.message = m;
        this.calendar = Calendar.getInstance();
    }


    public String getNewLine() {

        return getTimeStamp(calendar) + "  " + message;

    }

    private final String getTimeStamp(Calendar calendar) {
        return String.format("%02d", calendar.get(Calendar.DAY_OF_MONTH)) + "/" + String.format("%02d", calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR) +
                " " + String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE)) + ":" +
                String.format("%02d", calendar.get(Calendar.SECOND)) + "." + String.format("%03d", calendar.get(Calendar.MILLISECOND));
    }

}
