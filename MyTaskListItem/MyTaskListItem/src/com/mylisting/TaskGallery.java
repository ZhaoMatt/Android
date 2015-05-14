package com.mylisting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myTaskListItem.util.BmpName;
import com.myTaskListItem.util.ClickScreenInterface;
import com.myTaskListItem.util.FingerViewGroup;
import com.myTaskListItem.util.FingerViewGroupInterface;
import com.myTaskListItem.util.FreeFrameLayout;
import com.myTaskListItem.util.FreeFrameLayoutInterface;
import com.myTaskListItem.util.MemoryCenter;
import com.myTaskListItem.util.ShowIndexPage;
import com.myTaskListItem.util.TitleImgView;

public class TaskGallery extends Activity implements FingerViewGroupInterface,
		ClickScreenInterface, FreeFrameLayoutInterface {

	String[] strEnglishTitle = new String[] { "Good Phone", "Da Ge Da",
			"HTC Util", "Samsung Phone", "ZTE Phone" };
	String[] strTitle = new String[] { "好手机", "大哥大", "HTC", "三星", "中兴手机" };
	String[] strContent = new String[] { "第共轭的 是 个饿个 的我\n饿他好 个放到的的饿饿 是",
			"搜哦票根对哦iehg 哦饿哦根据诶覅欧文 我 的个个饿 ", "第的各位为她他个到的的饿饿 是",
			"欧共；打开为空欧文看i看好好个个个的饿饿 是", "平哦iu一他他人飞 吧你就就看看 了饿饿 是" };

	int[] topImageId = new int[] { R.drawable.testa, R.drawable.testb,
			R.drawable.testc, R.drawable.testd, R.drawable.teste };
	int[] bomContetOneImgId = new int[] { R.drawable.itemone_one,
			R.drawable.itemone_two, R.drawable.itemone_three,
			R.drawable.itemone_four, R.drawable.itemone_five,
			R.drawable.itemone_six };
	int[] bomContetTwoImgId = new int[] { R.drawable.itemtwo_one,
			R.drawable.itemtow_tow, R.drawable.itemtow_three,
			R.drawable.itemtow_four, R.drawable.itemone_three,
			R.drawable.itemtow_six };
	int[] bomContetThreeImgId = new int[] { R.drawable.itemthree_one,
			R.drawable.itemthree_tow, R.drawable.itemthree_three,
			R.drawable.itemthree_four, R.drawable.itemthree_five,
			R.drawable.itemthree_six };
	int[] bomContetFourImgId = new int[] { R.drawable.itemfour_one,
			R.drawable.itemfour_tow, R.drawable.itemfour_three,
			R.drawable.itemfour_four, R.drawable.itemfour_five,
			R.drawable.itemfour_six };
	int[] bomContetFiveImgId = new int[] { R.drawable.itemfive_one,
			R.drawable.itemfive_tow, R.drawable.itemfive_three,
			R.drawable.itemfive_four, R.drawable.itemfive_five,
			R.drawable.itemfive_six };

	int[][] bomContentImgId = new int[][] { bomContetOneImgId,
			bomContetTwoImgId, bomContetThreeImgId, bomContetFourImgId,
			bomContetFiveImgId };

	TextView titleEnglishView;
	TextView titleChinaView;
	TextView strContentView;
	FingerViewGroup fingerViewGroup;
	ShowIndexPage showUpIndexPage;
	ShowIndexPage showDownIndexPage;
	FreeFrameLayout freeLayout;

	Bitmap titleBmp;
	TitleImgView titleImage;

	Bitmap bomBmp;
	TitleImgView bomImage;

	Bitmap[] bmpArray = null;
	int[] iPicStatusArray = null;

	// 加载图片和释放图片资源用的(对应于列表)
	List<List<View>> listAllScreenView;
	private int iIndex;
	boolean thread_once_flag = true;// 确保线程只被开启一次
	private ManagerBmp managerBmp;

	private int iItem = 0; // 这个用来存放哪个主题

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		FrameLayout.LayoutParams rootParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT,
				FrameLayout.LayoutParams.FILL_PARENT);
		FrameLayout rootLayout = new FrameLayout(this);
		rootLayout.setBackgroundColor(Color.WHITE);
		rootLayout.setLayoutParams(rootParams);
		// 顶部标题图片
		titleBmp = BitmapFactory.decodeResource(this.getResources(),
				R.drawable.title);
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

		// 画廊元素题头
		FrameLayout topTitleLayout = new FrameLayout(this);
		FrameLayout.LayoutParams topTitleParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, 100);
		topTitleParams.gravity = Gravity.LEFT;
		topTitleParams.setMargins(0, 53, 0, 0);
		topTitleLayout.setLayoutParams(topTitleParams);
		rootLayout.addView(topTitleLayout);

		LinearLayout strLinearLayout = new LinearLayout(this);
		strLinearLayout.setOrientation(LinearLayout.VERTICAL);
		FrameLayout.LayoutParams strLinearParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		strLinearParams.gravity = Gravity.LEFT;
		strLinearParams.setMargins(20, 20, 0, 0);
		strLinearLayout.setLayoutParams(strLinearParams);
		topTitleLayout.addView(strLinearLayout);

		titleEnglishView = new TextView(this);
		titleEnglishView.setId(1);
		titleEnglishView.setTextSize(15);
		titleEnglishView.setTextColor(Color.BLACK);
		LinearLayout.LayoutParams strEnglishTitleParams = new LinearLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		titleEnglishView.setLayoutParams(strEnglishTitleParams);
		titleEnglishView.setText(strEnglishTitle[0]);
		strLinearLayout.addView(titleEnglishView);

		titleChinaView = new TextView(this);
		titleChinaView.setId(1);
		titleChinaView.setTextSize(12);
		titleChinaView.setTextColor(Color.BLACK);
		LinearLayout.LayoutParams strChinaTitleParams = new LinearLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT);
		titleChinaView.setLayoutParams(strChinaTitleParams);
		titleChinaView.setText(strTitle[0]);
		strLinearLayout.addView(titleChinaView);

		ImageView top_leftImgView = new ImageView(this);
		top_leftImgView.setScaleType(ScaleType.FIT_XY);
		top_leftImgView.setImageResource(R.drawable.gallery_top_left);
		FrameLayout.LayoutParams imgTopLeftParams = new FrameLayout.LayoutParams(
				28, 25);
		imgTopLeftParams.gravity = Gravity.LEFT;
		imgTopLeftParams.setMargins(420, 40, 0, 0);
		top_leftImgView.setLayoutParams(imgTopLeftParams);
		topTitleLayout.addView(top_leftImgView);

		// 画廊大图
		fingerViewGroup = new FingerViewGroup(this, this, this);
		FrameLayout.LayoutParams fingerParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, 300);
		fingerParams.gravity = Gravity.LEFT;
		fingerParams.topMargin = 153;
		fingerViewGroup.setLayoutParams(fingerParams);
		rootLayout.addView(fingerViewGroup);

		// 画廊大图的页数提示
		showUpIndexPage = new ShowIndexPage(this, topImageId.length);
		showUpIndexPage.setSelected(0);
		FrameLayout.LayoutParams showUpParams = new FrameLayout.LayoutParams(
				80, 10);
		showUpParams.setMargins(200, 460, 0, 0);
		showUpParams.gravity = Gravity.LEFT;
		showUpIndexPage.setLayoutParams(showUpParams);
		rootLayout.addView(showUpIndexPage);

		// 分割线一
		View one_view = new View(this);
		one_view.setBackgroundColor(Color.argb(255, 221, 221, 221));
		FrameLayout.LayoutParams one_viewParams = new FrameLayout.LayoutParams(
				460, 2);
		one_viewParams.setMargins(10, 463 + 10 + 10, 0, 0);
		one_viewParams.gravity = Gravity.LEFT;
		one_view.setLayoutParams(one_viewParams);
		rootLayout.addView(one_view);

		// 分割线中间的文字
		strContentView = new TextView(this);
		strContentView.setId(1);
		strContentView.setTextSize(8);
		strContentView.setTextColor(Color.BLACK);
		FrameLayout.LayoutParams strContentParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.WRAP_CONTENT, 30);
		strContentParams.gravity = Gravity.LEFT;
		strContentParams.setMargins(20, 463 + 10 + 10 + 10, 0, 0);
		strContentView.setLayoutParams(strContentParams);
		strContentView.setText(strContent[0]);
		rootLayout.addView(strContentView);

		// 分割线二
		View two_view = new View(this);
		two_view.setBackgroundColor(Color.argb(255, 221, 221, 221));
		FrameLayout.LayoutParams two_viewParams = new FrameLayout.LayoutParams(
				460, 2);
		two_viewParams.setMargins(10, 463 + 10 + 10 + 10 + 30 + 10, 0, 0);
		two_viewParams.gravity = Gravity.LEFT;
		two_view.setLayoutParams(two_viewParams);
		rootLayout.addView(two_view);

		// 底图每个主题下的内容图片
		freeLayout = new FreeFrameLayout(this, FreeFrameLayout.HORIZONTAL, this);
		FrameLayout.LayoutParams freeParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, 172);
		freeParams.gravity = Gravity.LEFT;
		freeParams.topMargin = 463 + 10 + 10 + 10 + 30 + 10 + 10;
		freeLayout.setLayoutParams(freeParams);

		int h = 0;
		for (int i = 0; i < bomContentImgId[iItem].length; i++) {
			FrameLayout.LayoutParams itemParams = new FrameLayout.LayoutParams(
					90, 90);
			itemParams.leftMargin = h;
			TitleImgView tiv = new TitleImgView(this);
			h += 10 + 90;
			freeLayout.addView(tiv, itemParams);
		}
		rootLayout.addView(freeLayout);

		// 最下面的分页提示
		showDownIndexPage = new ShowIndexPage(this, topImageId.length);
		showDownIndexPage.setSelected(0);
		FrameLayout.LayoutParams showDownParams = new FrameLayout.LayoutParams(
				80, 10);
		showDownParams.setMargins(200, 463 + 10 + 10 + 10 + 30 + 10 + 10 + 172
				+ 10, 0, 0);
		showDownParams.gravity = Gravity.LEFT;
		showDownIndexPage.setLayoutParams(showDownParams);
		rootLayout.addView(showDownIndexPage);

		setContentView(rootLayout);

		// 初始化大图
		bmpArray = new Bitmap[topImageId.length];
		iPicStatusArray = new int[topImageId.length];
		for (int i = 0; i < topImageId.length; i++) {
			iPicStatusArray[i] = 0; // 初始化为没有加载图片
			LinearLayout layout = new LinearLayout(this);
			TitleImgView imgView = new TitleImgView(this);
			// 写死子控件尺寸，不能让它切换图时自动缩放大小
			imgView.setMinimumHeight(300);
			imgView.setMinimumWidth(480);
			// 初始化前两张图时候，需要直接加载，因为随时应对手指左滑
			if (0 == i || 1 == i) {
				iPicStatusArray[i] = 1; // 加载图，所以设为1
				bmpArray[i] = MemoryCenter.getInstance().getLimitBmp(
						BmpName.TASKGALLERY_GALLERY_CONTENT + i,
						this.getBmp(topImageId[i]), i + "", i + "");
				imgView.setBitmap(bmpArray[i]);
				String[] bigImg_only_flag = new String[] {
						BmpName.TASKGALLERY_GALLERY_CONTENT + i, i + "" };
				imgView.setTag(bigImg_only_flag);
			} else {
				iPicStatusArray[i] = 0; // 没加载实际显示图，所以设为0
				// bmpArray[i] = // 此时也可以加载显示 "正在加载,请稍等" 等字样、且占 极少内存的图;
				// imgView.setImageBitmap(bmpArray[i]);
			}
			layout.addView(imgView);
			fingerViewGroup.addView(layout);
		}

		// 初始化底部的内容图片
		freeLayout.initScreenView();
		listAllScreenView = freeLayout.getAll_screen_View();
		List<View> preOne_screen_view = new ArrayList<View>(); // 记录上一屏有多少张图片
		int iBeforPresentBmps = 0;// 当前屏以前共有多少张图片
		for (int i = 0; i < listAllScreenView.size(); i++) {
			List<View> one_screen_view = listAllScreenView.get(i);
			// 给前两屏添加图片
			if (0 == i || 1 == i) {
				for (int j = 0; j < one_screen_view.size(); j++) {
					TitleImgView imgView_left = (TitleImgView) one_screen_view
							.get(j);
					Bitmap bmp = imgView_left.getBitmap();
					if (null == bmp) {
						imgView_left.setBitmap(MemoryCenter.getInstance()
								.getLimitBmp(
										BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT
												+ iItem + "" + i + "" + j,
										this.getBmp(bomContentImgId[iItem][j
												+ iBeforPresentBmps]),
										iItem + "" + i + "" + j,
										iItem + "" + i + "" + j));
						String[] bmp_only_flag = new String[] {
								BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT + iItem
										+ "" + i + "" + j,
								iItem + "" + i + "" + j };
						imgView_left.setTag(bmp_only_flag);
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
								BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT + iItem
										+ "" + i + "" + j,
								iItem + "" + i + "" + j);

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
	public void onCurChileCtrlIndex(int index, int direct) {
		// TODO Auto-generated method stub

		showUpIndexPage.setSelected(index);
		showDownIndexPage.setSelected(index);
		for (int i = 0; i < topImageId.length; i++) {
			TitleImgView imgView = (TitleImgView) ((LinearLayout) fingerViewGroup
					.getChildAt(i)).getChildAt(0);
			if (i == (index - 1) || i == index || i == index + 1) {
				// 属于当前显示子控件为中心的左右三个控件，需要保留内存，因为随时会被左或右滑动
				if (0 == iPicStatusArray[i]) { // 说明未加载图或者加载 内存小的提示图
					// freeBmp(bmpArray[i]); // 不管有没有，先释放该图
					bmpArray[i] = MemoryCenter.getInstance().getLimitBmp(
							BmpName.TASKGALLERY_GALLERY_CONTENT + i,
							this.getBmp(topImageId[i]), i + "", i + "");
					imgView.setBitmap(bmpArray[i]);
					iPicStatusArray[i] = 1; // 加载图后设置状态
					String[] bigImg_only_flag = new String[] {
							BmpName.TASKGALLERY_GALLERY_CONTENT + i, i + "" };
					imgView.setTag(bigImg_only_flag);
				}
			} else {
				if (1 == iPicStatusArray[i]) { // 说明已加载图，需要释放(此处释放后要加载提示图)
					// Bitmap s1 = getBmp("占内存极少的正在加载提示图");
					// imgView.setImageBitmap(s1);
					imgView.setBitmap(null);
					MemoryCenter.getInstance().FreeLimitBmp(
							BmpName.TASKGALLERY_GALLERY_CONTENT + i, i + "");
					// bmpArray[i] = s1;
					iPicStatusArray[i] = 0;
				}
			}
		}

		// 根据不同的主题来更换主题的英文名字和中文名字以及 主题对应的内容文字
		titleEnglishView.setText(strEnglishTitle[index]);
		titleChinaView.setText(strTitle[index]);
		strContentView.setText(strContent[index]);

		int preIndex = iItem;
		iItem = index;
		// 根据不同的主题来初始化内容视图
		if (preIndex != iItem) {
			if (null != freeLayout) {
				for (int i = 0; i < freeLayout.getChildCount(); i++) {
					TitleImgView imgView = (TitleImgView) freeLayout
							.getChildAt(i);
					String[] img_only_flag = (String[]) imgView.getTag();
					if (null != imgView.getBitmap() && null != img_only_flag) {
						MemoryCenter.getInstance().FreeLimitBmp(
								img_only_flag[0], img_only_flag[1]);
					}
				}
				freeLayout.removeAllViews();
				freeLayout.goToFist();
				int h = 0;
				for (int i = 0; i < bomContentImgId[iItem].length; i++) {
					FrameLayout.LayoutParams itemParams = new FrameLayout.LayoutParams(
							90, 90);
					itemParams.leftMargin = h;
					TitleImgView titleImgView = new TitleImgView(this);
					h += 10 + 90;
					freeLayout.addView(titleImgView, itemParams);
				}
				freeLayout.initScreenView();
				listAllScreenView = freeLayout.getAll_screen_View();
				List<View> preOne_screen_view = new ArrayList<View>(); // 记录上一屏有多少张图片
				int iBeforPresentBmps = 0;// 当前屏以前共有多少张图片
				for (int i = 0; i < listAllScreenView.size(); i++) {
					List<View> one_screen_view = listAllScreenView.get(i);
					// 给前两屏添加图片
					if (0 == i || 1 == i) {
						for (int j = 0; j < one_screen_view.size(); j++) {
							TitleImgView imgView_left = (TitleImgView) one_screen_view
									.get(j);
							Bitmap bmp = imgView_left.getBitmap();
							if (null == bmp) {
								imgView_left
										.setBitmap(MemoryCenter
												.getInstance()
												.getLimitBmp(
														BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT
																+ iItem
																+ ""
																+ i + "" + j,
														this.getBmp(bomContentImgId[iItem][j
																+ iBeforPresentBmps]),
														iItem + "" + i + "" + j,
														iItem + "" + i + "" + j));
								String[] bmp_only_flag = new String[] {
										BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT
												+ iItem + "" + i + "" + j,
										iItem + "" + i + "" + j };
								imgView_left.setTag(bmp_only_flag);
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
										BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT
												+ iItem + "" + i + "" + j,
										iItem + "" + i + "" + j);

							}
						}
					}
					preOne_screen_view = one_screen_view;
					iBeforPresentBmps += preOne_screen_view.size();
				}

			}
		}
	}

	@Override
	public void Click_Screen() {
		// TODO Auto-generated method stub

	}

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
							TitleImgView imgView_left = (TitleImgView) one_screen_view
									.get(j);
							Bitmap bmp = imgView_left.getBitmap();
							if (null == bmp) {
								imgView_left
										.setBitmap(MemoryCenter
												.getInstance()
												.getLimitBmp(
														BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT
																+ iItem
																+ ""
																+ i + "" + j,
														TaskGallery.this
																.getBmp(bomContentImgId[iItem][j
																		+ iBeforPresentBmps]),
														iItem + "" + i + "" + j,
														iItem + "" + i + "" + j));
								String[] bmp_only_flag = new String[] {
										BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT
												+ iItem + "" + i + "" + j,
										iItem + "" + i + "" + j };
								imgView_left.setTag(bmp_only_flag);
							}
						}
					} else {
						for (int j = 0; j < one_screen_view.size(); j++) {
							TitleImgView imgView_left = (TitleImgView) one_screen_view
									.get(j);
							Bitmap bmp = imgView_left.getBitmap();
							if (null != bmp) {
								imgView_left.setBitmap(null);
								MemoryCenter.getInstance().FreeLimitBmp(
										BmpName.TASKGALLERY_BOTTOMVIEW_CONTENT
												+ iItem + "" + i + "" + j,
										iItem + "" + i + "" + j);
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
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// 释放FreeFrameLayout中的图片资源
		for (int i = 0; i < freeLayout.getChildCount(); i++) {
			TitleImgView imgView = (TitleImgView) freeLayout.getChildAt(i);
			String[] img_only_flag = (String[]) imgView.getTag();
			if (null != imgView.getBitmap() && null != img_only_flag) {
				MemoryCenter.getInstance().FreeLimitBmp(img_only_flag[0],
						img_only_flag[1]);
			}
		}
		// 释放FingerViewGroup中的图片资源
		for (int i = 0; i < fingerViewGroup.getChildCount(); i++) {
			LinearLayout linear = (LinearLayout) fingerViewGroup.getChildAt(i);
			TitleImgView tiv = (TitleImgView) linear.getChildAt(0);
			String[] bigImg_only_flag = (String[]) tiv.getTag();
			if (null != tiv.getBitmap() && null != bigImg_only_flag) {
				MemoryCenter.getInstance().FreeLimitBmp(bigImg_only_flag[0],
						bigImg_only_flag[1]);
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
			Intent intent = new Intent(this, TaskProductImg.class);
			this.startActivity(intent);
		}
		return super.onTouchEvent(event);

	}
}
