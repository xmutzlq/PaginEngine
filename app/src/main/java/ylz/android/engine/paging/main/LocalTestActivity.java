package ylz.android.engine.paging.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ylz.android.engine.paging.CustomPaging;
import ylz.android.engine.paging.main.repository.CustomRepository;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/15<p>
 * <p>Description：<p>
 */
public class LocalTestActivity extends AppCompatActivity {

    public static void openLocalTestActivity(Context context) {
        Intent intent = new Intent(context, LocalTestActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_info);

        CustomRepository customRepository = new CustomRepository();
        CustomPaging customPaging = new SimplePaging(customRepository);
        customPaging.work(this);
    }
}
