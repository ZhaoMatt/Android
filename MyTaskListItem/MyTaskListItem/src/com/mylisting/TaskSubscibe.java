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
import android.widget.LinearLayout;

import com.myTaskListItem.util.BmpName;
import com.myTaskListItem.util.FreeFrameLayoutInterface;
import com.myTaskListItem.util.FreeLinearLayout;
import com.myTaskListItem.util.MemoryCenter;
import com.myTaskListItem.util.SumListItem_BomImg;
import com.myTaskListItem.util.SumListItem_BomImgTitle;
import com.myTaskListItem.util.TitleImgView;

//注意，在这里没有将标题视图考虑在图片释放中，
public class TaskSubscibe extends Activity implements FreeFrameLayoutInterface {

	// 标题视图资源
	int[] leftImg_title = new int[] { 0, 0, 0, R.drawable.riqi, 0, 0,
			R.drawable.riqi, 0, R.drawable.riqi, 0, R.drawable.riqi };
	String[] strTitle_title = new String[] { "", "", "", "测试标题视图一", "", "",
			"测试标题视图二", "", "测试标题视图三", "", "测试标题视图四" };

	// 内容视图资源
	String[] strTitle_content = new String[] { "好手机", "大哥大", "HTC", "三星",
			"中兴手机", "长虹手机", "海尔手机", "华为手机", "熊猫手机", "盗墓笔记", "天天面" };
	String[] strContent_content = new String[] { "第共轭的 是 个饿个 的我饿他好 个放到的的饿饿 是",
			"搜哦票根对哦iehg 哦饿哦根据诶覅欧文 我 的个个饿 ", "第的各位为她他个到的的饿饿 是",
			"欧共；打开为空欧文看i看好好个个个的饿饿 是", "平哦iu一他他人飞 吧你就就看看 了饿饿 是",
			"第共轭的 是 嗎你 吧吧好好uu 解決是", "第共轭的 是 个饿个 的我饿他好 个放到的的饿饿 是",
			"平哦iu一他他人飞 吧你就就看看 了uhuhugyytfrd饿饿 是", "平哦iu一他他人飞 吧你就就看看 了饿饿 是",
			"平哦iu一他他人飞 吧你就就看看 了饿饿 是", "平哦iu一他他人飞 吧你就就看看 了饿饿 是" };

	int[] bomImgId_content = new int[] { 0, 0, R.drawable.dingyuelistbom, 0,
			R.drawable.dylistbom2, 0, 0, R.drawable.dylistbom3, 0,
			R.drawable.dylistbom4, 0 };
	int[] leftImgId_content = new int[] { R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon };
	private int[] iNumber = new int[] { 0, 0, 0, 1, 0, 0, 1, 0, 1, 0, 1 };// 来判断是否有标题视图

	private List<Object> list_title_content = new ArrayList<Object>(); // 里面存放每个标题视图所包含的内容视图的List

	private int iAllVisibleViewHeight_px;// 所有可见的控件的高度和

	FreeLinearLayout freeLayout;
	Bitmap titleBmp;
	TitleImgView titleImage;

	Bitmap bomBmp;
	TitleImgView bomImage;

	// 加载图片和释放图片资源用的(对应于列表)
	List<List<View>> listAllScreenView;
	private int iIndex;
	private ManagerBmp managerBmp_subscibe;

	// int moveOpenDistance = 0;// 记录标题视图展开时移动的距离

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		LinearLayout rootLayout = new LinearLayout(this);
		rootLayout.setOrientation(LinearLayout.VERTICAL);
		rootLayout.setBackgroundColor(Color.argb(255, 164, 164, 164));
		rootLayout.setLayoutParams(rootParams);
		// 顶部标题
		titleImage = new TitleImgView(this);
		FrameLayout.LayoutParams titleParams = new FrameLayout.LayoutParams(
				480, 53);
		titleImage.setLayoutParams(titleParams);
		rootLayout.addView(titleImage);

		// 添加列表内容视图
		freeLayout = new FreeLinearLayout(this, FreeLinearLayout.VERTICAL, this);
		LinearLayout.LayoutParams freeParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 692);
		freeLayout.setLayoutParams(freeParams);
		freeLayout.setBackgroundColor(Color.argb(255, 164, 164, 164));

		int h = 0;
		List<Object> one_title_content = null;// 存放标题视图所包含的内容视图
		boolean once_flag = false;
		for (int i = 0; i < strTitle_content.length; i++) {
			// 添加题头视图并给其注册单击事件
			if (0 != iNumber[i]) {
				one_title_content = new ArrayList<Object>();
				once_flag = true;
				SumListItem_BomImgTitle sbiTitle = new SumListItem_BomImgTitle(
						this);
				sbiTitle.setTag(i);// 将这个标题位于第几行存储起来，这样就可以根据这个存储起来的值来去判断它有几个内容视图
				LinearLayout.LayoutParams sbiTitleParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 100);
				sbiTitleParams.topMargin = h;
				sbiTitle.getImgViewLeft().setBitmap(
						this.getBmp(leftImg_title[i]));
				sbiTitle.getTextVewTitle().setText(strTitle_title[i]);
				sbiTitle.getImgViewRight().setBitmap(
						this.getBmp(R.drawable.xiaojiantou));
				freeLayout.addView(sbiTitle, sbiTitleParams);
				iAllVisibleViewHeight_px += 100;

				// 给每个标题视图注册单击事件，
				sbiTitle.setOnTouchListener(new View.OnTouchListener() {
					private boolean onTouchFlag = true;// 来确定是展开内容视图还是收起内容视图

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (MotionEvent.ACTION_UP == event.getAction()) {
							int containItemHeight = 0;// 内容视图的总高度
							if (onTouchFlag) {// 展开内容视图
								List list_content = (List) list_title_content
										.get(Integer.parseInt(v.getTag() + ""));
								for (int j = 0; j < list_content.size(); j++) {
									((SumListItem_BomImg) list_content.get(j))
											.setVisibility(View.VISIBLE);
									containItemHeight += ((SumListItem_BomImg) list_content
											.get(j)).getLayoutParams().height;
									iAllVisibleViewHeight_px += ((SumListItem_BomImg) list_content
											.get(j)).getLayoutParams().height;
								}
								freeLayout
										.setIallViewHeight_px(iAllVisibleViewHeight_px);

								// 如果标题视图所包含的内容视图高度大于屏幕，则是标题视图滚到屏幕顶端
								if (containItemHeight > freeLayout
										.getLayoutParams().height) {
									int moveDistance = v.getTop()
											- freeLayout.getIsScroller_y();
									freeLayout.snapToPotion(moveDistance);
									// moveOpenDistance = moveDistance;
								} else { // 如果标题视图包含的内容视图小于等于屏幕大小，则滚动到内容视图的底部

									/*
									 * v.getBottom() -
									 * freeLayout.getIsScroller_y
									 * ()通过这个能够计算出标题视图相对于当前显示的屏幕的位置
									 * 
									 * freeLayout.getLayoutParams().height - (v
									 * .getBottom() - freeLayout
									 * .getIsScroller_y()) 就得出了v距离当前显示的屏幕底部有多少距离
									 */

									int viewRelativeScreenBom = freeLayout
											.getLayoutParams().height
											- (v.getBottom() - freeLayout
													.getIsScroller_y());
									if (viewRelativeScreenBom < containItemHeight) {
										if (viewRelativeScreenBom < 0) {
											freeLayout.snapToPotion(containItemHeight
													+ freeLayout
															.getIsScroller_y()
													- viewRelativeScreenBom);
											// moveOpenDistance =
											// containItemHeight
											// + freeLayout
											// .getIsScroller_y()
											// - viewRelativeScreenBom;
										} else if (viewRelativeScreenBom
												+ containItemHeight
												+ v.getHeight() > 800) {
											freeLayout.snapToPotion(containItemHeight
													+ freeLayout
															.getIsScroller_y()
													- viewRelativeScreenBom);
										} else {
											freeLayout.snapToPotion(containItemHeight
													+ freeLayout
															.getIsScroller_y());
											// moveOpenDistance =
											// containItemHeight
											// + freeLayout
											// .getIsScroller_y();
										}

									}
								}
								onTouchFlag = false;
							} else {// 关闭内容视图
								List list_content = (List) list_title_content
										.get(Integer.parseInt(v.getTag() + ""));
								for (int j = 0; j < list_content.size(); j++) {
									((SumListItem_BomImg) list_content.get(j))
											.setVisibility(View.GONE);
									iAllVisibleViewHeight_px -= ((SumListItem_BomImg) list_content
											.get(j)).getLayoutParams().height;
								}
								freeLayout
										.setIallViewHeight_px(iAllVisibleViewHeight_px);
								onTouchFlag = true;

								if (iAllVisibleViewHeight_px
										- freeLayout.getIsScroller_y() < 692) {
									freeLayout
											.snapToPotion(iAllVisibleViewHeight_px - 692);
								}

								// freeLayout.snapToPotion(freeLayout
								// .getIsScroller_y() - moveOpenDistance);

								// System.out.println(-moveOpenDistance
								// + "收缩时移动的距离");
							}
						}
						freeLayout
								.setIallViewHeight_px(iAllVisibleViewHeight_px);
						return true;
					}
				});
			} else {
				list_title_content.add(0);
			}

			// 添加内容视图
			LinearLayout.LayoutParams itemParams;
			SumListItem_BomImg sItem = new SumListItem_BomImg(this);
			sItem.getTitleTxtView().setText(strTitle_content[i]);
			sItem.getContentTxtView().setText(strContent_content[i]);
			sItem.getImageView_right().setImageResource(R.drawable.xiaojiantou);
			if (0 != bomImgId_content[i]) {// 如果内容视图有下面的图片，就让它可见，但是不给它设置图片值
				sItem.getTileImgView_bom().setVisibility(View.VISIBLE);
				itemParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 150 + 100);
				itemParams.topMargin = h;
			} else {
				sItem.getTileImgView_bom().setVisibility(View.GONE);
				itemParams = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 150);
				itemParams.topMargin = h;
			}
			if (i < 3) { // 这里暂时定为是3个
				sItem.setVisibility(View.VISIBLE);
				freeLayout.addView(sItem, itemParams);
				iAllVisibleViewHeight_px += itemParams.height
						+ itemParams.topMargin;
			} else {// 当i大于3后，下面的内容视图都不可见了
				sItem.setVisibility(View.GONE);
				freeLayout.addView(sItem, itemParams);
				if (null != one_title_content) {
					one_title_content.add(sItem);
				}
			}
			if (once_flag) {
				list_title_content.add(one_title_content);
			}
			once_flag = false;
			h = 1;
		}
		freeLayout.initScreenView();
		freeLayout.setIallViewHeight_px(iAllVisibleViewHeight_px);// 这个放到initScreenView()方法下面比较好
		rootLayout.addView(freeLayout);
		setContentView(rootLayout);

		// 底部导航条
		bomImage = new TitleImgView(this);
		FrameLayout.LayoutParams bomParams = new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.FILL_PARENT, 55);
		bomParams.gravity = Gravity.LEFT;
		bomParams.topMargin = 745;
		bomImage.setLayoutParams(bomParams);
		rootLayout.addView(bomImage);

		// 初始化内容视图，在这里说明下，即使不显示的内容视图，也会初始化的图片（给其赋值图片资源 ）
		listAllScreenView = freeLayout.getAll_screen_View();

		List<View> preOne_screen_view = new ArrayList<View>(); // 记录上一屏有多少张图片
		int iBeforPresentBmps = 0;// 当前屏以前共有多少张图片
		for (int i = 0; i < listAllScreenView.size(); i++) {
			List<View> one_screen_view = listAllScreenView.get(i);
			// 给前两屏添加图片
			if (0 == i || 1 == i) {
				for (int j = 0; j < one_screen_view.size(); j++) {
					if (null == one_screen_view.get(j).getTag()) {// 由于标题视图都在其Tag属性中放了一个int型数据
						TitleImgView imgView_left = ((SumListItem_BomImg) one_screen_view
								.get(j)).getTileImgView_left();
						TitleImgView imgView_bom = ((SumListItem_BomImg) one_screen_view
								.get(j)).getTileImgView_bom();

						Bitmap bmp_left = imgView_left.getBitmap();
						Bitmap bmp_bom = imgView_bom.getBitmap();

						if (null == bmp_left) {
							imgView_left.setBitmap(MemoryCenter.getInstance()
									.getLimitBmp(
											BmpName.TASKSUBSCIBE_CONTENT_LEFT
													+ i + "" + j,
											this.getBmp(leftImgId_content[j
													+ iBeforPresentBmps]),
											i + "" + j, i + "" + j));
						}
						if (null == bmp_bom
								&& View.VISIBLE == imgView_bom.getVisibility()
								&& 0 != this.bomImgId_content[j
										+ iBeforPresentBmps]) {
							imgView_bom.setBitmap(MemoryCenter.getInstance()
									.getLimitBmp(
											BmpName.TASKSUBSCIBE_CONTENT_BOM
													+ i + "" + j,
											this.getBmp(bomImgId_content[j
													+ iBeforPresentBmps]),
											i + "" + j, i + "" + j));
						}
					} else {
						iBeforPresentBmps--;// 不计算标题视图
					}

				}
				// 也可以在这里给其添加非常小的提示图片正在加载的小图标
			} else {
				for (int j = 0; j < one_screen_view.size(); j++) {
					if (null == one_screen_view.get(j).getTag()) {// 由于标题视图都在其Tag属性中放了一个int型数据
						TitleImgView imgView_left = ((SumListItem_BomImg) one_screen_view
								.get(j)).getTileImgView_left();
						TitleImgView imgView_bom = ((SumListItem_BomImg) one_screen_view
								.get(j)).getTileImgView_bom();
						Bitmap bmp_left = imgView_left.getBitmap();
						Bitmap bmp_bom = imgView_bom.getBitmap();
						if (null != bmp_left) {
							imgView_left.setBitmap(null);
							MemoryCenter.getInstance().FreeLimitBmp(
									BmpName.TASKSUBSCIBE_CONTENT_LEFT + i + ""
											+ j, i + "" + j);
						}
						if (null != bmp_bom) {
							imgView_bom.setBitmap(null);
							MemoryCenter.getInstance().FreeLimitBmp(
									BmpName.TASKSUBSCIBE_CONTENT_BOM + i + ""
											+ j, i + "" + j);
						}
					} else {
						iBeforPresentBmps--;// 不计算标题视图
					}
				}
			}
			preOne_screen_view = one_screen_view;
			iBeforPresentBmps += preOne_screen_view.size();
		}
		managerBmp_subscibe = new ManagerBmp();
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

	@Override
	public void onCurChileCtrlScreen(int index, int direct) {
		// TODO Auto-generated method stub
		// int preIndex = this.iIndex;
		this.iIndex = index;
		if (0 != direct) {// 当是滑动时执行操作
			if (!managerBmp_subscibe.isAlive()) {
				managerBmp_subscibe = new ManagerBmp(); // 注意这里必须在重新赋值;如果不重新赋值的话线程结束后，这个引用指向的是已经结束了的线程对象（已经在内存中不存在咯）
				managerBmp_subscibe.query_flag = true;
				managerBmp_subscibe.manaBmp_flag = true;
				managerBmp_subscibe.start(); // 守护线程只开启一次
			}

			// 在这里去掉这个是因为当点击标题视图后就会引发变化来对图片进行操作，所以要去除掉这个判断,使线程随时监视图片的变化
			// if (preIndex != index) { // 当屏幕变化时使守护线程内部循环开始执行
			managerBmp_subscibe.manaBmp_flag = true;
			// }
		}
	}

	// 当不在屏幕上显示的时候就只保留当前屏幕上的图片资源，其它两屏图片资源释放,并且终止图片释放和加载线程
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		managerBmp_subscibe.query_flag = false;
		managerBmp_subscibe.manaBmp_flag = false;
		// 释放FreeFrameLayout布局中的图片资源
		if (null != this.listAllScreenView) {
			for (int i = 0; i < listAllScreenView.size(); i++) {
				int iGoneViewHeight_px = 0;
				List<View> one_screen_view = listAllScreenView.get(i);
				if (iIndex - 1 == i || iIndex + 1 == i) {
					for (int j = 0; j < one_screen_view.size(); j++) {
						if (null == one_screen_view.get(j).getTag()) {// 由于标题视图都在其Tag属性中放了一个int型数据
							SumListItem_BomImg sumListItem_bomimg = (SumListItem_BomImg) one_screen_view
									.get(j);
							TitleImgView imgView_left = sumListItem_bomimg
									.getTileImgView_left();
							TitleImgView imgView_bom = sumListItem_bomimg
									.getTileImgView_bom();
							imgView_left.setBitmap(null);
							imgView_bom.setBitmap(null);
							MemoryCenter.getInstance().FreeLimitBmp(
									BmpName.TASKSUBSCIBE_CONTENT_LEFT + i + ""
											+ j, i + "" + j);
							MemoryCenter.getInstance().FreeLimitBmp(
									BmpName.TASKSUBSCIBE_CONTENT_BOM + i + ""
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
		managerBmp_subscibe.query_flag = false;
		managerBmp_subscibe.manaBmp_flag = false;
		// 释放FreeFrameLayout布局中的图片资源
		if (null != this.listAllScreenView) {
			for (int i = 0; i < listAllScreenView.size(); i++) {
				List<View> one_screen_view = listAllScreenView.get(i);
				if (iIndex == i) {
					for (int j = 0; j < one_screen_view.size(); j++) {
						if (null == one_screen_view.get(j).getTag()) {// 由于标题视图都在其Tag属性中放了一个int型数据
							TitleImgView imgView_left = ((SumListItem_BomImg) one_screen_view
									.get(j)).getTileImgView_left();
							TitleImgView imgView_bom = ((SumListItem_BomImg) one_screen_view
									.get(j)).getTileImgView_bom();
							imgView_left.setBitmap(null);
							imgView_bom.setBitmap(null);
							MemoryCenter.getInstance().FreeLimitBmp(
									BmpName.TASKSUBSCIBE_CONTENT_LEFT + i + ""
											+ j, i + "" + j);
							MemoryCenter.getInstance().FreeLimitBmp(
									BmpName.TASKSUBSCIBE_CONTENT_BOM + i + ""
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
					int iGoneViewHeight_px = 0;
					// 在这里由于有特殊情况，所以要判断一屏中是否包含不可见的View，如果包含不可见的View的高度总和大于屏幕的一半则跳过该屏
					// 属于当前屏幕中心的上下三个屏幕，需要保留内存，因为随时会被上下滑动
					if (iIndex - 1 == i || iIndex == i || iIndex + 1 == i) {
						for (int j = 0; j < one_screen_view.size(); j++) {
							if (null == one_screen_view.get(j).getTag()) {// 由于标题视图都在其Tag属性中放了一个int型数据
								SumListItem_BomImg sumListItem_bomimg = (SumListItem_BomImg) one_screen_view
										.get(j);
								TitleImgView imgView_left = sumListItem_bomimg
										.getTileImgView_left();
								TitleImgView imgView_bom = sumListItem_bomimg
										.getTileImgView_bom();
								Bitmap bmp_left = imgView_left.getBitmap();
								Bitmap bmp_bom = imgView_bom.getBitmap();
								if (null == bmp_left) {
									imgView_left
											.setBitmap(MemoryCenter
													.getInstance()
													.getLimitBmp(
															BmpName.TASKSUBSCIBE_CONTENT_LEFT
																	+ i
																	+ ""
																	+ j,
															TaskSubscibe.this
																	.getBmp(leftImgId_content[j
																			+ iBeforPresentBmps]),
															i + "" + j,
															i + "" + j));

								}
								if (null == bmp_bom
										&& View.VISIBLE == imgView_bom
												.getVisibility()
										&& 0 != TaskSubscibe.this.bomImgId_content[j
												+ iBeforPresentBmps]) {
									imgView_bom
											.setBitmap(MemoryCenter
													.getInstance()
													.getLimitBmp(
															BmpName.TASKSUBSCIBE_CONTENT_BOM
																	+ i
																	+ ""
																	+ j,
															TaskSubscibe.this
																	.getBmp(bomImgId_content[(j + iBeforPresentBmps)]),
															i + "" + j,
															i + "" + j));

								}
								if (View.GONE == sumListItem_bomimg
										.getVisibility()) {
									iGoneViewHeight_px += sumListItem_bomimg
											.getLayoutParams().height;
								}
							} else {
								iBeforPresentBmps--;// 不计算标题视图
							}

						}
					} else {
						for (int j = 0; j < one_screen_view.size(); j++) {
							if (null == one_screen_view.get(j).getTag()) {// 由于标题视图都在其Tag属性中放了一个int型数据
								TitleImgView imgView_left = ((SumListItem_BomImg) one_screen_view
										.get(j)).getTileImgView_left();
								TitleImgView imgView_bom = ((SumListItem_BomImg) one_screen_view
										.get(j)).getTileImgView_bom();
								Bitmap bmp_left = imgView_left.getBitmap();
								Bitmap bmp_bom = imgView_bom.getBitmap();
								if (null != bmp_left) {
									imgView_left.setBitmap(null);
									MemoryCenter.getInstance().FreeLimitBmp(
											BmpName.TASKSUBSCIBE_CONTENT_LEFT
													+ i + "" + j, i + "" + j);

								}
								if (null != bmp_bom) {
									imgView_bom.setBitmap(null);
									MemoryCenter.getInstance().FreeLimitBmp(
											BmpName.TASKSUBSCIBE_CONTENT_BOM
													+ i + "" + j, i + "" + j);
								}
							} else {
								iBeforPresentBmps--;// 不计算标题视图
							}
						}
					}
					// 不可见的View的高度占屏幕高度大于一半时,就不在以当前屏幕为中心来加载和释放图片了，而是以下一个屏幕为中心来加载和释放图片
					if (iGoneViewHeight_px >= 400) {
						iIndex++;
					}
					preOne_screen_view = one_screen_view;
					iBeforPresentBmps += preOne_screen_view.size();
					i++;
				}
				this.manaBmp_flag = false; // 内层循环执行完后停止
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
			Intent intent = new Intent(this, CollectionActivity.class);
			this.startActivity(intent);
		}
		return super.onTouchEvent(event);
	}
}
