package com.example.metrosystem;

import com.example.db.DBAdapter;
import com.example.entity.Visitor;
import com.example.service.MusicService;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity {
	private DBAdapter dbAdapter;
	private Button login, register, repw;
	private Intent intent;
	private EditText phone, password;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupView();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		
		login.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(!right()){
					return;
				}
				String Vphone = phone.getText().toString();
				String Vpassword = password.getText().toString();
					Visitor[] visitor = dbAdapter.queryOneVisitor(Vphone);
					if(visitor == null){
						Toast.makeText(getApplicationContext(), "账户错误或不存在该账户！", Toast.LENGTH_SHORT).show();
					}else{
						String pw = visitor[0].getPassword().toString();
						if(!pw.equals(Vpassword)){
							Toast.makeText(getApplicationContext(), "密码错误！", Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
							Bundle bundle = new Bundle();
							bundle.putString("vid", String.valueOf(visitor[0].getVid()));
							intent = new Intent(MainActivity.this, HomeActivity.class);
							intent.putExtras(bundle);
							finish();
							startService(new Intent(MainActivity.this, MusicService.class));
							startActivity(intent);
						}
					}
			}
		});
		register.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				intent = new Intent(MainActivity.this, RegisterActivity.class);
				startActivity(intent);
			}
		});
		repw.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("phone", phone.getText().toString());
				intent = new Intent(MainActivity.this, UpdateVisitorActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
	
	public boolean right(){
		int p1 = phone.getText().toString().length();
		int p2 = password.getText().toString().length();
		if(p1 == 0 || p2 == 0){
			Toast.makeText(getApplicationContext(), "请输入账号和密码！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(p1 != 11){
			Toast.makeText(getApplicationContext(), "账号必须为11位的手机号！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void setupView() {
		login = (Button) findViewById(R.id.LoginBtn);
		register = (Button) findViewById(R.id.RegisterBtn);
		repw = (Button) findViewById(R.id.Repw);

		phone = (EditText) findViewById(R.id.editPhone);
		password = (EditText) findViewById(R.id.editPassword);
	}
}
