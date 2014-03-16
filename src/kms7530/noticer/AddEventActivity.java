package kms7530.noticer;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapEditText;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import kms7530.noticer.alarm.AlarmRegister;
import kms7530.noticer.alarm.Intelligent;
import kms7530.noticer.data.ContectValue;
import kms7530.noticer.data.TagBox;
import kms7530.noticer.defines.PrefDefines;
import kms7530.noticer.defines.PrefDefines.PrefCMD;
import kms7530.noticer.defines.PrefDefines.PrefName;
import kms7530.noticer.view.adapter.SlidingSubMenu;
import kms7530.noticer.view.helpers.CalendarHelper;
import kms7530.notier.R;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.CalendarContract.Events;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.telephony.SmsManager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddEventActivity extends Activity implements OnClickListener{
	private TextView startDate, endDate, startTime, endTime;
	private BootstrapButton btnOK, btnDel;
	private BootstrapEditText edtTitle, edtLocation;
	private Calendar start, end;
	private int pos;
	private SharedPreferences pref;
	private long calID;
	private boolean isRenew = false;
	private String host;
	
	public SelectContectDialog contectDialog;
	
	public static int newTag = 0;
	public static ArrayList<ContectValue> contectList = new ArrayList<ContectValue>();
	public static Typeface roboto_Thin, roboto_Light;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.init();
		setContentView(R.layout.activity_add_event);
		
		PowerManager pm=(PowerManager)getSystemService(Context.POWER_SERVICE);
		 
		PowerManager.WakeLock sCpuWakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "Alarm");
        sCpuWakeLock.acquire();
        
        StrictMode.setThreadPolicy(
				new StrictMode.ThreadPolicy.Builder()
			    .detectDiskReads()
			    .detectDiskWrites()
			    .detectNetwork()
			    .penaltyLog().build()
		    );
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
        		WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
        		| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		
		roboto_Light = Typeface.createFromAsset(getAssets(), "Roboto_Light.ttf");
		roboto_Thin = Typeface.createFromAsset(getAssets(), "Roboto_Thin.ttf");
		
		startDate = (TextView)findViewById(R.id.eventDate_Start);
		startDate.setTypeface(roboto_Light);
		startDate.setOnClickListener(this);
		
		startTime = (TextView)findViewById(R.id.eventTime_start);
		startTime.setTypeface(roboto_Thin);
		startTime.setOnClickListener(this);
		
		endDate = (TextView)findViewById(R.id.eventDate_finish);
		endDate.setTypeface(roboto_Light);
		endDate.setOnClickListener(this);
		
		endTime = (TextView)findViewById(R.id.eventTime_Finish);
		endTime.setTypeface(roboto_Thin);
		endTime.setOnClickListener(this);
		
		btnOK = (BootstrapButton)findViewById(R.id.addEvent);
		btnOK.setOnClickListener(this);
		
		btnDel = (BootstrapButton)findViewById(R.id.delEvent);
		btnDel.setOnClickListener(this);
		btnDel.setText("뒤로");
		
		edtLocation = (BootstrapEditText)findViewById(R.id.addEventLocation);
		edtTitle = (BootstrapEditText)findViewById(R.id.addEventName);
		
		contectDialog = new SelectContectDialog(this);
		start = GregorianCalendar.getInstance();
		end = GregorianCalendar.getInstance();
		
		pref = this.getSharedPreferences(PrefDefines.PrefName.SETTING, this.MODE_PRIVATE);
		
		if(this.getIntent().getExtras().getString("location")!=null) {
			this.setInfo();
		}
	}
	
	private void setInfo() {
		String title = this.getIntent().getExtras().getString("title");
		String location = this.getIntent().getExtras().getString("location");
		calID = this.getIntent().getExtras().getLong("calID");
		start = (Calendar)this.getIntent().getExtras().get("dtStart");
		end = (Calendar)this.getIntent().getExtras().get("dtEnd");
		if(this.getIntent().getExtras().getInt("pos") != 0) {
			pos = this.getIntent().getExtras().getInt("pos");
		}
		if(this.getIntent().getExtras().getString("host") != "") {
			host = this.getIntent().getExtras().getString("host");
		}
		btnDel.setText("삭제");
		newTag = this.getIntent().getExtras().getInt("icon");
		
		this.edtTitle.setText(title);
		this.edtLocation.setText(location);
		endTime.setText(this.getTime(end));
		endDate.setText(this.getDate(end));
		startTime.setText(this.getTime(start));
		startDate.setText(this.getDate(start));
		if(calID != 0) {
			isRenew = true;
		}
	}
	
	private String getTime(Calendar cal) {
		// Hour, Minute, Second
		int h=cal.get(Calendar.HOUR);
		int m=cal.get(Calendar.MINUTE);
		
		String strH;
		String strM;
		
		if(h<10) {
			strH="0"+String.valueOf(h);
		}
		else if(h==0) {
			strH="12";
		}
		else strH=String.valueOf(h);
		
		if(m<10) {
			strM="0"+String.valueOf(m);
		}
		else strM=String.valueOf(m);
		
		return strH+" : "+strM;
	}
	
	private String getDate(Calendar cal) {
		// Hour, Minute, Second
		int mNom=cal.get(Calendar.MONTH);
		int dNom=cal.get(Calendar.DATE);
		
		String m = mNom+1+"";
		String d = dNom+"";
		
		return m+"/"+d;
	}
	
	public void init() {
		if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		else {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
		
		//SlidingSubMenu.pos = 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if(v==startDate) {
			new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					start.set(year, monthOfYear, dayOfMonth);
					startDate.setText(AddEventActivity.this.getDate(start));
				}
			}, new Date().getYear()+1900, new Date().getMonth(), new Date().getDate()
			).show();
		}
		if(v==startTime) {
			new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					start.set(Calendar.HOUR_OF_DAY, hourOfDay);
					start.set(Calendar.MINUTE, minute);
					start.set(Calendar.MILLISECOND, 0);
					startTime.setText(AddEventActivity.this.getTime(start));
				}
			}, new Date().getHours(), new Date().getMinutes(), true).show();
		}
		if(v==endDate) {
			new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					end.set(year, monthOfYear, dayOfMonth);
					endDate.setText(AddEventActivity.this.getDate(end));
				}
			}, new Date().getYear()+1900, new Date().getMonth(), new Date().getDate()
			).show();
		}
		if(v==endTime) {
			new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
				@Override
				public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
					end.set(Calendar.HOUR_OF_DAY, hourOfDay);
					end.set(Calendar.MINUTE, minute);
					end.set(Calendar.MILLISECOND, 0);
					endTime.setText(AddEventActivity.this.getTime(end));
				}
			}, new Date().getHours(), new Date().getMinutes(), true).show();
		}
		if(v==btnOK) {
			boolean state = true;
			
			if(edtTitle.getText().equals("") || start.equals(end) || start.after(end) || newTag == 0) state = false;
			
			if(state && !isRenew) {
				this.registEvent();
			}
			else if(state && isRenew) {
				this.renewEvent();
			}
			else {
				View layout = View.inflate(this, R.layout.toast, null);
				
				TextView tv = (TextView)layout.findViewById(R.id.toastText);
				tv.setText("내용을 확인하세요.");
				FontAwesomeText iCon = (FontAwesomeText)layout.findViewById(R.id.toastIcon);
				iCon.setIcon("fa-exclamation");
				
				Toast toast = new Toast(getApplicationContext());
            	toast.setGravity(Gravity.CENTER, 0, 0);
            	toast.setDuration(Toast.LENGTH_SHORT);
            	toast.setView(layout);
            	toast.show();
			}
		}
		if(v==btnDel) {
			if(isRenew) {
				this.removeEvent();
			}
			else {
				finish();
			}
		}
	}
	
	private void renewEvent() {
		new CalendarHelper(this).renewEvent(edtTitle.getText().toString(), edtLocation.getText().toString(), start, end, 
											"", newTag, calID);
		MainActivity.datas.set(pos, new TagBox(newTag, edtTitle.getText().toString(), start.get(Calendar.HOUR), 
												start.get(Calendar.MINUTE), start.get(Calendar.AM_PM), calID, 
												edtLocation.getText().toString(), start, end));
		
		View layout = View.inflate(this, R.layout.toast, null);
		Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.CENTER, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
    	
    	if(!contectList.isEmpty()) {
    		for(int i=0;i<contectList.size();i++) {
    			this.SendSMS(contectList.get(i).getPhoneNum(), 
    						new Intelligent().getNewEventSMS(pref.getString(PrefCMD.USER_NAME, ""), edtTitle.getText().toString(), 
    						edtLocation.getText().toString(), newTag, start, end));
    		}
    	}
    	
    	this.sendBroadcast(new Intent(this, AlarmRegister.class));
    	
    	finish();
	}
	
	private void removeEvent() {
		new CalendarHelper(this).deleteEvent(calID);
		
		MainActivity.datas.remove(pos);
		View layout = View.inflate(this, R.layout.toast, null);
		TextView tv = (TextView)layout.findViewById(R.id.toastText);
		tv.setText("삭제");
		FontAwesomeText iCon = (FontAwesomeText)layout.findViewById(R.id.toastIcon);
		iCon.setIcon("fa-trash-o");
		Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.CENTER, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
    	
		finish();
	}
	
	private void registEvent() {
		new CalendarHelper(this).addEvent(edtTitle.getText().toString(), edtLocation.getText().toString(), start, end, "", newTag);
		
		View layout = View.inflate(this, R.layout.toast, null);
		Toast toast = new Toast(getApplicationContext());
    	toast.setGravity(Gravity.CENTER, 0, 0);
    	toast.setDuration(Toast.LENGTH_SHORT);
    	toast.setView(layout);
    	toast.show();
    	
    	this.sendBroadcast(new Intent(this, AlarmRegister.class));
    	if(MainActivity.datas!=null){
    		MainActivity.datas.add(new TagBox(newTag, edtTitle.getText().toString(), start.get(Calendar.HOUR_OF_DAY), start.get(Calendar.MINUTE), 
					start.get(Calendar.AM_PM), calID, edtLocation.getText().toString(), start, end));
    	}
    	
    	if(!contectList.isEmpty()) {
    		for(int i=0;i<contectList.size();i++) {
    			this.SendSMS(contectList.get(i).getPhoneNum(), 
    						new Intelligent().getNewEventSMS(pref.getString(PrefCMD.USER_NAME, ""), edtTitle.getText().toString(), 
    						edtLocation.getText().toString(), newTag, start, end));
    		}
    	}
		
		this.finish();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		contectList.clear();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK) {
			boolean isHe = true;
			String[] q = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER};
			Cursor cursor = getContentResolver().query(data.getData(), q, null, null, null);
			cursor.moveToFirst();
			
			for(int i=0;i<contectList.size();i++) {
				if((contectList.get(i).getName().equals(cursor.getString(0)) && contectList.get(i).getPhoneNum().equals(cursor.getString(1)))) {
					isHe = false;
				}
			}
			
			if(isHe) contectDialog.refresh(cursor.getString(0), cursor.getString(1));
			else Toast.makeText(this, "이미 추가되어 있습니다.", Toast.LENGTH_SHORT).show();
			cursor.close();
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
	
	public void selectTag(View v) {
		TagPickerDialog dialog = new TagPickerDialog(this);
		dialog.show();
	}
	
	public void selectContect(View v) {
		contectDialog.show();
	}
	
	private void SendSMS(String phonenumber, String message) {
        SmsManager smsManager = SmsManager.getDefault();
        String sendTo = phonenumber;
        String myMessage = message;
        smsManager.sendTextMessage(sendTo, null, myMessage, null, null);
        finish();
    }
}
