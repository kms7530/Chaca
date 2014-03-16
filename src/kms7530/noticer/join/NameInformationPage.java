package kms7530.noticer.join;

import kms7530.notier.R;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import com.beardedhen.androidbootstrap.*;
import com.beardedhen.androidbootstrap.FontAwesomeText.AnimationSpeed;

public class NameInformationPage extends Activity implements View.OnClickListener, TextWatcher {
	private BootstrapButton btnBack, btnNext;
	private BootstrapEditText edtName;
	private FontAwesomeText arrow;
	private SharedPreferences pref;
	private SharedPreferences.Editor editer;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		else {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
		
		setContentView(R.layout.activity_name_information_page);
		
		// Arrow Animation
		arrow = (FontAwesomeText)findViewById(R.id.arrow);
		arrow.startFlashing(this, true, AnimationSpeed.MEDIUM);
		
		// BackButton
		btnBack = (BootstrapButton)findViewById(R.id.btnNext_hello_sir);
		btnBack.setOnClickListener(this);
		
		// NextButton At first can't use
		btnNext = (BootstrapButton)findViewById(R.id.btnNext);
		btnNext.setOnClickListener(this);
		btnNext.setEnabled(false);
		
		// Text field that write down the user name
		edtName = (BootstrapEditText)findViewById(R.id.edtName);
		edtName.addTextChangedListener(this);		
		pref = this.getSharedPreferences("setting", Activity.MODE_PRIVATE);
		editer = pref.edit();
	}

	@Override
	public void onClick(View v) {
		if (v==btnBack) {
			// Finish this activity
			this.finish();
		}
		else if (v==btnNext) {
			// About Animation until...
			btnBack.startAnimation(AnimationUtils.loadAnimation(this, R.anim.goout));
			btnNext.startAnimation(AnimationUtils.loadAnimation(this, R.anim.goout));
			// here
			
			//Write down user name
			editer.putString("user name", edtName.getText().toString());
			editer.commit();
			
			// Make Intent and start
			Intent intent = new Intent(this, HelloSir.class);
			this.startActivity(intent);
			// Animation
			this.overridePendingTransition(R.anim.goin, R.anim.goout);
			this.finish();
		}
	}

	@Override
	public void afterTextChanged(Editable arg0) {}

	@Override
	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

	@Override
	public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
		if(arg0.toString().isEmpty()) {
			// If empty can't use nextButton
			btnNext.setEnabled(false);
		}
		else if(!arg0.toString().isEmpty()) {
			// If not blank turn to state(can use)
			btnNext.setEnabled(true);
		}
	}
}
