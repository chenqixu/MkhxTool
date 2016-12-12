package client;

import java.util.List;

public class Http91ClientThread implements Runnable {

	protected boolean flag = false;//标志
	protected String userParad = "";
	protected String nick = "";
	protected int repeat_count = 0;//重试次数
	protected int flags = 1;//标志 0失败 1开始 2成功
	protected boolean showInfo = false;//体力为0 重新查询标记

	/**
	 * 构造1
	 * */
	public Http91ClientThread(String _userParad, String _nick){
		this.userParad = _userParad;
		this.nick = _nick;
	}
	
	/**
	 * 主线程
	 * */
	public synchronized void run(){//线程安全
		try{
			this.flags = 1;//开始
			Http91Client client = new Http91Client();
			client.setNick(this.nick);
			String Cookie = client.mysticalcard_login(this.userParad);//通过userParad获得登陆Cookie
			if(Cookie!=null && Cookie.length()>0){
				System.out.println("Cookie:"+Cookie);
				client.mysticalcard_GetUserinfo(Cookie);//通过登录Cookie获得体力
				client.mysticalcard_GetLoginAwardType(Cookie);//通过登录Cookie签到
				client.mysticalcard_GetUserSalary(Cookie);//通过登录Cookie查询薪水
				client.mysticalcard_AwardSalary(Cookie);//通过登录Cookie获得薪水
				List<String> MapStageslist = client.mysticalcard_GetUserMapStages(Cookie);//通过登录Cookie获得入侵
				for(int j=0;j<MapStageslist.size();j++){
					client.mysticalcard_EditUserMapStages(Cookie, MapStageslist.get(j));//通过登录Cookie消灭入侵
				}
				if(client.Energy<2){//如果体力小于2,看看是否还有隐藏体力
					client.mysticalcard_GetUserinfo(Cookie);//通过登录Cookie获得体力
					showInfo = true;
				}
				if(client.isopen8 && client.Energy>=2){//8塔是否开启
					client.autoBattle("8", Cookie, client);
				}
				if(client.Energy<2 && !showInfo){//如果体力小于2,看看是否还有隐藏体力
					client.mysticalcard_GetUserinfo(Cookie);//通过登录Cookie获得体力
					showInfo = true;
				}
				if(client.isopen7 && client.Energy>=2){//7塔是否开启
					client.autoBattle("7", Cookie, client);
				}
				if(client.Energy<2 && !showInfo){//如果体力小于2,看看是否还有隐藏体力
					client.mysticalcard_GetUserinfo(Cookie);//通过登录Cookie获得体力
					showInfo = true;
				}
				if(client.isopen6 && client.Energy>=2){//6塔是否开启
					client.autoBattle("6", Cookie, client);
				}
				if(client.Energy<2 && !showInfo){//如果体力小于2,看看是否还有隐藏体力
					client.mysticalcard_GetUserinfo(Cookie);//通过登录Cookie获得体力
					showInfo = true;
				}
				if(client.isopen5 && client.Energy>=2){//5塔是否开启
					client.autoBattle("5", Cookie, client);
				}
				if(client.Energy<2 && !showInfo){//如果体力小于2,看看是否还有隐藏体力
					client.mysticalcard_GetUserinfo(Cookie);//通过登录Cookie获得体力
					showInfo = true;
				}
				//探索
				while(client.Energy>=2){
					client.mysticalcard_Explore(Cookie, String.valueOf(client.max_Mapstagedetailid));//探索
					if(client.Energy<2 && !showInfo){//如果体力小于2,看看是否还有隐藏体力
						client.mysticalcard_GetUserinfo(Cookie);//通过登录Cookie获得体力
						showInfo = true;
					}
				}
			}				
			client.logInfo();//写日志
			this.flag = client.has_next;//执行完成,设置标记
			if(!this.flag){
				this.flags = 0;//失败,未开始
			}else{
				this.flags = 2;//成功
			}
		}catch(Exception e){
			this.flags = 0;//失败,未开始
			e.printStackTrace();
		}
	}
}
