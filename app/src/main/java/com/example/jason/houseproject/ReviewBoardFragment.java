package com.example.jason.houseproject;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReviewBoardFragment extends ListFragment {
    private static final int REVIEW_BOARD = 2;
    private static final String strURL = "http://cir112.cafe24.com/reviewBoardList.php";

    private String strLat;
    private String strLng;
    static int listCnt = 0;
    ListView listView;
    static FragmentListViewAdapter fragmentListViewAdapter;
    GetData gb;

    public ReviewBoardFragment(){ }  //default constructor

    public static ReviewBoardFragment newInstance(String param1, String param2) {
        ReviewBoardFragment fragment = new ReviewBoardFragment();
        Bundle args = new Bundle();
        args.putString("latitude", param1);
        args.putString("longitude", param2);
        fragment.setArguments(args);
        Log.d("LatLng",param1+" "+param2);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            strLat = getArguments().getString("latitude");
            strLng = getArguments().getString("longitude");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        Log.d("프래그먼트","onCreateView 실행");
        View view = inflater.inflate(R.layout.board_review,null);
        listView = (ListView)view.findViewById(R.id.listViewReview);
        fragmentListViewAdapter = new FragmentListViewAdapter(getContext());
        setListAdapter(fragmentListViewAdapter);

        gb = new GetData(REVIEW_BOARD,strLat,strLng);//DB에 좌표값을 보내서 해당 좌표에 해당하는 게시글만 뽑아옴
        gb.getData(strURL);

        return super.onCreateView(inflater,container,savedInstanceState);
    }

    public static void show(String myJson){
        try {
            JSONObject jsonObj = new JSONObject(myJson);

            JSONArray list = jsonObj.getJSONArray("result");
            listCnt = list.length();
            Log.d("listCnt",Integer.toString(listCnt));

            String[] no = new String[listCnt];
            String[] sub = new String[listCnt];
            String[] nick = new String[listCnt];
            String[] strDate = new String[listCnt];

            for (int i = 0; i < listCnt; i++) {
                JSONObject c = list.getJSONObject(i);
                no[i] = c.getString("no");
                sub[i] = c.getString("sub");
                nick[i] = c.getString("nick");
                strDate[i] = c.getString("date");
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
                Date date = df.parse(strDate[i]);
                strDate[i] = new SimpleDateFormat("MM-dd",Locale.KOREA).format(date);
            }
            fragmentListViewAdapter.addItem(no,sub,strDate,nick);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*@Override
    public void onListItemClick (ListView l, View v, int postion, long id){

    }*/
}
