package com.pop.activity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.craise.common.NetRequestConstant;
import com.example.craise.common.NetUrlConstant;
import com.example.craise.domain.Details;
import com.example.craise.domain.Good;
import com.example.craise.interfaces.Netcallback;
import com.pop.R;

public class OrderActivity extends BaseActivity implements OnClickListener {

	//private AppContext appContext;
	//private List<Good> goods;
	//private Good good;
		private TextView textview_order_number;
	public static int i=1;
	private TextView textview_order_totalPrice;
	public static Details details;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	void init() {
		setContentView(R.layout.activity_order);
		
		//appContext = (AppContext) getApplicationContext();
//		goods = appContext.getGoods();
//		good = goods.get(GroupBuyDetailsActivity.position);
//		
		// 返回
		LinearLayout linearLayout_login_back = (LinearLayout) findViewById(R.id.linearLayout_login_back);
		linearLayout_login_back.setOnClickListener(this);
		// 商品单价
		TextView textview_order_singlePrice = (TextView) findViewById(R.id.textview_order_singlePrice);
		//textview_order_singlePrice.setText(good.getGoods_price()+"�?);
		// 订单商品名字
		TextView textview_order_name = (TextView) findViewById(R.id.textview_order_name);
		//textview_order_name.setText(good.getGoods_name());
		//商品数量
		textview_order_number = (TextView) findViewById(R.id.textview_order_number);
		textview_order_number.setText(i+"");
		textview_order_totalPrice = (TextView) findViewById(R.id.textview_order_totalPrice);
		//textview_order_totalPrice.setText(good.getGoods_price()+"�?);
		textview_order_totalPrice.setOnClickListener(this);
		// 商品数量添加
		ImageView imageview_order_in = (ImageView) findViewById(R.id.imageview_order_in);
		imageview_order_in.setOnClickListener(this);
		// 商品数量减少
		ImageView imageview_order_out = (ImageView) findViewById(R.id.imageview_order_out);
		imageview_order_out.setOnClickListener(this);
		// 提交订单
		Button button_order_submit = (Button) findViewById(R.id.button_order_submit);
		button_order_submit.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearLayout_login_back:
            finish();
			break;
		case R.id.imageview_order_in:
			if(i>=0){
				i++;
				textview_order_number.setText(i+"");
				//double order_totalPrice1=i*(good.getGoods_price());
				//textview_order_totalPrice.setText(order_totalPrice1+"�?);
			}
			break;
		case R.id.imageview_order_out:
			if(i>=1){
				i--;
				textview_order_number.setText(i+"");
				//double order_totalPrice2=i*(good.getGoods_price());
				//textview_order_totalPrice.setText(order_totalPrice2+"�?);
			}
			break;
		case R.id.button_order_submit:
			
//			Map<String,Object> map=new HashMap<String,Object>();
//			details = new Details();
//			//details.setGood_id(good.getGoods_id());
//			
//			details.setDetails_quantity(i);
//			//details.setDetails_prices(i*(good.getGoods_price()));
//			//未付�?//			details.setDetails_isPay(0);
//			//系统时间
//			Date date = new Date ();
//			SimpleDateFormat f = new SimpleDateFormat("yyyy-MM--dd hh:mm");
//			String time = f.format(date);
//			details.setDetails_time(time);
//			
//			map.put("good_id", details.getGood_id()+"");
//			map.put("details_prices", details.getDetails_prices()+"");
//			map.put("details_quantity", details.getDetails_quantity()+"");
//			map.put("details_time", details.getDetails_time());
//			map.put("details_isPay", details.getDetails_isPay()+"");
//			
//			String user_name=appContext.getUser().getUsername();
//			map.put("user_name", user_name+"");
//			
//			NetRequestConstant nrc=new NetRequestConstant();
//			// post请求
//			nrc.setType(HttpRequestType.POST);
//			NetRequestConstant.context=this;
//			NetRequestConstant.requestUrl=NetUrlConstant.DETAILSURL;
//			NetRequestConstant.setMap(map);
//			
//			getServer(new Netcallback(){
//
//				public void preccess(Object res, boolean flag) {
//					// TODO Auto-generated method stub
//					System.out.println("Details请求网络返回的�?"+res);
//				}
//				
//			}, nrc);
			
		    startActivityForResult(new Intent(this,PayOrderActivity.class),22);
		    
			break;

		default:
			break;
		}
	}
  
}
