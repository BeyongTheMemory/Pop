package com.pop.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public class Communication {
	
	 /** 
     * @param 只发送普通数据,调用此方法 
     * @param urlString 对应的url
     * @param params 需要发送的相关数据 包括调用的方法 
     * @param imageuri 图片或文件手机上的地址 如:sdcard/photo/123.jpg 
     * @param img 图片名称 
     * @return Json 
     */  
    public String communication02(String urlString,Map<String, Object> params,String  imageuri ,String img){  
        String result="";  
          
        String end = "\r\n";          
       // String uploadUrl=new BingoApp().URLIN+"/";//new BingoApp().URLIN 是我定义的上传URL  
        String MULTIPART_FORM_DATA = "multipart/form-data";   
        String BOUNDARY = "---------7d4a6d158c9"; //数据分隔线  
        String imguri ="";  
        if (!imageuri.equals("")) {  
            imguri = imageuri.substring(imageuri.lastIndexOf("/") + 1);//获得图片或文件名称  
            //System.out.println(imguri);
        }  
          
          
          
          
        if (!urlString.equals("")) {  
            //uploadUrl = uploadUrl+urlString;  
        	//return "faile";
          
        	try {  
        		URL url = new URL(urlString);    
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();    
                conn.setDoInput(true);//允许输入    
                conn.setDoOutput(true);//允许输出    
                conn.setUseCaches(false);//不使用Cache    
                conn.setConnectTimeout(6000);// 6秒钟连接超时  
                conn.setReadTimeout(6000);// 6秒钟读数据超时  
                conn.setRequestMethod("POST");              
                conn.setRequestProperty("Connection", "Keep-Alive");    
                conn.setRequestProperty("Charset", "UTF-8");    
                conn.setRequestProperty("Content-Type", MULTIPART_FORM_DATA + "; boundary=" + BOUNDARY);  
              
                StringBuilder sb = new StringBuilder();    
                
                //上传的表单参数部分，格式请参考文章    
                for (Map.Entry<String, Object> entry : params.entrySet()) {//构建表单字段内容    
                	sb.append("--");    
                    sb.append(BOUNDARY);    
                    sb.append("\r\n");    
                    sb.append("Content-Disposition: form-data; name=\""+ entry.getKey() + "\"\r\n\r\n");    
                    sb.append(entry.getValue());    
                    sb.append("\r\n");    
                }    
  
                sb.append("--");    
                sb.append(BOUNDARY);    
                sb.append("\r\n");    
  
                //System.out.println(sb);
                
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());  
                dos.write(sb.toString().getBytes());  
              
                if (!imageuri.equals("")&&!imageuri.equals(null)) {  
                	dos.writeBytes("Content-Disposition: form-data; name=\""+img+"\"; filename=\"" + imguri + "\"" + "\r\n"+"Content-Type: image/jpeg\r\n\r\n");  
                    FileInputStream fis = new FileInputStream(imageuri);  
                    byte[] buffer = new byte[1024]; // 8k  
                    int count = 0;  
                    while ((count = fis.read(buffer)) != -1)  
                    {  
                      dos.write(buffer, 0, count);  
                    }  
                    dos.writeBytes(end);  
                    fis.close();  
                }  
                System.out.println(dos.toString().getBytes());
                dos.writeBytes("--" + BOUNDARY + "--\r\n");  
                dos.flush();                
                
                int res = conn.getResponseCode();
                System.out.println(res);
                InputStream is = conn.getInputStream();  
                InputStreamReader isr = new InputStreamReader(is, "utf-8");  
                BufferedReader br = new BufferedReader(isr);  
                result = br.readLine();  
              
        	}catch (Exception e) {  
        		e.printStackTrace();
        		result = "{\"ret\":\"898\"}";  
        	}  
        }  
        return result;  
          
    }  
    
    /**  
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输  
     * @param acti.nUrl  
     * @param params  
     * @param files  
     * @return  
     * @throws IOException  
     */  
    public static String post(String actionUrl ,  
            Map <String , String> params , Map <String , File> files)  
            throws IOException  {  

            String BOUNDARY = java.util.UUID.randomUUID ( ).toString ( ) ;  
            String PREFIX = "--" , LINEND = "\r\n" ;  
            String MULTIPART_FROM_DATA = "multipart/form-data" ;  
            String CHARSET = "UTF-8" ;  

            URL uri = new URL ( actionUrl ) ;  
            HttpURLConnection conn = ( HttpURLConnection ) uri  
                    .openConnection ( ) ;  
            conn.setReadTimeout ( 5 * 1000 ) ; // 缓存的最长时间  
            conn.setDoInput ( true ) ;// 允许输入  
            conn.setDoOutput ( true ) ;// 允许输出  
            conn.setUseCaches ( false ) ; // 不允许使用缓存  
            conn.setRequestMethod ( "POST" ) ;  
            conn.setRequestProperty ( "connection" , "keep-alive" ) ;  
            conn.setRequestProperty ( "Charsert" , "UTF-8" ) ;  
            conn.setRequestProperty ( "Content-Type" , MULTIPART_FROM_DATA  
                    + ";boundary=" + BOUNDARY ) ;  

            // 首先组拼文本类型的参数  
            StringBuilder sb = new StringBuilder ( ) ;  
            for ( Map.Entry < String , String > entry : params.entrySet ( ) )  
                {  
                    sb.append ( PREFIX ) ;  
                    sb.append ( BOUNDARY ) ;  
                    sb.append ( LINEND ) ;  
                    sb.append ( "Content-Disposition: form-data; name=\""  
                            + entry.getKey ( ) + "\"" + LINEND ) ;  
                    sb.append ( "Content-Type: text/plain; charset="  
                            + CHARSET + LINEND ) ;  
                    sb.append ( "Content-Transfer-Encoding: 8bit" + LINEND ) ;  
                    sb.append ( LINEND ) ;  
                    sb.append ( entry.getValue ( ) ) ;  
                    sb.append ( LINEND ) ;  
                }  

            DataOutputStream outStream = new DataOutputStream (  
                    conn.getOutputStream ( ) ) ;  
            outStream.write ( sb.toString ( ).getBytes ( ) ) ;  
            // 发送文件数据  
            if ( files != null )  
                for ( Map.Entry < String , File > file : files.entrySet ( ) )  
                    {  
                        StringBuilder sb1 = new StringBuilder ( ) ;  
                        sb1.append ( PREFIX ) ;  
                        sb1.append ( BOUNDARY ) ;  
                        sb1.append ( LINEND ) ;  
                        sb1.append ( "Content-Disposition: form-data; name=\"file\"; filename=\""  
                                + file.getKey ( ) + "\"" + LINEND ) ;  
                        sb1.append ( "Content-Type: application/octet-stream; charset="  
                                + CHARSET + LINEND ) ;  
                        sb1.append ( LINEND ) ;  
                        outStream.write ( sb1.toString ( ).getBytes ( ) ) ;  

                        InputStream is = new FileInputStream (  
                                file.getValue ( ) ) ;  
                        byte [ ] buffer = new byte [ 1024 ] ;  
                        int len = 0 ;  
                        while ( ( len = is.read ( buffer ) ) != - 1 )  
                            {  
                                outStream.write ( buffer , 0 , len ) ;  
                            }  

                        is.close ( ) ;  
                        outStream.write ( LINEND.getBytes ( ) ) ;  
                    }  

            // 请求结束标志  
            byte [ ] end_data = ( PREFIX + BOUNDARY + PREFIX + LINEND )  
                    .getBytes ( ) ;  
            outStream.write ( end_data ) ;  
            outStream.flush ( ) ;  
            // 得到响应码  
            int res = conn.getResponseCode ( ) ;  
            InputStream in = conn.getInputStream ( ) ;  
            if ( res == 200 )  
                {  
                    int ch ;  
                    StringBuilder sb2 = new StringBuilder ( ) ;  
                    while ( ( ch = in.read ( ) ) != - 1 )  
                        {  
                            sb2.append ( ( char ) ch ) ;  
                        }  
                }  
            outStream.close ( ) ;  
            conn.disconnect ( ) ;  
            return in.toString ( ) ;  
              
        }
}
