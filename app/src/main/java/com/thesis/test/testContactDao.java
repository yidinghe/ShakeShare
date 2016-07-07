package com.thesis.test;

import com.thesis.db.dao.ContactSQLiteOpenHelper;

import android.test.AndroidTestCase;

public class testContactDao extends AndroidTestCase {
	
	public void testCreateDB() throws Exception{
		ContactSQLiteOpenHelper helper = new ContactSQLiteOpenHelper(getContext());
		helper.getWritableDatabase();
	}
	
//	public void testAdd() throws Exception{
//		ContactDao dao = new ContactDao(getContext());
//		dao.add("wangwu","123");
//	}
//	public void testFind() throws Exception{
//		ContactDao dao = new ContactDao(getContext());
//		boolean result = dao.find("wangwu");
//		assertEquals(true,result);
//	}
//	public void testUpdate() throws Exception{
//		ContactDao dao = new ContactDao(getContext());
//		dao.update("wangwu","321");
//	}
//	public void testDelete() throws Exception{
//		ContactDao dao = new ContactDao(getContext());
//		dao.delete("wangwu");
//	}
//	public void findAll() throws Exception{
//		ContactDao dao = new ContactDao(getContext());
//		List<Contact> contacts = dao.findAll();
//		for(Contact c:contacts){
//		System.out.println(c.toString());
//		}
//	}
}
