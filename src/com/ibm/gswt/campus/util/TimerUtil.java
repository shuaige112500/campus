package com.ibm.gswt.campus.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimerUtil {
	public static String formateDate(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str=sdf.format(date);
		return str;
	}
	
	public static int getCurrentDay(){
		Calendar c = Calendar.getInstance();
		int date = c.get(Calendar.DATE); 
		return date;
	}
	
	public static String getCurrentTime(Date date){
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
		String str=sdf.format(date);
		return str;
	}
}
