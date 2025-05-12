/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.project.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author anPV
 */
public class DateUtil {
    public static Date[] getDayBoundary(Date dt){
        Date[] bound = {new Date(), new Date()};        
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);        
        
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);        
        bound[0] = cal.getTime();
        
        cal.add(Calendar.DAY_OF_MONTH, 1);
        bound[1] = cal.getTime();
        
        return bound;
    }
    
    public static Date getOffsetDay(Date dt, int days){        
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
    
    public static String toString(Date dt, String format){
        SimpleDateFormat df = new SimpleDateFormat(format);
        String sDate = df.format(dt);
        return sDate;
    }

    public static String toString(java.sql.Timestamp dt, String format){
        try {
            String formatted = dt.toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern(format));
            return formatted;
        }
        catch (Exception ex){
            return null;
        }
    }

    public static Date fromString(String dateStr, String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(dateStr);
            return date;
        }
        catch (Exception ex){
            return null;
        }
    }
}
