package kms7530.noticer.view.adapter;

import java.util.ArrayList;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import kms7530.noticer.data.DrawerData;
import kms7530.noticer.view.helpers.ViewHelper;
import kms7530.notier.R;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SlidingSubMenu extends ArrayAdapter<DrawerData> {
	private Activity context;
	private int id;
	private LayoutInflater inflater;
	//public static int pos = 0;
	private int[] colorArr = new int[2];
	private RelativeLayout right, left;
	private FontAwesomeText tagLogo, checkBox;
	private TextView title;
	private ArrayList<DrawerData> arr;
	
	public SlidingSubMenu(Activity context, int resource, ArrayList<DrawerData> arr) {
		super(context, resource, arr);
		this.context = context;
		this.id = resource;
		this.arr = arr;
	}
	
	public void updateResults(ArrayList<DrawerData> datas) {
        arr = datas;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			inflater = context.getLayoutInflater();
			convertView = inflater.inflate(id, null);
		}
		colorArr = ViewHelper.getColor(position);
		
		right = (RelativeLayout)convertView.findViewById(R.id.drawer_R);
		right.setBackgroundColor(colorArr[0]);
		
		left = (RelativeLayout)convertView.findViewById(R.id.drawer_L);
		left.setBackgroundColor(colorArr[1]);
		
		tagLogo = (FontAwesomeText)convertView.findViewById(R.id.drawer_tag);
		tagLogo.setIcon(ViewHelper.getIcon(arr.get(position).getIcon()));
		
		checkBox = (FontAwesomeText)convertView.findViewById(R.id.checkBox);
		checkBox.setVisibility(arr.get(position).isVisiable());
		
		title = (TextView)convertView.findViewById(R.id.drawer_title);
		title.setText(arr.get(position).getTitle());
		return convertView;
	}
}
