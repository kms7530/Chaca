
package kms7530.noticer.join;

import kms7530.noticer.MainActivity;
import kms7530.noticer.defines.PrefDefines;
import kms7530.notier.R;
import android.os.Build;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.beardedhen.androidbootstrap.*;

public class HelloSir extends Activity implements OnClickListener{
	private LinearLayout sayHello;
	private ImageView imgLogo;
	private TextView sirName;
	private SharedPreferences pref;
	private SharedPreferences.Editor editor;
	private BootstrapButton btnNext, btnBack;

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
		
		setContentView(R.layout.activity_hello_sir);
		
		// Get Preferences
		pref = this.getSharedPreferences(PrefDefines.PrefName.SETTING, this.MODE_PRIVATE);
		
		// SayHello Animation
		sayHello = (LinearLayout)findViewById(R.id.sayHello);
		sayHello.startAnimation(AnimationUtils.loadAnimation(this, R.anim.become_big));
		
		// Say hello to user
		sirName = (TextView)findViewById(R.id.txtHello);
		sirName.setText(pref.getString(PrefDefines.PrefCMD.USER_NAME, "")+sirName.getText());
		
		// Logo Animation
		imgLogo = (ImageView)findViewById(R.id.imgLogo);
		imgLogo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.become_big));
		
		// Next Button
		btnNext = (BootstrapButton)findViewById(R.id.btnNext_hello_sir);
		btnNext.setOnClickListener(this);
		
		// Back Button
		btnBack = (BootstrapButton)findViewById(R.id.btnBack_hello_sir);
		btnBack.setOnClickListener(this);
		
		// Make Editor
		editor = pref.edit();
	}

	@Override
	public void onClick(View v) {
		if(v==btnNext) {
			editor.putBoolean(PrefDefines.PrefCMD.ISFIRST, false);
			editor.commit();
			
			Intent intent = new Intent(this, MainActivity.class);
			this.startActivity(intent);
			this.finish();
		}
		else if(v==btnBack) {
			this.finish();
		}
	}
}
