/**
 * 注册界面
 * @author xg
 *6.1
 */
package com.pop.activity;



import com.pop.R;
import com.pop.servlet.RegistAsyncServlet;
import com.pop.activity.widget.MyEditText;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class RegistActivity extends Activity{
	private  MyEditText usernameText= null;
	private  MyEditText passwordText= null;
	private  MyEditText surepasswordText= null;
	private  TextView backText= null;
	private TextView informationText = null;
	private Button registButton = null;
	String url = "http://1.utifpop.sinaapp.com/newAccount.php";
	  public void onCreate(Bundle savedInstanceState) {  
		  Window window = this.getWindow();
	        // 去标题栏
	       window.requestFeature(window.FEATURE_NO_TITLE);
	   super.onCreate(savedInstanceState);
	   super.requestWindowFeature(Window.FEATURE_NO_TITLE);
      setContentView(R.layout.regist_activity); 
      
      //用户名输入框
      usernameText = (MyEditText)findViewById(R.id.username_text);
      usernameText.setShowWords("输入用户名");
      usernameText.invalidate();
      //失去焦点时重绘
      usernameText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    		public void onFocusChange(View v, boolean hasFocus) {
    			// TODO 自动生成的方法存根
    			if(hasFocus){
    					usernameText.setJudge(false);
    					usernameText.invalidate();
    			}
    			else{
    				if(usernameText.getText().toString().equals("")){
    				usernameText.setJudge(true);
    				usernameText.invalidate();
    				}
    			}
    		}
    	});
      //密码输入框
      passwordText = (MyEditText)findViewById(R.id.password_text);
      passwordText.setShowWords("输入密码：");
      passwordText.invalidate();
      passwordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    		public void onFocusChange(View v, boolean hasFocus) {
    			// TODO 自动生成的方法存根
    			if(hasFocus){
    				 passwordText.setJudge(false);
    				 passwordText.invalidate();
    			}
    			else{
    				if(passwordText.getText().toString().equals("")){
    				 passwordText.setJudge(true);
    				 passwordText.invalidate();
    				}
    			}
    		}
    	});
      //确认输入框
      surepasswordText = (MyEditText)findViewById(R.id.sure_password_text);
      surepasswordText.setShowWords("再次输入密码：");
      surepasswordText.invalidate();
      surepasswordText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    		public void onFocusChange(View v, boolean hasFocus) {
    			// TODO 自动生成的方法存根
    			if(hasFocus){
    				 surepasswordText.setJudge(false);
    				 surepasswordText.invalidate();
    			}
    			else{
    				if(surepasswordText.getText().toString().equals("")){
    					surepasswordText.setJudge(true);
    					surepasswordText.invalidate();
    				}
    			}
    		}
    	});
      
      //返回
      backText = (TextView)findViewById(R.id.back_text);
      backText.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO 自动生成的方法存根
			RegistActivity.this.finish();
		}
    	  
      });
      
      
      
      //提示信息
      informationText = (TextView)findViewById(R.id.information_textView);
      
      //注册按钮
      registButton = (Button)findViewById(R.id.regist_button);
      registButton.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			if(passwordText.getText().toString().equals(surepasswordText.getText().toString())){
			RegistAsyncServlet registAsyncServlet = new RegistAsyncServlet(usernameText.getText().toString(),passwordText.getText().toString(),informationText);
			//Log.v("information","sc");
			registAsyncServlet.execute(url);
			//Log.v("information","sc2");
			}
			else{
				informationText.setText("两次输入的密码不一致！");
			}
			
		}
    	  
      });
       
	  }

}
