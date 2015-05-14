package com.mylisting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.myTaskListItem.util.BmpName;
import com.myTaskListItem.util.FreeFrameLayout;
import com.myTaskListItem.util.FreeFrameLayoutInterface;
import com.myTaskListItem.util.MemoryCenter;
import com.myTaskListItem.util.TitleImgView;

public class TaskProductImg extends Activity implements
		FreeFrameLayoutInterface {
	private List<FrameLayout.LayoutParams> listParams; // 存放内容视图各个控件的布局属性
	int[] iBmpId = new int[] { R.drawable.test1, R.drawable.test2,
			R.drawable.test3, R.drawable.test4, R.drawable.test5,
			R.drawable.test6, R.drawable.test7, R.drawable.test8,
			R.drawable.test9, R.drawable.test10, R.drawable.test11,
			R.drawable.test12, R.drawable.test13, R.drawable.test14,
			R.drawable.test15, R.drawable.test16, R.drawable.test17,
			R.drawable.test18, R.drawable.test19, R.drawable.test20,
			R.drawable.test21, R.drawable.test22, R.drawable.test24,
			R.drawable.test25, R.drawable.test26, R.drawable.test27,
			R.drawable.test28, R.drawable.test29, R.drawable.test30,
			R.drawable.test31, R.drawable.test32, R.drawable.test33,
			R.drawable.test34, R.drawable.test35, R.drawable.test36,
			R.drawable.test37, R.drawable.test38, R.drawable.test39,
			R.drawable.test40 };
	int iLeftHeightCount_px = 0; // 不规则布局中左边的控件的总高度
	int iRightHeightCount_px = 0; // 不规则布局中右边控件的总高度
	FrameLayout.LayoutParams bmpParams_list;
	private List<List<View>> all_screen_view; // 用于图片资源释放用的
	private int iIndex;
	private ManagerBmp managerBmp;

	Bitmap titleBmp;
	TitleImgView titleImage;

	Bitmap bomBmp;
	TitleImgView bomImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.product);

		Display display = ((WindowManager) this
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		listParams = new ArrayList<FrameLayout.LayoutParams>();

		FrameLayout rootLayout = (FrameLayout) this
				.findViewById(R.id.productlayout);

		// 顶部标题
		titleImage = new TitleImgView(this);
		FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(
				480, 53);
		titleImage.setLayoutParams(titleParams);
		rootLayout.addView(titleImage);

		// 底部导航条
		bomImage = new TitleImgView(this);
		FrameLayout.LayoutParams bomParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, 55);
		bomParams.gravity = Gravity.LEFT;
		bomParams.topMargin = 745;
		bomImage.setLayoutParams(bomParams);
		rootLayout.addView(bomImage);

		// 加载内容视图
		FreeFrameLayout freeLayout = new FreeFrameLayout(this,
				FreeFrameLayout.VERTICAL, this);
		FrameLayout.LayoutParams freeParams = new FrameLayout.LayoutParams(480,
				display.getHeight() - 50 - 55);
		freeParams.topMargin = 50;
		freeParams.gravity = Gravity.LEFT;
		freeLayout.setLayoutParams(freeParams);
		freeLayout.setBackgroundColor(Color.argb(255, 221, 221, 221));
		rootLayout.addView(freeLayout);

		// 下面实现了一个简单的排序：先从左边开始排，当左边的控件总高度>右边的控件总高度时，就往右边排；当
		// 左边的控件总高度<右边控件总高度是就往左边排
		List<FrameLayout.LayoutParams> listLeftBmpParams = new ArrayList<FrameLayout.LayoutParams>();
		List<FrameLayout.LayoutParams> listRightBmpParams = new ArrayList<FrameLayout.LayoutParams>();
		// 获得内容图片的宽和高
		for (int i = 0; i < iBmpId.length; i++) {
			Bitmap bmp = this.getBmp(iBmpId[i]);
			FrameLayout.LayoutParams bmpParams = new FrameLayout.LayoutParams(
					233, bmp.getHeight()); // 所有图片的宽度都是233个像素
			listLeftBmpParams.add(bmpParams);
			bmp.recycle();
		}

		// 所有左边的图片距离左边框 5px,所有右边的图片距离右边框5px，并且所有图片和它上面的图片相距5px
		for (int i = 0; i < listLeftBmpParams.size(); i++) {
			if (0 == i) { // 左边屏幕 拥有第一张图片属性
				bmpParams_list = new FrameLayout.LayoutParams(
						listLeftBmpParams.get(i).width,
						listLeftBmpParams.get(i).height);
				bmpParams_list.setMargins(5, 5, 0, 0);// 距离屏幕顶部有5像素
				bmpParams_list.gravity = Gravity.LEFT;
				listParams.add(bmpParams_list);

				// 这里 listLeftBmpParams.get(i).height +5
				// 是第一个控件底部的位置，再加5才能距离第一张图片底部5个像素
				iLeftHeightCount_px = listLeftBmpParams.get(i).height + 5 + 5;
			} else {
				// 计算右边屏幕的图片总高度
				for (int j = 0; j < listRightBmpParams.size(); j++) {
					iRightHeightCount_px += listRightBmpParams.get(j).height + 5;
				}
				if (iLeftHeightCount_px > iRightHeightCount_px) {
					listRightBmpParams.add(listLeftBmpParams.get(i));
					bmpParams_list = new FrameLayout.LayoutParams(
							listLeftBmpParams.get(i).width,
							listLeftBmpParams.get(i).height);
					bmpParams_list.setMargins(241, iRightHeightCount_px + 5, 0,
							0);
					bmpParams_list.gravity = Gravity.LEFT;
					listParams.add(bmpParams_list);
					listLeftBmpParams.remove(i);
					i--;
				} else {
					bmpParams_list = new FrameLayout.LayoutParams(
							listLeftBmpParams.get(i).width,
							listLeftBmpParams.get(i).height);
					bmpParams_list.setMargins(5, iLeftHeightCount_px, 0, 0);
					bmpParams_list.gravity = Gravity.LEFT;
					listParams.add(bmpParams_list);
					iLeftHeightCount_px += (listLeftBmpParams.get(i).height + 5);
				}
			}
			iRightHeightCount_px = 0;
		}

		// 初始化控件并设置控件的属性
		for (int i = 0; i < listParams.size(); i++) {
			TitleImgView tiv = new TitleImgView(this);
			tiv.setBackgroundColor(Color.WHITE);
			freeLayout.addView(tiv, listParams.get(i));
		}

		freeLayout.initScreenView();
		all_screen_view = freeLayout.getAll_screen_View();

		// 从这里开始初始化加载图片资源
		List<View> preOne_screen_view = new ArrayList<View>(); // 记录上一屏有多少张图片
		int iBeforPresentBmps = 0;// 当前屏以前共有多少张图片
		for (int i = 0; i < all_screen_view.size(); i++) {
			List<View> one_screen_view = all_screen_view.get(i);
			// 给前两屏添加图片
			if (0 == i || 1 == i) {
				for (int j = 0; j < one_screen_view.size(); j++) {
					TitleImgView imgView_left = (TitleImgView) one_screen_view
							.get(j);
					Bitmap bmp = imgView_left.getBitmap();
					if (null == bmp) {
						imgView_left.setBitmap(MemoryCenter.getInstance()
								.getLimitBmp(
										BmpName.TASKPRODUCTIMG_CONTENT + i + ""
												+ j,
										this.getBmp(iBmpId[j
												+ iBeforPresentBmps]),
										i + "" + j, i + "" + j));
					}
				}
				// 也可以在这里给其添加非常小的提示图片正在加载的小图标
			} else {
				for (int j = 0; j < one_screen_view.size(); j++) {
					TitleImgView imgView_left = (TitleImgView) one_screen_view
							.get(j);
					Bitmap bmp = imgView_left.getBitmap();
					if (null != bmp) {
						imgView_left.setBitmap(null);
						MemoryCenter.getInstance().FreeLimitBmp(
								BmpName.TASKPRODUCTIMG_CONTENT + i + "" + j,
								i + "" + j);
					}
				}
			}
			preOne_screen_view = one_screen_view;
			iBeforPresentBmps += preOne_screen_view.size();
		}
		managerBmp = new ManagerBmp();
	}

	public Bitmap getBmp(int bmpId) {
		return BitmapFactory.decodeResource(this.getResources(), bmpId);
	}

	// 释放内存图片
	public void freeBmp(Bitmap curBitmap) {
		if (null != curBitmap) {
			if (!curBitmap.isRecycled()) {
				try {
					curBitmap.recycle();
				} catch (Exception e) {
				}
			}
			curBitmap = null;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		if (null == titleImage.getBitmap() && null == bomImage.getBitmap()) {
			titleBmp = MemoryCenter.getInstance().getLimitBmp(
					BmpName.APP_TITLE_IMG,
					BitmapFactory.decodeResource(this.getResources(),
							R.drawable.title), "Listing_title", "title");
			bomBmp = MemoryCenter.getInstance().getLimitBmp(
					BmpName.APP_BOM_IMG,
					BitmapFactory.decodeResource(this.getResources(),
							R.drawable.bottom), "Listing_bom", "bottom");
			this.titleImage.setBitmap(titleBmp);
			this.bomImage.setBitmap(bomBmp);
		}
	}

	@Override
	public void onCurChileCtrlScreen(int index, int direct) {
		// TODO Auto-generated method stub

//		 System.out.println("index---------------->" + index);
//		 System.out.println("direct-------------->" + direct);

		int preIndex = this.iIndex;
		this.iIndex = index;
		if (0 != direct) {// 当是滑动时执行操作
			if (!managerBmp.isAlive()) {
				managerBmp = new ManagerBmp(); // 注意这里必须在重新赋值;如果不重新赋值的话线程结束后，这个引用指向的是已经结束了的线程对象（已经在内存中不存在咯）
				managerBmp.query_flag = true;
				managerBmp.manaBmp_flag = true;
				managerBmp.start(); // 守护线程只开启一次
			}
			if (preIndex != index) { // 当屏幕变化时使守护线程内部循环开始执行
				managerBmp.manaBmp_flag = true;
			}
		}
	}

	class ManagerBmp extends Thread {

		public boolean manaBmp_flag = true;
		public boolean query_flag = true;

		public void run() {
			while (query_flag) {
				List<View> preOne_screen_view = new ArrayList<View>(); // 记录上一屏有多少张图片
				int iBeforPresentBmps = 0;// 当前屏以前共有多少张图片
				int i = 0;
				while (i < all_screen_view.size() && manaBmp_flag) {
					List<View> one_screen_view = all_screen_view.get(i);
					// 属于当前屏幕中心的上下三个屏幕，需要保留内存，因为随时会被上或下滑动
					if (iIndex - 1 == i || iIndex == i || iIndex + 1 == i) {
						for (int j = 0; j < one_screen_view.size()
								&& manaBmp_flag; j++) {
							TitleImgView imgView_left = (TitleImgView) one_screen_view
									.get(j);
							Bitmap bmp = imgView_left.getBitmap();
							if (null == bmp) {
								bmp = MemoryCenter.getInstance().getLimitBmp(
										BmpName.TASKPRODUCTIMG_CONTENT + i + ""
												+ j,
										TaskProductImg.this.getBmp(iBmpId[j
												+ iBeforPresentBmps]),
										i + "" + j, i + "" + j);
								imgView_left.setBitmap(bmp);
							}
						}
					} else {
						for (int j = 0; j < one_screen_view.size()
								&& manaBmp_flag; j++) {
							TitleImgView imgView_left = (TitleImgView) one_screen_view
									.get(j);
							Bitmap bmp = imgView_left.getBitmap();
							imgView_left.setBitmap(null);
							if (null != bmp) {
								// System.out.println("第" + i + "屏开始释放内存");
								MemoryCenter.getInstance().FreeLimitBmp(
										BmpName.TASKPRODUCTIMG_CONTENT + i + ""
												+ j, i + "" + j);
							}
						}
					}
					preOne_screen_view = one_screen_view;
					iBeforPresentBmps += preOne_screen_view.size();
					i++;
				}
				this.manaBmp_flag = false; // 内层循环执行完后停止
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// System.out.println("线程已经死亡");
		}
	}

	// 当不在屏幕上显示的时候就只保留当前屏幕上的图片资源，其它两屏图片资源释放,并且终止图片释放和加载线程
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		managerBmp.query_flag = false;
		managerBmp.manaBmp_flag = false;
		// 释放FreeFrameLayout布局中的图片资源
		if (null != this.all_screen_view) {
			for (int i = 0; i < all_screen_view.size(); i++) {
				List<View> one_screen_view = all_screen_view.get(i);
				if (iIndex - 1 == i || iIndex + 1 == i) {
					for (int j = 0; j < one_screen_view.size(); j++) {
						TitleImgView imgView_left = (TitleImgView) one_screen_view
								.get(j);
						Bitmap bmp = imgView_left.getBitmap();
						imgView_left.setBitmap(null);
						if (null != bmp) {
							// System.out.println("第" + i + "屏开始释放内存");
							MemoryCenter.getInstance()
									.FreeLimitBmp(
											BmpName.TASKPRODUCTIMG_CONTENT + i
													+ "" + j, i + "" + j);
						}
					}
				}
			}
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		managerBmp.query_flag = false;
		managerBmp.manaBmp_flag = false;
		// 释放FreeFrameLayout布局中的图片资源
		if (null != this.all_screen_view) {
			for (int i = 0; i < all_screen_view.size(); i++) {
				List<View> one_screen_view = all_screen_view.get(i);
				if (iIndex - 1 == i || iIndex == i || iIndex + 1 == i) {
					for (int j = 0; j < one_screen_view.size(); j++) {
						TitleImgView imgView_left = (TitleImgView) one_screen_view
								.get(j);
						Bitmap bmp = imgView_left.getBitmap();
						imgView_left.setBitmap(null);
						if (null != bmp) {
							// System.out.println("第" + i + "屏开始释放内存");
							MemoryCenter.getInstance()
									.FreeLimitBmp(
											BmpName.TASKPRODUCTIMG_CONTENT + i
													+ "" + j, i + "" + j);
						}
					}
				}
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		long lDown = 0;
		long lUp = 0;
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			lDown = System.currentTimeMillis();
		}
		if (event.getAction() == MotionEvent.ACTION_UP) {
			lUp = System.currentTimeMillis();
		}
		if (lUp - lDown >= 2000) {
			Intent intent = new Intent(this, TaskSubscibe.class);
			this.startActivity(intent);
		}
		return super.onTouchEvent(event);
	}

}
