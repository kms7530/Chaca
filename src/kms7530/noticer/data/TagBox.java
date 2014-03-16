package kms7530.noticer.data;

import java.util.Calendar;

public class TagBox {
	private String title, location;
	private int iConID, am_pm;
	private long calID;
	private Calendar dtStart, dtEnd;
	public int hour, minute;
	
	public TagBox(int eventNum, String title, int hour, int minute, int am_pm, long calID, String location, Calendar dtStart, Calendar dtEnd) {
		this.iConID = eventNum;
		this.title = title;
		this.hour = hour;
		this.minute = minute;
		this.am_pm = am_pm;
		this.calID = calID;
		this.location = location;
		this.dtStart = dtStart;
		this.dtEnd = dtEnd;
	}
	
	public Calendar getDtStart() {
		return dtStart;
	}
	
	public Calendar getDtEnd() {
		return dtEnd;
	}
	
	public String getTitle() {
		return title;
	}
	
	public int getEventNum() {
		return iConID;
	}
	
	public String getLocation() {
		return location;
	}
	
	public String getAmPm() {
		String str="";
		if(am_pm==0) str = "AM";
		else str = "PM";
		return str;
	}
	
	public long getCalID() {
		return calID;
	}
}
