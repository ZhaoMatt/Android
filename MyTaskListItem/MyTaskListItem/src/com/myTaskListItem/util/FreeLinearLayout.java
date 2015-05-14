package com.myTaskListItem.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Scroller;

//这个类和FreeFrameLayout一样，只是为了实现“订阅页面”的展开和收起功能而该了下布局而已
public class FreeLinearLayout extends LinearLayout {

	public static String HORIZONTAL = "horizontal";
	public static String VERTICAL = "vertical";
	private String orientation;

	private Scroller mScroller; // 这个暂时没有用上
	private VelocityTracker mVelocityTracker;
	private Context context;

	private FreeFrameLayoutInterface mEventInterface;// 事件回调接口

	private int iDrection = 0;// 滚动方向,向上滚动值是-1，向下滚动值是1
	private float fLastPoint_y;// 手指按下去时的坐Y标
	private int isScroller_y;// 记录开始滚动的位置的Y坐标
	private float fLastPoint_x;// 手指按下去时的坐X标
	private int isScroller_x;// 记录开始滚动的位置的X坐标
	private boolean moveFlag = false;// 当不滑动时就不要执行 ACTION_UP中的代码

	private int iAllViewHeight_px = 0;// 本布局中所有view高度的像素值和
	private int iAllViewWidth_px = 0;// 本布局中所有view宽度的像素值和

	private List<List<View>> all_screen_View;// 存放每屏幕能放得下的View
	private List<LinearLayout.LayoutParams> all_params;// 存放所有子View的LayoutParams属性!!本版本没有用上
	private List<Rect> childView_rect; // 存放所有子View的矩形坐标

	public boolean bMoveIsDone = false;// 控制当执行完手指移动事件后方法后再执行computeScroll()方法

	private int self_height = 0;// 这个布局本身的高度
	private int self_width = 0;// 这个布局本身的宽度

	private int preChildHeight;// 上面个子控件的高度总和
	private boolean preChildHeight_once = true;// 保证preChildHeight在addView()中开始时只执行一次

	public FreeLinearLayout(Context context, String orientation,
			FreeFrameLayoutInterface mEventInterface) {
		super(context);

		this.orientation = orientation;

		if (this.orientation.equals(HORIZONTAL)) {
			this.setOrientation(LinearLayout.HORIZONTAL);
		} else if (this.orientation.equals(VERTICAL)) {
			this.setOrientation(LinearLayout.VERTICAL);
		}

		this.mScroller = new Scroller(context);
		this.context = context;
		this.mEventInterface = mEventInterface;
		all_screen_View = new ArrayList<List<View>>();
		all_params = new ArrayList<LinearLayout.LayoutParams>();
		childView_rect = new ArrayList<Rect>();
	}

	// 获得屏幕的像素高度
	public int getDisplayHeight() {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getHeight();
	}

	// 获得屏幕的像素宽度
	public int getDisplayWidth() {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		return display.getWidth();
	}

	// 返回封装每屏View集合的集合
	public List<List<View>> getAll_screen_View() {
		if (0 != all_screen_View.size()) {
			return all_screen_View;
		} else
			return null;
	}

	// 计算每屏显示多少个View，
	public void initScreenView() {
		if (0 != childView_rect.size()) {
			if (0 == self_height) {
				this.self_height = this.getDisplayHeight();
			}
			if (0 == self_width) {
				this.self_width = this.getDisplayWidth();
			}
			if (VERTICAL.equals(this.orientation)) {
				int screen_count = (int) Math
						.ceil((double) this.iAllViewHeight_px / self_height);// 计算总共有多少屏
				List<Rect> self_rect = new ArrayList<Rect>();// 存放本布局的每屏矩形范围
				for (int i = 0; i < screen_count; i++) {
					self_rect.add(new Rect(0, i * self_height, self_width, i
							* self_height + self_height));
					List<View> one_screen = new ArrayList<View>();// 存放一屏所包含的View
					for (int j = 0; j < this.getChildCount(); j++) {// 判断出每屏包含的View
						if (childView_rect.get(j).intersect(self_rect.get(i))
								|| childView_rect.get(j).contains(
										self_rect.get(i))) {
							one_screen.add(this.getChildAt(j));
						}
					}
					all_screen_View.add(one_screen);
				}
			} else if (HORIZONTAL.equals(this.orientation)) {
				int screen_count = (int) Math
						.ceil((double) this.iAllViewWidth_px / self_width);// 计算总共有多少屏
				List<Rect> self_rect = new ArrayList<Rect>();// 存放本布局的每屏矩形范围
				for (int i = 0; i < screen_count; i++) {
					self_rect.add(new Rect(i * self_width, 0, i * self_width
							+ self_width, self_height));
					List<View> one_screen = new ArrayList<View>();// 存放一屏所包含的View
					for (int j = 0; j < this.getChildCount(); j++) {// 判断出每屏包含的View
						if (childView_rect.get(j).intersect(self_rect.get(i))
								|| childView_rect.get(j).contains(
										self_rect.get(i))) {
							one_screen.add(this.getChildAt(j));
						}
					}
					all_screen_View.add(one_screen);
				}
			}
			// System.out.println("self_height-->" + self_height
			// + ";self_width-->" + self_width);
			// System.out.println("控件总高度" + this.iAllViewHeight_px);
		}

	}

	// 带动画的滑动
	public void snapToScreen_vertical(int desY) {
		// Log.i(LOG_TAG, "snap To Screen " + whichScreen);
		final int newY = desY;
		final int delta = newY - this.isScroller_y;
		this.mScroller.startScroll(0, this.isScroller_y, 0, delta);
		this.invalidate();
		// System.out.println(this.isScroller_y+"----------->"+delta);
	}

	public void snapToScreen_horizontal(int desX) {
		// Log.i(LOG_TAG, "snap To Screen " + whichScreen);
		final int newX = desX;
		final int delta = newX - this.isScroller_x;
		this.mScroller.startScroll(this.isScroller_x, 0, delta, 0);
		this.invalidate();
	}

	// 带动画效果滚动到指定的位置
	public void snapToPotion(int desY) {
		final int newY = desY;
		final int delta = newY - this.isScroller_y;
		this.mScroller.startScroll(0, this.isScroller_y, 0, delta);
		this.computeScroll();
		this.invalidate();
	}

	// 回到初始位置
	public void goToFist() {
		this.isScroller_x = 0;
		this.isScroller_y = 0;
		this.scrollTo(0, 0);
	}

	// 设置本布局中所有控件的高度(这样就可以在展开和收缩时来防止本布局可以滚动到空白区域)
	public void setIallViewHeight_px(int iAllViewHeight_px) {
		this.iAllViewHeight_px = iAllViewHeight_px;

	}

	public int getIsScroller_y() {
		return isScroller_y;
	}

	// ==================以下是重写方法========================

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (VERTICAL.equals(this.orientation)) {
			if (0 != iAllViewHeight_px) {
				if (null == mVelocityTracker) {
					this.mVelocityTracker = VelocityTracker.obtain();
				}
				this.mVelocityTracker.addMovement(event);

				final int action = event.getAction();
				final float fY = event.getY();
				int delta_Y = 0;
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (!mScroller.isFinished()) {
						mScroller.abortAnimation();
					}
					fLastPoint_y = event.getY();
					break;
				case MotionEvent.ACTION_MOVE:
					delta_Y = (int) (fLastPoint_y - fY);// 计算移动了多少距离
					this.fLastPoint_y = fY; // 再记录下移动后的最后一次坐标
					if (delta_Y > 0) { // 说明手指向上滑动
						iDrection = -1;
						if ((iAllViewHeight_px - isScroller_y) >= (this.self_height - 100)) {// 判断下面是否还有本布局的子View
							this.scrollBy(0, delta_Y);
						}
					} else if (delta_Y < 0) {// 手指向下滑动
						iDrection = 1;
						if (isScroller_y + 100 >= 0) {// 判断上面是否还有本布局的子View(加100是为了实现滑动完View后动画返回)
							this.scrollBy(0, delta_Y);
						}
					}
					this.bMoveIsDone = true;
					moveFlag = true;
					break;
				case MotionEvent.ACTION_UP:
					if (isScroller_y < 0 && moveFlag) {
						this.snapToScreen_vertical(0);
					} else if (isScroller_y + this.self_height
							- iAllViewHeight_px > 0
							&& iAllViewHeight_px >= this.self_height
							&& moveFlag) { // 这个是判断向上滑时子控件超出屏幕后，再回到屏幕最底部(子控件的总高度大于本布局的高度)
						this.snapToScreen_vertical(iAllViewHeight_px
								- this.self_height);
						this.postInvalidate();
					} else if (iAllViewHeight_px < this.self_height && moveFlag) { // 这个是判断向上滑时子控件的总高度小于本布局的高度
						this.scrollTo(0, 0);
					}
					moveFlag = false;
					this.computeScroll();
					break;
				}
				this.isScroller_y = this.getScrollY();
			}
		} else if (HORIZONTAL.equals(this.orientation)) {
			if (0 != iAllViewWidth_px) {
				if (null == mVelocityTracker) {
					this.mVelocityTracker = VelocityTracker.obtain();
				}
				this.mVelocityTracker.addMovement(event);

				final int action = event.getAction();
				final float fX = event.getX();
				int delta_X = 0;
				switch (action) {
				case MotionEvent.ACTION_DOWN:
					if (!mScroller.isFinished()) {
						mScroller.abortAnimation();
					}
					fLastPoint_x = event.getX();
					break;
				case MotionEvent.ACTION_MOVE:
					delta_X = (int) (fLastPoint_x - fX);// 计算移动了多少距离
					this.fLastPoint_x = fX; // 再记录下移动后的最后一次坐标
					if (delta_X > 0) { // 说明手指向左滑动
						iDrection = -1;
						if ((iAllViewWidth_px - isScroller_x) >= (this.self_width - 100)) {// 判断下面是否还有本布局的子View
							this.scrollBy(delta_X, 0);
						}
					} else if (delta_X < 0) {// 手指向右滑动
						iDrection = 1;
						if (isScroller_x + 100 >= 0) {// 判断上面是否还有本布局的子View(加100是为了实现滑动完View后动画返回)
							this.scrollBy(delta_X, 0);
						}
					}
					this.bMoveIsDone = true;
					moveFlag = true;
					break;
				case MotionEvent.ACTION_UP:
					if (isScroller_x < 0 && moveFlag) {
						this.snapToScreen_horizontal(0);
					} else if (isScroller_x + this.self_width
							- iAllViewWidth_px > 0
							&& iAllViewWidth_px >= this.self_width && moveFlag) {
						this.snapToScreen_horizontal(iAllViewWidth_px
								- this.self_width);
					} else if (iAllViewWidth_px < this.self_width && moveFlag) {
						this.scrollTo(0, 0);
					}
					this.computeScroll();
					moveFlag = false;
					break;
				}
				this.isScroller_x = this.getScrollX();
			}
		}

		return true;
	}

	// 这个没有考虑横向布局的情况
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub\
		boolean bRet = true;
		int action = ev.getAction();
		if (MotionEvent.ACTION_MOVE == action) { // 如果是滑动动作，则执行本类的onTouch()事件，不向子控件传递
			bRet = true;
			fLastPoint_y = ev.getY();
		} else {
			bRet = false;
		}
		return bRet;
	}

	// 没有判断wrap_content 和match_parent 的这种情况
	public void setLayoutParams(LayoutParams params) {
		// TODO Auto-generated method stub
		super.setLayoutParams(params);

		int groupHeight = params.height;
		int groupWidth = params.width;
		if (0 == groupHeight || LayoutParams.FILL_PARENT == params.height) {
			this.self_height = this.getDisplayHeight();
		} else {
			this.self_height = groupHeight;
		}
		if (0 == groupWidth || LayoutParams.FILL_PARENT == params.width) {
			this.self_width = this.getDisplayWidth();
		} else {
			this.self_width = groupWidth;
		}
	}

	// 将子View都投影成屏幕上的Rect并连同其LayoutParams属性一同保存起来
	public void addView(View child, LayoutParams params) {
		// TODO Auto-generated method stub
		super.addView(child, params);

		if (preChildHeight_once) {
			preChildHeight = params.topMargin;
			preChildHeight_once = false;
		}

		Rect retChildView = null;
		if (params.width == LayoutParams.FILL_PARENT
				&& params.height != LayoutParams.FILL_PARENT) {

			retChildView = new Rect(params.leftMargin, preChildHeight,
					params.leftMargin + this.getDisplayWidth(), preChildHeight
							+ params.height); // 每个子控件的矩形位置
			childView_rect.add(retChildView);
			preChildHeight += params.height;
		} else if (params.width != LayoutParams.FILL_PARENT
				&& params.height == LayoutParams.FILL_PARENT) {
			retChildView = new Rect(params.leftMargin, preChildHeight,
					params.leftMargin + params.width, preChildHeight
							+ this.getDisplayHeight()); // 每个子控件的矩形位置
			childView_rect.add(retChildView);
			preChildHeight += this.getDisplayHeight();
		} else if (params.width == LayoutParams.FILL_PARENT
				&& params.height == LayoutParams.FILL_PARENT) {
			retChildView = new Rect(params.leftMargin, preChildHeight,
					params.leftMargin + this.getDisplayWidth(), preChildHeight
							+ this.getDisplayHeight()); // 每个子控件的矩形位置
			childView_rect.add(retChildView);
			preChildHeight += this.getDisplayHeight();
		} else {
			retChildView = new Rect(params.leftMargin, preChildHeight,
					params.leftMargin + params.width, preChildHeight
							+ params.height); // 每个子控件的矩形位置
			childView_rect.add(retChildView);
			preChildHeight += this.getDisplayHeight();
		}

		all_params.add(params);
		if (VERTICAL.equals(this.orientation)) {
			if (iAllViewHeight_px < retChildView.bottom) {
				iAllViewHeight_px = retChildView.bottom;
			}
		} else if (HORIZONTAL.equals(this.orientation)) {
			if (iAllViewWidth_px < retChildView.right) {
				iAllViewWidth_px = retChildView.right;
			}
		}
	}

	@Override
	public void removeAllViewsInLayout() {
		// TODO Auto-generated method stub
		super.removeAllViewsInLayout();
		this.all_screen_View.clear();
	}

	/* 每次拖动的时候这个方法都会执行 */
	@Override
	protected void dispatchDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.dispatchDraw(canvas);
	}

	// 这个方法是在dispatchDraw()方法执行前第一次执行的,然后再触发执行
	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		int iCureetScreen = 0;
		if (VERTICAL.equals(this.orientation)) {
			if (this.mScroller.computeScrollOffset()) {
				this.isScroller_y = this.mScroller.getCurrY();
				this.scrollTo(0, this.isScroller_y);
				this.postInvalidate();
			}
			iCureetScreen = (int) this.isScroller_y / (int) this.self_height;
			if (bMoveIsDone) {
				this.mEventInterface.onCurChileCtrlScreen(iCureetScreen,
						iDrection);
			}
		} else if (HORIZONTAL.equals(this.orientation)) {
			if (this.mScroller.computeScrollOffset()) {
				this.isScroller_x = this.mScroller.getCurrX();
				this.scrollTo(isScroller_x, 0);
				this.postInvalidate();
			}
			iCureetScreen = (int) this.isScroller_x / (int) this.self_width;
			if (bMoveIsDone) {
				this.mEventInterface.onCurChileCtrlScreen(iCureetScreen,
						iDrection);
			}
		}
	}

}
