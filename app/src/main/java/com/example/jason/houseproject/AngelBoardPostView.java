package com.example.jason.houseproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    }
}
