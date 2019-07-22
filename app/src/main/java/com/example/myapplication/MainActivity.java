package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    TextView tvWebContent;
    TextView tvWebContent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvWebContent = findViewById(R.id.tvWebContent);
        tvWebContent2 = findViewById(R.id.tvWebContent2);
        getJson("https://www.baidu.com",
                tvWebContent, false);
        getJson("https://jsonplaceholder.typicode.com/posts",
                tvWebContent2, true);

    }

    public void getJson(final String url_s, final TextView tv, final boolean ifParse){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //你的URL
//                    String url_s = "https://jsonplaceholder.typicode.com/posts"; // json
////                    String url_s = "http://www.baidu.com";
////                    String url_s = "https://www.baidu.com";
                    URL url = new URL(url_s);
//                    HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
//                    conn.setSSLSocketFactory();
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    //设置连接属性。不喜欢的话直接默认也阔以

                    conn.setConnectTimeout(5000);//设置超时
                    conn.setUseCaches(false);//数据不多不用缓存了

                    //这里连接了
                    conn.connect();
                    //这里才真正获取到了数据
                    InputStream inputStream = conn.getInputStream();
                    InputStreamReader input = new InputStreamReader(inputStream);
                    BufferedReader buffer = new BufferedReader(input);
                    if(conn.getResponseCode() == 200){//200意味着返回的是"OK"
                        String inputLine;
                        StringBuffer resultData  = new StringBuffer();//StringBuffer字符串拼接很快
                        while((inputLine = buffer.readLine())!= null){
                            resultData.append(inputLine);
                        }
                        final String text = resultData.toString();
                        Log.v("out---------------->",text);

                        MainActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv.setText(text);
                            }
                        });

                        if(ifParse)
                            parseJson(text);
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }



    public void parseJson(String text){
        try{
            JSONArray jsonArr = new JSONArray(text);
            Log.v("out---------------->", "*******************");
            Log.v("out---------------->", "*******************");
            Log.v("out---------------->", "*******************");
            int length = jsonArr.length();
            for(int i =0;i<length;i++){
                JSONObject person = jsonArr.getJSONObject(i);
                String title = person.getString("title");
                Log.v("out---------------->",
                        i+"---------------"+title);
            }


//            //这里的text就是上边获取到的数据，一个String.
//            JSONObject jsonObject = new JSONObject(text);
//            //getJSONArray中的参数就是你想要分析的JSON的键
//            JSONArray jsonDatas = jsonObject.getJSONArray("data");
//            int length = jsonDatas.length();
//            String test;
//
//            Log.v("out---------------->", "*******************");
//            Log.v("out---------------->", "*******************");
//            Log.v("out---------------->", "*******************");
//
//            for(int i =0;i<length;i++){
//                JSONObject person = jsonDatas.getJSONObject(i);
//                test = person.getString("id");
//                Log.v("out---------------->",
//                        i+"---------------"+test);
//            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}
