package zhaozihao.encryrt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EncryptActivity_login extends Activity{
	
	private Button button;
	private EditText user,key;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layoutlogin);
        user = (EditText)findViewById(R.id.username);
        key = (EditText)findViewById(R.id.userkey);
        button = (Button)findViewById(R.id.loginbutton);
        button.setOnClickListener(new OnClickListener(){

    		public void onClick(View v) 
    		{
    			
    			if (user.getText().toString().equals("") || key.getText().toString().equals(""))
    			{
    				Toast.makeText( getApplicationContext(),"请先填入用户名和密码", Toast.LENGTH_SHORT).show();
    			}
    			else
    			{
    				Toast.makeText(getApplicationContext(), "登录中...", Toast.LENGTH_SHORT).show();
    				Thread child = new Thread(new Login());
    				Log.i("button","Login");
    		        child.start();
    		        Log.i("button","Start");
    		        }
    			}
        });
    }
	
	public class Login implements Runnable{

		public void run() {
			// TODO Auto-generated method stub
			
			try 
			{
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
				Socket socket = new Socket(IP,6666);
				DataInputStream din = new DataInputStream(socket.getInputStream());
	   			DataOutputStream dout = new DataOutputStream(socket.getOutputStream());
	   			String username = user.getText().toString();
	   			String userkey = key.getText().toString();
	   			dout.writeUTF("<#LOGIN#>"+username+"|"+userkey);
	   			String str = din.readUTF();
	   			System.out.println(str);
	   			if(str.equals("true"))
	   			{
	   				System.out.println("succeed!!!");
	   				Log.i("button", "成功了LOGIN");
//	   				Toast.makeText(EncryptActivity_login.this, "登录成功", Toast.LENGTH_SHORT).show();
	   				Intent intent = new Intent();
	   				intent.setClass(EncryptActivity_login.this, EncryptActivity_wifi.class);
	   				startActivity(intent);
	   			}
	   			else 
	   			{
	   				System.out.println(str);
//	   				Toast.makeText(EncryptActivity_login.this, "登录失败，请检查网络，用户名和密码", Toast.LENGTH_LONG).show();
	   			}
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	
	}
	
}
