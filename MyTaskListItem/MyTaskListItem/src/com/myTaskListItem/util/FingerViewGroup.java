package com.myTaskListItem.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/* 使用说明
 1.必须传入实现FingerSlideViewGroupInterface接口的类，该类负责管理内存的获取和释放(内存动态变化时时刻保持3个控件内存加载，因为随时会左右滑动)：
 2.放入本类的控件序列如果为优化内存要实现动态加载和释放序列中的子控件,例如ImageView，
 则必须设置所有子控件的窗口值为固定值《严重注意》，否则滑动结束后可能会出现自动前后移动界面的情况
 3.如果动态管理子控件序列中的内存，比如ImageView的内存，就必须实现上一条接口的onCurChileCtrlIndex(int index,int direct),
 该接口函数在滑动完成后被调起，其中index表示滑动完成后当前子控件在序列里的索引值(从0开始),direct表示本次滑动的方向。
 注意：测试发现，当一次滑动完成后，该接口函数会被连续调起两次《严重注意》，注意这个的处理要得当。
 4.对外接口:
 snapToScreen(int whichScreen),自动滑动到指定页，带实际滑动距离的动画效果,注意此操作时子控件序列必须全部加载，不能做动态管理内存
 setToScreen(int whichScreen),跳跃滑动到指定页，滑动距离相当短, 注意此操作时子控件序列虽然可以做动态管理内存，但目标子控件内存必须提前加载好
 5.使用示例(显示一系列图片)

 public class MainActivity extends Activity implements FingerSlideViewGroupInterface{
 int	   iChileCtrlLen = 25;	//假设这里等于25
 Bitmap bmpArray[] = null;	//存图片
 int	   iPicStatusArray[] = null;	//存图片内存加载状态，1为已加载，0为没加载(0时也可以固定加载占内存非常小的图,比如正在加载提示,滑动时如果图片太大,加载慢,就先显示它)
 FingerSlideViewGroup mSlideViewGroup = null;

 public MainActivity(Context context){
 super(context);
 bmpArray = new Bitmap[iChileCtrlLen];
 iPicStatusArray = new int[iChileCtrlLen];
 for(int i=0; i<iChileCtrlLen; i++){
 iPicStatusArray[i] = 0;	//初始化为没有加载图片
 LinearLayout layout = new LinearLayout(this);
 ImageView imgView = new ImageView(this);
 //写死子控件尺寸，不能让它切换图时自动缩放大小
 imgView.setMinimumHeight(800);
 imgView.setMinimumWidth(480);
 imgView.setMaxHeight(800);
 imgView.setMaxWidth(480);
 //初始化前两张图时候，需要直接加载，因为随时应对手指左滑
 if(0==i||1==i){
 iPicStatusArray[i] = 1;	//加载图，所以设为1
 bmpArray[i] = ;
 imgView.setImageBitmap(bmpArray[i]);
 }else{
 iPicStatusArray[i] = 0;	//没加载实际显示图，所以设为0
 bmpArray[i] = //此时也可以加载显示 "正在加载,请稍等" 等字样、且占 极少内存的图;
 imgView.setImageBitmap(bmpArray[i]);
 }
 layout.addView(imgView);
 mSlideViewGroup.addView(layout);
 }
 }

 //重载接口的事件响应函数，注意：经测试发现会被连续执行两次,也就是需要第二次执行时什么操作都不做
 @Override
 public void onCurChileCtrlIndex(int index, int direct){
 for(int i=0; i<iChileCtrlLen; i++){			
 ImageView imgView = (ImageView)((LinearLayout)mSlideViewGroup.getChildAt(i)).getChildAt(0);
 if(i==(index-1) || i==index || i==index+1){
 //属于当前显示子控件为中心的左右三个控件，需要保留内存，因为随时会被左或右滑动
 if(0 == iPicStatusArray[i]){	//说明未加载图或者加载 内存小的提示图
 freeBmp(bmpArray[i]);	//不管有没有，先释放该图
 bmpArray[i] = getBmp("指定图路径");
 imgView.setImageBitmap(bmpArray[i]);
 iPicStatusArray[i]=1;	//加载图后设置状态
 }
 }else{
 if(1 == iPicStatusArray[i]){	//说明已加载图，需要释放(此处释放后要加载提示图)
 Bitmap s1 = getBmp("占内存极少的正在加载提示图");
 imgView.setImageBitmap(s1);
 freeBmp(bmpArray[i]);
 bmpArray[i] = s1;
 iPicStatusArray[i]=0;
 }
 } 
 }

 }
 public void freeBmp(Bitmap curBitmap) {
 if(null != curBitmap){
 if(!curBitmap.isRecycled()){
 try{ curBitmap.recycle();}catch(Exception e){}
 }
 curBitmap = null;
 }
 }
 public Bitmap getBmp(int resourImage) {
 InputStream is = this.getResources().openRawResource(resourImage);
 Bitmap bitmapContent = BitmapFactory.decodeStream(is);
 try{ is.close();}catch(IOException e){}
 return bitmapContent;
 }
 }
 */

public class FingerViewGroup extends ViewGroup {
	// 常量定义
	private static final String LOG_TAG = "FingerSlideViewGroup";
	private static final int SNAP_VELOCITY = 1000; //
	private static final int TOUCH_STATE_REST = 0; // 静止，非滑动状态
	private static final int TOUCH_STATE_SCROLLING = 1; // 滑动状态
	private static final int TOUCH_DIRECT_REST = 0; // 静止无方向
	private static final int TOUCH_DIRECT_LEFT = 1; // 方向向左
	private static final int TOUCH_DIRECT_RIGHT = 2; // 方向向右
	// 变量定义
	private Scroller mScroller = null; // 滚动对象
	private FingerViewGroupInterface mParent = null; // 回调上层控制类方法的接口
	private ClickScreenInterface clickScreenInterface = null;

	// 点击事件
	private float startX;
	private float endX;
	private Context context;
	// 用来跟踪触摸速度的类
	// 当你需要跟踪的时候使用obtain()方法来来获得VelocityTracker类的一个实例对象
	// 使用addMovement(MotionEvent)函数将当前的移动事件传递给VelocityTracker对象
	// 使用computeCurrentVelocity (int units)函数来计算当前的速度
	// 使用getXVelocity ()、getYVelocity ()函数来获得当前的速度
	private VelocityTracker mVelocityTracker = null; // 用来跟踪触摸速度的类
	private int iScrollX = 0;
	private int iCurScreen = 0; // 指示当前屏幕
	private float fLastMotionX = 0f; // 手指按下时的坐标X值
	private float fLastMotionY = 0f;// 手指按下时的坐标Y值
	private int iTouchState = TOUCH_STATE_REST; // 触摸状态
	private int iTouchDirect = TOUCH_DIRECT_REST; // 静止状态，无方向
	// 获取滑动最小作用距离(表示滑动的时候，手的移动要大于这个距离才开始移动控件)
	private int mTouchSlop = 0; // 需调用系统函数获取

	public FingerViewGroup(Context context, FingerViewGroupInterface parent,
			ClickScreenInterface csInterface) {
		super(context);
		this.mParent = parent;
		this.clickScreenInterface = csInterface;
		this.mScroller = new Scroller(context);
		// styleContentActivity = (Actvity)context;
		this.context = context;
		// 获取系统默认的滑动最小距离值，
		this.mTouchSlop = ViewConfiguration.get(getContext())
				.getScaledTouchSlop();
		// 设置试图同时适配屏幕大小和控件内容
		this.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.FILL_PARENT));
	}

	private void snapToDestination() {
		final int screenWidth = getWidth();
		final int whichScreen = (this.iScrollX + (screenWidth / 2))
				/ screenWidth;
		// Log.i(LOG_TAG, "from des");
		snapToScreen(whichScreen);
	}

	// 强制设置显示当前序列中的第几屏到屏幕上，带滑动效果
	public void snapToScreen(int whichScreen) {
		// Log.i(LOG_TAG, "snap To Screen " + whichScreen);
		this.iCurScreen = whichScreen;
		final int newX = whichScreen * this.getWidth();
		final int delta = newX - this.iScrollX;
		this.mScroller.startScroll(this.iScrollX, 0, delta, 0,
				Math.abs(delta) * 2);
		this.invalidate();
	}

	public void setToScreen(int whichScreen) {
		// Log.i(LOG_TAG, "set To Screen " + whichScreen);
		this.iCurScreen = whichScreen;
		final int newX = whichScreen * getWidth();
		this.mScroller.startScroll(newX, 0, 0, 0, 10);
		this.invalidate();
	}

	// ******************************** 重载函数 *********************************
	// 拦截所有的手势事件,如果返回false则事件向子控件传递，返回true则调起OnTouchEvent方法
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		boolean bRet = true; // 截取手势消息的函数返回值
								// 指引事件流向，如果返回false则事件向子控件传递，返回true则调起OnTouchEvent方法

		final int action = ev.getAction();
		if ((MotionEvent.ACTION_MOVE == action)
				&& (TOUCH_STATE_REST != this.iTouchState)) {
			// 确认是滑动动作，去执行本类的OnTouchEvent，不传递消息给子控件
			bRet = true;
		} else {
			// 不确定是否滑动动作，那计算当前滑动状态，并引导后续事件处理方向
			final float x = ev.getX();
			final float y = ev.getY();
			switch (action) {
			case MotionEvent.ACTION_MOVE:
				// 计算与最后一次手指点击按下屏幕时记录的X值距离
				final int xDiff = (int) Math.abs(x - this.fLastMotionX);
				boolean xMoved = xDiff > this.mTouchSlop;
				if (xMoved)
					this.iTouchState = TOUCH_STATE_SCROLLING; // 记录滑动状态̬

				final int yDiff = (int) Math.abs(y - this.fLastMotionY);
				boolean yMoved = yDiff > this.mTouchSlop;
				if (yMoved)
					this.iTouchState = TOUCH_STATE_REST; // 记录滑动状态
				break;

			case MotionEvent.ACTION_DOWN:
				// 记录当前触摸的X值，并根据此时自动互动是否结束判断并设置滑动状态
				this.fLastMotionX = x;
				this.fLastMotionY = y;
				this.iTouchState = this.mScroller.isFinished() ? TOUCH_STATE_REST
						: TOUCH_STATE_SCROLLING;
				break;

			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				// 取消或手指离开屏幕，设置滑动状态为静止
				this.iTouchState = TOUCH_STATE_REST;
				break;
			}
			// 不是静止状态不把事件传递给子控件
			bRet = this.iTouchState != TOUCH_STATE_REST;
		}
		return bRet;
	}

	// 本类手指触摸响应重载
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// 把事件 添加进 跟踪触摸速度的类
		if (null == this.mVelocityTracker)
			this.mVelocityTracker = VelocityTracker.obtain();
		this.mVelocityTracker.addMovement(event);

		final int action = event.getAction();
		final float x = event.getX();

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			// 记录当前点击点X值，如果有动画则停止动画
			// this.iTouchDirect = TOUCH_DIRECT_REST;
			if (!this.mScroller.isFinished())
				this.mScroller.abortAnimation();
			this.fLastMotionX = x;
			startX = event.getX();
			break;
		case MotionEvent.ACTION_MOVE:
			// Log.i(LOG_TAG,"event : move");
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			// Scroll to follow the motion event
			final int deltaX = (int) (this.fLastMotionX - x);
			this.fLastMotionX = x;

			// Log.i(LOG_TAG, "event : move, deltaX " + deltaX + ", iScrollX " +
			// iScrollX);
			if (deltaX < 0) {// 说明手指往右滑
				if (this.iScrollX > 0) { // 左边还有隐藏图
					this.scrollBy(Math.max(-this.iScrollX, deltaX), 0); // 按照给定的像素数据量<x轴距离,y轴距离>滚动。没有动画。
				}
			} else if (deltaX > 0) { // 说明手指往左滑
				final int availableToScroll = this.getChildAt(
						this.getChildCount() - 1).getRight()
						- this.iScrollX - this.getWidth();
				if (availableToScroll > 0) { // 右侧还有隐藏图
					this.scrollBy(Math.min(availableToScroll, deltaX), 0);
				}
			}

			break;
		case MotionEvent.ACTION_UP:
			Log.i(LOG_TAG, "event : up");
			// if (mTouchState == TOUCH_STATE_SCROLLING) {
			final VelocityTracker velocityTracker = this.mVelocityTracker;
			velocityTracker.computeCurrentVelocity(1000);
			int velocityX = (int) velocityTracker.getXVelocity();
			endX = event.getX();

			if (velocityX > SNAP_VELOCITY && this.iCurScreen > 0) {
				// 可以向左滑
				this.iTouchDirect = TOUCH_DIRECT_LEFT;
				this.snapToScreen(this.iCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& this.iCurScreen < this.getChildCount() - 1) {
				// 可以向右滑
				this.iTouchDirect = TOUCH_DIRECT_RIGHT;
				this.snapToScreen(this.iCurScreen + 1);
			} else if (startX == endX) {
				clickScreenInterface.Click_Screen();
			} else {
				this.snapToDestination();
			}

			if (null != this.mVelocityTracker) {
				this.mVelocityTracker.recycle();
				this.mVelocityTracker = null;
			}
			// }
			this.iTouchState = TOUCH_STATE_REST;
			break;
		// 取消则设置状态为静止，非滑动状态
		case MotionEvent.ACTION_CANCEL:
			// Log.i(LOG_TAG, "event : cancel");
			this.iTouchState = TOUCH_STATE_REST;
			this.iTouchDirect = TOUCH_DIRECT_REST;
		}
		this.iScrollX = this.getScrollX();

		return true;
	}

	@Override
	// 布局处理
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childLeft = 0;
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				final int childWidth = child.getMeasuredWidth();
				child.layout(childLeft, 0, childLeft + childWidth,
						child.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	@Override
	// 计算view的宽和高
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		// final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		// if (widthMode != MeasureSpec.EXACTLY) {
		// throw new IllegalStateException("error mode.");
		// }

		final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		if (heightMode != MeasureSpec.EXACTLY) {
			throw new IllegalStateException("error mode.");
		}

		// The children are given the same width and height as the workspace
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}
		Log.i(LOG_TAG, "moving to screen " + iCurScreen);
		this.scrollTo(this.iCurScreen * width, 0);
	}

	@Override
	public void computeScroll() {
		if (this.mScroller.computeScrollOffset()) {
			this.iScrollX = this.mScroller.getCurrX();
			this.scrollTo(this.iScrollX, 0);
			this.postInvalidate();
		}
		if (this.mScroller.isFinished() && TOUCH_STATE_REST == this.iTouchState)
			this.mParent
					.onCurChileCtrlIndex(this.iCurScreen, this.iTouchDirect);
	}
}