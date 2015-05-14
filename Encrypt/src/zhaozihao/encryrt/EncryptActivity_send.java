package zhaozihao.encryrt;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.ClipboardManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EncryptActivity_send extends Activity{
	private EditText pwText, keyText;
	private Spinner spinner1,spinner2;
	private Button goButton, copyButton,backButtonofabout;
	private TextView resultView;
	private static final String[] prevStr={"����Ա","�ο�"};
	private static final String[] timeStr={"����ʱ","10����","30����","1Сʱ","2Сʱ","3Сʱ","5Сʱ"};
	private ArrayAdapter<String> adapter1,adapter2;
	public ProgressDialog dialog1=null;
	Socket s = null;
	DataOutputStream dout;//�����
	DataInputStream din;//������
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutsend);
        Intent intent=getIntent();
		String keystr= intent.getStringExtra("keystr");
        
        //����Ϊ��ɫ
        Resources resources = getBaseContext().getResources();
        Drawable HippoDrawable = resources.getDrawable(R.drawable.black);
        pwText=(EditText)this.findViewById(R.id.sdpwEdit);
        keyText=(EditText)this.findViewById(R.id.sdkeyEdit);
        keyText.setText(keystr);
        spinner1=(Spinner)this.findViewById(R.id.sdmySpinner1);
        spinner2=(Spinner)this.findViewById(R.id.sdmySpinner2);
        goButton=(Button)this.findViewById(R.id.sdgoButton);
        copyButton=(Button)this.findViewById(R.id.sdcopyButton);
        resultView=(TextView)this.findViewById(R.id.sdresultView);
        resultView.setTextColor(Color.MAGENTA);
        adapter1=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,prevStr);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(adapter1);
        adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,timeStr);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);
        resultView.setVisibility(View.INVISIBLE);
        copyButton.setVisibility(View.INVISIBLE);  
        goButton.setOnClickListener(new OnClickListener(){
        	public void onClick(View v) {
				// TODO Auto-generated method stub
        		if(pwText.getText().toString().equals("") || keyText.getText().toString().equals("")||pwText.length()<6)
        		{
        			Toast.makeText(EncryptActivity_send.this,"��������6~16λ����", Toast.LENGTH_LONG).show();
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
        	
        	public void onClick(View v)
            {
        		new Thread(){
        			public void run(){
        				try{//�������粢����
        					BufferedReader bf =null;
        			        File file = new File("/mnt/sdcard/IP.txt");
        			        FileReader filer;
        			        String IP = null;
        					try {
        						filer = new FileReader(file);
        						bf = new BufferedReader(filer);
        						IP = bf.readLine();
        					} catch (FileNotFoundException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					} catch (IOException e) {
        						// TODO Auto-generated catch block
        						e.printStackTrace();
        					}
        					Socket s = new Socket(IP,6666);
        					DataInputStream din = new DataInputStream(s.getInputStream());
        					DataOutputStream dout = new DataOutputStream(s.getOutputStream());
        					String result = resultView.getText().toString();
        					String [] number = result.split("\\-");
            	    		int a = number[0].length();
            	    		int b = number[1].length();
            	    		String len1 = Integer.toString(a);
            	    		String len2 = Integer.toString(b);
            	    		String msg = len1+len2+number[0]+number[1];
        					dout.writeUTF("<#result#>"+msg);
        					String str=din.readUTF();//���շ���������������Ϣ
        					Log.i("button",str);
        					if(str!=null)
        					{
        						Toast.makeText(getApplicationContext(),"���ͳɹ���", Toast.LENGTH_LONG).show();
        					}
        					else  Toast.makeText(getApplicationContext(), "����ʧ�ܣ�", Toast.LENGTH_LONG).show(); 
        			        
        				}
        				catch(Exception e){//�����쳣
        					e.printStackTrace();//��ӡ�쳣
        				}			
        			}
        		}.start();
        		
        	 }
        });
    }
    
    public void sendToServerMsg(Socket s) throws IOException
    {//�����������������Ϣ
    	s = this.s;
		dout = new DataOutputStream(s.getOutputStream());
        din = new DataInputStream(s.getInputStream());
    	new Thread(){
    		public void run(){
    	    	try{
    	    		String str = resultView.getText().toString();
    	    		String [] number = str.split("\\-");
    	    		int a = number[0].length();
    	    		int b = number[1].length();
    	    		String len1 = Integer.toString(a);
    	    		String len2 = Integer.toString(b);
    	    		str = len1+len2+number[0]+number[1];
    	    		dout.writeUTF(str);//�����������������Ϣ
    	    		Toast.makeText(getApplicationContext(), "������...", Toast.LENGTH_LONG).show();
    	    		String msg=din.readUTF();//���շ���������������Ϣ
					if(msg.equals("true"))
					{
						Toast.makeText(getApplicationContext(), "���ͳɹ�", Toast.LENGTH_LONG).show();
					}
    	    	}
    	    	catch(Exception e){
    	    		e.printStackTrace();
    	    	}  			
    		}
    	}.start();
    }
    
    
    
    
    //�˵���
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
    	//��������ǰ��ťid��������ʾ����
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
		case 4:
			Intent intent1=new Intent();
			intent1.setClass(EncryptActivity_send.this, EncryptActivity_init.class);
			startActivity(intent1);
			break;
		case 5:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	/** ���ܲ���  */
    private long decode(String str)	//���룺�ַ���ת��������
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
    
    private long midcode(char c)	//�м������1~62!!!
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
    
    private long SquareMultiply(long x,long c,long n)	//ƽ�����㷨������x^c mod n
    {
    	int l, b[];
    	b=new int[128];
    	for(l=0; c!=0; l++)			//ת��Ϊ�����ƴ���ͳ�Ƴ���
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
    
    private String encode(long data)	//���룺��long��ת�����ַ�����
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
    
    private String doEncrypt(String plain,String pk_bstr,String pk_nstr)	//���ɶ�̬����
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