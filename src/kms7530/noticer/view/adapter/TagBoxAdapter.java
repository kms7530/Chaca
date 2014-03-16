package kms7530.noticer.view.adapter;

import java.util.ArrayList;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import kms7530.noticer.data.TagBox;
import kms7530.noticer.defines.IconDefines;
import kms7530.noticer.view.helpers.ViewHelper;
import kms7530.notier.R;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TagBoxAdapter extends ArrayAdapter<TagBox> {
	private Activity context;
	private LayoutInflater inflater;
	private int id;
	private ArrayList<TagBox> arr;
	private LinearLayout layout;
	public static Typeface robo, namsan;
	
	public TagBoxAdapter(Activity context, int resource, ArrayList<TagBox> boxes) {
		super(context, resource, boxes);
		this.arr = boxes;
		this.context = context;
		this.id = resource;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			inflater = context.getLayoutInflater();
			convertView = inflater.inflate(id, null);
		}
		
		int icon = arr.get(position).getEventNum();
		
		// Set Icon
		FontAwesomeText tag = (FontAwesomeText)convertView.findViewById(R.id.mark);
		tag.setIcon(ViewHelper.getIcon(icon));
		
		TextView am_pm, todo, time;
		am_pm = (TextView)convertView.findViewById(R.id.am_pm);
		todo = (TextView)convertView.findViewById(R.id.todo);
		time = (TextView)convertView.findViewById(R.id.time);
		
		// Set Value
		am_pm.setText(arr.get(position).getAmPm());
		todo.setText(arr.get(position).getTitle());
		time.setText(ViewHelper.chageTimeFormat(arr.get(position).hour, arr.get(position).minute));
		
		// Set Background
		layout = (LinearLayout)convertView.findViewById(R.id.tag_back);
		if(icon==IconDefines.USER) layout.setBackgroundResource(R.drawable.tag_box_alizarin);
		if(icon==IconDefines.CHEAK) layout.setBackgroundResource(R.drawable.tag_box_carrot);
		if(icon==IconDefines.HOME) layout.setBackgroundResource(R.drawable.tag_box_sun_flower);
		if(icon==IconDefines.BRIEFCASE) layout.setBackgroundResource(R.drawable.tag_box_turquoise);
		if(icon==IconDefines.COFFEE) layout.setBackgroundResource(R.drawable.tag_box_emerald);
		if(icon==IconDefines.BOOK) layout.setBackgroundResource(R.drawable.tag_box_peter_river);
		if(icon==IconDefines.GAMEPAD) layout.setBackgroundResource(R.drawable.tag_box_amethst);
		
		// Set Font
		robo = Typeface.createFromAsset(context.getAssets(), "Roboto_Light.ttf");
		namsan = Typeface.createFromAsset(context.getAssets(), "namsan.otf.mp3");
		am_pm.setTypeface(robo);
		time.setTypeface(robo);
		todo.setTypeface(namsan);
		
		return convertView;
	}

}
