package kms7530.noticer;

import kms7530.noticer.defines.IconDefines;
import kms7530.notier.R;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class TagPickerDialog extends Dialog implements OnClickListener{
	LinearLayout user, check, home, briefcase, coffee, book, gamepad;

	public TagPickerDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tag_picker_dialog);
		this.setTitle("Tag를 고르세요.");
		
		user = (LinearLayout)findViewById(R.id.tag_User);
		user.setOnClickListener(this);
		check = (LinearLayout)findViewById(R.id.tag_Check);
		check.setOnClickListener(this);
		home = (LinearLayout)findViewById(R.id.tag_Home);
		home.setOnClickListener(this);
		briefcase = (LinearLayout)findViewById(R.id.tag_Briefcase);
		briefcase.setOnClickListener(this);
		coffee = (LinearLayout)findViewById(R.id.tag_Coffee);
		coffee.setOnClickListener(this);
		book = (LinearLayout)findViewById(R.id.tag_Book);
		book.setOnClickListener(this);
		gamepad = (LinearLayout)findViewById(R.id.tag_gamepad);
		gamepad.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==user) 		AddEventActivity.newTag = IconDefines.USER;
		if(v==check) 		AddEventActivity.newTag = IconDefines.CHEAK;
		if(v==home) 		AddEventActivity.newTag = IconDefines.HOME;
		if(v==briefcase)	AddEventActivity.newTag = IconDefines.BRIEFCASE;
		if(v==coffee)		AddEventActivity.newTag = IconDefines.COFFEE;
		if(v==book)			AddEventActivity.newTag = IconDefines.BOOK;
		if(v==gamepad)		AddEventActivity.newTag = IconDefines.GAMEPAD;
		
		this.dismiss();
	}
}
