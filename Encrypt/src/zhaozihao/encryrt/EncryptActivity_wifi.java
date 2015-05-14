package zhaozihao.encryrt;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

public class EncryptActivity_wifi extends Activity{
	
	private Button button1,button2,button3;
	private MediaPlayer m;
	private VideoView mVideoView;
	private static final String TAG ="RTSPStreamAudioPlayer";
	private boolean bIsReleased = false;
	private final int BufferLength = 10240;
	private String currentFilePath = "";	//���ڲ����е�URL
	private boolean flag = true;
	//Դ��ַ
	private String strAudioURL = "";			
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layoutwifi);
		
		button1=(Button)this.findViewById(R.id.button1);
		button2=(Button)this.findViewById(R.id.button2);
		button3=(Button)this.findViewById(R.id.button3);
		mVideoView=(VideoView)this.findViewById(R.id.videoView1);
		button1.setText("��ʼ��Ƶ");
		button2.setText("�ؾ�����");
		button3.setText("��������");
		button2.setVisibility(View.INVISIBLE);
		button3.setVisibility(View.INVISIBLE);
		button1.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//				/*65-91��
				File fCreate = new File("/mnt/sdcard/playvideo.avi");
				if (!fCreate.exists()) {
					try {
						fCreate.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Log.i("button", "�����ļ�");
				}
				button1.setVisibility(View.INVISIBLE);
				try{
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
					Socket soc = new Socket(IP,6666);
					DataInputStream din = new DataInputStream(soc.getInputStream());
					DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
					InputStream input = soc.getInputStream();
					dout.writeUTF("playvideo");
					long length = din.readLong();
 					Log.i("button","���յ��˳���"+length);
 					int a = (int)length;
					byte [] buffer = new byte[(int)length];
					FileOutputStream fout = new FileOutputStream(fCreate);
					int size = 0;
					while(size!=-1)
					{
						fout.write(buffer,0,size);
						a= a-size;
						if(a==0)size = -1;
						Log.i("button","��ɵ������С"+size);
						Log.i("button","��ɵ����黹ʣ����û��"+a);
						fout.flush();
						Log.i("button","ִ��flush()");
						if(a==0)break;
						size = input.read(buffer);
						Log.i("button","ִ�����һ��");
				   }
					Log.i("button","����forѭ��");
					fout.close();
					soc.close();
					
//					din.read(buffer);
//					for(int i = 0;i<(int)length;i++)
//					{
//						buffer[i] = din.readByte();
//						Log.i("button","buffer"+i+" "+buffer[i]);
//					}
//					for(int i = 0;i<4096;i++)
//					{
//						Log.i("button", "changdu"+i+" "+buffer[i]);
//					}
//					FileOutputStream fout = new FileOutputStream(fCreate);
//					fout.write(buffer);
//		            fout.flush();
//		            fout.close();
					/*
					File fCreate = new File("/mnt/sdcard/playvideo.avi");
					if (!fCreate.exists()) {
						fCreate.createNewFile();
					}
					BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(fCreate));
					FileOutputStream fileOut = new FileOutputStream(bout);
					long length = din.readLong();
					Log.i("button", "�ļ�����"+length);
					// д���ļ�
					while (true)
					{
						int read = 0;
						byte[] buf = new byte[(int)length];
						if (din != null) 
						{
							read = din.read(buf);
						
						}
						if (read == -1) 
						{
							break;
						}
						
						fileOut.write(buf, 0, read);
					}
					fileOut.flush();
					fileOut.close();
					din.close();
					*/
					

					
//					String str = din.readUTF();
//					while(flag)
//					{
//						if(str.equals(""))
//						{
//							flag = true;
//						}
//						else
//						{
//							//д�ļ�
//							String sContent = str;
//			   				String sDestFile = "/mnt/sdcard/playvideo.avi";
//			   				File destFile = new File(sDestFile);
//			   				if (!destFile.exists()) {
//			   				destFile.createNewFile();
//			   				}
//			   				writeByFileWrite(sDestFile, sContent);
			   				
//			   		SystemClock.sleep(5000);
					//������Ƶ
					String path = "/mnt/sdcard/playvideo.avi";
					mVideoView.setVideoPath(path);
					MediaController mc = new MediaController(EncryptActivity_wifi.this);
					mVideoView.setMediaController(mc);
					mVideoView.requestFocus();
					mVideoView.start();
					button2.setVisibility(View.VISIBLE);
					button3.setVisibility(View.VISIBLE);
//						}
//					}
						    
						    
//					if(str.equals("OK"))
//					{
//						Log.i("button", "bofang");
//						//д�ļ�
//						String sContent = "playvideo";
//		   				String sDestFile = "d:\\playvideo.txt";
//		   				File destFile = new File(sDestFile);
//		   				if (!destFile.exists()) {
//		   				destFile.createNewFile();
//		   				}
//		   				writeByFileWrite(sDestFile, sContent);
//						
//						
//						String path = "/mnt/sdcard/playvideo";
//						String path = "http://www.imobilebbs.com/download/android/boy.3gp";
//						mVideoView.setVideoPath(path);
//					    MediaController mc = new MediaController(EncryptActivity_wifi.this);
//					    mVideoView.setMediaController(mc);
//					    mVideoView.requestFocus();
//					    mVideoView.start();
//					}
//					else Toast.makeText(EncryptActivity_wifi.this,"����ʧ�ܣ�", Toast.LENGTH_SHORT).show();
//				/*193-199
				}catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
				
				
			}
			
		});
		
		button2.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//mVideoView.stopPlayback();
//				m.stop();
				try {
					File fCreate = new File("/mnt/sdcard/playvideo.avi");
					if (!fCreate.exists()) {
						try {
							fCreate.createNewFile();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Log.i("button", "�����ļ�");
					}
					button1.setVisibility(View.INVISIBLE);
					try{
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
					
					Socket soc = new Socket(IP,6666);
					DataInputStream din = new DataInputStream(soc.getInputStream());
					DataOutputStream dout = new DataOutputStream(soc.getOutputStream());
					dout.writeUTF("refuse");
					String str = din.readUTF();
					if(str.equals("OK"))
					{
						Intent intent = new Intent();
						intent.setClass(EncryptActivity_wifi.this, EncryptActivity_init.class);
						startActivity(intent);
					}
					else
					{
						Toast.makeText(EncryptActivity_wifi.this,"�ؾ�ʧ��", Toast.LENGTH_SHORT).show();
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
			}
				
			
		});
		
		button3.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					try{
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
						dout.writeUTF("yanzhengma");
						String msg=din.readUTF();//���շ���������������Ϣ
						if(msg!=null)
						{
							String keystr = msg.trim();
							Intent intent=new Intent();
							intent.setClass(EncryptActivity_wifi.this, EncryptActivity_send.class);
							Bundle bundle=new Bundle();
							bundle.putString("keystr", keystr);
							//��Bundle����assign��Intent
							intent.putExtras(bundle);
							//����EncryptActivity_returninfo
							startActivity(intent);
						}
						else  Toast.makeText(EncryptActivity_wifi.this, "�������", Toast.LENGTH_LONG).show(); 
					}catch (UnknownHostException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}catch(Exception e)
					{
						e.printStackTrace();
					}
				}
				catch(Exception e)
				{
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

	
}
