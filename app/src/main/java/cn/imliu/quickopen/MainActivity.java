package cn.imliu.quickopen;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import cn.imliu.quickopen.service.SystemOverlayService;
import cn.imliu.quickopen.util.Code;
import cn.imliu.quickopen.util.Logger;
import cn.imliu.quickopen.util.PreferencesUtils;
import cn.imliu.quickopen.util.SysUtil;
import cn.imliu.quickopen.util.ToastUtils;

public class MainActivity extends Activity {
	private final static Logger LOGGER = Logger.getLogger(MainActivity.class);

	private final static String KEY_OPEN = "open_action";
	private final static String KEY_PERMISSION = "open_permission";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		PrefFragment fragment = new PrefFragment( );
		getFragmentManager().beginTransaction()
				.replace(R.id.activity_main,fragment)
				.commit();
		if(SysUtil.checkOverlays(this, Code.REQUEST_OVERLAY)){
			this.start();
		}else{
			ToastUtils.show("需要开启其他应用的显示权限");
		}
	}

	public void click(View view){
		Class<? extends Service> clz = SystemOverlayService.class;
		if(SysUtil.isServiceRunning(this,clz)){
			SysUtil.stopService(this,clz);
		}else{
			SysUtil.startService(this,clz);
		}
	}
	public void click2(View view){
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		Point point = new Point(); wm.getDefaultDisplay().getSize(point);
		// 以屏幕左上角为原点，设置x、y初始值
		PreferencesUtils.putInt("x",0);
		PreferencesUtils.putInt("y",point.y/2);
		SysUtil.checkAndStopService(this, SystemOverlayService.class);
		this.start();
	}

	private void start(){
		SysUtil.checkAndStartService(this, SystemOverlayService.class);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if( requestCode == Code.REQUEST_OVERLAY){
			if(SysUtil.checkOverlays(this, null)){
				this.start();
			}else{
				ToastUtils.show("依然未开启其他应用的显示权限");
			}
		}
	}

	public static class PrefFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_main);
			PreferenceManager manager = this.getPreferenceManager();
			Preference pref = manager.findPreference(KEY_OPEN);
			pref.setOnPreferenceChangeListener( this );
			SharedPreferences prefs = manager.getSharedPreferences();
			String lp = prefs.getString(KEY_OPEN,"1");
			String title = this.getTitle(lp);
			if( title != null ){
				pref.setSummary(title);
			}
			if( !SysUtil.afterVer(23) ){//低于6.0的话不需要配置权限
				//参考：http://blog.csdn.net/wh_19910525/article/details/8294729
				try{
					getPreferenceScreen().removePreference(findPreference(KEY_PERMISSION));//这是 删除整个 一级节点
				}catch (Exception e){
					LOGGER.error(e);
				}
			}
			pref.setDefaultValue(lp);
		}
		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			preference.setSummary(this.getTitle(String.valueOf(newValue)));
			return false;
		}
		private String getTitle(String value){
			String[] codes = getResources().getStringArray(R.array.open_item_code);
			if(TextUtils.isEmpty(value) || codes.length == 0){
				return null;
			}
			String[] texts = getResources().getStringArray(R.array.open_itsm_title);
			int len = codes.length;
			if( texts.length != len ){
				return value;
			}
			for( int i = 0; i < len; i++){
				if( codes[i] .equals(value)){
					return texts[i];
				}
			}
			return codes[0];
		}

	}


}
