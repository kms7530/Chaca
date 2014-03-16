package kms7530.noticer.view;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import kms7530.noticer.DrawerSettingActivity;
import kms7530.noticer.InformationActivity;
import kms7530.noticer.defines.IconDefines;
import kms7530.noticer.join.NameInformationPage;
import kms7530.noticer.view.helpers.ViewHelper;
import kms7530.notier.R;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingMenu extends Fragment implements OnClickListener{
	public static int pos = 0;
	private FontAwesomeText tag;
	private int color;
	private TextView name;
	private RelativeLayout layout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.view_setting_menu, container, false);
		
		tag = (FontAwesomeText)v.findViewById(R.id.settingiCon);
		name = (TextView)v.findViewById(R.id.settingName);
		layout = (RelativeLayout)v.findViewById(R.id.settingBack);
		layout.setOnClickListener(this);
		
		this.init();
		layout.setBackgroundColor(color);
		
		pos++;
		return v;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		pos=0;
	}
	
	private void init() {
		if(pos==0) 			{tag.setIcon(ViewHelper.getIcon(IconDefines.USER));	color = Color.rgb(241, 196, 15);	name.setText("사용자");}
		//else if(pos==1) 	{tag.setIcon(ViewHelper.getIcon(IconDefines.TAGS));	color = Color.rgb(230, 126, 34);	name.setText("테  그");}
		else if(pos==1) 	{tag.setIcon(ViewHelper.getIcon(IconDefines.INFO));	/*color = Color.rgb(231, 76, 60);*/
								color = Color.rgb(230, 126, 34);		name.setText("정  보");}
	}

	@Override
	public void onClick(View v) {
		if(name.getText().equals("사용자")) {
			Intent intent = new Intent(getActivity(), NameInformationPage.class);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
		else if(name.getText().equals("테  그")) {
			Intent intent = new Intent(getActivity(), DrawerSettingActivity.class);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
		else if(name.getText().equals("정  보")) {
			Intent intent = new Intent(getActivity(), InformationActivity.class);
			getActivity().startActivity(intent);
			getActivity().overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
		}
	}
}
