package com.example.service;

import com.example.metrosystem.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
	private MediaPlayer myplayer;
	private NotificationManager msg;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		myplayer = MediaPlayer.create(this, R.raw.lisao);
		myplayer.setLooping(true);
		msg = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,"Music启动",System.currentTimeMillis());
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,MusicService.class), 0);
		notification.setLatestEventInfo(this, "离骚--易烊千玺", "歌曲正在播放中", contentIntent);
		msg.notify(R.string.hello_world,notification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		myplayer.stop();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(!myplayer.isPlaying()){
			myplayer.start();
		}else if(myplayer.isPlaying()){
			myplayer.pause();
		}
		return super.onStartCommand(intent, flags, startId);
	}

}
