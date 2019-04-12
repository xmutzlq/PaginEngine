package ylz.android.engine.paging.jectpack;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
public class CustomPageDataSource<T> extends PageKeyedDataSource<Integer, T> {

    private CustomLoader customLoader;

    CustomPageDataSource(CustomLoader customLoader) {
        this.customLoader = customLoader;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, T> callback) {
        customLoader.setDataResult(dataList -> {
            if(dataList != null && dataList.size() > 0) {
                callback.onResult(dataList, null, 1);
            }
        });
        customLoader.fetchData(true, 1, params.requestedLoadSize);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, T> callback) { }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, T> callback) {
        customLoader.setDataResult(dataList -> {
            if(dataList != null && dataList.size() > 0) {
                callback.onResult(dataList, params.key + 1);
            }
        });
        customLoader.fetchData(false, params.key + 1, params.requestedLoadSize);
    }
}
