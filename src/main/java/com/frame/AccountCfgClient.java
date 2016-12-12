package com.frame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import util.DuokuAesUtil;

import client.HttpDuokuClient;

public class AccountCfgClient extends JDialog implements ActionListener,WindowListener {	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JPanel select_jp = null;//选择面板
	private JPanel edit_jp = null;//编辑面板
	private JPanel set_jp = null;//确认面板

	private JLabel select_all_jlb = null;
	private JComboBox select_all_jcbb = null;
	private JComboBox select_duoku_jcbb = null;
	private JButton select_duoku_btn = null;
	private JTextField select_duoku_jtf = null;
	
	private JTextArea edit_jta = null;
	
	private JButton set_edit_btn = null;//编辑按钮
	private JButton set_save_btn = null;//保存按钮
	private JButton set_cancel_btn = null;//取消按钮
	
	private StringBuffer sb360 = null;//360账号配置
	private StringBuffer sbduoku = null;//duoku账号配置
	private StringBuffer sbgw = null;//gw账号配置
	
	private StringBuffer sbtmp = null;//临时保存 用于取消
	private boolean is_modify = false;//是否修改
	private MainClient mc = null;//父窗口,用来重置修改后的配置
	
	private static AccountCfgClient acfg = null;
	
	public static AccountCfgClient getInstance(JFrame _frame){
		if(acfg==null){
			acfg = new AccountCfgClient(_frame);
		}
		return acfg;
	}

	/**
	 *  构造函数
	 * */
	private AccountCfgClient(JFrame _frame) {
		//继承
		super(_frame, "账号配置", true);
		
		if(_frame instanceof MainClient)
			this.mc = (MainClient)_frame;
		
		//选择面板
		select_jp = new JPanel();
		select_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"选择面板",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		select_jp.setFont(Constants.font);
		select_all_jlb = new JLabel("账号平台:");
		select_all_jlb.setFont(Constants.font);
		Vector<String> list_all = new Vector<String>();
		list_all.add("360");
		list_all.add("多酷");
		list_all.add("官网");
		select_all_jcbb = new JComboBox(list_all);
		select_all_jcbb.setFont(Constants.font);
		select_all_jcbb.setActionCommand("select_all_jcbb");
		select_all_jcbb.addActionListener(this);
		
		Vector<String> select_duoku = new Vector<String>();
		for(int i=0;i<Constants.getInstance().getListduoku().size();i++){
			select_duoku.add(Constants.getInstance().getListduoku().get(i).getNick());
		}
		select_duoku_jcbb = new JComboBox(select_duoku);
		select_duoku_jcbb.setFont(Constants.font);
		select_duoku_btn = new JButton("获得新sessionid");
		select_duoku_btn.setFont(Constants.font);
		select_duoku_btn.setActionCommand("select_duoku_btn");
		select_duoku_btn.addActionListener(this);
		select_duoku_jtf = new JTextField(20);
		select_duoku_jtf.setFont(Constants.font);
		select_duoku_jtf.setEditable(false);//不可编辑
		select_jp.add(select_all_jlb);
		select_jp.add(select_all_jcbb);
		select_jp.add(select_duoku_jcbb);
		select_jp.add(select_duoku_btn);
		select_jp.add(select_duoku_jtf);
		
		//隐藏多酷控件
		changeDuoku(false);
		
		//编辑面板
		edit_jp = new JPanel();
		edit_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"编辑面板",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		edit_jp.setLayout(new GridLayout(1,1));
		edit_jta = new JTextArea("");
		edit_jta.setLineWrap(true);//自动换行
		edit_jta.setWrapStyleWord(true);//断行不断字
		edit_jp.add(new JScrollPane(edit_jta));
		
		//加载配置
		loadConfig();
		
		//确认面板
		set_jp = new JPanel();
		set_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"确认面板",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		set_edit_btn = new JButton("编辑");
		set_edit_btn.setFont(Constants.font);
		set_edit_btn.setActionCommand("set_edit_btn");
		set_edit_btn.addActionListener(this);
		set_save_btn = new JButton("保存");
		set_save_btn.setFont(Constants.font);
		set_save_btn.setActionCommand("set_save_btn");
		set_save_btn.addActionListener(this);
		set_cancel_btn = new JButton("取消");
		set_cancel_btn.setFont(Constants.font);
		set_cancel_btn.setActionCommand("set_cancel_btn");
		set_cancel_btn.addActionListener(this);
		set_jp.add(set_edit_btn);
		set_jp.add(set_save_btn);
		set_jp.add(set_cancel_btn);
		
		//不可编辑
		changeJFType(false);
		
		//整体容器
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(select_jp, BorderLayout.NORTH);
		c.add(edit_jp, BorderLayout.CENTER);
		c.add(set_jp, BorderLayout.SOUTH);
		
		//主窗口属性
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();//获得显示器大小对象
		this.setBounds((displaySize.width-700)/2, (displaySize.height-500)/2, 700, 500);
		this.setResizable(false);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(false);//第一次不显示
	}

	
	/**
	 * 载入配置文件
	 * class.getResource("/") --> 返回class文件所在的顶级目录，一般为包名的顶级目录
	 * */
	public void loadConfig(){
		FileReader fr = null;
    	BufferedReader br = null;
        String tmp = "";
    	StringBuffer sb = new StringBuffer("");
    	sbtmp = new StringBuffer("");
    	File f = null;
		try{
			//360 config
			sb360 = new StringBuffer("");
			f = new File(Constants.path360);
	        fr = new FileReader(f);
	        br = new BufferedReader(fr);
	        while((tmp = br.readLine()) != null){
	        	sb.append(tmp);
	        	sb.append("\r\n");
	        }
	        sb360.append(sb.toString());
			f = null;
	        fr.close();
	        br.close();
	        if(sb360.length()>0){
	        	select_all_jcbb.setSelectedIndex(0);//选择
	        	edit_jta.setText(sb360.toString());//设置文本
	        }
			
			//duoku config
			sbduoku = new StringBuffer("");
			f = new File(Constants.pathduoku);
	        fr = new FileReader(f);
	        br = new BufferedReader(fr);
	        sb.delete(0, sb.length());//删除历史数据
	        while((tmp = br.readLine()) != null){
	        	sb.append(tmp);
	        	sb.append("\r\n");
	        }
	        sbduoku.append(sb.toString());
			f = null;
	        fr.close();
	        br.close();
	        if(edit_jta.getText().length()==0 && sbduoku.length()>0){
	        	select_all_jcbb.setSelectedIndex(1);//选择
	        	edit_jta.setText(sbduoku.toString());//设置文本
	        }
	
			//mysticalcard config
			sbgw = new StringBuffer("");
			f = new File(Constants.pathgw);
	        fr = new FileReader(f);
	        br = new BufferedReader(fr);
	        sb.delete(0, sb.length());//删除历史数据
	        while((tmp = br.readLine()) != null){
	        	sb.append(tmp);
	        	sb.append("\r\n");
	        }
	        sbgw.append(sb.toString());
			f = null;
	        fr.close();
	        br.close();
	        if(edit_jta.getText().length()==0 && sbgw.length()>0){
	        	select_all_jcbb.setSelectedIndex(2);//选择
	        	edit_jta.setText(sbgw.toString());//设置文本
	        }

	        setTmp();//临时保存
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(f!=null)
				f = null;
			if(fr!=null)
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	/**
	 * 修改配置入库
	 * */
	private void saveToConfig(StringBuffer _sb){
		FileWriter fw = null;
    	BufferedWriter bw = null;
    	String filename = "";
    	File f = null;
    	if(select_all_jcbb.getSelectedItem().toString().equals("360")){
    		filename = Constants.path360;
			sb360.delete(0, sb360.length());
			sb360.append(_sb);
		}else if(select_all_jcbb.getSelectedItem().toString().equals("多酷")){
			filename = Constants.pathduoku;
			sbduoku.delete(0, sbduoku.length());
			sbduoku.append(_sb);
		}else if(select_all_jcbb.getSelectedItem().toString().equals("官网")){
			filename = Constants.pathgw;
			sbgw.delete(0, sbgw.length());
			sbgw.append(_sb);
		}
    	try{
			f = new File(filename);
	    	fw = new FileWriter(f);
	        bw = new BufferedWriter(fw);            
	        bw.write(_sb.toString());//写入流
	        bw.flush();//刷新流
	        
	        f = null;
	        fw.close();
	        bw.close();
    	}catch(Exception e){
    		e.printStackTrace();
    	}finally{
    		if(f!=null)
    			f = null;
    		if(fw!=null)
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    		if(bw!=null)
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    	}
	}
	
	/**
	 * 临时保存文本
	 * */
	private void setTmp(){
		sbtmp.delete(0, sbtmp.length());//先删除原有内容
		sbtmp.append(edit_jta.getText());//临时保存
	}
	
	/**
	 * 界面控件状态
	 * */
	private void changeJFType(boolean _flag){
		select_all_jcbb.setEnabled(!_flag);
		edit_jta.setEditable(_flag);//文本输入
		set_edit_btn.setEnabled(!_flag);//编辑按钮
		set_cancel_btn.setEnabled(_flag);//取消按钮
		set_save_btn.setEnabled(_flag);//保存按钮		
		select_duoku_jcbb.setEnabled(!_flag);//多酷选择
		select_duoku_btn.setEnabled(!_flag);//多酷生成新session按钮
	}
	
	/**
	 * 多酷获得新sessionid界面状态控制
	 * */
	private void changeDuoku(boolean _flag){
		select_duoku_jcbb.setVisible(_flag);
		select_duoku_btn.setVisible(_flag);
		select_duoku_jtf.setVisible(_flag);
	}

	/**
	 * action事件
	 * */
	public void actionPerformed(ActionEvent e) {
		String acommand = e.getActionCommand();
		if(acommand.equals("set_edit_btn")){//编辑按钮
			if(!edit_jta.isEditable())//是否可编辑
				changeJFType(true);//改变界面控件状态
		}else if(acommand.equals("set_cancel_btn")){//取消按钮
			edit_jta.setText(sbtmp.toString());//从临时保存读取
			changeJFType(false);//改变界面控件状态
		}else if(acommand.equals("set_save_btn")){//保存按钮
			if(edit_jta.isEditable()){//是否可编辑
				int _flag = JOptionPane.showConfirmDialog(null, "是否保存?", "是否保存?", JOptionPane.YES_NO_OPTION);
				if(_flag==JOptionPane.YES_OPTION){
					this.is_modify = true;
					//写入到配置
					saveToConfig(new StringBuffer(edit_jta.getText()));
			        setTmp();//临时保存
					changeJFType(false);//改变界面控件状态
				}
			}
		}else if(acommand.equals("select_all_jcbb")){//配置内容显示
			if(select_all_jcbb.getSelectedItem().toString().equals("360")){
				edit_jta.setText(sb360.toString());
				changeDuoku(false);
			}else if(select_all_jcbb.getSelectedItem().toString().equals("多酷")){
				edit_jta.setText(sbduoku.toString());
				changeDuoku(true);
			}else if(select_all_jcbb.getSelectedItem().toString().equals("官网")){
				edit_jta.setText(sbgw.toString());
				changeDuoku(false);
			}
	        setTmp();//临时保存
		}else if(acommand.equals("select_duoku_btn")){//多酷获得新sessionid
			HttpDuokuClient dk = new HttpDuokuClient();
//			DuokuAesUtil d = new DuokuAesUtil();
			String select_str = select_duoku_jcbb.getSelectedItem().toString();
			for(int i=0;i<Constants.getInstance().getListduoku().size();i++){
				if(Constants.getInstance().getListduoku().get(i).getNick().equals(select_str)){
					select_duoku_jtf.setText(dk.gameSDKLogin(Constants.getInstance().getListduoku().get(i).getSdklogin()));
					break;
				}
			}
		}
	}

	/**
	 * 窗口事件 活动
	 * */
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * 窗口事件 关闭
	 * */
	public void windowClosed(WindowEvent e) {
		if(this.mc!=null && this.is_modify){
			this.mc.reLoadIni();//重置配置
		}
	}

	/**
	 * 窗口事件 关闭中
	 * */
	public void windowClosing(WindowEvent e) {
	}

	/**
	 * 窗口事件 Deactivated
	 * */
	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * 窗口事件 Deiconified
	 * */
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * 窗口事件 Iconified
	 * */
	public void windowIconified(WindowEvent e) {
	}

	/**
	 * 窗口事件 Opened
	 * */
	public void windowOpened(WindowEvent e) {
	}
}
