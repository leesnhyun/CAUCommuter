package sh.cau.commuter.Maps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sh.cau.commuter.Model.Constant;
import sh.cau.commuter.Model.Database;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-11-29.
 */
public class SearchLocationActivity extends AppCompatActivity implements LocationListener {

    private SupportMapFragment fragment;
    private GoogleMap googleMap;
    private LocationManager locationManager;
    private Marker marker;

    private SQLiteDatabase db;
    private Database DBhelper;

    private Toolbar toolbar;
    private TextView title;
    private SearchBox search;

    private SharedPreferences pref;
    private Intent intent;
    private ArrayList<CommuteLocation> list = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedBundle) {
        super.onCreate(savedBundle);
        setContentView(R.layout.activity_location_search);

        /// Init Object
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.title = (TextView)findViewById(R.id.toolbar_name);
        this.search = (SearchBox)findViewById(R.id.searchbox);
        this.locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.fragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        this.googleMap = fragment.getMap();
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        this.DBhelper = new Database(this, Constant.DB_FILE, null, 1);
        this.db = DBhelper.getReadableDatabase();
        this.intent = getIntent();

        /// set Markers
        _setMarkers();
        _setMapPosition(37.50781, 126.961299);

        /// Hide transition animation
        this.overridePendingTransition(0, 0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        _initToolbar();
        _initSearchBox();
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // GPS로 사용하도록 설정
        String provider = LocationManager.GPS_PROVIDER;

        // GPS의 사용 가능 여부 조회, 사용이 불가능할 경우 통신망의 IP주소 기반으로 변경
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            provider = LocationManager.NETWORK_PROVIDER;
        }

        Toast.makeText(this, provider, Toast.LENGTH_SHORT).show();

        // 위치정보 취득 시작
        // 하드웨어이름, 갱신시간주기, 갱신거리주기(m), 이벤트핸들러
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }


    /// private method
    private void _initToolbar(){
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Title
            title.setText(getString(R.string.location_search));

            // Action
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    private void _initSearchBox(){

        search.setMenuListener(new SearchBox.MenuListener() {
            @Override
            public void onMenuClick() { }
        });

        search.setSearchListener(new SearchBox.SearchListener() {

            @Override
            public void onSearchOpened() {
                //Use this to tint the screen
            }

            @Override
            public void onSearchClosed() {
                //Use this to un-tint the screen
            }

            @Override
            public void onSearchTermChanged(String term) {

                if (_ajaxResult(list, term)) {
                    for (int x = 0; x < list.size(); x++) {
                        SearchResult option = new SearchResult(list.get(x).getStnName(), getResources().getDrawable(R.drawable.ic_cast_dark));
                        search.addSearchable(option);
                    }
                }

            }

            @Override
            public void onSearch(String searchTerm) {
                ArrayList<CommuteLocation> b = new ArrayList<>();
                if (_ajaxResult(b, searchTerm)) {
                    _setMapPosition(Double.parseDouble(b.get(0).getGpsXcord()), Double.parseDouble(b.get(0).getGpsYcord()));
                }
            }

            @Override
            public void onResultClick(SearchResult result) {
                ArrayList<CommuteLocation> b = new ArrayList<>();
                if (_ajaxResult(b, result.title)) {
                    _setMapPosition(Double.parseDouble(b.get(0).getGpsXcord()), Double.parseDouble(b.get(0).getGpsYcord()));
                }
            }

            @Override
            public void onSearchCleared() {
                //Called when the clear button is clicked
            }
        });

    }

    private boolean _ajaxResult(ArrayList<CommuteLocation> list, String stnName){

        String getBus = "select * from stops where stnName LIKE '%"+stnName+"%' LIMIT 5";
        String getSubway = "select * from stations where stnName LIKE '%"+stnName+"%' LIMIT 5";
        Cursor result = db.rawQuery(getSubway, null);

        list.clear();

        // 지하철
        if(result.moveToFirst()){
            list.add(new SubwayStation(result.getString(1), result.getString(4), result.getString(5), result.getString(2), result.getString(3)));
        }
        result.close();
        
        // 버스
        result = db.rawQuery(getBus, null);
        if(result.moveToFirst()){
            list.add(new BusStop(result.getString(1),result.getString(2),result.getString(3),result.getString(4)));
        }
        result.close();

        if( list.size() == 0 ) return false;
        else return true;
    }

    private void _setMarkers(){

        String getBusStops = "select * from stops";
        String getStations = "select * from stations";
        
        Cursor bus = db.rawQuery(getBusStops, null);
        Cursor sub = db.rawQuery(getStations, null);
        
        bus.moveToFirst();
        sub.moveToFirst();
        
        while(!bus.isAfterLast()){
            double xpos = Double.parseDouble(bus.getString(2));
            double ypos = Double.parseDouble(bus.getString(3));

            MarkerOptions options = new MarkerOptions();
            options.title(bus.getString(4)); options.position(new LatLng(xpos, ypos));
            this.googleMap.addMarker(options);

            bus.moveToNext();
        }
        bus.close();

        while(!sub.isAfterLast()){

            try {
                double xpos = Double.parseDouble(sub.getString(4));
                double ypos = Double.parseDouble(sub.getString(5));

                MarkerOptions options = new MarkerOptions();
                options.title(sub.getString(2));
                options.position(new LatLng(xpos, ypos));
                this.googleMap.addMarker(options);

            } catch(NullPointerException e) {
                e.printStackTrace();

            } finally {
                sub.moveToNext();
            }

        }
        sub.close();

        this.googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                ArrayList<CommuteLocation> b = new ArrayList<>();
                if (_ajaxResult(b, marker.getTitle())) {
                    _createAlertDialog(b.get(0));
                }
            }
        });

    }

    private void _createAlertDialog(final CommuteLocation b){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        String msg = "";

        if( b instanceof SubwayStation ){
            msg = "선택하신 위치는 '"+b.getStnName()+"("+((SubwayStation) b).getLine()+")' 입니다.";
        } else {
            msg = "선택하신 위치는 '"+b.getStnName()+"' 입니다.";
        }

        alertDialog.setMessage(msg)
                .setCancelable(false).setPositiveButton("이 위치를 선택",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (intent.getStringExtra("action").equals("depart")) {
                            pref.edit().putString("pref_location_departure", b.getStnName())
                                    .putString("pref_location_depart_id", b.getStnId())
                                    .putString("pref_location_depart_x", b.getGpsXcord())
                                    .putString("pref_location_depart_y", b.getStnId())
                                    .putBoolean("pref_location_depart_set", true).apply();
                        } else if(intent.getStringExtra("action").equals("arrive")) {
                            pref.edit().putString("pref_location_arrival", b.getStnName())
                                    .putString("pref_location_arrival_id", b.getStnId())
                                    .putString("pref_location_arrival_x", b.getGpsXcord())
                                    .putString("pref_location_arrival_y", b.getStnId())
                                    .putBoolean("pref_location_arrival_set", true).apply();
                        }

                        dialog.cancel();
                        finish();
                    }
                })

                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertDialog.create();
        alert.show();
    }

    private String _getAddress(double lat, double lng) {
        String address = null;

        // 위치정보를 활용하기 위한 구글 API 객체
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        // 주소 목록을 담기 위한 List
        List<Address> list = null;

        try {
            // 주소 목록을 가져온다. --> 위도,경도,조회 갯수
            list = geocoder.getFromLocation(lat, lng, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (list == null) {
            Log.e("getAddress", "주소 데이터 얻기 실패");
            return null;
        }

        if (list.size() > 0) {
            // getFromLocation() 메서드에서 결과를 하나만 요청했기 때문에,
            // 반복처리는 필요 없다.
            Address addr = list.get(0);
            address = addr.getCountryName() + " "
                    + addr.getAdminArea() + " "
                    + addr.getLocality() + " "
                    + addr.getThoroughfare() + " "
                    + addr.getFeatureName();
        }

        return address;
    }

    private void _setMapPosition(double lat, double lng) {

        LatLng position = new LatLng(lat, lng);

        if (marker == null) {
            // 마커가 없는 경우 새로 생성하여 지도에 추가
            MarkerOptions options = new MarkerOptions();
            options.position(position);
            options.title(_getAddress(lat, lng));
            marker = googleMap.addMarker(options);
        } else {
            // 이미 존재하는 경우, 위치만 갱신
            marker.setPosition(position);
            marker.setTitle(_getAddress(lat, lng));
        }

        // zoom : 1~21 (값이 커질 수록 확대)
        CameraUpdate camera = CameraUpdateFactory.newLatLngZoom(position, 17.5f);
        googleMap.animateCamera(camera);
    }


    /// overriding method
    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        this._setMapPosition(lat, lng);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}