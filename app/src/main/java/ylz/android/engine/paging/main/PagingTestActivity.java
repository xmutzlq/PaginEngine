package ylz.android.engine.paging.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ylz.android.engine.paging.CustomPaging;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
public class PagingTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_info);

        CustomRepository customRepository = new CustomRepository();
        CustomPaging customPaging = new SimplePaging(customRepository);

        customPaging.work(this);
    }
}
