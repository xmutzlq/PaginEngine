package ylz.android.engine.paging.main.model;

import java.io.Serializable;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/15<p>
 * <p>Description：<p>
 */
public class GankResponse <T> implements Serializable {

    private static final long serialVersionUID = 5213230387175987834L;

    public boolean error;
    public T results;
}
