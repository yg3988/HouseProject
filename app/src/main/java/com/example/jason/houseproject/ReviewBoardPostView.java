package com.example.jason.houseproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

public class ReviewBoardPostView extends AppCompatActivity{
    Toolbar toolbar;
    TextView textViewBoardTitle;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_review_post_view);

        Intent intent = getIntent();

        textViewBoardTitle=(TextView)findViewById(R.id.textViewBuildingName);
        toolbar = (Toolbar)findViewById(R.id.toolbarReviewPostView);

        setSupportActionBar(toolbar);
        textViewBoardTitle.setText(intent.getStringExtra("build"));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_view_menu, menu);
        return true;
    }
}
