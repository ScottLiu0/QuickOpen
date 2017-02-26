package cn.imliu.quickopen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import cn.imliu.quickopen.service.SystemOverlayService;
import cn.imliu.quickopen.util.Code;
import cn.imliu.quickopen.util.PreferencesUtils;
import cn.imliu.quickopen.util.SysUtil;
import cn.imliu.quickopen.util.ToastUtils;

public class MainActivity extends AppCompatActivity implements Preference.OnPreferenceChangeListener{

	private final static String KEY_OPEN = "open_action";
	private PrefFragment fragment = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		fragment = new PrefFragment(this);
		getFragmentManager().beginTransaction()
				.replace(R.id.activity_main,fragment)
				.commit();
		if(SysUtil.checkOverlays(this, Code.REQUEST_OVERLAY)){
			this.start();
		}else{
			ToastUtils.show("需要开启其他应用的显示权限");
		}
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

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		preference.setSummary(fragment.getTitle(String.valueOf(newValue)));
		return false;
	}

	public static class PrefFragment extends PreferenceFragment {
		private Preference.OnPreferenceChangeListener preferenceChangeListener;
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pref_main);
			if( preferenceChangeListener != null ){
				PreferenceManager manager = this.getPreferenceManager();
				Preference pref = manager.findPreference(KEY_OPEN);
				pref.setOnPreferenceChangeListener(preferenceChangeListener);
				SharedPreferences prefs = manager.getSharedPreferences();
				String lp = prefs.getString(KEY_OPEN,null);
				String title = this.getTitle(lp);
				if( title != null ){
					pref.setSummary(title);
				}
			}
		}
		public PrefFragment(){
		}

		public PrefFragment(Preference.OnPreferenceChangeListener pref){
			this.preferenceChangeListener = pref;
		}
		private String getTitle(String value){
			String[] codes = getResources().getStringArray(R.array.open_item_code);
			if(TextUtils.isEmpty(value) || codes == null || codes.length == 0){
				return null;
			}
			String[] texts = getResources().getStringArray(R.array.open_itsm_title);
			int len = codes.length;
			if( texts== null || texts.length != len ){
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
