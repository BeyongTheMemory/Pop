package com.anjoyo.meituan.myview;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.anjoyo.meituan.R;
import com.anjoyo.meituan.Parser.AllCategoriesParser;
import com.anjoyo.meituan.Parser.SpecificCategoryParser;
import com.anjoyo.meituan.adapter.MerchantAllAdapter;
import com.anjoyo.meituan.adapter.MerchantSaleAdapter;
import com.anjoyo.meituan.adapter.TextAdapter;
import com.anjoyo.meituan.app.AppContext;
import com.anjoyo.meituan.common.NetRequestConstant;
import com.anjoyo.meituan.common.NetUrlConstant;
import com.anjoyo.meituan.domain.AllCategories;
import com.anjoyo.meituan.domain.Good;
import com.anjoyo.meituan.domain.Seller;
import com.anjoyo.meituan.domain.SpecificCategories;
import com.anjoyo.meituan.interfaces.Netcallback;
import com.anjoyo.meituan.ui.MerchantActivity;
import com.anjoyo.meituan.ui.BaseActivity.HttpRequestType;
import com.anjoyo.meituan.utils.Logger;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ViewMiddle extends LinearLayout implements ViewBaseAction {

	private ListView regionListView;
	private ListView plateListView;
	private ArrayList<String> groups = new ArrayList<String>();
	private LinkedList<String> childrenItem = new LinkedList<String>();
	private SparseArray<LinkedList<String>> children = new SparseArray<LinkedList<String>>();
	private TextAdapter plateListViewAdapter;
	private TextAdapter earaListViewAdapter;
	private OnSelectListener mOnSelectListener;
	private int tEaraPosition = 0;
	private int tBlockPosition = 0;
	private String showString = "全部商家";
	private int showPhoto=0;
	private ArrayList<AllCategories>   allCategories;
	private ArrayList<AllCategories>   allCategoriesList=new ArrayList<AllCategories>();
	private List<SpecificCategories>  specificCategories;
	private ArrayList<SpecificCategories>  specificCategoriesList=new ArrayList<SpecificCategories>();
	private ArrayList<Good>   goods;
	private ArrayList<Seller>  sellers;

	public ViewMiddle(Context context) {
		super(context);
		init(context);
	}

	public ViewMiddle(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void updateShowText(String showArea, String showBlock) {
		if (showArea == null || showBlock == null) {
			return;
		}
		for (int i = 0; i < groups.size(); i++) {
			if (groups.get(i).equals(showArea)) {
				earaListViewAdapter.setSelectedPosition(i);
				childrenItem.clear();
				if (i < children.size()) {
					childrenItem.addAll(children.get(i));
				}
				tEaraPosition = i;
				break;
			}
		}
		for (int j = 0; j < childrenItem.size(); j++) {
			if (childrenItem.get(j).replace("不限", "").equals(showBlock.trim())) {
				plateListViewAdapter.setSelectedPosition(j);
				tBlockPosition = j;
				break;
			}
		}
		setDefaultSelect();
	}

	private void init(final Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.view_region, this, true);
		regionListView = (ListView) findViewById(R.id.listView);
		plateListView = (ListView) findViewById(R.id.listView2);
		setBackgroundDrawable(getResources().getDrawable(R.drawable.choosearea_bg_left));
		groups.add("全部分类");
		groups.add("电影");
		groups.add("美食");
		groups.add("酒店");
		groups.add("休闲娱乐");
		groups.add("生活服务");
		groups.add("丽人");
		
		String[]  foods={"全部","火锅","自助餐 /西餐 ","日韩料理","蛋糕甜点","烧烤烤鱼","川湘菜","江浙菜","粤菜","西北/东北菜","清真菜","东南亚菜","台湾菜","海鲜 ","汤/粥/炖汤","特色菜","小吃快餐","咖啡酒吧","其他美食"};
		String[]  wineShop={"全部","经济酒店","豪华酒店"};
		String[]  entertainment={"电影","ktv","温泉/洗浴","足疗按摩 ","运动健身","桌游/电玩","密室逃脱","咖啡酒吧","演出赛事","DIY手工","真人cs","4D/5D电影","其他娱乐"};
		String[]   life={"母婴亲子","摄影写真","体检保健 ","汽车服务","照片冲印","配需课程","鲜花婚庆","服饰服务","配镜","商场购物卡","其他生活"};
		String[]   beauty={"美发","美甲","美容SPA","瑜伽/舞蹈"};
		LinkedList<String> allItem = new LinkedList<String>();
		children.put(0,allItem);
		LinkedList<String> movieItem = new LinkedList<String>();
		children.put(1,movieItem);
		LinkedList<String> foodsItem = new LinkedList<String>();
		for(int i=0;i<foods.length;i++){
			foodsItem.add(foods[i]);
		}
		children.put(2,foodsItem);
		
		LinkedList<String> wineShopItem = new LinkedList<String>();
		for(int i=0;i<wineShop.length;i++){
			wineShopItem.add(wineShop[i]);
		}
		children.put(3,wineShopItem);
		
		LinkedList<String> entertainmentItem = new LinkedList<String>();
		for(int i=0;i<entertainment.length;i++){
			entertainmentItem.add(entertainment[i]);
		}
		children.put(4,entertainmentItem);
		
		LinkedList<String> lifeItem = new LinkedList<String>();
		for(int i=0;i<life.length;i++){
			lifeItem.add(life[i]);
		}
		children.put(5,lifeItem);
		
		LinkedList<String> beautyItem = new LinkedList<String>();
		for(int i=0;i<beauty.length;i++){
			beautyItem.add(beauty[i]);
		}
		children.put(6,beautyItem);
		
		
		//筛选用到的数据源    AllCategories   specificCategories  goods    seller
/*		NetRequestConstant nrc = new NetRequestConstant();
		NetRequestConstant.requestUrl = NetUrlConstant.ALLCATEGORIESURL;
		NetRequestConstant.context = context;
		nrc.setType(HttpRequestType.GET);
		getServer(new Netcallback() {

			public void preccess(Object res, boolean flag) {

				if (res != null) {
						AllCategoriesParser  allCategoriesParser=new AllCategoriesParser();
						allCategories=allCategoriesParser.getAllCategories(res);
						Logger.i("allCategories"+allCategories.size());
						if (allCategories != null && !allCategories.isEmpty()) {
							allCategoriesList.addAll(allCategories);
						}
				}
			}
		}, nrc);
		
		NetRequestConstant.requestUrl = NetUrlConstant.SPECIFICCATEGORIESURL;
		NetRequestConstant.context = context;
		nrc.setType(HttpRequestType.GET);
		getServer(new Netcallback() {

			public void preccess(Object res, boolean flag) {

				if (res != null) {
						SpecificCategoryParser specificCategoryParser=new SpecificCategoryParser();
						specificCategories=specificCategoryParser.getSpecificCategory(res.toString());
						if (specificCategories!= null && !specificCategories.isEmpty()) {
							specificCategoriesList.addAll(specificCategories);
						}
				}
			}
		}, nrc);
		
		goods=new  ArrayList<Good>();
		goods.addAll((ArrayList<Good>)((AppContext)context.getApplicationContext()).getGoods());
		sellers=new  ArrayList<Seller>();
		sellers.addAll(MerchantActivity.allList);
	*/	

		

		final int[] photo={R.drawable.ic_category_all,R.drawable.ic_category_movie,R.drawable.ic_category_food,R.drawable.ic_category_hotel,
				R.drawable.ic_category_entertainment,R.drawable.ic_category_live,R.drawable.ic_category_health};
		
		earaListViewAdapter = new TextAdapter(context, groups, R.drawable.choose_eara_item_selector,photo);
		earaListViewAdapter.setTextSize(12);
		earaListViewAdapter.setSelectedPositionNoNotify(tEaraPosition);
		regionListView.setAdapter(earaListViewAdapter);
		earaListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			public void onItemClick(View view, int position) {
				if (position < children.size()) {
					showPhoto=photo[position];
					showString=groups.get(position);
					childrenItem.clear();
					childrenItem.addAll(children.get(position));
		 			plateListViewAdapter.notifyDataSetChanged();
		 			mOnSelectListener.getValue(showString);
					mOnSelectListener.getPhoto(showPhoto);
				}
				
			/*	 for(int i=0;i<allCategoriesList.size();i++){
				    	if(showString.equals(allCategoriesList.get(i).getAllCategories_name())){
				    		for(int j=0;j<goods.size();j++){
				    			if(allCategoriesList.get(i).getAllCategories_id()==goods.get(j).getAllCategories_id()){
				    				for(int k=0;k<sellers.size();k++){
				    				  if(sellers.get(k).getSeller_id()==goods.get(j).getSeller_id()){
				    					  Seller seller=new Seller();
				    					  seller=sellers.get(k);
				    					  sellers.clear();
				    					  sellers.add(seller);
				    				  }
				    				}
				    			}
				    		}
				    	}
				        MerchantAllAdapter  merchantAllAdapter=new MerchantAllAdapter(context,sellers);
				        MerchantActivity.aListView.setAdapter(merchantAllAdapter);
				   
				        MerchantSaleAdapter merchantSaleAdapter=new MerchantSaleAdapter(context,sellers );
				        MerchantActivity.sListView.setAdapter(merchantSaleAdapter);
				    }
				 */
			}
		});

		if (tEaraPosition < children.size())
			childrenItem.addAll(children.get(tEaraPosition));
		plateListViewAdapter = new TextAdapter(context, childrenItem, R.drawable.choose_plate_item_selector,null);
		plateListViewAdapter.setTextSize(12);
		plateListViewAdapter.setSelectedPositionNoNotify(tBlockPosition);
		plateListView.setAdapter(plateListViewAdapter);
		plateListViewAdapter.setOnItemClickListener(new TextAdapter.OnItemClickListener() {

			public void onItemClick(View view, final int position) {

				showString = childrenItem.get(position);
				showPhoto=photo[tEaraPosition];
				if (mOnSelectListener != null) {
					mOnSelectListener.getValue(showString);
					mOnSelectListener.getPhoto(showPhoto);
				}
				
				/* for(int i=0;i<specificCategoriesList.size();i++){
				    	if(showString.equals(specificCategoriesList.get(i).getSpecificCategories_name())){
				    		for(int j=0;j<goods.size();j++){
				    			if(specificCategoriesList.get(i).getSpecificCategories_id()==goods.get(j).getSpecificCategories_id()){
				    				for(int k=0;k<sellers.size();k++){
				    				  if(sellers.get(k).getSeller_id()==goods.get(j).getSeller_id()){
				    					  Seller seller=new Seller();
				    					  seller=sellers.get(k);
				    					  sellers.clear();
				    					  sellers.add(seller);
				    				  }
				    				}
				    			}
				    		}
				    	}
				    	 MerchantAllAdapter  merchantAllAdapter=new MerchantAllAdapter(context,sellers);
					     MerchantActivity.aListView.setAdapter(merchantAllAdapter);
					   
					     MerchantSaleAdapter merchantSaleAdapter=new MerchantSaleAdapter(context,sellers );
					     MerchantActivity.sListView.setAdapter(merchantSaleAdapter);
				    }*/
			
			}
		});
		if (tBlockPosition < childrenItem.size())
			showString = childrenItem.get(tBlockPosition);
		if (showString.contains("不限")) {
			showString = showString.replace("不限", "");
		}
		setDefaultSelect();

	}

	private void getServer(Netcallback netcallback, NetRequestConstant nrc) {
		// TODO Auto-generated method stub
		
	}

	public void setDefaultSelect() {
		regionListView.setSelection(tEaraPosition);
		plateListView.setSelection(tBlockPosition);
	}

	public String getShowText() {
		return showString;
	}
	
	
	public int getShowPhoto() {
		return showPhoto;
	}

	public void setOnSelectListener(OnSelectListener onSelectListener) {
		mOnSelectListener = onSelectListener;
	}

	public interface OnSelectListener {
		public void getValue(String showText);
		public void getPhoto(int showPhoto);
	}

	public void hide() {
		// TODO Auto-generated method stub

	}

	public void show() {
		// TODO Auto-generated method stub

	}
}
