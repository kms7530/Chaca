package kms7530.noticer.view.listener;

import java.util.ArrayList;

import kms7530.noticer.MainActivity;
import kms7530.noticer.SettingActivity;
import kms7530.noticer.data.DrawerData;
import kms7530.noticer.view.helpers.CalendarHelper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class DrawerListener implements ListView.OnItemClickListener{
	private ArrayList<DrawerData> drawer;
	private Activity activity;
	private DrawerData drawerData;
	
	public DrawerListener(ArrayList<DrawerData> drawer, Activity activity) {
		this.activity = activity;
		this.drawer = drawer;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
		drawerData = drawer.get(pos);
		if(drawerData.getTitle()=="설정") {
			Intent intent = new Intent(activity, SettingActivity.class);
			activity.startActivity(intent);
		}
		else {
			refreshTimeTable(drawerData.getIcon());
		}
	}
	
	public void refreshTimeTable(int iCon) {
		CalendarHelper helper = new CalendarHelper(activity);
		MainActivity.datas = helper.getTagBoxList(iCon);
		MainActivity.drawer_adapter.notifyDataSetChanged();
	}
}