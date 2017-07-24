package com.example.wn.networktest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView responseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendRequest= (Button) findViewById(R.id.send_request_btn);
        responseText= (TextView) findViewById(R.id.response_text_tv);
        sendRequest.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send_request_btn:
                sendRequestWithHttpUrlConnection();
                break;
            default:break;
        }
    }

    private void sendRequestWithHttpUrlConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader bufferedReader=null;
                HttpURLConnection connection=null;
                try {
                    URL url=new URL("https://www.baidu.com");
                    connection=(HttpURLConnection)url.openConnection();
                    if(200==connection.getResponseCode()){
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream is=connection.getInputStream();
                        bufferedReader=new BufferedReader(new InputStreamReader(is));
                        StringBuilder responseBuilder=new StringBuilder();
                        String line;
                        while((line=bufferedReader.readLine())!=null) {
                            responseBuilder.append(line);
                        }
                        showResponse(responseBuilder.toString());
                    }
                }  catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if(bufferedReader!=null){
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if(connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    private void showResponse(final String response){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(response);
            }
        });
    }
}
