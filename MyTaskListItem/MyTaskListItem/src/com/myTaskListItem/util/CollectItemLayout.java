package com.myTaskListItem.util;

import android.content.Context;
import android.view.Gravity;
import android.widget.FrameLayout;

public class CollectItemLayout extends FrameLayout {

	TitleImgView imgItemView;
	TitleImgView imgDeletFlag;

	public CollectItemLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		imgItemView = new TitleImgView(context);
		FrameLayout.LayoutParams itemParams = new FrameLayout.LayoutParams(140,
				140);
		imgItemView.setLayoutParams(itemParams);
		this.addView(imgItemView);

		imgDeletFlag = new TitleImgView(context);
		FrameLayout.LayoutParams deletParams = new FrameLayout.LayoutParams(12,
				12);
		deletParams.gravity = Gravity.LEFT;
		deletParams.setMargins(127, 127, 0, 0);
		imgDeletFlag.setLayoutParams(deletParams);
		this.addView(imgDeletFlag);
	}

	public TitleImgView getImgItemView() {
		return imgItemView;
	}

	public TitleImgView getImgDeletFlag() {
		return imgDeletFlag;
	}

}
