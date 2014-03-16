package kms7530.noticer.join;

import kms7530.notier.R;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.beardedhen.androidbootstrap.*;

public class StartPage extends Activity implements View.OnClickListener{
	BootstrapButton btnStart;

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
		
		setContentView(R.layout.activity_start_page);
		
		// StartButton
		btnStart = (BootstrapButton)findViewById(R.id.btnNext_hello_sir);
		btnStart.setOnClickListener(this);
		btnStart.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_1000));
		
		// Logo
		LinearLayout icons = (LinearLayout)findViewById(R.id.icons);
		icons.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in_none_repeat));
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = new Intent(this, NameInformationPage.class);
		this.startActivity(intent);
		this.finish();
	}
}