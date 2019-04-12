package ylz.android.engine.paging.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import ylz.android.engine.paging.util.DimensionsUtil;
import ylz.android.engine.paging.IRefresh;
import ylz.android.engine.paging.IRegisterRefresh;

public class YlzRefreshView extends SwipeRefreshLayout implements IRefresh {

    public YlzRefreshView(@NonNull Context context) {
        super(context);
    }

    public YlzRefreshView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void registerRefresh(IRegisterRefresh registerRefresh) {
        setOnRefreshListener(() -> {
            if(registerRefresh != null) {
                registerRefresh.onRefresh();
            }
        });
    }

    @Override
    public void initRefreshView() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)getLayoutParams();
        layoutParams.setMargins(DimensionsUtil.dip2px(getContext(),10),
                DimensionsUtil.dip2px(getContext(),10),
                DimensionsUtil.dip2px(getContext(),10),
                DimensionsUtil.dip2px(getContext(),10));
    }

    @Override
    public boolean isOnRefreshState() {
        return isRefreshing();
    }

    @Override
    public void setRefresh(boolean refresh) {
        setRefreshing(refresh);
    }

    @Override
    public void setEnable(boolean enable) {
        setEnabled(enable);
    }
}
