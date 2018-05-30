package com.example.jason.houseproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

public class AngelBoardPostView extends AppCompatActivity {
    private TextView textViewSubject;
    private TextView textViewNick;
    private TextView textViewHits;
    private TextView textViewContent;
    private ImageView imageViewObject;
    Toolbar toolbar;

    String no;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_angel_post_view);

        toolbar = (Toolbar)findViewById(R.id.toolbarAngelPostView);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewSubject = (TextView)findViewById(R.id.textViewSub);
        textViewNick = (TextView)findViewById(R.id.textViewNIck);
        textViewHits = (TextView)findViewById(R.id.textViewHits);
        textViewContent = (TextView)findViewById(R.id.textViewContent);
        imageViewObject = (ImageView)findViewById(R.id.imageViewObject);

        Intent intent = getIntent();
        no = intent.getStringExtra("no");
        textViewSubject.setText(intent.getStringExtra("subject"));
        textViewNick.setText(intent.getStringExtra("nick"));
        textViewHits.setText(intent.getStringExtra("hits"));
        textViewContent.setText(intent.getStringExtra("contents"));
        imageViewObject.setImageBitmap(StringToBitMap(intent.getStringExtra("img")));
    }

    public static Bitmap StringToBitMap(String image){
        Log.e("StringToBitMap","StringToBitMap");
        try{
            byte [] encodeByte=Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            Log.e("StringToBitMap","good");
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_view_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.delete_post:
                SharedPreferences sp = getSharedPreferences("loginInfo", MODE_PRIVATE);
                String id = sp.getString("ID","");

                PostDelete pd = new PostDelete();
                pd.execute("delete_angel_post.php", no, id);

                Intent intent = new Intent(getApplicationContext(), AngelBoardActivity.class);
                startActivity(intent);
                finish();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
