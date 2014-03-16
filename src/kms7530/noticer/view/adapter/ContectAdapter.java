package kms7530.noticer.view.adapter;

import java.util.ArrayList;

import kms7530.noticer.data.ContectValue;
import kms7530.notier.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ContectAdapter extends ArrayAdapter<ContectValue> {
	private Activity context;
	private int id;
	private ArrayList<ContectValue> arr;
	private LayoutInflater inflater;
	private TextView first, last, num;
	
	public ContectAdapter(Activity context, int id, ArrayList<ContectValue> arr) {
		super(context, id, arr);
		this.arr = arr;
		this.context = context;
		this.id = id;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		String name = arr.get(position).getName(), phoneNum = arr.get(position).getPhoneNum();
		
		if(convertView==null) {
			inflater = context.getLayoutInflater();
			convertView = inflater.inflate(id, null);
		}
		
		first = (TextView)convertView.findViewById(R.id.txtFirstName);
		first.setText(String.valueOf(name.charAt(0)));
		last = (TextView)convertView.findViewById(R.id.txtLastName);
		last.setText(String.valueOf(name.subSequence(1, name.length())));
		num = (TextView)convertView.findViewById(R.id.txtPhoneNum);
		num.setText(phoneNum);
		
		return convertView;
	}
}
