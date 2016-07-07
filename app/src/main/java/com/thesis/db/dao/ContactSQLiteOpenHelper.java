package com.thesis.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactSQLiteOpenHelper extends SQLiteOpenHelper {
	
	public ContactSQLiteOpenHelper(Context context) {
		super(context, "contact.db", null, 1);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table contact (name varchar(20), key varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
