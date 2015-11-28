package sh.cau.commuter.Splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import android.support.v7.app.AppCompatActivity;

import com.pnikosis.materialishprogress.ProgressWheel;

import sh.cau.commuter.Activity.MainActivity;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-11-27.
 */
public class SplashActivity extends AppCompatActivity {

    private ProgressWheel wheel;
    private Handler handler;

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_splash);

        /// Init Object
        this.wheel = (ProgressWheel)findViewById(R.id.wheel);

        /// Hide transition animation
        this.overridePendingTransition(0, 0);

        /// Thread Operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                wheel.spin();

                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i); finish();
                    }
                }, 2000); // 2초 지연실행

            }
        }).run();

    }

}
