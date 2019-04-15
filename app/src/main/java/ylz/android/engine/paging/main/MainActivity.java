package ylz.android.engine.paging.main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import ylz.android.engine.paging.main.helper.MainHelper;
import ylz.android.engine.paging.main.utils.GlideCacheUtil;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/15<p>
 * <p>Description：<p>
 */
public class MainActivity extends AppCompatActivity {

    private MainHelper mainHelper;

    private TextView clearCacheTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mainHelper = new MainHelper();

        findViewById(R.id.local_btn).setOnClickListener(v -> {
            LocalTestActivity.openLocalTestActivity(this);
        });

        findViewById(R.id.remote_btn).setOnClickListener(v -> {
            PagingTestActivity.openPagingTestActivity(this);
        });

        clearCacheTv = findViewById(R.id.clear_cache_btn);
        reSetCache();
        clearCacheTv.setOnClickListener(v -> {
            GlideCacheUtil.getInstance().clearImageAllCache(this);
            new Handler(Looper.getMainLooper()).postDelayed(()->{
                reSetCache();
            }, 1000);
        });
    }

    private void reSetCache() {
        clearCacheTv.setText("图片缓存清理：" + GlideCacheUtil.getInstance().getCacheSize(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        reSetCache();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mainHelper.onKeyDown(keyCode, event) ? true : super.onKeyDown(keyCode, event);
    }
}
