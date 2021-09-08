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
import android.content.Intent;

public class UpdateVisitorActivity extends Activity {
	private DBAdapter dbAdapter;
	private Intent intent;
	private Button update, cancel;
	private EditText editPhone, editPw1, editPw2;
	private String phone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_visitor);
		setTitle("修改密码");
		setupView();
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		Bundle bundle = getIntent().getExtras();
		phone = bundle.getString("phone");
		if(phone.length() != 0){
			editPhone.setText(phone);
		}
		update.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				if(!right()){
					return;
				}
				Visitor visitor = new Visitor();
				visitor.setPassword(editPw1.getText().toString());
				phone = editPhone.getText().toString();
				Log.d("phone", phone);
				long i = dbAdapter.updateOneVisitor(phone, visitor);
				if(i != 0){
					Toast.makeText(getApplicationContext(), "修改成功！", Toast.LENGTH_SHORT).show();
					intent = new Intent(UpdateVisitorActivity.this, MainActivity.class);
					finish();
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), "修改失败！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				editPw1.setText("");
				editPw2.setText("");
			}
		});
	}
	
	private boolean right(){
		String pw1 = editPw1.getText().toString();
		String pw2 = editPw2.getText().toString();
		if(pw1.length() == 0 || pw2.length() == 0){
			Toast.makeText(getApplicationContext(), "请输入修改的密码！", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(!pw1.equals(pw2)){
			Toast.makeText(getApplicationContext(), "两次修改的密码不一致！", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	
	private void setupView(){
		update = (Button) findViewById(R.id.Update);
		cancel = (Button) findViewById(R.id.Cancel);
		
		editPhone = (EditText) findViewById(R.id.editTextPhone);
		editPw1 = (EditText) findViewById(R.id.editTextPW1);
		editPw2 = (EditText) findViewById(R.id.editTextPW2);
	}
}
