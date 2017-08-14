package com.bruce.note.greennote.utils;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 计时提醒
 * Created by liq on 2017/7/14.
 */

public class RestUtils {

    private RestUtils() {
    }

    private static CompositeDisposable compositeDisposable = new CompositeDisposable();
    private static final String REMAIN_TIME = "remain_time";
    private static Long totalTime = 61L;
    private static AtomicLong atomicLong = new AtomicLong();

    /**
     * 初始化
     *
     * @param TotalTime
     */
    public static void init(Long TotalTime) {
        totalTime = TotalTime;
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(TotalTime)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .map(new Function<Long, Long>() {
                    @Override
                    public Long apply(@NonNull Long aLong) throws Exception {
                        return aLong;
                    }
                })
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        long l = atomicLong.incrementAndGet();
                        if (l >= totalTime) {
                            EventBus.getDefault().post(new EventRest(1));
                            reset();
                        } else {
                            EventBus.getDefault().post(new EventRest(2));
                            AppPrefs.setLong(REMAIN_TIME, l);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * 初始化
     */
    public static void init() {
        init(totalTime);
    }

    public static String getRemainTime() {
        return String.valueOf(atomicLong.floatValue());
    }

    /**
     * 暂停
     */
    public static void onPause() {
        compositeDisposable.clear();
        AppPrefs.setLong(REMAIN_TIME, atomicLong.get());
    }

    /**
     * 恢复
     */
    public static void onStart() {
        Long aLong = AppPrefs.getLong(REMAIN_TIME);
        atomicLong = new AtomicLong(aLong);
        init(totalTime);
    }

    public static void reset() {
        AppPrefs.setLong(REMAIN_TIME, 0L);
        compositeDisposable.clear();
    }

    /**
     * 释放
     */
    public static void release() {
        compositeDisposable.clear();
        atomicLong = null;
    }

    public static class EventRest {
        public int tag;

        public EventRest(int tag) {
            this.tag = tag;
        }
    }
}
