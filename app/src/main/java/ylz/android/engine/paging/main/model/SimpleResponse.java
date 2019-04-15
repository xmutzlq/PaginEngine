package ylz.android.engine.paging.main.model;

import java.io.Serializable;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/15<p>
 * <p>Description：<p>
 */
public class SimpleResponse implements Serializable {

    private static final long serialVersionUID = -1477609349345966116L;

    public boolean error;

    public GankResponse toLzyResponse() {
        GankResponse lzyResponse = new GankResponse();
        lzyResponse.error = error;
        return lzyResponse;
    }
}
