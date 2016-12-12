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
 * ϵͳ����
 * */
public class MainTray implements ActionListener {
	
	private MainClient mc = null;
	private boolean showflag = true;
	/**
	 * ���캯��
	 * */
	public MainTray(){
		mc = MainClient.getInstance();//new MainClient();
	}
	
	/**
	 * ��ʼ��
	 * */
	public void Ini(){
		TrayIcon trayIcon = null;//����
		if(SystemTray.isSupported()){//�ж�ϵͳ�Ƿ�֧��ϵͳ����
			SystemTray tray = SystemTray.getSystemTray(); // ����ϵͳ����
			Image image = Toolkit.getDefaultToolkit().getImage(Constants.jpgPath);//����ͼƬ

			//���������˵�
			PopupMenu popup = new PopupMenu();
			//������ѡ��
			MenuItem mainFrameItem = new MenuItem("��ʾ/����");
			mainFrameItem.setFont(Constants.font);
			mainFrameItem.setActionCommand("mainFrame");
			mainFrameItem.addActionListener(this);
			//�˳�����ѡ��
			MenuItem exitItem = new MenuItem("�˳�����");
			exitItem.setFont(Constants.font);
			exitItem.setActionCommand("exitItem");
			exitItem.addActionListener(this);
			
			popup.add(mainFrameItem);
			popup.add(exitItem);

			trayIcon = new TrayIcon(image, "ħ�����븨������", popup);//����trayIcon
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
	 * �����¼�
	 * */
	public void actionPerformed(ActionEvent e) {
		String acommand = e.getActionCommand();
		if(acommand.equals("mainFrame")){//������
			this.showflag = this.mc.isVisible();//�Ƿ���ʾ
			if(this.showflag){
				this.showflag = false;
				this.mc.setVisible(false);//����ʾ
			}else{
				this.showflag = true;
				this.mc.setVisible(true);//��ʾ
			}
		}else if(acommand.equals("exitItem")){//�˳�����
			if(JOptionPane.showConfirmDialog(null, "ȷ���˳�ħ�����븨������") == 0) {
				System.exit(0);
			}
		}else if(acommand.equals("trayIcon")){//����			
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
