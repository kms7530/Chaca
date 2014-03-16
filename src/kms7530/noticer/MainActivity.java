package kms7530.noticer;

import java.util.ArrayList;

import tmp.SubMenuAdapter;

import kms7530.noticer.alarm.AlarmActivity;
import kms7530.noticer.data.DrawerData;
import kms7530.noticer.data.TagBox;
import kms7530.noticer.defines.IconDefines;
import kms7530.noticer.defines.PrefDefines;
import kms7530.noticer.defines.PrefDefines.PrefCMD;
import kms7530.noticer.view.adapter.SlidingSubMenu;
import kms7530.noticer.view.adapter.TagBoxAdapter;
import kms7530.noticer.view.helpers.CalendarHelper;
import kms7530.notier.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements ListView.OnItemClickListener, SensorEventListener{
	// UI
	private ListView timeTable;
	private CalendarHelper helper;
	public static ArrayList<TagBox> datas;
	//public static ArrayList<DrawerData> drawerData;
	private SubMenuAdapter adp;
	public static SlidingSubMenu drawer_adapter;
	public static TagBoxAdapter adapter;
	private SharedPreferences pref;
	boolean is = false;
	
	// Motion
	private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;
    private float x, y, z;
    private static final int SHAKE_THRESHOLD = 800;
    private static final int DATA_X = SensorManager.DATA_X;
    private static final int DATA_Y = SensorManager.DATA_Y;
    private static final int DATA_Z = SensorManager.DATA_Z;
    private int times = 0;
	private SensorManager sensorManager;
    private Sensor accelerormeterSensor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.init();
		setContentView(R.layout.activity_main);
		
		timeTable = (ListView)findViewById(R.id.timeTable);
		helper = new CalendarHelper(this);
		datas = helper.getTagBoxList();
		adapter = new TagBoxAdapter(this, R.layout.view_tag_list, datas);
		timeTable.setAdapter(adapter);
		timeTable.setOnItemClickListener(this);
		pref = this.getSharedPreferences(PrefDefines.PrefName.DRAWER, this.MODE_PRIVATE);
		
		//drawerData = new ArrayList<DrawerData>();
		//drawer_list = (ListView)findViewById(R.id.drawer);

		View v = this.getLayoutInflater().inflate(R.layout.view_list_footer, null, false);
		TextView logo = (TextView)v.findViewById(R.id.listView_Footer);
		Typeface thin = Typeface.createFromAsset(getAssets(), "Roboto_Thin.ttf");
		logo.setTypeface(thin);
		
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerormeterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.putExtra("hoster", "me");
        intent.putExtra("title", "면접");
        intent.putExtra("location", "디지털 미디어 고등학교");
        intent.putExtra("icon", IconDefines.BRIEFCASE);
        this.startActivity(intent);
	}
	
	@Override
    public void onStart() {
        super.onStart();
        if (accelerormeterSensor != null)
            sensorManager.registerListener(this, accelerormeterSensor,
            SensorManager.SENSOR_DELAY_GAME);
    }

	@Override
	protected void onPause() {
		super.onPause();
		this.init();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		this.init();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		this.init();
		if (sensorManager != null){
			sensorManager.unregisterListener(this);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//drawerData = this.putInDraw();
		//drawer_adapter.updateResults(drawerData);
		adapter.notifyDataSetChanged();
		this.init();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		TagBox box = datas.get(arg2);
		Intent intent = new Intent(this, AddEventActivity.class);
		intent.putExtra("title", box.getTitle());
		intent.putExtra("location", box.getLocation());
		intent.putExtra("calID", box.getCalID());
		intent.putExtra("dtStart", box.getDtStart());
		intent.putExtra("dtEnd", box.getDtEnd());
		intent.putExtra("pos", arg2);
		intent.putExtra("icon", box.getEventNum());
		this.startActivity(intent);
	}
	
	private ArrayList<DrawerData> putInDraw() {
		// Set the adapter for the list view
		ArrayList<DrawerData> d = new ArrayList<DrawerData>();
		DrawerData dData; 
		
		if(pref.getBoolean(PrefCMD.USER, true)) {
			dData = new DrawerData("개인", IconDefines.USER, false);
			d.add(dData);
		}
		if(pref.getBoolean(PrefCMD.CHECK, true)) {
			dData = new DrawerData("할일", IconDefines.CHEAK, false);
			d.add(dData);
		}
		if(pref.getBoolean(PrefCMD.HOME, true)) {
			dData = new DrawerData("가족", IconDefines.HOME, false);
			d.add(dData);
		}
		if(pref.getBoolean(PrefCMD.BRIEFCASE, true)) {
			dData = new DrawerData("업무", IconDefines.BRIEFCASE, false);
			d.add(dData);
		}
		if(pref.getBoolean(PrefCMD.COFFEE, true)) {
			dData = new DrawerData("회의", IconDefines.COFFEE, false);
			d.add(dData);
		}
		if(pref.getBoolean(PrefCMD.BOOK, true)) {
			dData = new DrawerData("학습", IconDefines.BOOK, false);
			d.add(dData);
		}
		if(pref.getBoolean(PrefCMD.GAMEPAD, true)) {
			dData = new DrawerData("여가", IconDefines.GAMEPAD, false);
			d.add(dData);
		}
		dData = new DrawerData("설정", IconDefines.SETTING, false);
		d.add(dData);
		
		return d;
	}
	
	public void addEvent(View v) {
		Intent intent = new Intent(this, SettingActivity.class);
		this.startActivity(intent);
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

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = currentTime - lastTime;
            if(gabOfTime > 100) {
                lastTime = currentTime;
                x = event.values[SensorManager.DATA_X];
                y = event.values[SensorManager.DATA_Y];
                z = event.values[SensorManager.DATA_Z];
 
                speed = Math.abs(x + y + z - lastX - lastY - lastZ) / gabOfTime * 10000;
 
                if(speed > SHAKE_THRESHOLD) {
                	times++;
                    if(times>5) {
                    	MediaPlayer shake = MediaPlayer.create(this, R.raw.shake);
                    	if(!shake.isPlaying()) {
                    		try{
                    			shake.prepare();
                    		}
                    		catch(Exception e) {}
                    		shake.start();
                    	}
                    	times = 0;
                    	Intent intent = new Intent(this, AddEventActivity.class);
                		intent.putExtra("title", "");
                		startActivity(intent);
                    }
                }
                lastX = event.values[DATA_X];
                lastY = event.values[DATA_Y];
                lastZ = event.values[DATA_Z];
            }
        }
    }
}