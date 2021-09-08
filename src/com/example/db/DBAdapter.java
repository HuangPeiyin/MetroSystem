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
	private static final String DB_NAME = "metro.db";// ���ݿ���
	public static final String VISITOR_TABLE = "visitor";// �ο���Ϣ��
	public static final String CITY_TABLE = "city";// ������Ϣ��
	public static final String METRO_TABLE = "metro";// ������Ϣ��
	public static final String SCHEDULE_TABLE = "schedule";// ʱ���
	private static final int DB_VERSION = 1;// ���ݿ�汾��
	
	public static final String VISITOR_ID = "Vid";// �οͱ��
	public static final String VISITOR_Phone = "Vphone";// �˺�
	public static final String VISITOR_Password = "Vpassword";// ����
	public static final String VISITOR_Nickname = "Vnickname";// �ǳ�

	public static final String CITY_ID = "Cid";// ���б��
	public static final String CITY_location = "Clocation";// �ص�
	public static final String CITY_route_num = "Croute_num";// ��·����

	public static final String METRO_ID = "Mid";// �������
	public static final String METRO_route = "Mroute";// ·��
	public static final String METRO_direction = "Mdirection";// ����
	public static final String METRO_start = "Mstart";// ���
	public static final String METRO_destination = "Mdestination";// �յ�
	public static final String METRO_number = "Mnumber";// ;��վ��
	public static final String METRO_duration = "Mduration";// ����ʱ��
	public static final String METRO_price = "Mprice";// Ʊ��

	public static final String SCHEDULE_ID = "Sid";// ʱ����
	public static final String SCHEDULE_start = "Sstart";// ����ʱ��
	public static final String SCHEDULE_end = "Send";// ͣ��ʱ��

	private static SQLiteDatabase db;
	private Context mcontext;
	private DBOpenHelper dbOpenHelper;

	public DBAdapter(Context context) {
		mcontext = context;
	}

	public void open() throws SQLiteException {
		// ����һ��DBOpenHelperʵ��
		dbOpenHelper = new DBOpenHelper(mcontext, DB_NAME, null, DB_VERSION);
		try {
			db = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			db = dbOpenHelper.getReadableDatabase();
		}
	}

	// �ر����ݿ�
	public void close() {
		if (db != null) {
			db.close();
			db = null;
		}
	}
	
	/**
	 * ���οͱ����һ����Ϣ
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
	 * ����б����һ����Ϣ
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
	 * ����������һ������
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
	 * ��ʱ������һ������
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

	// ͨ��CITY_IDɾ��һ��������Ϣ
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

	// ͨ��METRO_IDɾ��һ��������Ϣ
	public long deleteOneMetro(int mid) {
		Schedule[] sche = queryOneSchedule_Metro(mid);
		if(sche != null){
			db.delete(SCHEDULE_TABLE, METRO_ID + "=" + mid, null);
		}
		return db.delete(METRO_TABLE, METRO_ID + "=" + mid, null);
	}
	
	// ͨ��SCHEDULE_IDɾ��һ��ʱ����Ϣ
	public long deleteOneSchedule(int sid){
		return db.delete(SCHEDULE_TABLE, SCHEDULE_ID + "=" + sid, null);
	}
	
	// �޸��οͱ��е�����
	public long updateOneVisitor(Visitor visitor){
		ContentValues newValues = new ContentValues();
		newValues.put(VISITOR_Phone, visitor.getPhone());
		newValues.put(VISITOR_Password, visitor.getPassword());
		newValues.put(VISITOR_Nickname, visitor.getNickname());
		return db.update(VISITOR_TABLE, newValues, VISITOR_ID + "=" + visitor.getVid(), null);
	}
	
	// �޸�ָ���ο͵�����
	public long updateOneVisitor(String phone, Visitor visitor){
		ContentValues newValues = new ContentValues();
		newValues.put(VISITOR_Password, visitor.getPassword());
		return db.update(VISITOR_TABLE, newValues, VISITOR_Phone + "='" + phone + "'", null);
	}

	// �޸ĳ��б��е�����
	public long updateOneCity(City city) {
		ContentValues newValues = new ContentValues();
		newValues.put(CITY_location, city.getClocation());
		newValues.put(CITY_route_num, city.getCroute_num());
		return db.update(CITY_TABLE, newValues, CITY_ID + "=" + city.getCID(), null);
	}

	// �޸ĵ������е�����
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
	
	// �޸�ʱ����е�����
	public long updateOneSchedule(int mid, Schedule schedule) {
		ContentValues newValues = new ContentValues();
		newValues.put(SCHEDULE_start, schedule.getSstart());
		newValues.put(SCHEDULE_end, schedule.getSend());
		return db.update(SCHEDULE_TABLE, newValues, SCHEDULE_ID + "=" + schedule.getSid() 
				+ " And " + METRO_ID + "=" + mid, null);
	}

	// ��ѯCityΪid�ĳ�����Ϣ
	public City[] queryOneCity(long cid) {
		Cursor result = db.query(CITY_TABLE, null, CITY_ID + "=" + cid, null,
				null, null, null, null);
		return ConvertToCity(result);
	}

	// ��ѯMetroΪid�ĵ�����Ϣ
	public Metro[] queryOneMetro(long id) {
		Cursor result = db.query(METRO_TABLE, null, METRO_ID + "=" + id,
				null, null, null, null);
		return ConvertToMetro(result);
	}
	
	// ��ѯCityΪid�ĵ�����Ϣ
	public Metro[] queryOneMetro_City(long id){
		Cursor result = db.query(METRO_TABLE, null, CITY_ID + "=" + id,
				null, null, null, null);
		return ConvertToMetro(result);
	}
	
	// ��ѯMetroΪDirection�ĵ�����Ϣ
	public Metro[] queryMetro_Direction(String d){
		Cursor result = db.query(METRO_TABLE, null, METRO_direction + "='" + d + "'", 
				null, null, null, null);
		return ConvertToMetro(result);
	}

	// ��ѯScheduleΪid��ʱ����Ϣ
	public Schedule[] queryOneSchedule(long id) {
		Cursor result = db.query(SCHEDULE_TABLE, null, SCHEDULE_ID + "=" + id,
				null, null, null, null);
		return ConvertToSchedule(result);
	}
	
	// ��ѯMetroΪid��ʱ����Ϣ
	public Schedule[] queryOneSchedule_Metro(long id){
		Cursor result = db.query(SCHEDULE_TABLE, null, METRO_ID + "=" + id,
				null, null, null, null);
		return ConvertToSchedule(result);
	}
	
	// ��ѯVisitorΪid���ο���Ϣ
	public Visitor[] queryOneVisitor(long id){
		Cursor result = db.query(VISITOR_TABLE, null, VISITOR_ID + "=" + id,
				null, null, null, null);
		return ConvertToVisitor(result);
	}
	
	// ��ѯVisitor��phone��password���ο���Ϣ
	public Visitor[] queryOneVisitor(String phone){
		Cursor result = db.query(VISITOR_TABLE, null, VISITOR_Phone + "='" + phone + "'",
				null, null, null, null);
		return ConvertToVisitor(result);
	}
	
	// ��ѯ���г�����Ϣ
	public City[] queryAllCity() {
		Cursor result = db.query(CITY_TABLE, 
				new String[]{CITY_ID, CITY_location, CITY_route_num}, null,
				null, null, null, null);
		return ConvertToCity(result);
	}

	// ��ѯ���е�����Ϣ
	public Metro[] queryAllMetro() {
		Cursor result = db.query(METRO_TABLE,
				new String[] { METRO_ID, CITY_ID, METRO_route, METRO_direction,
						METRO_start, METRO_destination, METRO_number, METRO_duration,
						METRO_price }, null, null, null, null, null);
		return ConvertToMetro(result);
	}

	// ��ѯ����ʱ����Ϣ
	public Schedule[] queryAllSchedule() {
		Cursor result = db.query(SCHEDULE_TABLE, new String[] { SCHEDULE_ID,
				SCHEDULE_start, SCHEDULE_end, METRO_ID }, null, null, null, null, null);
		return ConvertToSchedule(result);
	}
	
	// ��ȡ�ο���Ϣ
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

	// ��ȡ������Ϣ
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

	// ��ȡ������Ϣ
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

	// ��ȡʱ����Ϣ
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
	 * ��̬Helper�࣬���ڽ��������ºʹ����ݿ�
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

		// ���û�����DBOpenHelper�Ĺ��캯�������Զ����������onCreate(SQLiteDatabase db)����
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
