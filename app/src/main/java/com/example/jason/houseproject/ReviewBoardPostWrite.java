package com.example.jason.houseproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ReviewBoardPostWrite extends AppCompatActivity {
    Toolbar toolbar;

    String lat;
    String lng;
    String building;
    String[] isOption = new String[9];

    EditText editTextTitle;
    EditText editTextDeposit;
    EditText editTextMonthly;
    EditText editTextMaintenance;
    EditText editTextContents;

    CheckBox[] checkBoxOption = new CheckBox[9];
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_post_write);

        toolbar = (Toolbar) findViewById(R.id.toolbarReviewWrite);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        building = intent.getStringExtra("building");
        lat = intent.getStringExtra("lat");
        lng = intent.getStringExtra("lng");

        editTextTitle = (EditText)findViewById(R.id.editTextTitle);
        editTextDeposit = (EditText)findViewById(R.id.editTextDeposit);
        editTextMonthly = (EditText)findViewById(R.id.editTextMonthly);
        editTextMaintenance = (EditText)findViewById(R.id.editTextMaintenance);
        editTextContents = (EditText)findViewById(R.id.editTextContents);

        checkBoxOption[0] = (CheckBox)findViewById(R.id.checkBoxOption0);
        checkBoxOption[1] = (CheckBox)findViewById(R.id.checkBoxOption1);
        checkBoxOption[2] = (CheckBox)findViewById(R.id.checkBoxOption2);
        checkBoxOption[3] = (CheckBox)findViewById(R.id.checkBoxOption3);
        checkBoxOption[4] = (CheckBox)findViewById(R.id.checkBoxOption4);
        checkBoxOption[5] = (CheckBox)findViewById(R.id.checkBoxOption5);
        checkBoxOption[6] = (CheckBox)findViewById(R.id.checkBoxOption6);
        checkBoxOption[7] = (CheckBox)findViewById(R.id.checkBoxOption7);
        checkBoxOption[8] = (CheckBox)findViewById(R.id.checkBoxOption8);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.review_write_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.newPost:
                for(int i=0; i<9; i++){
                    if(true == checkBoxOption[i].isChecked()) isOption[i] = "1";
                    else isOption[i] = "0";
                }
                ReviewBoardPostWrite.InsertPost task = new ReviewBoardPostWrite.InsertPost();
                task.execute();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class InsertPost extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        private final String TAG="Post_write";
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ReviewBoardPostWrite.this,"Please Wait",null,true,true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            progressDialog.dismiss();
            Log.d(TAG,"POST response - "+result);
        }

        @Override
        protected String doInBackground(String... params) {
            SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
            String strUrl = "http://cir112.cafe24.com/insert_review_post.php";
            String postParams = "build_name=" + building + "&lat=" + lat + "&lng=" + lng + "&title=" +editTextTitle.getText().toString()+"&id="+sp.getString("ID","root")+"&contents=" +editTextContents.getText().toString()+ "&monthly=" +editTextMonthly.getText().toString()+ "&deposit=" +editTextDeposit.getText().toString()+ "&maintenance=" +editTextMaintenance.getText().toString()+ "&bed=" +isOption[0]+ "&desk=" +isOption[1]+ "&closet=" +isOption[2]+ "&refri=" +isOption[3]+ "&aircon=" +isOption[4]+ "&micro=" +isOption[5]+ "&net=" +isOption[6]+ "&water=" +isOption[7]+ "&tv=" +isOption[8];

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
}