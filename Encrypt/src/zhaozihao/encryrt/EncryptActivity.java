package zhaozihao.encryrt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import zhaozihao.encryrt.R.id;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsSpinner;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EncryptActivity extends Activity {
    /** Called when the activity is first created. */
	private EditText pwText=null; 
	private EditText keyText=null;
	private Spinner spinner1,spinner2;
	private Button goButton, copyButton,backButtonofabout,recbutton,unrecbutton;
	private TextView resultView;
	private static final String[] prevStr={"管理员","游客"};
	private static final String[] timeStr={"不限时","10分钟","30分钟","1小时","2小时","3小时","5小时"};
	private ArrayAdapter<String> adapter1,adapter2;
	private EncryptActivity_SMSreceiver smsReceiver;
	private static final String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
	public ProgressDialog dialog1=null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        String keyword = intent.getStringExtra("keyword");
        //以下为颜色
        Resources resources = getBaseContext().getResources();
        Drawable HippoDrawable = resources.getDrawable(R.drawable.black);
        pwText=(EditText)this.findViewById(R.id.pwEdit);
        keyText=(EditText)this.findViewById(R.id.keyEdit);
        spinner1=(Spinner)this.findViewById(R.id.mySpinner1);
        spinner2=(Spinner)this.findViewById(R.id.mySpinner2);
        goButton=(Button)this.findViewById(R.id.goButton);
        copyButton=(Button)this.findViewById(R.id.copyButton);
        recbutton=(Button)this.findViewById(R.id.receiverSMS);
        unrecbutton=(Button)this.findViewById(R.id.unreceiverSMS);
        resultView=(TextView)this.findViewById(R.id.resultView);
        resultView.setTextColor(Color.MAGENTA);
        adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,prevStr);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,timeStr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        resultView.setVisibility(View.INVISIBLE);
        copyButton.setVisibility(View.INVISIBLE); 
        unrecbutton.setVisibility(View.INVISIBLE);
        goButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
				// TODO Auto-generated method stub
        		if(pwText.getText().toString().equals("") || keyText.getText().toString().equals("")||pwText.length()<6)
        		{
        			Toast.makeText(EncryptActivity.this,"请先填入6~16位密码和验证码", Toast.LENGTH_LONG).show();
        		}
        		else
        		{
        			String plain,pk_b="",pk_n="",tmpstr;
        			String pasw="",Repasw="";
        			pasw += pwText.getText();
        			String Rights = String.valueOf(spinner1.getSelectedItemPosition());
        			String Timer = String.valueOf(spinner2.getSelectedItemPosition());
        			int mod = Integer.parseInt(Timer)+Integer.parseInt(Rights)-96;
        			int tmp = mod%2;
        			if(tmp < 0) tmp += 2;
        			switch(tmp){
        					case 0:Repasw = pasw.substring(0,6);break;
        					case 1:Repasw = pasw.substring(pasw.length()-6,pasw.length());break;
        					default:break;
        			}
        			plain=String.valueOf(spinner1.getSelectedItemPosition())
                            +String.valueOf(spinner2.getSelectedItemPosition())
                            +Repasw
                            +String.valueOf(spinner1.getSelectedItemPosition())
                            +String.valueOf(spinner2.getSelectedItemPosition());
    				tmpstr=""+keyText.getText();
    				for (int i=0;i<3;i++)
    					pk_b+=tmpstr.charAt(i);
    				for (int i=3;i<tmpstr.length();i++)
    					pk_n+=tmpstr.charAt(i);
    				String result="";
    				Log.d("OOOOOOOOOOOOOOOO",plain);
    				result=doEncrypt(plain,pk_b,pk_n);
    				resultView.setText(result);
    				resultView.setVisibility(View.VISIBLE);
    				copyButton.setVisibility(View.VISIBLE);
        		}
			}
        });
        copyButton.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(recbutton.isShown())
				{
					Toast.makeText(getApplicationContext(), "请先开启短信服务", Toast.LENGTH_SHORT).show();
				}
				
				else
				{
					//new一个新的Intent对象，并指定要启动的class
					Intent intent=new Intent();
					intent.setClass(EncryptActivity.this, EncryptActivity_returninfo.class);
					//new一个新的Bundle对象，并将要传递的数据传入
					Bundle bundle=new Bundle();
					String result=(String)resultView.getText();
					bundle.putString("resultView",result);
					//将Bundle对象assign给Intent
					intent.putExtras(bundle);
					//调用EncryptActivity_returninfo
					startActivity(intent);
//					EncryptActivity.this.unregisterReceiver(smsReceiver);
				}
				
			}
        });
        
        recbutton.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//生成一个BroiadcastReceiver对象
				smsReceiver = new EncryptActivity_SMSreceiver();
				//生成一个IntentFilter对象
				IntentFilter filter = new IntentFilter();
				//为IntentFilter添加一个Action
				filter.addAction(SMS_ACTION);
				//将BroadcastReceiver对象注册到系统当中
				EncryptActivity.this.registerReceiver(smsReceiver, filter);
				recbutton.setVisibility(View.INVISIBLE);
				unrecbutton.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "开始短信服务", Toast.LENGTH_SHORT).show();
			}
        });
        unrecbutton.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				BufferedReader bf =null;
		        File file = new File("/mnt/sdcard/Body.txt");
		        FileReader filer;
		        String playv = null;
				try {
					filer = new FileReader(file);
					bf = new BufferedReader(filer);
					playv = bf.readLine();
					keyText.setText(playv);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
        	
        });
        
        /*
        unrecbutton.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//解除BroadcastReceiver对象的注册
				EncryptActivity.this.unregisterReceiver(smsReceiver);
				unrecbutton.setVisibility(View.INVISIBLE);
				recbutton.setVisibility(View.VISIBLE);
				Toast.makeText(getApplicationContext(), "结束短信服务", Toast.LENGTH_SHORT).show();
			}
        });*/
    }
    
    
    
    //菜单栏
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
    	//组名，当前按钮id，排序，显示内容
    	menu.add(1, 1, 1, R.string.changekey);
    	menu.add(2, 2, 3, R.string.about);
    	menu.add(1, 3, 5, R.string.exit);
    	menu.add(1, 4, 2, R.string.backtomain);
    	menu.add(1, 5, 4, R.string.setting);
		return super.onCreateOptionsMenu(menu);
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case 1:
		break;
		case 2:
			setContentView(R.layout.layout3);		
			backButtonofabout= (Button)this.findViewById(R.id.backButtonofabout);
			backButtonofabout.setOnClickListener(new OnClickListener(){

				public void onClick(View v) {
					// TODO Auto-generated method stub
					setContentView(R.layout.main);
				}
			
			});
			break;
		case 3:
			System.exit(0);
			EncryptActivity.this.unregisterReceiver(smsReceiver);
		case 4:
			Intent intent1=new Intent();
			intent1.setClass(EncryptActivity.this, EncryptActivity_init.class);
			startActivity(intent1);
			break;
		case 5:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/** 加密部分  */
    private long decode(String str)	//解码：字符串转化成整型
    {
    	int d, n = str.length();
    	char c;
    	long  data = 0;
    	for(int i=0; i<n; i++)
    	{
    		c = str.charAt(i);
    		if(c>='0' && c<='9')
    			d = c-'0';
    		else if(c>='A' && c<='Z')
    			d = c-'A'+10;
    		else if(c>='a' && c<='z')
    			d = c-'a'+36;
    		else if(c == '#')
    			d = 62;
    		else
    			d = 63;
    		data = data*64 + d;
    	}
    	return data;
    }
    
    private long midcode(char c)	//中间变量：1~62!!!
    {
    	long code=0;
		if(c>='0'&&c<='9')
			code = c-47;
		else if(c>='A'&&c<='Z')
			code = c-64+10;
		else if(c>='a'&&c<='z')
			code = c-96+36;
		return code;
    }
    
    private long SquareMultiply(long x,long c,long n)	//平方乘算法：计算x^c mod n
    {
    	int l, b[];
    	b=new int[128];
    	for(l=0; c!=0; l++)			//转化为二进制串，统计长度
    	{
    		b[l] = (int) (c%2);
    		c = c/2;
    	}
    	long z = 1;
    	for(int i=l-1; i>=0; i--)
    	{
    		z = (z*z)%n;
    		if(b[i] == 1) z = (z*x)%n;
    	}
    	return z;
    }
    
    private String encode(long data)	//编码：将long型转化成字符串型
    {
    	int temp;
    	String s="";
    	temp = (int) (data%64);
    	if(data>=64)
    		s +=encode(data/64);
    	if(temp<10)
    		s += (char)(temp+48);
    	else if(temp<36)
    		s += (char)(temp+65-10);
    	else if(temp<62)
    		s += (char)(temp+97-36);
    	else if(temp == 62)
    		s += '#';
    	else
    		s += '*';
    	return s;
    }
    
    private String doEncrypt(String plain,String pk_bstr,String pk_nstr)	//生成动态密码
    {
    	long pk_b,pk_n;
    	pk_b=decode(pk_bstr);
    	pk_n=decode(pk_nstr);
    	long part1,part2;
     	long a=0,b=0;
   // 	int m = pwText.length();

 /*   	for(int i=0;i<n;i++)
    	{
    		a=a+midcode(plain.charAt(i))*(long)Math.pow(64, n-1-i);
    	}*/
    	a=midcode(plain.charAt(0))*16777216+midcode(plain.charAt(1))*262144+midcode(plain.charAt(2))*4096
    	  +midcode(plain.charAt(3))*64+midcode(plain.charAt(4));
    	part1=SquareMultiply(a,pk_b,pk_n);
/*    	for(int i=n;i<n*2;i++)
    	{
    		b=b+midcode(plain.charAt(i))*(long)Math.pow(64, 2*n-1-i);
    	}*/
    	b=midcode(plain.charAt(5))*16777216+midcode(plain.charAt(6))*262144+midcode(plain.charAt(7))*4096
   	  +midcode(plain.charAt(8))*64+midcode(plain.charAt(9));
    	part2=SquareMultiply(b,pk_b,pk_n);    	
    	return encode(part1)+"-"+encode(part2);
    }
}