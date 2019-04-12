package ylz.android.engine.paging.util;

import android.util.Log;

import ylz.android.engine.paging.BuildConfig;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/9<p>
 * <p>Description：<p>
 */
public class LogUtils {

    public static void eTag(String tag, String msg) {
        if(BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }
}
