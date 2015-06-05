/**
 * ��½����
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
        // ȥ������
       window.requestFeature(window.FEATURE_NO_TITLE);
	      super.onCreate(savedInstanceState);  
	      setContentView(R.layout.login_activity);
	      usernameText= (EditText)findViewById(R.id.username_text);
	      passwordText= (EditText)findViewById(R.id.password_text);
	    
	    
	    
	      
	      //��½��ť
	      loginButton = (Button)findViewById(R.id.login_button);
	      loginButton.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
//				LoginAsyncServlet loginServlet=new  LoginAsyncServlet(usernameText.getText().toString(),passwordText.getText().toString(),informationText,LoginActivity.this);
//				loginServlet.execute(url);
				//ֱ�ӽ���
				 Intent intent = new Intent();
			     intent.setClass(LoginActivity.this,MainActivity.class);
			     LoginActivity.this.startActivity(intent);
			     LoginActivity.this.finish();
			}
	    	  
	      });
	      
	      //���û�ע��
	      registText = (TextView)findViewById(R.id.regist_text);
	      registText.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
				 Intent intent = new Intent();
			     intent.setClass(LoginActivity.this,RegistActivity.class);
			    LoginActivity.this.startActivity(intent);
					//LoginActivity.this.finish();
			}
	    	  
	      });
	      
	      //��������
	      forgetPwdText = (TextView)findViewById(R.id.forgetpassword_text);
	      forgetPwdText.setOnClickListener(new OnClickListener(){

			
			public void onClick(View v) {
		
				
			}
	    	  
	      });
	      
	      //��ʾ��Ϣ
	      informationText = (TextView)findViewById(R.id.information_text);
	      
		   }


	public TextView getInformationText() {
		return informationText;
	}


	
	
   
}
