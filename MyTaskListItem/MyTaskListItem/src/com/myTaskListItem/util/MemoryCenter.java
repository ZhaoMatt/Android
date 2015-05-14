package com.myTaskListItem.util;

import java.util.Hashtable;

import android.graphics.Bitmap;

/**
 * 
 * 这个类实现的大体思路:
 * 
 * 1.在本类中有一个自身引用的静态变量，以保证在整个应用程序中只被实例化一次
 * 
 * 2.本类的 一个Hashtable变量来维护着不同的Bitmap。而本类总的一个数据结构（内部类Node），
 * 则来存放对Bitmap的描述并且如果是相同的Bitmap则就在Node中的引用数加1，不再存放到Hashtable变量中了
 * 
 * 3.执行过程是这样的，当需要Bitmap时，先将Bitmap给本类，让本类记录Bitmap信息，当不用Bitmap时，来通过本类释放Bitmap；
 * 
 * 4.
 * 
 * 
 * */

public class MemoryCenter {
	public static final int SUCCESS = 0; // 成功
	public static final int DEFAULT = -1; // 缺省
	private static MemoryCenter memoryCenter = null;// 静态的能够保证这个类的对象 始终只被实例化一次
	// Vector<Node> vFirstBitmaps = null;
	Hashtable<String, Node> hBitmaps = null;

	// 构造函数
	private MemoryCenter() {
		// this.vFirstBitmaps = new Vector<Node>();
		this.hBitmaps = new Hashtable<String, MemoryCenter.Node>();

	}

	// 保证在内存中只有一个管理中心对象
	static synchronized public MemoryCenter getInstance() {
		if (null == memoryCenter)
			memoryCenter = new MemoryCenter();
		return memoryCenter;
	}

	// 存储信息的实体类
	class Node {
		public String strName = null;// 图片的名称
		public String strPath = null;// 图片的路径
		public Bitmap bmpPic = null;// 图片
		public int iRef = 0;// 引用次数
		// 存储引用对象的名称
		public Hashtable<String, String> hRefLog = new Hashtable<String, String>();

		// 为内存结构的属性赋值
		public void setAttribute(String tag, Bitmap bitmap, String obj,
				String name) {
			strPath = tag;
			bmpPic = bitmap;
			iRef++;
			hRefLog.put(obj, name);
		}
	}

	// 获得内存结构中的图片,在这里各个参数的作用：tag是图片的唯一标识（一般为路径），bitmap是图片对象的引用，obj是存储图片数据结构的键值名（凭借这个值可以获得bitmap引用），
	// name是bitmap的名称
	public Bitmap getLimitBmp(String tag, Bitmap bitmap, String obj, String name) {
		if (null == tag || 0 == tag.trim().length() || null == bitmap
				|| null == obj || 0 == obj.trim().length() || null == name
				|| 0 == name.trim().length())
			return null;

		// for (int i = 0; i < this.vFirstBitmaps.size(); i++) {
		// if (tag.equals(this.vFirstBitmaps.get(i).strPath)) {
		// this.vFirstBitmaps.get(i).hRefLog.put(obj, name);
		// this.vFirstBitmaps.get(i).iRef++;
		// return this.vFirstBitmaps.get(i).bmpPic;
		// }
		// }
		// 根据图片的唯一标识来判断图片是否已经存在，以及判断图片被引用的次数
		if (this.hBitmaps.get(tag) != null
				&& tag.equals(this.hBitmaps.get(tag).strPath)) {
			this.hBitmaps.get(tag).hRefLog.put(obj, name);
			this.hBitmaps.get(tag).iRef++;
			return this.hBitmaps.get(tag).bmpPic; // 有了这一句话下面的就不用else咯
		}
		Node node = new Node();
		node.setAttribute(tag, bitmap, obj, name);
		// this.vFirstBitmaps.add(node);
		this.hBitmaps.put(tag, node);
		return bitmap; // 只是在管理中心给bitmap“备个案”，再把bitmap原封不动的给返回去
	}

	// 根据请求释放内存结构中相应的图片
	public int FreeLimitBmp(String tag, String obj) {
		if (null == tag || 0 == tag.trim().length() || null == obj
				|| 0 == obj.trim().length())
			return DEFAULT;

		// for (int i = 0; i < this.vFirstBitmaps.size(); i++) {
		// if (null != vFirstBitmaps.get(i).bmpPic && tag ==
		// vFirstBitmaps.get(i).strPath) {
		// if (!vFirstBitmaps.get(i).bmpPic.isRecycled()) {
		// vFirstBitmaps.get(i).bmpPic.recycle();
		// this.vFirstBitmaps.get(i).hRefLog.remove(obj);
		// }
		// }
		// }
		if (null != this.hBitmaps.get(tag)) {
			if (tag.equals(this.hBitmaps.get(tag).strPath)
					&& null != this.hBitmaps.get(tag).bmpPic) {
				this.hBitmaps.get(tag).hRefLog.remove(obj);// 在这里不对Node.iRef进行减一吗
				if (0 == checkBitmapNum(tag)) {
					if (!this.hBitmaps.get(tag).bmpPic.isRecycled()) {
						this.hBitmaps.get(tag).bmpPic.recycle();
						this.hBitmaps.remove(tag);
					}
				}
			}
		}
		/*
		 * else if (tag != "") { for (int i = 0; i < this.vSecondBitmaps.size();
		 * i++) { if (null != vSecondBitmaps.get(i) ) { if
		 * (!vSecondBitmaps.get(i).isRecycled()) {
		 * vSecondBitmaps.get(i).recycle(); } } }
		 * this.vSecondBitmaps.removeAllElements(); }else if (tag !="") { for
		 * (int i = 0; i < this.vThirdBitmaps.size(); i++) { if (null !=
		 * vThirdBitmaps.get(i) ) { if (!vThirdBitmaps.get(i).isRecycled()) {
		 * vThirdBitmaps.get(i).recycle(); } } }
		 * this.vThirdBitmaps.removeAllElements(); }
		 */
		return SUCCESS;

	}

	// 检查某张图片的引用次数
	public int checkBitmapNum(String tag) {
		if (null == tag || 0 == tag.trim().length())
			return DEFAULT;
		int num = -1;
		if (tag.equals(this.hBitmaps.get(tag).strPath))
			num = this.hBitmaps.get(tag).hRefLog.size();
		return num;
	}

	// 检查某个引用是否还在内存空间中
	public String checkBitmapRef(String tag, String obj) {
		String strRef = null;
		if (null == tag || 0 == tag.trim().length() || null == obj
				|| 0 == obj.trim().length())
			return null;
		strRef = this.hBitmaps.get(tag).hRefLog.get(obj);
		return strRef;
	}

	public void freeBmp(Bitmap curBitmap) {
		if (null != curBitmap) {
			if (!curBitmap.isRecycled()) {
				try {
					curBitmap.recycle();
				} catch (Exception e) {
				}
			}
			curBitmap = null;
		}
	}

}
