package ylz.android.engine.paging.jectpack;

import java.util.List;
import java.util.Map;

import ylz.android.engine.paging.ILoader;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
public class CustomLoader {
    public static final int STATE_COMPLETE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_END = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_ERROR = 4;

    //核心
    ILoader loader;
    ILoader.IUpdateUI updateUI;
    CustomLoader.INotifyUIState notifyUIState;

    public ILoader.IDataResult dataResult;

    private int page;
    private int pageSize;

    private boolean isRefresh;                  //是否下拉刷新
    private boolean isDownRefresh;              //是否下拉刷新
    private boolean isFull;                     //是否全部加载完
    private boolean isError;                    //是否加载错误
    private boolean isEmpty;                    //是否空数据
    private boolean isNeedComplete = true;      //是否需要回调Complete状态，修复网络加载生命周期异常问题

    private Map<String, Object> params;

    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setDataSource(ILoader loader) {
        this.loader = loader;
    }

    public void setUpdateUI(ILoader.IUpdateUI updateUI) {
        this.updateUI = updateUI;
    }

    public void setDataResult(ILoader.IDataResult dataResult) {
        this.dataResult = dataResult;
    }

    public void setUpdateUIState(CustomLoader.INotifyUIState notifyUIState) {
        this.notifyUIState = notifyUIState;
    }

    public void setLoadApi(Map<String, Object> params) {
        this.params = params;
    }

    public void fetchData(final boolean isPullRefresh, int page, int pageSize) {
        this.page = page;
        this.pageSize = pageSize;
        if(loader == null) return;
        if(isRefresh) return;
        isRefresh = true;
        isDownRefresh = isPullRefresh;
        if(notifyUIState != null) {
            notifyUIState.notifyUIStat(STATE_LOADING);
        }
        loader.onLoadData(params, page, pageSize, new ILoader.ILoadResult() {
            @Override
            public void onLoadResult(List dataList) {
                isRefresh = false;
                isNeedComplete = false;

                //如果无数据并且下拉刷新，那么统一处理成空的状态
                if(isPullRefresh && (dataList == null || dataList.size() == 0)) {
                    isError = false;
                    isEmpty = true;
                    if (notifyUIState != null) {
                        notifyUIState.notifyUIStat(STATE_EMPTY);
                    }
                } else if(dataList == null) { //失败的情况
                    isError = true;
                    isEmpty = false;
                    if(notifyUIState != null) {
                        notifyUIState.notifyUIStat(STATE_ERROR);
                    }
                } else {
                    if(dataList.size() == 0 || dataList.size() < pageSize) {
                        //数据全部加载完毕
                        isError = false;
                        isEmpty = false;
                        isFull = true;
                    } else {
                        isFull = false;
                    }

                    if(updateUI != null) {
                        updateUI.onUpdateUI(dataList);
                    }

                    if(notifyUIState != null) {
                        if(isFull) {
                            notifyUIState.notifyUIStat(STATE_END);
                        } else {
                            notifyUIState.notifyUIStat(STATE_COMPLETE);
                        }
                    }
                }
            }

            @Override
            public void onLoadComplete() { //加载过程快速退出界面到lunch，导致回调无法通知到
                isRefresh = false;
                if(notifyUIState != null && isNeedComplete) {
                    notifyUIState.notifyUIStat(STATE_COMPLETE);
                }
                isNeedComplete = true;
            }
        });
    }

    public boolean isDownRefresh() {
        return isDownRefresh;
    }

    public boolean isError() {
        return isError;
    }

    public void clear() {
        loader = null;
        updateUI = null;
        notifyUIState = null;
    }

    public interface INotifyUIState {
        void notifyUIStat(int state);
    }
}
