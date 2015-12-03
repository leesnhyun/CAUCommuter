package sh.cau.commuter.PathSetting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Stack;

import sh.cau.commuter.R;

/**
 * Created by SH on 2015-12-01.
 */
public class PathSettingActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private Toolbar toolbar;
    private TextView title, departLocation, arriveLocation;
    private ImageView addBtn;
    private View path;
    private ArrayList<View> pathList = new ArrayList<>();

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
                final LinearLayout holder = (LinearLayout) findViewById(R.id.item_holder);
                path = getLayoutInflater().inflate(R.layout.path_item_foot, null);

                switch (item){
                    case 0 : // 버스
                        path = getLayoutInflater().inflate(R.layout.path_item_bus, null);break;

                    case 1 : // 지하철
                        path = getLayoutInflater().inflate(R.layout.path_item_subway, null);break;

                    case 2 : // 도보(고정시간)
                        path = getLayoutInflater().inflate(R.layout.path_item_foot, null);break;
                }

                pathList.add(path);

                if( path.findViewById(R.id.trans_start_location) != null ||
                        path.findViewById(R.id.trans_dest_location) != null ) {

                    EditText start = (EditText) path.findViewById(R.id.trans_start_location);
                    EditText dest = (EditText) path.findViewById(R.id.trans_start_location);
                    final EditText num = (EditText) path.findViewById(R.id.trans_num);
                    final Intent i = new Intent(getApplicationContext(), TransSearchActivity.class);

                    start.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if( num.getText().toString().equals("") ){
                                Toast.makeText(getApplicationContext(), "번호(호선)를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                i.putExtra("trans", "BUS").putExtra("line", num.getText().toString());
                                startActivity(i);
                            }
                        }
                    });

                    dest.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if( num.getText().toString().equals("") ){
                                Toast.makeText(getApplicationContext(), "번호(호선)를 입력해주세요.", Toast.LENGTH_SHORT).show();
                            } else {
                                i.putExtra("trans", "BUS").putExtra("line", num.getText().toString());
                                startActivity(i);
                            }
                        }
                    });

                }

                ImageView btnRemove = (ImageView) path.findViewById(R.id.btn_remove);
                btnRemove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(int i=0; i<pathList.size(); i++){
                            if( v.equals(pathList.get(i).findViewById(R.id.btn_remove)) ){
                                holder.removeView(pathList.get(i));
                                pathList.remove(i); break;
                            }
                        }
                    }
                });

                holder.addView(path, holder.getChildCount() - 1);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

}
