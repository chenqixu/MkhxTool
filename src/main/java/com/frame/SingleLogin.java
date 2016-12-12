package com.frame;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 * �����˺ŵ�½,�����Ұ�,ڤ��,��ѯ����,ǿ������,���׾��Ž��
 * */
public class SingleLogin extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//�����һ��Ϊ2,������� �˺����,������� ������� ״̬��ʾ���
	private JPanel jptop = null;
	private JPanel jpend = null;
	//�˺����
	private JPanel jpaccount = null;	
	private JLabel account_360_jlb = null;
	private JLabel account_duoku_jlb = null;
	private JLabel account_gw_jlb = null;
	private MyComboBox account_360_jcbb = null;
	private MyComboBox account_duoku_jcbb = null;
	private MyComboBox account_gw_jcbb = null;
	//�������
	private JPanel jpcontrol = null;
	private JButton control_login_jb = null;
	private JButton control_buy_jb = null;
	private JButton control_meditation_jb = null;
	private JButton control_query_jb = null;
	private JButton control_strengthen_jb = null;
	private JButton control_donation_jb = null;
	//״̬��ʾ���
	private JPanel jpstatus = null;
	
	/**
	 * ����
	 * */
	public SingleLogin(){		
		//�˺����
		jpaccount = new JPanel();
		jpaccount.setFont(Constants.font);
		jpaccount.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"�˺����",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));		
		account_360_jlb = new JLabel("360:");
		account_360_jlb.setFont(Constants.font);
		account_duoku_jlb = new JLabel("���:");
		account_duoku_jlb.setFont(Constants.font);
		account_gw_jlb = new JLabel("����:");
		account_gw_jlb.setFont(Constants.font);
		//�������
		Constants.getInstance().loadConfig();
		account_360_jcbb = new MyComboBox();
		for(int i=0;i<Constants.getInstance().getList360().size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(Constants.getInstance().getList360().get(i).getNick());
			cValue.setBolValue(false);
			account_360_jcbb.addItem(cValue);
		}
		account_360_jcbb.setRenderer(new CheckListCellRenderer());
		account_360_jcbb.setFont(Constants.font);
		
		account_duoku_jcbb = new MyComboBox();
		for(int i=0;i<Constants.getInstance().getListduoku().size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(Constants.getInstance().getListduoku().get(i).getNick());
			cValue.setBolValue(false);
			account_duoku_jcbb.addItem(cValue);
		}
		account_duoku_jcbb.setRenderer(new CheckListCellRenderer());
		account_duoku_jcbb.setFont(Constants.font);

		account_gw_jcbb = new MyComboBox();
		for(int i=0;i<Constants.getInstance().getListgw().size();i++){
			MyCheckBox cValue = new MyCheckBox();
			cValue.setValue(Constants.getInstance().getListgw().get(i).getNick());
			cValue.setBolValue(false);
			account_gw_jcbb.addItem(cValue);
		}
		account_gw_jcbb.setRenderer(new CheckListCellRenderer());
		account_gw_jcbb.setFont(Constants.font);
		jpaccount.add(account_360_jlb);
		jpaccount.add(account_360_jcbb);
		jpaccount.add(account_duoku_jlb);
		jpaccount.add(account_duoku_jcbb);
		jpaccount.add(account_gw_jlb);
		jpaccount.add(account_gw_jcbb);
		
		//�������
		jpcontrol = new JPanel();
		jpcontrol.setFont(Constants.font);
		jpcontrol.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"�������",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		control_login_jb = new JButton("��½");
		control_login_jb.setFont(Constants.font);
		control_buy_jb = new JButton("�����Ұ�");
		control_buy_jb.setFont(Constants.font);
		control_meditation_jb = new JButton("ڤ��");
		control_meditation_jb.setFont(Constants.font);
		control_query_jb = new JButton("��ѯ����");
		control_query_jb.setFont(Constants.font);
		control_strengthen_jb = new JButton("ǿ��");
		control_strengthen_jb.setFont(Constants.font);
		control_donation_jb = new JButton("����");
		control_donation_jb.setFont(Constants.font);
		jpcontrol.add(control_login_jb);
		jpcontrol.add(control_buy_jb);
		jpcontrol.add(control_meditation_jb);
		jpcontrol.add(control_query_jb);
		jpcontrol.add(control_strengthen_jb);
		jpcontrol.add(control_donation_jb);
		//״̬��ʾ���
		jpstatus = new JPanel();
		jpstatus.setFont(Constants.font);
		jpstatus.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"״̬��ʾ���",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		//�������
		jptop = new JPanel();
		jptop.setFont(Constants.font);
		jptop.setLayout(new GridLayout(2,1));
		jptop.add(jpaccount);
		jptop.add(jpcontrol);
		//�������
		jpend = new JPanel();
		jpend.setFont(Constants.font);
		jpend.setLayout(new GridLayout(1,1));
		jpend.add(jpstatus);
		//��������
		Container c = this.getContentPane();
		c.setLayout(new GridLayout(2, 1));
		c.add(jptop);
		c.add(jpend);
		
		//��������
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();//�����ʾ����С����
		this.setTitle("ħ�����븨������");
		this.setBounds((displaySize.width-900)/2, (displaySize.height-600)/2, 900, 600);
		this.setResizable(false);
		this.setIconImage(this.getToolkit().getImage(Constants.pngPath));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//�ر��˳�jvm
		this.setVisible(true);
	}

	/**
	 * �����¼�
	 * */
	public void actionPerformed(ActionEvent e) {
		
	}
	
	public static void main(String[] args) {
		new SingleLogin();
	}
}
