package ylz.android.engine.paging;

import java.util.List;
import java.util.Map;

public class Loader {

    public static final int STATE_COMPLETE = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_END = 2;
    public static final int STATE_EMPTY = 3;
    public static final int STATE_ERROR = 4;

    //核心
    ILoader loader;
    ILoader.IUpdateUI updateUI;
    INotifyUIState notifyUIState;

    public static final int PAGE = 1;           //第一页
    public static final int PAGE_SIZE = 10;     //每页条数
    private int     page = PAGE;                //当前页
    private boolean isRefresh;                  //是否下拉刷新
    private boolean isDownRefresh;              //是否下拉刷新
    private boolean isFull;                     //是否全部加载完
    private boolean isError;                    //是否加载错误
    private boolean isEmpty;                    //是否空数据
    private boolean isNeedComplete = true;      //是否需要回调Complete状态，修复网络加载生命周期异常问题

    private Map<String, Object> params;

    public void setDataSource(ILoader loader) {
        this.loader = loader;
    }

    public void setUpdateUI(ILoader.IUpdateUI updateUI) {
        this.updateUI = updateUI;
    }

    public void setUpdateUIState(INotifyUIState notifyUIState) {
        this.notifyUIState = notifyUIState;
    }

    public void setLoadApi(Map<String, Object> params) {
        this.params = params;
    }

    public void fetchData(boolean refresh) {
        if(loader == null) return;
        if(isRefresh) return;
        isRefresh = true;
        isDownRefresh = refresh;
        if (refresh) {
            isFull = false;
            page = PAGE;
        } else {
            page++;
        }
        if(notifyUIState != null) {
            notifyUIState.notifyUIStat(STATE_LOADING);
        }
        loader.onLoadData(params, page, PAGE_SIZE, new ILoader.ILoadResult() {
            @Override
            public void onLoadResult(List dataList) {
                isRefresh = false;
                isNeedComplete = false;

                if(dataList == null) { //失败的情况
                    isError = true;
                    if(page > 1) page--;
                    if(notifyUIState != null) {
                        notifyUIState.notifyUIStat(STATE_ERROR);
                    }
                    return;
                } else {
                    isError = false;
                    //空数据
                    if(isDownRefresh && dataList.size() == 0) {
                        isEmpty = true;
                        if(notifyUIState != null) {
                            notifyUIState.notifyUIStat(STATE_EMPTY);
                        }
                        //空数据的时候并没有将dataList带给adapter，需注意！！！
                        return;
                    }

                    if(dataList.size() < PAGE_SIZE
                            || dataList.size() == 0) {
                        isFull = true;
                    }
                    if(updateUI != null) {
                        updateUI.onUpdateUI(dataList);
                    }
                }
                if(notifyUIState != null) {
                    if(isFull) {
                        notifyUIState.notifyUIStat(STATE_END);
                    } else {
                        notifyUIState.notifyUIStat(STATE_COMPLETE);
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

    public boolean getRefresh() {
        return isRefresh;
    }

    public boolean isDownRefresh() {
        return isDownRefresh;
    }

    public boolean isFull() {
        return isFull;
    }

    public boolean isEmpty() {
        return isEmpty;
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
