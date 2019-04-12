package ylz.android.engine.paging;

public interface IRefresh {
    void initRefreshView();
    void registerRefresh(IRegisterRefresh registerRefresh);
    boolean isOnRefreshState();
    void setRefresh(boolean refresh);
    void setEnable(boolean enable);
}
