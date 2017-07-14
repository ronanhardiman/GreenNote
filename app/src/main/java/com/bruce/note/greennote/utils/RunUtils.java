package com.bruce.note.greennote.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.Pair;
import android.os.Process;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

/**
 * Created by renyugang on 16/11/10.
 * from 360
 */

public class RunUtils {
    private static final int MESSAGE_RUN_ON_UITHREAD = 0x1;

    private static Handler sHandler;

    /**
     * execute a runnable on ui thread, then return immediately. see also {@link #runOnUiThread(Runnable, boolean)}
     * @param runnable the runnable prepared to run
     */
    public static void runOnUiThread(Runnable runnable) {
        runOnUiThread(runnable, false);
    }

    /**
     * execute a runnable on ui thread
     * @param runnable the runnable prepared to run
     * @param waitUtilDone if set true, the caller thread will wait until the specific runnable finished.
     */
    public static void runOnUiThread(Runnable runnable, boolean waitUtilDone) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }

        CountDownLatch countDownLatch = null;
        if (waitUtilDone) {
            countDownLatch = new CountDownLatch(1);
        }
        Pair<Runnable, CountDownLatch> pair = new Pair<>(runnable, countDownLatch);
        getHandler().obtainMessage(MESSAGE_RUN_ON_UITHREAD, pair).sendToTarget();
        if (waitUtilDone) {
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static Executor getThreadPool() {
        return AsyncTask.THREAD_POOL_EXECUTOR;
    }

    public static String getProcessNameByPid(Context context, int pid) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessList = manager.getRunningAppProcesses();
        if (appProcessList != null) {
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessList) {
                if (pid == appProcessInfo.pid) {
                    return appProcessInfo.processName;
                }
            }
        }

        return null;
    }

    public static boolean isMainProcess(Context context) {
        String processName = getProcessNameByPid(context, Process.myPid());
        if (context.getPackageName().equals(processName)) {
            return true;
        }

        return false;
    }

    private static Handler getHandler() {
        synchronized (RunUtils.class) {
            if (sHandler == null) {
                sHandler = new InternalHandler();
            }
            return sHandler;
        }
    }

    private static class InternalHandler extends Handler {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_RUN_ON_UITHREAD) {
                Pair<Runnable, CountDownLatch> pair = (Pair<Runnable, CountDownLatch>)msg.obj;
                Runnable runnable = pair.first;
                runnable.run();
                if (pair.second != null) {
                    pair.second.countDown();
                }
            }
        }
    }
}
