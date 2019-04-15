# PaginEngine  [![](https://jitpack.io/v/xmutzlq/PaginEngine.svg)](https://jitpack.io/#xmutzlq/PaginEngine)
下拉刷新、加载更多框架；3行代码搞定，就是这么简单
## Gradle

[![](https://jitpack.io/v/xmutzlq/PaginEngine.svg)](https://jitpack.io/#xmutzlq/PaginEngine)

``` groovy
repositories { 
    maven { url "https://jitpack.io" }
} 

dependencies {
    implementation 'com.github.xmutzlq:PaginEngine:1.0.0'
}
```
## Usage
  

**基本用法**

主要由三部分结构组成: ILoader(网络请求接口类), CustomPaging(基础分页类), include_paging(xml配置)

``` java
// 实现ILoader网络请求接口类，并将数据结果抛给ILoadResult
class GankRepository implement ILoader {
    @Override
    public void onLoadData(Map<String, Object> params, int page, int pageSize, ILoadResult loadResult) {
    // 以下是使用Gank.io请求的数据
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
``` 

``` java
// 继承CustomPaging基础分页类，并实现基础抽象方法
public class GankPaging extends CustomPaging<GankModel> {

    private ILoader loader;

    public GankPaging(ILoader loader) {
        this.loader = loader;
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
```
