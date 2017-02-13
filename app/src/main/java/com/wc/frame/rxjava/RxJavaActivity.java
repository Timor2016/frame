package com.wc.frame.rxjava;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Path;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wc.frame.MainActivity;
import com.wc.frame.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * author: Timor
 * Describe:rxjava学习
 * time: 2017/1/19.
 */

public class RxJavaActivity extends AppCompatActivity {
    private TextView text;
    private ImageView img;
    private ImageView img2;
    private TextView text1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
        initView();
        //rxjava简单使用
        initData();
        //map简单变换
        initData2();
        //flatMap() 难理解的变换
        initData3();
        //变换的原理：lift()
        initLeft();
        Log.e("当前线程main",Thread.currentThread().getName());
    }

    private void initView() {
        text = (TextView) findViewById(R.id.text);
        img = (ImageView)findViewById(R.id.img);
        img2 = (ImageView)findViewById(R.id.img2);
        text1 = (TextView) findViewById(R.id.text1);
    }
    
    private void initLeft() {
        Observable.Operator operator = new Observable.Operator<Integer, String>() {
            @Override
            public Subscriber<? super String> call(final Subscriber<? super Integer> subscriber) {
                return new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.e("initLeft","onCompleted");
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("initLeft","onError");
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e("initLeft","onNext");
                        int value = Integer.valueOf(s); //进行转换
                        subscriber.onNext(value);
                    }
                };
            }
        };
        
        Observable<String> originObservalbe = Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext("100");
                subscriber.onCompleted();
                subscriber.onStart();
            }
        });
        
        Observable<Integer> targetObservable = originObservalbe.lift(operator);



        Subscriber subscribe = new Subscriber<Integer>() {
            @Override
            public void onStart() {
                Log.e("当前线程onStart",Thread.currentThread().getName());
                text.setText("来啊！互相伤害啊");

            }
            @Override
            public void onCompleted() {
                Log.e("initLeftsubscribe","onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.e("initLeftsubscribe","onError");
                Log.e("initLeftsubscribe",e.getMessage());
                Log.e("initLeftsubscribe",e.toString());
            }

            @Override
            public void onNext(Integer s) {
               /* Log.e("当前线程onNext",Thread.currentThread().getName());
                if("互相伤害啊".equals(text.getText().toString())){
                    Toast.makeText(RxJavaActivity.this, "互相伤害啊", Toast.LENGTH_SHORT).show();
                }*/
                text1.setText(s+"");
            }
        };
        targetObservable.subscribe(subscribe);
        
      /*  Observable.just("123").lift(new Observable.Operator<Integer, String>() {

            @Override
            public Subscriber<? super String> call(final Subscriber<? super Integer> subscriber) {
                return new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        Log.i("Z-MainActivity", "onCompleted: ");
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Z-MainActivity", "onError: " + e.getMessage());
                        subscriber.onError(e);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.i("Z-MainActivity", "onNext: s:" + s);
                        int value = Integer.valueOf(s) * 2; //转化为Integer类型的值并乘以2
                        subscriber.onNext(value);
                    }
                };
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Integer integer) {
                Log.i("Z-MainActivity", "onNext: END:" + integer);
            }
        });
        
        */
        
        
    }

    private void initData3() {
        Subscriber<Bitmap> subscriber = new Subscriber<Bitmap>() {
            @Override
            public void onCompleted() {
                Log.e(">>>>>>>>>","onCompleted执行了");
            }

            @Override
            public void onError(Throwable e) {
                Log.e(">>>>>>>>>","onError执行了"+e.getMessage());
            }

            @Override
            public void onNext(Bitmap bitmap) {
                Log.e(">>>>>>>>>","onNext执行了");
                
                img2.setImageBitmap(bitmap);
            }
        };


        String[][] path = {{"img/ic_launcher.png","img/test_zhibo.png","img/xfrx_icon.png"},{"img/ic_launcher.png","img/test_zhibo.png","img/xfrx_icon.png"}};
        Observable.from(path)
                .flatMap(new Func1<String[], Observable<Bitmap>>() {
                    @Override
                    public Observable<Bitmap> call(final String[] strings) {
                        
                        return Observable.from(new Future<Bitmap>() {
                            @Override
                            public boolean cancel(boolean mayInterruptIfRunning) {
                                return false;
                            }
                            @Override
                            public boolean isCancelled() {
                                return false;
                            }

                            @Override
                            public boolean isDone() {
                                return false;
                            }

                            @Override
                            public Bitmap get() throws InterruptedException, ExecutionException {
                                Bitmap image = null;
                                AssetManager am = getResources().getAssets();
                                try
                                {
                                    InputStream is = am.open(strings[1]);
                                    image = BitmapFactory.decodeStream(is);
                                    is.close();
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }

                                return image;
                            }

                            @Override
                            public Bitmap get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
                                Bitmap image = null;
                                AssetManager am = getResources().getAssets();
                                try
                                {
                                    InputStream is = am.open(strings[2]);
                                    image = BitmapFactory.decodeStream(is);
                                    is.close();
                                }
                                catch (IOException e)
                                {
                                    e.printStackTrace();
                                }

                                return image;
                            }
                        });
                    }
                }).subscribe(subscriber);
    }

    private void initData2() {
        Observable.just("img/ic_launcher.png") // 输入类型 String
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String filePath) { // 参数类型 String
                        Bitmap image = null;
                        AssetManager am = getResources().getAssets();
                        try
                        {
                            InputStream is = am.open(filePath);
                            image = BitmapFactory.decodeStream(is);
                            is.close();
                        }
                        catch (IOException e)
                        {
                            e.printStackTrace();
                        }

                        return image;
                    }
                })
                .subscribe(new Action1<Bitmap>() {
                    @Override
                    public void call(Bitmap bitmap) { // 参数类型 Bitmap
                        img.setImageBitmap(bitmap);
                    }
                });
    }

 

    Subscriber<String> subscribe;
    private void initData() {
        Observable<String> observable = Observable.create(
                new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        Log.e("当前线程observable",Thread.currentThread().getName());
                        try {
                            Thread.currentThread().sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext("期待成为大神");
                        subscriber.onCompleted();
                        subscriber.onStart();
                    }
                }) .subscribeOn(Schedulers.newThread()) // 指定 subscribe() 发生在 IO 线程
                .observeOn(AndroidSchedulers.mainThread());// 指定 Subscriber 的回调发生在主线程;
        //next
       /* Action1<String> onNextAction = new Action1<String>() {
            // onNext()
            @Override
            public void call(String s) {
                Log.e(">>>>", s);
            }
        };
        observable.subscribe(onNextAction);
        //
        Action1<Throwable> onErrorAction = new Action1<Throwable>() {
            // onError()
            @Override
            public void call(Throwable throwable) {
                // Error handling
            }
        };
        observable.subscribe(onNextAction,onErrorAction);
        Action0 onCompletedAction = new Action0() {
            // onCompleted()
            @Override
            public void call() {
                Log.e("onCompletedAction", "completed");
            }
        };
        observable.subscribe(onNextAction, onErrorAction, onCompletedAction);
        
*/
         subscribe = new Subscriber<String>() {
            @Override
            public void onStart() {
                Log.e("当前线程onStart",Thread.currentThread().getName());
                text.setText("来啊！互相伤害啊");
               
            }

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.e("当前线程onNext",Thread.currentThread().getName());
               if("互相伤害啊".equals(text.getText().toString())){
                   Toast.makeText(RxJavaActivity.this, "互相伤害啊", Toast.LENGTH_SHORT).show();
               }
                text.setText(s);
            }
        };
        observable.subscribe(subscribe);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscribe.unsubscribe();
    }
}
