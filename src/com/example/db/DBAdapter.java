package com.example.db;

import com.example.entity.City;
import com.example.entity.Metro;
import com.example.entity.Schedule;
import com.example.entity.Visitor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBAdapter {
	private static final String DB_NAME = "metro.db";// 数据库名
	public static final String VISITOR_TABLE = "visitor";// 游客信息表
	public static final String CITY_TABLE = "city";// 城市信息表
	public static final String METRO_TABLE = "metro";// 地铁信息表
	public static final String SCHEDULE_TABLE = "schedule";// 时间表
	private static final int DB_VERSION = 1;// 数据库版本号
	
	public static final String VISITOR_ID = "Vid";// 游客编号
	public static final String VISITOR_Phone = "Vphone";// 账号
	public static final String VISITOR_Password = "Vpassword";// 密码
	public static final String VISITOR_Nickname = "Vnickname";// 昵称

	public static final String CITY_ID = "Cid";// 城市编号
	public static final String CITY_location = "Clocation";// 地点
	public static final String CITY_route_num = "Croute_num";// 总路线数

	public static final String METRO_ID = "Mid";// 地铁编号
	public static final String METRO_route = "Mroute";// 路线
	public static final String METRO_direction = "Mdirection";// 方向
	public static final String METRO_start = "Mstart";// 起点
	public static final String METRO_destination = "Mdestination";// 终点
	public static final String METRO_number = "Mnumber";// 途径站数
	public static final String METRO_duration = "Mduration";// 经历时长
	public static final String METRO_price = "Mprice";// 票价

	public static final String SCHEDULE_ID = "Sid";// 时间编号
	public static final String SCHEDULE_start = "Sstart";// 发车时间
	public static final String SCHEDULE_end = "Send";// 停车时间

	private static SQLiteDatabase db;
	private Context mcontext;
	private DBOpenHelper dbOpenHelper;

	public DBAdapter(Context context) {
		mcontext = context;
	}

	public void open() throws SQLiteException {
		// 创建一个DBOpenHelper实例
		dbOpenHelper = new DBOpenHelper(mcontext, DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			db = dbOpenHelper.getReadableDatabase();
		}
	}

	// 关闭数据库
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
	
	/**
	 * 向游客表插入一条信息
	 * @param visitor
	 * @return
	 */
	public long insert(Visitor visitor){
		ContentValues newValues = new ContentValues();

		newValues.put(VISITOR_Phone, visitor.getPhone());
		newValues.put(VISITOR_Password, visitor.getPassword());
		newValues.put(VISITOR_Nickname, visitor.getNickname());

		return db.insert(VISITOR_TABLE, null, newValues);
	}

	/**
	 * 向城市表插入一条信息
	 * @param person
	 * @param id
	 * @return
	 */
	public long insert(City city) {
		ContentValues newValues = new ContentValues();

		newValues.put(CITY_location, city.getClocation());
		newValues.put(CITY_route_num, Integer.valueOf(city.getCroute_num()));

		return db.insert(CITY_TABLE, null, newValues);
	}

	/**
	 * 向地铁表插入一条数据
	 * @param course
	 * @return
	 */
	public long insert(Metro metro) {
		ContentValues newValues = new ContentValues();

		newValues.put(CITY_ID, Integer.valueOf(metro.getCID()));
		newValues.put(METRO_route, metro.getMroute());
		newValues.put(METRO_direction, metro.getMdirection());
		newValues.put(METRO_start, metro.getMstart());
		newValues.put(METRO_destination, metro.getMdestination());
		newValues.put(METRO_number, Integer.valueOf(metro.getMnumber()));
		newValues.put(METRO_duration, Float.valueOf(metro.getMduration()));
		newValues.put(METRO_price, Float.valueOf(metro.getMprice()));

		return db.insert(METRO_TABLE, null, newValues);
	}

	/**
	 * 向时间表插入一条数据
	 * @param sc
	 * @param id
	 * @return
	 */
	public long insert(Schedule schedule) {
		ContentValues newValues = new ContentValues();

		newValues.put(SCHEDULE_start, schedule.getSstart());
		newValues.put(SCHEDULE_end, schedule.getSend());
		newValues.put(METRO_ID, Integer.valueOf(schedule.getMid()));

		return db.insert(SCHEDULE_TABLE, null, newValues);
	}

	// 通过CITY_ID删除一条城市信息
	public long deleteOneCity(int cid) {
		Metro[] metro = queryOneMetro_City(cid);
		if(metro != null){
			for(int i = 0; i < metro.length; i++){
				Schedule[] sche = queryOneSchedule_Metro(metro[i].getMID());
				if(sche != null){
					db.delete(SCHEDULE_TABLE, METRO_ID + "=" + metro[i].getMID(), null);
					continue;
				}
			}
			db.delete(METRO_TABLE, CITY_ID + "=" + cid, null);
		}
		return db.delete(CITY_TABLE, CITY_ID + "=" + cid, null);
	}

	// 通过METRO_ID删除一条地铁信息
	public long deleteOneMetro(int mid) {
		Schedule[] sche = queryOneSchedule_Metro(mid);
		if(sche != null){
			db.delete(SCHEDULE_TABLE, METRO_ID + "=" + mid, null);
		}
		return db.delete(METRO_TABLE, METRO_ID + "=" + mid, null);
	}
	
	// 通过SCHEDULE_ID删除一条时间信息
	public long deleteOneSchedule(int sid){
		return db.delete(SCHEDULE_TABLE, SCHEDULE_ID + "=" + sid, null);
	}
	
	// 修改游客表中的数据
	public long updateOneVisitor(Visitor visitor){
		ContentValues newValues = new ContentValues();
		newValues.put(VISITOR_Phone, visitor.getPhone());
		newValues.put(VISITOR_Password, visitor.getPassword());
		newValues.put(VISITOR_Nickname, visitor.getNickname());
		return db.update(VISITOR_TABLE, newValues, VISITOR_ID + "=" + visitor.getVid(), null);
	}
	
	// 修改指定游客的密码
	public long updateOneVisitor(String phone, Visitor visitor){
		ContentValues newValues = new ContentValues();
		newValues.put(VISITOR_Password, visitor.getPassword());
		return db.update(VISITOR_TABLE, newValues, VISITOR_Phone + "='" + phone + "'", null);
	}

	// 修改城市表中的数据
	public long updateOneCity(City city) {
		ContentValues newValues = new ContentValues();
		newValues.put(CITY_location, city.getClocation());
		newValues.put(CITY_route_num, city.getCroute_num());
		return db.update(CITY_TABLE, newValues, CITY_ID + "=" + city.getCID(), null);
	}

	// 修改地铁表中的数据
	public long updateOneMetro(int cid, Metro metro) {
		ContentValues newValues = new ContentValues();
		newValues.put(METRO_route, metro.getMroute());
		newValues.put(METRO_direction, metro.getMdirection());
		newValues.put(METRO_start, metro.getMstart());
		newValues.put(METRO_destination, metro.getMdestination());
		newValues.put(METRO_number, Integer.valueOf(metro.getMnumber()));
		newValues.put(METRO_duration, Float.valueOf(metro.getMduration()));
		newValues.put(METRO_price, Float.valueOf(metro.getMprice()));
		return db.update(METRO_TABLE, newValues, METRO_ID + "=" + metro.getMID()
				+ " And " + CITY_ID + "=" + cid, null);
	}
	
	// 修改时间表中的数据
	public long updateOneSchedule(int mid, Schedule schedule) {
		ContentValues newValues = new ContentValues();
		newValues.put(SCHEDULE_start, schedule.getSstart());
		newValues.put(SCHEDULE_end, schedule.getSend());
		return db.update(SCHEDULE_TABLE, newValues, SCHEDULE_ID + "=" + schedule.getSid() 
				+ " And " + METRO_ID + "=" + mid, null);
	}

	// 查询City为id的城市信息
	public City[] queryOneCity(long cid) {
		Cursor result = db.query(CITY_TABLE, null, CITY_ID + "=" + cid, null,
				null, null, null, null);
		return ConvertToCity(result);
	}

	// 查询Metro为id的地铁信息
	public Metro[] queryOneMetro(long id) {
		Cursor result = db.query(METRO_TABLE, null, METRO_ID + "=" + id,
				null, null, null, null);
		return ConvertToMetro(result);
	}
	
	// 查询City为id的地铁信息
	public Metro[] queryOneMetro_City(long id){
		Cursor result = db.query(METRO_TABLE, null, CITY_ID + "=" + id,
				null, null, null, null);
		return ConvertToMetro(result);
	}
	
	// 查询Metro为Direction的地铁信息
	public Metro[] queryMetro_Direction(String d){
		Cursor result = db.query(METRO_TABLE, null, METRO_direction + "='" + d + "'", 
				null, null, null, null);
		return ConvertToMetro(result);
	}

	// 查询Schedule为id的时间信息
	public Schedule[] queryOneSchedule(long id) {
		Cursor result = db.query(SCHEDULE_TABLE, null, SCHEDULE_ID + "=" + id,
				null, null, null, null);
		return ConvertToSchedule(result);
	}
	
	// 查询Metro为id的时间信息
	public Schedule[] queryOneSchedule_Metro(long id){
		Cursor result = db.query(SCHEDULE_TABLE, null, METRO_ID + "=" + id,
				null, null, null, null);
		return ConvertToSchedule(result);
	}
	
	// 查询Visitor为id的游客信息
	public Visitor[] queryOneVisitor(long id){
		Cursor result = db.query(VISITOR_TABLE, null, VISITOR_ID + "=" + id,
				null, null, null, null);
		return ConvertToVisitor(result);
	}
	
	// 查询Visitor的phone和password的游客信息
	public Visitor[] queryOneVisitor(String phone){
		Cursor result = db.query(VISITOR_TABLE, null, VISITOR_Phone + "='" + phone + "'",
				null, null, null, null);
		return ConvertToVisitor(result);
	}
	
	// 查询所有城市信息
	public City[] queryAllCity() {
		Cursor result = db.query(CITY_TABLE, 
				new String[]{CITY_ID, CITY_location, CITY_route_num}, null,
				null, null, null, null);
		return ConvertToCity(result);
	}

	// 查询所有地铁信息
	public Metro[] queryAllMetro() {
		Cursor result = db.query(METRO_TABLE,
				new String[] { METRO_ID, CITY_ID, METRO_route, METRO_direction,
						METRO_start, METRO_destination, METRO_number, METRO_duration,
						METRO_price }, null, null, null, null, null);
		return ConvertToMetro(result);
	}

	// 查询所有时间信息
	public Schedule[] queryAllSchedule() {
		Cursor result = db.query(SCHEDULE_TABLE, new String[] { SCHEDULE_ID,
				SCHEDULE_start, SCHEDULE_end, METRO_ID }, null, null, null, null, null);
		return ConvertToSchedule(result);
	}
	
	// 获取游客信息
	private Visitor[] ConvertToVisitor(Cursor cursor){
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Visitor[] visitor = new Visitor[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			visitor[i] = new Visitor();
			visitor[i].setVid(cursor.getInt(cursor.getColumnIndex(VISITOR_ID)));
			visitor[i].setPhone(cursor.getString(cursor.getColumnIndex(VISITOR_Phone)));
			visitor[i].setPassword(cursor.getString(cursor.getColumnIndex(VISITOR_Password)));
			visitor[i].setNickname(cursor.getString(cursor.getColumnIndex(VISITOR_Nickname)));
			cursor.moveToNext();
		}
		return visitor;
	}

	// 获取城市信息
	private City[] ConvertToCity(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		City[] city = new City[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			city[i] = new City();
			city[i].setCID(cursor.getInt(cursor.getColumnIndex(CITY_ID)));
			city[i].setClocation(cursor.getString(cursor
					.getColumnIndex(CITY_location)));
			city[i].setCroute_num(cursor.getInt(cursor
					.getColumnIndex(CITY_route_num)));
			cursor.moveToNext();
		}
		return city;
	}

	// 获取地铁信息
	public Metro[] ConvertToMetro(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if (resultCounts == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Metro[] metro = new Metro[resultCounts];
		for (int i = 0; i < resultCounts; i++) {
			metro[i] = new Metro();
			metro[i].setMID(cursor.getInt(cursor.getColumnIndex(METRO_ID)));
			metro[i].setCID(cursor.getInt(cursor.getColumnIndex(CITY_ID)));
			metro[i].setMroute(cursor.getString(cursor.getColumnIndex(METRO_route)));
			metro[i].setMdirection(cursor.getString(cursor.getColumnIndex(METRO_direction)));
			metro[i].setMstart(cursor.getString(cursor.getColumnIndex(METRO_start)));
			metro[i].setMdestination(cursor.getString(cursor.getColumnIndex(METRO_destination)));
			metro[i].setMnumber(cursor.getInt(cursor.getColumnIndex(METRO_number)));
			metro[i].setMduration(cursor.getFloat(cursor.getColumnIndex(METRO_duration)));
			metro[i].setMprice(cursor.getFloat(cursor.getColumnIndex(METRO_price)));
			cursor.moveToNext();
		}
		return metro;
	}

	// 获取时间信息
	public Schedule[] ConvertToSchedule(Cursor cursor) {
		int resultSC = cursor.getCount();
		if (resultSC == 0 || !cursor.moveToFirst()) {
			return null;
		}
		Schedule[] schedule = new Schedule[resultSC];
		for (int i = 0; i < resultSC; i++) {
			schedule[i] = new Schedule();
			schedule[i].setSid(cursor.getInt(cursor.getColumnIndex(SCHEDULE_ID)));
			schedule[i].setSstart(cursor.getString(cursor.getColumnIndex(SCHEDULE_start)));
			schedule[i].setSend(cursor.getString(cursor.getColumnIndex(SCHEDULE_end)));
			schedule[i].setMid(cursor.getInt(cursor.getColumnIndex(METRO_ID)));
			cursor.moveToNext();
		}
		return schedule;
	}

	/**
	 * 静态Helper类，用于建立、更新和打开数据库
	 */
	private static class DBOpenHelper extends SQLiteOpenHelper {
		private static final String VISITOR_CREATE = "CREATE TABLE " + VISITOR_TABLE 
				+ "(" + VISITOR_ID + " Integer primary key AUTOINCREMENT, "
				+ VISITOR_Phone + " text not null, " + VISITOR_Password
				+ " text not null, " + VISITOR_Nickname + " text not null);";
		private static final String CITY_CREATE = "CREATE TABLE " + CITY_TABLE 
				+ "(" + CITY_ID + " Integer primary key AUTOINCREMENT, "
				+ CITY_location + " text not null, " + CITY_route_num
				+ " Integer not null);";
		private static final String METRO_CREATE = "CREATE TABLE " + METRO_TABLE 
				+ "(" + METRO_ID + " Integer primary key AUTOINCREMENT, "
				+ CITY_ID + " Integer not null, "
				+ METRO_route + " text not null, " + METRO_direction + " text not null, "
				+ METRO_start + " text not null, " + METRO_destination + " text not null, "
				+ METRO_number + " Integer not null, " + METRO_duration + " float not null, "
				+ METRO_price + " float not null);";
		private static final String SCHEDULE_CREATE = "CREATE TABLE " + SCHEDULE_TABLE
				+ "(" + SCHEDULE_ID + " Integer primary key AUTOINCREMENT, " 
				+ METRO_ID + " Integer not null, "
				+ SCHEDULE_start + " text, " + SCHEDULE_end + " text);";

		// 在用户创建DBOpenHelper的构造函数，其自动调用自身的onCreate(SQLiteDatabase db)函数
		public DBOpenHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CITY_CREATE);
			db.execSQL(METRO_CREATE);
			db.execSQL(SCHEDULE_CREATE);
			db.execSQL(VISITOR_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
			_db.execSQL("DROP TABLE IF EXISTS " + CITY_TABLE);
			_db.execSQL("DROP TABLE IF EXISTS " + METRO_TABLE);
			_db.execSQL("DROP TABLE IF EXISTS " + SCHEDULE_TABLE);
			_db.execSQL("DROP TABLE IF EXISTS " + VISITOR_TABLE);
			onCreate(_db);
		}
	}
}
