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

``` java 
// xml中配置include_paging
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <include layout="@layout/include_paging"/>
</FrameLayout>
```

``` java 
// 最后在Activity中调用work方法
public class PagingTestActivity extends AppCompatActivity {

    public static void openPagingTestActivity(Context context) {
        Intent intent = new Intent(context, PagingTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_info);

        GankRepository customRepository = new GankRepository();
        GankPaging gankPaging = new GankPaging(customRepository);
        gankPaging.work(this);
    }
}
```

## License

```
Copyright 2019 xmutzlq

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
