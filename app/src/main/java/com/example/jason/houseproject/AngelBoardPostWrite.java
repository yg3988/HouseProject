package com.example.jason.houseproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AngelBoardPostWrite extends AppCompatActivity
{
    Toolbar toolbar;
    String[] postElement = new String [3];
    EditText editTextSub;
    EditText editTextCon;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.angel_post_write);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        editTextSub = (EditText)findViewById(R.id.editTextSubject);
        editTextCon = (EditText)findViewById(R.id.editTextContents);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_write_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == R.id.newPost ){
            postElement[0] = "ADMIN";
            postElement[1] = editTextSub.getText().toString();
            postElement[2] = editTextCon.getText().toString();

            InsertPost task = new InsertPost();
            task.execute(postElement);

            Intent intent = new Intent(getApplicationContext(), AngelBoardActivity.class);
            startActivity(intent);
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
            progressDialog = ProgressDialog.show(AngelBoardPostWrite.this,"Please Wait",null,true,true);
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            progressDialog.dismiss();
            Toast.makeText(AngelBoardPostWrite.this, result, Toast.LENGTH_SHORT).show();
            Log.d(TAG,"POST response - "+result);
        }

        @Override
        protected String doInBackground(String... params){
            String name = (String)params[0];
            String subject = (String)params[1];
            String contents = (String)params[2];

            String strUrl = "http://35.194.105.42/insert_Post.php";
            String postParams = "name="+name+"&subject="+subject+"&contents="+contents;

            try{
                URL url=new URL(strUrl);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

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
                Log.d(TAG, "POST response code - "+responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode==HttpURLConnection.HTTP_OK){
                    inputStream = httpURLConnection.getInputStream();
                }else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line=bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString();
            }catch (Exception e){
                Log.d(TAG,"InsertData : Error", e);

                return new String("Error: "+e.getMessage());
            }
        }

    }
}