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
 * ��	 ״̬2��
��ˮ��	 ״̬2+n��(n����ɵ��¶ȸ���)	 �¶�30~98
�յ�	 ״̬2+n��	 		 �¶�16~30
����	 ״̬2��
��	 ״̬2��

���Ϳ�����Ϣ����
#define MSG_INVALID		0		// ��Ч��Ϣ
#define DOOR_OPEN		1		// �ŵĿ���
#define DOOR_CLOSE		2
#define CURTAIN_OPEN		3		// �����Ŀ���
#define CURTAIN_CLOSE		4		
#define HEATER_ON		5		// ��ˮ���Ŀ���
#define HEATER_OFF		6		
#define AIR_ON			7		// �յ��Ŀ���
#define AIR_OFF			8
#define LIGHT_ON		9		// �ƵĿ���
#define LIGHT_OFF		10
#define SENDBACK		11		// Ҫ�󷵻�����״̬����

����״̬��������
��������Ҫ��������״̬���������SENDBACK�������������ĳһ���������ŵ�״̬������65,
���͵���1���ֽڵĿ�����Ϣ��65��0100,0001b������DOOR_OPEN�ֽ��е�D6��Ϊ1��
ͬ����ѯ����״̬������67����0100,0011b��b����ʹ�õ��Ƕ����Ʊ�ʾ��ʵ�ʷ��͵�ʱ��û��b��
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
	//��״̬
	public boolean door=false;
	public boolean curtain=false;
	public boolean heater=false;
	public boolean aircondition=false;
	public boolean light=false;
	//��״̬
	public boolean door1=false;
	public boolean curtain1=false;
	public boolean heater1=false;
	public boolean aircondition1=false;
	public boolean light1=false;
	//��ѯ״̬
	public boolean door2=false;
	public boolean curtain2=false;
	public boolean heater2=false;
	public boolean aircondition2=false;
	public boolean light2=false;
	public boolean food = false;
	//�ȴ�״̬
	public boolean door3=false;
	public boolean curtain3=false;
	public boolean heater3=false;
	public boolean aircondition3=false;
	public boolean light3=false;
	public boolean food1=false;
	//���״̬�����ֻ�
	public boolean check=false;
	//�¶ȿ���
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
		textArea.setText("�ȴ����������߳�......\n");
		close.setVisible(false);
		
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(IPAD.getText().equals("")||Port.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null, "������IP��ַ�Ͷ˿ں�!", "������IP��ַ�Ͷ˿ں�!", JOptionPane.ERROR_MESSAGE);
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
//					JOptionPane.showMessageDialog(null, "������IP��ַ�Ͷ˿ں�!", "������IP��ַ�Ͷ˿ں�!", JOptionPane.ERROR_MESSAGE);
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
						textArea.append("���������̣߳��ȴ��ֻ�����ָ��...\n");
						serverSocket = new ServerSocket(6666);//��ͨ6666�˿�
						Socket socket = serverSocket.accept();
						textArea.append("�ɹ����ֻ�������socket���˿ں�Ϊ6666\n");
						addrMP = socket.getInetAddress().getHostAddress();
						portMP = socket.getPort();
						//��Socket���еõ�InputStream����
						din = new DataInputStream(socket.getInputStream());
			   			dout = new DataOutputStream(socket.getOutputStream());
			   			String str = din.readUTF();
			   			textArea.append(str+"\n");
			   			if(str.startsWith("<#door#>"))
			   			{
			   				str = str.substring(8);
			   				if(str.equals("1")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ�����ţ�\n");
			   					door = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ�����ţ�\n");
			   					door1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ����ѯ�ŵ�״̬��\n");
			   					door2 = true;
			   					while(!door3);
			   					door3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open");
				   				}
				   				else dout.writeUTF("close");
			   				}
			   				
				   			textArea.append("���ֻ��˷�����Ϣ���ɹ�\n");
							
			   			}
			   			
			   			if(str.startsWith("<#curtain#>"))
			   			{
			   				str = str.substring(11);
			   				if(str.equals("1")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ����������\n");
			   					curtain = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ���ش�����\n");
			   					curtain1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ����ѯ������״̬��\n");
			   					curtain2 = true;
			   					while(!curtain3);
			   					curtain3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open");
				   				}
				   				else dout.writeUTF("close");
			   				}
			   				
				   			textArea.append("���ֻ��˷�����Ϣ���ɹ�\n");
							
			   			}
			   			
			   			if(str.startsWith("<#heater#>"))
			   			{
			   				str = str.substring(10);
			   				if(str.startsWith("1"))
//			   				if(str.equals("1")) 
			   				{
			   					Heater = str.substring(1);
			   					textArea.append("�ֻ��˴�����Ϣ������ˮ����"+"ˮ��Ϊ"+Heater+"��\n");
			   					heater = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ������ˮ����\n");
			   					heater1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ����ѯ��ˮ����״̬��\n");
			   					heater2 = true;
			   					while(!heater3);
			   					heater3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open"+Heater);
				   				}
				   				else dout.writeUTF("close");
			   				}
			   				
				   			textArea.append("���ֻ��˷�����Ϣ���ɹ�\n");
							
			   			}
			   			
			   			if(str.startsWith("<#aircondition#>"))
			   			{
			   				str = str.substring(16);
			   				if(str.startsWith("1"))
//			   				if(str.equals("1")) 
			   				{
			   					Air = str.substring(1);
			   					textArea.append("�ֻ��˴�����Ϣ�����յ���"+"�¶�Ϊ"+Air+"��\n");
			   					aircondition = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ���ؿյ���\n");
			   					aircondition1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ����ѯ�յ���״̬��\n");
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
			   				
				   			textArea.append("���ֻ��˷�����Ϣ���ɹ�\n");
							
			   			}
			   			
			   			if(str.startsWith("<#light#>"))
			   			{
			   				str = str.substring(9);
	//			   			String [] number = str.split("\\|"); 
	//			   			System.out.println(number[0]);
	//			   			System.out.println(number[1]);
			   				if(str.equals("1")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ�����ƣ�\n");
			   					light = true;
			   				}
			   				else if(str.equals("0")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ���صƣ�\n");
			   					light1 = true;
			   				}
			   				else if(str.equals("2")) 
			   				{
			   					textArea.append("�ֻ��˴�����Ϣ����ѯ�Ƶ�״̬��\n");
			   					light2 = true;
			   					while(!light3);
			   					light3=false;
			   					if(check)
				   				{
				   					dout.writeUTF("open");
				   				}
				   				else dout.writeUTF("close");
			   				}
			   				
				   			textArea.append("���ֻ��˷�����Ϣ���ɹ�\n");
							
			   			}
			   			
			   			if(str.startsWith("<#food#>"))
			   			{
			   				textArea.append("�ֻ��˴�����Ϣ����ѯʳ���б�\n");
			   				food = true;
			   				while(!food1);
			   				food1=false;
			   				int m=0;int br=0;int be=0;
//		   					mListTitle[i] = 10000ţ�� or 10001��� or 10002ţ��;
//		   					mListStr[i] = "��ʣ��" or ��ȱ�١�;
			   				for(int i=0;i<length;i++)
			   				{
			   					
			   					if(mListStr[i].equals("��ʣ��"))
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
			   				mListStr2[0]="ʣ����Ϊ:"+m;
			   				mListStr2[1]="ʣ����Ϊ:"+br;
			   				mListStr2[2]="ʣ����Ϊ:"+be;
			   				dout.writeInt(3);
			   				String mListTitle2[]= {"10000","10001","10002"};
			   				for(int i=0;i<3;i++){
			   					dout.writeUTF(mListTitle2[i]);
			   					textArea.append("���ֻ����䣺"+mListTitle2[i]+"\n");
			   				}
			   				for(int i=0;i<3;i++){
			   					dout.writeUTF(mListStr2[i]);
			   					textArea.append("���ֻ����䣺"+mListStr2[i]+"\n");
			   				}
			   				/**
			   				dout.writeInt(length);
			   				for(int i=0;i<length;i++){
			   					dout.writeUTF(mListTitle[i]);
			   					textArea.append("���ֻ����䣺"+mListTitle[i]+"\n");
			   				}
			   				for(int i=0;i<length;i++){
			   					dout.writeUTF(mListStr[i]);
			   					textArea.append("���ֻ����䣺"+mListStr[i]+"\n");
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
							textArea.append("�����ֻ���socket");
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
						textArea.append("��������PC�߳�...\n");
						serverSocketPC = new ServerSocket(8888);//��ͨ8888�˿�
						Socket socket4PC = serverSocketPC.accept();
						textArea.append("�ɹ���PC������socket���˿ں�Ϊ8888\n");
						//�ɹ�����MP��socket֮������PC��socket
						DataInputStream dinPC = new DataInputStream(socket4PC.getInputStream());
			   			DataOutputStream doutPC = new DataOutputStream(socket4PC.getOutputStream());
						textArea.append("�ɹ���PC������socket����\n");
						//��Socket���еõ�InputStream����
						dinPC = new DataInputStream(socket4PC.getInputStream());
			   			doutPC = new DataOutputStream(socket4PC.getOutputStream());
			   			while(monitorMP)
			   			{
			   				if(door)
				   			{
				   				doutPC.write(1);
			   					textArea.append("��PC�˷�����Ϣ��1�����ţ�\n");
			   					door = false;
				   			}
				   			if(door1)
				   			{
				   				doutPC.write(2);
			   					textArea.append("��PC�˷�����Ϣ��2�����ţ�\n");
			   					door1 = false;
				   			}
				   			if(door2)
				   			{
				   				doutPC.write(65);
			   					textArea.append("��PC�˷�����Ϣ��65���鿴�ŵ�״̬��\n");
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
			   					textArea.append("��PC�˷�����Ϣ��3����������\n");
			   					curtain = false;
				   			}
				   			if(curtain1)
				   			{
				   				doutPC.write(4);
			   					textArea.append("��PC�˷�����Ϣ��4���ش�����\n");
			   					curtain1 = false;
				   			}
				   			if(curtain2)
				   			{
				   				doutPC.write(67);
			   					textArea.append("��PC�˷�����Ϣ��67���鿴������״̬��\n");
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
			   					textArea.append("��PC�˷�����Ϣ��5������ˮ����"+"�¶�Ϊ��"+b+"\n");
			   					heater = false;
				   			}
				   			if(heater1)
				   			{
				   				doutPC.write(6);
			   					textArea.append("��PC�˷�����Ϣ��6������ˮ����\n");
			   					heater1 = false;
				   			}
				   			if(heater2)
				   			{
				   				doutPC.write(69);
			   					textArea.append("��PC�˷�����Ϣ��69���鿴��ˮ����״̬��\n");
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
			   					textArea.append("��PC�˷�����Ϣ��7�����յ���"+"�¶�Ϊ��"+a+"\n");
			   					aircondition = false;
				   			}
				   			if(aircondition1)
				   			{
				   				doutPC.write(8);
			   					textArea.append("��PC�˷�����Ϣ��8���ؿյ���\n");
			   					aircondition1 = false;
				   			}
				   			if(aircondition2)
				   			{
				   				doutPC.write(71);
			   					textArea.append("��PC�˷�����Ϣ��71���鿴�յ���״̬��\n");
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
			   					textArea.append("��PC�˷�����Ϣ��9�����ƣ�\n");
			   					light = false;
				   			}
				   			if(light1)
				   			{
				   				doutPC.write(10);
			   					textArea.append("��PC�˷�����Ϣ��10���صƣ�\n");
			   					light1 = false;
				   			}
				   			if(light2)
				   			{
				   				doutPC.write(73);
			   					textArea.append("��PC�˷�����Ϣ��73���鿴�Ƶ�״̬��\n");
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
				   				textArea.append("��PC�˷�����Ϣ��12���鿴ʳ���б�\n");
				   				//����int�͵��ĸ��ֽڵ����֣�����Ƭ���� ��ʼֵ7��
				   				byte[] byte6 = new byte[5];
				   				byte[] byte7 = new byte[4];
				   				dinPC.read(byte6);
//				   				dinPC.read();
//				   				length = dinPC.read(byte6);
//				   				textArea.append("�յ�������"+length+"\n");
				   				byte7[0]=byte6[3];
				   				byte7[2]=byte6[1];
				   				byte7[1]=byte6[2];
				   				byte7[3]=byte6[0];
				   				for (int i = 0; i < 4; i++) 
				   				{            
				   					length += (byte7[i] & 0xFF) << (8 * (3 - i));            
				   				}
				   				textArea.append("ʳ��������Ϊ"+length+"\n");
				   				doutPC.write(13);
				   				textArea.append("��PC�˷�����Ϣ��13��ȷ���յ���������\n");
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
				   						mListStr[i] = "��ʣ��";
				   						textArea.append("��ʣ��"+"  ");
				   					}
				   					else
				   					{
				   						mListStr[i] = "ȱ��";
				   						textArea.append("ȱ��"+"  ");
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
				   						mListStr[i] = "��ʣ��";
				   						textArea.append("��ʣ��"+"  \n");
				   					}
				   					else
				   					{
				   						mListStr[i] = "ȱ��";
				   						textArea.append("ȱ��"+"  \n");
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
							textArea.append("����PC��socket");
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


