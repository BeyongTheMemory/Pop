package com.pop.listview;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import com.pop.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	private static final int VIEW_TYPE = 2;
	private static final int TYPE_1 = 0;
	private static final int TYPE_2 = 1;

	private Context context;
	private LayoutInflater inflater;
	private List<Map<String, String>> dataList;

	public MyAdapter(List<Map<String, String>> list, Context con) {
		this.context = con;
		dataList = list;
		inflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		Map<String, String> map = dataList.get(position);
		if (map.get("r_words") == null) {
			return TYPE_1;
		}
		return TYPE_2;

	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder1 holder1 = null;
		ViewHolder2 holder2 = null;
		int type = getItemViewType(position);

		if (convertView == null) {
			if (type == TYPE_1) {
				convertView = inflater.inflate(R.layout.item, parent, false);
				holder1 = new ViewHolder1();
				holder1.messageText = (TextView) convertView
						.findViewById(R.id.message);
				holder1.date = (TextView) convertView.findViewById(R.id.date);
				convertView.setTag(holder1);
				// holder1.messageText.setText(dataList.get(position).get("message"));
			} else {
				convertView = inflater.inflate(R.layout.list, parent, false);
				holder2 = new ViewHolder2();
				holder2.messageText = (TextView) convertView
						.findViewById(R.id.message);
				holder2.replyText = (TextView) convertView
						.findViewById(R.id.reply);
				holder2.date = (TextView) convertView
						.findViewById(R.id.date);
				convertView.setTag(holder2);
			}
		} else {
			if (type == TYPE_1) {
				holder1 = (ViewHolder1) convertView.getTag();
				// System.out.println(position);
				// Log.e("convertView !!!!!!= ", "NULL TYPE_1");
			} else {
				holder2 = (ViewHolder2) convertView.getTag();
			}
		}

		if (type == TYPE_1) {
			holder1.messageText.setText(dataList.get(position).get("username") + ":  " + dataList.get(position).get("message"));
			holder1.date.setText(dataList.get(position).get("date"));
		} else {
			holder2.messageText.setText(dataList.get(position).get("username") + ":  " + dataList.get(position).get("message"));
			holder2.replyText.setText("»Ø¸´£º" + dataList.get(position).get("r_words"));
			holder2.date.setText(dataList.get(position).get("date"));
		}
		return convertView;
	}

	class ViewHolder1 {
		TextView messageText;
		TextView date;
	}

	class ViewHolder2 {
		TextView messageText;
		TextView replyText;
		TextView date;
	}

}
