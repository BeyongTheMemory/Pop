package com.pop.activity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;





import com.example.craise.common.NetRequestConstant;
import com.example.craise.common.NetUrlConstant;
import com.example.craise.domain.Good;
import com.example.craise.domain.User;
import com.example.craise.interfaces.Netcallback;
import com.example.craise.myview.CustomDialog;
//import com.example.craise.ui.BaseActivity;
//import com.example.craise.ui.GroupBuyDetailsActivity;
//import com.example.craise.ui.GroupBuyImageDetailsActivity;
//import com.example.craise.ui.MerchantDetailActivity;
//import com.example.craise.ui.OrderActivity;
//import com.example.craise.ui.BaseActivity.HttpRequestType;
import com.example.craise.utils.MyShare;
import com.example.craise.utils.SplitNetImagePath;
import com.pop.R;

public class BusnisePopActivity extends BaseActivity implements
OnClickListener {
private RelativeLayout relativeLayout_groupbuydetails_left;// 返回
private ImageView imageView_details;// 显示的默认图片
private FrameLayout imageView_groupbuydetails_background;// 图片详情
String[] strings;// 网络图片地址数组
private String netImagePath;// 第一张图片地址
CustomDialog dialog;
public  static int position;
private ImageButton imagebutton_groupbuydetails_favorite;//收藏
private Button imagebutton_groupbuydetails_participate;//立即抢购
private int i = 1;
private List<Good> goods;
//private AppContext appContext;
private User user;
private Good good;


@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.groupbuy_details);
Log.v("xx","65");
//appContext = (AppContext) getApplicationContext();
//goods = appContext.getGoods();
Log.v("xx","68");
Intent intent = super.getIntent();
Log.v("xx","70");
Log.v("xx",intent.toString());
Log.v("xx","71");
//intent.
//截取最后一个句号后的内容
if(intent.getAction() == null){
	Log.v("xx","cccccccccccccccccc");
	 int j = intent.toString().lastIndexOf('.');
	    System.out.println(j);
       String fileName = intent.toString().substring(j + 1); 
       intent.setAction(fileName);
}
if (intent.getAction().equals("GroupBuyActivity")) {
	Log.v("xx","72");
	position = intent.getIntExtra("position", 0);
	good=goods.get(position);
	String netPath =good.getGoods_picturePath();
	strings = SplitNetImagePath.splitNetImagePath(netPath);
	netImagePath = strings[0];
	Log.v("xx","76");
} else if (intent.getAction().equals("GroupBuyDetailsViewPAger")){
	Log.v("xx","80");
	netImagePath = intent.getStringExtra(netImagePath);
	Log.v("xx","82");
} else if (intent.getAction().equals("TodayNewOrderActivity")) {
	Log.v("xx","84");
	int goods_id=intent.getIntExtra("goods_id", 0);
	good=getData(goods_id);
	String netPath =good.getGoods_picturePath();
	strings = SplitNetImagePath.splitNetImagePath(netPath);
	netImagePath = strings[0];
	Log.v("xx","86");
} else if (intent.getAction().equals("MovieActivity")) {
	Log.v("xx","92");
	int goods_id=intent.getIntExtra("goods_id", 0);
	good=getData(goods_id);
	String netPath =good.getGoods_picturePath();
	strings = SplitNetImagePath.splitNetImagePath(netPath);
	netImagePath = strings[0];
	Log.v("xx","94");
}else if(intent.getAction().equals("MerchantDetailActivity")){
	Log.v("xx","100");
	int position=intent.getIntExtra("position", 0);
	//good=MerchantDetailActivity.detail_goods.get(position);
	Log.v("xx","103");
}
Log.v("xx","105");
this.initView();
}

public Good getData(int goods_id){
Good good=null;
for(int i=0;i<goods.size();i++){
	if(goods_id==goods.get(i).getGoods_id()){
		good=goods.get(i);
	}
}
return good;
}

public void initView() {
//返回
relativeLayout_groupbuydetails_left = (RelativeLayout) findViewById(R.id.relativeLayout_groupbuydetails_left);
relativeLayout_groupbuydetails_left.setOnClickListener(this);

imageView_details = (ImageView) findViewById(R.id.imageView_details);
// 启动线程，添加图片
Thread thread = new Thread(runnable);
thread.start();

imageView_groupbuydetails_background = (FrameLayout) findViewById(R.id.imageView_groupbuydetails_background);
imageView_groupbuydetails_background.setOnClickListener(this);
//收藏
imagebutton_groupbuydetails_favorite = (ImageButton) findViewById(R.id.imagebutton_groupbuydetails_favorite);
imagebutton_groupbuydetails_favorite.setOnClickListener(this);
// 分享
ImageButton imagebutton_groupbuydetails_share = (ImageButton) findViewById(R.id.imagebutton_groupbuydetails_share);
imagebutton_groupbuydetails_share.setOnClickListener(this);
//立即抢购
imagebutton_groupbuydetails_participate = (Button) findViewById(R.id.imagebutton_groupbuydetails_participate);
imagebutton_groupbuydetails_participate.setOnClickListener(this);
//现在的价格
Log.v("xx","152");
//TextView textView_curPrice=(TextView) findViewById(R.id.textView_curPrice);
//textView_curPrice.setText(good.getGoods_price()+"元");
//原来的价格
Log.v("xx","156");
//TextView textView_oldPrice=(TextView) findViewById(R.id.textView_oldPrice);
//textView_oldPrice.setText(good.getGoods_oldPrice()+"元");
Log.v("xx","159");
//加下划线
//textView_oldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//商品的名字
//TextView textView_groupbuy_name=(TextView) findViewById(R.id.textView_groupbuy_name);
//textView_groupbuy_name.setText(good.getGoods_name());
//商品的信息
//TextView textView_groupbuy_content=(TextView) findViewById(R.id.textView_groupbuy_content);
//textView_groupbuy_content.setText(good.getGoods_saleInfo());

//user = appContext.getUser();
}

Runnable runnable = new Runnable() {
public void run() {
	// 把网络地址转换为BitMap
	URL picUrl;
	try {
		picUrl = new URL(netImagePath);
		Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());
		Message msg = new Message();
		msg.obj = pngBM;
		handler.sendMessage(msg);
	} catch (MalformedURLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
};


@SuppressLint("HandlerLeak")
Handler handler = new Handler() {
public void handleMessage(Message msg) {
	// TODO Auto-generated method stub
	super.handleMessage(msg);
	Bitmap pngBM = (Bitmap) msg.obj;
	imageView_details.setImageBitmap(pngBM);
}
};

public void onClick(View v) {
// TODO Auto-generated method stub
switch (v.getId()) {
case R.id.relativeLayout_groupbuydetails_left:// 返回
	finish();
	break;
//case R.id.imageView_groupbuydetails_background:// 图片详情
//	Intent intent = new Intent(this, GroupBuyImageDetailsActivity.class);
//	intent.putExtra("netPathStrings", strings);
//	intent.putExtra("goods_name", good.getGoods_name());
//	intent.putExtra("goods_saleInfo", good.getGoods_saleInfo());
//	startActivity(intent);
//	break;
case R.id.imagebutton_groupbuydetails_favorite:// 我喜欢
	
		int id = goods.get(position).getGoods_id();

		String username = user.getUsername();

		NetRequestConstant nrc = new NetRequestConstant();
		// post请求
		nrc.setType(HttpRequestType.POST);
		NetRequestConstant.requestUrl = NetUrlConstant.CHANGECOLLECTURL;
		NetRequestConstant.context = this;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("action", "" + i);
		map.put("id", "" + id);
		map.put("username", username);
		NetRequestConstant.setMap(map);

		getServer(new Netcallback() {

			public void preccess(Object res, boolean flag) {
				if (res != null) {
					try {
						JSONObject object = new JSONObject((String) res);
						String success = object.optString("success");
						if (success.equals("1")) {
							if (i == -1) {
								imagebutton_groupbuydetails_favorite
										.setImageResource(R.drawable.ic_action_favorite_on);
								Toast.makeText(
										BusnisePopActivity.this,
										"已收藏成功", Toast.LENGTH_SHORT)
										.show();
							} else {
								imagebutton_groupbuydetails_favorite
										.setImageResource(R.drawable.ic_action_favorite_off);
								Toast.makeText(
										BusnisePopActivity.this,
										"已取消收藏", Toast.LENGTH_SHORT)
										.show();
							}
						} else {
							Toast.makeText(
									BusnisePopActivity.this,
									"网络异常,请稍后再试",
									Toast.LENGTH_SHORT).show();
							imagebutton_groupbuydetails_favorite
							.setImageResource(R.drawable.ic_action_favorite_on);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}, nrc);

		i = -i;

	

	break;
case R.id.imagebutton_groupbuydetails_share:// 分享

	MyShare myShare = new MyShare(this);
	myShare.share("输入要分享的内容");

	break;
case R.id.imagebutton_groupbuydetails_participate:// 立即抢购

	//if (user != null) {
//		startActivityForResult(new Intent(BusnisePopActivity.this,
//				OrderActivity.class);
		Intent intent = new Intent();
		intent.setClass(BusnisePopActivity.this,OrderActivity.class);
		//MainActivity.this.startActivity(intent);
		BusnisePopActivity.this.startActivity(intent);
		//MainActivity.this.onPause();
		BusnisePopActivity.this.finish();
	//} else {
		//Toast.makeText(this, "亲,请先登录", Toast.LENGTH_SHORT).show();
	//}
	break;
	
	
	

default:
	break;
}
}

@Override
void init() {
// TODO Auto-generated method stub

}

protected void onActivityResult(int requestCode, int resultCode, Intent data) {
if(data!=null){
	finish();
}
}
}

