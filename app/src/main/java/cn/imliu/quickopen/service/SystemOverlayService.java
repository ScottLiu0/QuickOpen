package cn.imliu.quickopen.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import cn.imliu.quickopen.R;
import cn.imliu.quickopen.util.PreferencesUtils;
import cn.imliu.quickopen.util.ToastUtils;


//http://blog.csdn.net/zhangphil/article/details/50271303

public class SystemOverlayService extends Service implements View.OnTouchListener {
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
	    fabIconNew = (ImageView) View.inflate(this,R.layout.floating_main,null);
		//fabIconNew = (ImageView) view.findViewById(R.id.floating_image);//new ImageView(this);
	    //ViewGroup.LayoutParams slaParams = (ViewGroup.LayoutParams)fabIconNew.getLayoutParams();
	    //slaParams.height = 32;slaParams.width = 32;//单位需要转成DP,，而且如果new 的图片，layoutParams为空
		fabIconNew.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				popInput();
			}
		});
	    fabIconNew.setOnTouchListener(this);
		wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		wmParams.type = WindowManager.LayoutParams.TYPE_PHONE; // 设置window type
		wmParams.format = PixelFormat.RGBA_8888; // 设置图片格式，效果为背景透明

		wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		/*
        * 下面的flags属性的效果形同“锁定”。 悬浮窗的内容不可触摸，不接受任何事件；注：同时不影响后面的事件响应。
        */
		/*wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;*/
		wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至右侧中间
	    Point point = new Point(); wm.getDefaultDisplay().getSize(point);
		// 以屏幕左上角为原点，设置x、y初始值
		wmParams.x = PreferencesUtils.getInt("x",0); wmParams.y = PreferencesUtils.getInt("y",point.y/2);
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
	//开始点击的位置，相对点击的元素的位置
	private int ix = 0,iy = 0;
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int x = (int) event.getX(); int y = (int) event.getY();
		Log.e("aaaaaaa","点击位置：" + x+ ":" + y);
		Log.e("aaaaaaa","图标位置：" + wmParams.x+ ":" + wmParams.y);
		switch (event.getAction()) {//第一个触摸点
			case MotionEvent.ACTION_DOWN:  //按下 = 0
				ix = x ; iy = y ;
				break;
			case MotionEvent.ACTION_MOVE:  //移动 = 2
				wmParams.x = wmParams.x + x - ix;//ix - x;
				wmParams.y = wmParams.y + y - iy;//iy - y;*/
				// 使参数生效
				wm.updateViewLayout(v, wmParams);
				break;
			case MotionEvent.ACTION_UP:    // 抬起 = 1
				//ix = iy = 0;
				PreferencesUtils.putInt("x",wmParams.x);
				PreferencesUtils.putInt("y",wmParams.y);
				break;
		}
		return false;
	}
}
