package kms7530.noticer.alarm;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;

import kms7530.noticer.defines.IconDefines;
import kms7530.noticer.defines.PrefDefines;
import kms7530.noticer.defines.PrefDefines.PrefCMD;
import kms7530.noticer.view.helpers.ViewHelper;
import kms7530.notier.R;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;

public class AlarmActivity extends Activity implements OnClickListener, OnInitListener{
	private Bitmap userRow;
	private ImageView userImg;
	private MediaPlayer alarmSong;
	private int icon;
	private String title, hosterName, hoster, location, phoneNum;
	private TextSwitcher switcher;
	private TextView owner, clock, name;
	private BootstrapButton btnMap;
	private FontAwesomeText iCon;
	private SharedPreferences pref;
	private boolean isClock = true, isSay = false, isRecog = false;
	private LinearLayout layout;
	private static final String[] PROJECTION = new String[] {
		ContactsContract.CommonDataKinds.Phone._ID,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
        ContactsContract.CommonDataKinds.Phone.NUMBER,
        ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
        ContactsContract.CommonDataKinds.Phone.CONTACT_ID
	 };
	private TextToSpeech tts;
	private SayIt sayIt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);
		
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
		
		AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
		am.setStreamVolume(AudioManager.STREAM_MUSIC, 15, AudioManager.FLAG_VIBRATE);
		
		alarmSong = MediaPlayer.create(this, R.raw.dialing);
		try {
			alarmSong.prepare();
		} catch (IllegalStateException e) {} 
		catch (IOException e) {}
		alarmSong.start();
		
		pref = this.getSharedPreferences(PrefDefines.PrefName.SETTING, this.MODE_PRIVATE);
		
		icon = this.getIntent().getExtras().getInt("icon");
		title = this.getIntent().getExtras().getString("title");
		hoster = this.getIntent().getExtras().getString("hoster");
		hosterName = this.getOwnerName(hoster);
		location = this.getIntent().getExtras().getString("location");
		
		switcher = (TextSwitcher)findViewById(R.id.alarm_textSwit);
		switcher.setInAnimation(this, android.R.anim.slide_in_left);
		switcher.setOutAnimation(this, android.R.anim.slide_out_right);
		
		clock = (TextView)findViewById(R.id.alarm_Clock);
		clock.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto_Thin.ttf"));
		name = (TextView)findViewById(R.id.alarm_Event);
		name.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto_Thin.ttf"));
		
		owner = (TextView)findViewById(R.id.alarm_userName);
		owner.setText(hosterName);
		owner.setTypeface(Typeface.createFromAsset(getAssets(), "namsan.otf.mp3"));
		
		btnMap = (BootstrapButton)findViewById(R.id.alarm_btn_map);
		btnMap.setEnabled(false);
		if(location != null) {
			btnMap.setEnabled(true);
		}
		btnMap.setOnClickListener(this);
		
		iCon = (FontAwesomeText)findViewById(R.id.alarm_tag);
		iCon.setIcon(ViewHelper.getIcon(icon));
		
		layout = (LinearLayout)findViewById(R.id.alarm_back);
		layout.setBackgroundResource(this.getBack(icon));
		
		if(this.getOwnerImg()==null) {
			userImg = (ImageView)findViewById(R.id.alarmUserImg);
			userRow = BitmapFactory.decodeResource(getResources(), R.drawable.user);
			userImg.setImageBitmap(this.getRoundedCornerBitmap(userRow));
		}
		else {
			userImg = (ImageView)findViewById(R.id.alarmUserImg);
			userRow = BitmapFactory.decodeResource(getResources(), R.drawable.user);
			userImg.setImageBitmap(this.getRoundedCornerBitmap(this.getOwnerImg()));
		}
		
		new Title2Clock().start();
		sayIt = new SayIt();
		sayIt.start();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		tts = new TextToSpeech(this, this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		tts.shutdown();
	}
	
	private Bitmap getOwnerImg() {
		Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
		cursor.moveToFirst();
		Bitmap pic = null;
		
		while(cursor.moveToNext()) {
			String imgID;
			if(cursor.getString(1).equals(hosterName) && cursor.getString(2).equals(hoster)) {
				imgID = cursor.getString(3);
				Uri uri = Uri.withAppendedPath( ContactsContract.Data.CONTENT_URI, imgID);
				InputStream stream = null;
				try {
					stream = getContentResolver().openInputStream(uri);
					pic = BitmapFactory.decodeStream(stream);
					
				} catch (FileNotFoundException e) {}
				try {
					stream.close();
				} catch (IOException e) {}
			}
		}
		
		return pic;
	}
	
	private String getOwnerName(String phoneNom) {
		String name = "";
		
		if(phoneNom.equals("me")) name = pref.getString(PrefCMD.USER_NAME, "");
		else {
			Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, PROJECTION, null, null, null);
			cursor.moveToFirst();
			while(cursor.moveToNext()) {
				if(cursor.getString(2).equals(hoster)) {
					name = cursor.getString(1);
				}
			}
		}
		
		return name;
	}
	
	public Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), 
        		bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        float rx = bitmap.getHeight()/2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0); // 투명도화
        paint.setColor(color);
        canvas.drawRoundRect(rectF, rx, rx, paint); // 모서리 둥근 사각형

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); // 모서리 깎기 
        canvas.drawBitmap(bitmap, rect, rect, paint); // 이미지 설정

        return output;
    }
	
	private int getBack(int color) {
		int back = 0;
		
		if(color==IconDefines.BOOK) back = R.drawable.alarm_peter_river;
		if(color==IconDefines.BRIEFCASE) back = R.drawable.alarm_turquoise;
		if(color==IconDefines.CHEAK) back = R.drawable.alarm_carrot;
		if(color==IconDefines.COFFEE) back = R.drawable.alarm_emerald;
		if(color==IconDefines.GAMEPAD) back = R.drawable.alarm_amethst;
		if(color==IconDefines.HOME) back = R.drawable.alarm_sun_flower;
		
		return back;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		int what = 0;
		Intelligent intel = new Intelligent();
		
		if(requestCode==0 && resultCode==this.RESULT_OK) {
			String str="";
			ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			str+=result.get(0)+" ";
			what = intel.query(str);
		}
		
		if(what==1) {
			// Map
			tts.speak("네 여기 지도 입니다.", TextToSpeech.QUEUE_FLUSH, null);
			String auth  = "geo:0,0";
	        String query = "q="+location;
	        Uri location = Uri.parse(auth + "?" + query);
	        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
	        startActivity(mapIntent);
		}
		if(what==2) {
			// Call
			if(!hoster.equals("me")) {
				tts.speak("네 "+owner.getText()+"님에게 전화를 겁니다.", TextToSpeech.QUEUE_FLUSH, null);
				Uri nom = Uri.parse("tel:"+phoneNum);
				Intent intent = new Intent(Intent.ACTION_CALL, nom);
				startActivity(intent);
			}
			else {
				tts.speak("죄송합니다. 전화를 걸 수 없습니다.", TextToSpeech.QUEUE_FLUSH, null);
				isRecog = false;
			}
		}
		if(what==3) {
			// Send msg
			if(!hoster.equals("me")) {
				tts.speak("네 문자를 작성해 주세요", TextToSpeech.QUEUE_FLUSH, null);
				Uri uri = Uri.parse("smsto:"+phoneNum);
				Intent it = new Intent(Intent.ACTION_SENDTO, uri); 
				startActivity(it);
			}
			else {
				tts.speak("죄송합니다. 문자를 보낼 수 없습니다.", TextToSpeech.QUEUE_FLUSH, null);
				isRecog = false;
			}
		}
		if(what%5 == 0 && what/5 != 0) {
			// Send msg
			tts.speak("네 "+what+"분 후에 다시 알람 드리겠습니다.", TextToSpeech.QUEUE_FLUSH, null);
			
			Intent intent=new Intent(this, AlarmActivity.class);
			
			intent.putExtra("title", title);
			intent.putExtra("hoster", hoster);
			intent.putExtra("icon", icon);
			intent.putExtra("location", location);
			Calendar calender = GregorianCalendar.getInstance();
			calender.setTimeInMillis(GregorianCalendar.getInstance().getTimeInMillis()+60*1000*what);
			
			PendingIntent pIntent=PendingIntent.getActivity(this, 0, intent, 0);
			AlarmManager am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
			am.set(AlarmManager.RTC_WAKEUP, calender.getTimeInMillis(), pIntent);
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			this.finish();
		}
		if(what==6) {
			tts.speak("네 알겠습니다.", TextToSpeech.QUEUE_FLUSH, null);
		}
		if(what==0) {
			//tts.speak("죄송합니다. 다시 말씀해 주세요.", TextToSpeech.QUEUE_FLUSH, null);
			//isRecog = false;
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private String getTime() {
		// This method is for return current time
		Calendar cal = new GregorianCalendar();
		
		// Hour, Minute, Second
		int h=cal.get(Calendar.HOUR);
		int m=cal.get(Calendar.MINUTE);
		
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
			if(!isClock) {
				switcher.setText(title);
				isClock = true;
			}
			else {
				switcher.setText(AlarmActivity.this.getTime());
				isClock = false;
			}
		}
	};
	
	private void hear() {
		try {
			Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(
					RecognizerIntent.EXTRA_LANGUAGE_MODEL, 
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			intent.putExtra(
					RecognizerIntent.EXTRA_PROMPT, 
					"말하세요.");
			startActivityForResult(intent, 0);
		} catch(Exception e){}
	}
	
	Handler say = new Handler(){
		// It also work for time
		public void handleMessage(Message msg) {
			if(!alarmSong.isPlaying() && !isSay) {
				Intelligent intel = new Intelligent();
				tts.speak(intel.getSaySentence(pref, title, location), TextToSpeech.QUEUE_FLUSH, null);
				isSay = true;
			}
			if(isSay && !AlarmActivity.this.tts.isSpeaking() && !isRecog) {
				AlarmActivity.this.hear();
				isRecog = true;
			}
		}
	};

	@Override
	public void onClick(View v) {
		if(v==btnMap) {
			String auth  = "geo:0,0";
	        String query = "q="+location;
	        Uri location = Uri.parse(auth + "?" + query);
	        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
	        startActivity(mapIntent);
		}
	}

	@Override
	public void onInit(int status) {}
	
	private class SayIt extends Thread {
		@Override
		public void run() {
			while(true) {
				try{
					Thread.sleep(1025);
				}
				catch(Exception e) {}
				Message msg = new Message();
				say.sendMessage(msg);
			}
		}
	}
	
	private class Title2Clock extends Thread {
		@Override
		public void run() {
			while(true) {
				try{
					Message msg = new Message();
					handler.sendMessage(msg);
					Thread.sleep(3000);
				}
				catch(Exception e) {
					
				}
			}
		}
	}
}
