package ylz.android.engine.paging.main;

import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.Map;

import ylz.android.engine.paging.CustomPaging;
import ylz.android.engine.paging.ILoader;
import ylz.android.engine.paging.jectpack.BasePageListAdapter;
import ylz.android.engine.paging.main.adapter.FuliAdapter;
import ylz.android.engine.paging.main.model.GankModel;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/15<p>
 * <p>Description：<p>
 */
public class GankPaging extends CustomPaging<GankModel> {

    private ILoader loader;

    public GankPaging(ILoader loader) {
        this.loader = loader;
    }

    @Override
    protected void preRecyclerView() {
        StaggeredGridLayoutManager staggeredGridLayoutManager =
                new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
    }

    @Override
    public BasePageListAdapter getAdapter() {
        return new FuliAdapter(R.layout.item_fuli);
    }

    @Override
    public Map<String, Object> getLoadApi() {
        return null;
    }

    @Override
    public ILoader getALoader() {
        return loader;
    }
}
