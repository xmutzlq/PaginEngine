package ylz.android.engine.paging.main;

import android.app.Application;

import com.sherlockshi.toast.ToastUtils;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ToastUtils.init(this);
    }
}
