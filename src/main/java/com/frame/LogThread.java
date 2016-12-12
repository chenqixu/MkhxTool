package com.frame;

import javax.swing.JTextArea;

import client.Http360ClientThread;
import client.HttpDuokuClientThread;
import client.HttpMysticalcardClientThread;

/**
 * 日志监视进程
 * */
public class LogThread implements Runnable {
	
	private StringBuffer sblog = null;
	private JTextArea jta = null;
	private boolean flag = true;
	private Object threadobj = null;
	
	public void setThreadobj(Object threadobj) {
		this.threadobj = threadobj;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public void setSblog(StringBuffer sblog) {
		this.sblog = sblog;
	}

	public LogThread(JTextArea _jta){
		this.jta = _jta;
	}

	public synchronized void run() {
		try{
			synchronized(this.jta){
				while(this.flag){
					Thread.sleep(500);
					if(this.jta!=null && this.sblog!=null && this.sblog.toString().length()>0){
						this.jta.setText(util.StringUtil.unicodeToString(this.sblog.toString()));
						this.jta.selectAll();

						//线程结束 或者 重试超过指定次数
						if(this.threadobj instanceof Http360ClientThread){
							if(((Http360ClientThread)this.threadobj).isIs_end() ||
									((Http360ClientThread)this.threadobj).getRepeat_count()==Constants.failrepeatcnt){
								this.flag = false;
							}
						}else if(this.threadobj instanceof HttpDuokuClientThread){
							if(((HttpDuokuClientThread)this.threadobj).isIs_end() ||
									((HttpDuokuClientThread)this.threadobj).getRepeat_count()==Constants.failrepeatcnt){
								this.flag = false;
							}
						}else if(this.threadobj instanceof HttpMysticalcardClientThread){
							if(((HttpMysticalcardClientThread)this.threadobj).isIs_end() ||
									((HttpMysticalcardClientThread)this.threadobj).getRepeat_count()==Constants.failrepeatcnt){
								this.flag = false;
							}
						}
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
