package com.example.jason.houseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ReviewBoardItem extends LinearLayout{

    private TextView textViewNo;
    private TextView textViewSub;
    private TextView textViewNick;
    private TextView textViewDate;

    public ReviewBoardItem(Context context){
        super(context);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.board_review_item,this,true);

        textViewNo = (TextView)findViewById(R.id.reviewNo);
        textViewSub = (TextView)findViewById(R.id.reviewSub);
        textViewNick = (TextView)findViewById(R.id.reviewNick);
        textViewDate = (TextView)findViewById(R.id.reviewDate);
    }

    public void setStrNo(String data){
        textViewNo.setText(data);
    }

    public void setStrSub(String data){
        textViewSub.setText(data);
    }

    public void setStrNick(String data){
        textViewNick.setText(data);
    }
    public void setStrDate(String data){
        textViewDate.setText(data);
    }
}
