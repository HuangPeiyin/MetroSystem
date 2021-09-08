package com.example.metrosystem;

import com.example.db.DBAdapter;
import com.example.entity.Visitor;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {
	private DBAdapter dbAdapter;
	private Intent intent;
	private Button register, cancel;
	private EditText editNickname, editRphone, editRpw, editRpwc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setTitle("ע���ο���Ϣ");
		setupView();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		
		register.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(!right()){
					return;
				}
				Visitor v = new Visitor();
				v.setNickname(editNickname.getText().toString());
				v.setPhone(editRphone.getText().toString());
				v.setPassword(editRpw.getText().toString());
				Log.d("nickname", v.getNickname());
				Log.d("phone", v.getPhone());
				Log.d("password", v.getPassword());
				long i = dbAdapter.insert(v);
				if(i != 0){
					Toast.makeText(getApplicationContext(), "ע��ɹ���", Toast.LENGTH_SHORT).show();
					intent = new Intent(RegisterActivity.this, MainActivity.class);
					finish();
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "ע��ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				editNickname.setText("");
				editRphone.setText("");
				editRpw.setText("");
				editRpwc.setText("");
			}
		});
	}
	
	private boolean right(){
		int nickname = editNickname.getText().toString().length();
		String phone = editRphone.getText().toString();
		String pwl1 = editRpw.getText().toString();
		String pwl2 = editRpwc.getText().toString();
		int pw1 = pwl1.length();
		int pw2 = pwl2.length();
		if(nickname == 0 || phone.length() == 0 || pw1 == 0 || pw2 == 0){
			Toast.makeText(getApplicationContext(), "ÿ����Ϣ�������ϣ�", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(phone.length() != 11){
			Toast.makeText(getApplicationContext(), "�˺ű���Ϊ11λ���ֻ��ţ�", Toast.LENGTH_SHORT).show();
			return false;
		}
		Visitor[] v = dbAdapter.queryOneVisitor(phone);
		if(v != null){
			Toast.makeText(getApplicationContext(), "���˻��Ѵ��ڣ�", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!pwl1.equals(pwl2)){
			Toast.makeText(getApplicationContext(), "�������벻һ�£�", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}

	private void setupView(){
		register = (Button) findViewById(R.id.Register);
		cancel = (Button) findViewById(R.id.CancelBtn);
		
		editNickname = (EditText) findViewById(R.id.editNickname);
		editRphone = (EditText) findViewById(R.id.editRphone);
		editRpw = (EditText) findViewById(R.id.editRpw);
		editRpwc = (EditText) findViewById(R.id.editRpwc);
	}
}
