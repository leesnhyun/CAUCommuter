package sh.cau.commuter.PathSetting;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import sh.cau.commuter.Maps.BusStop;
import sh.cau.commuter.Maps.CommuteLocation;
import sh.cau.commuter.Maps.SubwayStation;
import sh.cau.commuter.Model.Constant;
import sh.cau.commuter.Model.Database;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-12-01.
 */
public class TransSearchActivity extends AppCompatActivity {

    private SharedPreferences pref;
    private Toolbar toolbar;
    private TextView title;

    private ArrayList<CommuteLocation> lists = new ArrayList<>();
    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private SQLiteDatabase db;
    private Database DBhelper;

    private Intent intent;

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_trans_search);

        /// Init Object
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.title = (TextView) toolbar.findViewById(R.id.toolbar_name);
        this.recyclerView = (RecyclerView)findViewById(R.id.recycler_pathinfo);
        this.DBhelper = new Database(this, Constant.DB_FILE, null, 1);
        this.db = DBhelper.getReadableDatabase();
        this.intent = getIntent();

        /// Thread Operation
        new Thread(new Runnable() {
            @Override
            public void run() {
                toolbar.post(new Runnable() {
                    @Override
                    public void run() {
                        _initToolbar();
                        _initRecycler( intent.getStringExtra("trans"), intent.getStringExtra("line") );
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

    private void _initRecycler(String trans, String line){

        String query = "";

        switch(trans){
            case "BUS":
                query = "select * from allbus where ( busNm='"+line+"' )";
                break;

            case "SUBWAY":
                query = "select * from stations where ( lineNum = '"+line+"' ) order by stnId";
                break;
        }

        Cursor cur = db.rawQuery(query, null);
        cur.moveToFirst();

        while(!cur.isAfterLast()) {
            if (trans.equals("BUS")){
                lists.add(new BusStop(cur.getString(2), cur.getString(3), cur.getString(4), cur.getString(5)));
            }else if( trans.equals("SUBWAY") ) {
                lists.add(new SubwayStation(cur.getString(1), cur.getString(4), cur.getString(5), cur.getString(2), line));
            }

            cur.moveToNext();
        }

        cur.close();

        final LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        this.adapter = new RecyclerAdapter(TransSearchActivity.this, lists);
        this.adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent();

                if (intent.getStringExtra("sd").equals("s")) {
                    i.putExtra("start",((AppCompatTextView)view).getText().toString());
                } else {
                    i.putExtra("dest",((AppCompatTextView)view).getText().toString());
                }

            setResult(RESULT_OK, i);

            finish();
        }});

        recyclerView.setAdapter(adapter);
    }

}
