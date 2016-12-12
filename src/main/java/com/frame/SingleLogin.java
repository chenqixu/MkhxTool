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
 * 单个账号登陆,购买金币包,冥想,查询卡牌,强化卡牌,捐献军团金币
 * */
public class SingleLogin extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//主面板一份为2,上面放置 账号面板,控制面板 下面放置 状态显示面板
	private JPanel jptop = null;
	private JPanel jpend = null;
	//账号面板
	private JPanel jpaccount = null;	
	private JLabel account_360_jlb = null;
	private JLabel account_duoku_jlb = null;
	private JLabel account_gw_jlb = null;
	private MyComboBox account_360_jcbb = null;
	private MyComboBox account_duoku_jcbb = null;
	private MyComboBox account_gw_jcbb = null;
	//控制面板
	private JPanel jpcontrol = null;
	private JButton control_login_jb = null;
	private JButton control_buy_jb = null;
	private JButton control_meditation_jb = null;
	private JButton control_query_jb = null;
	private JButton control_strengthen_jb = null;
	private JButton control_donation_jb = null;
	//状态显示面板
	private JPanel jpstatus = null;
	
	/**
	 * 构造
	 * */
	public SingleLogin(){		
		//账号面板
		jpaccount = new JPanel();
		jpaccount.setFont(Constants.font);
		jpaccount.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"账号面板",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));		
		account_360_jlb = new JLabel("360:");
		account_360_jlb.setFont(Constants.font);
		account_duoku_jlb = new JLabel("多酷:");
		account_duoku_jlb.setFont(Constants.font);
		account_gw_jlb = new JLabel("官网:");
		account_gw_jlb.setFont(Constants.font);
		//获得配置
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
		
		//控制面板
		jpcontrol = new JPanel();
		jpcontrol.setFont(Constants.font);
		jpcontrol.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"控制面板",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		control_login_jb = new JButton("登陆");
		control_login_jb.setFont(Constants.font);
		control_buy_jb = new JButton("购买金币包");
		control_buy_jb.setFont(Constants.font);
		control_meditation_jb = new JButton("冥想");
		control_meditation_jb.setFont(Constants.font);
		control_query_jb = new JButton("查询卡牌");
		control_query_jb.setFont(Constants.font);
		control_strengthen_jb = new JButton("强化");
		control_strengthen_jb.setFont(Constants.font);
		control_donation_jb = new JButton("捐献");
		control_donation_jb.setFont(Constants.font);
		jpcontrol.add(control_login_jb);
		jpcontrol.add(control_buy_jb);
		jpcontrol.add(control_meditation_jb);
		jpcontrol.add(control_query_jb);
		jpcontrol.add(control_strengthen_jb);
		jpcontrol.add(control_donation_jb);
		//状态显示面板
		jpstatus = new JPanel();
		jpstatus.setFont(Constants.font);
		jpstatus.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Constants.lineColor,1),
				"状态显示面板",TitledBorder.LEFT,TitledBorder.TOP,Constants.font));
		//主面板上
		jptop = new JPanel();
		jptop.setFont(Constants.font);
		jptop.setLayout(new GridLayout(2,1));
		jptop.add(jpaccount);
		jptop.add(jpcontrol);
		//主面板下
		jpend = new JPanel();
		jpend.setFont(Constants.font);
		jpend.setLayout(new GridLayout(1,1));
		jpend.add(jpstatus);
		//整体容器
		Container c = this.getContentPane();
		c.setLayout(new GridLayout(2, 1));
		c.add(jptop);
		c.add(jpend);
		
		//自身设置
		Dimension displaySize = Toolkit.getDefaultToolkit().getScreenSize();//获得显示器大小对象
		this.setTitle("魔卡幻想辅助工具");
		this.setBounds((displaySize.width-900)/2, (displaySize.height-600)/2, 900, 600);
		this.setResizable(false);
		this.setIconImage(this.getToolkit().getImage(Constants.pngPath));
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//关闭退出jvm
		this.setVisible(true);
	}

	/**
	 * 监听事件
	 * */
	public void actionPerformed(ActionEvent e) {
		
	}
	
	public static void main(String[] args) {
		new SingleLogin();
	}
}
