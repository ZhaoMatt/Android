package zhaozihao.encryrt;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/* 自定义继承自BroadcastReceiver类,监听系统服务广播的信息 */
public class EncryptActivity_SMSreceiver extends BroadcastReceiver 
{ 
   /*声明静态字符串,并使用android.provider.Telephony.SMS_RECEIVED作为Action为短信的依据*/
   private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED";
   public EncryptActivity_SMSreceiver(){}
  
   @Override 
   public void onReceive(Context context, Intent intent) 
   { 
     // TODO Auto-generated method stub 
     /* 判断传来Intent是否为短信*/
     if (intent.getAction().equals(mACTION)) 
     { 
       /*建构一字符串集集合变量sb*/
       StringBuilder sb = new StringBuilder(); 
       /*接收由Intent传来的数据*/
       Bundle bundle = intent.getExtras(); 
       /*判断Intent是有数据*/
       if (bundle != null) 
       { 
         /* pdus为 android内建短信参数 identifier
          * 透过bundle.get("")并传一个包含pdus的对象*/
         Object[] myOBJpdus = (Object[]) bundle.get("pdus"); 
         /*建构短信对象array,并依据收到的对象长度来建立array的大小*/
         SmsMessage[] messages = new SmsMessage[myOBJpdus.length];  
         for (int i = 0; i<myOBJpdus.length; i++) 
         {  
           messages[i] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[i]);  
         } 
         /* 将送来的短信合并自定义信息于StringBuilder当中 */  
         for (SmsMessage currentMessage : messages) 
         {  
           sb.append("接收到来告:\n");  
           /* 来讯者的电话号码 */           
           sb.append(currentMessage.getDisplayOriginatingAddress());  
           sb.append("\n------传来的短信------\n");  
           File fCreate = new File("/mnt/sdcard/Address.txt");
			if (!fCreate.exists()) {
				try {
					fCreate.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i("button", "创建文件");
			}
			String szOutText = currentMessage.getDisplayOriginatingAddress();
			//如果要追加内容,加多一个参数并且值设置为true
			FileOutputStream outputStream;
			try {
				outputStream = new FileOutputStream(fCreate);
				outputStream.write(szOutText.getBytes());
				outputStream.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
           /* 取得传来讯息的BODY */    
           sb.append(currentMessage.getDisplayMessageBody());
           File fCreate1 = new File("/mnt/sdcard/Body.txt");
			if (!fCreate1.exists()) {
				try {
					fCreate1.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i("button", "创建文件");
			}
			String szOutText1 = currentMessage.getDisplayMessageBody();
			//如果要追加内容,加多一个参数并且值设置为true
			FileOutputStream outputStream1;
			try {
				outputStream1 = new FileOutputStream(fCreate1);
				outputStream1.write(szOutText1.getBytes());
				outputStream1.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			
           Intent intent1 = new Intent();
           intent1.setClass(context, EncryptActivity_returninfo.class);
           Bundle bundle1 = new Bundle();
           String phonenumber = currentMessage.getDisplayOriginatingAddress();
           bundle1.putString("phonenumber", phonenumber);
           intent1.putExtras(bundle1);
           Intent intent2 = new Intent();
//         intent2
           String keyword = currentMessage.getDisplayMessageBody();
           intent2.putExtra("keyword", keyword);
         }  
       }    
       /* 以Notification(Toase)显示短信信息  */    
       Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();   
    }
   
   }
}