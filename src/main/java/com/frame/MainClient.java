package com.frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
//import java.util.Locale;
import java.util.Properties;
//import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import client.Http360ClientThread;
import client.HttpDuokuClientThread;
import client.HttpMysticalcardClientThread;
import client.MainClientRunThread;

import bean.ParamsMysticalcard;

public class MainClient extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private JPanel top_jp = null;//����
	private JPanel center_jp = null;//�в�
	private StatusBar statusBar = null;//�ײ�
	
	private JPanel proxy_jp = null;//�������
	private JPanel account_jp = null;//�˺����
	private JPanel control_jp = null;//�������
	private JPanel log_jp = null;//��־���
	private JPanel status_jp = null;//״̬���
	
	private JCheckBox proxy_jcb = null;
	private JLabel proxy_ip_jlb = null;
	private JLabel proxy_port_jlb = null;
	private JTextField proxy_ip_jtf = null;
	private JTextField proxy_port_jtf = null;
	
	private JCheckBox account_360_jcb = null;
	private JCheckBox account_duoku_jcb = null;
	private JCheckBox account_gw_jcb = null;
	private MyComboBox account_360_jcbb = null;
	private MyComboBox account_duoku_jcbb = null;
	private MyComboBox account_gw_jcbb = null;
	private JButton account_config = null;
	
	private JLabel control_runcount_jlb = null;
	private JTextField control_runcount_jtf = null;//�̲߳�������
	private JButton control_gomaze_jbtn = null;//�Թ�
	private JButton control_fail_repeat_jbtn = null;//ʧ�ܶ��д���
	
	private JTextArea log_jta = null;//��־
	
	private List<Http360ClientThread> list360 = null;//360�˺�����
	private List<HttpDuokuClientThread> listduoku = null;//duoku�˺�����
	private List<HttpMysticalcardClientThread> listgw = null;//gw�˺�����

	private LogThread lt = null;//������־����
	private MainClientRunThread mainthread = null;//�������߳�
	
	private static MainClient mc = null;
	
	/**
	 * ���칤��(����)
	 * */
	public static MainClient getInstance(){
		if(mc==null){
			mc = new MainClient();
		}
		return mc;
	}

	/**
	 * ���캯��
	 * */
	private MainClient(){
		//״̬���
		status_jp = new JPanel();
		status_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"״̬���[��ʼ�� ��ɫ,������ ��ɫ,�ɹ� ��ɫ,ʧ�� ��ɫ]",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		status_jp.setFont(Constants.font);
		status_jp.setLayout(new GridLayout(5 ,5 ,5 ,5));
		
		//��־���
		log_jp = new JPanel();
		log_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"��־���",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		log_jp.setLayout(new GridLayout(1, 1));//������JTextAreaռ�����
		log_jta = new JTextArea();
		log_jta.setFont(Constants.font);
		log_jta.setLineWrap(true);//�Զ�����
		log_jta.setWrapStyleWord(true);//���в�����
		log_jta.setEditable(false);//���ܱ༭
		log_jp.add(new JScrollPane(log_jta));
		
		//������־���ӽ���
		this.StartLog();

		//���ü���
		this.loadConfig();
		
		//�������
		proxy_jp = new JPanel();
		proxy_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"��������",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		proxy_jcb = new JCheckBox("�Ƿ�������");
		proxy_jcb.setFont(Constants.font);
		proxy_jcb.setActionCommand("proxy_jcb");
		proxy_jcb.addActionListener(this);
		proxy_ip_jlb = new JLabel("ip��ַ:");
		proxy_ip_jlb.setFont(Constants.font);
		proxy_port_jlb = new JLabel("port�˿�:");
		proxy_port_jlb.setFont(Constants.font);
		proxy_ip_jtf = new JTextField(10);
		proxy_ip_jtf.setText("127.0.0.1");
		proxy_ip_jtf.setFont(Constants.font);
		proxy_port_jtf = new JTextField(10);
		proxy_port_jtf.setText("8087");
		proxy_port_jtf.setFont(Constants.font);
		proxy_jp.add(proxy_jcb);
		proxy_jp.add(proxy_ip_jlb);
		proxy_jp.add(proxy_ip_jtf);
		proxy_jp.add(proxy_port_jlb);
		proxy_jp.add(proxy_port_jtf);
		
		//�˺����
		account_jp = new JPanel();
		account_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"�˺�����",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		account_360_jcb = new JCheckBox("�Ƿ���360", true);
		account_360_jcb.setFont(Constants.font);
		account_duoku_jcb = new JCheckBox("�Ƿ������", true);
		account_duoku_jcb.setFont(Constants.font);
		account_gw_jcb = new JCheckBox("�Ƿ�������", true);
		account_gw_jcb.setFont(Constants.font);
		
		account_360_jcbb = new MyComboBox();
		for(int i=0;i<this.list360.size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(this.list360.get(i).getNick());
			cValue.setBolValue(true);
			cValue.setThread1(this.list360.get(i));
			account_360_jcbb.addItem(cValue);
		}
		account_360_jcbb.setRenderer(new CheckListCellRenderer());
		account_360_jcbb.setFont(Constants.font);
		
		account_duoku_jcbb = new MyComboBox();
		for(int i=0;i<this.listduoku.size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(this.listduoku.get(i).getNick());
			cValue.setBolValue(true);
			cValue.setThread2(this.listduoku.get(i));
			account_duoku_jcbb.addItem(cValue);
		}
		account_duoku_jcbb.setRenderer(new CheckListCellRenderer());
		account_duoku_jcbb.setFont(Constants.font);

		account_gw_jcbb = new MyComboBox();
		for(int i=0;i<this.listgw.size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(this.listgw.get(i).getNick());
			cValue.setBolValue(true);
			cValue.setThread3(this.listgw.get(i));
			account_gw_jcbb.addItem(cValue);
		}
		account_gw_jcbb.setRenderer(new CheckListCellRenderer());
		account_gw_jcbb.setFont(Constants.font);

		account_config = new JButton("�����˺�");
		account_config.setFont(Constants.font);
		account_config.setActionCommand("account_config");
		account_config.addActionListener(this);
		account_jp.add(account_360_jcb);
		account_jp.add(account_360_jcbb);
		account_jp.add(account_gw_jcb);
		account_jp.add(account_gw_jcbb);
		account_jp.add(account_duoku_jcb);
		account_jp.add(account_duoku_jcbb);
		account_jp.add(account_config);
		
		//�������
		control_jp = new JPanel();
		control_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"�������",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		control_runcount_jlb = new JLabel("ͬʱ����������:");
		control_runcount_jlb.setFont(Constants.font);
		control_runcount_jtf = new JTextField(10);
		control_runcount_jtf.setFont(Constants.font);
		control_runcount_jtf.setText("5");
		control_gomaze_jbtn = new JButton("һ���Թ�̽��");
		control_gomaze_jbtn.setFont(Constants.font);
		control_gomaze_jbtn.setActionCommand("control_gomaze_jbtn");
		control_gomaze_jbtn.addActionListener(this);
		control_fail_repeat_jbtn = new JButton("ʧ�ܶ��д���");
		control_fail_repeat_jbtn.setFont(Constants.font);
		control_fail_repeat_jbtn.setActionCommand("control_fail_repeat_jbtn");
		control_fail_repeat_jbtn.addActionListener(this);
		control_jp.add(control_runcount_jlb);
		control_jp.add(control_runcount_jtf);
		control_jp.add(control_gomaze_jbtn);
		control_jp.add(control_fail_repeat_jbtn);
		
		//����
		top_jp = new JPanel();
		top_jp.setLayout(new GridLayout(3, 1));
		top_jp.add(proxy_jp);
		top_jp.add(account_jp);
		top_jp.add(control_jp);
		
		//�в�
		center_jp = new JPanel();
		center_jp.setLayout(new GridLayout(1, 2));
		center_jp.add(log_jp);
		center_jp.add(status_jp);
		
		//�ײ� ״̬��
		statusBar = new StatusBar();
		
		//��������
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(top_jp, BorderLayout.NORTH);
		c.add(center_jp, BorderLayout.CENTER);
		c.add(statusBar, BorderLayout.SOUTH);
		
		//�ı�״̬
		this.changeJFType(true, 0);
		
		//��������
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();//�����ʾ����С����
		this.setTitle("ħ�����븨������");
		this.setBounds((displaySize.width-900)/2, (displaySize.height-600)/2, 900, 600);
		this.setResizable(false);
		this.setIconImage(this.getToolkit().getImage(Constants.pngPath));
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�ر��˳�jvm
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//�رվ�dispose
		this.setVisible(true);
	}
	
	/**
	 * ���������ļ�
	 * */
	public void loadConfig(){
		status_jp.removeAll();//״̬����Ƴ��������
		try{
			Constants constants = Constants.getInstance();//��ʼ����̬������
			constants.loadConfig();//��������
			//360 config			
			list360 = new Vector<Http360ClientThread>();
			Vector<ParamsMysticalcard> v360 = constants.getList360();
			for(int j=0;j<v360.size();j++){
				Http360ClientThread thread360 = new Http360ClientThread(v360.get(j));
				list360.add(thread360);
				status_jp.add(thread360.getMonitor());
				thread360.getMonitor().setLog(this.lt);//���ü����־�߳�,Ϊ�˵����ʾ��־
				thread360.getMonitor().setMainClient(this);//����������
				status_jp.revalidate();//ˢ��
			}
			
			//duoku config
			listduoku = new Vector<HttpDuokuClientThread>();
			Vector<ParamsMysticalcard> vduoku = constants.getListduoku();
			for(int j=0;j<vduoku.size();j++){
				HttpDuokuClientThread threadDuoku = new HttpDuokuClientThread(vduoku.get(j));
				listduoku.add(threadDuoku);
				status_jp.add(threadDuoku.getMonitor());
				threadDuoku.getMonitor().setLog(this.lt);//���ü����־�߳�,Ϊ�˵����ʾ��־
				threadDuoku.getMonitor().setMainClient(this);//����������
				status_jp.revalidate();//ˢ��
			}
			
			//mysticalcard config
			listgw = new Vector<HttpMysticalcardClientThread>();
			Vector<ParamsMysticalcard> vgw = constants.getListgw();
			for(int j=0;j<vgw.size();j++){
				HttpMysticalcardClientThread threadMysticalcard = new HttpMysticalcardClientThread(vgw.get(j));
				listgw.add(threadMysticalcard);
				status_jp.add(threadMysticalcard.getMonitor());
				threadMysticalcard.getMonitor().setLog(this.lt);//���ü����־�߳�,Ϊ�˵����ʾ��־
				threadMysticalcard.getMonitor().setMainClient(this);//����������
				status_jp.revalidate();//ˢ��
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * ��������
	 * */
	public void actionPerformed(ActionEvent e) {
		String acommand = e.getActionCommand();
		if(acommand.equals("control_gomaze_jbtn")){//һ���Թ�̽��
			//����ʧ��
			for(int i=0;i<list360.size();i++)
				list360.get(i).repeated();
			for(int i=0;i<listduoku.size();i++)
				listduoku.get(i).repeated();
			for(int i=0;i<listgw.size();i++)
				listgw.get(i).repeated();
			
			//���ܵ������б�
			Vector<Runnable> waitlist = new Vector<Runnable>();
			if(account_360_jcb.isSelected() && list360!=null){//���360��ѡ��
				List<Http360ClientThread> list360_run = new Vector<Http360ClientThread>();//360�����˺�����
				for(int i=1;i<account_360_jcbb.getItemCount();i++){					
					if(((MyCheckBox)account_360_jcbb.getItemAt(i)).isBolValue()
							&& ((MyCheckBox)account_360_jcbb.getItemAt(i)).getThread1()!=null)//��ѡ��
						list360_run.add(((MyCheckBox)account_360_jcbb.getItemAt(i)).getThread1());
				}
				waitlist.addAll(list360_run);
			}
			if(account_duoku_jcb.isSelected() && listduoku!=null){//�����ᱻѡ��
				List<HttpDuokuClientThread> listduoku_run = new Vector<HttpDuokuClientThread>();//duoku�����˺�����
				for(int i=1;i<account_duoku_jcbb.getItemCount();i++){
					if(((MyCheckBox)account_duoku_jcbb.getItemAt(i)).isBolValue()
							&& ((MyCheckBox)account_duoku_jcbb.getItemAt(i)).getThread2()!=null)//��ѡ��
						listduoku_run.add(((MyCheckBox)account_duoku_jcbb.getItemAt(i)).getThread2());
				}				
				waitlist.addAll(listduoku_run);
			}
			if(account_gw_jcb.isSelected() && listgw!=null){//���������ѡ��
				List<HttpMysticalcardClientThread> listgw_run = new Vector<HttpMysticalcardClientThread>();//gw�����˺�����
				for(int i=1;i<account_gw_jcbb.getItemCount();i++){
					if(((MyCheckBox)account_gw_jcbb.getItemAt(i)).isBolValue()
							&& ((MyCheckBox)account_gw_jcbb.getItemAt(i)).getThread3()!=null)//��ѡ��
						listgw_run.add(((MyCheckBox)account_gw_jcbb.getItemAt(i)).getThread3());
				}				
				waitlist.addAll(listgw_run);
			}
			
			//�������߳�
			if(waitlist!=null && waitlist.size()>0){
				mainthread = null;
				mainthread = new MainClientRunThread(waitlist);
				mainthread.setRunprocesscnt(Integer.valueOf(this.control_runcount_jtf.getText()));//�����̲߳�������
				statusBar.createJProgressBar(mainthread, this);//��ʼ��������
				mainthread.start();//��ʼִ������
				//�ı�״̬
				this.changeJFType(false, 2);
			}
		}else if(acommand.equals("control_fail_repeat_jbtn")){//ʧ�ܶ�������
			if(mainthread!=null && !mainthread.isAlive()){//�����̲߳��
				List faillist = mainthread.getFaillist();
				if(faillist.size()>0){
					//�ı�״̬
					this.changeJFType(false, 1);
					//ʧ������
					for(int i=0;i<faillist.size();i++){
						if(faillist.get(i) instanceof Http360ClientThread){
							((Http360ClientThread)faillist.get(i)).repeated();
						}else if(faillist.get(i) instanceof HttpDuokuClientThread){
							((HttpDuokuClientThread)faillist.get(i)).repeated();
						}else if(faillist.get(i) instanceof HttpMysticalcardClientThread){
							((HttpMysticalcardClientThread)faillist.get(i)).repeated();
						}
					}					
					mainthread = null;
					mainthread = new MainClientRunThread(faillist);
					mainthread.setRunprocesscnt(Integer.valueOf(this.control_runcount_jtf.getText()));//�����̲߳�������
					statusBar.createJProgressBar(mainthread, this);//��ʼ��������
					mainthread.start();//��ʼִ������
				}
			}
		}else if(acommand.equals("account_config")){//�˺�����
			//�˺����ý���(����)
			AccountCfgClient acfg = AccountCfgClient.getInstance(this);
			acfg.setVisible(true);
//			AccountCfgClient acfg = new AccountCfgClient(this);
		}else if(acommand.equals("proxy_jcb")){//�Ƿ�������
			//���ô���
			String proxy_ip = "127.0.0.1";
			String proxy_port = "8087";
			Properties prop = System.getProperties();
			if(proxy_jcb.isSelected()){//��������
				proxy_ip = this.proxy_ip_jtf.getText();
				proxy_port = this.proxy_port_jtf.getText();
				//����http����Ҫʹ�õĴ���������ĵ�ַ
				prop.setProperty("http.proxyHost", proxy_ip);
				//����http����Ҫʹ�õĴ���������Ķ˿� 
				prop.setProperty("http.proxyPort", proxy_port);
			}else{//û�п�������
				proxy_ip = "";
				proxy_port = "";
				//����http����Ҫʹ�õĴ���������ĵ�ַ
				prop.setProperty("http.proxyHost", proxy_ip);
				//����http����Ҫʹ�õĴ���������Ķ˿� 
				prop.setProperty("http.proxyPort", proxy_port);
			}
		}
	}
	
	/**
	 * ��־���ӽ���
	 * */
	private void StartLog(){
		this.lt = new LogThread(log_jta);
		new Thread(this.lt).start();
	}
	
	/**
	 * ������־���ı���
	 * */
	public void setLogNick(String _nick){
		log_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"��־���["+_nick+"]",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
	}
	
	/**
	 * �ı�ؼ�״̬
	 * @param _flag �ؼ�״̬
	 * @param _tag 0,��ʾ���� 1,��ʾʧ������ 2,��ʾ��ʼִ������
	 * */
	public void changeJFType(boolean _flag, int _tag){
		if(_tag==0){
			proxy_ip_jtf.setEditable(_flag);
			proxy_port_jtf.setEditable(_flag);
			account_config.setEnabled(_flag);
			control_runcount_jtf.setEditable(_flag);
			control_gomaze_jbtn.setEnabled(_flag);
			control_fail_repeat_jbtn.setEnabled(!_flag);
		}else if(_tag==1){
			control_fail_repeat_jbtn.setEnabled(_flag);
		}else if(_tag==2){
			proxy_ip_jtf.setEditable(_flag);
			proxy_port_jtf.setEditable(_flag);
			account_config.setEnabled(_flag);
			control_runcount_jtf.setEditable(_flag);
			control_gomaze_jbtn.setEnabled(_flag);
			control_fail_repeat_jbtn.setEnabled(_flag);
		}
	}
	
	/**
	 * ���¼�������
	 * */
	public void reLoadIni(){
		loadConfig();
		
		//�Ƴ�
		int ItemCount = account_360_jcbb.getItemCount()-1;
		while(ItemCount>=0){
			account_360_jcbb.removeItemAt(ItemCount);
			ItemCount = account_360_jcbb.getItemCount()-1;
		}
		//����
		for(int i=0;i<this.list360.size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(this.list360.get(i).getNick());
			cValue.setBolValue(true);
			cValue.setThread1(this.list360.get(i));
			account_360_jcbb.addItem(cValue);
		}
		account_360_jcbb.setRenderer(new CheckListCellRenderer());
		account_360_jcbb.setFont(Constants.font);

		//�Ƴ�
		ItemCount = account_duoku_jcbb.getItemCount()-1;
		while(ItemCount>=0){
			account_duoku_jcbb.removeItemAt(ItemCount);
			ItemCount = account_duoku_jcbb.getItemCount()-1;
		}
		//����
		for(int i=0;i<this.listduoku.size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(this.listduoku.get(i).getNick());
			cValue.setBolValue(true);
			cValue.setThread2(this.listduoku.get(i));
			account_duoku_jcbb.addItem(cValue);
		}
		account_duoku_jcbb.setRenderer(new CheckListCellRenderer());
		account_duoku_jcbb.setFont(Constants.font);

		//�Ƴ�
		ItemCount = account_gw_jcbb.getItemCount()-1;
		while(ItemCount>=0){
			account_gw_jcbb.removeItemAt(ItemCount);
			ItemCount = account_gw_jcbb.getItemCount()-1;
		}
		//����
		for(int i=0;i<this.listgw.size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(this.listgw.get(i).getNick());
			cValue.setBolValue(true);
			cValue.setThread3(this.listgw.get(i));
			account_gw_jcbb.addItem(cValue);
		}
		account_gw_jcbb.setRenderer(new CheckListCellRenderer());
		account_gw_jcbb.setFont(Constants.font);
	}
	
	public static void main(String[] args) {
		new MainClient();
	}
}
