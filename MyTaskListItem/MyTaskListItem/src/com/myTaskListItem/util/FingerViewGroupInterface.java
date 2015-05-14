package com.myTaskListItem.util;

public interface FingerViewGroupInterface {
	//index是，direct是本次滑动的方向 
	/*	滑动处理完后的通知
	 *	@param index, 序列组件里的顺序值(从0开始)
	 *	@param direct, 本次滑动的方向，值取以下值：	FingerSlideViewGroup.TOUCH_DIRECT_REST	没滑动
	 *												FingerSlideViewGroup.TOUCH_DIRECT_LEFT	向左滑
	 *												FingerSlideViewGroup.TOUCH_DIRECT_RIGHT 向右滑
	 */
	public void onCurChileCtrlIndex(int index, int direct);
}
