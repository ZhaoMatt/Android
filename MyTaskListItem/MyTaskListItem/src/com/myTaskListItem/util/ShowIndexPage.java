package com.myTaskListItem.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.mylisting.R;

/**
 * 用于主页面翻页
 * 
 * @author 做标识
 * 
 */
public class ShowIndexPage extends LinearLayout {
	private Drawable[] icons = new Drawable[2];
	private int size = 2;
	private ImageView[] items;
	private int selected = 0;

	public ShowIndexPage(Context context, int size) {
		super(context);
		this.size = size;
		items = new ImageView[size];
		icons[0] = getResources().getDrawable(R.drawable.ab_3);
		icons[1] = getResources().getDrawable(R.drawable.ab_4);
		setPadding(0, 0, 0, 0);
		// setGravity(Gravity.CENTER);
		LayoutParams lp = new LayoutParams(10, 10);
		lp.setMargins(5, 0, 0, 0);
		for (int i = 0; i < size; i++) {
			items[i] = new ImageView(context);
			items[i].setPadding(0, 0, 0, 0);
			if (0 == i)
				items[i].setImageDrawable(icons[i]);
			else
				items[i].setImageDrawable(icons[1]);
			addView(items[i], lp);
		}
	}

	/**
	 * 设置selected
	 * 
	 * @param index
	 */
	public void setSelected(int index) {
		selected = index;
		for (int i = 0; i < size; i++) {
			if (i == selected)
				items[i].setImageDrawable(icons[1]);
			else
				items[i].setImageDrawable(icons[0]);
		}
	}

	/**
	 * 获得selected
	 * 
	 * @return
	 */
	public int getSelected() {
		return selected;
	}

}
