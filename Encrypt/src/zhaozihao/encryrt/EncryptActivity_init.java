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
		button01.setText("��������ģʽ");
		button02.setText("����wifiģʽ");
		button03=(Button)this.findViewById(R.id.openwifi);
		text1=(TextView)this.findViewById(R.id.into2G);
		text2=(TextView)this.findViewById(R.id.intowifi);
		textView1=(TextView)this.findViewById(R.id.textView1);
		textView2=(TextView)this.findViewById(R.id.textView2);
		textView3=(TextView)this.findViewById(R.id.textView3);
		button01.setOnClickListener(new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//ת������ģʽ
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
		//�ж�wifi�Ƿ�Ϊ�Ѵ򿪻����ڴ���
		if(wifimanager.isWifiEnabled())
		{
			//�ж�wifi�Ƿ��Ѿ��򿪡�
			if(wifimanager.getWifiState()==wifimanager.WIFI_STATE_ENABLED)
			{
				//��wifi�Ѿ���
				button03.setText(R.string.closewifi);
				WIFI_VALUE=1;
			}
			//���ڴ���
			else
			{
				button03.setText(R.string.openwifi);
				WIFI_VALUE=0;
			}
		}
		//wifiΪ�رջ�ر���
		else
		{
			button03.setText(R.string.openwifi);
			WIFI_VALUE=0;
		}
		
		button03.setOnClickListener(new OnClickListener(){

			public void onClick(View v) {
				// TODO Auto-generated method stub
				//wifi�Ѵ�ʱ
				if(WIFI_VALUE==1)
				{
					try
					{
						/* �ж�WiFi״̬�Ƿ�Ϊ������ */
			            if(wifimanager.isWifiEnabled() )
			            {
			              /* �ر�WiFi */
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
			              /* WiFi״̬��Ϊ������״̬ʱ */
			              switch(wifimanager.getWifiState())
			              {
			                /* WiFi�������������У������޷��ر�... */
			                case WifiManager.WIFI_STATE_ENABLING:
			                	textView3.setText(getResources().getText(R.string.str_stop_wifi_failed)
			                			+":"+getResources().getText(R.string.str_wifi_enabling));
			                	WIFI_VALUE=1;
			                  break;
			                /* WiFi���ڹرչ����У������޷��ر�... */
			                case WifiManager.WIFI_STATE_DISABLING:
			                	textView3.setText(
			                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_disabling));
			                	WIFI_VALUE=0;
			                  break;
			                /* WiFi�Ѿ��ر� */
			                case WifiManager.WIFI_STATE_DISABLED:
			                	textView3.setText(
			                    getResources().getText(R.string.str_stop_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_disabled));
			                	WIFI_VALUE=0;
			                  break;
			                /* �޷�ȡ�û��ʶWiFi״̬ */
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
				//wifi�����Ѵ�״̬
				else if(WIFI_VALUE==0)
				{
					try
			          {
			            /* ȷ��WiFi�����ǹر��Ҳ������������� */
			            if(!wifimanager.isWifiEnabled() && wifimanager.getWifiState()!=WifiManager.WIFI_STATE_ENABLING )
			            {
			              if(wifimanager.setWifiEnabled(true))
			              {
			                switch(wifimanager.getWifiState())
			                {
			                  /* WiFi���������������У������޷�����... */
			                  case WifiManager.WIFI_STATE_ENABLING:
			                	  textView3.setText(getResources().getText(R.string.str_wifi_enabling));
			                	  WIFI_VALUE=1;	
			                	  
			                	  textView3.setText(getResources().getText(R.string.str_start_wifi_done));
			                    break;
			                  /* WiFi�Ѿ�Ϊ�������޷��ٴ�����... */
			                  case WifiManager.WIFI_STATE_ENABLED:
			                	  textView3.setText(getResources().getText(R.string.str_start_wifi_done));
			                	  WIFI_VALUE=1;
			                    break;
			                  /* ����δ֪�Ĵ��� */
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
			                /* WiFi�������������У������޷�����... */
			                case WifiManager.WIFI_STATE_ENABLING:
			                  textView3.setText(
			                    getResources().getText(R.string.str_start_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_enabling));
			                  WIFI_VALUE=1;
			                  
			                  textView3.setText(getResources().getText(R.string.str_start_wifi_done));
			                  break;
			                /* WiFi���ڹرչ����У������޷�����... */
			                case WifiManager.WIFI_STATE_DISABLING:
			                	textView3.setText(
			                    getResources().getText(R.string.str_start_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_disabling));
			                	WIFI_VALUE=0;
			                  break;
			                /* WiFi�Ѿ��ر� */
			                case WifiManager.WIFI_STATE_DISABLED:
			                	textView3.setText(
			                    getResources().getText(R.string.str_start_wifi_failed)+":"+
			                    getResources().getText(R.string.str_wifi_disabled));
			                	WIFI_VALUE=0;
			                  break;
			                /* �޷�ȡ�û��ʶWiFi״̬ */
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
