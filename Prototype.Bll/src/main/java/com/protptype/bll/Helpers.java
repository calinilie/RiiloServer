package com.protptype.bll;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Helpers {
	

	public static String dateToString(Date date){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
    	return df.format(date);
    }
    
    public static Date stringToDate(String string){
    	try {
			return new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH).parse(string);
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    public static double distanceFrom(double lat1, double lng1, double lat2, double lng2) {
		double earthRadius = 6371;//in km 
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    double dist = earthRadius * c;
	    
	    return Double.valueOf(dist).doubleValue();
	}
    
}
