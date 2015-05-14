package com.myTaskListItem.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SumListItem extends RelativeLayout {

	// private Context context;
	TitleImgView imageView_left; // 左边的ImageView
	ImageView imageView_right;// 右边的箭头ImageView
	LinearLayout linearLayout_content;// 中间文字内容的外布局
	TextView titleTxtView; // 显示标题的TextView控件
	TextView contentTxtView;// 显示标题下面内容的TextView控件

	public SumListItem(Context context) {
		// this.context = context;
		super(context);
		this.setBackgroundColor(Color.WHITE);
		RelativeLayout.LayoutParams listFL = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(listFL);
		this.setPadding(0, 10, 0, 10);

		imageView_left = new TitleImgView(context);
		imageView_left.setId(1);
		RelativeLayout.LayoutParams imageView_left_params = new RelativeLayout.LayoutParams(
				80, 80);
		imageView_left_params.setMargins(10, 0, 0, 0);
		imageView_left_params.addRule(RelativeLayout.CENTER_VERTICAL);
		imageView_left.setBackgroundColor(Color.argb(0, 0, 0, 0));
		this.addView(imageView_left, imageView_left_params);

		imageView_right = new ImageView(context);
		imageView_right.setId(2);
		RelativeLayout.LayoutParams image_right_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		image_right_params.addRule(RelativeLayout.CENTER_VERTICAL);
		image_right_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		image_right_params.setMargins(0, 0, 5, 0);
		imageView_right.setLayoutParams(image_right_params);

		linearLayout_content = new LinearLayout(context);
		linearLayout_content.setId(3);
		linearLayout_content.setOrientation(LinearLayout.VERTICAL);
		RelativeLayout.LayoutParams linear_content_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		linear_content_params.addRule(RelativeLayout.CENTER_VERTICAL);
		linear_content_params.addRule(RelativeLayout.LEFT_OF, 2);
		linear_content_params.addRule(RelativeLayout.RIGHT_OF, 1);
		linear_content_params.setMargins(5, 0, 0, 0);
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

		this.addView(imageView_right);
		this.addView(linearLayout_content);

	}

	public TitleImgView getTileImgView_left() {
		return imageView_left;
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
