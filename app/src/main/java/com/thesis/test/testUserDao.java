package com.thesis.test;

import java.util.List;



import android.test.AndroidTestCase;

import com.thesis.db.dao.User;
import com.thesis.db.dao.UserDao;
import com.thesis.db.dao.UserSQLiteOpenHelper;

public class testUserDao extends AndroidTestCase {
	
	public void testCreateDB() throws Exception{
		UserSQLiteOpenHelper helper = new UserSQLiteOpenHelper(getContext());
		helper.getWritableDatabase();
	}
	
	public void testAdd() throws Exception{
		UserDao dao = new UserDao(getContext());
		dao.add("wangwu","123");
	}
	public void testFind() throws Exception{
		UserDao dao = new UserDao(getContext());
		String result = dao.findPassword("wangwu");
		System.out.println(result);
	}
	public void testUpdate() throws Exception{
		UserDao dao = new UserDao(getContext());
		dao.update("wangwu","321");
	}
	public void testDelete() throws Exception{
		UserDao dao = new UserDao(getContext());
		dao.delete("wangwu");
	}
	public void findAll() throws Exception{
		UserDao dao = new UserDao(getContext());
		List<User> users = dao.findAll();
		for(User u:users){
		System.out.println(u.toString());
		}
	}
}
