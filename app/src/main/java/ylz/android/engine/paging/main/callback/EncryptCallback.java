package ylz.android.engine.paging.main.callback;

import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.base.Request;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import ylz.android.engine.paging.main.utils.MD5Utils;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/15<p>
 * <p>Description：<p>
 */
public abstract class EncryptCallback<T> extends JsonCallback<T> {

    private static final Random RANDOM = new Random();
    private static final String CHARS = "0123456789abcdefghijklmnopqrstuvwxyz";

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
        //以下是示例加密代码，根据自己的业务需求和服务器的配合，算法自行决定，这里只是demo，不能用于商业项目
        sign(request.getParams());
    }

    /**
     * 针对URL进行签名，关于这几个参数的作用，详细请看
     * http://www.cnblogs.com/bestzrz/archive/2011/09/03/2164620.html
     */
    private void sign(HttpParams params) {
        params.put("nonce", getRndStr(6 + RANDOM.nextInt(8)));
        params.put("timestamp", "" + (System.currentTimeMillis() / 1000L));
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : params.urlParamsMap.entrySet()) {
            map.put(entry.getKey(), entry.getValue().get(0));
        }
        for (Map.Entry<String, String> entry : getSortedMapByKey(map).entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.delete(sb.length() - 1, sb.length());
        String sign = MD5Utils.encode(sb.toString());
        params.put("sign", sign);
    }

    /** 获取随机数 */
    private String getRndStr(int length) {
        StringBuilder sb = new StringBuilder();
        char ch;
        for (int i = 0; i < length; i++) {
            ch = CHARS.charAt(RANDOM.nextInt(CHARS.length()));
            sb.append(ch);
        }
        return sb.toString();
    }

    /** 按照key的自然顺序进行排序，并返回 */
    private Map<String, String> getSortedMapByKey(Map<String, String> map) {
        Comparator<String> comparator = new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);
            }
        };
        Map<String, String> treeMap = new TreeMap<>(comparator);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        return treeMap;
    }
}
