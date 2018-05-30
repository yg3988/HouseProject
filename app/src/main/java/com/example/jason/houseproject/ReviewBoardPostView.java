package com.example.jason.houseproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.TextView;

public class ReviewBoardPostView extends AppCompatActivity{
    Toolbar toolbar;

    TextView textViewBoardTitle;
    TextView textViewMonthly;
    TextView textViewDeposit;
    TextView textViewMaintenance;
    TextView textViewContents;

    CheckBox checkBoxOption0;
    CheckBox checkBoxOption1;
    CheckBox checkBoxOption2;
    CheckBox checkBoxOption3;
    CheckBox checkBoxOption4;
    CheckBox checkBoxOption5;
    CheckBox checkBoxOption6;
    CheckBox checkBoxOption7;
    CheckBox checkBoxOption8;

    String lat;
    String lng;
    boolean[] isOption;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_review_post_view);
        isOption = new boolean[9];
        Intent intent = getIntent();

        toolbar = (Toolbar)findViewById(R.id.toolbarReviewPostView);
        textViewBoardTitle=(TextView)findViewById(R.id.textViewBoardTitle);

        setSupportActionBar(toolbar);

        textViewBoardTitle.setText(intent.getStringExtra("build"));
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        textViewMonthly = (TextView)findViewById(R.id.textViewMonthly);
        textViewMaintenance = (TextView)findViewById(R.id.textViewMaintenance);
        textViewDeposit = (TextView)findViewById(R.id.textViewDesposit);

        textViewMonthly.setText(intent.getStringExtra("monthly")+"만원");
        textViewDeposit.setText(intent.getStringExtra("deposit")+"만원");
        textViewMaintenance.setText(intent.getStringExtra("maintenance")+"만원");

        checkBoxOption0 = (CheckBox)findViewById(R.id.checkBoxOption0);
        checkBoxOption1 = (CheckBox)findViewById(R.id.checkBoxOption1);
        checkBoxOption2 = (CheckBox)findViewById(R.id.checkBoxOption2);
        checkBoxOption3 = (CheckBox)findViewById(R.id.checkBoxOption3);
        checkBoxOption4 = (CheckBox)findViewById(R.id.checkBoxOption4);
        checkBoxOption5 = (CheckBox)findViewById(R.id.checkBoxOption5);
        checkBoxOption6 = (CheckBox)findViewById(R.id.checkBoxOption6);
        checkBoxOption7 = (CheckBox)findViewById(R.id.checkBoxOption7);
        checkBoxOption8 = (CheckBox)findViewById(R.id.checkBoxOption8);

        for(int i=0; i<isOption.length; i++){
            if(intent.getStringExtra(Integer.toString(i)).equals("1")) isOption[i] = true;
            else isOption[i] = false;
        }

        checkBoxOption0.setChecked(isOption[0]);
        checkBoxOption1.setChecked(isOption[1]);
        checkBoxOption2.setChecked(isOption[2]);
        checkBoxOption3.setChecked(isOption[3]);
        checkBoxOption4.setChecked(isOption[4]);
        checkBoxOption5.setChecked(isOption[5]);
        checkBoxOption6.setChecked(isOption[6]);
        checkBoxOption7.setChecked(isOption[7]);
        checkBoxOption8.setChecked(isOption[8]);

        textViewContents = (TextView)findViewById(R.id.textViewContents);
        textViewContents.setText(intent.getStringExtra("contents"));
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
        }
        return super.onOptionsItemSelected(item);
    }
}