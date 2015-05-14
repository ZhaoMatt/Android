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

/* �Զ���̳���BroadcastReceiver��,����ϵͳ����㲥����Ϣ */
public class EncryptActivity_SMSreceiver extends BroadcastReceiver 
{ 
   /*������̬�ַ���,��ʹ��android.provider.Telephony.SMS_RECEIVED��ΪActionΪ���ŵ�����*/
   private static final String mACTION = "android.provider.Telephony.SMS_RECEIVED";
   public EncryptActivity_SMSreceiver(){}
  
   @Override 
   public void onReceive(Context context, Intent intent) 
   { 
     // TODO Auto-generated method stub 
     /* �жϴ���Intent�Ƿ�Ϊ����*/
     if (intent.getAction().equals(mACTION)) 
     { 
       /*����һ�ַ��������ϱ���sb*/
       StringBuilder sb = new StringBuilder(); 
       /*������Intent����������*/
       Bundle bundle = intent.getExtras(); 
       /*�ж�Intent��������*/
       if (bundle != null) 
       { 
         /* pdusΪ android�ڽ����Ų��� identifier
          * ͸��bundle.get("")����һ������pdus�Ķ���*/
         Object[] myOBJpdus = (Object[]) bundle.get("pdus"); 
         /*�������Ŷ���array,�������յ��Ķ��󳤶�������array�Ĵ�С*/
         SmsMessage[] messages = new SmsMessage[myOBJpdus.length];  
         for (int i = 0; i<myOBJpdus.length; i++) 
         {  
           messages[i] = SmsMessage.createFromPdu ((byte[]) myOBJpdus[i]);  
         } 
         /* �������Ķ��źϲ��Զ�����Ϣ��StringBuilder���� */  
         for (SmsMessage currentMessage : messages) 
         {  
           sb.append("���յ�����:\n");  
           /* ��Ѷ�ߵĵ绰���� */           
           sb.append(currentMessage.getDisplayOriginatingAddress());  
           sb.append("\n------�����Ķ���------\n");  
           File fCreate = new File("/mnt/sdcard/Address.txt");
			if (!fCreate.exists()) {
				try {
					fCreate.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i("button", "�����ļ�");
			}
			String szOutText = currentMessage.getDisplayOriginatingAddress();
			//���Ҫ׷������,�Ӷ�һ����������ֵ����Ϊtrue
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
			
           /* ȡ�ô���ѶϢ��BODY */    
           sb.append(currentMessage.getDisplayMessageBody());
           File fCreate1 = new File("/mnt/sdcard/Body.txt");
			if (!fCreate1.exists()) {
				try {
					fCreate1.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i("button", "�����ļ�");
			}
			String szOutText1 = currentMessage.getDisplayMessageBody();
			//���Ҫ׷������,�Ӷ�һ����������ֵ����Ϊtrue
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
       /* ��Notification(Toase)��ʾ������Ϣ  */    
       Toast.makeText(context, sb.toString(), Toast.LENGTH_LONG).show();   
    }
   
   }
}