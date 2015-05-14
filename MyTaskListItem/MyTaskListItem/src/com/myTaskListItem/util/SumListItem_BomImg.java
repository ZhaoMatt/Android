package com.myTaskListItem.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SumListItem_BomImg extends RelativeLayout {

	// private Context context;
	TitleImgView titleImg_left; // 左边的ImageView
	ImageView imageView_right;// 右边的箭头ImageView
	TitleImgView titleImg_bom;// 最下面的图片
	LinearLayout linearLayout_content;// 中间文字内容的外布局
	TextView titleTxtView; // 显示标题的TextView控件
	TextView contentTxtView;// 显示标题下面内容的TextView控件

	public SumListItem_BomImg(Context context) {
		// this.context = context;
		super(context);
		this.setBackgroundColor(Color.WHITE);
		RelativeLayout.LayoutParams listFL = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(listFL);
		this.setPadding(0, 10, 0, 10);

		imageView_right = new ImageView(context);
		imageView_right.setId(2);
		RelativeLayout.LayoutParams image_right_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		image_right_params.addRule(RelativeLayout.CENTER_VERTICAL);
		image_right_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		image_right_params.setMargins(0, 0, 5, 0);
		imageView_right.setLayoutParams(image_right_params);

		RelativeLayout relativeLayout = new RelativeLayout(context);
		relativeLayout.setId(4);
		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		relativeParams.addRule(RelativeLayout.LEFT_OF, 2);
		relativeLayout.setLayoutParams(relativeParams);

		titleImg_left = new TitleImgView(context);
		titleImg_left.setId(1);
		RelativeLayout.LayoutParams imageView_left_params = new RelativeLayout.LayoutParams(
				80, 80);
		imageView_left_params.setMargins(10, 0, 0, 0);
		imageView_left_params.addRule(RelativeLayout.CENTER_VERTICAL);
		titleImg_left.setBackgroundColor(Color.argb(0, 0, 0, 0));

		linearLayout_content = new LinearLayout(context);
		linearLayout_content.setId(3);
		linearLayout_content.setOrientation(LinearLayout.VERTICAL);
		RelativeLayout.LayoutParams linear_content_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		linear_content_params.addRule(RelativeLayout.CENTER_VERTICAL);
		linear_content_params.addRule(RelativeLayout.RIGHT_OF, 1);
		linear_content_params.setMargins(10, 0, 0, 0);
		linearLayout_content.setLayoutParams(linear_content_params);
		titleTxtView = new TextView(context);
		LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		titleTxtView.setTextColor(Color.BLACK);
		titleTxtView.setTextSize(20);
		titleTxtView.setLayoutParams(ll);
		contentTxtView = new TextView(context);
		contentTxtView.setTextSize(10);
		contentTxtView.setTextColor(Color.BLACK);
		contentTxtView.setLayoutParams(ll);
		linearLayout_content.addView(titleTxtView);
		linearLayout_content.addView(contentTxtView);

		relativeLayout.addView(titleImg_left, imageView_left_params);
		relativeLayout.addView(linearLayout_content);

		titleImg_bom = new TitleImgView(context);
		RelativeLayout.LayoutParams titleImg_bom_params = new RelativeLayout.LayoutParams(
				300, 100);
		titleImg_bom_params.setMargins(100, 0, 0, 0);
		titleImg_bom_params.addRule(RelativeLayout.BELOW, 4);
		titleImg_bom.setLayoutParams(titleImg_bom_params);
		titleImg_bom.setVisibility(View.GONE);
		titleImg_bom.setBackgroundColor(Color.argb(0, 0, 0, 0));

		this.addView(imageView_right);
		this.addView(relativeLayout);
		this.addView(titleImg_bom);
	}

	public TitleImgView getTileImgView_left() {
		return titleImg_left;
	}

	public TitleImgView getTileImgView_bom() {
		return titleImg_bom;
	}

	public ImageView getImageView_right() {
		return imageView_right;
	}

	public LinearLayout getLinearLayout_content() {
		return linearLayout_content;
	}

	public TextView getTitleTxtView() {
		return titleTxtView;
	}

	public TextView getContentTxtView() {
		return contentTxtView;
	}

}
