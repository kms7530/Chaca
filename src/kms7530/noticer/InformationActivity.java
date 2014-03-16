package kms7530.noticer;

import kms7530.notier.R;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;

public class InformationActivity extends Activity implements OnClickListener{
	private TextView name, code, code2, ver, ver2, thanks;
	private int pos = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		
		Typeface font = Typeface.createFromAsset(this.getAssets(), "Roboto_Thin.ttf");
		name = (TextView)findViewById(R.id.infoLogo);
		name.setTypeface(font);
		name.setOnClickListener(this);
		code = (TextView)findViewById(R.id.infoCode);
		code.setTypeface(font);
		code2 = (TextView)findViewById(R.id.infoCode_2);
		code2.setTypeface(font);
		ver = (TextView)findViewById(R.id.infoVersion);
		ver.setTypeface(font);
		ver2 = (TextView)findViewById(R.id.infoVersion_2);
		ver2.setTypeface(font);
		thanks = (TextView)findViewById(R.id.infoThaks);
		thanks.setTypeface(font);
		thanks.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v==thanks) {
			Intent intent = new Intent(this, ThankYou.class);
			intent.putExtra("she", false);
			startActivity(intent);
		}
		if(v==name) {
			pos++;
			if(pos>16) {
				pos = 0;
				Intent intent = new Intent(this, ThankYou.class);
				intent.putExtra("she", true);
				startActivity(intent);
			}
		}
	}
}
