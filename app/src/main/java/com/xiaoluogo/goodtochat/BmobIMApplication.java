package com.xiaoluogo.goodtochat;

import android.app.Activity;
import android.app.Application;

import com.xiaoluogo.goodtochat.receiver.BmobMessageHandler;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import cn.bmob.newim.BmobIM;

/**
 * Created by xiaoluogo on 2017/7/21.
 * Email: angel-lwl@126.com
 */
public class BmobIMApplication extends Application {

    private static BmobIMApplication INSTANCE;

    public static BmobIMApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }

    private static Map<String, Activity> destoryMap = new HashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        LitePal.initialize(this);//初始化数据库
        //TODO 7、初始化IM SDK，只有主进程运行的时候才需要初始化
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            BmobIM.init(this);
            //TODO 6.3、注册消息接收器
            BmobIM.registerDefaultMessageHandler(new BmobMessageHandler(this));
        }

    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 添加到销毁队列
     *
     * @param activity 要销毁的activity
     */
    public static void addDestoryActivity(Activity activity, String activityName) {
        destoryMap.put(activityName, activity);
    }

    /**
     * 销毁指定Activity
     */
    public static void destoryActivity(String activityName) {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            if (activityName == key) {
                destoryMap.get(key).finish();
            }
        }
    }

    public static void destoryAllActivity() {
        Set<String> keySet = destoryMap.keySet();
        for (String key : keySet) {
            destoryMap.get(key).finish();
        }
    }
}