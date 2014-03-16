package kms7530.noticer;

import kms7530.noticer.defines.Letter;
import kms7530.notier.R;
import kms7530.notier.R.id;
import kms7530.notier.R.layout;
import android.os.Bundle;
import android.widget.TextView;
import android.app.Activity;

public class ThankYou extends Activity {
	private TextView forYou;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_thank_you);
		forYou = (TextView)findViewById(R.id.forYou);
		
		if(this.getIntent().getExtras().getBoolean("she")==true) {
			forYou.setText(Letter.FORHER);
		}
		else {
			forYou.setText(Letter.FORTHEM);
		}
	}
}