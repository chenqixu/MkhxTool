package com.frame;

import javax.swing.JProgressBar;

import client.MainClientRunThread;

/**
 * ˢ�½�����
 * */
public class JProgressBarThread implements Runnable {
	
	private MainClientRunThread mainthread = null;
	private JProgressBar jpb = null;
	private MainClient mc = null;
	
	/**
	 * ����
	 * */
	public JProgressBarThread(MainClientRunThread _mainthread, MainClient _mc, JProgressBar _jpb){
		this.mainthread = _mainthread;
		this.jpb = _jpb;
		this.mc = _mc;
	}

	public synchronized void run() {
		try{
			int all_count = this.mainthread.getAlltaskcount();
			int success_count = this.mainthread.getSuccessCount();
			int fail_count = this.mainthread.getFailCount();
			while((fail_count+success_count)!=all_count){//�ɹ�+ʧ�� ������ ����
				Thread.sleep(500);
				success_count = this.mainthread.getSuccessCount();
				fail_count = this.mainthread.getFailCount();

				this.jpb.setValue(success_count);
				this.jpb.setString("����["+success_count+"] ʧ��["+fail_count+"] �ܹ�["+all_count+"]");
				this.jpb.setStringPainted(true);
				if((fail_count+success_count)==all_count && fail_count>0){//����ʧ��
					this.mc.changeJFType(true, 1);
				}else if((fail_count+success_count)==all_count){//ȫ���ɹ�
					this.mc.changeJFType(true, 0);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
