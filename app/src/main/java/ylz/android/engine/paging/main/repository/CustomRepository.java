package ylz.android.engine.paging.main.repository;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ylz.android.engine.paging.ILoader;
import ylz.android.engine.paging.main.model.StudentModel;
import ylz.android.engine.paging.util.LogUtils;

/**
 * <p>Author：     zenglq<p>
 * <p>Email：      380233376@qq.com<p>
 * <p>Date：       2019/4/12<p>
 * <p>Description：<p>
 */
public class CustomRepository implements ILoader {

    private Handler handler = new Handler(Looper.getMainLooper());

    List<StudentModel> studentModels = new ArrayList<>();

    public CustomRepository() {
        for (int i = 0; i < 50; i++) {
            StudentModel studentModel = new StudentModel();
            studentModel.setId(i);
            studentModel.setName("name" + i);
            studentModels.add(studentModel);
        }
    }

    List<StudentModel> getStudentsInit(@IntRange(from = 0) int initSize) {
        return studentModels.subList(0, initSize);//包含~不包含
    }

    List<StudentModel> getStudentsByRange(@IntRange(from = 0) int from, int to) {
        return studentModels.subList(from, to);
    }

    List<StudentModel> getStudentsByPage(@IntRange(from = 0) int page, int size) {
        int totalPage = 0;
        if (studentModels.size() % size == 0) {
            totalPage = studentModels.size() / size;
        } else {
            totalPage = studentModels.size() / size + 1;
        }
        if (page >= totalPage || page < 0) {
            return new ArrayList<>();
        }
        if (page == totalPage - 1) {
            return studentModels.subList(page * size, studentModels.size());
        }
        return studentModels.subList(page * size, (page + 1) * size);
    }

    @Override
    public void onLoadData(Map<String, Object> params, int page, int pageSize, ILoadResult loadResult) {
        new Thread(()->{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(()->{
                LogUtils.eTag("zlq", "page = " + page + ", pageSize = " + pageSize);
                List<StudentModel> students = getStudentsByPage(page, pageSize);
                loadResult.onLoadResult(students);
            });
        }).start();
    }
}
