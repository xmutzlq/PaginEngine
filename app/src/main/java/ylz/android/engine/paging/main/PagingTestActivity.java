package ylz.android.engine.paging.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.bumptech.glide.Glide;

import ylz.android.engine.paging.CustomPaging;
import ylz.android.engine.paging.main.helper.MainHelper;
import ylz.android.engine.paging.main.repository.GankRepository;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/11<p>
 * <p>Description：<p>
 */
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
