package com.thesis.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDao {

	private UserSQLiteOpenHelper helper;

	public UserDao(Context context) {
		helper = new UserSQLiteOpenHelper(context);
	}

	public void add(String username, String password) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("Insert into user (username,password) values (?,?)",
				new Object[] { username, password });
		db.close();
	}

	public boolean find(String username) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select * from user where username = ?",
				new String[] { username });
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}

	public String findPassword(String username) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("Select * from user where username = ?",
				new String[] { username });
		cursor.moveToNext();
		String password = cursor.getString(cursor.getColumnIndex("password"));
		cursor.close();
		db.close();
		return password;
	}

	public void update(String username, String password) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("update user set password = ? where username = ?",
				new Object[] { password, username });
		db.close();
	}

	public void delete(String username) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.execSQL("delete from user where username = ?",
				new Object[] { username });
		db.close();
	}

	public List<User> findAll() {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<User> users = new ArrayList<User>();
		Cursor cursor = db.rawQuery("select * from user", null);
		while (cursor.moveToNext()) {
			String username = cursor.getString(cursor
					.getColumnIndex("username"));
			String password = cursor.getString(cursor
					.getColumnIndex("password"));
			User u = new User(username, password);
			users.add(u);
		}
		cursor.close();
		db.close();
		return users;
	}

	public void deleteAll() {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from user", null);
		while (cursor.moveToNext()) {
			String username = cursor.getString(cursor
					.getColumnIndex("username"));
			db.execSQL("delete from user where username = ?",
					new Object[] { username });
		}
		cursor.close();
		db.close();
	}
}
