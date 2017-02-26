package cn.imliu.quickopen.util;

import android.util.Log;

/**
 * Created by gs601 on 2017-02-02.
 */

public class Logger {
    private String clzName;
    public static Logger getLogger( Class<?> clzName ){
        Logger log = new Logger();
        log.clzName = clzName.getName();
        return log;
    }

    public void error(String msg){
        Log.e(clzName,msg);
    }

    public void error(Exception e){
        Log.e(clzName,e.getMessage());
    }

}
