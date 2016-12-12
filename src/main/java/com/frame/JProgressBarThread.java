package com.frame;

import javax.swing.JProgressBar;

import client.MainClientRunThread;

/**
 * 刷新进度条
 * */
public class JProgressBarThread implements Runnable {
	
	private MainClientRunThread mainthread = null;
	private JProgressBar jpb = null;
	private MainClient mc = null;
	
	/**
	 * 构造
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
			while((fail_count+success_count)!=all_count){//成功+失败 不等于 总数
				Thread.sleep(500);
				success_count = this.mainthread.getSuccessCount();
				fail_count = this.mainthread.getFailCount();

				this.jpb.setValue(success_count);
				this.jpb.setString("进度["+success_count+"] 失败["+fail_count+"] 总共["+all_count+"]");
				this.jpb.setStringPainted(true);
				if((fail_count+success_count)==all_count && fail_count>0){//还有失败
					this.mc.changeJFType(true, 1);
				}else if((fail_count+success_count)==all_count){//全部成功
					this.mc.changeJFType(true, 0);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
