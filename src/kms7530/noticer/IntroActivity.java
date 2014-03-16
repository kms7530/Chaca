
package kms7530.noticer;
 
import kms7530.noticer.defines.PrefDefines;
import kms7530.noticer.join.StartPage;
import kms7530.notier.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
 
 
public class IntroActivity extends Activity {
    private SharedPreferences pref;
    private TextView logo, version;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
		else {
			View decorView = getWindow().getDecorView();
			decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
		}
        
        setContentView(R.layout.activity_intro);
        
        Typeface buddytalk = Typeface.createFromAsset(this.getAssets(), "buddytalk.otf");
        Typeface robo_Thin = Typeface.createFromAsset(this.getAssets(), "Roboto_Thin.ttf");
        
        logo = (TextView)findViewById(R.id.txtLogo);
        logo.setTypeface(buddytalk);
        
        version = (TextView)findViewById(R.id.txtVersion);
        version.setTypeface(robo_Thin);
        
        pref = this.getSharedPreferences(PrefDefines.PrefName.SETTING, Activity.MODE_PRIVATE);
        if(pref.getBoolean(PrefDefines.PrefCMD.ISFIRST, true)) {
        	Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(IntroActivity.this, StartPage.class);
                    startActivity(intent);
                    // 뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
                    finish();
                }
            }, 3000);
        }
        else if(!pref.getBoolean(PrefDefines.PrefCMD.ISFIRST, true)) {
        	Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                    startActivity(intent);
                    // 뒤로가기 했을경우 안나오도록 없애주기 >> finish!!
                    finish();
                }
            }, 3000);
        }
    }
}