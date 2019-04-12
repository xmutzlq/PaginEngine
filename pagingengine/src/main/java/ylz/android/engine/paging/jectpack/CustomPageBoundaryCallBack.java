package ylz.android.engine.paging.jectpack;

import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import ylz.android.engine.paging.util.LogUtils;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
public class CustomPageBoundaryCallBack extends PagedList.BoundaryCallback {
    @Override
    public void onZeroItemsLoaded() {
        LogUtils.eTag("zlq", "onZeroItemsLoaded");
    }

    @Override
    public void onItemAtFrontLoaded(@NonNull Object itemAtFront) {
        LogUtils.eTag("zlq", "onItemAtFrontLoaded");
    }

    @Override
    public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
        LogUtils.eTag("zlq", "itemAtEnd");
    }
}
