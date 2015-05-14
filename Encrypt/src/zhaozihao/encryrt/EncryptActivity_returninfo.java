package zhaozihao.encryrt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.provider.ContactsContract; 
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.net.Uri;

public class EncryptActivity_returninfo extends Activity
{
	private Button mButton1; 
	private EditText mEditText1; 
	private EditText mEditText2; 
	private Button mButton2;
	private static final int PICK_CONTACT_SUBACTIVITY = 1;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout2);
		Intent intent=getIntent();
		String result= intent.getStringExtra("resultView");
		/**���Ͷ���*/
	    /*͸��findViewById������������EditText1,EditText2��Button����*/
	    mEditText1= (EditText)this.findViewById(R.id.myEditText1);
		mEditText2 = (EditText)this.findViewById(R.id.myEditText2);
	    mButton1 = (Button)this.findViewById(R.id.myButton1);
	    mButton2 = (Button)this.findViewById(R.id.myButton2);
	    /*��Ĭ�����ּ���EditText��*/ 
	    BufferedReader bf =null;
        File file = new File("/mnt/sdcard/Address.txt");
        FileReader filer;
        String playv = null;
		try {
			filer = new FileReader(file);
			bf = new BufferedReader(filer);
			playv = bf.readLine();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mEditText1.setText(playv);
//	    mEditText1.setText("������绰����..."); 
	    mEditText2.setText("��̬������"+result); 
	    /*�趨onClickListener ��ʹ���ߵ�ѡEditTextʱ������Ӧ*/
//	    mEditText1.setOnClickListener(new EditText.OnClickListener()
//	    { 
//	      public void onClick(View v) 
//	      {
//	        /*��ѡEditTextʱ�������*/
//	        mEditText1.setText("");
//	        } 
//	      });
//	    /*�趨onClickListener ��ʹ���ߵ�ѡEditTextʱ������Ӧ*/ 
//	    mEditText2.setOnClickListener(new EditText.OnClickListener()
//	    { 
//	      public void onClick(View v) 
//	      { 
//	    	  /*��ѡEditTextʱ�������*/ 
//		        mEditText2.setText("");
//	        }
//	      } ); 
	    /*�趨onClickListener ��ʹ���ߵ�ѡButtonʱ������Ӧ��ͨѶ¼*/
	    mButton2.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v)
	    	{
	    		// TODO Auto-generated method stub 
	    	      /*����Uri��ȡ�������˵���Դλ��*/ 
	    	       Uri uri = Uri.parse("content://contacts/people/"); 
	    	      /*͸��Intent��ȡ�����������ݲ��ش���ѡ��ֵ*/
	    	      Intent intent = new Intent(Intent.ACTION_PICK, uri);
	    	      /*�����µ�Activity��������Activity�ش�ֵ*/ 
	    	    //startActivityForResult(intent, 0);
	    		/*��ѡbutton2ʱ�������*/ 
		        mEditText1.setText("");
	    	    startActivityForResult(new Intent(Intent.ACTION_PICK,
	    	    		  android.provider.ContactsContract.Contacts.CONTENT_URI),
	    	    		  PICK_CONTACT_SUBACTIVITY);
	    	}
	    });
	    
	    /*�趨onClickListener ��ʹ���ߵ�ѡButtonʱ������Ӧ*/
	    mButton1.setOnClickListener(new Button.OnClickListener() 
	    {
	      
	    	public void onClick(View v) 
	    	{ 
	    		/*��EditText1ȡ�ü�Ѷ�ռ��˵绰*/ 
	    		String strDestAddress = mEditText1.getText().toString();
	    		/*��EditText2ȡ�ü�Ѷ��������*/ 
	    		String strMessage = mEditText2.getText().toString(); 
	    		/*����һȡ��default instance�� SmsManager���� */ 
	    		SmsManager smsManager = SmsManager.getDefault(); 
	    		// TODO Auto-generated method stub
	    		/*����ռ��˵绰��ʽ���Ѷ�����Ƿ񳬹�70�ַ�*/ 
	    		if(isPhoneNumberValid(strDestAddress)==true && iswithin70(strMessage)==true)
	    		{ 
	    			try 
	    			{ 
	    				/*�������������ͨ���������,���ͼ�Ѷ * 
	    				 * �Ƚ���һPendingIntent����ʹ��getBroadcast()��������Broadcast * 
	    				 * ��PendingIntent,�绰,��Ѷ���ֵȲ�������sendTextMessage()�������ͼ�Ѷ*/ 
	    				PendingIntent mPI = PendingIntent.getBroadcast(EncryptActivity_returninfo.this, 0, new Intent(), 0); 
	    				smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
	    			} 
	    			catch(Exception e) 
	    			{
	    				e.printStackTrace();
	    			}
	    			Toast.makeText(EncryptActivity_returninfo.this, "�ͳ��ɹ�!!" , Toast.LENGTH_SHORT).show();
	    			mEditText1.setText("");
	    			mEditText2.setText(""); 
	    		} 
	    		/*�绰��ʽ���Ѷ���ֲ���������ʱ,ʹ��Toast��֪�û����*/ 
	    		else
	    		{
	    			/*�绰��ʽ����*/ 
	    			if (isPhoneNumberValid(strDestAddress)==false) 
	    			{ 
	    				/*����������70�ַ�*/
	    				if(iswithin70(strMessage)==false) 
	    				{ 
	    					Toast.makeText(EncryptActivity_returninfo.this, "�绰�����ʽ����+�������ݳ���70��,����!!", Toast.LENGTH_SHORT).show();
	    				} 
	    				else 
	    				{
	    					Toast.makeText(EncryptActivity_returninfo.this, "�绰�����ʽ����,����!!" , Toast.LENGTH_SHORT).show();
	    				}
	    		} 
	    		/*��������70�ַ�*/
	    		else if (iswithin70(strMessage)==false) 
	    		{ 
	    			Toast.makeText(EncryptActivity_returninfo.this, "�������ݳ���70��,��ɾ����������!!", Toast.LENGTH_SHORT).show();
	    		}
	    	} 
	    		
	    		try {
					writeByFileWrite("/mnt/sdcard/Body.txt","" );
					writeByFileWrite("/mnt/sdcard/Adress.txt","" );
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
	     }
	   });
	}
	
	public static void writeByFileWrite(String _sDestFile, String _sContent)throws IOException {
		FileWriter fw = null;
		try 
		{
			fw = new FileWriter(_sDestFile);
			fw.write(_sContent);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fw != null) 
			{
				fw.close();
				fw = null;
			}
		}
    }
	
	/*����ַ����Ƿ�Ϊ�绰����ķ���,���ش�true or false���ж�ֵ*/
	public static boolean isPhoneNumberValid(String phoneNumber)
	{
	    boolean isValid = false; 
	    /* �ɽ��ܵĵ绰��ʽ��: * ^\\(? : ����ʹ�� "(" ��Ϊ��ͷ * (\\d{3}):
	     *  �������������� * \\)? : ����ʹ��")"���� * [- ]? : 
	     *  ��������ʽ�����ʹ�þ�ѡ���Ե� "-". * (\\d{3}) :
	     *   �ٽ������������� * [- ]? : 
	     *   ����ʹ�þ�ѡ���Ե� "-" ����. * (\\d{4})$: 
	     *   ���ĸ����ֽ���. * ���Աȶ��������ָ�ʽ:
	     *    * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */ 
	    String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	    /* �ɽ��ܵĵ绰��ʽ��: * ^\\(? :
	     *  ����ʹ�� "(" ��Ϊ��ͷ * (\\d{2}): 
	     *  �������������� * \\)? : ����ʹ��")"���� * [- ]? : 
	     *  ��������ʽ�����ʹ�þ�ѡ���Ե� "-". * (\\d{4}) : 
	     *  �ٽ������ĸ����� * [- ]? : 
	     *  ����ʹ�þ�ѡ���Ե� "-" ����. * (\\d{4})$: 
	     *  ���ĸ����ֽ���. * ���Աȶ��������ָ�ʽ: * 
	     *  (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
	    String expression2 ="^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	    /* �ɽ��ܵĵ绰��ʽ��: * ^\\(? :
	     *  ����ʹ�� "(" ��Ϊ��ͷ * (\\d{2}): 
	     *  �������������� * \\)? : ����ʹ��")"���� * [- ]? : 
	     *  ��������ʽ�����ʹ�þ�ѡ���Ե� "-". * (\\d{4}) : 
	     *  �ٽ������ĸ����� * [- ]? : 
	     *  ����ʹ�þ�ѡ���Ե� "-" ����. * (\\d{4})$: 
	     *  ���ĸ����ֽ���. * ���Աȶ��������ָ�ʽ: * 
	     *  (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
	    String expression3 ="^\\+?+(\\d{2})[- ]?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	    CharSequence inputStr = phoneNumber;
	    /*����Pattern*/ Pattern pattern = Pattern.compile(expression);
	    /*��Pattern �Բ�������Matcher��Regular expression*/ 
	    Matcher matcher = pattern.matcher(inputStr); 
	    /*����Pattern2*/ Pattern pattern2 =Pattern.compile(expression2);
	    /*��Pattern2 �Բ�������Matcher2��Regular expression*/ 
	    Matcher matcher2= pattern2.matcher(inputStr); 
	    /*����Pattern3*/ Pattern pattern3 = Pattern.compile(expression3);
	    /*��Pattern �Բ�������Matcher��Regular expression*/ 
	    Matcher matcher3 = pattern3.matcher(inputStr); 
	    if(matcher.matches()||matcher2.matches()||matcher3.matches()) 
	    {
	      isValid = true;
	    } 
	    return isValid;
	}
	public static boolean iswithin70(String text)
	{
	    if (text.length()<= 70) 
	      return true; 
	    else
	      return false; 
	} 
	/*ͨѶ¼*/
	protected void onActivityResult (int requestCode, int resultCode, Intent data)
	{ 
	    // TODO Auto-generated method stub 
	    try 
	    { 
	      switch (requestCode) 
	      { 
	        case PICK_CONTACT_SUBACTIVITY:
	          final Uri uriRet = data.getData();
	          if(uriRet != null) 
	          { 
	            try
	            {
	              /* ��android.permission.READ_CONTACTSȨ�� */
	              Cursor c = managedQuery(uriRet, null, null, null, null);
	              /*��Cursor�Ƶ�������ǰ��*/
	              c.moveToFirst(); 
	              /*ȡ�������˵ĵ绰*/ 
	              int contactId = c.getInt(c.getColumnIndex(ContactsContract.Contacts._ID)); 
	              Cursor phones = getContentResolver().query ( 
	            		  ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, 
	            		  ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId, null, null ); 
	              StringBuffer sb = new StringBuffer(); 
	              int typePhone, resType; 
	              String numPhone; 
	              if (phones.getCount()>0) 
	              { 
	                phones.moveToFirst(); 
	                /* �趨һ��绰���� */
	                typePhone = phones.getInt ( phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE) );
	                numPhone = phones.getString (phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) ); 
	                resType = ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(typePhone);
	                sb.append(getString(resType) +": "+ numPhone +"\n");
	                /*���绰д��EditText1��*/
	                mEditText1.setText(numPhone);
	                } 
	              else
	              { 
	                sb.append("no phone number found");
	              }
	              /*Toast�Ƿ��ȡ�������ĵ绰������绰����*/
	              Toast.makeText(this, sb.toString(), Toast.LENGTH_SHORT).show(); 
	            } 
	            catch(Exception e)
	            { 
	              e.printStackTrace();
	            }
	          } 
	          break;
	          default: break; 
	        }
	    } 
	    catch(Exception e) 
	    { 
	      e.printStackTrace(); 
	    } 
	    super.onActivityResult(requestCode, resultCode, data);
	}
}
