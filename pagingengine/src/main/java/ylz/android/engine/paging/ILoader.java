package ylz.android.engine.paging;

import java.util.List;
import java.util.Map;

public interface ILoader {
    void onLoadData(Map<String, Object> params,
                    int page, int pageSize, ILoadResult loadResult);

    interface ILoadResult<T> {
        void onLoadResult(List<T> dataList);
        void onLoadComplete();
    }

    interface IUpdateUI<T> {
        void onUpdateUI(List<T> dataList);
    }

    interface IDataResult<T> {
        void onDataResult(List<T> dataList);
    }
}
