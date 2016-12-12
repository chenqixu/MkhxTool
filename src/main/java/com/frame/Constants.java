package com.frame;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import bean.ParamsMysticalcard;

public class Constants {
	public final static String spltstr = File.separator;//文件路径分隔符(区分windows和linux)
	public final static int failrepeatcnt = 3;//失败重试次数
	public final static java.awt.Font font = new java.awt.Font("微软雅黑", 0, 13);
	public final static Color lineColor = new Color(184,207,229);
	public final static String rootPath = System.getProperty("user.dir");
	public final static String icoPath = rootPath+spltstr+"res"+spltstr+"icon_29.ico";
	public final static String pngPath = rootPath+spltstr+"res"+spltstr+"icon_29.png";
	public final static String jpgPath = rootPath+spltstr+"res"+spltstr+"icon_29.jpg";
	public final static String path360 = rootPath+spltstr+"config"+spltstr+"application_360.properties";//Constants.class.getResource("/config/application_360.properties").getFile();
	public final static String pathduoku = rootPath+spltstr+"config"+spltstr+"application_duoku.properties";//Constants.class.getResource("/config/application_duoku.properties").getFile();
	public final static String pathgw = rootPath+spltstr+"config"+spltstr+"application_mysticalcard.properties";//Constants.class.getResource("/config/application_mysticalcard.properties").getFile();
	public final static int connectTimeout = 20000;//20秒超时(毫秒)
	
	private final static String pathVersion = rootPath+spltstr+"config"+spltstr+"application_version.properties";
	private String version_360 = null;//360版本
	private String version_duoku = null;//duoku版本
	private String version_gw = null;//官网版本
	
	private static Constants cc = null;
	private Vector<ParamsMysticalcard> list360 = null;
	private Vector<ParamsMysticalcard> listduoku = null;
	private Vector<ParamsMysticalcard> listgw = null;
	
	/**
	 * 构造函数
	 * */
	private Constants(){
		loadVersion();
	}
	
	/**
	 * 单例函数
	 * */
	public static Constants getInstance(){
		if(cc==null){
			cc = new Constants();
		}
		return cc;
	}
	
	/**
	 * 载入版本配置
	 * */
	private void loadVersion(){
		File f = null;
		FileInputStream pInStream = null;
		Properties p = null;
		try{
			f = new File(Constants.pathVersion);
			pInStream = new FileInputStream(f);
			p = new Properties();
			p.load(pInStream);
			Enumeration enuVersion = p.propertyNames();
			while(enuVersion.hasMoreElements()){
				enuVersion.nextElement();
			}
			version_360 = p.getProperty("config.param1");
			version_duoku = p.getProperty("config.param2");
			version_gw = p.getProperty("config.param3");

			f = null;
			pInStream.close();
			p.clear();
			p = null;
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(f!=null)
				f = null;
			if(pInStream!=null)
				try {
					pInStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(p!=null){
				p.clear();
				p = null;
			}
		}
	}
	
	/**
	 * 载入配置文件
	 * */
	public void loadConfig(){
		File f = null;
		FileInputStream pInStream = null;
		Properties p = null;
		try{
			//360 config
			f = new File(Constants.path360);
			pInStream = new FileInputStream(f);
			p = new Properties();
			p.load(pInStream);
			list360 = null;
			list360 = new Vector<ParamsMysticalcard>();
			Enumeration enu360 = p.propertyNames();
			int i = 0;
			while(enu360.hasMoreElements()){
				i++;
				enu360.nextElement();
			}
			for(int j=1;j<=i/2;j++){
				ParamsMysticalcard config360 = new ParamsMysticalcard();
				config360.setUserParad(p.getProperty("config"+j+".userParad"));
				config360.setNick(p.getProperty("config"+j+".nick"));
				list360.add(config360);
			}
			f = null;
			pInStream.close();
			p.clear();
			p = null;

			//duoku config
			listduoku = null;
			listduoku = new Vector<ParamsMysticalcard>();
			f = new File(Constants.pathduoku);
			pInStream = new FileInputStream(f);
			p = new Properties();
			p.load(pInStream);
			Enumeration enuduoku = p.propertyNames();
			i = 0;
			while(enuduoku.hasMoreElements()){
				i++;
				enuduoku.nextElement();	
			}
			for(int j=1;j<=i/3;j++){
				ParamsMysticalcard configduoku = new ParamsMysticalcard();
				configduoku.setUserParad(p.getProperty("config"+j+".userParad"));
				configduoku.setNick(p.getProperty("config"+j+".nick"));
				configduoku.setSdklogin(p.getProperty("config"+j+".sdklogin"));
				listduoku.add(configduoku);
			}
			f = null;
			pInStream.close();
			p.clear();
			p = null;
			
			//mysticalcard config
			listgw = null;
			listgw = new Vector<ParamsMysticalcard>();
			f = new File(Constants.pathgw);
			pInStream = new FileInputStream(f);
			p = new Properties();
			p.load(pInStream);
			Enumeration enumy = p.propertyNames();
			i = 0;
			while(enumy.hasMoreElements()){
				i++;
				enumy.nextElement();			
			}
			for(int j=1;j<=i/9;j++){
				ParamsMysticalcard configgw = new ParamsMysticalcard();
				configgw.setCallPara(p.getProperty("config"+j+".callPara"));
				configgw.setNick(p.getProperty("config"+j+".nick"));
				configgw.setPp_gs_chat_ip(p.getProperty("config"+j+".pp_gs_chat_ip"));
				configgw.setPp_gs_desc(p.getProperty("config"+j+".pp_gs_desc"));
				configgw.setPp_gs_ip(p.getProperty("config"+j+".pp_gs_ip"));
				configgw.setPp_gs_name(p.getProperty("config"+j+".pp_gs_name"));
				configgw.setPp_u_id(p.getProperty("config"+j+".pp_u_id"));
				configgw.setPp_username(p.getProperty("config"+j+".pp_username"));
				configgw.setServerhost(p.getProperty("config"+j+".serverhost"));
				listgw.add(configgw);
			}
			f = null;
			pInStream.close();
			p.clear();
			p = null;			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(f!=null)
				f = null;
			if(pInStream!=null)
				try {
					pInStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			if(p!=null){
				p.clear();
				p = null;
			}
		}
	}

	public Vector<ParamsMysticalcard> getList360() {
		return list360;
	}

	public Vector<ParamsMysticalcard> getListduoku() {
		return listduoku;
	}

	public Vector<ParamsMysticalcard> getListgw() {
		return listgw;
	}

	public String getVersion_360() {
		return version_360;
	}

	public String getVersion_duoku() {
		return version_duoku;
	}

	public String getVersion_gw() {
		return version_gw;
	}
}
