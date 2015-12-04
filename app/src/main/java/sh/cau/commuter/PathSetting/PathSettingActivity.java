package sh.cau.commuter.PathSetting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
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

import sh.cau.commuter.Maps.BusStop;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-12-01.
 */
public class PathSettingActivity extends AppCompatActivity {

    private static final int START_ = 1;
    private static final int DEST_ = 2;

    private SharedPreferences pref;
    private Toolbar toolbar;
    private TextView title, departLocation, arriveLocation;
    private ImageView addBtn;
    private ArrayList<View> pathList = new ArrayList<>();
    private View last;

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_) {

            if (resultCode == RESULT_OK) {
                ((EditText)(last.findViewById(R.id.trans_start_location))).setText(data.getExtras().getString("start"));
            }

            Log.i("test", requestCode + "");

        } else if(requestCode == DEST_) {

            if (resultCode == RESULT_OK) {
                ((EditText)(last.findViewById(R.id.trans_dest_location))).setText(data.getExtras().getString("dest"));
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
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
                    if( pathList.size() > 0 ) _saveDialog();
                    else onBackPressed();
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

    private void _setDefaultAction(final View path, final String trans){

        final EditText num = (EditText) path.findViewById(R.id.trans_num);

        if( trans.equals("SUBWAY") ){
            path.findViewById(R.id.trans_num).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((EditText)path.findViewById(R.id.trans_start_location)).setText("");
                    ((EditText)path.findViewById(R.id.trans_dest_location)).setText("");
                    _createSubwayDialog(v);
                }
            });
        }

        path.findViewById(R.id.trans_start_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), TransSearchActivity.class);

                if (num.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "번호(호선)를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (trans.equals("BUS")) {
                        i.putExtra("trans", "BUS").putExtra("line", num.getText().toString()).putExtra("sd", "s");
                    } else if (trans.equals("SUBWAY")) {
                        i.putExtra("trans", "SUBWAY").putExtra("line", num.getText().toString()).putExtra("sd", "s");
                    }

                    last = path; startActivityForResult(i, START_);
                }
            }
        });

        path.findViewById(R.id.trans_dest_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), TransSearchActivity.class);

                if (num.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "번호(호선)를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {

                    if (trans.equals("BUS")) {
                        i.putExtra("trans", "BUS").putExtra("line", num.getText().toString()).putExtra("sd", "d");
                    } else if (trans.equals("SUBWAY")) {
                        i.putExtra("trans", "SUBWAY").putExtra("line", num.getText().toString()).putExtra("sd", "d");
                    }

                    last = path;
                    startActivityForResult(i, DEST_);
                }
            }
        });
    }

    private void _createSubwayDialog(final View v){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        final String[] items = {"1호선", "2호선", "3호선", "4호선", "5호선", "6호선",
                                "7호선", "8호선", "9호선", "분당선", "인천선", "신분당선",
                                "경의중앙", "경춘선", "공항철도", "의정부경전철", "수인선",
                                "에버라인" };

        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {

                String line = "1";

                switch (item) {
                    case 0:
                        line = "1";
                        break;
                    case 1:
                        line = "2";
                        break;
                    case 2:
                        line = "3";
                        break;
                    case 3:
                        line = "4";
                        break;
                    case 4:
                        line = "5";
                        break;
                    case 5:
                        line = "6";
                        break;
                    case 6:
                        line = "7";
                        break;
                    case 7:
                        line = "8";
                        break;
                    case 8:
                        line = "9";
                        break;
                    case 9:
                        line = "B";
                        break;
                    case 10:
                        line = "I";
                        break;
                    case 11:
                        line = "S";
                        break;
                    case 12:
                        line = "K";
                        break;
                    case 13:
                        line = "G";
                        break;
                    case 14:
                        line = "A";
                        break;
                    case 15:
                        line = "U";
                        break;
                    case 16:
                        line = "SU";
                        break;
                    case 17:
                        line = "E";
                        break;
                }

                ((EditText) v).setText(line);
            }
        });
        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private void _createListDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        final String[] items = {"버스", "지하철", "도보(고정시간)"};

        alertDialog.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                final LinearLayout holder = (LinearLayout) findViewById(R.id.item_holder);
                View path = getLayoutInflater().inflate(R.layout.path_item_foot, null);

                switch (item){
                    case 0 : // 버스
                        path = getLayoutInflater().inflate(R.layout.path_item_bus, null);
                        _setDefaultAction(path, "BUS");
                        break;

                    case 1 : // 지하철
                        path = getLayoutInflater().inflate(R.layout.path_item_subway, null);
                        _setDefaultAction(path, "SUBWAY");
                        break;

                    case 2 : // 도보(고정시간)
                        path = getLayoutInflater().inflate(R.layout.path_item_foot, null);break;
                }

                pathList.add(path);

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

    private void _pathSave(){

        String type, num, start, dest, fullContent="";

        for(int i=0; i<pathList.size(); i++){
            type = ((TextView)pathList.get(i).findViewById(R.id.trans_type)).getText().toString();

            switch( type ){
                case "B":
                    num = ((EditText)pathList.get(i).findViewById(R.id.trans_num)).getText().toString();
                    start = ((EditText)pathList.get(i).findViewById(R.id.trans_start_location)).getText().toString();
                    dest = ((EditText)pathList.get(i).findViewById(R.id.trans_dest_location)).getText().toString();
                    fullContent += "bus;"+num+";"+start+";"+dest+"##"; break;

                case "S":
                    num = ((EditText)pathList.get(i).findViewById(R.id.trans_num)).getText().toString();
                    start = ((EditText)pathList.get(i).findViewById(R.id.trans_start_location)).getText().toString();
                    dest = ((EditText)pathList.get(i).findViewById(R.id.trans_dest_location)).getText().toString();
                    fullContent += "subway;"+num+";"+start+";"+dest+"##"; break;

                case "F":
                    num = ((EditText)pathList.get(i).findViewById(R.id.trans_num)).getText().toString();
                    Log.i("FOOT", "도보로"+num+"분");
                    fullContent += "foot;"+num+"##"; break;
            }
        }

        Log.i("fullon", fullContent);
    }

    private void _saveDialog(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setMessage("현재 경로를 저장하시겠습니까?")
                .setCancelable(false).setPositiveButton("저장",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        _pathSave();
                        dialog.cancel(); finish();
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel(); finish();
                    }
                });

        AlertDialog alert = alertDialog.create();

        if(pathList.size() != 0) alert.show();
    }



}