package cn.imliu.quickopen.util;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.List;

/**
 * Created by scott on 2017/2/26.
 */

public class SysUtil {
	//原因是RequestCode不能为负值,也不能大于16位bit值65536
	public final static int CODE_REQUEST_OVERLAYS = 65535;
	public final static int CODE_WRITE_CALENDAR = 65534;

	/**
	 * 检查服务是否还在运行
	 * @param serviceClass
	 * @param context
	 * @return
	 */
	public static boolean isServiceRunning(Context context,Class<? extends Service> serviceClass) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager
				.getRunningServices(Integer.MAX_VALUE);
		if (serviceList == null || serviceList.size() == 0)
			return false;
		for (ActivityManager.RunningServiceInfo info : serviceList) {
			if (info.service.getClassName().equals(serviceClass.getName()))
				return true;
		}
		return false;
	}

	public static void checkAndStartService(Context context,Class<? extends Service> serviceClass){
		if( !isServiceRunning(context,serviceClass)){
			startService(context,serviceClass);
		}
	}
	public static void checkAndStopService(Context context,Class<? extends Service> serviceClass){
		if( isServiceRunning(context,serviceClass)){
			stopService(context,serviceClass);
		}
	}
	/**
	 * 启动服务
	 * @param context
	 * @param serviceClass
	 */
	public static void startService(Context context,Class<? extends Service> serviceClass){
		Intent intent = new Intent(context,	serviceClass);
		context.startService(intent);
	}
	/**
	 * 停止服务
	 * @param context
	 * @param serviceClass
	 */
	public static void stopService(Context context,Class<? extends Service> serviceClass){
		Intent intent = new Intent(context,	serviceClass);
		context.stopService(intent);
	}

	///Manifest.permission.SYSTEM_ALERT_WINDOW
	public static boolean checkPermission(Activity thiaActivity,String permission){
		return checkPermission(thiaActivity,permission,null);
	}


	/**
	 * 检查并申请权限
	 * //权限申请结果
	 * onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
	 * @param thiaActivity
	 * @param permission 具体的权限内容
	 * @param requestCode 申请权限的请求code，为null时不申请
	 * @return
	 */
	public static boolean checkPermission(Activity thiaActivity, String permission, Integer requestCode){
		int perm = ContextCompat.checkSelfPermission(thiaActivity,permission);///判断是否有权限
		boolean result = ( perm == PackageManager.PERMISSION_GRANTED);//true有权限
		if(!result && requestCode != null){
			//请求权限
			ActivityCompat.requestPermissions(thiaActivity,
					new String[]{permission},requestCode);
			//判断是否需要 向用户解释，为什么要申请该权限
			ActivityCompat.shouldShowRequestPermissionRationale(thiaActivity,
					permission);
		}
		return result;
	}


	public static boolean afterVer(Integer ver){
		return (Build.VERSION.SDK_INT >= ver);
	}

	/**
	 * 检查悬浮层
	 * http://blog.csdn.net/chenlove1/article/details/52047105
	 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
	 * @param thiaActivity
	 * @return
	 */
	public static boolean checkOverlays(Activity thiaActivity,Integer requestCode){
		if ( Build.VERSION.SDK_INT >= 23
				&& !Settings.canDrawOverlays(thiaActivity)) {
			if( requestCode != null ){
				Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
						Uri.parse("package:" + thiaActivity.getPackageName()));
				thiaActivity.startActivityForResult(intent,requestCode);
			}
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}
