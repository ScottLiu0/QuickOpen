package cn.imliu.quickopen.util;

import android.app.Application;

/**
 * Application对象是全局的，单例的
 * Created by gs601 on 2017-02-03.
 */

public class AppContext extends Application {
    private static AppContext instance;

    /**
     * 获取Application的单例对象
     */
    public static AppContext getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //在application开始创建的时候，把该对象保存到静态变量中
        instance = this;
        instance.init();
    }

    private void init(){

    }

}
