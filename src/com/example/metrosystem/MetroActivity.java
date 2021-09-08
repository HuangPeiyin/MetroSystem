package com.example.metrosystem;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.MetroAdapter;
import com.example.db.DBAdapter;
import com.example.entity.City;
import com.example.entity.Metro;

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

public class MetroActivity extends Activity {
	private static DBAdapter dbAdapter;
	private static Spinner spinnerCityID;
	private static String[] cityID;
	private static int chooseID = -1;
	private Button MetroInsertBtn, MetroSelectBtn, MetroAllBtn;
	private EditText editRoute, editDirection, editStart, editDestination, editNumber, 
					editDuration, editPrice, editMetroID;
	private static TextView MetroNode;
	private static ListView MetroData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_metro);
		setTitle("管理地铁信息");
		setupView();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		showAllMetro();
		initCityID();
		chooseCityID();
		
		// 增加地铁信息按钮
		MetroInsertBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!isRight()){
					return;
				}
				Metro metro = new Metro();
				metro.setCID(chooseCityID());
				metro.setMroute(editRoute.getText().toString());
				metro.setMdirection(editDirection.getText().toString());
				metro.setMstart(editStart.getText().toString());
				metro.setMdestination(editDestination.getText().toString());
				metro.setMnumber(Integer.valueOf(editNumber.getText().toString()));
				metro.setMduration(Float.valueOf(editDuration.getText().toString()));
				metro.setMprice(Float.valueOf(editPrice.getText().toString()));
				long i = dbAdapter.insert(metro);
				if(i != 0){
					Toast.makeText(getApplicationContext(), "增加成功！", Toast.LENGTH_SHORT).show();
					showAllMetro();
				}else{
					Toast.makeText(getApplicationContext(), "增加失败！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		// 选择地铁ID信息按钮
		MetroSelectBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				if(!right()){
					return;
				}
				showOneMetro(Integer.valueOf(editMetroID.getText().toString()));
				
			}
		});
		
		// 查看所有地铁信息
		MetroAllBtn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				showAllMetro();
			}
		});
	}
	
	private void initCityID(){
		City[] city = dbAdapter.queryAllCity();
		if(city == null){
			Toast.makeText(getApplicationContext(), "没有可选的城市ID，请前往‘管理城市信息’进行添加城市的相关信息", Toast.LENGTH_LONG).show();	
		}else{
			cityID = new String[city.length];
			for(int i = 0; i < city.length; i++){
				cityID[i] = String.valueOf(city[i].getCID())+ " " + city[i].getClocation();
			}
			ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_spinner_dropdown_item, cityID);
			spinnerCityID.setAdapter(myAdapter);
		}
	}
	
	public static int chooseCityID(){
		MetroActivity.spinnerCityID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				String cid = cityID[pos].toString().split(" ")[0];
				chooseID = Integer.valueOf(cid);
				Log.d("城市ID", String.valueOf(chooseID));
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				Toast.makeText(null, "请选择城市ID！", Toast.LENGTH_SHORT).show();
			}
		});
		return chooseID;
	}
	
	private boolean right(){
		int mid = editMetroID.getText().toString().length();
		if(mid == 0){
			Toast.makeText(getApplicationContext(), "请输入地铁ID！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private boolean isRight(){
		int cid = chooseCityID();
		int route = editRoute.getText().toString().length();
		int direction = editDirection.getText().toString().length();
		int start = editStart.getText().toString().length();
		int destination = editDestination.getText().toString().length();
		int number = editNumber.getText().toString().length();
		int duration = editDuration.getText().toString().length();
		int price = editPrice.getText().toString().length();
		if(cid == -1 || route == 0 || direction == 0 || start == 0 || destination == 0 || 
				number == 0 || duration == 0 || price == 0){
			Toast.makeText(getApplicationContext(), "请输入路线、方向、起点、终点、途径站数、历经时长和票价！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void showOneMetro(int mid){
		Metro[] metro = dbAdapter.queryOneMetro(mid);
		if(metro == null){
			MetroNode.setText("数据库中没有该地铁ID的数据！");
			Toast.makeText(getApplicationContext(), "查询无结果！", Toast.LENGTH_SHORT).show();
		}else{
			MetroNode.setText("数据库中的地铁ID信息如下：");
			Toast.makeText(getApplicationContext(), "查询成功！", Toast.LENGTH_SHORT).show();
			List<Metro> data = new ArrayList<Metro>();
			data.add(metro[0]);
			MetroAdapter adapter = new MetroAdapter(data);
			MetroData.setAdapter(adapter);
		}
	}
	
	public static void showAllMetro(){
		Metro[] metros = dbAdapter.queryAllMetro();
		if(metros == null){
			MetroNode.setText("数据库中没有一条数据！");
		}else{
			MetroNode.setText("数据库中的地铁ID信息如下：");
			List<Metro> data = new ArrayList<Metro>();
			for (int i = 0; i < metros.length; i++) {
				data.add(metros[i]);
			}
			MetroAdapter adapter = new MetroAdapter(data);
			MetroData.setAdapter(adapter);
		}
	}
	
	private void setupView() {
		spinnerCityID = (Spinner) findViewById(R.id.spinnerCityID);
		
		MetroInsertBtn = (Button) findViewById(R.id.MetroInsertBtn);
		MetroSelectBtn = (Button) findViewById(R.id.MetroSelectBtn);
		MetroAllBtn = (Button) findViewById(R.id.MetroAllBtn);

		editRoute = (EditText) findViewById(R.id.editMetroRoute);
		editDirection = (EditText) findViewById(R.id.editMetroDirection);
		editStart = (EditText) findViewById(R.id.editMetroStart);
		editDestination = (EditText) findViewById(R.id.editMetroDestination);
		editNumber = (EditText) findViewById(R.id.editMetroNumber);
		editDuration = (EditText) findViewById(R.id.editMetroDuration);
		editPrice = (EditText) findViewById(R.id.editMetroPrice);
		editMetroID = (EditText) findViewById(R.id.editMetroID);

		MetroNode = (TextView) findViewById(R.id.MetroNode);
		MetroData = (ListView) findViewById(R.id.listViewMetro);
	}

}
