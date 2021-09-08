package com.example.metrosystem;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.CityAdapter;
import com.example.db.DBAdapter;
import com.example.entity.City;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CityActivity extends Activity {
	private static DBAdapter dbAdapter;
	private Button CityInsertBtn, CitySelectBtn, CityAllBtn;
	private EditText editLocation, editRouteNum, editID;
	private static TextView node;
	private static ListView CityData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);
		setTitle("管理城市信息");
		setupView();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		showAllCity();

		// 增加城市信息按钮
		CityInsertBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isRight()) {
					return;
				}
				City city = new City();
				city.setClocation(editLocation.getText().toString());
				city.setCroute_num(Integer.valueOf(editRouteNum.getText()
						.toString()));
				if(city.getCroute_num() instanceof Integer){
					long i = dbAdapter.insert(city);
					if (i != 0) {
						Toast.makeText(getApplicationContext(), "增加成功！",
								Toast.LENGTH_SHORT).show();
						showAllCity();
					} else {
						Toast.makeText(getApplicationContext(), "增加失败！",
								Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getApplicationContext(), "总路线数的填写必须为数字！", Toast.LENGTH_SHORT).show();
				}
			}
		});

		// 选择城市ID信息按钮
		CitySelectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!right()) {
					return;
				}
				showOneCity(Integer.valueOf(editID.getText().toString()));
			}
		});

		CityAllBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showAllCity();
			}
		});
	}

	private boolean right() {
		int cid = editID.getText().toString().length();
		if (cid == 0) {
			Toast.makeText(getApplicationContext(), "请输入城市ID！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private boolean isRight() {
		int clo = editLocation.getText().toString().length();
		int cnum = editRouteNum.getText().toString().length();
		if (clo == 0 || cnum == 0) {
			Toast.makeText(getApplicationContext(), "请输入地点和总路线数！",
					Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void showOneCity(int cid) {
		City[] city = dbAdapter.queryOneCity(cid);
		if (city == null) {
			node.setText("数据库中没有该城市ID的数据！");
			Toast.makeText(getApplicationContext(), "查询无结果！", Toast.LENGTH_SHORT).show();
		} else {
			node.setText("数据库中的城市ID信息如下：");
			Toast.makeText(getApplicationContext(), "查询成功！", Toast.LENGTH_SHORT).show();
			List<City> data = new ArrayList<City>();
			data.add(city[0]);
			CityAdapter adapter = new CityAdapter(data);
			CityData.setAdapter(adapter);
		}
	}

	public static void showAllCity() {
		City[] citys = dbAdapter.queryAllCity();
		if (citys == null) {
			node.setText("数据库中没有一条数据！");
		} else {
			node.setText("数据库中的城市ID信息如下：");
			List<City> data = new ArrayList<City>();
			for(int i = 0; i < citys.length; i++){
				data.add(citys[i]);
			}
			CityAdapter adapter = new CityAdapter(data);
			CityData.setAdapter(adapter);
		}
	}

	private void setupView() {
		CityInsertBtn = (Button) findViewById(R.id.CityInsertBtn);
		CitySelectBtn = (Button) findViewById(R.id.CitySelectBtn);
		CityAllBtn = (Button) findViewById(R.id.CityAllBtn);

		editLocation = (EditText) findViewById(R.id.editCityLocation);
		editRouteNum = (EditText) findViewById(R.id.editCityRouteNumber);
		editID = (EditText) findViewById(R.id.editCityID);

		node = (TextView) findViewById(R.id.CityNote);
		CityData = (ListView) findViewById(R.id.listViewCity);
	}
}
