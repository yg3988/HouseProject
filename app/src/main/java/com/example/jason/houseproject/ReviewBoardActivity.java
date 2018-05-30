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
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ReviewBoardActivity extends AppCompatActivity{
    private static final String TAG_RESULT = "result";
    private static final String TAG_SUBJECT = "sub";
    private static final String TAG_NICK = "nick";
    private static final String TAG_HIT = "hits";
    private static final String TAG_DATE = "date";
    private static final String TAG_DESCRIPTION = "content";
    private static final String TAG_MONTHLY = "monthly";
    private static final String TAG_DEPOSIT="deposit";
    private static final String TAG_MAINTENANCE = "maintenance";
    private static final String[] TAG_OPTION = {    "bed",
                                                        "desk",
                                                        "closet",
                                                        "refrigerator",
                                                        "airconditioner",
                                                        "microwave",
                                                        "internet",
                                                        "water",
                                                        "tv"};
    private static final String STRING_URI = "http://cir112.cafe24.com/reviewBoardList2.php";

    private Toolbar toolbar;
    private String lat;
    private String lng;
    private String build;
    private ListView listView;
    private TextView textViewBuildTitle;
    ArrayList<HashMap<String,String>> arrBoard;
    JSONArray list=null;
    String myJson;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_board);
        textViewBuildTitle = (TextView)findViewById(R.id.textViewBuildingName);
        Intent intent = getIntent();
        build = intent.getStringExtra("building");
        toolbar = (Toolbar) findViewById(R.id.toolbarReviewBoard);
        textViewBuildTitle.setText(build);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lat = intent.getStringExtra("latitude");
        lng = intent.getStringExtra("longitude");

        listView = (ListView)findViewById(R.id.listViewReviewBoard);

        arrBoard=new ArrayList<HashMap<String, String>>();

        getData(STRING_URI);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ReviewBoardPostView.class);
                intent.putExtra("build",build);
                intent.putExtra("lat",lat);
                intent.putExtra("lng",lng);
                intent.putExtra("contents",arrBoard.get(i).get(TAG_DESCRIPTION));
                intent.putExtra("monthly",arrBoard.get(i).get(TAG_MONTHLY));
                intent.putExtra("deposit",arrBoard.get(i).get(TAG_DEPOSIT));
                intent.putExtra("maintenance", arrBoard.get(i).get(TAG_MAINTENANCE));
                for(int j = 0; j<TAG_OPTION.length;j++){
                    intent.putExtra(Integer.toString(j),arrBoard.get(i).get(TAG_OPTION[j]));
                }
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
                Intent intentWrite = new Intent(getApplicationContext(), ReviewBoardPostWrite.class);
                intentWrite.putExtra("building",build);
                intentWrite.putExtra("lat",lat);
                intentWrite.putExtra("lng",lng);
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
                String sub = c.getString(TAG_SUBJECT);
                String nick = c.getString(TAG_NICK);
                String contents = c.getString(TAG_DESCRIPTION);
                String monthly = c.getString(TAG_MONTHLY);
                String deposit = c.getString(TAG_DEPOSIT);
                String maintenance = c.getString(TAG_MAINTENANCE);
                String[] option = new String[TAG_OPTION.length];
                for(int j = 0; j<TAG_OPTION.length;j++)  option[j] = c.getString(TAG_OPTION[j]);


                String strDate = c.getString(TAG_DATE);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                Date date = df.parse(strDate);
                strDate = new SimpleDateFormat("MM-dd").format(date);

                HashMap<String,String> boardItem = new HashMap<String, String>();

                boardItem.put(TAG_SUBJECT,sub);
                boardItem.put(TAG_NICK,nick);
                boardItem.put(TAG_DATE,strDate);
                boardItem.put(TAG_DESCRIPTION, contents);
                boardItem.put(TAG_MONTHLY,monthly);
                boardItem.put(TAG_DEPOSIT,deposit);
                boardItem.put(TAG_MAINTENANCE,maintenance);
                for(int j = 0; j<TAG_OPTION.length;j++) boardItem.put(TAG_OPTION[j],option[j]);

                arrBoard.add(boardItem);
            }

            ListAdapter adapter = new SimpleAdapter
                    (
                            ReviewBoardActivity.this,
                            arrBoard,
                            R.layout.review_listview_item,
                            new String[]{TAG_SUBJECT,TAG_NICK,TAG_DATE},
                            new int[]{ R.id.sub, R.id.nick,R.id.date}
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
                String postParams="lat=" + lat + "&lng=" + lng;
                BufferedReader bufferedReader = null;

                try
                {
                    URL url = new URL(strUrl);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    con.setReadTimeout(5000);
                    con.setConnectTimeout(5000);
                    con.setRequestMethod("POST");
                    con.setDoInput(true);
                    con.connect();

                    OutputStream outputStream = con.getOutputStream();
                    outputStream.write(postParams.getBytes("UTF-8"));
                    outputStream.flush();
                    outputStream.close();

                    int responseStatusCode = con.getResponseCode();

                    InputStream inputStream;
                    if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                        inputStream = con.getInputStream();
                    } else {
                        inputStream = con.getErrorStream();
                    }

                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                    bufferedReader = new BufferedReader(inputStreamReader);

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