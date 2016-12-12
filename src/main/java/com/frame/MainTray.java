package com.frame;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

/**
 * 系统托盘
 * */
public class MainTray implements ActionListener {
	
	private MainClient mc = null;
	private boolean showflag = true;
	/**
	 * 构造函数
	 * */
	public MainTray(){
		mc = MainClient.getInstance();//new MainClient();
	}
	
	/**
	 * 初始化
	 * */
	public void Ini(){
		TrayIcon trayIcon = null;//托盘
		if(SystemTray.isSupported()){//判断系统是否支持系统托盘
			SystemTray tray = SystemTray.getSystemTray(); // 创建系统托盘
			Image image = Toolkit.getDefaultToolkit().getImage(Constants.jpgPath);//载入图片

			//创建弹出菜单
			PopupMenu popup = new PopupMenu();
			//主界面选项
			MenuItem mainFrameItem = new MenuItem("显示/隐藏");
			mainFrameItem.setFont(Constants.font);
			mainFrameItem.setActionCommand("mainFrame");
			mainFrameItem.addActionListener(this);
			//退出程序选项
			MenuItem exitItem = new MenuItem("退出程序");
			exitItem.setFont(Constants.font);
			exitItem.setActionCommand("exitItem");
			exitItem.addActionListener(this);
			
			popup.add(mainFrameItem);
			popup.add(exitItem);

			trayIcon = new TrayIcon(image, "魔卡幻想辅助工具", popup);//创建trayIcon
			trayIcon.setActionCommand("trayIcon");
			trayIcon.addActionListener(this);
			try {
				tray.add(trayIcon);
			} catch (AWTException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 监听事件
	 * */
	public void actionPerformed(ActionEvent e) {
		String acommand = e.getActionCommand();
		if(acommand.equals("mainFrame")){//主界面
			this.showflag = this.mc.isVisible();//是否显示
			if(this.showflag){
				this.showflag = false;
				this.mc.setVisible(false);//不显示
			}else{
				this.showflag = true;
				this.mc.setVisible(true);//显示
			}
		}else if(acommand.equals("exitItem")){//退出程序
			if(JOptionPane.showConfirmDialog(null, "确定退出魔卡幻想辅助工具") == 0) {
				System.exit(0);
			}
		}else if(acommand.equals("trayIcon")){//托盘			
		}
	}

	/**
	 * main
	 * */
	public static void main(String[] args) {
		MainTray mt = new MainTray();
		mt.Ini();
	}
}
