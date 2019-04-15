package ylz.android.engine.paging.main;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;

import java.util.Map;

import ylz.android.engine.paging.CustomPaging;
import ylz.android.engine.paging.ILoader;
import ylz.android.engine.paging.jectpack.BasePageListAdapter;
import ylz.android.engine.paging.main.model.StudentModel;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/12<p>
 * <p>Description：<p>
 */
public class SimplePaging extends CustomPaging<StudentModel> {

    private ILoader loader;

    public SimplePaging(ILoader loader) {
        this.loader = loader;
    }

    @Override
    public BasePageListAdapter getAdapter() {
        return new MyAdapter(android.R.layout.simple_list_item_2);
    }

    @Override
    public Map<String, Object> getLoadApi() {
        return null;
    }

    @Override
    public ILoader getALoader() {
        return loader;
    }

    private static class MyAdapter extends BasePageListAdapter<StudentModel, BaseViewHolder> {

        @Override
        public boolean areItemsTheSame(@NonNull StudentModel oldItem, @NonNull StudentModel newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull StudentModel oldItem, @NonNull StudentModel newItem) {
            return oldItem == newItem;
        }

        public MyAdapter(int layoutResId) {
            super(layoutResId);
        }

        @Override
        protected void convert(BaseViewHolder helper, StudentModel item) {
            helper.setText(android.R.id.text1, String.valueOf(item.getId()));
            helper.setText(android.R.id.text2, String.valueOf(item.getName()));

            helper.setTextColor(android.R.id.text1, Color.RED);
            helper.setTextColor(android.R.id.text2, Color.BLUE);
        }
    }
}
