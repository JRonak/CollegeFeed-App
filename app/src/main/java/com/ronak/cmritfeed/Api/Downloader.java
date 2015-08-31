package com.ronak.cmritfeed.Api;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Telephony;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by ronak on 12/8/15.
 */
public class Downloader  {
    Handler handler;
    String urlStr;

    public Downloader(Handler handler,String url){
        this.handler=handler;
        this.urlStr=url;
    }

    public void Download(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(urlStr);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String s;
                    while((s=bufferedReader.readLine())!=null){
                        stringBuilder.append(s);
                    }
                    Log.e("Downloader", "Downloader");
                    Message message = handler.obtainMessage();
                    Bundle bundle = new Bundle();
                    bundle.putString("data",stringBuilder.toString());
                    message.setData(bundle);
                    handler.dispatchMessage(message);
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
                Log.e("Download/Helper","Error Reading file");
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }



}
