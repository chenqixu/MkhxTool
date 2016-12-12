package client;

import java.util.List;
//import java.util.Properties;
import java.util.Vector;

import bean.ParamsMysticalcard;

import com.frame.Constants;

/**
 * dos窗口下一键迷宫,探索
 * */
public class MainClient{

	public MainClient(){		
	}
	
	public static void main(String[] args) {
//		String proxy_ip = "127.0.0.1";
//		String proxy_port = "8087";
//		Properties prop = System.getProperties();
//		//设置http访问要使用的代理服务器的地址
//		prop.setProperty("http.proxyHost", proxy_ip);
//		//设置http访问要使用的代理服务器的端口 
//		prop.setProperty("http.proxyPort", proxy_port);
		
		List waitlist = new Vector();//等待列表
		try{
			Constants constants = Constants.getInstance();//初始化静态公共类
			constants.loadConfig();//加载配置
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
			//开始执行
			MainClientRunThread mainthread = new MainClientRunThread(waitlist);
//			mainthread.setRunprocesscnt(2);//一次最多执行2个线程,默认5个线程
			mainthread.setDos(true);//纯Dos界面执行
			mainthread.start();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
