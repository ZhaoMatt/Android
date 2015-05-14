package com.myTaskListItem.util;

import android.content.Context;
import android.graphics.Color;
import android.widget.RelativeLayout;
import android.widget.TextView;

//这个类是订阅页面的内容标题视图，可以展开和关闭它里面的内容视图
public class SumListItem_BomImgTitle extends RelativeLayout {

	private TitleImgView imgViewLeft;
	private TitleImgView imgViewRight;
	private TextView textVewTitle;

	public SumListItem_BomImgTitle(Context context) {
		super(context);
		// TODO Auto-generated constructor stub

		this.setPadding(0, 10, 0, 10);
		this.setBackgroundColor(Color.argb(255, 221, 221, 221));

		imgViewLeft = new TitleImgView(context);
		imgViewLeft.setId(1);
		RelativeLayout.LayoutParams imageView_left_params = new RelativeLayout.LayoutParams(
				80, 30);
		imageView_left_params.setMargins(10, 0, 0, 0);
		imageView_left_params.addRule(RelativeLayout.CENTER_VERTICAL);
		imgViewLeft.setBackgroundColor(Color.argb(0, 0, 0, 0));
		this.addView(imgViewLeft, imageView_left_params);

		imgViewRight = new TitleImgView(context);
		RelativeLayout.LayoutParams image_right_params = new RelativeLayout.LayoutParams(
				30, 30);
		image_right_params.addRule(RelativeLayout.CENTER_VERTICAL);
		image_right_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		image_right_params.setMargins(0, 0, 5, 0);
		imgViewRight.setLayoutParams(image_right_params);
		this.addView(imgViewRight);

		textVewTitle = new TextView(context);
		RelativeLayout.LayoutParams text_title_params = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		text_title_params.addRule(RelativeLayout.CENTER_VERTICAL);
		text_title_params.addRule(RelativeLayout.RIGHT_OF, 1);
		textVewTitle.setTextColor(Color.BLACK);
		textVewTitle.setTextSize(20);
		this.addView(textVewTitle, text_title_params);

	}

	public TitleImgView getImgViewLeft() {
		return imgViewLeft;
	}

	public TitleImgView getImgViewRight() {
		return imgViewRight;
	}

	public TextView getTextVewTitle() {
		return textVewTitle;
	}

}
