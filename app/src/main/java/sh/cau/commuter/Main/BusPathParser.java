package sh.cau.commuter.Main;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import sh.cau.commuter.Model.Constant;

/**
 * Created by SH on 2015-12-11.
 */
public class BusPathParser extends AsyncTask<String, Void, Boolean> {

    private String result;
    private String line;
    private OnPathCallBack callback;

    @Override
    /* @param1 : startX
     * @param2 : startY
     * @param3 : destX
     * @param4 : destY */

    protected Boolean doInBackground(String... params) {
        try {
            URL url = new URL(Constant.BUS_PATH_URL);

            HttpURLConnection c = (HttpURLConnection)url.openConnection();
            c.setRequestMethod("POST");
            c.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            c.setUseCaches(false);
            c.setDoInput(true);
            c.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("startX", params[1])
                    .appendQueryParameter("startY", params[0])
                    .appendQueryParameter("endX", params[3])
                    .appendQueryParameter("endY", params[2]);

            this.line = params[4]+"번";

            String query = builder.build().getEncodedQuery();
            c.setFixedLengthStreamingMode(query.getBytes().length);

            OutputStream os = c.getOutputStream();
            BufferedWriter buf = new BufferedWriter(new OutputStreamWriter(os));

            buf.write(query);
            buf.flush(); buf.close();
            os.close();

            c.connect();
            if( c.getResponseCode() == HttpURLConnection.HTTP_OK ){
                StringBuilder sb = new StringBuilder();

                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream(), "EUC-KR"));
                String line;

                while((line = br.readLine())!=null){
                    sb.append(line);
                }

                result = sb.toString(); c.disconnect();
                return true;
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        super.onPostExecute(isSuccess);
        if(isSuccess){

            try {
                int time=0, distance=0;
                JSONObject src = new JSONObject(result);
                JSONArray result = src.getJSONArray("resultList");

                for (int i = 0; i < result.length(); i++) {
                    JSONObject sub = result.getJSONObject(i);
                    JSONArray subr = sub.getJSONArray("pathList");
                    if( subr.getJSONObject(0).getString("routeNm").contains(line) ){
                        time = Integer.parseInt(sub.getString("time"));
                        distance = Integer.parseInt(sub.getString("distance"));
                    }
                }

                this.callback.success(time, distance);
                return;

            } catch (JSONException e) {
                Log.i("JSONParsing", "error");
            }
        }

        this.callback.fail();
    }

    public void setOnCallbackListener(OnPathCallBack l){
        this.callback = l;
    }

}
