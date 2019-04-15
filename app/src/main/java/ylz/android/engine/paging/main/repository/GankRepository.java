package ylz.android.engine.paging.main.repository;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.Map;

import ylz.android.engine.paging.ILoader;
import ylz.android.engine.paging.main.AppConstant;
import ylz.android.engine.paging.main.model.GankResponseEntity;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/14<p>
 * <p>Description：<p>
 */
public class GankRepository implements ILoader {

    @Override
    public void onLoadData(Map<String, Object> params, int page, int pageSize, ILoadResult loadResult) {

    String url = AppConstant.API_URL + "/" + pageSize + "/" + page;
    OkGo.<String>get(url).tag(this).execute(new StringCallback() {
            @Override
            public void onSuccess(Response<String> response) {
                Gson gson = new Gson();
                GankResponseEntity gankModels = gson.fromJson(response.body(), GankResponseEntity.class);
                loadResult.onLoadResult(gankModels.results);
            }
        });
    }
}
