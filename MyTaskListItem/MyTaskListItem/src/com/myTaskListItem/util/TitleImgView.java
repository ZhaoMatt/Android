package com.myTaskListItem.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * 1.默认的背景色是浅灰色的RGB（190,190,190）;
 * 
 * 2.本版本不支持图片大小超出控件大小(当图片大小超出View大小时会自动对图片进行缩放);
 * 
 * 3.默认图片是沾满整个View的
 * 
 * 4.默认文字是顶部左边显示
 * 
 * 5.本版本对图片位置的确定是根据上下左右本分比计算的（以后可能考虑用像素来计算）
 * 
 * 6.注意本版本不支持设置 LayoutParams的属性为 wrap_content
 * */

public class TitleImgView extends View {

	private Bitmap bmp = null; // 控件中要显示的图片
	private float fLeft_scale = 0f; // 图片距离控件右边框的百分比
	private float fRight_scale = 0f; // 图片距离控件左边框的百分比
	private float fTop_scale = 0f;// 图片距离控件上部的百分比
	private float fBom_scale = 0f;// 图片距离控件底部的百分比

	// private int left_x = 0;// 图片距离控件左边的X轴距离
	// private int right_x = 0;// 图片距离控件右边的X轴距离
	// private int top_y = -1;// 图片距离控件顶部的Y轴距离
	// private int bom_y = -1;// 图片距离控件底部的Y轴距离

	// 控制文字的位置，有六个位置：顶端左边，顶端居中，顶端右边，底部左边，底部居中，底部右边
	public static final String TOP_LEFT = "top_left";
	public static final String TOP_RIGHT = "top_right";
	public static final String TOP_CENTER = "top_center";
	public static final String BOM_LEFT = "bom_left";
	public static final String BOM_RIGHT = "bom_right";
	public static final String BOM_CENTER = "bom_center";

	private String strText = null;// 要显示的文字
	private String strText_location; // 显示文字的位置
	private float fTrimming_location_x = 0;// 微调文字距离边框的位置(X轴上的,当文字位置居中时不起作用),举例：若文字在左边位置则代表文字到左边框距离的微调，若在右边则代表到右边框距离的微调
	private float fTrimming_location_y = 0;// 微调文字距离边框的位置(Y轴上的)举例：如果是在顶部就是距离顶部的微调，如果是在底部就是距离底部的微调
	private Paint paint;// 画文字的画笔

	private Rect ret_dst; // 图片的目标位置矩形范围

	// private boolean iner_img_zoom = false;// 控件里面的图片是否可以缩放的标识

	public TitleImgView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setBackgroundColor(Color.argb(255, 190, 190, 190));
		ret_dst = new Rect();
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
	}

	public TitleImgView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setBackgroundColor(Color.argb(255, 190, 190, 190));
		ret_dst = new Rect();
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setAntiAlias(true);
	}

	public void onDraw(Canvas canvas) {
		if (null != this.bmp && !this.bmp.isRecycled() && null != ret_dst) {
			ret_dst.left = (int) (getWidth() * fLeft_scale);
			ret_dst.top = (int) (this.getHeight() * fTop_scale);
			ret_dst.right = (int) (this.getWidth() - this.getWidth()
					* fRight_scale);
			ret_dst.bottom = (int) (this.getHeight() - this.getHeight()
					* fBom_scale);

			// if (right_x >= 0 && bom_y < 0) {
			// dst.left = left_x;
			// dst.top = top_y;
			// dst.right = right_x;
			// dst.bottom = top_y + bitmap.getHeight();
			// } else if (bom_y >= 0 && right_x < 0) {
			// dst.left = left_x;
			// dst.top = top_y;
			// dst.right = left_x + bitmap.getWidth();
			// dst.bottom = bom_y;
			// } else if (bom_y >= 0 && right_x >= 0) {
			// dst.left = left_x;
			// dst.top = top_y;
			// dst.right = right_x;
			// dst.bottom = bom_y;
			// } else {
			// dst.left = left_x;
			// dst.top = top_y;
			// dst.right = left_x + bitmap.getWidth();
			// dst.bottom = top_y + bitmap.getHeight();
			// }

			canvas.drawBitmap(bmp,
					new Rect(0, 0, bmp.getWidth(), bmp.getHeight()), ret_dst,
					null);

		}

		if (null != strText) {
			if (null == strText_location || TOP_LEFT.equals(strText_location)) {
				canvas.drawText(strText, fTrimming_location_x,
						paint.getTextSize() + fTrimming_location_y, paint);
			} else if (TOP_CENTER.equals(strText_location)) {
				canvas.drawText(strText, (this.getWidth() - paint.getTextSize()
						* strText.length()) / 2, paint.getTextSize()
						+ fTrimming_location_y, paint);
			} else if (TOP_RIGHT.equals(strText_location)) {
				canvas.drawText(strText, this.getWidth() - strText.length()
						* paint.getTextSize() - fTrimming_location_x,
						paint.getTextSize() + fTrimming_location_y, paint);
			} else if (BOM_LEFT.equals(strText_location)) {
				canvas.drawText(strText, fTrimming_location_x, this.getHeight()
						- paint.getTextSize() / 2 - fTrimming_location_y, paint);
			} else if (BOM_CENTER.equals(strText_location)) {
				canvas.drawText(strText, (this.getWidth() - paint.getTextSize()
						* strText.length()) / 2,
						this.getHeight() - paint.getTextSize() / 2
								- fTrimming_location_y, paint);
			} else if (BOM_RIGHT.equals(strText_location)) {
				canvas.drawText(strText, this.getWidth() - strText.length()
						* paint.getTextSize() - fTrimming_location_x,
						this.getHeight() - paint.getTextSize() / 2
								- fTrimming_location_y, paint);
			}
		}
		super.onDraw(canvas);
	}

	public Bitmap getBitmap() {
		return bmp;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bmp = bitmap;
		this.postInvalidate();
	}

	// ===================
	// public int getLeft_x() {
	// return left_x;
	// }
	//
	// public void setLeft_x(int left_x) {
	// this.left_x = left_x;
	// }
	//
	// public int getRight_x() {
	// return right_x;
	// }
	//
	// public void setRight_x(int right_x) {
	// this.right_x = right_x;
	// }
	//
	// public int getTop_y() {
	// return top_y;
	// }
	//
	// public void setTop_y(int top_y) {
	// this.top_y = top_y;
	// }
	//
	// public int getBom_y() {
	// return bom_y;
	// }
	//
	// public void setBom_y(int bom_y) {
	// this.bom_y = bom_y;
	// }

	// ===================

	public float getFleft_scale() {

		return fLeft_scale;
	}

	public void setFleft_scale(float fLeft_scale) {
		this.fLeft_scale = fLeft_scale;
	}

	public float getFright_scale() {
		return fRight_scale;
	}

	public void setFright_scale(float fRight_scale) {
		this.fRight_scale = fRight_scale;
		this.postInvalidate();
	}

	public float getFtop_scale() {
		return fTop_scale;
	}

	public void setFtop_scale(float fTop_scale) {
		this.fTop_scale = fTop_scale;
		this.postInvalidate();
	}

	public float getFbom_scale() {
		return fBom_scale;
	}

	public void setFbom_scale(float fBom_scale) {
		this.fBom_scale = fBom_scale;
		this.postInvalidate();
	}

	public String getText() {
		return strText;
	}

	public String getTextLocation() {
		return strText_location;
	}

	public void setText(String str, String location) {
		this.strText = str;
		this.strText_location = location;
	}

	// 设置文字的颜色
	public void setTextColor(int color) {
		paint.setColor(color);
		this.postInvalidate();
	}

	// 设置文字的大小
	public void setTextSize(int size) {
		paint.setTextSize(size);
		this.postInvalidate();
	}

	public float getTrimming_location_x() {
		return fTrimming_location_x;
	}

	public void setTrimming_location_x(float trimming_location_x) {
		this.fTrimming_location_x = trimming_location_x;
		this.postInvalidate();
	}

	public float getTrimming_location_y() {
		return fTrimming_location_y;
	}

	public void setTrimming_location_y(float trimming_location_y) {
		this.fTrimming_location_y = trimming_location_y;
		this.postInvalidate();
	}
}
