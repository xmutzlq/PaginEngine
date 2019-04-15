package ylz.android.engine.paging.main.adapter;

import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseViewHolder;

import ylz.android.engine.paging.jectpack.BasePageListAdapter;
import ylz.android.engine.paging.main.R;
import ylz.android.engine.paging.main.model.GankModel;
import ylz.android.engine.paging.main.utils.GlideUtils;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/15<p>
 * <p>Description：<p>
 */
public class FuliAdapter extends BasePageListAdapter<GankModel, BaseViewHolder> {

    public FuliAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    public boolean areItemsTheSame(@NonNull GankModel oldItem, @NonNull GankModel newItem) {
        return oldItem._id.equals(newItem._id);
    }

    @Override
    public boolean areContentsTheSame(@NonNull GankModel oldItem, @NonNull GankModel newItem) {
        return oldItem == newItem;
    }

    @Override
    protected void convert(BaseViewHolder helper, GankModel item) {
        GlideUtils.getInstance().load(helper.getView(R.id.pic), item.url, false);
    }
}
