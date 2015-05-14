package com.myTaskListItem.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class SqlHelper {

	private static SqlHelper sqlHelper;
	private Helper helper;
	static String name = "MYSAMSUNG.DB";
	static CursorFactory factory = null;
	static int version = 1;

	private final static String DB_MY_SUB = "create table if not exists mysub(_id integer primary key,_sub integer)";

	private SqlHelper(Context context) {
		if (null == helper) {
			helper = new Helper(context);
		}
	}

	public static synchronized SqlHelper getInstance(Context context) {
		if (null == sqlHelper) {
			sqlHelper = new SqlHelper(context);
		}
		return sqlHelper;
	}

	static class Helper extends android.database.sqlite.SQLiteOpenHelper {

		android.database.sqlite.SQLiteDatabase database;

		public Helper(Context context) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
			database = this.getWritableDatabase();
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(DB_MY_SUB);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("drop table DB_MY_SUB if exists");
		}
	}

	public void close() {
		if (null != sqlHelper && null != helper.database) {
			helper.database.close();
		}
		sqlHelper = null;
	}

	public boolean insertSub(int sub_id) {
		try {
			String sql = "insert into mysub(_sub)values(?)";
			helper.database.execSQL(sql, new Object[] { sub_id });
		} catch (android.database.sqlite.SQLiteException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public int[] getSub() {
		String sql = "select * from mysub";
		int[] sub_id;
		Cursor cursor = helper.database.rawQuery(sql, null);
		sub_id = new int[cursor.getCount()];
		int i = 0;
		while (cursor.moveToNext()) {
			sub_id[i] = cursor.getInt(cursor.getColumnIndex("_sub"));
			i++;
		}
		cursor.close();
		return sub_id;
	}

	public int getSubById(int id) {
		String sql = "select _id from mysub where _id=?";
		Cursor cursor = helper.database.rawQuery(sql, new String[] { id + "" });
		if (cursor.moveToNext()) {
			cursor.close();
			return cursor.getInt(0);
		} else {
			cursor.close();
			return -1;
		}
	}
}
