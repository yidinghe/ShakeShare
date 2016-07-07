package com.thesis.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ContactDao {

	private ContactSQLiteOpenHelper helper;

	public ContactDao(Context context) {
		helper = new ContactSQLiteOpenHelper(context);
	}
	
//	public long add(String name, String key) {
//		SQLiteDatabase db = helper.getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put("name", name);
//		values.put("key", key);
//		long id = db.insert("contact", null, values);
//		db.close();
//		return id;
//	}
	
	public long addContactName(String contactname, String username) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", contactname);
		long id = db.insert(username, null, values);
		db.close();
		return id;
	}
	
	public long updateContactKey(String contactkey, String contactname, String username) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("key", contactkey);
		int number = db.update(username, values, "name = ?",
				new String[] { contactname });
		db.close();
		return number;
	}

//	public boolean find(String name) {
//		SQLiteDatabase db = helper.getReadableDatabase();
//		Cursor cursor = db.query("contact", null, "name = ?",
//				new String[] { name }, null, null, null);
//		boolean result = cursor.moveToNext();
//		cursor.close();
//		db.close();
//		return result;
//	}
	
	public boolean find(String username, String contactname) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query(username, null, "name = ?",
				new String[] { contactname }, null, null, null);
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}

	public int update(String name, String newkey) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("key", newkey);
		int number = db.update("contact", values, "name = ?",
				new String[] { name });
		db.close();
		return number;
	}
	
	public int updateName(String username, String cotactname, String newname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", newname);
		int number = db.update(username, values, "name = ?",
				new String[] { cotactname });
		db.close();
		return number;
	}

	public int delete(String username, String contactname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int number = db.delete(username, "name=?", new String[] { contactname });
		db.close();
		return number;
	}

//	public List<Contact> findAll() {
//		SQLiteDatabase db = helper.getReadableDatabase();
//		List<Contact> contacts = new ArrayList<Contact>();
//		Cursor cursor = db.query("contact", new String[] { "name", "key" },
//				null, null, null, null, null);
//		while (cursor.moveToNext()) {
//			String name = cursor.getString(cursor.getColumnIndex("name"));
//			String key = cursor.getString(cursor.getColumnIndex("key"));
//			Contact c = new Contact(name, key);
//			contacts.add(c);
//		}
//		cursor.close();
//		db.close();
//		return contacts;
//	}
	
	public List<Contact> findAll(String username) {
		SQLiteDatabase db = helper.getReadableDatabase();
		List<Contact> contacts = new ArrayList<Contact>();
		Cursor cursor = db.query(username, new String[] { "name", "key" },
				null, null, null, null, null);
		while (cursor.moveToNext()) {
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String key = cursor.getString(cursor.getColumnIndex("key"));
			Contact c = new Contact(name, key);
			contacts.add(c);
		}
		cursor.close();
		db.close();
		return contacts;
	}
}
