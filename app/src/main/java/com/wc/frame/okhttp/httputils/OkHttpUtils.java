package com.wc.frame.okhttp.httputils;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.CacheControl;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * author: Timor
 * Describe: okhttp 网络请求 2.7.5
 * time: 2017/1/12.
 */

public class OkHttpUtils {
    private ResaultData data;
    private Context context;
    private OkHttpClient mOkHttpClient;

    public OkHttpUtils(Context context, ResaultData data) {
        this.data = data;
        this.context = context;
        mOkHttpClient = new OkHttpClient();
        
        //超时设置
        //连接时间
        mOkHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        //等待时间
        mOkHttpClient.setWriteTimeout(20, TimeUnit.SECONDS);
        //读取时间
        mOkHttpClient.setReadTimeout(20, TimeUnit.SECONDS);


        //设置缓存
        File sdcache = context.getExternalCacheDir();
        int cacheSize = 10 * 1024 * 1024;
        mOkHttpClient.setCache(new Cache(sdcache.getAbsoluteFile(), cacheSize));
    }

    /**
     * 异步get请求
     */
    public void getAsynHttp() {
        //创建okHttpClient对象

        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                //   .cacheControl(CacheControl.FORCE_CACHE)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.e("wangshu", "cache---" + str);
                    Log.e("wangshu", "cache---" + response.cacheResponse().body().string());
                    data.resatlt(str);
                } else {
                    String str = response.body().string();
                    Log.e("wangshu", "network---" + str);
                    data.resatlt(str);

                }
                
            /*  String str = response.body().string();
                data.resatlt(str);*/
            }
        });
    }

    /**
     * 同步Get请求
     */
    public void tongBu() throws IOException {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        Log.e("isSuccessful", ">>>>>");
        Call call = mOkHttpClient.newCall(request);
        Response mResponse = call.execute();
        if (mResponse.isSuccessful()) {
            Log.e("isSuccessful", "isSuccessful");
            data.resatlt(mResponse.body().string());
        } else {
            Log.e("isSuccessful", "false");
            throw new IOException("Unexpected code " + mResponse);
        }
    }

    /**
     * 异步post
     */
    private void postAsynHttp() {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        RequestBody formBody = new FormEncodingBuilder()
                .add("size", "10")
                .build();
        Request request = new Request.Builder()
                .url("http://api.1-blog.com/biz/bizserver/article/list.do")
                .post(formBody)
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {
                String str = response.body().string();
                data.resatlt(str);
            }
        });

    }

    public interface ResaultData {
        void resatlt(String data);
    }
}
