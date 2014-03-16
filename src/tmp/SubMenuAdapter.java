package tmp;

import java.util.ArrayList;

import com.beardedhen.androidbootstrap.FontAwesomeText;

import kms7530.noticer.data.DrawerData_TMP;
import kms7530.noticer.view.helpers.ViewHelper;
import kms7530.notier.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SubMenuAdapter extends ArrayAdapter<DrawerData_TMP> {
	private LayoutInflater inflater;
	private Activity context;
	private int resID;
	private ArrayList<DrawerData_TMP> list;

	public SubMenuAdapter(Activity context, int resource, ArrayList<DrawerData_TMP> list) {
		super(context, resource);
		this.context = context;
		this.resID = resource;
		this.list = list;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null) {
			inflater = context.getLayoutInflater();
			convertView = inflater.inflate(resID, null);
		}
		
		FontAwesomeText icon = (FontAwesomeText)convertView.findViewById(R.id.drawer_icon);
		icon.setIcon(ViewHelper.getIcon(list.get(position).getIcon()));
		TextView text = (TextView)convertView.findViewById(R.id.drawer_title);
		text.setText(list.get(position).getTitle());
		LinearLayout back = (LinearLayout)convertView.findViewById(R.id.drawer_back);
		back.setBackgroundColor(list.get(position).getIcon());
		
		return convertView;
	}
}
