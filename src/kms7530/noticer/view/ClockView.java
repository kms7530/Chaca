package kms7530.noticer.view;

import java.util.Calendar;
import java.util.GregorianCalendar;

import kms7530.notier.R;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextSwitcher;
import android.widget.TextView;

public class ClockView extends Fragment {
	public static int h, m, s;
	private TextView txtClock[];
	private TextSwitcher txtSwitch;
	private ProgressBar barClock;
	private String first, now;
	public static Typeface typeface;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Inflate View
		View v = inflater.inflate(R.layout.view_clock, container, false);
		// Make TextSwitcher
		txtSwitch = (TextSwitcher)v.findViewById(R.id.txtClock);
		typeface = Typeface.createFromAsset(this.getActivity().getAssets(), "Roboto_Thin.ttf");
		txtClock = new TextView[2];
		txtClock[0] = (TextView)v.findViewById(R.id.txtClock_1);
		txtClock[1] = (TextView)v.findViewById(R.id.txtClock_2);
		// Set Font
		for(int i=0;i<2;i++) {
			txtClock[i].setTypeface(typeface);
			txtClock[i].setText(getTime());
		}
		
		// Set animation
		txtSwitch.setInAnimation(getActivity(), android.R.anim.fade_in);
		txtSwitch.setOutAnimation(getActivity(), android.R.anim.fade_out);
		first = getTime();
		
		// make BarClock
		barClock = (ProgressBar)v.findViewById(R.id.barClock);
		
		ClockViewThread thread = new ClockViewThread();
		thread.setDaemon(false);
		thread.start();
		
		return v;
	}
	
	private String getTime() {
		// This method is for return current time
		Calendar cal = new GregorianCalendar();
		
		// Hour, Minute, Second
		h=cal.get(Calendar.HOUR);
		m=cal.get(Calendar.MINUTE);
		s=cal.get(Calendar.SECOND);
		
		String strH;
		String strM;
		
		if(h<10) {
			strH="0"+String.valueOf(h);
		}
		else strH=String.valueOf(h);
		
		if(m<10) {
			strM="0"+String.valueOf(m);
		}
		else strM=String.valueOf(m);
		
		return strH+" : "+strM;
	}
	
	Handler handler = new Handler(){
		// It also work for time
		public void handleMessage(Message msg) {
			now = getTime();
			
			if(!first.equals(now)) {
				txtSwitch.setText(now);
				first = now;
			}
			
			barClock.setProgress(h*60+m);
		}
	};

	private class ClockViewThread extends Thread {
		// This Class is for time Thread
		@Override
		public void run() {
			while(true) {
				try{
					Thread.sleep(1000);
				}
				catch(Exception e) {}
				Message msg = new Message();
				handler.sendMessage(msg);
			}
		}
	}
}
