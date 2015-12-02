package sh.cau.commuter.PathSetting;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sh.cau.commuter.Maps.CommuteLocation;
import sh.cau.commuter.Maps.SubwayStation;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-12-01.
 */
public class PathSettingActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private Toolbar toolbar;
    private TextView title, departLocation, arriveLocation;
    private ImageView addBtn;

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_path_setting);

        /// Init Object
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.title = (TextView) toolbar.findViewById(R.id.toolbar_name);
        this.departLocation = (TextView)findViewById(R.id.txt_depart);
        this.arriveLocation = (TextView)findViewById(R.id.txt_arrive);
        this.addBtn = (ImageView)findViewById(R.id.btnAddPath);

        /// Thread Operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                _initTextView();
                toolbar.post(new Runnable() {
                    @Override
                    public void run() {
                        _initToolbar();
                    }
                });
                addBtn.post(new Runnable() {
                    @Override
                    public void run() {
                        _initAddBtn();
                    }
                });
            }
        }).run();
    }

    private void _initToolbar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Title
            title.setText(getString(R.string.pref_path_config));

            // Action
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void _initTextView(){
        departLocation.setText("출발 : "+pref.getString("pref_location_departure",""));
        arriveLocation.setText("도착 : " + pref.getString("pref_location_arrival", ""));
    }

    private void _initAddBtn(){
        this.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _createListDialog();
            }
        });
    }

    private void _createListDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        final String[] items = {"버스", "지하철", "도보(고정시간)"};

        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                LinearLayout holder = (LinearLayout) findViewById(R.id.item_holder);
                View path = getLayoutInflater().inflate(R.layout.path_item, null);

                switch (item){
                    case 0 : // 버스
                        ((ImageView)path.findViewById(R.id.transIcon)).setImageResource(R.drawable.ic_bus);
                        break;

                    case 1 : // 지하철
                        ((ImageView)path.findViewById(R.id.transIcon)).setImageResource(R.drawable.ic_subway);
                        break;

                    case 2 : // 도보(고정시간)
                        ((ImageView)path.findViewById(R.id.transIcon)).setImageResource(R.drawable.ic_foot);
                        break;
                }

                holder.addView(path, holder.getChildCount() - 1);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

}
