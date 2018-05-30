package com.example.jason.houseproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

//ListView 요소에 실질적인 값을 넣는 Class
//@Author 문윤기
//참고 : http://answerofgod.tistory.com

public class BoardViewItem extends LinearLayout{
    private TextView textViewItemNo;
    private TextView textViewSub;
    private TextView textViewNick;
    private TextView textViewHit;
    private TextView textViewDate;

    public BoardViewItem(Context context) {
        super(context);
        init(context);
    }

    public void setTextViewItemNo(String data){
        textViewItemNo.setText(data);
        Log.d("No",data);
    }

    public void setTextViewSub(String data){
        textViewSub.setText(data);
        Log.d("Sub",data);
    }

    public void setTextViewNick(String data){
        textViewNick.setText(data);
        Log.d("Nick",data);
    }

    public void setTextViewHit(String data){
        textViewHit.setText(data);
        Log.d("Hit",data);
    }

    public void setTextViewDate(String data){
        textViewDate.setText(data);
        Log.d("Date",data);
    }

    public void setTextViewError(String data){
        textViewItemNo.setText(data);
        textViewSub.setText(data);
        textViewNick.setText(data);
        textViewHit.setText(data);
        textViewDate.setText(data);
    }

    public void init(Context context){
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        inflater.inflate(R.layout.board_angel_listview_item,this,true);

        textViewItemNo = (TextView)findViewById(R.id.angelNo);
        textViewSub = (TextView)findViewById(R.id.angelSub);
        textViewNick = (TextView)findViewById(R.id.angelNick);
        textViewHit = (TextView)findViewById(R.id.angelHit);
        textViewDate = (TextView)findViewById(R.id.angelDate);
    }
}
