package com.thesis.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserSQLiteOpenHelper extends SQLiteOpenHelper {
	
	public UserSQLiteOpenHelper(Context context) {
		super (context,"user.db",null,1);
	}
	
	public void onCreate(SQLiteDatabase db){
		db.execSQL("create table user (username varchar(20), password varchar(20))");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	
	}
}
