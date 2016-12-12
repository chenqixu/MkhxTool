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
		
	private JPanel top_jp = null;//顶部
	private JPanel center_jp = null;//中部
	private StatusBar statusBar = null;//底部
	
	private JPanel proxy_jp = null;//代理面板
	private JPanel account_jp = null;//账号面板
	private JPanel control_jp = null;//控制面板
	private JPanel log_jp = null;//日志面板
	private JPanel status_jp = null;//状态面板
	
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
	private JTextField control_runcount_jtf = null;//线程并发数量
	private JButton control_gomaze_jbtn = null;//迷宫
	private JButton control_fail_repeat_jbtn = null;//失败队列处理
	
	private JTextArea log_jta = null;//日志
	
	private List<Http360ClientThread> list360 = null;//360账号配置
	private List<HttpDuokuClientThread> listduoku = null;//duoku账号配置
	private List<HttpMysticalcardClientThread> listgw = null;//gw账号配置

	private LogThread lt = null;//监视日志进程
	private MainClientRunThread mainthread = null;//任务主线程
	
	private static MainClient mc = null;
	
	/**
	 * 构造工厂(单例)
	 * */
	public static MainClient getInstance(){
		if(mc==null){
			mc = new MainClient();
		}
		return mc;
	}

	/**
	 * 构造函数
	 * */
	private MainClient(){
		//状态面板
		status_jp = new JPanel();
		status_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"状态面板[初始化 灰色,运行中 黄色,成功 绿色,失败 红色]",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		status_jp.setFont(Constants.font);
		status_jp.setLayout(new GridLayout(5 ,5 ,5 ,5));
		
		//日志面板
		log_jp = new JPanel();
		log_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"日志面板",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		log_jp.setLayout(new GridLayout(1, 1));//可以让JTextArea占满面板
		log_jta = new JTextArea();
		log_jta.setFont(Constants.font);
		log_jta.setLineWrap(true);//自动换行
		log_jta.setWrapStyleWord(true);//断行不断字
		log_jta.setEditable(false);//不能编辑
		log_jp.add(new JScrollPane(log_jta));
		
		//开启日志监视进程
		this.StartLog();

		//配置加载
		this.loadConfig();
		
		//代理面板
		proxy_jp = new JPanel();
		proxy_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"代理设置",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		proxy_jcb = new JCheckBox("是否开启代理");
		proxy_jcb.setFont(Constants.font);
		proxy_jcb.setActionCommand("proxy_jcb");
		proxy_jcb.addActionListener(this);
		proxy_ip_jlb = new JLabel("ip地址:");
		proxy_ip_jlb.setFont(Constants.font);
		proxy_port_jlb = new JLabel("port端口:");
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
		
		//账号面板
		account_jp = new JPanel();
		account_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"账号设置",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		account_360_jcb = new JCheckBox("是否开启360", true);
		account_360_jcb.setFont(Constants.font);
		account_duoku_jcb = new JCheckBox("是否开启多酷", true);
		account_duoku_jcb.setFont(Constants.font);
		account_gw_jcb = new JCheckBox("是否开启官网", true);
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

		account_config = new JButton("配置账号");
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
		
		//控制面板
		control_jp = new JPanel();
		control_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"控制面板",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		control_runcount_jlb = new JLabel("同时运行任务数:");
		control_runcount_jlb.setFont(Constants.font);
		control_runcount_jtf = new JTextField(10);
		control_runcount_jtf.setFont(Constants.font);
		control_runcount_jtf.setText("5");
		control_gomaze_jbtn = new JButton("一键迷宫探索");
		control_gomaze_jbtn.setFont(Constants.font);
		control_gomaze_jbtn.setActionCommand("control_gomaze_jbtn");
		control_gomaze_jbtn.addActionListener(this);
		control_fail_repeat_jbtn = new JButton("失败队列处理");
		control_fail_repeat_jbtn.setFont(Constants.font);
		control_fail_repeat_jbtn.setActionCommand("control_fail_repeat_jbtn");
		control_fail_repeat_jbtn.addActionListener(this);
		control_jp.add(control_runcount_jlb);
		control_jp.add(control_runcount_jtf);
		control_jp.add(control_gomaze_jbtn);
		control_jp.add(control_fail_repeat_jbtn);
		
		//顶部
		top_jp = new JPanel();
		top_jp.setLayout(new GridLayout(3, 1));
		top_jp.add(proxy_jp);
		top_jp.add(account_jp);
		top_jp.add(control_jp);
		
		//中部
		center_jp = new JPanel();
		center_jp.setLayout(new GridLayout(1, 2));
		center_jp.add(log_jp);
		center_jp.add(status_jp);
		
		//底层 状态栏
		statusBar = new StatusBar();
		
		//整体容器
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(top_jp, BorderLayout.NORTH);
		c.add(center_jp, BorderLayout.CENTER);
		c.add(statusBar, BorderLayout.SOUTH);
		
		//改变状态
		this.changeJFType(true, 0);
		
		//自身设置
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();//获得显示器大小对象
		this.setTitle("魔卡幻想辅助工具");
		this.setBounds((displaySize.width-900)/2, (displaySize.height-600)/2, 900, 600);
		this.setResizable(false);
		this.setIconImage(this.getToolkit().getImage(Constants.pngPath));
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭退出jvm
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//关闭就dispose
		this.setVisible(true);
	}
	
	/**
	 * 载入配置文件
	 * */
	public void loadConfig(){
		status_jp.removeAll();//状态面板移除所有组件
		try{
			Constants constants = Constants.getInstance();//初始化静态公共类
			constants.loadConfig();//加载配置
			//360 config			
			list360 = new Vector<Http360ClientThread>();
			Vector<ParamsMysticalcard> v360 = constants.getList360();
			for(int j=0;j<v360.size();j++){
				Http360ClientThread thread360 = new Http360ClientThread(v360.get(j));
				list360.add(thread360);
				status_jp.add(thread360.getMonitor());
				thread360.getMonitor().setLog(this.lt);//设置监控日志线程,为了点击显示日志
				thread360.getMonitor().setMainClient(this);//设置主程序
				status_jp.revalidate();//刷新
			}
			
			//duoku config
			listduoku = new Vector<HttpDuokuClientThread>();
			Vector<ParamsMysticalcard> vduoku = constants.getListduoku();
			for(int j=0;j<vduoku.size();j++){
				HttpDuokuClientThread threadDuoku = new HttpDuokuClientThread(vduoku.get(j));
				listduoku.add(threadDuoku);
				status_jp.add(threadDuoku.getMonitor());
				threadDuoku.getMonitor().setLog(this.lt);//设置监控日志线程,为了点击显示日志
				threadDuoku.getMonitor().setMainClient(this);//设置主程序
				status_jp.revalidate();//刷新
			}
			
			//mysticalcard config
			listgw = new Vector<HttpMysticalcardClientThread>();
			Vector<ParamsMysticalcard> vgw = constants.getListgw();
			for(int j=0;j<vgw.size();j++){
				HttpMysticalcardClientThread threadMysticalcard = new HttpMysticalcardClientThread(vgw.get(j));
				listgw.add(threadMysticalcard);
				status_jp.add(threadMysticalcard.getMonitor());
				threadMysticalcard.getMonitor().setLog(this.lt);//设置监控日志线程,为了点击显示日志
				threadMysticalcard.getMonitor().setMainClient(this);//设置主程序
				status_jp.revalidate();//刷新
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * 动作监视
	 * */
	public void actionPerformed(ActionEvent e) {
		String acommand = e.getActionCommand();
		if(acommand.equals("control_gomaze_jbtn")){//一键迷宫探索
			//重置失败
			for(int i=0;i<list360.size();i++)
				list360.get(i).repeated();
			for(int i=0;i<listduoku.size();i++)
				listduoku.get(i).repeated();
			for(int i=0;i<listgw.size();i++)
				listgw.get(i).repeated();
			
			//汇总到任务列表
			Vector<Runnable> waitlist = new Vector<Runnable>();
			if(account_360_jcb.isSelected() && list360!=null){//如果360被选中
				List<Http360ClientThread> list360_run = new Vector<Http360ClientThread>();//360运行账号配置
				for(int i=1;i<account_360_jcbb.getItemCount();i++){					
					if(((MyCheckBox)account_360_jcbb.getItemAt(i)).isBolValue()
							&& ((MyCheckBox)account_360_jcbb.getItemAt(i)).getThread1()!=null)//被选中
						list360_run.add(((MyCheckBox)account_360_jcbb.getItemAt(i)).getThread1());
				}
				waitlist.addAll(list360_run);
			}
			if(account_duoku_jcb.isSelected() && listduoku!=null){//如果多酷被选中
				List<HttpDuokuClientThread> listduoku_run = new Vector<HttpDuokuClientThread>();//duoku运行账号配置
				for(int i=1;i<account_duoku_jcbb.getItemCount();i++){
					if(((MyCheckBox)account_duoku_jcbb.getItemAt(i)).isBolValue()
							&& ((MyCheckBox)account_duoku_jcbb.getItemAt(i)).getThread2()!=null)//被选中
						listduoku_run.add(((MyCheckBox)account_duoku_jcbb.getItemAt(i)).getThread2());
				}				
				waitlist.addAll(listduoku_run);
			}
			if(account_gw_jcb.isSelected() && listgw!=null){//如果官网被选中
				List<HttpMysticalcardClientThread> listgw_run = new Vector<HttpMysticalcardClientThread>();//gw运行账号配置
				for(int i=1;i<account_gw_jcbb.getItemCount();i++){
					if(((MyCheckBox)account_gw_jcbb.getItemAt(i)).isBolValue()
							&& ((MyCheckBox)account_gw_jcbb.getItemAt(i)).getThread3()!=null)//被选中
						listgw_run.add(((MyCheckBox)account_gw_jcbb.getItemAt(i)).getThread3());
				}				
				waitlist.addAll(listgw_run);
			}
			
			//任务主线程
			if(waitlist!=null && waitlist.size()>0){
				mainthread = null;
				mainthread = new MainClientRunThread(waitlist);
				mainthread.setRunprocesscnt(Integer.valueOf(this.control_runcount_jtf.getText()));//设置线程并发数量
				statusBar.createJProgressBar(mainthread, this);//初始化进度条
				mainthread.start();//开始执行任务
				//改变状态
				this.changeJFType(false, 2);
			}
		}else if(acommand.equals("control_fail_repeat_jbtn")){//失败队列重试
			if(mainthread!=null && !mainthread.isAlive()){//任务线程不活动
				List faillist = mainthread.getFaillist();
				if(faillist.size()>0){
					//改变状态
					this.changeJFType(false, 1);
					//失败重置
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
					mainthread.setRunprocesscnt(Integer.valueOf(this.control_runcount_jtf.getText()));//设置线程并发数量
					statusBar.createJProgressBar(mainthread, this);//初始化进度条
					mainthread.start();//开始执行任务
				}
			}
		}else if(acommand.equals("account_config")){//账号配置
			//账号配置界面(单例)
			AccountCfgClient acfg = AccountCfgClient.getInstance(this);
			acfg.setVisible(true);
//			AccountCfgClient acfg = new AccountCfgClient(this);
		}else if(acommand.equals("proxy_jcb")){//是否开启代理
			//配置代理
			String proxy_ip = "127.0.0.1";
			String proxy_port = "8087";
			Properties prop = System.getProperties();
			if(proxy_jcb.isSelected()){//开启代理
				proxy_ip = this.proxy_ip_jtf.getText();
				proxy_port = this.proxy_port_jtf.getText();
				//设置http访问要使用的代理服务器的地址
				prop.setProperty("http.proxyHost", proxy_ip);
				//设置http访问要使用的代理服务器的端口 
				prop.setProperty("http.proxyPort", proxy_port);
			}else{//没有开启代理
				proxy_ip = "";
				proxy_port = "";
				//设置http访问要使用的代理服务器的地址
				prop.setProperty("http.proxyHost", proxy_ip);
				//设置http访问要使用的代理服务器的端口 
				prop.setProperty("http.proxyPort", proxy_port);
			}
		}
	}
	
	/**
	 * 日志监视进程
	 * */
	private void StartLog(){
		this.lt = new LogThread(log_jta);
		new Thread(this.lt).start();
	}
	
	/**
	 * 设置日志面板的标题
	 * */
	public void setLogNick(String _nick){
		log_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"日志面板["+_nick+"]",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
	}
	
	/**
	 * 改变控件状态
	 * @param _flag 控件状态
	 * @param _tag 0,表示其他 1,表示失败重试 2,表示开始执行任务
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
	 * 重新加载配置
	 * */
	public void reLoadIni(){
		loadConfig();
		
		//移除
		int ItemCount = account_360_jcbb.getItemCount()-1;
		while(ItemCount>=0){
			account_360_jcbb.removeItemAt(ItemCount);
			ItemCount = account_360_jcbb.getItemCount()-1;
		}
		//增加
		for(int i=0;i<this.list360.size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(this.list360.get(i).getNick());
			cValue.setBolValue(true);
			cValue.setThread1(this.list360.get(i));
			account_360_jcbb.addItem(cValue);
		}
		account_360_jcbb.setRenderer(new CheckListCellRenderer());
		account_360_jcbb.setFont(Constants.font);

		//移除
		ItemCount = account_duoku_jcbb.getItemCount()-1;
		while(ItemCount>=0){
			account_duoku_jcbb.removeItemAt(ItemCount);
			ItemCount = account_duoku_jcbb.getItemCount()-1;
		}
		//增加
		for(int i=0;i<this.listduoku.size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(this.listduoku.get(i).getNick());
			cValue.setBolValue(true);
			cValue.setThread2(this.listduoku.get(i));
			account_duoku_jcbb.addItem(cValue);
		}
		account_duoku_jcbb.setRenderer(new CheckListCellRenderer());
		account_duoku_jcbb.setFont(Constants.font);

		//移除
		ItemCount = account_gw_jcbb.getItemCount()-1;
		while(ItemCount>=0){
			account_gw_jcbb.removeItemAt(ItemCount);
			ItemCount = account_gw_jcbb.getItemCount()-1;
		}
		//增加
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
