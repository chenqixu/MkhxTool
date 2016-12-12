package client;

import java.util.List;

import bean.MonitorGrid;
import bean.ParamsMysticalcard;
import bean.json.HttpServiceReturnObjs;

public class HttpMysticalcardClientThread implements Runnable {

	protected boolean flag = false;//标志
	protected String userParad = "";
	protected String nick = "";
	protected int repeat_count = 0;//重试次数
	protected int flags = 1;//标志 0失败 1开始 2成功
	protected boolean showInfo = false;//体力为0 重新查询标记
	
	private String serverhost = "";//服务器地址
	private String params[] = null;//参数组
	private String callPara = "";//获得key和timestamp
	
	protected MonitorGrid monitor = null;//监控面板下的状态图形
	protected StringBuffer sblog = new StringBuffer("");//监控日志
	protected boolean is_end = false;//线程是否完成

	public boolean isIs_end() {
		return is_end;
	}
	
	public StringBuffer getSblog() {
		return sblog;
	}
	
	public MonitorGrid getMonitor() {
		return monitor;
	}

	public String getNick() {
		return nick;
	}

	public int getFlags() {
		return flags;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	public int getRepeat_count() {
		return repeat_count;
	}

	public void setRepeat_count() {
		this.repeat_count ++;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	
	/**
	 * 构造1
	 * */
	public HttpMysticalcardClientThread(String[] _params, String _nick, String _serverhost, String _callPara){
		this.nick = _nick;
		this.params = _params;
		this.serverhost = _serverhost;
		this.callPara = _callPara;
	}
	/**
	 * 构造2
	 * */
	public HttpMysticalcardClientThread(ParamsMysticalcard _param){
		this.nick = _param.getNick();
		this.serverhost = _param.getServerhost();
		this.callPara = _param.getCallPara();
//		PP_key key
//		PP_GS_IP s4一样 s5估计也一样
//		PP_userName PP_source UserName
//		PP_U_ID Password
//		PP_GS_CHAT_IP s4一样 s5估计也一样
//		PP_GS_DESC s4一样 s5估计也一样
//		PP_GS_NAME s4一样 s5估计也一样
//		PP_timestamp time
		this.params = new String[8];
		this.params[0] = "";//PP_key key通过查询得出
		this.params[1] = _param.getPp_gs_ip();//PP_GS_IP
		this.params[2] = _param.getPp_username();//PP_userName PP_source
		this.params[3] = _param.getPp_u_id();//PP_U_ID Password
		this.params[4] = _param.getPp_gs_chat_ip();//PP_GS_CHAT_IP
		this.params[5] = _param.getPp_gs_desc();//PP_GS_DESC
		this.params[6] = _param.getPp_gs_name();//PP_GS_NAME
		this.params[7] = "";//PP_timestamp time通过查询得出
		this.monitor = new MonitorGrid(this.nick,20,20,this);
	}
	/**
	 * 构造3 dos窗口下的构造 禁止使用monitor
	 * */
	public HttpMysticalcardClientThread(ParamsMysticalcard _param, int arg1){
		this.nick = _param.getNick();
		this.serverhost = _param.getServerhost();
		this.callPara = _param.getCallPara();
		this.params = new String[8];
		this.params[0] = "";//PP_key key通过查询得出
		this.params[1] = _param.getPp_gs_ip();//PP_GS_IP
		this.params[2] = _param.getPp_username();//PP_userName PP_source
		this.params[3] = _param.getPp_u_id();//PP_U_ID Password
		this.params[4] = _param.getPp_gs_chat_ip();//PP_GS_CHAT_IP
		this.params[5] = _param.getPp_gs_desc();//PP_GS_DESC
		this.params[6] = _param.getPp_gs_name();//PP_GS_NAME
		this.params[7] = "";//PP_timestamp time通过查询得出
	}
	/**
	 * 重置
	 * */
	public void repeated(){
		this.repeat_count = 0;
		this.flags = 1;
		this.flag = false;
		this.showInfo = false;
		this.sblog = new StringBuffer("");
		this.is_end = false;//标志线程未结束
	}

	
	/**
	 * 主线程
	 * */
	public synchronized void run(){//线程安全
		try{
			this.is_end = false;//标志线程未结束
			this.flags = 1;//开始
			if(this.monitor!=null)
				this.monitor.setType(this.flags);//改变颜色
			HttpMysticalcardClient client = new HttpMysticalcardClient(this.serverhost);
			this.sblog = client.getSb_log();//设置监控日志
			HttpServiceReturnObjs objs = client.mysticalcard_httpService(this.nick, this.callPara);//通过username和callPara获得登陆的key和timestamp
			if(objs!=null){
				this.params[0] = objs.getKey();//改变key
				this.params[7] = objs.getTimestamp();//改变timestamp
				client.setNick(this.nick);
				System.out.println("Nick:"+this.nick);
				String Cookie = client.mysticalcard_login_gf(this.params);//通过params获得登陆Cookie
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
					client.mysticalcard_dungeon_GetUserDungeon(Cookie);//判断是否可以扫荡地下城
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
					while(client.Energy>=2 && client.has_next){//while必须加上has_next 防止异常时候无法停止
						client.mysticalcard_Explore(Cookie, String.valueOf(client.max_Mapstagedetailid));//探索
						if(client.Energy<2 && !showInfo){//如果体力小于2,看看是否还有隐藏体力
							client.mysticalcard_GetUserinfo(Cookie);//通过登录Cookie获得体力
							showInfo = true;
						}
					}
					if(client.DevoteActStatus){//判断兽人商店是否开放
						client.mysticalcard_GetActStatus(Cookie);//兽人商店兑奖
					}
				}
			}
			client.getSb_log().append("一键迷宫探索完成。");
			client.getSb_log().append("\r\n");
			client.logInfo();//写日志
			this.flag = client.has_next;//执行完成,设置标记
			if(!this.flag){
				this.flags = 0;//失败,未开始
				if(this.monitor!=null)
					this.monitor.setType(3);//改变颜色
			}else{
				this.flags = 2;//成功
				if(this.monitor!=null)
					this.monitor.setType(this.flags);//改变颜色
			}
			this.is_end = true;//标志线程结束
		}catch(Exception e){
			this.flags = 0;//失败,未开始
			if(this.monitor!=null)
				this.monitor.setType(3);//改变颜色
			e.printStackTrace();
		}
	}
}
