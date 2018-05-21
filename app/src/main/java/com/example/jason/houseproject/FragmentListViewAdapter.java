package com.example.jason.houseproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

public class FragmentListViewAdapter extends BaseAdapter{
    private static Context parentContext;
    private static Context context;
    private ArrayList<ReviewBoardItem> reviewBoardItemsList = new ArrayList<ReviewBoardItem>();
    public FragmentListViewAdapter(Context context){
        this.context = context;
        Log.d("ListViewAdapter","잘되나");
    }

    String itemNo;
    String itemSub;
    String nick;
    String strDate;

    @Override
    public int getCount(){
        return reviewBoardItemsList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        parentContext = parent.getContext();

        ReviewBoardItem item = new ReviewBoardItem(context.getApplicationContext());

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) parentContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.board_review_item, parent, false);
        }



        item.setStrNo(itemNo);
        item.setStrSub(itemSub);
        item.setStrDate(strDate);
        item.setStrNick(nick);

        reviewBoardItemsList.add(item);

        return convertView;
    }
    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return reviewBoardItemsList.get(position) ;
    }

    public void addItem(String no, String sub, String date, String nick) {
        itemNo = no;
        itemSub = sub;
        strDate = date;
        this.nick = nick;
    }

}
