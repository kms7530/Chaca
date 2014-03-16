package kms7530.noticer.view.helpers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import kms7530.noticer.data.TagBox;
import kms7530.noticer.defines.DateDefines;
import kms7530.noticer.defines.IconDefines;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract.Calendars;
import android.provider.CalendarContract.Events;
import android.util.Log;

public class CalendarHelper {
	private Context context;
	public static int cal_id;
	
	// Quary Projection
	public static final String[] EVENT_PROJECTION = new String[] {
	    Events.TITLE,					// Title			0
	    Events.DESCRIPTION,				// Host				1
	    Events.DTSTART,					// StartTime		2
	    Events.DTEND,					// EndTime			3
	    Events.EVENT_COLOR,				// TagIcon			4
	    Events.EVENT_LOCATION,			// Location			5
	    Events.RRULE,					// Rotation			6
		"calendar_id",					// Calendar ID		7
		Calendars.OWNER_ACCOUNT, 		// Owner_Account	8
		Calendars._ID					// Event Nom		9
	};
	
	public CalendarHelper(Activity context) {
		this.context = context;
	}
	
	public CalendarHelper(Context context) {
		this.context = context;
	}

	// return TagBoxes that using in mainActvtivity
	public ArrayList<TagBox> getTagBoxList() {
		ArrayList<TagBox> list = new ArrayList<TagBox>();
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Events.CONTENT_URI;
		cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
		while (cur.moveToNext()) {
			String title;
			int h, m, iConID, am_pm;
			Log.e(cur.getString(0), String.valueOf(cur.getInt(7)));
			Log.e(cur.getString(0), String.valueOf(cur.getString(8)));
			
		    if(cur.getLong(4)==IconDefines.BOOK || cur.getLong(4)==IconDefines.BRIEFCASE || cur.getLong(4)==IconDefines.CHEAK || 
		    	cur.getLong(4)==IconDefines.COFFEE || cur.getLong(4)==IconDefines.GAMEPAD || cur.getLong(4)==IconDefines.HOME || 
		    	cur.getLong(4)==IconDefines.USER) {
	    		title = cur.getString(0);
	    		Calendar cal = new GregorianCalendar();
	    		cal.setTimeInMillis(cur.getLong(2));
	    		h = cal.get(Calendar.HOUR);
	    		m = cal.get(Calendar.MINUTE);
	    		iConID = cur.getInt(4);
	    		am_pm = cal.get(Calendar.AM_PM);
	    		long calID = cur.getInt(9);
	    		String location = cur.getString(5);
	    		Calendar dtStart = GregorianCalendar.getInstance();
	    		Calendar dtEnd = GregorianCalendar.getInstance();
	    		dtStart.setTimeInMillis(cur.getLong(2));
	    		dtEnd.setTimeInMillis(cur.getLong(3));
	    		
	    		if(GregorianCalendar.getInstance().before(dtStart)) {
	    			list.add(new TagBox(iConID, title, h, m, am_pm, calID, location, dtStart, dtEnd));
	    		}
		    }
		    if(cur.getString(8).matches(".*gmail.com.*")) {
		    	cal_id = cur.getInt(7);
		    }
		}
		
		return list;
	}
	
	public ArrayList<TagBox> getTagBoxList(int iCon) {
		ArrayList<TagBox> list = new ArrayList<TagBox>();
		Cursor cur = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Events.CONTENT_URI;
		cur = cr.query(uri, EVENT_PROJECTION, null, null, null);
		while (cur.moveToNext()) {
			String title;
			int h, m, iConID, am_pm;
			Log.e(cur.getString(0), String.valueOf(cur.getInt(7)));
			Log.e(cur.getString(0), String.valueOf(cur.getString(8)));
			
		    if(cur.getLong(4)==IconDefines.BOOK || cur.getLong(4)==IconDefines.BRIEFCASE || cur.getLong(4)==IconDefines.CHEAK || 
		    	cur.getLong(4)==IconDefines.COFFEE || cur.getLong(4)==IconDefines.GAMEPAD || cur.getLong(4)==IconDefines.HOME || 
		    	cur.getLong(4)==IconDefines.USER) {
	    		title = cur.getString(0);
	    		Calendar cal = new GregorianCalendar();
	    		cal.setTimeInMillis(cur.getLong(2));
	    		h = cal.get(Calendar.HOUR);
	    		m = cal.get(Calendar.MINUTE);
	    		iConID = cur.getInt(4);
	    		am_pm = cal.get(Calendar.AM_PM);
	    		long calID = cur.getInt(9);
	    		String location = cur.getString(5);
	    		Calendar dtStart = GregorianCalendar.getInstance();
	    		Calendar dtEnd = GregorianCalendar.getInstance();
	    		dtStart.setTimeInMillis(cur.getLong(2));
	    		dtEnd.setTimeInMillis(cur.getLong(3));
	    		
	    		if(GregorianCalendar.getInstance().before(dtStart) && iConID==iCon) {
	    			list.add(new TagBox(iConID, title, h, m, am_pm, calID, location, dtStart, dtEnd));
	    		}
		    }
		    if(cur.getString(8).matches(".*gmail.com.*")) {
		    	cal_id = cur.getInt(7);
		    }
		}
		
		return list;
	}
	
	// return Event cursor
	public Cursor getEvents() {
		Cursor curRow = null;
		ContentResolver cr = context.getContentResolver();
		Uri uri = Events.CONTENT_URI;
		curRow = cr.query(uri, EVENT_PROJECTION, null, null, null);
		
		return curRow;
	}
	
	public void deleteEvent(long calID) {
		Uri deleteUri = ContentUris.withAppendedId(Events.CONTENT_URI, calID);
		context.getContentResolver().delete(deleteUri, null, null);
	}
	
	// Renew Event 
	public void renewEvent(String title, String location, Calendar dtStart, Calendar dtEnd, String rRule, int tag, long calID) {
		ContentValues eventValues = new ContentValues();
		Uri updateUri = null;
		// The new title for the event
		eventValues.put("calendar_id", cal_id); 							// id, We need to choose from our mobile for primary its 1
		eventValues.put("title", title); 									// Title
		eventValues.put("eventLocation", location); 						// Location
		eventValues.put("dtstart", dtStart.getTimeInMillis()); 				// Start Time
		eventValues.put("dtend", dtEnd.getTimeInMillis()); 					// End Time
		eventValues.put("eventTimezone", TimeZone.getDefault().getID()); 	// Timezone
		eventValues.put("eventStatus", 1); 									// This information is sufficient for     most entries tentative (0), confirmed (1) or canceled (2):
		eventValues.put("hasAlarm", 0); 									// 0 for false, 1 for true
		eventValues.put("rrule", rRule); 									// Rotation
		eventValues.put("eventColor", tag);									// Tag
		eventValues.put(Events.DESCRIPTION, "me");							// owner
		updateUri = ContentUris.withAppendedId(Events.CONTENT_URI, calID);
		context.getContentResolver().update(updateUri, eventValues, null, null);
	}
	
	// Regist on calendar without user
	public void addEvent(String title, String location, Calendar dtStart, Calendar dtEnd, String rRule, int tag) {
		Uri eventUriString;
		
		dtStart.set(Calendar.SECOND, 0);
		dtEnd.set(Calendar.SECOND, 0);

		 if(Build.VERSION.SDK_INT >= 8)
		      eventUriString  = Uri.parse("content://com.android.calendar/events");
		else
		      eventUriString  = Uri.parse("content://calendar/events");
		             
		ContentValues eventValues = new ContentValues();

		eventValues.put("calendar_id", cal_id); 							// id, We need to choose from our mobile for primary its 1
		eventValues.put("title", title); 									// Title
		eventValues.put("eventLocation", location); 						// Location
		eventValues.put("dtstart", dtStart.getTimeInMillis()); 				// Start Time
		eventValues.put("dtend", dtEnd.getTimeInMillis()); 					// End Time
		eventValues.put("eventTimezone", TimeZone.getDefault().getID()); 	// Timezone
		eventValues.put("eventStatus", 1); 									// This information is sufficient for     most entries tentative (0), confirmed (1) or canceled (2):
		eventValues.put("hasAlarm", 0); 									// 0 for false, 1 for true
		eventValues.put("rrule", rRule); 									// Rotation
		eventValues.put("eventColor", tag);									// Tag
		eventValues.put(Events.DESCRIPTION, "me");							// owner
		context.getContentResolver().insert(eventUriString, eventValues);
	}
	
	// Regist on calendar with user
	public void addEvent(String title, String location, Calendar dtStart, Calendar dtEnd, String rRule, int tag, String owner) {
		Uri eventUriString;
		
		dtStart.set(Calendar.SECOND, 0);
		dtEnd.set(Calendar.SECOND, 0);

		 if(Build.VERSION.SDK_INT >= 8)
		      eventUriString  = Uri.parse("content://com.android.calendar/events");
		else
		      eventUriString  = Uri.parse("content://calendar/events");
		             
		ContentValues eventValues = new ContentValues();

		eventValues.put("calendar_id", cal_id); 							// id, We need to choose from our mobile for primary its 1
		eventValues.put("title", title); 									// Title
		eventValues.put("eventLocation", location); 						// Location
		eventValues.put("dtstart", dtStart.getTimeInMillis()); 				// Start Time
		eventValues.put("dtend", dtEnd.getTimeInMillis()); 					// End Time
		eventValues.put("eventTimezone", TimeZone.getDefault().getID()); 	// Timezone
		eventValues.put("eventStatus", 1); 									// This information is sufficient for     most entries tentative (0), confirmed (1) or canceled (2):
		eventValues.put("hasAlarm", 0); 									// 0 for false, 1 for true
		eventValues.put("rrule", rRule); 									// Rotation
		eventValues.put("eventColor", tag);									// Tag
		eventValues.put(Events.DESCRIPTION, owner);							// owner
		//Uri uri = context.getContentResolver().insert(eventUriString, eventValues);
		context.getContentResolver().insert(eventUriString, eventValues);
	}
	
	// Destigush what day of week
	public static String getDay(Calendar now) {
		String day = "";
		int dayNom = now.get(Calendar.DAY_OF_WEEK);
		
		if(dayNom == Calendar.SUNDAY) day = DateDefines.SUN;
		if(dayNom == Calendar.MONDAY) day = DateDefines.MON;
		if(dayNom == Calendar.TUESDAY) day = DateDefines.TUE;
		if(dayNom == Calendar.WEDNESDAY) day = DateDefines.WED;
		if(dayNom == Calendar.THURSDAY) day = DateDefines.THU;
		if(dayNom == Calendar.FRIDAY) day = DateDefines.FRI;
		if(dayNom == Calendar.SATURDAY) day = DateDefines.SAT;
		return day;
	}
}
