package com.example.metrosystem;

import com.example.service.MusicService;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

public class HomeActivity extends Activity implements OnClickListener {
	private Intent intent;
	private Button musicbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		setTitle("地铁信息系统");
		Button citybutton = (Button) findViewById(R.id.MenegementCityBtn);
		Button metrobutton = (Button) findViewById(R.id.MenegementMetroBtn);
		Button schedulebutton = (Button) findViewById(R.id.MenegementScheduleBtn);
		Button visitorbutton = (Button) findViewById(R.id.MenegementVisitorBtn);
		Button exitbutton = (Button) findViewById(R.id.exitBtn);
		musicbutton = (Button) findViewById(R.id.musicBtn);
		citybutton.setOnClickListener(this);
		metrobutton.setOnClickListener(this);
		schedulebutton.setOnClickListener(this);
		visitorbutton.setOnClickListener(this);
		exitbutton.setOnClickListener(this);
		this.registerForContextMenu(musicbutton);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.MenegementCityBtn:
			intent = new Intent(HomeActivity.this, CityActivity.class);
			startActivity(intent);
			break;
		case R.id.MenegementMetroBtn:
			intent = new Intent(HomeActivity.this, MetroActivity.class);
			startActivity(intent);
			break;
		case R.id.MenegementScheduleBtn:
			intent = new Intent(HomeActivity.this, ScheduleActivity.class);
			startActivity(intent);
			break;
		case R.id.MenegementVisitorBtn:
			intent = new Intent(HomeActivity.this, VisitorActivity.class);
			Bundle bundle1 = getIntent().getExtras();
			String vid = bundle1.getString("vid");
			Bundle bundle2 = new Bundle();
			bundle2.putString("vid", vid);
			intent.putExtras(bundle2);
			startActivity(intent);
			break;
		case R.id.exitBtn:
			intent = new Intent(HomeActivity.this, MainActivity.class);
			finish();
			stopService(new Intent(HomeActivity.this, MusicService.class));
			startActivity(intent);
			break;
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if(item.getTitle().toString().equals("开始/暂停")){
			Toast.makeText(getApplicationContext(), "音乐开始/暂停", Toast.LENGTH_SHORT).show();
			startService(new Intent(HomeActivity.this, MusicService.class));
		}else if(item.getTitle().toString().equals("结束")){
			Toast.makeText(getApplicationContext(), "音乐停止", Toast.LENGTH_SHORT).show();
			stopService(new Intent(HomeActivity.this, MusicService.class));
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		if(view == musicbutton){
			menu.add("开始/暂停");
			menu.add("结束");
		}
		super.onCreateContextMenu(menu, view, menuInfo);
	}

}
