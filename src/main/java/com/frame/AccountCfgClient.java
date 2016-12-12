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

	private JPanel select_jp = null;//ѡ�����
	private JPanel edit_jp = null;//�༭���
	private JPanel set_jp = null;//ȷ�����

	private JLabel select_all_jlb = null;
	private JComboBox select_all_jcbb = null;
	private JComboBox select_duoku_jcbb = null;
	private JButton select_duoku_btn = null;
	private JTextField select_duoku_jtf = null;
	
	private JTextArea edit_jta = null;
	
	private JButton set_edit_btn = null;//�༭��ť
	private JButton set_save_btn = null;//���水ť
	private JButton set_cancel_btn = null;//ȡ����ť
	
	private StringBuffer sb360 = null;//360�˺�����
	private StringBuffer sbduoku = null;//duoku�˺�����
	private StringBuffer sbgw = null;//gw�˺�����
	
	private StringBuffer sbtmp = null;//��ʱ���� ����ȡ��
	private boolean is_modify = false;//�Ƿ��޸�
	private MainClient mc = null;//������,���������޸ĺ������
	
	private static AccountCfgClient acfg = null;
	
	public static AccountCfgClient getInstance(JFrame _frame){
		if(acfg==null){
			acfg = new AccountCfgClient(_frame);
		}
		return acfg;
	}

	/**
	 *  ���캯��
	 * */
	private AccountCfgClient(JFrame _frame) {
		//�̳�
		super(_frame, "�˺�����", true);
		
		if(_frame instanceof MainClient)
			this.mc = (MainClient)_frame;
		
		//ѡ�����
		select_jp = new JPanel();
		select_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"ѡ�����",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		select_jp.setFont(Constants.font);
		select_all_jlb = new JLabel("�˺�ƽ̨:");
		select_all_jlb.setFont(Constants.font);
		Vector<String> list_all = new Vector<String>();
		list_all.add("360");
		list_all.add("���");
		list_all.add("����");
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
		select_duoku_btn = new JButton("�����sessionid");
		select_duoku_btn.setFont(Constants.font);
		select_duoku_btn.setActionCommand("select_duoku_btn");
		select_duoku_btn.addActionListener(this);
		select_duoku_jtf = new JTextField(20);
		select_duoku_jtf.setFont(Constants.font);
		select_duoku_jtf.setEditable(false);//���ɱ༭
		select_jp.add(select_all_jlb);
		select_jp.add(select_all_jcbb);
		select_jp.add(select_duoku_jcbb);
		select_jp.add(select_duoku_btn);
		select_jp.add(select_duoku_jtf);
		
		//���ض��ؼ�
		changeDuoku(false);
		
		//�༭���
		edit_jp = new JPanel();
		edit_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"�༭���",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		edit_jp.setLayout(new GridLayout(1,1));
		edit_jta = new JTextArea("");
		edit_jta.setLineWrap(true);//�Զ�����
		edit_jta.setWrapStyleWord(true);//���в�����
		edit_jp.add(new JScrollPane(edit_jta));
		
		//��������
		loadConfig();
		
		//ȷ�����
		set_jp = new JPanel();
		set_jp.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"ȷ�����",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		set_edit_btn = new JButton("�༭");
		set_edit_btn.setFont(Constants.font);
		set_edit_btn.setActionCommand("set_edit_btn");
		set_edit_btn.addActionListener(this);
		set_save_btn = new JButton("����");
		set_save_btn.setFont(Constants.font);
		set_save_btn.setActionCommand("set_save_btn");
		set_save_btn.addActionListener(this);
		set_cancel_btn = new JButton("ȡ��");
		set_cancel_btn.setFont(Constants.font);
		set_cancel_btn.setActionCommand("set_cancel_btn");
		set_cancel_btn.addActionListener(this);
		set_jp.add(set_edit_btn);
		set_jp.add(set_save_btn);
		set_jp.add(set_cancel_btn);
		
		//���ɱ༭
		changeJFType(false);
		
		//��������
		Container c = this.getContentPane();
		c.setLayout(new BorderLayout());
		c.add(select_jp, BorderLayout.NORTH);
		c.add(edit_jp, BorderLayout.CENTER);
		c.add(set_jp, BorderLayout.SOUTH);
		
		//����������
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();//�����ʾ����С����
		this.setBounds((displaySize.width-700)/2, (displaySize.height-500)/2, 700, 500);
		this.setResizable(false);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setVisible(false);//��һ�β���ʾ
	}

	
	/**
	 * ���������ļ�
	 * class.getResource("/") --> ����class�ļ����ڵĶ���Ŀ¼��һ��Ϊ�����Ķ���Ŀ¼
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
	        	select_all_jcbb.setSelectedIndex(0);//ѡ��
	        	edit_jta.setText(sb360.toString());//�����ı�
	        }
			
			//duoku config
			sbduoku = new StringBuffer("");
			f = new File(Constants.pathduoku);
	        fr = new FileReader(f);
	        br = new BufferedReader(fr);
	        sb.delete(0, sb.length());//ɾ����ʷ����
	        while((tmp = br.readLine()) != null){
	        	sb.append(tmp);
	        	sb.append("\r\n");
	        }
	        sbduoku.append(sb.toString());
			f = null;
	        fr.close();
	        br.close();
	        if(edit_jta.getText().length()==0 && sbduoku.length()>0){
	        	select_all_jcbb.setSelectedIndex(1);//ѡ��
	        	edit_jta.setText(sbduoku.toString());//�����ı�
	        }
	
			//mysticalcard config
			sbgw = new StringBuffer("");
			f = new File(Constants.pathgw);
	        fr = new FileReader(f);
	        br = new BufferedReader(fr);
	        sb.delete(0, sb.length());//ɾ����ʷ����
	        while((tmp = br.readLine()) != null){
	        	sb.append(tmp);
	        	sb.append("\r\n");
	        }
	        sbgw.append(sb.toString());
			f = null;
	        fr.close();
	        br.close();
	        if(edit_jta.getText().length()==0 && sbgw.length()>0){
	        	select_all_jcbb.setSelectedIndex(2);//ѡ��
	        	edit_jta.setText(sbgw.toString());//�����ı�
	        }

	        setTmp();//��ʱ����
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
	 * �޸��������
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
		}else if(select_all_jcbb.getSelectedItem().toString().equals("���")){
			filename = Constants.pathduoku;
			sbduoku.delete(0, sbduoku.length());
			sbduoku.append(_sb);
		}else if(select_all_jcbb.getSelectedItem().toString().equals("����")){
			filename = Constants.pathgw;
			sbgw.delete(0, sbgw.length());
			sbgw.append(_sb);
		}
    	try{
			f = new File(filename);
	    	fw = new FileWriter(f);
	        bw = new BufferedWriter(fw);            
	        bw.write(_sb.toString());//д����
	        bw.flush();//ˢ����
	        
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
	 * ��ʱ�����ı�
	 * */
	private void setTmp(){
		sbtmp.delete(0, sbtmp.length());//��ɾ��ԭ������
		sbtmp.append(edit_jta.getText());//��ʱ����
	}
	
	/**
	 * ����ؼ�״̬
	 * */
	private void changeJFType(boolean _flag){
		select_all_jcbb.setEnabled(!_flag);
		edit_jta.setEditable(_flag);//�ı�����
		set_edit_btn.setEnabled(!_flag);//�༭��ť
		set_cancel_btn.setEnabled(_flag);//ȡ����ť
		set_save_btn.setEnabled(_flag);//���水ť		
		select_duoku_jcbb.setEnabled(!_flag);//���ѡ��
		select_duoku_btn.setEnabled(!_flag);//���������session��ť
	}
	
	/**
	 * �������sessionid����״̬����
	 * */
	private void changeDuoku(boolean _flag){
		select_duoku_jcbb.setVisible(_flag);
		select_duoku_btn.setVisible(_flag);
		select_duoku_jtf.setVisible(_flag);
	}

	/**
	 * action�¼�
	 * */
	public void actionPerformed(ActionEvent e) {
		String acommand = e.getActionCommand();
		if(acommand.equals("set_edit_btn")){//�༭��ť
			if(!edit_jta.isEditable())//�Ƿ�ɱ༭
				changeJFType(true);//�ı����ؼ�״̬
		}else if(acommand.equals("set_cancel_btn")){//ȡ����ť
			edit_jta.setText(sbtmp.toString());//����ʱ�����ȡ
			changeJFType(false);//�ı����ؼ�״̬
		}else if(acommand.equals("set_save_btn")){//���水ť
			if(edit_jta.isEditable()){//�Ƿ�ɱ༭
				int _flag = JOptionPane.showConfirmDialog(null, "�Ƿ񱣴�?", "�Ƿ񱣴�?", JOptionPane.YES_NO_OPTION);
				if(_flag==JOptionPane.YES_OPTION){
					this.is_modify = true;
					//д�뵽����
					saveToConfig(new StringBuffer(edit_jta.getText()));
			        setTmp();//��ʱ����
					changeJFType(false);//�ı����ؼ�״̬
				}
			}
		}else if(acommand.equals("select_all_jcbb")){//����������ʾ
			if(select_all_jcbb.getSelectedItem().toString().equals("360")){
				edit_jta.setText(sb360.toString());
				changeDuoku(false);
			}else if(select_all_jcbb.getSelectedItem().toString().equals("���")){
				edit_jta.setText(sbduoku.toString());
				changeDuoku(true);
			}else if(select_all_jcbb.getSelectedItem().toString().equals("����")){
				edit_jta.setText(sbgw.toString());
				changeDuoku(false);
			}
	        setTmp();//��ʱ����
		}else if(acommand.equals("select_duoku_btn")){//�������sessionid
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
	 * �����¼� �
	 * */
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * �����¼� �ر�
	 * */
	public void windowClosed(WindowEvent e) {
		if(this.mc!=null && this.is_modify){
			this.mc.reLoadIni();//��������
		}
	}

	/**
	 * �����¼� �ر���
	 * */
	public void windowClosing(WindowEvent e) {
	}

	/**
	 * �����¼� Deactivated
	 * */
	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * �����¼� Deiconified
	 * */
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * �����¼� Iconified
	 * */
	public void windowIconified(WindowEvent e) {
	}

	/**
	 * �����¼� Opened
	 * */
	public void windowOpened(WindowEvent e) {
	}
}
