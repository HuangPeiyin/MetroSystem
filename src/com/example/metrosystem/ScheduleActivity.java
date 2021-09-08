package com.example.metrosystem;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.ScheduleAdapter;
import com.example.db.DBAdapter;
import com.example.entity.Metro;
import com.example.entity.Schedule;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class ScheduleActivity extends Activity {
	private static DBAdapter dbAdapter;
	private Spinner spinnerMetroID;
	private static String[] md;
	private static int[] Mid;
	private Button ScheduleInsertBtn, ScheduleSelectBtn, ScheduleAllBtn;
	private EditText editStart, editEnd, editSID;
	private static TextView ScheduleNode;
	private static ListView ScheduleData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		setTitle("管理时间信息");
		setupView();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		showAllSchedule();
		initMetroID();
		chooseMetroID();
		
		// 增加时间信息按钮
		ScheduleInsertBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!isRight()){
					return;
				}
				Schedule s = new Schedule();
				int[] mid = chooseMetroID();
				boolean b = false;
				for(int i = 0 ; i < mid.length; i++){
					Log.d("添加", String.valueOf(mid[i]));
					s.setMid(mid[i]);
					s.setSstart(editStart.getText().toString());
					s.setSend(editEnd.getText().toString());
					long j = dbAdapter.insert(s);
					if(j != 0){
						b = true;
					}else{
						break;
					}
				}
				if(b == true){
					Toast.makeText(getApplicationContext(), "增加成功！", Toast.LENGTH_SHORT).show();
					showAllSchedule();
				}else{
					Toast.makeText(getApplicationContext(), "增加失败！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		// 选择时间ID信息按钮
		ScheduleSelectBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!right()){
					return;
				}
				showOneSchedule(Integer.valueOf(editSID.getText().toString()));
			}
		});
		
		// 查看所有地铁信息
		ScheduleAllBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showAllSchedule();
			}
		});
	}

	private void initMetroID(){
		Metro[] metro = dbAdapter.queryAllMetro();
		ArrayList<String> metroDirection = new ArrayList<String>();
		if(metro == null){
			Toast.makeText(getApplicationContext(), "没有可选的地铁ID，请前往‘管理地铁信息’进行添加地铁的相关信息", Toast.LENGTH_LONG).show();	
		}else{
			for(int i = 0; i < metro.length; i++){
				metroDirection.add(metro[i].getMdirection().toString().trim());
			}
			for(int i = 0; i < metroDirection.size()-1; i++){
				for(int j = metroDirection.size()-1 ; j > i; j--){
					if(metroDirection.get(i).equals(metroDirection.get(j))){
						metroDirection.remove(j);
					}
				}
			}
			md = new String[metroDirection.size()];
			for(int i = 0; i < metroDirection.size(); i++){
				md[i] = metroDirection.get(i);
			}
			ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item, md);
			spinnerMetroID.setAdapter(myAdapter);
		}
	}
	
	private int[] chooseMetroID(){
		this.spinnerMetroID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String direction = md[pos].toString();
				Log.d("地铁方向", direction);
				Metro[] metro = dbAdapter.queryMetro_Direction(direction);
				Mid = new int[metro.length];
				for(int i = 0; i < metro.length; i++){
					Mid[i] = Integer.valueOf(metro[i].getMID());
					Log.d("", String.valueOf(Mid[i]));
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(getApplicationContext(), "请选择地铁ID！", Toast.LENGTH_SHORT).show();
			}
		});
		return Mid;
	}
	
	private boolean right(){
		int sid = editSID.getText().toString().length();
		if(sid == 0){
			Toast.makeText(getApplicationContext(), "请输入时间ID！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private boolean isRight(){
		int start = editStart.getText().toString().length();
		int end = editEnd.getText().toString().length();
		if(start == 0 || end == 0){
			Toast.makeText(getApplicationContext(), "请输入发车时间和停车时间！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void showOneSchedule(int sid){
		Schedule[] s = dbAdapter.queryOneSchedule(sid);
		if(s == null){
			ScheduleNode.setText("数据库中没有该时间ID的数据！");
			Toast.makeText(getApplicationContext(), "查询无结果！", Toast.LENGTH_SHORT).show();
		}else{
			ScheduleNode.setText("数据库中的时间ID信息如下：");
			Toast.makeText(getApplicationContext(), "查询成功！", Toast.LENGTH_SHORT).show();
			List<Schedule> data = new ArrayList<Schedule>();
			data.add(s[0]);
			ScheduleAdapter adapter = new ScheduleAdapter(data);
			ScheduleData.setAdapter(adapter);
		}
	}
	
	public static void showAllSchedule(){
		Schedule[] s = dbAdapter.queryAllSchedule();
		if(s == null){
			ScheduleNode.setText("数据库中没有一条数据！");
		}else{
			ScheduleNode.setText("数据库中的时间ID信息如下：");
			List<Schedule> data = new ArrayList<Schedule>();
			for (int i = 0; i < s.length; i++) {
				data.add(s[i]);
			}
			ScheduleAdapter adapter = new ScheduleAdapter(data);
			ScheduleData.setAdapter(adapter);
		}
	}
	
	private void setupView() {
		spinnerMetroID = (Spinner) findViewById(R.id.spinnerMetroID);
		
		ScheduleInsertBtn = (Button) findViewById(R.id.ScheduleInsertBtn);
		ScheduleSelectBtn = (Button) findViewById(R.id.ScheduleSelectBtn);
		ScheduleAllBtn = (Button) findViewById(R.id.ScheduleAllBtn);

		editStart = (EditText) findViewById(R.id.editScheduleStart);
		editEnd = (EditText) findViewById(R.id.editScheduleEnd);
		editSID = (EditText) findViewById(R.id.editScheduleID);

		ScheduleNode = (TextView) findViewById(R.id.ScheduleNode);
		ScheduleData = (ListView) findViewById(R.id.listViewSchedule);
	}
}
