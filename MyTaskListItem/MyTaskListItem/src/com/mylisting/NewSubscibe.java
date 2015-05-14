package com.mylisting;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.myTaskListItem.util.BmpName;
import com.myTaskListItem.util.ClickScreenInterface;
import com.myTaskListItem.util.FingerViewGroupInterface;
import com.myTaskListItem.util.FreeFrameLayoutInterface;
import com.myTaskListItem.util.FreeLinearLayout;
import com.myTaskListItem.util.MemoryCenter;
import com.myTaskListItem.util.NewSubManagerView;
import com.myTaskListItem.util.SqlHelper;
import com.myTaskListItem.util.SumListItem_BomImg;
import com.myTaskListItem.util.SumListItem_BomImgTitle;
import com.myTaskListItem.util.TitleImgView;

/*在这里用 数组的索引位置来模拟 它们相应的ID */

public class NewSubscibe extends Activity implements FingerViewGroupInterface,
		ClickScreenInterface, FreeFrameLayoutInterface, View.OnClickListener {

	String[] strEnglishTitle = new String[] { "Good Phone", "Da Ge Da",
			"HTC Util", "Samsung Phone", "ZTE Phone" };
	String[] strTitle = new String[] { "好手机", "大哥大", "HTC", "三星", "中兴手机" }; // 收藏的栏目
	String[][] strTitle_content_title = new String[][] {
			{ "栏目中的而一个项的标题", "第二个项的标题" }, { "第二个栏目中的项标题" }, { "第三个栏目中项的标题" },
			{ "第四个栏目项中的标题" }, { "第五个栏目中项的标题" } };
	String[][] str_content_content = new String[][] { { "sdfeggg" },
			{ "dgggg" }, { "rrrrrrr" }, { "yyyyy" }, { "ppppp" } };
	String[] strTitle_content_content = new String[] { "栏目中的一个项的内容" };
	int[] topImageId = new int[] { R.drawable.testa, R.drawable.testb,
			R.drawable.testc, R.drawable.testd, R.drawable.teste };// 栏目介绍
	String[] strContent = new String[] { "第共轭的 是 个饿个 的我\n饿他好 个放到的的饿饿 是",
			"搜哦票根对哦iehg 哦饿哦根据诶覅欧文 我 的个个饿 ", "第的各位为她他个到的的饿饿 是",
			"欧共；打开为空欧文看i看好好个个个的饿饿 是", "平哦iu一他他人飞 吧你就就看看 了饿饿 是" };
	int[] bomImgId_content = new int[] { 0, 0, R.drawable.dingyuelistbom, 0,
			R.drawable.dylistbom2, 0, 0, R.drawable.dylistbom3, 0,
			R.drawable.dylistbom4, 0 };
	int[] leftImgId_content = new int[] { R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon, R.drawable.icon, R.drawable.icon, R.drawable.icon,
			R.drawable.icon };

	Bitmap titleBmp;
	TitleImgView titleImage;

	Bitmap bomBmp;
	TitleImgView bomImage;

	TitleImgView newSubtopImage_right;
	TitleImgView newSubtopImage_left;

	NewSubManagerView newSubMangView;
	FreeLinearLayout mySubContent;

	// 释放左右拖动图片控件中的图片时用的
	Bitmap[] bmpArray = null;
	int[] iPicStatusArray = null;

	// 查询我订阅的标题ID
	int[] mySub_id = null;

	// 加载图片和释放图片资源用的(对应于列表)
	List<List<View>> listAllScreenView;
	private int iIndex;
	private ManagerBmp managerBmp_subscibe;

	int iId = 0;// 用来模拟点击订阅按钮后所获得的订阅内容ID;

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
		setContentView(rootLayout);
		// 顶部标题
		titleImage = new TitleImgView(this);
		LinearLayout.LayoutParams titleParams = new LinearLayout.LayoutParams(
				480, 53);
		titleImage.setLayoutParams(titleParams);
		rootLayout.addView(titleImage);

		// 顶部的导航
		LinearLayout topNewsubmenuLayout = new LinearLayout(this);
		LinearLayout.LayoutParams topsubmenuParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 38);
		topNewsubmenuLayout.setLayoutParams(topsubmenuParams);

		newSubtopImage_left = new TitleImgView(this);
		LinearLayout.LayoutParams topleftParams = new LinearLayout.LayoutParams(
				135, 38);
		newSubtopImage_left.setLayoutParams(topleftParams);
		newSubtopImage_left.setOnClickListener(this);

		newSubtopImage_right = new TitleImgView(this);
		LinearLayout.LayoutParams toprightParams = new LinearLayout.LayoutParams(
				345, 38);
		newSubtopImage_right.setLayoutParams(toprightParams);
		newSubtopImage_right.setOnClickListener(this);
		topNewsubmenuLayout.addView(newSubtopImage_left);
		topNewsubmenuLayout.addView(newSubtopImage_right);
		rootLayout.addView(topNewsubmenuLayout);

		// 订阅管理
		LinearLayout.LayoutParams contentParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 654);
		newSubMangView = new NewSubManagerView(this, 5, contentParams, this,
				this);
		newSubMangView.getSubImgBtn().setImageBitmap(
				this.getBmp(R.drawable.dingyuebtn));
		newSubMangView.getSubImgBtn().setOnClickListener(this);
		rootLayout.addView(newSubMangView);

		// 我的订阅
		LinearLayout.LayoutParams mySubcontentParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 654);
		mySubContent = new FreeLinearLayout(this, FreeLinearLayout.VERTICAL,
				this);
		mySubContent.setLayoutParams(mySubcontentParams);
		rootLayout.addView(mySubContent);

		// 底部导航条
		bomImage = new TitleImgView(this);
		LinearLayout.LayoutParams bomParams = new LinearLayout.LayoutParams(
				480, 55);
		bomImage.setLayoutParams(bomParams);
		rootLayout.addView(bomImage);

		mySub_id = this.getMySub();
		if (0 == mySub_id.length) {// 证明我还没有订阅任何内容
			mySubContent.setVisibility(View.GONE);
			newSubMangView.setVisibility(View.VISIBLE);
		} else {
			mySubContent.setVisibility(View.VISIBLE);
			newSubMangView.setVisibility(View.GONE);
		}

		// 初始化大图
		bmpArray = new Bitmap[topImageId.length];
		iPicStatusArray = new int[topImageId.length];
		for (int i = 0; i < topImageId.length; i++) {
			iPicStatusArray[i] = 0; // 初始化为没有加载图片
			LinearLayout layout = new LinearLayout(this);
			TitleImgView imgView = new TitleImgView(this);
			// 写死子控件尺寸，不能让它切换图时自动缩放大小
			imgView.setMinimumHeight(400);
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
			newSubMangView.getFingerViewGroup().addView(layout);
		}

		// 初始化我的订阅控件
		for (int i = 0; i < mySub_id.length; i++) {
			SumListItem_BomImgTitle title = new SumListItem_BomImgTitle(this);
			title.setTag("flag");
			title.getTextVewTitle().setText(strTitle[mySub_id[i]]);
			title.getImgViewRight().setMinimumHeight(35);
			title.getImgViewRight().setMinimumWidth(100);
			title.getImgViewRight().setBitmap(
					this.getBmp(R.drawable.morejingcai));
			SumListItem_BomImg content = new SumListItem_BomImg(this);
			content.getImageView_right().setVisibility(View.GONE); // 取出列表中的箭头并设置不可见
			content.getTitleTxtView().setText(strTitle_content_title[i][0]);
			content.getContentTxtView().setText(str_content_content[i][0]);

			LinearLayout.LayoutParams title_params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT, 100);

			mySubContent.addView(title, title_params);
			mySubContent.addView(content, title_params);
		}

		// 初始化我的订阅控件中的图片
		mySubContent.initScreenView();
		listAllScreenView = mySubContent.getAll_screen_View();

		if (null != listAllScreenView) {
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
								imgView_left
										.setBitmap(MemoryCenter
												.getInstance()
												.getLimitBmp(
														BmpName.TASKSUBSCIBE_CONTENT_LEFT
																+ i + "" + j,
														this.getBmp(leftImgId_content[j
																+ iBeforPresentBmps]),
														i + "" + j, i + "" + j));
							}
							if (null == bmp_bom
									&& View.VISIBLE == imgView_bom
											.getVisibility()
									&& 0 != this.bomImgId_content[j
											+ iBeforPresentBmps]) {
								imgView_bom
										.setBitmap(MemoryCenter
												.getInstance()
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
						if (null == one_screen_view.get(j).getTag()) {// 由于标题视图都在其Tag属性中放了一个String型数据
							TitleImgView imgView_left = ((SumListItem_BomImg) one_screen_view
									.get(j)).getTileImgView_left();
							TitleImgView imgView_bom = ((SumListItem_BomImg) one_screen_view
									.get(j)).getTileImgView_bom();
							Bitmap bmp_left = imgView_left.getBitmap();
							Bitmap bmp_bom = imgView_bom.getBitmap();
							if (null != bmp_left) {
								imgView_left.setBitmap(null);
								MemoryCenter.getInstance().FreeLimitBmp(
										BmpName.TASKSUBSCIBE_CONTENT_LEFT + i
												+ "" + j, i + "" + j);
							}
							if (null != bmp_bom) {
								imgView_bom.setBitmap(null);
								MemoryCenter.getInstance().FreeLimitBmp(
										BmpName.TASKSUBSCIBE_CONTENT_BOM + i
												+ "" + j, i + "" + j);
							}
						} else {
							iBeforPresentBmps--;// 取得图片时不能计算标题视图
						}
					}
				}
				preOne_screen_view = one_screen_view;
				iBeforPresentBmps += preOne_screen_view.size();
			}
			managerBmp_subscibe = new ManagerBmp();
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

		newSubtopImage_left.setBitmap(MemoryCenter.getInstance().getLimitBmp(
				"newsubtopImageleft", getBmp(R.drawable.newsubtopdhmy),
				"newsubtop_my", "stmy"));
		newSubtopImage_right.setBitmap(MemoryCenter.getInstance().getLimitBmp(
				"newsubtopImageright", getBmp(R.drawable.newsubtopdhguanli),
				"newsubtop_gl", "stgl"));
	}

	public int[] getMySub() {
		SqlHelper sqlHelper = SqlHelper.getInstance(this);
		int[] mySub = sqlHelper.getSub();
		sqlHelper.close();
		return mySub;
	}

	public Bitmap getBmp(int id) {
		return BitmapFactory.decodeResource(this.getResources(), id);
	}

	@Override
	public void onCurChileCtrlScreen(int index, int direct) {
		// TODO Auto-generated method stub
		int preIndex = this.iIndex;
		this.iIndex = index;
		if (0 != direct) {// 当是滑动时执行操作
			if (!managerBmp_subscibe.isAlive()) {
				managerBmp_subscibe = new ManagerBmp(); // 注意这里必须在重新赋值;如果不重新赋值的话线程结束后，这个引用指向的是已经结束了的线程对象（已经在内存中不存在咯）
				managerBmp_subscibe.query_flag = true;
				managerBmp_subscibe.manaBmp_flag = true;
				managerBmp_subscibe.start(); // 守护线程只开启一次
			}
			if (preIndex != index) { // 当屏幕变化时使守护线程内部循环开始执行
				managerBmp_subscibe.manaBmp_flag = true;
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
		iId = index;
		newSubMangView.getShowUpIndexPage().setSelected(index);
		for (int i = 0; i < topImageId.length; i++) {
			TitleImgView imgView = (TitleImgView) ((LinearLayout) newSubMangView
					.getFingerViewGroup().getChildAt(i)).getChildAt(0);
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
		newSubMangView.getTextEnTitle().setText(strEnglishTitle[index]);
		newSubMangView.getTextZhTitle().setText(strTitle[index]);
		newSubMangView.getTextContent().setText(strContent[index]);
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
					// 在这里由于有特殊情况，所以要判断一屏中是否包含不可见的View，如果包含不可见的View的高度总和大于屏幕的一半则跳过该屏
					// 属于当前屏幕中心的上下三个屏幕，需要保留内存，因为随时会被上下滑动
					if (iIndex - 1 == i || iIndex == i || iIndex + 1 == i) {
						for (int j = 0; j < one_screen_view.size(); j++) {
							if (null == one_screen_view.get(j).getTag()) {// 由于标题视图都在其Tag属性中放了一个String型数据
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
															NewSubscibe.this
																	.getBmp(leftImgId_content[j
																			+ iBeforPresentBmps]),
															i + "" + j,
															i + "" + j));

								}
								if (null == bmp_bom
										&& View.VISIBLE == imgView_bom
												.getVisibility()
										&& 0 != NewSubscibe.this.bomImgId_content[j
												+ iBeforPresentBmps]) {
									imgView_bom
											.setBitmap(MemoryCenter
													.getInstance()
													.getLimitBmp(
															BmpName.TASKSUBSCIBE_CONTENT_BOM
																	+ i
																	+ ""
																	+ j,
															NewSubscibe.this
																	.getBmp(bomImgId_content[(j + iBeforPresentBmps)]),
															i + "" + j,
															i + "" + j));

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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.hashCode() == newSubMangView.getSubImgBtn().hashCode()) {
			SqlHelper.getInstance(this).insertSub(iId);
			SqlHelper.getInstance(this).close();

			if (-1 != SqlHelper.getInstance(this).getSubById(iId)) {
				SqlHelper.getInstance(this).close();
				// 给我的订阅界面添加视图
				SumListItem_BomImgTitle title = new SumListItem_BomImgTitle(
						this);
				title.setTag("flag");
				title.getTextVewTitle().setText(strTitle[mySub_id[iId]]);
				title.getImgViewRight().setMinimumHeight(35);
				title.getImgViewRight().setMinimumWidth(100);
				title.getImgViewRight().setBitmap(
						this.getBmp(R.drawable.morejingcai));
				SumListItem_BomImg content = new SumListItem_BomImg(this);
				content.getImageView_right().setVisibility(View.GONE); // 取出列表中的箭头并设置不可见
				content.getTitleTxtView().setText(
						strTitle_content_title[iId][0]);
				content.getContentTxtView()
						.setText(str_content_content[iId][0]);

				LinearLayout.LayoutParams title_params = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT, 100);

				mySubContent.addView(title, title_params);
				mySubContent.addView(content, title_params);

				// 再重新初始化FreeLinearLayout
				mySubContent.initScreenView();
				listAllScreenView = mySubContent.getAll_screen_View();

				// 向数据库中插入数据

				SqlHelper.getInstance(this).insertSub(iId);
				SqlHelper.getInstance(this).close();
			} else {
				
			}

		} else if (v.hashCode() == newSubtopImage_left.hashCode()) {
			newSubMangView.setVisibility(View.GONE);
			mySubContent.setVisibility(View.VISIBLE);

		} else if (v.hashCode() == newSubtopImage_right.hashCode()) {
			newSubMangView.setVisibility(View.VISIBLE);
			mySubContent.setVisibility(View.GONE);
		}
	}
}
