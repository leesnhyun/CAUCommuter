package sh.cau.commuter.Splash;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.net.HttpURLConnection;
import java.net.URL;

import sh.cau.commuter.Model.Constant;
import sh.cau.commuter.Model.Database;

/**
 * Created by SH on 2015-11-30.
 */
public class DBUpdate extends AsyncTask<Void, Void, Boolean> {

    private ProgressDialog pdialog;
    private OnPostExecuteListener callback;
    private Activity activity;
    private File outputFile;
    private SQLiteDatabase db;
    private Database DBhelper;

    public DBUpdate(Activity activity) {
        this.activity = activity;
        this.pdialog = new ProgressDialog(activity);
        this.pdialog.setCancelable(false);
        this.pdialog.setMessage(Constant.LOADING);
        this.DBhelper = new Database(activity, Constant.DB_NAME, null, 1);
    }

    public void setOnPostExecuteListener(OnPostExecuteListener l){
        this.callback = l;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        this.pdialog.show();
    }

    @Override
    protected Boolean doInBackground(Void... params) {

        try {
            URL url = new URL(Constant.DATA_ADDR);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            File file = new File("/data/data/sh.cau.commuter/databases"); file.mkdir();
            //if (outputFile.exists() && isCreate) outputFile.delete();
            outputFile = new File(file, "stops.db");

            FileOutputStream fos = new FileOutputStream(outputFile);

            if (c.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.i("Update Err", "http response code is " + c.getResponseCode());
                return false;
            }

            BufferedInputStream is = new BufferedInputStream(c.getInputStream());

            byte[] buffer = new byte[1024];
            int len1;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }

            fos.close();
            is.close();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean result){

        if( this.callback == null ){ Log.e("Update Err", "You must implements listener."); return; }

        if( result ){
            Log.i("Update", "success");
            this.callback.onSuccess();
        } else {
            this.callback.onFail();
        }

        this.pdialog.dismiss();
    }

    private void _insert(String stnid, String gpsX, String gpsY, String stnName){
        String sql = "INSERT INTO stops values( NULL, '"+stnid+"','"+gpsX+"','"+gpsY+"','"+stnName+"');";
        db.execSQL(sql);
    }

    private void _select(String stnName){
        // for debuging
        this.db = DBhelper.getReadableDatabase();
        String sql = "select * from station where stnName ='"+stnName+"';";
        Cursor result = db.rawQuery(sql, null);

        // result(Cursor 객체)가 비어 있으면 false 리턴
        if(result.moveToFirst()){
            Log.i("result", result.getString(0)+"/"+result.getString(1)+"/"+result.getString(2));
        }
        result.close();
    }

}
