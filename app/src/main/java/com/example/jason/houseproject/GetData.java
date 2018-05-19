package com.example.jason.houseproject;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

//DB에 존재하는 Data를 JSON형태로 가져오는 Class
//@Author 문윤기
//참고 : http://answerofgod.tistory.com

public class GetData {

    private static final int ANGEL_BOARD = 1;
    private static final int REVIEW_BOARD = 2;;

    private int mode;

    String myJson;

    GetData(int mode){
        this.mode = mode;
    }

    public void getData(String url){
        switch (mode){
            case ANGEL_BOARD:
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
                            bufferedReader.close();
                            return sb.toString().trim();
                        }catch (Exception e)
                        {
                            return null;
                        }
                    }
                    protected void onPostExecute(String result)
                    {
                        myJson = result;
                        AngelBoardActivity.show(myJson);//AngelBoardActivity 인스턴스의 show 메소드 호출
                    }
                }
                GetDataJson g = new GetDataJson();
                g.execute(url);
                break;
        }

    }
}
