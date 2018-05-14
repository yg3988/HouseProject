package com.example.jason.houseproject;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class AngelBoardPostWrite extends AppCompatActivity
{
    final int REQ_CODE_SELECT_IMAGE=100;
    Toolbar toolbar;
    String[] postElement = new String [3];
    EditText editTextSub;
    EditText editTextCon;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_angel_post_write);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        editTextSub = (EditText)findViewById(R.id.editTextSubject);
        editTextCon = (EditText)findViewById(R.id.editTextContents);

        if (ContextCompat.checkSelfPermission(AngelBoardPostWrite.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(AngelBoardPostWrite.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(AngelBoardPostWrite.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        1000);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
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
            finish();
            return true;
        }
        if(id==R.id.openGallery){
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
            intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQ_CODE_SELECT_IMAGE:
                sendPicture(data.getData());
                break;
            }
        }
    }

    private void sendPicture(Uri data) {
        imageView = (ImageView)findViewById(R.id.imageViewShot);
        String imagePath = getRealPathFromURI(data);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        imageView.setImageBitmap(bitmap);//이미지 뷰에 비트맵 넣기
    }

    private String getRealPathFromURI(Uri data) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(data, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
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
        protected String doInBackground(String... params) {
            String name = (String) params[0];
            String title = (String) params[1];
            String contents = (String) params[2];

            String strUrl = "http://cir112.cafe24.com/insert_Post.php";
            String postParams = "name=" + name + "&title=" + title + "&contents=" + contents;

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