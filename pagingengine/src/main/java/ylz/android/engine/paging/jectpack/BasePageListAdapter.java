package ylz.android.engine.paging.jectpack;

import android.arch.paging.AsyncPagedListDiffer;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/10<p>
 * <p>Description：<p>
 */
public abstract class BasePageListAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {

    public AsyncPagedListDiffer<T> mDiffer;

    public abstract boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem);

    public abstract boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem);

    public BasePageListAdapter(int layoutResId) {
        super(layoutResId);
        mDiffer = new AsyncPagedListDiffer<>(this, mDiffCallback);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                mData.clear();
                mData.addAll(mDiffer.getCurrentList().subList(0, mDiffer.getCurrentList().size()));
            }
        });
    }

    public void submitList(PagedList<T> pagedList) {
        mDiffer.submitList(pagedList);
    }

    @Nullable
    @Override
    public T getItem(int position) {
        mDiffer.getItem(position);
        return super.getItem(position);
    }

    private DiffUtil.ItemCallback<T> mDiffCallback = new DiffUtil.ItemCallback<T>() {
        @Override
        public boolean areItemsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return areItemsTheSame(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull T oldItem, @NonNull T newItem) {
            return areContentsTheSame(oldItem, newItem);
        }
    };
}
