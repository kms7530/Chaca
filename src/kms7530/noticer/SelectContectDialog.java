package kms7530.noticer;

import kms7530.noticer.data.ContectValue;
import kms7530.noticer.view.adapter.ContectAdapter;
import kms7530.notier.R;

import com.beardedhen.androidbootstrap.BootstrapButton;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ListView;

public class SelectContectDialog extends Dialog implements View.OnClickListener{
	private ListView list;
	private BootstrapButton btnOK, btnAdd;
	private Context context;
	private Activity activity;
	private ContectAdapter adapter;

	public SelectContectDialog(Activity context) {
		super((Context)context);
		this.context = (Context)context;
		this.activity = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_select_contect);
		this.setTitle("참석자를 선택 하세요.");
		
		list = (ListView)findViewById(R.id.listContect);
		btnAdd = (BootstrapButton)findViewById(R.id.btnShowContect);
		btnAdd.setOnClickListener(this);
		btnOK = (BootstrapButton)findViewById(R.id.btnContectOK);
		btnOK.setOnClickListener(this);
		
		adapter = new ContectAdapter(activity, R.layout.view_contect_row, AddEventActivity.contectList);
		list.setAdapter(adapter);
	}
	
	public void refresh(String name, String num) {
		AddEventActivity.contectList.add(new ContectValue(name, num));
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onClick(View v) {
		if(v==btnAdd) {
			Intent intent = new Intent(Intent.ACTION_PICK);
			intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
			activity.startActivityForResult(intent, 0);
		}
		if(v==btnOK) {
			this.dismiss();
		}
	}
}
