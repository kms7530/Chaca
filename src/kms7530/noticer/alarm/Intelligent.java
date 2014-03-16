package kms7530.noticer.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;

import kms7530.noticer.defines.PrefDefines.PrefCMD;

import android.content.SharedPreferences;

public class Intelligent {
	// QUERY Set																			// 0 is can't understand
	private static final String[] MAP = {"지도", "내비게이션", "길", "가는지", "네비게이션", "맵"};	// 1
	private static final String[] PHONE = {"전화", "연락"};									// 2
	private static final String[] MESSAGE = {"문자", "메세지", "메세지"};						// 3
	private static final String[] LATER5 = {"5분", "오분", "5 분", "오 분"};					// 5
	private static final String[] NON = {"없", "아니", "됬어", "됐어"};							// 6
	private static final String[] LATER10 = {"10분", "십분", "10 분", "십 분"};					// 10
	private static final String[] LATER15 = {"15분", "십오분", "15 분", "십오 분"};				// 15
	private static final String[] LATER20 = {"20분", "이십분", "20 분", "이십 분"};				// 20
	private static final String[] LATER25 = {"25분", "이십오분", "25 분", "이십오 분"};			// 25
	private static final String[] LATER30 = {"30분", "삼십분", "30 분", "삼십 분"};				// 30
	
	public String getSaySentence(SharedPreferences pref, String title, String location) {
		String sentence = "";
		String name = pref.getString(PrefCMD.USER_NAME, "");
		
		sentence += "안녕하세요.  ";
		sentence += name+"님 ";
		sentence += "현재 시각"+this.getTime();
		sentence += title+"이라는 일정이 ";
		sentence += location+"에서 있습니다. ";
		sentence += "더 도와드릴게 있나요?";
		
		return sentence;
	}
	
	public int query(String said) {
		int what = 0;
		
		for(int i = 0;i<MAP.length;i++) {
			if(isThis(said, ".*"+MAP[i]+".*")) what = 1;
		}
		
		for(int i = 0;i<PHONE.length;i++) {
			if(isThis(said, ".*"+PHONE[i]+".*")) what = 2;
		}
		
		for(int i = 0;i<MESSAGE.length;i++) {
			if(isThis(said, ".*"+MESSAGE[i]+".*")) what = 3;
		}
		
		for(int i = 0;i<LATER5.length;i++) {
			if(isThis(said, ".*"+LATER5[i]+".*")) what = 5;
		}
		
		for(int i = 0;i<LATER10.length;i++) {
			if(isThis(said, ".*"+LATER10[i]+".*")) what = 10;
		}
		
		for(int i = 0;i<LATER15.length;i++) {
			if(isThis(said, ".*"+LATER15[i]+".*")) what = 15;
		}
		
		for(int i = 0;i<LATER20.length;i++) {
			if(isThis(said, ".*"+LATER20[i]+".*")) what = 20;
		}
		
		for(int i = 0;i<LATER25.length;i++) {
			if(isThis(said, ".*"+LATER25[i]+".*")) what = 25;
		}
		
		for(int i = 0;i<LATER30.length;i++) {
			if(isThis(said, ".*"+LATER30[i]+".*")) what = 30;
		}
		
		for(int i = 0;i<NON.length;i++) {
			if(isThis(said, ".*"+NON[i]+".*")) what = 6;
		}
		
		return what;
	}
	
	private boolean isThis(String said, String query) {
		boolean isThis = false;
		if(said.matches(query)) isThis = true;
		return isThis;
	}
	
	private String getTime() {
		// This method is for return current time
		Calendar cal = new GregorianCalendar();
		
		// Hour, Minute, Second
		int h = cal.get(Calendar.HOUR_OF_DAY);
		int m = cal.get(Calendar.MINUTE);
		
		String noon = "";
		String strH = "";
		String strM = "";
		
		if(h>12) {
			strH = h-12+"시";
			noon = "오후 ";
		}
		if(h == 0) {
			strH = "12시 ";
		}
		else {
			noon = "오전 ";
		}
		
		strM = m+"분 ";
		
		return noon+"  "+strH+strM+"입니다. ";
	}
	
	public String getNewEventSMS(String name, String title, String location, int iCon, Calendar dtStart, Calendar dtEnd) {
		String str = "";
		str = name+" "+title+" "+location+" "+iCon+" "+dtStart.getTimeInMillis()+" "+dtEnd.getTimeInMillis()+" #20727";
		return str;
	}
}
