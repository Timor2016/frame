package com.wc.frame.okhttp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.wc.frame.R;
import com.wc.frame.okhttp.httputils.OkHttpUtils;

/**
 * author: Timor
 * Describe:
 * time: 2017/1/12.
 */

public class OkHttpActivity extends AppCompatActivity implements OkHttpUtils.ResaultData {
    private TextView tv;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);
        initView();
    }

    private void initView() {
        
        tv = (TextView) findViewById(R.id.text);
       final OkHttpUtils http = new OkHttpUtils(this, this);
        http.getAsynHttp();

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http.getAsynHttp();
            }
        });
        
      
        /*try{
            http.tongBu();
        }catch (Exception e){
            
        }*/

    }

    @Override
    public void resatlt(final String data) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(data);
            }
        });


    }
}
