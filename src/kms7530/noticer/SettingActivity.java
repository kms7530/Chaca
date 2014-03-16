package kms7530.noticer;

import kms7530.notier.R;
import android.os.Bundle;
import android.view.Window;
import android.app.Activity;

public class SettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_setting);
	}
}