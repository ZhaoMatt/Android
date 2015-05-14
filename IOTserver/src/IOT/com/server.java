package IOT.com;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.text.View;
import javax.swing.JTextField;
import javax.swing.JLabel;
import com.jgoodies.forms.factories.DefaultComponentFactory;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JButton;
import javax.swing.JTextPane;
import java.awt.TextField;
import java.awt.Button;
import javax.swing.JEditorPane;


import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

/**
 * 门	 状态2种
热水器	 状态2+n种(n代表可调温度个数)	 温度30~98
空调	 状态2+n种	 		 温度16~30
窗帘	 状态2种
灯	 状态2种

发送控制消息定义
#define MSG_INVALID		0		// 无效消息
#define DOOR_OPEN		1		// 门的开关
#define DOOR_CLOSE		2
#define CURTAIN_OPEN		3		// 窗帘的开关
#define CURTAIN_CLOSE		4		
#define HEATER_ON		5		// 热水器的开关
#define HEATER_OFF		6		
#define AIR_ON			7		// 空调的开关
#define AIR_OFF			8
#define LIGHT_ON		9		// 灯的开关
#define LIGHT_OFF		10
#define SENDBACK		11		// 要求返回所有状态数据

发送状态返回请求
例：如需要发送所有状态，用上面的SENDBACK，如果单独返回某一个，比如门的状态，则发送65,
发送的是1个字节的控制信息，65是0100,0001b，即将DOOR_OPEN字节中的D6置为1，
同理，查询窗帘状态，则发送67，即0100,0011b（b代表使用的是二进制表示，实际发送的时候没有b）
 * @author ZhaoZihao
 *
 */

public class server extends JFrame {
	private JTextField txtIp;
	private JTextField textField_1;
	private JTextArea textArea;
	private JEditorPane IPAD,Port;
	public Button start,close;
	public boolean monitorMP=true;
	public boolean monitorPC=true;
	public boolean flagMP = true;
	public int length;
	public String[] mListTitle = new String[10];
	public String[] mListStr = new String[10];
	public String[] mListTitle2 = new String[3];
	public String[] mListStr2 = new String[3];
	//开状态
	public boolean door=false;
	public boolean curtain=false;
	public boolean heater=false;
	public boolean aircondition=false;
	public boolean light=false;
	//关状态
	public boolean door1=false;
	public boolean curtain1=false;
	public boolean heater1=false;
	public boolean aircondition1=false;
	public boolean light1=false;
	//查询状态
	public boolean door2=false;
	public boolean curtain2=false;
	public boolean heater2=false;
	public boolean aircondition2=false;
	public boolean light2=false;
	public boolean food = false;
	//等待状态
	public boolean door3=false;
	public boolean curtain3=false;
	public boolean heater3=false;
	public boolean aircondition3=false;
	public boolean light3=false;
	public boolean food1=false;
	//检查状态返回手机
	public boolean check=false;
	//温度控制
	public String Heater = null;
	public String Air = null;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					server frame = new server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public server() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 476, 345);
		getContentPane().setLayout(null);
		
		txtIp = new JTextField();
		txtIp.setEditable(false);
		txtIp.setText("IP\u5730\u5740");
		txtIp.setBounds(8, 238, 66, 21);
		getContentPane().add(txtIp);
		txtIp.setColumns(10);
		
		IPAD = new JEditorPane();
		IPAD.setBounds(91, 238, 182, 21);
		getContentPane().add(IPAD);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setText("\u7AEF\u53E3");
		textField_1.setBounds(8, 269, 66, 21);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		Port = new JEditorPane();
		Port.setBounds(91, 269, 106, 21);
		getContentPane().add(Port);
		
		start = new Button("\u5F00\u59CB\u76D1\u542C");
		start.setActionCommand("");
		start.setBounds(292, 238, 76, 23);
		getContentPane().add(start);
		
		close = new Button("\u7ED3\u675F\u76D1\u542C");
		close.setBounds(374, 236, 76, 23);
		getContentPane().add(close);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 10, 440, 218);
		getContentPane().add(scrollPane);
		
		textArea = new JTextArea(){
			   public void append(String str) {
			    super.append(str);
			    this.setCaretPosition(getDocument().getLength());
			   }
			 };
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setText("等待开启监听线程......\n");
		close.setVisible(false);
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IPAD.getText().equals("")||Port.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "请输入IP地址和端口号!", "请输入IP地址和端口号!", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					start.setVisible(false);
					close.setVisible(true);
					flagMP = true;
					new Thread(new Server4MP()).start();
					flagMP = true;
					new Thread(new Server4PC()).start();
				}
				
			}
		});
		
//		btnpc.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//				if(IPAD.getText().equals("")||Port.getText().equals(""))
//				{
//					JOptionPane.showMessageDialog(null, "请输入IP地址和端口号!", "请输入IP地址和端口号!", JOptionPane.ERROR_MESSAGE);
//				}
//				else
//				{
//					start.setVisible(false);
//					btnpc.setVisible(false);
//					close.setVisible(true);
//					flagMP = true;
//					new Thread(new Server4PC()).start();
//				}
//			}
//		});
		
		/*
		close.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				close.setVisible(false);
				start.setVisible(true);
				flagMP = false;
				
			}
		});
		*/
	}
	
	

	public class Server4MP implements Runnable  {
		
//		public static final String ServerIP= "192.168.1.10X"; 
		public String addrMP = null;
		public int portMP ;
		
		

		@Override
		public void run() {
			// TODO Auto-generated method stub

			ServerSocket serverSocket = null;
			DataInputStream din = null;
			DataOutputStream dout = null;
			
			
			while(monitorMP)
			{
				if(flagMP)
				{
					try{
						textArea.append("*************************************\n");
						textArea.append("开启监听线程，等待手机传输指令...\n");
						serverSocket = new ServerSocket(6666);//开通6666端口
						Socket socket = serverSocket.accept();
						textArea.append("成功和手机建立了socket，端口号为6666\n");
						addrMP = socket.getInetAddress().getHostAddress();
						portMP = socket.getPort();
						//从Socket当中得到InputStream对象
						din = new DataInputStream(socket.getInputStream());
			   			dout = new DataOutputStream(socket.getOutputStream());
			   			String str = din.readUTF();
			   			textArea.append(str+"\n");
			   			if(str.startsWith("<#door#>"))
			   			{
			   				str = str.substring(8);
			   				if(str.equals("1")) 
			   				{
			   					textArea.append("手机端传来信息：开门！\n");
			   					door = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("手机端传来信息：关门！\n");
			   					door1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("手机端传来信息：查询门的状态！\n");
			   					door2 = true;
			   					while(!door3);
			   					door3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open");
				   				}
				   				else dout.writeUTF("close");
			   				}
			   				
				   			textArea.append("向手机端发送消息：成功\n");
							
			   			}
			   			
			   			if(str.startsWith("<#curtain#>"))
			   			{
			   				str = str.substring(11);
			   				if(str.equals("1")) 
			   				{
			   					textArea.append("手机端传来信息：开窗帘！\n");
			   					curtain = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("手机端传来信息：关窗帘！\n");
			   					curtain1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("手机端传来信息：查询窗帘的状态！\n");
			   					curtain2 = true;
			   					while(!curtain3);
			   					curtain3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open");
				   				}
				   				else dout.writeUTF("close");
			   				}
			   				
				   			textArea.append("向手机端发送消息：成功\n");
							
			   			}
			   			
			   			if(str.startsWith("<#heater#>"))
			   			{
			   				str = str.substring(10);
			   				if(str.startsWith("1"))
//			   				if(str.equals("1")) 
			   				{
			   					Heater = str.substring(1);
			   					textArea.append("手机端传来信息：开热水器！"+"水温为"+Heater+"度\n");
			   					heater = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("手机端传来信息：关热水器！\n");
			   					heater1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("手机端传来信息：查询热水器的状态！\n");
			   					heater2 = true;
			   					while(!heater3);
			   					heater3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open"+Heater);
				   				}
				   				else dout.writeUTF("close");
			   				}
			   				
				   			textArea.append("向手机端发送消息：成功\n");
							
			   			}
			   			
			   			if(str.startsWith("<#aircondition#>"))
			   			{
			   				str = str.substring(16);
			   				if(str.startsWith("1"))
//			   				if(str.equals("1")) 
			   				{
			   					Air = str.substring(1);
			   					textArea.append("手机端传来信息：开空调！"+"温度为"+Air+"度\n");
			   					aircondition = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("手机端传来信息：关空调！\n");
			   					aircondition1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("手机端传来信息：查询空调的状态！\n");
			   					aircondition2 = true;
			   					while(!aircondition3);
			   					aircondition3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open"+Air);
				   					textArea.append("open\n");
				   				}
				   				else 
				   				{
				   					dout.writeUTF("close");
				   					textArea.append("close\n");
				   				}
			   				}
			   				
				   			textArea.append("向手机端发送消息：成功\n");
							
			   			}
			   			
			   			if(str.startsWith("<#light#>"))
			   			{
			   				str = str.substring(9);
	//			   			String [] number = str.split("\\|"); 
	//			   			System.out.println(number[0]);
	//			   			System.out.println(number[1]);
			   				if(str.equals("1")) 
			   				{
			   					textArea.append("手机端传来信息：开灯！\n");
			   					light = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("手机端传来信息：关灯！\n");
			   					light1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("手机端传来信息：查询灯的状态！\n");
			   					light2 = true;
			   					while(!light3);
			   					light3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open");
				   				}
				   				else dout.writeUTF("close");
			   				}
			   				
				   			textArea.append("向手机端发送消息：成功\n");
							
			   			}
			   			
			   			if(str.startsWith("<#food#>"))
			   			{
			   				textArea.append("手机端传来信息：查询食物列表！\n");
			   				food = true;
			   				while(!food1);
			   				food1=false;
			   				int m=0;int br=0;int be=0;
//		   					mListTitle[i] = 10000牛奶 or 10001面包 or 10002牛肉;
//		   					mListStr[i] = "有剩余" or “缺少”;
			   				for(int i=0;i<length;i++)
			   				{
			   					
			   					if(mListStr[i].equals("有剩余"))
			   					{
			   						if(mListTitle[i].equals("10000"))
			   						{
			   							m++;
			   						}
			   						else if(mListTitle[i].equals("10001"))
			   						{
			   							br++;
			   						}
			   						else if(mListTitle[i].equals("10002"))
			   						{
			   							be++;
			   						}
			   					}

			   				}
			   				mListStr2[0]="剩余量为:"+m;
			   				mListStr2[1]="剩余量为:"+br;
			   				mListStr2[2]="剩余量为:"+be;
			   				dout.writeInt(3);
			   				String mListTitle2[]= {"10000","10001","10002"};
			   				for(int i=0;i<3;i++){
			   					dout.writeUTF(mListTitle2[i]);
			   					textArea.append("向手机传输："+mListTitle2[i]+"\n");
			   				}
			   				for(int i=0;i<3;i++){
			   					dout.writeUTF(mListStr2[i]);
			   					textArea.append("向手机传输："+mListStr2[i]+"\n");
			   				}
			   				/**
			   				dout.writeInt(length);
			   				for(int i=0;i<length;i++){
			   					dout.writeUTF(mListTitle[i]);
			   					textArea.append("向手机传输："+mListTitle[i]+"\n");
			   				}
			   				for(int i=0;i<length;i++){
			   					dout.writeUTF(mListStr[i]);
			   					textArea.append("向手机传输："+mListStr[i]+"\n");
			   				}
			   				*/
			   			}
						
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					finally{
						try {
							serverSocket.close();
							textArea.append("结束手机端socket");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else
				{
					break;
				}
				
			}
			
		}

	}
	
	public class Server4PC implements Runnable  {
		
//		public static final String ServerIP= "192.168.1.10X"; 
		public String addrMP = null;
		public int portMP ;
		
		

		@Override
		public void run() {
			// TODO Auto-generated method stub

			ServerSocket serverSocketPC = null;
			while(monitorMP)
			{
				if(flagMP)
				{
					try{
						textArea.append("##################################\n");
						textArea.append("开启监听PC线程...\n");
						serverSocketPC = new ServerSocket(8888);//开通8888端口
						Socket socket4PC = serverSocketPC.accept();
						textArea.append("成功和PC建立了socket，端口号为8888\n");
						//成功接受MP的socket之后建立和PC的socket
						DataInputStream dinPC = new DataInputStream(socket4PC.getInputStream());
			   			DataOutputStream doutPC = new DataOutputStream(socket4PC.getOutputStream());
						textArea.append("成功和PC建立了socket连接\n");
						//从Socket当中得到InputStream对象
						dinPC = new DataInputStream(socket4PC.getInputStream());
			   			doutPC = new DataOutputStream(socket4PC.getOutputStream());
			   			while(monitorMP)
			   			{
			   				if(door)
				   			{
				   				doutPC.write(1);
			   					textArea.append("向PC端发送消息：1（开门）\n");
			   					door = false;
				   			}
				   			if(door1)
				   			{
				   				doutPC.write(2);
			   					textArea.append("向PC端发送消息：2（关门）\n");
			   					door1 = false;
				   			}
				   			if(door2)
				   			{
				   				doutPC.write(65);
			   					textArea.append("向PC端发送消息：65（查看门的状态）\n");
			   					door2 = false;
			   					Byte byte1 = dinPC.readByte();
			   					String str1= byte1.toString();
			   					textArea.append(str1+"\n");
			   					if(str1.equals("1"))
			   					{
			   						check = true;
			   					}
			   					else 
			   					{
			   						check=false;
			   					}
			   					door3=true;
				   			}
				   			if(curtain)
				   			{
				   				doutPC.write(3);
			   					textArea.append("向PC端发送消息：3（开窗帘）\n");
			   					curtain = false;
				   			}
				   			if(curtain1)
				   			{
				   				doutPC.write(4);
			   					textArea.append("向PC端发送消息：4（关窗帘）\n");
			   					curtain1 = false;
				   			}
				   			if(curtain2)
				   			{
				   				doutPC.write(67);
			   					textArea.append("向PC端发送消息：67（查看窗帘的状态）\n");
			   					curtain2 = false;
			   					Byte byte2 = dinPC.readByte();
			   					String str2= byte2.toString();
			   					textArea.append(str2+"\n");
			   					if(str2.equals("3"))
			   					{
			   						check = true;
			   					}
			   					else
			   					{
			   						check=false;
			   					}
			   					curtain3=true;
				   			}
				   			if(heater)
				   			{
				   				byte b = (byte) Integer.parseInt(Heater);
				   				byte[] by = {5,b};
				   				doutPC.write(by);
//				   				doutPC.writeUTF("5"+Heater);
			   					textArea.append("向PC端发送消息：5（开热水器）"+"温度为："+b+"\n");
			   					heater = false;
				   			}
				   			if(heater1)
				   			{
				   				doutPC.write(6);
			   					textArea.append("向PC端发送消息：6（关热水器）\n");
			   					heater1 = false;
				   			}
				   			if(heater2)
				   			{
				   				doutPC.write(69);
			   					textArea.append("向PC端发送消息：69（查看热水器的状态）\n");
			   					heater2 = false;
			   					Byte byte3 = dinPC.readByte();
			   					String str3= byte3.toString();
			   					textArea.append(str3+"\n");
//			   					if(str3.startsWith("5"))
			   					if(str3.equals("5"))
			   					{
			   						check = true;
			   					}
			   					else if(str3.equals("6"))
			   					{
			   						check=false;
			   					}
			   					Byte bh = dinPC.readByte();
			   					Heater= bh.toString();
			   					heater3=true;
				   			}
				   			if(aircondition)
				   			{
				   				byte a = (byte) Integer.parseInt(Air);
				   				byte[] ar = {7,a};
				   				doutPC.write(ar);
			   					textArea.append("向PC端发送消息：7（开空调）"+"温度为："+a+"\n");
			   					aircondition = false;
				   			}
				   			if(aircondition1)
				   			{
				   				doutPC.write(8);
			   					textArea.append("向PC端发送消息：8（关空调）\n");
			   					aircondition1 = false;
				   			}
				   			if(aircondition2)
				   			{
				   				doutPC.write(71);
			   					textArea.append("向PC端发送消息：71（查看空调的状态）\n");
			   					aircondition2 = false;
			   					Byte byte4 = dinPC.readByte();
			   					String str4= byte4.toString();
			   					textArea.append(str4+"\n");
//			   					if(str4.startsWith("7"))
			   					if(str4.equals("7"))
			   					{
			   						check = true;
			   					}
			   					else if (str4.equals("8"))
			   					{
			   						check=false;
			   					}
			   					Byte ba = dinPC.readByte();
			   					Air= ba.toString();
			   					aircondition3=true;
				   			}
				   			if(light)
				   			{
				   				doutPC.write(9);
			   					textArea.append("向PC端发送消息：9（开灯）\n");
			   					light = false;
				   			}
				   			if(light1)
				   			{
				   				doutPC.write(10);
			   					textArea.append("向PC端发送消息：10（关灯）\n");
			   					light1 = false;
				   			}
				   			if(light2)
				   			{
				   				doutPC.write(73);
			   					textArea.append("向PC端发送消息：73（查看灯的状态）\n");
			   					light2 = false;
			   					Byte byte5 = dinPC.readByte();
			   					String str5= byte5.toString();
			   					textArea.append(str5+"\n");
			   					if(str5.equals("9"))
			   					{
			   						check = true;
			   					}
			   					else
			   					{
			   						check=false;
			   					}
			   					light3=true;
				   			}
				   			if(food)
				   			{
				   				doutPC.write(12);
				   				textArea.append("向PC端发送消息：12（查看食物列表）\n");
				   				//接受int型的四个字节的数字（代表卡片数量 初始值7）
				   				byte[] byte6 = new byte[5];
				   				byte[] byte7 = new byte[4];
				   				dinPC.read(byte6);
//				   				dinPC.read();
//				   				length = dinPC.read(byte6);
//				   				textArea.append("收到数字了"+length+"\n");
				   				byte7[0]=byte6[3];
				   				byte7[2]=byte6[1];
				   				byte7[1]=byte6[2];
				   				byte7[3]=byte6[0];
				   				for (int i = 0; i < 4; i++) 
				   				{            
				   					length += (byte7[i] & 0xFF) << (8 * (3 - i));            
				   				}
				   				textArea.append("食物种类数为"+length+"\n");
				   				doutPC.write(13);
				   				textArea.append("向PC端发送消息：13（确认收到种类数）\n");
				   				/**
				   				for(int i=0;i<length;i++){
				   					byte[] byte1 = new byte[50];
				   					dinPC.read(byte1);
				   					byte[] b = new byte[6];
				   					for(int k = 0; k<5;k++){
				   						b[k] = byte1[k];
				   					}
				   					String str = new String(b).trim();
				   					mListTitle[i] = str;
				   					textArea.append(str+"  ");
				   				}
				   				for(int i=0;i<length;i++){
				   					boolean flag = false;
				   					flag = dinPC.readBoolean();
				   					if(flag)
				   					{
				   						mListStr[i] = "有剩余";
				   						textArea.append("有剩余"+"  ");
				   					}
				   					else
				   					{
				   						mListStr[i] = "缺少";
				   						textArea.append("缺少"+"  ");
				   					}
				   				}
				   				*/
				   				for(int i=0;i<length;i++){
				   					
				   					byte[] byte1 = new byte[50];
				   					dinPC.read(byte1);
				   					byte[] b = new byte[6];
				   					for(int k = 0; k<5;k++){
				   						b[k] = byte1[k];
				   					}
				   					String str = new String(b).trim();
				   					mListTitle[i] = str;
				   					textArea.append(str+"  ");
				   					
				   					/**
				   					char[] char1 = new char[25];
				   					byte[] byte1 = new byte[50];
				   					dinPC.read(byte1);
				   					for(int b=0;b<10;b++)
				   					{
				   						System.out.print(byte1[b]+" ");
				   					}
				   					for(int k=0; k<50;k=k+2){
				   						char1[k/2]= (char) (((char)byte1[k+1])<<4 | byte1[k]);
				   					}
				   					String str = new String(char1).trim();
				   					*/
				   					mListTitle[i] = str;
				   					
				   					boolean flag = false;
				   					flag = dinPC.readBoolean();
				   					if(flag)
				   					{
				   						mListStr[i] = "有剩余";
				   						textArea.append("有剩余"+"  \n");
				   					}
				   					else
				   					{
				   						mListStr[i] = "缺少";
				   						textArea.append("缺少"+"  \n");
				   					}
				   					
				   				}
				   				food=false;
				   				food1=true;
				   			}
			   			}
			   			
					}catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					finally{
						try {
							serverSocketPC.close();
							textArea.append("结束PC端socket");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				else
				{
					break;
				}
				
			}
			
		}

	}
	
}


