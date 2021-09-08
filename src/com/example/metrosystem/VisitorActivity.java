package com.example.metrosystem;

import com.example.db.DBAdapter;
import com.example.entity.Visitor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;

public class VisitorActivity extends Activity {
	private DBAdapter dbAdapter;
	private Button update;
	private EditText name, phone, password;
	private int vid;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visitor);
		setTitle("个人信息");
		setupView();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		
		Bundle bundle = getIntent().getExtras();
		vid = Integer.valueOf(bundle.getString("vid"));
		Visitor[] v = dbAdapter.queryOneVisitor(vid);
		Log.d("v", String.valueOf(v.length));
		if(v != null){
			name.setText(v[0].getNickname());
			phone.setText(v[0].getPhone());
			password.setText(v[0].getPassword());
		}
		
		update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(!right()){
					return;
				}
				Visitor visitor = new Visitor();
				visitor.setVid(vid);
				visitor.setNickname(name.getText().toString());
				visitor.setPhone(phone.getText().toString());
				visitor.setPassword(password.getText().toString());
				long i = dbAdapter.updateOneVisitor(visitor);
				if(i != 0){
					Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
	}
	
	private boolean right(){
		int n = name.getText().toString().length();
		int ph = phone.getText().toString().length();
		int pw = password.getText().toString().length();
		if(n == 0 || ph == 0 || pw == 0){
			Toast.makeText(getApplicationContext(), "请填写以上信息！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void setupView(){
		update = (Button) findViewById(R.id.UpdateVisitorBtn);
		
		name = (EditText) findViewById(R.id.Nickname);
		phone = (EditText) findViewById(R.id.Phone);
		password = (EditText) findViewById(R.id.Password);
	}
}
