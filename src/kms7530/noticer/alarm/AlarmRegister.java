package kms7530.noticer.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;

import kms7530.noticer.defines.IconDefines;
import kms7530.noticer.view.helpers.CalendarHelper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

public class AlarmRegister extends BroadcastReceiver {
	private Cursor cursor;
	private Context context;

	@Override
	public void onReceive(Context context, Intent i) {
		this.context = context;
		CalendarHelper helper = new CalendarHelper(context);
		cursor = helper.getEvents();
		Toast.makeText(context, "Booting", Toast.LENGTH_LONG).show();
		while (cursor.moveToNext()) {
			if(cursor.getLong(4)==IconDefines.BOOK || cursor.getLong(4)==IconDefines.BRIEFCASE || cursor.getLong(4)==IconDefines.CHEAK || 
				cursor.getLong(4)==IconDefines.COFFEE || cursor.getLong(4)==IconDefines.GAMEPAD || cursor.getLong(4)==IconDefines.HOME || 
				cursor.getLong(4)==IconDefines.USER) {
				registAlarm();
		    }
		}
	}
	
	public void registAlarm() {
		//Log.e(cursor.getString(0), String.valueOf(cursor.getLong(2)));
		Calendar now = GregorianCalendar.getInstance();
		now.set(Calendar.SECOND, 0);
		Calendar calender = GregorianCalendar.getInstance();
		calender.set(Calendar.SECOND, 0);
		calender.setTimeInMillis(cursor.getLong(2));
		
		String rrule = cursor.getString(6);
		
		if(calender.after(now)) {
			Toast.makeText(context, cursor.getString(0), Toast.LENGTH_SHORT).show();
			Toast.makeText(context, calender.get(Calendar.MINUTE)+":"+calender.get(Calendar.SECOND), Toast.LENGTH_SHORT).show();
			Intent intent=new Intent(context, AlarmActivity.class);
			
			intent.putExtra("title", cursor.getString(0));
			intent.putExtra("hoster", cursor.getString(1));
			intent.putExtra("icon", cursor.getInt(4));
			intent.putExtra("location", cursor.getString(5));
			
			PendingIntent pIntent=PendingIntent.getActivity(context, 0, intent, 0);
			AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pIntent);
		}
		
		if(rrule != null) {
			rruleRegister(GregorianCalendar.getInstance(), calender, rrule);
		}
	}
	
	public void rruleRegister(Calendar now, Calendar start, String rrule) {
		if(rrule.matches(".*"+CalendarHelper.getDay(now)+".*")) {
			now.set(Calendar.HOUR_OF_DAY, start.get(Calendar.HOUR_OF_DAY));
			now.set(Calendar.MINUTE, start.get(Calendar.MINUTE));
			now.set(Calendar.SECOND, 0);
			
			Intent intent=new Intent(context, AlarmActivity.class);
			
			intent.putExtra("title", cursor.getString(0));
			intent.putExtra("hoster", cursor.getString(1));
			intent.putExtra("icon", cursor.getInt(4));
			intent.putExtra("location", cursor.getString(5));
			
			PendingIntent pIntent=PendingIntent.getActivity(context, 0, intent, 0);
			AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, now.getTimeInMillis(), pIntent);
		}
	}
}