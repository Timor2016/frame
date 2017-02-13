package com.wc.frame.okhttp.httputils;

import android.content.Context;

import com.squareup.okhttp.FormEncodingBuilder;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * author: Timor
 * Describe:
 * time: 2017/1/13.
 */

public class OkHttpUtils3 {
    private ResautData mResaut;
    private OkHttpClient okHttpClient;

    public OkHttpUtils3(Context context, ResautData data) {
        this.mResaut = data;
        okHttpClient = new OkHttpClient();
    }

    public void getRequest() {
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        try {
            Response response = okHttpClient.newCall(request).execute();
            if (response.isSuccessful()) {
                mResaut.resault(response.body().string());
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (Exception e) {

        }

    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //pos上传JSON数据
    public void post(String url, String json) throws IOException {
        //post带参数请求
       /* RequestBody formBody = new FormEncodingBuilder()
                .add("platform", "android")
                .add("name", "bug")
                .add("subject", "XXXXXXXXXXXXXXX")
                .build();*/
        //pos上传JSON数据
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = okHttpClient.newCall(request).execute();
        if (response.isSuccessful()) {
            mResaut.resault(response.body().string());
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }
    
    
    public interface ResautData {
        void resault(String data);
    }
}
