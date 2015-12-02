package sh.cau.commuter.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

/**
 * Created by SH on 2015-11-29.
 */
public class Database extends SQLiteOpenHelper {

    SharedPreferences pref;

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS station";
        this.pref.edit().putBoolean("pref_isFirstVisit", true).apply();
        db.execSQL(sql);
        onCreate(db);
    }

}
