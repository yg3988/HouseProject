package com.example.jason.houseproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AngelBoardActivity extends AppCompatActivity {
    private static final int MODE = 1;
    private static final String TAG_RESULT = "result";
    private static final String TAG_BOARDNO = "no";
    private static final String TAG_SUBJECT = "sub";
    private static final String TAG_NICK = "nick";
    private static final String TAG_HIT = "hits";
    private static final String TAG_DATE = "date";
    private static final String TAG_DESCRIPTION = "contents";
    private static final String TAG_IMAGE = "img";
    static final String url = "http://cir112.cafe24.com/boardList.php";

    static public ListView listView;
    static public SimpleListAdapter simpleListAdapter;
    static public ArrayList<HashMap<String, String>> arrBoard;
    static public JSONArray list = null;
    static public int listCnt;
    Toolbar toolbar;
    GetData gd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angel_board);

        toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        simpleListAdapter = new SimpleListAdapter(this);
        listView = (ListView) findViewById(R.id.listBoard);
        listCnt = 0;
        listView.setAdapter(simpleListAdapter);
        arrBoard = new ArrayList<HashMap<String, String>>();
        gd = new GetData(MODE);

        gd.getData(url);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AngelBoardPostView.class);
                intent.putExtra("subject", arrBoard.get(i).get(TAG_SUBJECT));
                intent.putExtra("nick", arrBoard.get(i).get(TAG_NICK));
                intent.putExtra("date", arrBoard.get(i).get(TAG_DATE));
                intent.putExtra("hits", arrBoard.get(i).get(TAG_HIT));
                intent.putExtra("contents", arrBoard.get(i).get(TAG_DESCRIPTION));
                intent.putExtra("img", arrBoard.get(i).get(TAG_IMAGE));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.newPost) {
            Intent intent = new Intent(getApplicationContext(), AngelBoardPostWrite.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static void show(String myJson) {
        try {
            JSONObject jsonObj = new JSONObject(myJson);

            list = jsonObj.getJSONArray(TAG_RESULT);
            listCnt = list.length();

            String[] no = new String[listCnt];
            String[] sub = new String[listCnt];
            String[] nick = new String[listCnt];
            String[] hit = new String[listCnt];
            String[] strDate = new String[listCnt];

            for (int i = 0; i < listCnt; i++) {
                JSONObject c = list.getJSONObject(i);
                no[i] = c.getString(TAG_BOARDNO);
                sub[i] = c.getString(TAG_SUBJECT);
                nick[i] = c.getString(TAG_NICK);
                hit[i] = c.getString(TAG_HIT);
                String img = c.getString(TAG_IMAGE);
                strDate[i] = c.getString(TAG_DATE);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.KOREA);
                Date date = df.parse(strDate[i]);
                strDate[i] = new SimpleDateFormat("MM-dd",Locale.KOREA).format(date);
                String contents = c.getString(TAG_DESCRIPTION);

                HashMap<String, String> boardItem = new HashMap<String, String>();

                boardItem.put(TAG_BOARDNO, no[i]);
                boardItem.put(TAG_SUBJECT, sub[i]);
                boardItem.put(TAG_NICK, nick[i]);
                boardItem.put(TAG_HIT, hit[i]);
                boardItem.put(TAG_DATE, strDate[i]);
                boardItem.put(TAG_DESCRIPTION, contents);
                boardItem.put(TAG_IMAGE, img);

                arrBoard.add(boardItem);
            }

            simpleListAdapter.setNo(no);
            simpleListAdapter.setSub(sub);
            simpleListAdapter.setNick(nick);
            simpleListAdapter.setHit(hit);
            simpleListAdapter.setStrDate(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}