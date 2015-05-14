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
		/**发送短信*/
	    /*透过findViewById建构子来建构EditText1,EditText2与Button对象*/
	    mEditText1= (EditText)this.findViewById(R.id.myEditText1);
		mEditText2 = (EditText)this.findViewById(R.id.myEditText2);
	    mButton1 = (Button)this.findViewById(R.id.myButton1);
	    mButton2 = (Button)this.findViewById(R.id.myButton2);
	    /*将默认文字加载EditText中*/ 
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
//	    mEditText1.setText("请输入电话号码..."); 
	    mEditText2.setText("动态密码是"+result); 
	    /*设定onClickListener 让使用者点选EditText时做出反应*/
//	    mEditText1.setOnClickListener(new EditText.OnClickListener()
//	    { 
//	      public void onClick(View v) 
//	      {
//	        /*点选EditText时清空内文*/
//	        mEditText1.setText("");
//	        } 
//	      });
//	    /*设定onClickListener 让使用者点选EditText时做出反应*/ 
//	    mEditText2.setOnClickListener(new EditText.OnClickListener()
//	    { 
//	      public void onClick(View v) 
//	      { 
//	    	  /*点选EditText时清空内文*/ 
//		        mEditText2.setText("");
//	        }
//	      } ); 
	    /*设定onClickListener 让使用者点选Button时做出反应：通讯录*/
	    mButton2.setOnClickListener(new Button.OnClickListener(){
	    	public void onClick(View v)
	    	{
	    		// TODO Auto-generated method stub 
	    	      /*建构Uri来取得联络人的资源位置*/ 
	    	       Uri uri = Uri.parse("content://contacts/people/"); 
	    	      /*透过Intent来取得联络人数据并回传所选的值*/
	    	      Intent intent = new Intent(Intent.ACTION_PICK, uri);
	    	      /*开启新的Activity并期望该Activity回传值*/ 
	    	    //startActivityForResult(intent, 0);
	    		/*点选button2时清空内文*/ 
		        mEditText1.setText("");
	    	    startActivityForResult(new Intent(Intent.ACTION_PICK,
	    	    		  android.provider.ContactsContract.Contacts.CONTENT_URI),
	    	    		  PICK_CONTACT_SUBACTIVITY);
	    	}
	    });
	    
	    /*设定onClickListener 让使用者点选Button时做出反应*/
	    mButton1.setOnClickListener(new Button.OnClickListener() 
	    {
	      
	    	public void onClick(View v) 
	    	{ 
	    		/*由EditText1取得简讯收件人电话*/ 
	    		String strDestAddress = mEditText1.getText().toString();
	    		/*由EditText2取得简讯文字内容*/ 
	    		String strMessage = mEditText2.getText().toString(); 
	    		/*建构一取得default instance的 SmsManager对象 */ 
	    		SmsManager smsManager = SmsManager.getDefault(); 
	    		// TODO Auto-generated method stub
	    		/*检查收件人电话格式与简讯字数是否超过70字符*/ 
	    		if(isPhoneNumberValid(strDestAddress)==true && iswithin70(strMessage)==true)
	    		{ 
	    			try 
	    			{ 
	    				/*两个条件都检查通过的情况下,发送简讯 * 
	    				 * 先建构一PendingIntent对象并使用getBroadcast()方法进行Broadcast * 
	    				 * 将PendingIntent,电话,简讯文字等参数传入sendTextMessage()方法发送简讯*/ 
	    				PendingIntent mPI = PendingIntent.getBroadcast(EncryptActivity_returninfo.this, 0, new Intent(), 0); 
	    				smsManager.sendTextMessage(strDestAddress, null, strMessage, mPI, null);
	    			} 
	    			catch(Exception e) 
	    			{
	    				e.printStackTrace();
	    			}
	    			Toast.makeText(EncryptActivity_returninfo.this, "送出成功!!" , Toast.LENGTH_SHORT).show();
	    			mEditText1.setText("");
	    			mEditText2.setText(""); 
	    		} 
	    		/*电话格式与简讯文字不符合条件时,使用Toast告知用户检查*/ 
	    		else
	    		{
	    			/*电话格式不符*/ 
	    			if (isPhoneNumberValid(strDestAddress)==false) 
	    			{ 
	    				/*且字数超过70字符*/
	    				if(iswithin70(strMessage)==false) 
	    				{ 
	    					Toast.makeText(EncryptActivity_returninfo.this, "电话号码格式错误+短信内容超过70字,请检查!!", Toast.LENGTH_SHORT).show();
	    				} 
	    				else 
	    				{
	    					Toast.makeText(EncryptActivity_returninfo.this, "电话号码格式错误,请检查!!" , Toast.LENGTH_SHORT).show();
	    				}
	    		} 
	    		/*字数超过70字符*/
	    		else if (iswithin70(strMessage)==false) 
	    		{ 
	    			Toast.makeText(EncryptActivity_returninfo.this, "短信内容超过70字,请删除部分内容!!", Toast.LENGTH_SHORT).show();
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
	
	/*检查字符串是否为电话号码的方法,并回传true or false的判断值*/
	public static boolean isPhoneNumberValid(String phoneNumber)
	{
	    boolean isValid = false; 
	    /* 可接受的电话格式有: * ^\\(? : 可以使用 "(" 作为开头 * (\\d{3}):
	     *  紧接着三个数字 * \\)? : 可以使用")"接续 * [- ]? : 
	     *  在上述格式后可以使用具选择性的 "-". * (\\d{3}) :
	     *   再紧接着三个数字 * [- ]? : 
	     *   可以使用具选择性的 "-" 接续. * (\\d{4})$: 
	     *   以四个数字结束. * 可以比对下列数字格式:
	     *    * (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */ 
	    String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	    /* 可接受的电话格式有: * ^\\(? :
	     *  可以使用 "(" 作为开头 * (\\d{2}): 
	     *  紧接着两个数字 * \\)? : 可以使用")"接续 * [- ]? : 
	     *  在上述格式后可以使用具选择性的 "-". * (\\d{4}) : 
	     *  再紧接着四个数字 * [- ]? : 
	     *  可以使用具选择性的 "-" 接续. * (\\d{4})$: 
	     *  以四个数字结束. * 可以比对下列数字格式: * 
	     *  (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
	    String expression2 ="^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	    /* 可接受的电话格式有: * ^\\(? :
	     *  可以使用 "(" 作为开头 * (\\d{2}): 
	     *  紧接着两个数字 * \\)? : 可以使用")"接续 * [- ]? : 
	     *  在上述格式后可以使用具选择性的 "-". * (\\d{4}) : 
	     *  再紧接着四个数字 * [- ]? : 
	     *  可以使用具选择性的 "-" 接续. * (\\d{4})$: 
	     *  以四个数字结束. * 可以比对下列数字格式: * 
	     *  (123)456-7890, 123-456-7890, 1234567890, (123)-456-7890 */
	    String expression3 ="^\\+?+(\\d{2})[- ]?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
	    CharSequence inputStr = phoneNumber;
	    /*建立Pattern*/ Pattern pattern = Pattern.compile(expression);
	    /*将Pattern 以参数传入Matcher作Regular expression*/ 
	    Matcher matcher = pattern.matcher(inputStr); 
	    /*建立Pattern2*/ Pattern pattern2 =Pattern.compile(expression2);
	    /*将Pattern2 以参数传入Matcher2作Regular expression*/ 
	    Matcher matcher2= pattern2.matcher(inputStr); 
	    /*建立Pattern3*/ Pattern pattern3 = Pattern.compile(expression3);
	    /*将Pattern 以参数传入Matcher作Regular expression*/ 
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
	/*通讯录*/
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
	              /* 有android.permission.READ_CONTACTS权限 */
	              Cursor c = managedQuery(uriRet, null, null, null, null);
	              /*将Cursor移到资料最前端*/
	              c.moveToFirst(); 
	              /*取得联络人的电话*/ 
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
	                /* 设定一组电话号码 */
	                typePhone = phones.getInt ( phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE) );
	                numPhone = phones.getString (phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER) ); 
	                resType = ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(typePhone);
	                sb.append(getString(resType) +": "+ numPhone +"\n");
	                /*将电话写入EditText1中*/
	                mEditText1.setText(numPhone);
	                } 
	              else
	              { 
	                sb.append("no phone number found");
	              }
	              /*Toast是否读取到完整的电话种类与电话号码*/
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
