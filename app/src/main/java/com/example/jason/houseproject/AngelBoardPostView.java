package com.example.jason.houseproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class AngelBoardPostView extends AppCompatActivity {
    private TextView textViewSubject;
    private TextView textViewNick;
    private TextView textViewHits;
    private TextView textViewContent;
    private ImageView imageViewObject;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_angel_post_view);

        textViewSubject = (TextView)findViewById(R.id.textViewSub);
        textViewNick = (TextView)findViewById(R.id.textViewNIck);
        textViewHits = (TextView)findViewById(R.id.textViewHits);
        textViewContent = (TextView)findViewById(R.id.textViewContent);
        imageViewObject = (ImageView)findViewById(R.id.imageViewObject);

        Intent intent = getIntent();

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
}
