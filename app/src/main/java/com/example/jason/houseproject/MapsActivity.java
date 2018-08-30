package com.example.jason.houseproject;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import static android.location.LocationManager.GPS_PROVIDER;
import static android.location.LocationManager.NETWORK_PROVIDER;
import static com.google.android.gms.maps.CameraUpdateFactory.*;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener {
    final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private GoogleMap mMap;

    double myLocationLatitude = .0d;
    double myLocationLongitude = .0d;

    LatLng myLocationLatLng;

    JSONArray list=null;
    String myJson;

    private static final String TAG_RESULT = "result";
    private static final String TAG_NAME = "name";
    private static final String TAG_LATITUDE = "latitude";
    private static final String TAG_LONGITUDE = "longitude";
    private static final String STRING_URI = "http://cir112.cafe24.com/mapMarkerInfo.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnabled = locationManager.isProviderEnabled(GPS_PROVIDER);

        boolean isNetworkEnabled = locationManager.isProviderEnabled(NETWORK_PROVIDER);

        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                myLocationLatitude = location.getLatitude();
                myLocationLongitude = location.getLongitude();
                myLocationLatLng = new LatLng(myLocationLatitude,myLocationLongitude);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) { }

            public void onProviderEnabled(String provider) { }

            public void onProviderDisabled(String provider) { }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(NETWORK_PROVIDER, 0, 0, locationListener);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(GPS_PROVIDER, 0, 0, locationListener);

        // 수동으로 위치 구하기
        String locationProvider = GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            myLocationLatitude = lastKnownLocation.getLatitude();
            myLocationLongitude = lastKnownLocation.getLongitude();

            myLocationLatLng = new LatLng(myLocationLatitude,myLocationLongitude);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        final int MY_PERMISSIONS_ACCESS_COARSE_LOCATION = 1;
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(MapsActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!= PackageManager.PERMISSION_GRANTED)
            mMap.setMyLocationEnabled(true);

        mMap.moveCamera(newLatLngZoom(new LatLng(35.154265,128.098157), 16));

        getData(STRING_URI,1);

        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(35.154265, 128.098157))
                .title("경상대학교").snippet("This place is GNU"));

        mMap.setOnInfoWindowClickListener(this);

        ImageButton addButton = findViewById(R.id.LocationAddButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dialog를 통한 자취방 이름 입력 후 마커 추가
                LayoutInflater inflater = getLayoutInflater();

                final View dialogView = inflater.inflate(R.layout.dialog_addmarker,null);
                AlertDialog.Builder adBuilder = new AlertDialog.Builder(MapsActivity.this);
                adBuilder.setTitle("자취방 입력");
                adBuilder.setView(dialogView);
                adBuilder.setPositiveButton("입력", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText editText = (EditText)dialogView.findViewById(R.id.editTextBuildName);
                        String buildName = editText.getText().toString();

                        //DB로 좌표값과 건물이름 전송
                        String [] strMyLocationLatLng = new String [3];

                        strMyLocationLatLng[0] = Double.toString(myLocationLatitude);
                        strMyLocationLatLng[1] = Double.toString(myLocationLongitude);
                        strMyLocationLatLng[2] = buildName;

                        InsertMaker insertMaker = new InsertMaker();
                        insertMaker.execute(strMyLocationLatLng);

                        mMap.addMarker(new MarkerOptions().position(myLocationLatLng).title(buildName).snippet("게시판 열기"));
                    }
                });
                adBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        return;
                    }
                });

                AlertDialog dialog = adBuilder.create();

                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }

    @Override
    public void onInfoWindowClick(Marker marker) {//마커 인포창 클릭 이벤트
        LatLng markerLocation = marker.getPosition();//선택 마커 위치

        Intent intent = new Intent(getApplicationContext(), ReviewBoardActivity.class);
        intent.putExtra("building",marker.getTitle());
        intent.putExtra("latitude",Double.toString(markerLocation.latitude));
        intent.putExtra("longitude", Double.toString(markerLocation.longitude));

        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case MY_PERMISSIONS_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    //마커 정보 DB로 전송, 건물 이름, 위도, 경도 값 전송
    public class InsertMaker extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        private final String TAG="ADD_MARKER";

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MapsActivity.this,"Please Wait",null,true,true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            progressDialog.dismiss();
            Toast.makeText(MapsActivity.this, result, Toast.LENGTH_SHORT).show();
            Log.d(TAG,"POST response - "+result);
        }

        @Override
        protected String doInBackground(String... params) {
            String myLocationLatitude = (String) params[0];
            String myLocationLongitude = (String) params[1];
            String buildName = (String) params[2];

            String strUrl = "http://cir112.cafe24.com/insertLocation.php";
            String postParams = "latitude=" + myLocationLatitude + "&longitude=" + myLocationLongitude + "&name=" + buildName;

            try {
                URL url = new URL(strUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParams.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

                InputStream inputStream;
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            } catch (Exception e) {
                Log.d(TAG, "InsertData : Error", e);

                return new String("Error: " + e.getMessage());
            }
        }
    }

    //DB에서 자취방 정보를 가져와 마커 출력
    protected void showMarkerList()
    {
        try
        {
            JSONObject jsonObj = new JSONObject(myJson);
            list = jsonObj.getJSONArray(TAG_RESULT);

            for(int i=0;i<list.length();i++)
            {
                JSONObject c = list.getJSONObject(i);
                String name = c.getString(TAG_NAME);
                double lat = Double.parseDouble(c.getString(TAG_LATITUDE));
                double lng = Double.parseDouble(c.getString(TAG_LONGITUDE));

                LatLng latLng = new LatLng(lat,lng);

                mMap.addMarker(new MarkerOptions().position(latLng).title(name).snippet("게시판 열기"));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(String url,final int mode){
        class GetDataJson extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground (String... params)
            {
                String strUrl = params[0];

                BufferedReader bufferedReader = null;

                try
                {
                    URL url = new URL(strUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;

                    while((json = bufferedReader.readLine())!=null)
                    {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                }catch (Exception e)
                {
                    return null;
                }
            }
            protected void onPostExecute(String result)
            {
                myJson = result;
                switch(mode){
                    case 1:
                        showMarkerList();
                    case 2:
                        //showRoomInfo();
                }

            }
        }
        GetDataJson g = new GetDataJson();
        g.execute(url);
    }
}
