package kms7530.noticer;

import java.util.ArrayList;

import kms7530.noticer.data.DrawerData;
import kms7530.noticer.defines.IconDefines;
import kms7530.noticer.defines.PrefDefines;
import kms7530.noticer.defines.PrefDefines.PrefName;
import kms7530.noticer.view.adapter.SlidingSubMenu;
import kms7530.notier.R;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;

public class DrawerSettingActivity extends Activity implements OnItemClickListener{
	private ArrayList<DrawerData> checkList;
	private SlidingSubMenu adapter;
	private ListView list;
	private SharedPreferences pref;
	private TextView logo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.init();
		setContentView(R.layout.activity_drawer_setting);
		
		checkList = new ArrayList<DrawerData>();
		pref = this.getSharedPreferences(PrefName.DRAWER, Activity.MODE_PRIVATE);
		
		list = (ListView)findViewById(R.id.setting_drawer);
		
		DrawerData data = new DrawerData("개인", IconDefines.USER, pref.getBoolean(PrefDefines.PrefCMD.USER, true));
		checkList.add(data);
		data = new DrawerData("할일", IconDefines.CHEAK, pref.getBoolean(PrefDefines.PrefCMD.CHECK, true));
		checkList.add(data);
		data = new DrawerData("가족", IconDefines.HOME, pref.getBoolean(PrefDefines.PrefCMD.HOME, true));
		checkList.add(data);
		data = new DrawerData("업무", IconDefines.BRIEFCASE, pref.getBoolean(PrefDefines.PrefCMD.BRIEFCASE, true));
		checkList.add(data);
		data = new DrawerData("회의", IconDefines.BRIEFCASE, pref.getBoolean(PrefDefines.PrefCMD.BRIEFCASE, true));
		checkList.add(data);
		data = new DrawerData("학습", IconDefines.BOOK, pref.getBoolean(PrefDefines.PrefCMD.BOOK, true));
		checkList.add(data);
		data = new DrawerData("여가", IconDefines.GAMEPAD, pref.getBoolean(PrefDefines.PrefCMD.GAMEPAD, true));
		checkList.add(data);
		
		View v = this.getLayoutInflater().inflate(R.layout.view_list_footer, null, false);
		logo = (TextView)v.findViewById(R.id.listView_Footer);
		Typeface thin = Typeface.createFromAsset(getAssets(), "Roboto_Thin.ttf");
		logo.setTypeface(thin);
		list.addFooterView(v);
		
		adapter = new SlidingSubMenu(this, R.layout.view_drawer_row, checkList);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
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
	protected void onDestroy() {
		super.onDestroy();
		
		SharedPreferences.Editor editor = pref.edit();
		//MainActivity.drawerData = new ArrayList<DrawerData>();
		
		for(int i=0;i<checkList.size();i++) {
			editor.putBoolean(checkList.get(i).getCMDName(), checkList.get(i).isVisiable);
			if(checkList.get(i).isVisiable) {
				DrawerData dData = new DrawerData("개인", IconDefines.USER, false);
				//MainActivity.drawerData.add(dData);
				MainActivity.adapter.notifyDataSetChanged();
			}
		}
		editor.commit();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		checkList.set(arg2, checkList.get(arg2).setVisiable(checkList.get(arg2).isVisiable(), checkList.get(arg2)));
		//SlidingSubMenu.pos = 0;
		adapter.notifyDataSetChanged();
	}
}
