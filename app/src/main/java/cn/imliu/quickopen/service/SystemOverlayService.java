package cn.imliu.quickopen.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import cn.imliu.quickopen.R;
import cn.imliu.quickopen.util.ToastUtils;


//http://blog.csdn.net/zhangphil/article/details/50271303

public class SystemOverlayService extends Service {
    /** 悬浮按钮*/
    //private FloatingActionButton rightTopBtn;
	private ImageView fabIconNew;

	private static WindowManager wm;

	//服务
    private boolean serviceWillBeDismissed = false;
	private WindowManager.LayoutParams wmParams=new WindowManager.LayoutParams();

    private final IBinder mBinder = new LocalBinder();
    public SystemOverlayService() {
    }

    @Override
    public void onCreate() {
		super.onCreate();
		//ImageView fabIconNew = new ImageView(this);
		fabIconNew = new ImageView(this);
		fabIconNew.setImageResource(R.drawable.keyboard_32px_1202984);
		fabIconNew.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				//ToastUtils.show("点击悬浮图标");
				popInput();
			}
		});
		fabIconNew.setPadding(0,0,0,0);
		wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window type
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
        * 下面的flags属性的效果形同“锁定”。 悬浮窗的内容不可触摸，不接受任何事件；注：同时不影响后面的事件响应。
        */
		/*wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;*/
		wmParams.gravity = Gravity.LEFT | Gravity.BOTTOM; // 调整悬浮窗口至右侧中间
		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = 0;
		wmParams.y = 0;
		//wmParams.width = 0; wmParams.height = 0;
		// 设置悬浮窗口长宽数据
		wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		if (fabIconNew.getParent() == null){//如果view没有被加入到某个父组件中，则加入WindowManager中
			wm.addView(fabIconNew, wmParams);
			ToastUtils.show("悬浮窗已创建");
		}
    }

    @Override
    public IBinder onBind(Intent intent) {
        //throw new UnsupportedOperationException("Not yet implemented");
        return mBinder;
    }


    public class LocalBinder extends Binder {
        SystemOverlayService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SystemOverlayService.this;
        }
    }

	@Override
	public void onDestroy() {
		super.onDestroy();
		if( wm != null ){
			if( fabIconNew != null ){
				wm.removeView(fabIconNew);
			}
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}

	/**
	 * 弹出系统输入法选择
	 */
	//http://www.cnblogs.com/weixing/p/3300908.html
	private void popInput(){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showInputMethodPicker();
	}
}
