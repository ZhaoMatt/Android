package com.mylisting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myTaskListItem.util.BmpName;
import com.myTaskListItem.util.ClickScreenInterface;
import com.myTaskListItem.util.CollectItemLayout;
import com.myTaskListItem.util.FingerViewGroup;
import com.myTaskListItem.util.FingerViewGroupInterface;
import com.myTaskListItem.util.FreeFrameLayout;
import com.myTaskListItem.util.FreeFrameLayoutInterface;
import com.myTaskListItem.util.MemoryCenter;
import com.myTaskListItem.util.TitleImgView;

//在这里明白了，View的注册事件和布局无关，就是你给一个View注册了事件，那么不管将这个View放到哪个布局中，这个注册事件都是不变的
//也就是说，View的注册事件是跟着View走的
public class CollectionActivity extends Activity implements
		FreeFrameLayoutInterface, FingerViewGroupInterface,
		ClickScreenInterface {

	int[] iCollectImgId = new int[] { R.drawable.c_test1, R.drawable.c_test2,
			R.drawable.c_test3, R.drawable.c_test4, R.drawable.c_test5,
			R.drawable.c_test6, R.drawable.c_test7, R.drawable.c_test8,
			R.drawable.c_test9, R.drawable.c_test10, R.drawable.c_test11,
			R.drawable.c_test12, R.drawable.c_test13, R.drawable.c_test14,
			R.drawable.c_test15, R.drawable.c_test16 }; // 用来存放图片资源ID
	List<CollectItemLayout> list_view = new ArrayList<CollectItemLayout>(); // 用来存放每一项的视图对象
	FreeFrameLayout freeFrameLayout; // 滚动布局
	List<List<View>> listAllScreenView;
	private int iIndex;
	private ManagerBmp managerBmp;

	FingerViewGroup fingerViewGroup;
	LinearLayout linearLayout;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		fingerViewGroup = new FingerViewGroup(this, this, this);

		// 声明滚动布局
		freeFrameLayout = new FreeFrameLayout(this, FreeFrameLayout.VERTICAL,
				this);
		freeFrameLayout.setMinimumHeight(800);
		freeFrameLayout.setMinimumWidth(480);
		linearLayout = new LinearLayout(this);
		linearLayout.setMinimumHeight(800);
		linearLayout.setMinimumWidth(480);

		TextView textView = new TextView(this);
		textView.setText("hello i am children screen");
		LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
				480, 50);
		textView.setLayoutParams(textParams);
		linearLayout.addView(textView);
		textView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(CollectionActivity.this, NewSubscibe.class);
				CollectionActivity.this.startActivity(intent);
			}
		});

		// 以下是布局出滚动界面的视图
		// --------------------------------------------------------------------
		int left = 15;
		int top = 15;
		for (int i = 0; i < iCollectImgId.length; i++) {
			// 实例化出收藏界面的 项的对象视图
			CollectItemLayout collectItemLayout = new CollectItemLayout(this);
			list_view.add(collectItemLayout);// 将这个对象给存放起来,以供删除后再重复利用这个对象并新实例化个布局属性来布局
			collectItemLayout.getImgDeletFlag().setBitmap(
					this.getBmp(R.drawable.deletflag));
			collectItemLayout.getImgDeletFlag().setTag(collectItemLayout);// 将收藏的整个视图对象放到它的删除标识的Tag属性中
			FrameLayout.LayoutParams collectParams = new FrameLayout.LayoutParams(
					140, 140);
			collectParams.leftMargin = left;
			collectParams.topMargin = top;
			freeFrameLayout.addView(collectItemLayout, collectParams);
			left += 15 + collectParams.width;
			if (left >= 480) {
				top += 15 + collectParams.height;
				left = 15;
			}

			// 给小的删除图片注册事件
			collectItemLayout.getImgDeletFlag().setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							TitleImgView item = (TitleImgView) v;
							CollectionActivity.this.freeBmp(item.getBitmap());
							freeFrameLayout.removeAllViews();// 清空滚动布局中的所有子View，但是我们先前已经将子View对象个存储起来了，所有还可以在复用子View对象的
							list_view.remove(v.getTag());// 根据删除标识中的对象来删除存储起来的子View对象
							CollectionActivity.this.layoutView();// 执行重新布局方法
						}
					});
		}
		// 初始化滚动布局
		freeFrameLayout.initScreenView();
		// -----------------------------------------------------------------------
		fingerViewGroup.addView(freeFrameLayout);
		fingerViewGroup.addView(linearLayout);
		this.setContentView(fingerViewGroup);
		listAllScreenView = freeFrameLayout.getAll_screen_View();
		// 初始化图片资源
		List<View> preOne_screen_view = new ArrayList<View>(); // 记录上一屏有多少张图片
		int iBeforPresentBmps = 0;// 当前屏以前共有多少张图片
		for (int i = 0; i < listAllScreenView.size(); i++) {
			List<View> one_screen_view = listAllScreenView.get(i);
			// 给前两屏添加图片
			if (0 == i || 1 == i) {
				for (int j = 0; j < one_screen_view.size(); j++) {
					TitleImgView imgView_item = ((CollectItemLayout) one_screen_view
							.get(j)).getImgItemView();
					Bitmap bmp = imgView_item.getBitmap();
					if (null == bmp) {
						imgView_item.setBitmap(MemoryCenter.getInstance()
								.getLimitBmp(
										BmpName.COLLECTIONACTIVITY_CONTENT + i
												+ "" + j,
										this.getBmp(iCollectImgId[j
												+ iBeforPresentBmps]),
										i + "" + j, i + "" + j));

					}
				}
				// 也可以在这里给其添加非常小的提示图片正在加载的小图标
			} else {
				for (int j = 0; j < one_screen_view.size(); j++) {
					TitleImgView imgView_item = ((CollectItemLayout) one_screen_view
							.get(j)).getImgItemView();
					Bitmap bmp = imgView_item.getBitmap();
					if (null != bmp) {
						imgView_item.setBitmap(null);
						MemoryCenter.getInstance()
								.FreeLimitBmp(
										BmpName.COLLECTIONACTIVITY_CONTENT + i
												+ "" + j, i + "" + j);
					}
				}
			}
			preOne_screen_view = one_screen_view;
			iBeforPresentBmps += preOne_screen_view.size();
		}

		managerBmp = new ManagerBmp();
	}

	public void layoutView() {
		int left = 15;
		int top = 15;
		for (int i = 0; i < list_view.size(); i++) {
			FrameLayout.LayoutParams collectParams = new FrameLayout.LayoutParams(
					140, 140);
			collectParams.leftMargin = left;
			collectParams.topMargin = top;
			freeFrameLayout.addView(list_view.get(i), collectParams);
			left += 15 + collectParams.width;
			if (left >= 480) {
				top += 15 + collectParams.height;
				left = 15;
			}
		}
		// 初始化滚动布局
		freeFrameLayout.initScreenView();
	}

	public Bitmap getBmp(int id) {
		return BitmapFactory.decodeResource(this.getResources(), id);
	}

	public void freeBmp(Bitmap bmp) {
		if (null != bmp) {
			bmp.recycle();
			bmp = null;
		}
	}

	@Override
	public void onCurChileCtrlScreen(int index, int direct) {
		// TODO Auto-generated method stub
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

	// 当不在屏幕上显示的时候就只保留当前屏幕上的图片资源，其它两屏图片资源释放,并且终止图片释放和加载线程
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		managerBmp.query_flag = false;
		managerBmp.manaBmp_flag = false;
		// 释放FreeFrameLayout布局中的图片资源
		if (null != this.listAllScreenView) {
			for (int i = 0; i < listAllScreenView.size(); i++) {
				List<View> one_screen_view = listAllScreenView.get(i);
				if (iIndex - 1 == i || iIndex + 1 == i) {
					for (int j = 0; j < one_screen_view.size(); j++) {
						TitleImgView imgView_item = ((CollectItemLayout) one_screen_view
								.get(j)).getImgItemView();
						Bitmap bmp = imgView_item.getBitmap();
						imgView_item.setBitmap(null);
						if (null != bmp) {
							MemoryCenter.getInstance().FreeLimitBmp(
									BmpName.COLLECTIONACTIVITY_CONTENT + i + ""
											+ j, i + "" + j);
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
		if (null != this.listAllScreenView) {
			for (int i = 0; i < listAllScreenView.size(); i++) {
				List<View> one_screen_view = listAllScreenView.get(i);
				if (iIndex == i) {
					for (int j = 0; j < one_screen_view.size(); j++) {
						TitleImgView imgView_item = ((CollectItemLayout) one_screen_view
								.get(j)).getImgItemView();
						Bitmap bmp = imgView_item.getBitmap();
						if (null != bmp) {
							MemoryCenter.getInstance().FreeLimitBmp(
									BmpName.COLLECTIONACTIVITY_CONTENT + i + ""
											+ j, i + "" + j);
						}
					}
				}
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
				while (i < listAllScreenView.size() && manaBmp_flag) {
					List<View> one_screen_view = listAllScreenView.get(i);
					// 属于当前屏幕中心的上下三个屏幕，需要保留内存，因为随时会被上或下滑动
					if (iIndex - 1 == i || iIndex == i || iIndex + 1 == i) {
						for (int j = 0; j < one_screen_view.size(); j++) {
							TitleImgView imgView_item = ((CollectItemLayout) one_screen_view
									.get(j)).getImgItemView();
							Bitmap bmp = imgView_item.getBitmap();
							if (null == bmp) {
								imgView_item.setBitmap(CollectionActivity.this
										.getBmp(iCollectImgId[j
												+ iBeforPresentBmps]));
							}
						}
					} else {
						for (int j = 0; j < one_screen_view.size(); j++) {
							TitleImgView imgView_item = ((CollectItemLayout) one_screen_view
									.get(j)).getImgItemView();
							Bitmap bmp = imgView_item.getBitmap();
							if (null != bmp) {
								CollectionActivity.this.freeBmp(bmp);
								imgView_item.setBitmap(null);
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
		}
	}

	@Override
	public void Click_Screen() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCurChileCtrlIndex(int index, int direct) {
		// TODO Auto-generated method stub

	}
}
