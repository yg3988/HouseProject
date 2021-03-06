package com.example.jason.houseproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AngelBoardActivity extends AppCompatActivity
{
    ListView listView;
    Toolbar toolbar;

    ArrayList<HashMap<String,String>> arrBoard;
    JSONArray list=null;
    String myJson;

    private static final String TAG_RESULT = "result";
    private static final String TAG_BOARDNO = "no";
    private static final String TAG_SUBJECT = "sub";
    private static final String TAG_NICK = "nick";
    private static final String TAG_HIT = "hits";
    private static final String TAG_DATE = "date";
    private static final String TAG_DESCRIPTION = "contents";
    private static final String TAG_IMAGE = "img";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_angel_board);

        toolbar = (Toolbar) findViewById(R.id.toolbarAngelBoard);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listBoard);

        arrBoard=new ArrayList<HashMap<String, String>>();
        getData("http://cir112.cafe24.com/boardList.php");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), AngelBoardPostView.class);
                intent.putExtra("no",arrBoard.get(i).get(TAG_BOARDNO));
                intent.putExtra("subject", arrBoard.get(i).get(TAG_SUBJECT));
                intent.putExtra("nick",arrBoard.get(i).get(TAG_NICK));
                intent.putExtra("date",arrBoard.get(i).get(TAG_DATE));
                intent.putExtra("hits",arrBoard.get(i).get(TAG_HIT));
                intent.putExtra("contents",arrBoard.get(i).get(TAG_DESCRIPTION));
                intent.putExtra("img",arrBoard.get(i).get(TAG_IMAGE));
                startActivity(intent);
                finish();
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
        switch (item.getItemId()){
            case android.R.id.home: //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            case R.id.newPost:
                Intent intentWrite = new Intent(getApplicationContext(), AngelBoardPostWrite.class);
                startActivity(intentWrite);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void showList()
    {
        try
        {
            JSONObject jsonObj = new JSONObject(myJson);
            list = jsonObj.getJSONArray(TAG_RESULT);

            for(int i=0;i<list.length();i++)
            {
                JSONObject c = list.getJSONObject(i);
                String no = c.getString(TAG_BOARDNO);
                String sub = c.getString(TAG_SUBJECT);
                String nick = c.getString(TAG_NICK);
                String hit = c.getString(TAG_HIT);
                String img = c.getString(TAG_IMAGE);
                String strDate = c.getString(TAG_DATE);

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = df.parse(strDate);
                strDate = new SimpleDateFormat("MM-dd").format(date);
                String contents = c.getString(TAG_DESCRIPTION);

                HashMap<String,String> boardItem = new HashMap<String, String>();

                boardItem.put(TAG_BOARDNO,no);
                boardItem.put(TAG_SUBJECT,sub);
                boardItem.put(TAG_NICK,nick);
                boardItem.put(TAG_HIT,hit);
                boardItem.put(TAG_DATE,strDate);
                boardItem.put(TAG_DESCRIPTION, contents);
                boardItem.put(TAG_IMAGE,img);

                arrBoard.add(boardItem);
            }

            ListAdapter adapter = new SimpleAdapter
                    (
                            AngelBoardActivity.this,
                            arrBoard,
                            R.layout.board_listview_item,
                            new String[]{TAG_BOARDNO,TAG_SUBJECT,TAG_NICK,TAG_HIT,TAG_DATE},
                            new int[]{R.id.item_no, R.id.sub, R.id.nick,R.id.hit,R.id.date}
                    );

            listView.setAdapter(adapter);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getData(String url){
        class GetDataJson extends AsyncTask<String, Void, String>
        {
            @Override
            protected String doInBackground (String... params)
            {
                String strUrl = params[0];

                BufferedReader bufferedReader = null;

                try
                {
                    URL url = new URL(strUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;

                    while((json = bufferedReader.readLine())!=null)
                    {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                }catch (Exception e)
                {
                    return null;
                }
            }
            protected void onPostExecute(String result)
            {
                myJson = result;
                showList();
            }
        }
        GetDataJson g = new GetDataJson();
        g.execute(url);
    }
}
