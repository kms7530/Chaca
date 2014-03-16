package kms7530.noticer.alarm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import kms7530.noticer.AddEventActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReciver extends BroadcastReceiver {
	private SmsMessage msgs[];
	public static String sSender[];
	public static String sBody[];

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle=intent.getExtras();
		msgs=null;
		sSender=null;
		sBody=null;
		
		if(bundle!=null) {
			Object[] pdus=(Object[])bundle.get("pdus");
			int len = pdus.length;
			msgs=new SmsMessage[len];
			sSender=new String[len];
			sBody=new String[len];
			for(int i=0;i<msgs.length;i++) {
				msgs[i]=SmsMessage.createFromPdu((byte[])pdus[i]);
				sSender[i]=msgs[i].getOriginatingAddress();
				sBody[i]=msgs[i].getMessageBody();
			}
		}
		if(sBody[0].matches(".*"+"20727"+".*")) {
			String[] infos = new String[6];
			StringTokenizer toq = new StringTokenizer(sBody[0], " ");
			for(int i=0;i<6;i++) {
				infos[i] = toq.nextToken();
			}
			Calendar start = GregorianCalendar.getInstance();
			Log.e("aaaa", infos[4]);
			start.setTimeInMillis(Long.valueOf(infos[4]));
			Calendar end = GregorianCalendar.getInstance();
			end.setTimeInMillis(Long.valueOf(infos[5]));
			Intent goRegist = new Intent(context, AddEventActivity.class);
			goRegist.putExtra("title", infos[1]);
			goRegist.putExtra("location", infos[2]);
			goRegist.putExtra("icon", infos[3]);
			goRegist.putExtra("dtStart", start);
			goRegist.putExtra("dtEnd", end);
			goRegist.putExtra("host", sSender[0]);
			goRegist.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(goRegist);
		}
	}
}