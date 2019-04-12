package ylz.android.engine.paging.jectpack;

import android.arch.paging.DataSource;
import android.support.annotation.NonNull;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
public class CustomPageDataSourceFactory<T> extends DataSource.Factory<Integer, T> {

    private CustomLoader mCustomLoader;

    public CustomPageDataSourceFactory(CustomLoader customLoader) {
        mCustomLoader = customLoader;
    }

    @NonNull
    @Override
    public DataSource<Integer, T> create() {
        return new CustomPageDataSource(mCustomLoader);
    }
}
