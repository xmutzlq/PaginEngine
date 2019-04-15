package ylz.android.engine.paging.main.helper;

import android.view.KeyEvent;

import com.sherlockshi.toast.ToastUtils;

import ylz.android.engine.paging.main.R;


public class MainHelper {

    //声明一个long类型变量：用于存放上一点击“返回键”的时刻
    private long mExitTime;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                ToastUtils.showHint(R.string.press_again_to_exit);
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序
                return false;
            }
            return true;
        }
        return false;
    }
}
