package ylz.android.engine.paging;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ylz.android.engine.paging.jectpack.BasePageListAdapter;
import ylz.android.engine.paging.jectpack.CustomLoader;
import ylz.android.engine.paging.jectpack.CustomPageDataSourceFactory;
import ylz.android.engine.paging.util.LogUtils;
import ylz.android.engine.paging.util.ResourceUtil;
import ylz.android.engine.paging.view.CustomLoadMoreView;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
public abstract class CustomPaging<T> implements IRegisterRefresh, IPagingWorker {
    protected WeakReference<AppCompatActivity> weakActivity;

    protected RecyclerView recyclerView;
    protected BasePageListAdapter adapter;

    //核心代码
    protected IRefresh refreshView;
    protected CustomLoader loader;
    protected ILoader aLoader;

    LiveData<PagedList<T>> liveData;
    CustomPageDataSourceFactory factory;
    PagedList.Config config;

    public abstract BasePageListAdapter getAdapter();
    public abstract Map<String, Object> getLoadApi();
    public abstract ILoader getALoader();

    private  Map<String, Object> apiParams;
    public void reSetLoadApi(Map<String, Object> apiParams) {
        if(apiParams == null || apiParams.size() == 0) {
            this.apiParams = getLoadApi();
        } else {
            this.apiParams = apiParams;
        }
    }

    public void stopPaging() {
        if(liveData != null
                && weakActivity != null && weakActivity.get() != null) {
            liveData.removeObservers(weakActivity.get());
        }
    }

    /** 初始化RecyclerView配置(LayoutManager已经在xml文件中配置过) **/
    protected void preRecyclerView(){}
    /** 给有StickyHeader的列表使用 **/
    protected List makeHeaderListData(List dataList, boolean isRefresh){return dataList;}
    /** 空数据 **/
    protected int getEmptyView() {return R.layout.include_no_data;}
    /** 测试使用 **/
    protected boolean isTestModel() {return false;}
    /** Item点击事件 **/
    protected void onItemClickListener(BaseQuickAdapter adapter, View view, int position){}
    /** 默认页数 **/
    protected int getDefaultPageSize() {return 10;}

    @Override
    public void work(AppCompatActivity activity) {
        weakActivity = new WeakReference<>(activity);

        int swipeRefreshId = ResourceUtil.getId(activity, "id_refresh_view");
        int recyclerViewId = ResourceUtil.getId(activity, "id_recycler_view");
        if(swipeRefreshId == 0 || recyclerViewId == 0) {
            LogUtils.eTag("zlq","控件Id命名有误！");
            activity.finish();
        }

        recyclerView = activity.findViewById(recyclerViewId);
        refreshView = activity.findViewById(swipeRefreshId);
        if(refreshView != null) {
            refreshView.registerRefresh(this);
        }

        if(getAdapter() == null) {
            LogUtils.eTag("zlq", "adapter不能为空！");
            activity.finish();
        }

        prepareAdapter();
        preRecyclerView();
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        if(apiParams == null || apiParams.size() == 0) {
            apiParams = getLoadApi();
        }

        aLoader = getALoader();
        loader = new CustomLoader();
        loader.setLoadApi(apiParams);
        loader.setDataSource(aLoader);
        loader.setUpdateUIState(state -> {
            if(Loader.STATE_COMPLETE == state) { //空闲状态，所有控件可刷新
                LogUtils.eTag("zlq", "STATE_COMPLETE");
                adapter.loadMoreComplete();
                refreshView.setRefresh(false);
            } else if(Loader.STATE_LOADING == state) {//加载状态，所有控件不可操作刷新
                LogUtils.eTag("zlq", "STATE_LOADING");
//                refreshView.setRefresh(true);
            } else if(Loader.STATE_END == state) { //结束状态
                LogUtils.eTag("zlq", "STATE_END");
                refreshView.setRefresh(false);
                adapter.loadMoreEnd(false);
            } else if(Loader.STATE_EMPTY == state) { //空数据状态
                LogUtils.eTag("zlq", "STATE_EMPTY");
                adapter.loadMoreComplete();
                refreshView.setRefresh(false);
                showEmptyView();
                showEmptyView(true);
            } else if(Loader.STATE_ERROR == state) { //请求失败状态
                LogUtils.eTag("zlq", "STATE_ERROR");
                refreshView.setRefresh(false);
            }
        });

        loader.setUpdateUI(dataList -> {
            List headerDataList = makeHeaderListData(dataList, loader.isDownRefresh());
            if (loader.isDownRefresh()) {
                showEmptyView(false);
            }
            if(loader.dataResult != null) {
                loader.dataResult.onDataResult(headerDataList);
            }
        });

        config = new PagedList.Config.Builder()
                .setInitialLoadSizeHint(getDefaultPageSize())              //设置首次加载的数量；
                .setPageSize(getDefaultPageSize())                         //设置每一页加载的数量；
                .setEnablePlaceholders(false)                              //配置是否启动PlaceHolders
                .build();
        factory = new CustomPageDataSourceFactory(loader);
        liveData = new LivePagedListBuilder(factory, config).build();
        liveData.observe(activity, adapter::submitList);
    }

    private void prepareAdapter() {
        refreshView.setRefresh(true);
        adapter = getAdapter();
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            onItemClickListener(adapter1, view, position);
        });
        adapter.setOnLoadMoreListener(() -> {}, recyclerView);
        adapter.setLoadMoreView(new CustomLoadMoreView()); // 加载更多的View
    }

    @Override
    public void onRefresh() {
        prepareAdapter();
        preRecyclerView();
        recyclerView.setAdapter(adapter);
        adapter.setEnableLoadMore(true);
        liveData = new LivePagedListBuilder(factory, config).build();
        liveData.observe(weakActivity.get(), adapter::submitList);
    }

    protected void showEmptyView() {
        LogUtils.eTag("zlq", "showEmptyView");
        adapter.setEmptyView(getEmptyView(), (ViewGroup) recyclerView.getParent());
        if(recyclerView.getLayoutParams() != null
                && recyclerView.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) recyclerView.getLayoutParams();
            layoutParams.weight = 1;
            recyclerView.setLayoutParams(layoutParams);
        }
    }

    protected void showEmptyView(boolean isShown) {
        if(adapter != null) {
            adapter.setNewData(new ArrayList());
        }
    }
}
