package ylz.android.engine.paging;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ylz.android.engine.paging.util.LogUtils;
import ylz.android.engine.paging.util.ResourceUtil;
import ylz.android.engine.paging.view.CustomLoadMoreView;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
public abstract class APaging implements IRegisterRefresh, IPagingWorker {
    public RecyclerView recyclerView;
    public BaseQuickAdapter adapter;

    //核心代码
    protected IRefresh refreshView;
    protected Loader loader;
    protected ILoader aLoader;

    public abstract BaseQuickAdapter getAdapter();
    public abstract Map<String, Object> getLoadApi();
    public abstract ILoader getALoader();

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

    @Override
    public void work(AppCompatActivity activity) {

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

        adapter = getAdapter();
        adapter.setOnItemClickListener((adapter1, view, position) -> {
            onItemClickListener(adapter1, view, position);
        });
        adapter.setOnLoadMoreListener(() -> {
            if(!loader.isFull() && !isTestModel() && !loader.isError() && !loader.isEmpty()) {
                LogUtils.eTag("zlq", "OnLoadMore");
                loader.fetchData(false);
            }
        }, recyclerView);
        adapter.disableLoadMoreIfNotFullPage();
        adapter.setLoadMoreView(new CustomLoadMoreView()); // 加载更多的View

        preRecyclerView();
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        aLoader = getALoader();
        loader = new Loader();
        loader.setLoadApi(getLoadApi());
        loader.setDataSource(aLoader);
        loader.setUpdateUIState(state -> {
            if(Loader.STATE_COMPLETE == state) { //空闲状态，所有控件可刷新
                LogUtils.eTag("zlq", "STATE_COMPLETE");
                adapter.loadMoreComplete();
                refreshView.setRefresh(false);
            } else if(Loader.STATE_LOADING == state) {//加载状态，所有控件不可操作刷新
                LogUtils.eTag("zlq", "STATE_LOADING");
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
                if(adapter != null) adapter.setNewData(headerDataList);
            } else {
                if(adapter != null) adapter.addData(headerDataList);
            }
        });

        if(!isTestModel()) {
            refreshView.setRefresh(true);
            loader.fetchData(true);
        }
    }

    @Override
    public void onRefresh() {
        adapter.setEnableLoadMore(true);
        if(!isTestModel()) {
            LogUtils.eTag("zlq", "onRefresh");
            loader.setLoadApi(getLoadApi());
            refreshView.setRefresh(true);
            loader.fetchData(true);
        } else {
            refreshView.setRefresh(false);
        }
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
