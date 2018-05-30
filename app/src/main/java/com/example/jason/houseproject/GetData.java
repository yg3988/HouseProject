package com.example.jason.houseproject;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Locale;

//DB에 존재하는 Data를 JSON형태로 가져오는 Class
//@Author 문윤기
//참고 : http://answerofgod.tistory.com

public class GetData {

    private static final int ANGEL_BOARD = 1;
    private static final int REVIEW_BOARD = 2;
    private static final int MAP_MARKER = 3;

    private int mode;

    private String myJson;
    private String lat;
    private String lng;

    GetData(int mode){
        this.mode = mode;
    }

    GetData(int mode, String lat, String lng){
        this.mode = mode;
        this.lat = lat;
        this.lng = lng;
    }

    public void getData(String url){
        class GetDataJson extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground (String... params)
            {
                Log.d("GetData LatLng",lat+" "+lng);
                String strUrl = params[0];
                String postParams = "lat=" + lat + "&lng=" + lng;

                BufferedReader bufferedReader = null;

                try
                {
                    URL url = new URL(strUrl);

                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    StringBuilder sb = new StringBuilder();
                    Log.d("isMarker",Integer.toString(mode));

                    if (mode == REVIEW_BOARD){
                        con.setReadTimeout(5000);
                        con.setConnectTimeout(5000);
                        con.setRequestMethod("POST");
                        con.setDoInput(true);
                        con.connect();

                        OutputStream outputStream = con.getOutputStream();
                        outputStream.write(postParams.getBytes("UTF-8"));
                        outputStream.flush();
                        outputStream.close();

                        int responseStatusCode = con.getResponseCode();

                        InputStream inputStream;
                        if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                            inputStream = con.getInputStream();
                        } else {
                            inputStream = con.getErrorStream();
                        }

                        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                        bufferedReader = new BufferedReader(inputStreamReader);
                    }else {
                        bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    }
                    String json;

                    while((json = bufferedReader.readLine())!=null)
                    {
                        sb.append(json + "\n");
                    }
                    bufferedReader.close();

                    return sb.toString().trim();//result
                }catch (Exception e)
                {
                    return null;
                }
            }
            protected void onPostExecute(String result)
            {
                myJson = result;
                switch (mode) {
                    case ANGEL_BOARD:
                        AngelBoardActivity.show(myJson);//AngelBoardActivity 인스턴스의 show 메소드 호출
                        break;
                    case REVIEW_BOARD:
                        Log.d("GetData Json",myJson);
                        ReviewBoardFragment.show(myJson);
                        break;
                    case MAP_MARKER:
                        MapsActivity.show(myJson);
                }
            }
        }
        GetDataJson g = new GetDataJson();
        g.execute(url);
    }


}
