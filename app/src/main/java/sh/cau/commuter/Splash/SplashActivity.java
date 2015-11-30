package sh.cau.commuter.Splash;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.pnikosis.materialishprogress.ProgressWheel;

import sh.cau.commuter.Main.MainActivity;
import sh.cau.commuter.Model.Constant;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-11-27.
 */
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private ProgressWheel wheel;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_splash);

        /// Init Object
        this.wheel = (ProgressWheel)findViewById(R.id.wheel);
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);

        /// Hide transition animation
        this.overridePendingTransition(0, 0);

        /// Thread Operation
        new Thread(new Runnable() {
            @Override
            public void run() {

                if( !pref.getBoolean("pref_isFirstVisit", true) ) {
                    wheel.spin();
                    handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 2000); // 2초 지연실행

                } else {
                    // 첫 방문이면 다운로드
                    DBUpdate download = new DBUpdate(SplashActivity.this);
                    download.setOnPostExecuteListener(new OnPostExecuteListener() {
                        @Override
                        public void onSuccess() {
                            Intent i = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }

                        @Override
                        public void onFail() {
                            Toast.makeText(getApplicationContext(), Constant.LOADING_FAIL, Toast.LENGTH_LONG).show();
                            finish();
                        }
                    });
                    download.execute();
                }

            }
        }).run();

    }

}
