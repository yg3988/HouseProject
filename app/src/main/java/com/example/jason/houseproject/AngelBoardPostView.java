package com.example.jason.houseproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class AngelBoardPostView extends AppCompatActivity {
    private TextView textViewSubject;
    private TextView textViewNick;
    private TextView textViewDate;
    private TextView textViewHits;
    private TextView textViewContent;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.angel_post_view);

        textViewSubject = (TextView)findViewById(R.id.textViewSub);
        textViewNick = (TextView)findViewById(R.id.textViewNIck);
        textViewDate = (TextView)findViewById(R.id.textViewDate);
        textViewHits = (TextView)findViewById(R.id.textViewHits);
        textViewContent = (TextView)findViewById(R.id.textViewContent);

        Intent intent = getIntent();

        textViewSubject.setText(intent.getStringExtra("subject"));
        textViewNick.setText(intent.getStringExtra("nick"));
        textViewDate.setText(intent.getStringExtra("date"));
        textViewHits.setText(intent.getStringExtra("hits"));
        textViewContent.setText(intent.getStringExtra("contents"));
    }
}
