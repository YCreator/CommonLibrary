package com.frame.core.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbBaseService {

	protected Context ctx = null;
	protected SQLiteDatabase mSQLiteDatabase = null;

	public DbBaseService(Context ctx) {
		this.ctx = ctx.getApplicationContext();
		this.mSQLiteDatabase = DatabaseManager.getInstance().getSQLiteDatabase();
	}
	
	public void open() {
		mSQLiteDatabase = DatabaseManager.getInstance().openDatabase();
	}
	
	public void close() {
		DatabaseManager.getInstance().closeDatabase();
	}

}
