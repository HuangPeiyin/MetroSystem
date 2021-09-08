package com.example.adapter;

import java.util.List;

import com.example.db.DBAdapter;
import com.example.entity.Schedule;
import com.example.metrosystem.R;
import com.example.metrosystem.ScheduleActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ScheduleAdapter extends BaseAdapter {
	private DBAdapter dbAdapter;
	private Context context;
	private List<Schedule> data;
	
	public ScheduleAdapter(List<Schedule> data) {
		this.data = data;
	}

	@Override
	public int getCount() {
		return data == null ? 0 : data.size();
	}

	@Override
	public Object getItem(int i) {
		return data.get(i);
	}

	@Override
	public long getItemId(int i) {
		return i;
	}

	@Override
	public View getView(final int i, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder = null;
		if(context == null){
			context = viewGroup.getContext();
		}
		if(view == null){
			view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
			viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.data);
			viewHolder.btn1 = (Button) view.findViewById(R.id.button1);
			viewHolder.btn2 = (Button) view.findViewById(R.id.button2);
			view.setTag(viewHolder);
		}
		dbAdapter = new DBAdapter(context);
		dbAdapter.open();  		
		viewHolder = (ViewHolder) view.getTag();
		viewHolder.text.setText(data.get(i).toString());
		
		viewHolder.btn1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.d("�޸�", "�޸�"+ data.get(i).toString());
				final int sid = data.get(i).getSid();
				LayoutInflater update_schedule = LayoutInflater.from(context);
				final View dialog = update_schedule.inflate(R.layout.update_schedule, null);
				
				new AlertDialog.Builder(context).setIcon(R.drawable.ic_launcher).setTitle("�޸���Ϣ")
					.setView(dialog).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Toast.makeText(context, "ȡ���޸ģ�", Toast.LENGTH_SHORT).show();								
						}
					}).setPositiveButton("ȷ��",  new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							EditText start = (EditText) dialog.findViewById(R.id.Sstart);
							EditText end = (EditText) dialog.findViewById(R.id.Send);
							
							if(start.getText().toString().length() == 0 || end.getText().toString().length() == 0){
								Toast.makeText(context, "������ȫ����Ϣ��", Toast.LENGTH_SHORT).show();
							}else{
								Schedule s = new Schedule();
								s.setSid(sid);
								s.setSstart(start.getText().toString());
								s.setSend(end.getText().toString());
								Schedule[] sche = dbAdapter.queryOneSchedule(Integer.valueOf(s.getSid()));
								long i = dbAdapter.updateOneSchedule(sche[0].getMid(), s);
								if(i != 0){
									Toast.makeText(context, "�޸ĳɹ���", Toast.LENGTH_SHORT).show();
									ScheduleActivity.showAllSchedule();
								}else{
									Toast.makeText(context, "�޸�ʧ�ܣ�", Toast.LENGTH_SHORT).show();
								}
							}
						}
					}).create().show();
			}
		});
		
		viewHolder.btn2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.d("ɾ��", "ɾ��"+ data.get(i).toString());
				final int sid = data.get(i).getSid();
				long i = dbAdapter.deleteOneSchedule(sid);
				if (i != 0) {
					Toast.makeText(context, "ɾ���ɹ���", Toast.LENGTH_SHORT).show();
					ScheduleActivity.showAllSchedule();
				} else {
					Toast.makeText(context, "ɾ��ʧ�ܣ�", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return view;
	}

}
