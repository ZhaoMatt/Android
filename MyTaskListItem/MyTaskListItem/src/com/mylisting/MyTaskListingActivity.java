package com.mylisting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.myTaskListItem.util.BmpName;
import com.myTaskListItem.util.ClickScreenInterface;
import com.myTaskListItem.util.FingerViewGroup;
import com.myTaskListItem.util.FingerViewGroupInterface;
import com.myTaskListItem.util.FreeFrameLayout;
import com.myTaskListItem.util.FreeFrameLayoutInterface;
import com.myTaskListItem.util.MemoryCenter;
import com.myTaskListItem.util.ShowIndexPage;
import com.myTaskListItem.util.SumListItem;
import com.myTaskListItem.util.TitleImgView;

/**
 * 对ShowIndexPage.java做了修改
 * */

public class MyTaskListingActivity extends Activity implements
		FreeFrameLayoutInterface, FingerViewGroupInterface,
		ClickScreenInterface {

	String[] strTitle = new String[] { "好手机", "大哥大", "HTC", "三星", "中兴手机",
			"长虹手机", "海尔手机", "华为手机", "熊猫手机", "盗墓笔记", "天天面" };
	String[] strContent = new String[] { "第共轭的 是 个饿个 的我饿他好 个放到的的饿饿 是",
			"搜哦票根对哦iehg 哦饿哦根据诶覅欧文 我 的个个饿 ", "第的各位为她他个到的的饿饿 是",
			"欧共；打开为空欧文看i看好好个个个的饿饿 是", "平哦iu一他他人飞 吧你就就看看 了饿饿 是",
			"第共轭的 是 嗎你 吧吧好好uu 解決是", "第共轭的 是 个饿个 的我饿他好 个放到的的饿饿 是",
			"平哦iu一他他人飞 吧你就就看看 了oioiokjojlhlkhjhuhuhuhugyytfrd饿饿 是",
			"平哦iu一他他人飞 吧你就就看看 了饿饿 是", "平哦iu一他他人飞 吧你就就看看 了饿饿 是",
			"平哦iu一他他人飞 吧你就就看看 了饿饿 是" };
	int[] leftImageId = new int[] { R.drawable.image_lefth,
			R.drawable.image_lefth, R.drawable.image_leftw,
			R.drawable.image_lefth, R.drawable.image_leftw,
			R.drawable.image_lefth, R.drawable.image_leftw,
			R.drawable.image_lefth, R.drawable.image_lefth,
			R.drawable.image_leftw, R.drawable.image_leftw };

	// 加载上部图片用的
	int[] topImageId = new int[] { R.drawable.testa, R.drawable.testb,
			R.drawable.testc, R.drawable.testd, R.drawable.teste };
	Bitmap[] bmpArray = null;
	int[] iPicStatusArray = null;

	Bitmap titleBmp;
	TitleImgView titleImage;

	Bitmap bomBmp;
	TitleImgView bomImage;

	FingerViewGroup fingerViewGroup;

	ShowIndexPage showIndexPage;

	// 加载图片和释放图片资源用的(对应于列表)
	List<List<View>> listAllScreenView;
	private int iIndex;
	boolean thread_once_flag = true;// 确保线程只被开启一次
	private ManagerBmp managerBmp;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		FrameLayout.LayoutParams rootParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT);
		FrameLayout rootLayout = new FrameLayout(this);
		rootLayout.setLayoutParams(rootParams);

		// 顶部标题
		titleImage = new TitleImgView(this);
		FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(
				480, 53);
		titleImage.setLayoutParams(titleParams);
		rootLayout.addView(titleImage);

		// 加载上部图片
		fingerViewGroup = new FingerViewGroup(this, this, this);
		FrameLayout.LayoutParams fingerParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, 250);
		fingerParams.gravity = Gravity.LEFT;
		fingerParams.topMargin = 53;
		fingerViewGroup.setLayoutParams(fingerParams);
		rootLayout.addView(fingerViewGroup);

		bmpArray = new Bitmap[topImageId.length];
		iPicStatusArray = new int[topImageId.length];
		for (int i = 0; i < topImageId.length; i++) {
			iPicStatusArray[i] = 0; // 初始化为没有加载图片
			LinearLayout layout = new LinearLayout(this);
			ImageView imgView = new ImageView(this);
			// 写死子控件尺寸，不能让它切换图时自动缩放大小
			imgView.setMinimumHeight(250);
			imgView.setMinimumWidth(480);
			imgView.setMaxHeight(250);
			imgView.setMaxWidth(480);
			imgView.setScaleType(ScaleType.FIT_XY);
			// 初始化前两张图时候，需要直接加载，因为随时应对手指左滑
			if (0 == i || 1 == i) {
				iPicStatusArray[i] = 1; // 加载图，所以设为1

				bmpArray[i] = MemoryCenter.getInstance().getLimitBmp(
						BmpName.MYTASKLISTINGACTIVITY_GALLERY_CONTENT + i,
						this.getBmp(topImageId[i]), i + "", i + "");
				imgView.setImageBitmap(bmpArray[i]);
			} else {
				iPicStatusArray[i] = 0; // 没加载实际显示图，所以设为0
				// bmpArray[i] = // 此时也可以加载显示 "正在加载,请稍等" 等字样、且占 极少内存的图;
				// imgView.setImageBitmap(bmpArray[i]);
			}
			layout.addView(imgView);
			fingerViewGroup.addView(layout);
		}

		// 加载索引页视图
		showIndexPage = new ShowIndexPage(this, topImageId.length);
		showIndexPage.setSelected(0);
		FrameLayout.LayoutParams showParams = new FrameLayout.LayoutParams(80,
				10);
		showParams.setMargins(200, 250 + 20, 0, 0);
		showParams.gravity = Gravity.LEFT;
		showIndexPage.setLayoutParams(showParams);
		rootLayout.addView(showIndexPage);

		// 底部导航条
		bomImage = new TitleImgView(this);
		FrameLayout.LayoutParams bomParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, 55);
		bomParams.gravity = Gravity.LEFT;
		bomParams.topMargin = 745;
		bomImage.setLayoutParams(bomParams);
		rootLayout.addView(bomImage);

		// 添加列表内容视图
		FreeFrameLayout freeLayout = new FreeFrameLayout(this,
				FreeFrameLayout.VERTICAL, this);
		FrameLayout.LayoutParams freeParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, 800 - 53 - 55 - 250);
		freeParams.gravity = Gravity.LEFT;
		freeParams.topMargin = 53 + 250 + 3;
		freeLayout.setLayoutParams(freeParams);
		freeLayout.setBackgroundColor(Color.argb(255, 221, 221, 221));

		int h = 0;
		for (int i = 0; i < leftImageId.length; i++) {
			FrameLayout.LayoutParams itemParams = new FrameLayout.LayoutParams(
					FrameLayout.LayoutParams.FILL_PARENT, 150);
			itemParams.topMargin = h;
			SumListItem sItem = new SumListItem(this);
			sItem.getTitleTxtView().setText(strTitle[i]);
			sItem.getContentTxtView().setText(strContent[i]);
			sItem.getImageView_right().setImageResource(R.drawable.xiaojiantou);
			h += 3 + 150;
			freeLayout.addView(sItem, itemParams);
		}
		rootLayout.addView(freeLayout);
		setContentView(rootLayout);

		freeLayout.initScreenView();

		listAllScreenView = freeLayout.getAll_screen_View();

		List<View> preOne_screen_view = new ArrayList<View>(); // 记录上一屏有多少张图片
		int iBeforPresentBmps = 0;// 当前屏以前共有多少张图片
		for (int i = 0; i < listAllScreenView.size(); i++) {
			List<View> one_screen_view = listAllScreenView.get(i);
			// 给前两屏添加图片
			if (0 == i || 1 == i) {
				for (int j = 0; j < one_screen_view.size(); j++) {
					TitleImgView imgView_left = ((SumListItem) one_screen_view
							.get(j)).getTileImgView_left();
					Bitmap bmp = imgView_left.getBitmap();
					if (null == bmp) {
						imgView_left
								.setBitmap(MemoryCenter
										.getInstance()
										.getLimitBmp(
												BmpName.MYTASKLISTINGACTIVITY_LIST_CONTENT
														+ i + "" + j,
												this.getBmp(leftImageId[j
														+ iBeforPresentBmps]),
												i + "" + j, i + "" + j));
					}
				}
				// 也可以在这里给其添加非常小的提示图片正在加载的小图标
			} else {
				for (int j = 0; j < one_screen_view.size(); j++) {
					TitleImgView imgView_left = ((SumListItem) one_screen_view
							.get(j)).getTileImgView_left();
					Bitmap bmp = imgView_left.getBitmap();
					if (null != bmp) {
						imgView_left.setBitmap(null);
						MemoryCenter.getInstance().FreeLimitBmp(
								BmpName.MYTASKLISTINGACTIVITY_LIST_CONTENT + i
										+ "" + j, i + "" + j);
					}
				}
			}
			preOne_screen_view = one_screen_view;
			iBeforPresentBmps += preOne_screen_view.size();
		}
		managerBmp = new ManagerBmp();
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

	// @Override
	// protected void onStop() {
	// // TODO Auto-generated method stub
	// super.onStop();
	// titleImage.setBitmap(null);
	// MemoryCenter.getInstance().FreeLimitBmp(BmpName.APP_TITLE_IMG,
	// "Listing_title");
	//
	// bomImage.setBitmap(null);
	// MemoryCenter.getInstance().FreeLimitBmp(BmpName.APP_BOM_IMG,
	// "Listing_bom");
	// }

	@Override
	public void onCurChileCtrlScreen(int index, int direct) {
		// TODO Auto-generated method stub

		int preIndex = this.iIndex;
		this.iIndex = index;
		if (0 != direct) {// 当是滑动时执行操作
			if (thread_once_flag) {
				managerBmp.start(); // 守护线程只开启一次
				thread_once_flag = false;
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
				while (i < listAllScreenView.size() && manaBmp_flag) {
					List<View> one_screen_view = listAllScreenView.get(i);
					// 属于当前屏幕中心的上下三个屏幕，需要保留内存，因为随时会被上或下滑动
					if (iIndex - 1 == i || iIndex == i || iIndex + 1 == i) {
						for (int j = 0; j < one_screen_view.size(); j++) {
							TitleImgView tivView = ((SumListItem) one_screen_view
									.get(j)).getTileImgView_left();
							Bitmap bmp = tivView.getBitmap();
							if (null == bmp) {
								bmp = MemoryCenter
										.getInstance()
										.getLimitBmp(
												BmpName.MYTASKLISTINGACTIVITY_LIST_CONTENT
														+ i + "" + j,
												MyTaskListingActivity.this
														.getBmp(leftImageId[j
																+ iBeforPresentBmps]),
												i + "" + j, i + "" + j);
								tivView.setBitmap(bmp);
							}
						}
					} else {
						for (int j = 0; j < one_screen_view.size(); j++) {
							TitleImgView tivView = ((SumListItem) one_screen_view
									.get(j)).getTileImgView_left();
							Bitmap bmp = tivView.getBitmap();
							tivView.setBitmap(null);
							if (null != bmp) {
								// System.out.println("第" + i + "屏开始释放内存");
								MemoryCenter
										.getInstance()
										.FreeLimitBmp(
												BmpName.MYTASKLISTINGACTIVITY_LIST_CONTENT
														+ i + "" + j,
												i + "" + j);
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
        Log.e("-----",""+index);		
		showIndexPage.setSelected(index);
		for (int i = 0; i < topImageId.length; i++) {
			ImageView imgView = (ImageView) ((LinearLayout) fingerViewGroup
					.getChildAt(i)).getChildAt(0);
			if (i == (index - 1) || i == index || i == index + 1) {
				// 属于当前显示子控件为中心的左右三个控件，需要保留内存，因为随时会被左或右滑动
				if (0 == iPicStatusArray[i]) { // 说明未加载图或者加载 内存小的提示图
					// freeBmp(bmpArray[i]); // 不管有没有，先释放该图,这里释放小图用的
					bmpArray[i] = MemoryCenter.getInstance().getLimitBmp(
							BmpName.MYTASKLISTINGACTIVITY_GALLERY_CONTENT + i,
							this.getBmp(topImageId[i]), i + "", i + "");
					imgView.setImageBitmap(bmpArray[i]);
					iPicStatusArray[i] = 1; // 加载图后设置状态
				}
			} else {
				if (1 == iPicStatusArray[i]) { // 说明已加载图，需要释放(此处释放后要加载提示图)
					// Bitmap s1 = getBmp("占内存极少的正在加载提示图");
					// imgView.setImageBitmap(s1);
					imgView.setImageBitmap(null);
					MemoryCenter.getInstance().FreeLimitBmp(
							BmpName.MYTASKLISTINGACTIVITY_GALLERY_CONTENT + i,
							i + "");
					// bmpArray[i] = s1;
					iPicStatusArray[i] = 0;
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
			Intent intent = new Intent(this, TaskGallery.class);
			this.startActivity(intent);
		}
		return super.onTouchEvent(event);
	}

}