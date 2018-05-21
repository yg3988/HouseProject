package com.example.jason.houseproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

//CostomListAdapter
//@Author 문윤기
//참고 : http://answerofgod.tistory.com
//참고 : http://i5on9i.blogspot.kr/

public class SimpleListAdapter extends BaseAdapter{
    private static final int ANGELBOARD = 1;
    private static final int REVIEW_BOARD = 2;

    private String[] no;
    private String[] sub;
    private String[] nick;
    private String[] hit;
    private String[] strDate;
    private Context context;

    private String temp_data[] = new String[getCount()];

    public SimpleListAdapter(){this.context = null;};
    public SimpleListAdapter(Context context){
        this.context = context;
    }

    public void setNo(String[] data){
        no=data;
    }
    public void setSub(String[] data){
        sub=data;
    }
    public void setNick(String[] data){
        nick=data;
    }
    public void setHit(String[] data){
        hit=data;
    }
    public void setStrDate(String[] data){
        strDate=data;
    }

    @Override
    public int getCount(){
        return AngelBoardActivity.listCnt;
    }

    @Override
    public View getView(int position, View convertView,ViewGroup parent) {

        BoardViewItem boardViewItem = null;

        if (convertView != null) {
            boardViewItem = (BoardViewItem) convertView;
        } else {
            boardViewItem = new BoardViewItem(context.getApplicationContext());
        }

        try {
            boardViewItem.setTextViewItemNo(no[position]);
            boardViewItem.setTextViewSub(sub[position]);
            boardViewItem.setTextViewNick(nick[position]);
            boardViewItem.setTextViewHit(hit[position]);
            boardViewItem.setTextViewDate(strDate[position]);
        } catch (Exception e) {
            boardViewItem.setTextViewError("Error");
        }
        return boardViewItem;

    }

    public long getItemId(int postion){return postion;}

    public Object getItem(int position){return temp_data[position];}

}
