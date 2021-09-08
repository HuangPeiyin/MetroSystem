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
		setTitle("����ʱ����Ϣ");
		setupView();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		showAllSchedule();
		initMetroID();
		chooseMetroID();
		
		// ����ʱ����Ϣ��ť
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
					Log.d("���", String.valueOf(mid[i]));
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
					Toast.makeText(getApplicationContext(), "���ӳɹ���", Toast.LENGTH_SHORT).show();
					showAllSchedule();
				}else{
					Toast.makeText(getApplicationContext(), "����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		// ѡ��ʱ��ID��Ϣ��ť
		ScheduleSelectBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!right()){
					return;
				}
				showOneSchedule(Integer.valueOf(editSID.getText().toString()));
			}
		});
		
		// �鿴���е�����Ϣ
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
			Toast.makeText(getApplicationContext(), "û�п�ѡ�ĵ���ID����ǰ�������������Ϣ��������ӵ����������Ϣ", Toast.LENGTH_LONG).show();	
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
				Log.d("��������", direction);
				Metro[] metro = dbAdapter.queryMetro_Direction(direction);
				Mid = new int[metro.length];
				for(int i = 0; i < metro.length; i++){
					Mid[i] = Integer.valueOf(metro[i].getMID());
					Log.d("", String.valueOf(Mid[i]));
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(getApplicationContext(), "��ѡ�����ID��", Toast.LENGTH_SHORT).show();
			}
		});
		return Mid;
	}
	
	private boolean right(){
		int sid = editSID.getText().toString().length();
		if(sid == 0){
			Toast.makeText(getApplicationContext(), "������ʱ��ID��", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private boolean isRight(){
		int start = editStart.getText().toString().length();
		int end = editEnd.getText().toString().length();
		if(start == 0 || end == 0){
			Toast.makeText(getApplicationContext(), "�����뷢��ʱ���ͣ��ʱ�䣡", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void showOneSchedule(int sid){
		Schedule[] s = dbAdapter.queryOneSchedule(sid);
		if(s == null){
			ScheduleNode.setText("���ݿ���û�и�ʱ��ID�����ݣ�");
			Toast.makeText(getApplicationContext(), "��ѯ�޽����", Toast.LENGTH_SHORT).show();
		}else{
			ScheduleNode.setText("���ݿ��е�ʱ��ID��Ϣ���£�");
			Toast.makeText(getApplicationContext(), "��ѯ�ɹ���", Toast.LENGTH_SHORT).show();
			List<Schedule> data = new ArrayList<Schedule>();
			data.add(s[0]);
			ScheduleAdapter adapter = new ScheduleAdapter(data);
			ScheduleData.setAdapter(adapter);
		}
	}
	
	public static void showAllSchedule(){
		Schedule[] s = dbAdapter.queryAllSchedule();
		if(s == null){
			ScheduleNode.setText("���ݿ���û��һ�����ݣ�");
		}else{
			ScheduleNode.setText("���ݿ��е�ʱ��ID��Ϣ���£�");
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
