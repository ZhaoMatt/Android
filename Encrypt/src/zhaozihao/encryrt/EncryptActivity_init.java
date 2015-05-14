package zhaozihao.encryrt;

import android.R.color;
import android.R.id;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EncryptActivity_init extends Activity{
	
	private Button button01,button02,button03;
	private TextView text1,text2,textView1,textView2,textView3;
	private WifiManager wifimanager;
	int WIFI_VALUE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.initlayout);
		button01=(Button)this.findViewById(R.id.into2G);
		button02=(Button)this.findViewById(R.id.intowifi);
		button01.setText("进入离线模式");
		button02.setText("进入wifi模式");
		button03=(Button)this.findViewById(R.id.openwifi);
		text1=(TextView)this.findViewById(R.id.into2G);
		text2=(TextView)this.findViewById(R.id.intowifi);
		textView1=(TextView)this.findViewById(R.id.textView1);
		textView2=(TextView)this.findViewById(R.id.textView2);
		textView3=(TextView)this.findViewById(R.id.textView3);
		button01.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//转到离线模式
				Intent intent1=new Intent();
				intent1.setClass(EncryptActivity_init.this, EncryptActivity.class);
				startActivity(intent1);
			}			
		});
		
		button02.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent();
				intent2.setClass(EncryptActivity_init.this, EncryptActivity_login.class);
				startActivity(intent2);
			}			
		});
		
		wifimanager=(WifiManager)this.getSystemService(Context.WIFI_SERVICE);
		//判断wifi是否为已打开或正在打开中
		if(wifimanager.isWifiEnabled())
		{
			//判断wifi是否已经打开、
			if(wifimanager.getWifiState()==wifimanager.WIFI_STATE_ENABLED)
			{
				//若wifi已经打开
				button03.setText(R.string.closewifi);
				WIFI_VALUE=1;
			}
			//正在打开中
			else
			{
				button03.setText(R.string.openwifi);
				WIFI_VALUE=0;
			}
		}
		//wifi为关闭或关闭中
		else
		{
			button03.setText(R.string.openwifi);
			WIFI_VALUE=0;
		}
		
		button03.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//wifi已打开时
				if(WIFI_VALUE==1)
				{
					try
					{
						/* 判断WiFi状态是否为已启动 */
			            if(wifimanager.isWifiEnabled() )
			            {
			              /* 关闭WiFi */
			              if(wifimanager.setWifiEnabled(false))
			              {
			                textView3.setText(R.string.str_stop_wifi_done);
			                button03.setText(R.string.openwifi);
			                WIFI_VALUE=0;
			              }
			              else
			              {
			            	  textView3.setText(R.string.str_stop_wifi_failed);
			            	  WIFI_VALUE=1;
			              }
			            }
			            else
			            {
			              /* WiFi状态不为已启动状态时 */
			              switch(wifimanager.getWifiState())
			              {
			                /* WiFi正在启动过程中，导致无法关闭... */
			                case WifiManager.WIFI_STATE_ENABLING:
			                	textView3.setText(getResources().getText(R.string.str_stop_wifi_failed)
			                			+":"+getResources().getText(R.string.str_wifi_enabling));
			                	WIFI_VALUE=1;
			                  break;
			                /* WiFi正在关闭过程中，导致无法关闭... */
			                case WifiManager.WIFI_STATE_DISABLING:
			                	textView3.setText(
			                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_disabling));
			                	WIFI_VALUE=0;
			                  break;
			                /* WiFi已经关闭 */
			                case WifiManager.WIFI_STATE_DISABLED:
			                	textView3.setText(
			                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_disabled));
			                	WIFI_VALUE=0;
			                  break;
			                /* 无法取得或辨识WiFi状态 */
			                case WifiManager.WIFI_STATE_UNKNOWN:
			                default:
			                	textView3.setText(
			                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_unknow));
			                	WIFI_VALUE=1;
			                  break;
			              }
			            }
					}
					catch(Exception e)
					{
						Log.i("HIPPO",e.toString());
						e.printStackTrace();
					}
				}
				//wifi不是已打开状态
				else if(WIFI_VALUE==0)
				{
					try
			          {
			            /* 确认WiFi服务是关闭且不在启动操作中 */
			            if(!wifimanager.isWifiEnabled() && wifimanager.getWifiState()!=WifiManager.WIFI_STATE_ENABLING )
			            {
			              if(wifimanager.setWifiEnabled(true))
			              {
			                switch(wifimanager.getWifiState())
			                {
			                  /* WiFi正在启动启过程中，导致无法启动... */
			                  case WifiManager.WIFI_STATE_ENABLING:
			                	  textView3.setText(getResources().getText(R.string.str_wifi_enabling));
			                	  WIFI_VALUE=1;	
			                	  
			                	  textView3.setText(getResources().getText(R.string.str_start_wifi_done));
			                    break;
			                  /* WiFi已经为启动，无法再次启动... */
			                  case WifiManager.WIFI_STATE_ENABLED:
			                	  textView3.setText(getResources().getText(R.string.str_start_wifi_done));
			                	  WIFI_VALUE=1;
			                    break;
			                  /* 其他未知的错误 */
			                  default:
			                	  textView3.setText(
			                      getResources().getText(R.string.str_start_wifi_failed)+":"+
			                      getResources().getText(R.string.str_wifi_unknow));
			                    break;
			                }
			              }
			              else
	                      {
	                        textView3.setText(R.string.str_start_wifi_failed);
	                        WIFI_VALUE=0;
	                      }
			            }
			            else
			            {
			              switch(wifimanager.getWifiState())
			              {
			                /* WiFi正在启动过程中，导致无法启动... */
			                case WifiManager.WIFI_STATE_ENABLING:
			                  textView3.setText(
			                    getResources().getText(R.string.str_start_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_enabling));
			                  WIFI_VALUE=1;
			                  
			                  textView3.setText(getResources().getText(R.string.str_start_wifi_done));
			                  break;
			                /* WiFi正在关闭过程中，导致无法启动... */
			                case WifiManager.WIFI_STATE_DISABLING:
			                	textView3.setText(
			                    getResources().getText(R.string.str_start_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_disabling));
			                	WIFI_VALUE=0;
			                  break;
			                /* WiFi已经关闭 */
			                case WifiManager.WIFI_STATE_DISABLED:
			                	textView3.setText(
			                    getResources().getText(R.string.str_start_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_disabled));
			                	WIFI_VALUE=0;
			                  break;
			                /* 无法取得或辨识WiFi状态 */
			                case WifiManager.WIFI_STATE_UNKNOWN:
			                default:
			                	textView3.setText(
			                    getResources().getText(R.string.str_start_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_unknow));
			                	WIFI_VALUE=0;
			                  break;
			              }
			            }
			            button03.setText(R.string.closewifi);
			          }
					catch(Exception e)
					{
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	
}
