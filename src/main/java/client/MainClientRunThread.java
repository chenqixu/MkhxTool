package client;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import util.Mail;

import com.frame.Constants;

public class MainClientRunThread extends Thread {
	
	private List runlist = new Vector();//ִ���б�
	private List waitlist = null;//�ȴ��б�
	private List successlist = new Vector();//�ɹ��б�
	private List faillist = new Vector();//ʧ���б�
	private final int failrepeatcnt = Constants.failrepeatcnt;//ʧ�����Դ���
	private int runprocesscnt = 5;//һ�����ִ���߳�����
	private final int runprocessinterval = 500;//ѭ���ļ�� ��λ:����
	private final int succstatus = 2;//�ɹ�״̬
	private final int failstatus = 0;//ʧ�� δ��ʼ״̬
	private int alltaskcount = 0;//������������
	private boolean isDos = false;//�Ƿ���dos������ִ��,�����,��Ҫ�����ʼ�����
	
	/**
	 * ���캯�� ����ȴ��б�
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
	 * ��óɹ����������
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
	 * ���ʧ�����������
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
	 * ���������������
	 * */
	public int getAlltaskcount() {
		return alltaskcount;
	}

	public synchronized void run(){
		synchronized (waitlist){
			try{
				//����ȴ����в�Ϊ0 ���� ִ�ж��в�Ϊ0
				while(waitlist.size()>0 || runlist.size()>0){
					Thread.sleep(runprocessinterval);
					//���Ӷ���
					int runcnt = runprocesscnt - runlist.size();
					for(int i=0;i<runcnt;i++){
//						System.out.println("���Ӷ��д��� runlist.size():"+runlist.size());
						java.util.Iterator it = waitlist.iterator();
						if(it.hasNext()){
							Object obj = it.next();
							if(obj instanceof Http360ClientThread){
								//�ж��Ƿ�����3��,����3�ξͷ���,д��ʧ�ܶ���
								if(((Http360ClientThread)obj).getRepeat_count()>=failrepeatcnt){//�������3��
									waitlist.remove(obj);//�ӵȴ������Ƴ�
									it = waitlist.iterator();
									faillist.add(obj);//���ӵ�ʧ�ܶ���,��������
								}else{
									new Thread((Http360ClientThread)obj).start();
									runlist.add(obj);//ִ�ж���
									waitlist.remove(obj);//�ӵȴ������Ƴ�
									it = waitlist.iterator();
								}
							}
							if(obj instanceof HttpDuokuClientThread){
								if(((HttpDuokuClientThread)obj).getRepeat_count()>=failrepeatcnt){//�������3��
									waitlist.remove(obj);//�ӵȴ������Ƴ�
									it = waitlist.iterator();
									faillist.add(obj);//���ӵ�ʧ�ܶ���,��������
								}else{
									new Thread((HttpDuokuClientThread)obj).start();
									runlist.add(obj);//ִ�ж���
									waitlist.remove(obj);//�ӵȴ������Ƴ�
									it = waitlist.iterator();
								}
							}
							if(obj instanceof HttpMysticalcardClientThread){	
								if(((HttpMysticalcardClientThread)obj).getRepeat_count()>=failrepeatcnt){//�������3��
									waitlist.remove(obj);//�ӵȴ������Ƴ�
									it = waitlist.iterator();
									faillist.add(obj);//���ӵ�ʧ�ܶ���,��������
								}else{
									new Thread((HttpMysticalcardClientThread)obj).start();
									runlist.add(obj);//ִ�ж���
									waitlist.remove(obj);//�ӵȴ������Ƴ�
									it = waitlist.iterator();
								}
							}
						}
					}
					//�ж��Ƿ�ִ����ɶ���
					Thread.sleep(runprocessinterval);
					if(runlist.size()>0){
//						System.out.println("�ж��Ƿ�ִ����ɶ���");
						java.util.Iterator it = runlist.iterator();
						while(it.hasNext()){
							Object obj = it.next();
							if(obj instanceof Http360ClientThread){
								if(((Http360ClientThread)obj).getFlags() == succstatus){//�ɹ�
									//��ִ�ж����Ƴ�
									runlist.remove(obj);
									it = runlist.iterator();
									//���ӵ��ɹ�����
									successlist.add(obj);
								}else if(((Http360ClientThread)obj).getFlags() == failstatus){//ʧ��,δ��ʼ
									//���Դ���+1
									((Http360ClientThread)obj).setRepeat_count();
									//��ִ�ж����Ƴ�
									runlist.remove(obj);
									it = runlist.iterator();
									//���ӵ��ȴ�����ĩβ
									waitlist.add(obj);
									System.out.println(((Http360ClientThread)obj).getNick()+"��ִ�ж����Ƴ� ���ӵ��ȴ�����ĩβ ׼����"+((Http360ClientThread)obj).getRepeat_count()+"������");
								}
							}
							if(obj instanceof HttpDuokuClientThread){
								if(((HttpDuokuClientThread)obj).getFlags() == succstatus){//�ɹ�
									//��ִ�ж����Ƴ�
									runlist.remove(obj);
									it = runlist.iterator();
									//���ӵ��ɹ�����
									successlist.add(obj);
								}else if(((HttpDuokuClientThread)obj).getFlags() == failstatus){//ʧ��,δ��ʼ
									//���Դ���+1
									((HttpDuokuClientThread)obj).setRepeat_count();
									//��ִ�ж����Ƴ�
									runlist.remove(obj);
									it = runlist.iterator();
									//���ӵ��ȴ�����ĩβ
									waitlist.add(obj);
									System.out.println(((HttpDuokuClientThread)obj).getNick()+"��ִ�ж����Ƴ� ���ӵ��ȴ�����ĩβ ׼����"+((HttpDuokuClientThread)obj).getRepeat_count()+"������");
								}
							}
							if(obj instanceof HttpMysticalcardClientThread){
								if(((HttpMysticalcardClientThread)obj).getFlags() == succstatus){//�ɹ�
									//��ִ�ж����Ƴ�
									runlist.remove(obj);
									it = runlist.iterator();
									//���ӵ��ɹ�����
									successlist.add(obj);
								}else if(((HttpMysticalcardClientThread)obj).getFlags() == failstatus){//ʧ��,δ��ʼ
									//���Դ���+1
									((HttpMysticalcardClientThread)obj).setRepeat_count();
									//��ִ�ж����Ƴ�
									runlist.remove(obj);
									it = runlist.iterator();
									//���ӵ��ȴ�����ĩβ
									waitlist.add(obj);
									System.out.println(((HttpMysticalcardClientThread)obj).getNick()+"��ִ�ж����Ƴ� ���ӵ��ȴ�����ĩβ ׼����"+((HttpMysticalcardClientThread)obj).getRepeat_count()+"������");
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
						System.out.println("�ɹ�����:"+((Http360ClientThread)obj).getNick());
						sb_result.append("�ɹ�����:"+((Http360ClientThread)obj).getNick());
						sb_result.append("<br>");
					}
					if(obj instanceof HttpDuokuClientThread){
						System.out.println("�ɹ�����:"+((HttpDuokuClientThread)obj).getNick());
						sb_result.append("�ɹ�����:"+((HttpDuokuClientThread)obj).getNick());
						sb_result.append("<br>");
					}
					if(obj instanceof HttpMysticalcardClientThread){
						System.out.println("�ɹ�����:"+((HttpMysticalcardClientThread)obj).getNick());
						sb_result.append("�ɹ�����:"+((HttpMysticalcardClientThread)obj).getNick());
						sb_result.append("<br>");
					}
				}
				java.util.Iterator failit = faillist.iterator();
				List<String> failfjlist = new Vector<String>();//ʧ�ܵĸ����б�
				while(failit.hasNext()){
					Object obj = failit.next();
					if(obj instanceof Http360ClientThread){
						System.out.println("ʧ�ܶ���:"+((Http360ClientThread)obj).getNick());
						sb_result.append("ʧ�ܶ���:"+((Http360ClientThread)obj).getNick());
						sb_result.append("<br>");
						String filename = Constants.rootPath+Constants.spltstr+"ALL-LOGS"+Constants.spltstr+((Http360ClientThread)obj).getNick()+".txt";
						failfjlist.add(filename);
					}
					if(obj instanceof HttpDuokuClientThread){
						System.out.println("ʧ�ܶ���:"+((HttpDuokuClientThread)obj).getNick());
						sb_result.append("ʧ�ܶ���:"+((HttpDuokuClientThread)obj).getNick());
						sb_result.append("<br>");
						String filename = Constants.rootPath+Constants.spltstr+"ALL-LOGS"+Constants.spltstr+((HttpDuokuClientThread)obj).getNick()+".txt";
						failfjlist.add(filename);
					}
					if(obj instanceof HttpMysticalcardClientThread){
						System.out.println("ʧ�ܶ���:"+((HttpMysticalcardClientThread)obj).getNick());
						sb_result.append("ʧ�ܶ���:"+((HttpMysticalcardClientThread)obj).getNick());
						sb_result.append("<br>");
						String filename = Constants.rootPath+Constants.spltstr+"ALL-LOGS"+Constants.spltstr+((HttpMysticalcardClientThread)obj).getNick()+".txt";
						failfjlist.add(filename);
					}
				}
				if(this.isDos){//��Dos����ִ��
					//�����ʼ�
					SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					Date n1 = new Date();
					String nn1 = format1.format(n1);
					Mail.getInstance().SendMailByParam("ħ�����븨�����߱���["+nn1+"]", sb_result.toString(), failfjlist);					
				}
				System.gc();//�ͷ�û���ڴ�
				if(this.isDos){//��Dos����ִ��
					System.exit(0);//�˳�
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
}
