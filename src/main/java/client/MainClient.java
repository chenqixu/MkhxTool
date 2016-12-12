package client;

import java.util.List;
//import java.util.Properties;
import java.util.Vector;

import bean.ParamsMysticalcard;

import com.frame.Constants;

/**
 * dos������һ���Թ�,̽��
 * */
public class MainClient{

	public MainClient(){		
	}
	
	public static void main(String[] args) {
//		String proxy_ip = "127.0.0.1";
//		String proxy_port = "8087";
//		Properties prop = System.getProperties();
//		//����http����Ҫʹ�õĴ���������ĵ�ַ
//		prop.setProperty("http.proxyHost", proxy_ip);
//		//����http����Ҫʹ�õĴ���������Ķ˿� 
//		prop.setProperty("http.proxyPort", proxy_port);
		
		List waitlist = new Vector();//�ȴ��б�
		try{
			Constants constants = Constants.getInstance();//��ʼ����̬������
			constants.loadConfig();//��������
			//360
			Vector<ParamsMysticalcard> v360 = constants.getList360();
			for(int j=0;j<v360.size();j++){
				Http360ClientThread thread360 = new Http360ClientThread(v360.get(j),0);
				waitlist.add(thread360);
			}
			//duoku
			Vector<ParamsMysticalcard> vduoku = constants.getListduoku();
			for(int j=0;j<vduoku.size();j++){
				HttpDuokuClientThread threadDuoku = new HttpDuokuClientThread(vduoku.get(j),0);
				waitlist.add(threadDuoku);
			}
			//gw
			Vector<ParamsMysticalcard> vgw = constants.getListgw();
			for(int j=0;j<vgw.size();j++){
				HttpMysticalcardClientThread threadMysticalcard = new HttpMysticalcardClientThread(vgw.get(j),0);
				waitlist.add(threadMysticalcard);
			}
			//��ʼִ��
			MainClientRunThread mainthread = new MainClientRunThread(waitlist);
//			mainthread.setRunprocesscnt(2);//һ�����ִ��2���߳�,Ĭ��5���߳�
			mainthread.setDos(true);//��Dos����ִ��
			mainthread.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
