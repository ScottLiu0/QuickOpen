package cn.imliu.quickopen.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Toast的公共方法
 * 独立出来，后续如果修改样式也比较方便
 */
public class ToastUtils {

    /**
     * 主要方法
     * @param context
     * @param text
     * @param duration
     */
    public static void show(Context context, CharSequence text, int duration) {
        if (duration != Toast.LENGTH_LONG) {
            duration = Toast.LENGTH_SHORT;
        }
        Toast.makeText(context, text, duration).show();
    }

    public static void show(CharSequence text, int duration) {
        show(AppContext.getInstance(),text,duration);
    }

    //----------------------------------------------------//


    /*public static void show(Context context, int resId) {
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration) {
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(Context context, CharSequence text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String format, Object... args) {
        show(context, String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, int resId, int duration, Object... args) {
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(Context context, String format, int duration, Object... args) {
        show(context, String.format(format, args), duration);
    }*/

    public static void show(int resId) {
        final Context context = AppContext.getInstance();
        show(context, context.getResources().getText(resId), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration) {
        final Context context = AppContext.getInstance();
        show(context, context.getResources().getText(resId), duration);
    }

    public static void show(CharSequence text) {
        show(text, Toast.LENGTH_SHORT);
    }

    public static void show(int resId, Object... args) {
        final Context context = AppContext.getInstance();
        show(context, String.format(context.getResources().getString(resId), args), Toast.LENGTH_SHORT);
    }

    public static void show(String format, Object... args) {
        show(String.format(format, args), Toast.LENGTH_SHORT);
    }

    public static void show(int resId, int duration, Object... args) {
        final Context context = AppContext.getInstance();
        show(context, String.format(context.getResources().getString(resId), args), duration);
    }

    public static void show(String format, int duration, Object... args) {
        show(String.format(format, args), duration);
    }

}
