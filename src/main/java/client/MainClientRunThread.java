package client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import util.Mail;

import com.frame.Constants;

public class MainClientRunThread extends Thread {
	
	private List runlist = new Vector();//执行列表
	private List waitlist = null;//等待列表
	private List successlist = new Vector();//成功列表
	private List faillist = new Vector();//失败列表
	private final int failrepeatcnt = Constants.failrepeatcnt;//失败重试次数
	private int runprocesscnt = 5;//一次最多执行线程数量
	private final int runprocessinterval = 500;//循环的间隔 单位:毫秒
	private final int succstatus = 2;//成功状态
	private final int failstatus = 0;//失败 未开始状态
	private int alltaskcount = 0;//所有任务数量
	private boolean isDos = false;//是否在dos界面下执行,如果是,则要发送邮件提醒
	
	/**
	 * 构造函数 传入等待列表
	 * */
	public MainClientRunThread(List _waitlist){
		if(_waitlist!=null){
			this.alltaskcount = _waitlist.size();
			this.waitlist = _waitlist;
		}else{
			this.waitlist = new Vector();
			this.alltaskcount = 0;
		}
	}
	
	public void setDos(boolean isDos) {
		this.isDos = isDos;
	}

	public void setRunprocesscnt(int runprocesscnt) {
		this.runprocesscnt = runprocesscnt;
	}

	public List getFaillist() {
		return faillist;
	}

	public List getRunlist() {
		return runlist;
	}

	public List getSuccesslist() {
		return successlist;
	}

	public List getWaitlist() {
		return waitlist;
	}
	
	/**
	 * 获得成功任务的数量
	 * */
	public int getSuccessCount(){
		synchronized(successlist){
			if(successlist!=null)
				return successlist.size();
			else
				return 0;
		}
	}

	/**
	 * 获得失败任务的数量
	 * */
	public int getFailCount(){
		synchronized(faillist){
			if(faillist!=null)
				return faillist.size();
			else
				return 0;
		}
	}

	/**
	 * 获得所有任务数量
	 * */
	public int getAlltaskcount() {
		return alltaskcount;
	}

	public synchronized void run(){
		synchronized (waitlist){
			try{
				//如果等待队列不为0 或者 执行队列不为0
				while(waitlist.size()>0 || runlist.size()>0){
					Thread.sleep(runprocessinterval);
					//增加队列
					int runcnt = runprocesscnt - runlist.size();
					for(int i=0;i<runcnt;i++){
//						System.out.println("增加队列处理 runlist.size():"+runlist.size());
						java.util.Iterator it = waitlist.iterator();
						if(it.hasNext()){
							Object obj = it.next();
							if(obj instanceof Http360ClientThread){
								//判断是否重试3次,重试3次就放弃,写入失败队列
								if(((Http360ClientThread)obj).getRepeat_count()>=failrepeatcnt){//最多重试3次
									waitlist.remove(obj);//从等待队列移除
									it = waitlist.iterator();
									faillist.add(obj);//增加到失败队列,不做处理
								}else{
									new Thread((Http360ClientThread)obj).start();
									runlist.add(obj);//执行队列
									waitlist.remove(obj);//从等待队列移除
									it = waitlist.iterator();
								}
							}
							if(obj instanceof HttpDuokuClientThread){
								if(((HttpDuokuClientThread)obj).getRepeat_count()>=failrepeatcnt){//最多重试3次
									waitlist.remove(obj);//从等待队列移除
									it = waitlist.iterator();
									faillist.add(obj);//增加到失败队列,不做处理
								}else{
									new Thread((HttpDuokuClientThread)obj).start();
									runlist.add(obj);//执行队列
									waitlist.remove(obj);//从等待队列移除
									it = waitlist.iterator();
								}
							}
							if(obj instanceof HttpMysticalcardClientThread){	
								if(((HttpMysticalcardClientThread)obj).getRepeat_count()>=failrepeatcnt){//最多重试3次
									waitlist.remove(obj);//从等待队列移除
									it = waitlist.iterator();
									faillist.add(obj);//增加到失败队列,不做处理
								}else{
									new Thread((HttpMysticalcardClientThread)obj).start();
									runlist.add(obj);//执行队列
									waitlist.remove(obj);//从等待队列移除
									it = waitlist.iterator();
								}
							}
						}
					}
					//判断是否执行完成队列
					Thread.sleep(runprocessinterval);
					if(runlist.size()>0){
//						System.out.println("判断是否执行完成队列");
						java.util.Iterator it = runlist.iterator();
						while(it.hasNext()){
							Object obj = it.next();
							if(obj instanceof Http360ClientThread){
								if(((Http360ClientThread)obj).getFlags() == succstatus){//成功
									//从执行队列移除
									runlist.remove(obj);
									it = runlist.iterator();
									//增加到成功队列
									successlist.add(obj);
								}else if(((Http360ClientThread)obj).getFlags() == failstatus){//失败,未开始
									//重试次数+1
									((Http360ClientThread)obj).setRepeat_count();
									//从执行队列移除
									runlist.remove(obj);
									it = runlist.iterator();
									//增加到等待队列末尾
									waitlist.add(obj);
									System.out.println(((Http360ClientThread)obj).getNick()+"从执行队列移除 增加到等待队列末尾 准备第"+((Http360ClientThread)obj).getRepeat_count()+"次重试");
								}
							}
							if(obj instanceof HttpDuokuClientThread){
								if(((HttpDuokuClientThread)obj).getFlags() == succstatus){//成功
									//从执行队列移除
									runlist.remove(obj);
									it = runlist.iterator();
									//增加到成功队列
									successlist.add(obj);
								}else if(((HttpDuokuClientThread)obj).getFlags() == failstatus){//失败,未开始
									//重试次数+1
									((HttpDuokuClientThread)obj).setRepeat_count();
									//从执行队列移除
									runlist.remove(obj);
									it = runlist.iterator();
									//增加到等待队列末尾
									waitlist.add(obj);
									System.out.println(((HttpDuokuClientThread)obj).getNick()+"从执行队列移除 增加到等待队列末尾 准备第"+((HttpDuokuClientThread)obj).getRepeat_count()+"次重试");
								}
							}
							if(obj instanceof HttpMysticalcardClientThread){
								if(((HttpMysticalcardClientThread)obj).getFlags() == succstatus){//成功
									//从执行队列移除
									runlist.remove(obj);
									it = runlist.iterator();
									//增加到成功队列
									successlist.add(obj);
								}else if(((HttpMysticalcardClientThread)obj).getFlags() == failstatus){//失败,未开始
									//重试次数+1
									((HttpMysticalcardClientThread)obj).setRepeat_count();
									//从执行队列移除
									runlist.remove(obj);
									it = runlist.iterator();
									//增加到等待队列末尾
									waitlist.add(obj);
									System.out.println(((HttpMysticalcardClientThread)obj).getNick()+"从执行队列移除 增加到等待队列末尾 准备第"+((HttpMysticalcardClientThread)obj).getRepeat_count()+"次重试");
								}
							}
						}
					}
				}
				
				StringBuffer sb_result = new StringBuffer("");				
				java.util.Iterator succit = successlist.iterator();
				while(succit.hasNext()){
					Object obj = succit.next();
					if(obj instanceof Http360ClientThread){
						System.out.println("成功队列:"+((Http360ClientThread)obj).getNick());
						sb_result.append("成功队列:"+((Http360ClientThread)obj).getNick());
						sb_result.append("<br>");
					}
					if(obj instanceof HttpDuokuClientThread){
						System.out.println("成功队列:"+((HttpDuokuClientThread)obj).getNick());
						sb_result.append("成功队列:"+((HttpDuokuClientThread)obj).getNick());
						sb_result.append("<br>");
					}
					if(obj instanceof HttpMysticalcardClientThread){
						System.out.println("成功队列:"+((HttpMysticalcardClientThread)obj).getNick());
						sb_result.append("成功队列:"+((HttpMysticalcardClientThread)obj).getNick());
						sb_result.append("<br>");
					}
				}
				java.util.Iterator failit = faillist.iterator();
				List<String> failfjlist = new Vector<String>();//失败的附件列表
				while(failit.hasNext()){
					Object obj = failit.next();
					if(obj instanceof Http360ClientThread){
						System.out.println("失败队列:"+((Http360ClientThread)obj).getNick());
						sb_result.append("失败队列:"+((Http360ClientThread)obj).getNick());
						sb_result.append("<br>");
						String filename = Constants.rootPath+Constants.spltstr+"ALL-LOGS"+Constants.spltstr+((Http360ClientThread)obj).getNick()+".txt";
						failfjlist.add(filename);
					}
					if(obj instanceof HttpDuokuClientThread){
						System.out.println("失败队列:"+((HttpDuokuClientThread)obj).getNick());
						sb_result.append("失败队列:"+((HttpDuokuClientThread)obj).getNick());
						sb_result.append("<br>");
						String filename = Constants.rootPath+Constants.spltstr+"ALL-LOGS"+Constants.spltstr+((HttpDuokuClientThread)obj).getNick()+".txt";
						failfjlist.add(filename);
					}
					if(obj instanceof HttpMysticalcardClientThread){
						System.out.println("失败队列:"+((HttpMysticalcardClientThread)obj).getNick());
						sb_result.append("失败队列:"+((HttpMysticalcardClientThread)obj).getNick());
						sb_result.append("<br>");
						String filename = Constants.rootPath+Constants.spltstr+"ALL-LOGS"+Constants.spltstr+((HttpMysticalcardClientThread)obj).getNick()+".txt";
						failfjlist.add(filename);
					}
				}
				if(this.isDos){//纯Dos界面执行
					//发送邮件
					SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date n1 = new Date();
					String nn1 = format1.format(n1);
					Mail.getInstance().SendMailByParam("魔卡幻想辅助工具报告["+nn1+"]", sb_result.toString(), failfjlist);					
				}
				System.gc();//释放没用内存
				if(this.isDos){//纯Dos界面执行
					System.exit(0);//退出
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
