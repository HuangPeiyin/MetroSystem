package com.example.adapter;

import java.util.List;

import com.example.db.DBAdapter;
import com.example.entity.Metro;
import com.example.metrosystem.MetroActivity;
import com.example.metrosystem.R;

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

public class MetroAdapter extends BaseAdapter {
	private DBAdapter dbAdapter;
	private Context context;
	private List<Metro> data;
	public MetroAdapter(List<Metro> data) {
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
				Log.d("修改", "修改"+ data.get(i).toString());
				final int mid = data.get(i).getMID();
				LayoutInflater update_metro = LayoutInflater.from(context);
				final View dialog = update_metro.inflate(R.layout.update_metro, null);
				
				new AlertDialog.Builder(context).setIcon(R.drawable.ic_launcher).setTitle("修改信息")
					.setView(dialog).setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							Toast.makeText(context, "取消修改！", Toast.LENGTH_SHORT).show();								
						}
					}).setPositiveButton("确定",  new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							EditText route = (EditText) dialog.findViewById(R.id.Mroute);
							EditText direction = (EditText) dialog.findViewById(R.id.Mdirection);
							EditText start = (EditText) dialog.findViewById(R.id.Mstart);
							EditText destination = (EditText) dialog.findViewById(R.id.Mdestination);
							EditText num = (EditText) dialog.findViewById(R.id.Mnum);
							EditText duration = (EditText) dialog.findViewById(R.id.Mduration);
							EditText price = (EditText) dialog.findViewById(R.id.Mprice);
							
							if(route.getText().toString().length() == 0 || direction.getText().toString().length() == 0 ||
									start.getText().toString().length() == 0 || destination.getText().toString().length() == 0 ||
									num.getText().toString().length() == 0 || duration.getText().toString().length() == 0 ||
									price.getText().toString().length() == 0){
								Toast.makeText(context, "请输入全部信息！", Toast.LENGTH_SHORT).show();
							}else{
								Metro metro = new Metro();
								metro.setMID(mid);
								metro.setMroute(route.getText().toString());
								metro.setMdirection(direction.getText().toString());
								metro.setMstart(start.getText().toString());
								metro.setMdestination(destination.getText().toString());
								metro.setMnumber(Integer.valueOf(num.getText().toString()));
								metro.setMduration(Float.valueOf(duration.getText().toString()));
								metro.setMprice(Float.valueOf(price.getText().toString()));
								long i = dbAdapter.updateOneMetro(MetroActivity.chooseCityID(), metro);
								if (i != 0) {
									Toast.makeText(context, "修改成功！", Toast.LENGTH_SHORT).show();
									MetroActivity.showAllMetro();
								} else {
									Toast.makeText(context, "修改失败！", Toast.LENGTH_SHORT).show();
								}
							}
						}
					}).create().show();
			}
		});
		
		viewHolder.btn2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				Log.d("删除", "删除"+ data.get(i).toString());
				final int mid = data.get(i).getMID();
				new AlertDialog.Builder(context)
				.setTitle("删除框")
				.setMessage("删除该地铁ID将会影响到时间的信息，是否确定删除？")
				.setPositiveButton("是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								long i = dbAdapter.deleteOneMetro(mid);
								if (i != 0) {
									Toast.makeText(context, "删除成功！", Toast.LENGTH_SHORT).show();
									MetroActivity.showAllMetro();
								} else {
									Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
								}
							}
						})
				.setNegativeButton("否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								Toast.makeText(context, "取消删除！", Toast.LENGTH_SHORT).show();
							}
						}).show();
			}
		});
		return view;
	}

}
