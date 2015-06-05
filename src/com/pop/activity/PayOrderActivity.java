package com.pop.activity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;



import com.example.craise.common.NetRequestConstant;
import com.example.craise.common.NetUrlConstant;
import com.example.craise.domain.Details;
import com.example.craise.domain.Good;
import com.example.craise.interfaces.Netcallback;
import com.pop.R;

public class PayOrderActivity extends BaseActivity implements OnClickListener{

	//返回
	private LinearLayout linearlayout_payorder_back;
	//单价
	private TextView textview_payorder_singlePrice;
	//数量
	private TextView textview_payorder_quantity;
	//总价
	private TextView textview_payorder_totalPrice;
	//还需支付
	private TextView textview_payorder_needPay;
	//确认支付
	private Button button_payorder_makeSure;
	
	//private Good good;
	private Details details;


	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  setContentView(R.layout.payorder_activity);		
		//appContext = (AppContext) getApplicationContext();
		
		//List<Good> goods=appContext.getGoods();
		//good = goods.get(GroupBuyDetailsActivity.position);
		
		//details = OrderActivity.details;
		this.initView();
	}

	private void initView() {
		linearlayout_payorder_back = (LinearLayout) findViewById(R.id.linearlayout_payorder_back);
		linearlayout_payorder_back.setOnClickListener(this);
		
		//textview_payorder_singlePrice = (TextView) findViewById(R.id.textview_payorder_singlePrice);
		//textview_payorder_singlePrice.setText(good.getGoods_price()+"�?);
		
		//textview_payorder_quantity = (TextView) findViewById(R.id.textview_payorder_quantity);
		//textview_payorder_quantity.setText(details.getDetails_quantity()+"");
		
		//textview_payorder_totalPrice = (TextView) findViewById(R.id.textview_payorder_totalPrice);
		//textview_payorder_totalPrice.setText(OrderActivity.i*good.getGoods_price()+"�?);
		
//		textview_payorder_needPay = (TextView) findViewById(R.id.textview_payorder_needPay);
//		textview_payorder_needPay.setText(details.getDetails_prices()+"�?);
		
		button_payorder_makeSure = (Button) findViewById(R.id.button_payorder_makeSure);
		button_payorder_makeSure.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.linearlayout_payorder_back:
			finish();
			break;
		case R.id.button_payorder_makeSure:
		    this.getHttp();
			Intent data = new Intent();
			setResult(22, data);
			finish();
			startActivity(new Intent(this,MainActivity.class));
			break;

		default:
			break;
		}
	}

	public void getHttp(){
//		Map<String,Object> map=new HashMap<String,Object>();
//		
//		map.put("details_time", details.getDetails_time());
//		
//		NetRequestConstant nrc=new NetRequestConstant();
//		// post请求
//		nrc.setType(HttpRequestType.POST);
//		NetRequestConstant.context=this;
//		NetRequestConstant.requestUrl=NetUrlConstant.DETAILSISPAYURL;
//		NetRequestConstant.setMap(map);
//		
//		getServer(new Netcallback() {
//			
//			public void preccess(Object res, boolean flag) {
//				System.out.println("把未付款该为已付�?+res);
//			}
//		}, nrc);
	}

	@Override
	void init() {
		// TODO 自动生成的方法存�?
		
	}
	
	
}
