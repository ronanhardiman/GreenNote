package com.bruce.note.greennote.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bruce.note.greennote.R;
import com.bruce.note.greennote.utils.AppPrefs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView mTextMessage;
    private Button button,button1;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final static String REMAIN_TIME = "remain_time";


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };
    private static Long COUNT = 60L;
    private static final int ITEM_COUNT = 30;

    private void resetTimeCount() {
        AppPrefs.setLong(REMAIN_TIME,0L);
    }
    private void initTimeCount() {
        button.setText("倒计时30S");
        button1.setText("暂停");
        final Long remain_time = AppPrefs.getLong(REMAIN_TIME);
        final AtomicLong atomicLong = new AtomicLong(remain_time);
//        COUNT = remain_time;
        Timber.d("initTimeCount thread %s, remain_time: %s",Thread.currentThread().getName(),remain_time);
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(COUNT)
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@io.reactivex.annotations.NonNull Long aLong) throws Exception {
//                        long l = atomicLong.incrementAndGet();
//                        Long totalCount = remain_time + aLong;
//                        if (l >= ITEM_COUNT) {
//                            clearDis(aLong);
//                            resetTimeCount();
//                            showDialog();
//                            Toast.makeText(MainActivity.this, "时间到："+l, Toast.LENGTH_SHORT).show();
//                        }else {
//                            AppPrefs.setLong(REMAIN_TIME,l);
//                        }
//                        Timber.d("Apply aLong: %s thread %s,l: %s,", aLong,Thread.currentThread().getName(),l);
                        return aLong;
                    }
                }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@io.reactivex.annotations.NonNull Disposable disposable) throws Exception {
                compositeDisposable.add(disposable);
                Timber.d("accept thread %s,",Thread.currentThread().getName());
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {
                        compositeDisposable.add(d);
                        Timber.d("onSubscribe thread %s,",Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Long aLong) {
                        Timber.d("onNext aLong: %s thread %s,", aLong,Thread.currentThread().getName());
                        long l = atomicLong.incrementAndGet();
                        button.setText(l+"=秒");
                        Long totalCount = remain_time + aLong;
                        if (l >= ITEM_COUNT) {
                            clearDis(aLong);
                            resetTimeCount();
                            showDialog();
                            Toast.makeText(MainActivity.this, "时间到："+l, Toast.LENGTH_SHORT).show();
                        }else {
                            AppPrefs.setLong(REMAIN_TIME,l);
                        }
                        Timber.d("Apply aLong: %s thread %s,l: %s,", aLong,Thread.currentThread().getName(),l);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        Timber.e(e,"onError");
                    }

                    @Override
                    public void onComplete() {
                        Timber.d("onComplete");
                    }
                });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("时间到，请休息一下")
                .setPositiveButton("休息", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("再看一小时", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        initTimeCount();
                    }
                })
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mTextMessage.setOnClickListener(this);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button.setText("倒计时30S");
        button1.setText("暂停");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message:
                initTimeCount();
                break;
            case R.id.button:
                initTimeCount();
                break;
            case R.id.button1:
                Long aLong = AppPrefs.getLong(REMAIN_TIME);
                clearDis(aLong);
                if (button1.getText().equals("暂停")) {
                    button1.setText("恢复");
                }else {
                    initTimeCount();
                    button1.setText("暂停");
                }
                break;
            case R.id.button2:
                initTimeCount();
                break;
        }
    }

    private void clearDis(Long along) {
        Timber.d("clearDis long :%s,",along);
        compositeDisposable.clear();
        AppPrefs.setLong(REMAIN_TIME,along);
    }
}
