package com.example.jason.houseproject;

import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostDelete extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground (String... params)
    {
        String strUrl = "http://cir112.cafe24.com/"+params[0];
        String postParams="no="+params[1]+"&id="+params[2];//"board="+params[0]+
        BufferedReader bufferedReader = null;

        try
        {
            URL url = new URL(strUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            StringBuilder sb = new StringBuilder();

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
    protected void onPostExecute(String result) {  }
}
