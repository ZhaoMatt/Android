package com.myTaskListItem.util;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NewSubManagerView extends LinearLayout {

	TextView textEnTitle;
	TextView textZhTitle;
	ImageView subImgBtn;
	FingerViewGroup fingerViewGroup;
	TextView textContent;
	ShowIndexPage showUpIndexPage;

	int pageCount;

	public NewSubManagerView(Context context, int pageCount,
			LayoutParams layoutParams, FingerViewGroupInterface fvinterface,
			ClickScreenInterface csinterface) {
		super(context);
		// TODO Auto-generated constructor stub
		this.pageCount = pageCount;
		this.setLayoutParams(layoutParams);
		this.setOrientation(LinearLayout.VERTICAL);

		// 订阅内容的标题
		LinearLayout.LayoutParams contentTitleParams = new LinearLayout.LayoutParams(
				480, 100);
		LinearLayout contentTitleLayout = new LinearLayout(context);
		contentTitleLayout.setOrientation(LinearLayout.HORIZONTAL);
		contentTitleLayout.setLayoutParams(contentTitleParams);

		LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(
				300, LinearLayout.LayoutParams.WRAP_CONTENT);
		textLayoutParams.setMargins(30, 10, 0, 0);
		LinearLayout textLayout = new LinearLayout(context);
		textLayout.setOrientation(LinearLayout.VERTICAL);
		textLayout.setLayoutParams(textLayoutParams);

		textEnTitle = new TextView(context);
		textEnTitle.setTextSize(20);
		textEnTitle.setTextColor(Color.BLACK);

		textZhTitle = new TextView(context);
		textZhTitle.setTextColor(Color.BLACK);
		textZhTitle.setTextSize(18);
		textLayout.addView(textEnTitle);
		textLayout.addView(textZhTitle);
		contentTitleLayout.addView(textLayout);

		subImgBtn = new ImageView(context);
		LinearLayout.LayoutParams subImgParams = new LinearLayout.LayoutParams(
				100, 40);
		subImgParams.gravity = Gravity.CENTER_VERTICAL;
		subImgParams.leftMargin= 20;
		subImgBtn.setLayoutParams(subImgParams);
		contentTitleLayout.addView(subImgBtn);
		this.addView(contentTitleLayout);

		// 订阅大图
		fingerViewGroup = new FingerViewGroup(context, fvinterface, csinterface);
		LinearLayout.LayoutParams fingerParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 300);
		fingerViewGroup.setLayoutParams(fingerParams);
		this.addView(fingerViewGroup);
		// 文字具体内容
		textContent = new TextView(context);
		LinearLayout.LayoutParams textContentParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT, 200);
		textContent.setPadding(20, 10, 20, 10);
		textContent.setLayoutParams(textContentParams);
		this.addView(textContent);

		// 分页标识
		showUpIndexPage = new ShowIndexPage(context, pageCount);
		showUpIndexPage.setSelected(0);
		LinearLayout.LayoutParams showUpParams = new LinearLayout.LayoutParams(
				80, 10);
		showUpParams.setMargins(200, 20, 0, 0);
		showUpIndexPage.setLayoutParams(showUpParams);
		this.addView(showUpIndexPage);

	}

	public TextView getTextEnTitle() {
		return textEnTitle;
	}

	public TextView getTextZhTitle() {
		return textZhTitle;
	}

	public ImageView getSubImgBtn() {
		return subImgBtn;
	}

	public FingerViewGroup getFingerViewGroup() {
		return fingerViewGroup;
	}

	public TextView getTextContent() {
		return textContent;
	}

	public ShowIndexPage getShowUpIndexPage() {
		return showUpIndexPage;
	}

}
