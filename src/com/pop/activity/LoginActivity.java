/**
 * 登陆界面
 * @author xg
 *6.1
 */
package com.pop.activity;



import com.pop.R;
import com.pop.servlet.LoginAsyncServlet;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class LoginActivity extends Activity{
	private  EditText usernameText = null;
	private  EditText passwordText= null;
	private  Button loginButton= null;
	private TextView registText= null;
	private TextView forgetPwdText= null;
	private TextView informationText= null;
	private String url = "http://1.utifpop.sinaapp.com/getAccountByUsernameAndPwd.php";
	
	
	public void onCreate(Bundle savedInstanceState) {  
		Window window = this.getWindow();
        // 去标题栏
       window.requestFeature(window.FEATURE_NO_TITLE);
	      super.onCreate(savedInstanceState);  
	      setContentView(R.layout.login_activity);
	      usernameText= (EditText)findViewById(R.id.username_text);
	      passwordText= (EditText)findViewById(R.id.password_text);
	    
	    
	    
	      
	      //登陆按钮
	      loginButton = (Button)findViewById(R.id.login_button);
	      loginButton.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
//				LoginAsyncServlet loginServlet=new  LoginAsyncServlet(usernameText.getText().toString(),passwordText.getText().toString(),informationText,LoginActivity.this);
//				loginServlet.execute(url);
				//直接进入
				 Intent intent = new Intent();
			     intent.setClass(LoginActivity.this,MainActivity.class);
			     LoginActivity.this.startActivity(intent);
			     LoginActivity.this.finish();
			}
	    	  
	      });
	      
	      //新用户注册
	      registText = (TextView)findViewById(R.id.regist_text);
	      registText.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				 Intent intent = new Intent();
			     intent.setClass(LoginActivity.this,RegistActivity.class);
			    LoginActivity.this.startActivity(intent);
					//LoginActivity.this.finish();
			}
	    	  
	      });
	      
	      //忘记密码
	      forgetPwdText = (TextView)findViewById(R.id.forgetpassword_text);
	      forgetPwdText.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
		
				
			}
	    	  
	      });
	      
	      //提示信息
	      informationText = (TextView)findViewById(R.id.information_text);
	      
		   }


	public TextView getInformationText() {
		return informationText;
	}


	
	
   
}
