package tw.idv.terry.myabc.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Vector;

/**
 * Created by wangtrying on 2015/3/22.
 */
public class MyLog {

    public static volatile boolean DEBUG = true;

    public static final int MSG_LOG_EXCEPTION = 0X0711;

    private static Object mHandlerLock = new Object();
    private static Vector<Handler> mHandlerList = new Vector<>(8);

    public static void addHandler(final Handler aHandler){
        synchronized (mHandlerLock) {
            if (!mHandlerList.contains(aHandler)) {
                mHandlerList.add(aHandler);
            }
        }
    }

    public static boolean removeHandler(final Handler aHandler){
        synchronized (mHandlerLock){
            return mHandlerList.remove(aHandler);
        }
    }

    public static void v(final String aTag, final String aLog) {
        if (DEBUG) {
            Log.v(aTag, aLog);
        }
    }


    public static void i(final String aTag, final String aLog){
        if (DEBUG) {
            Log.i(aTag, aLog);
        }
    }

    public static void e(final String aTag, final String aLog) {
        if (DEBUG) {
            Log.e(aTag, aLog);
        }
    }

    public static void d(final String aTag, final String aLog) {
        if (DEBUG) {
            Log.d(aTag, aLog);
        }
    }

    public static void catchIt(Throwable aThrowable){
        String throwableString = aThrowable.getMessage();

        if (DEBUG) {
            synchronized (mHandlerLock) {
                for (Handler handler : mHandlerList) {
                    Message msg = handler.obtainMessage(MSG_LOG_EXCEPTION);
                    msg.obj = throwableString;
                    handler.sendMessage(msg);
                }
            }
        }
    }
}
