package client;

import java.util.List;

import bean.MonitorGrid;
import bean.ParamsMysticalcard;
import bean.json.HttpServiceReturnObjs;

public class HttpMysticalcardClientThread implements Runnable {

	protected boolean flag = false;//��־
	protected String userParad = "";
	protected String nick = "";
	protected int repeat_count = 0;//���Դ���
	protected int flags = 1;//��־ 0ʧ�� 1��ʼ 2�ɹ�
	protected boolean showInfo = false;//����Ϊ0 ���²�ѯ���
	
	private String serverhost = "";//��������ַ
	private String params[] = null;//������
	private String callPara = "";//���key��timestamp
	
	protected MonitorGrid monitor = null;//�������µ�״̬ͼ��
	protected StringBuffer sblog = new StringBuffer("");//�����־
	protected boolean is_end = false;//�߳��Ƿ����

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
	 * ����1
	 * */
	public HttpMysticalcardClientThread(String[] _params, String _nick, String _serverhost, String _callPara){
		this.nick = _nick;
		this.params = _params;
		this.serverhost = _serverhost;
		this.callPara = _callPara;
	}
	/**
	 * ����2
	 * */
	public HttpMysticalcardClientThread(ParamsMysticalcard _param){
		this.nick = _param.getNick();
		this.serverhost = _param.getServerhost();
		this.callPara = _param.getCallPara();
//		PP_key key
//		PP_GS_IP s4һ�� s5����Ҳһ��
//		PP_userName PP_source UserName
//		PP_U_ID Password
//		PP_GS_CHAT_IP s4һ�� s5����Ҳһ��
//		PP_GS_DESC s4һ�� s5����Ҳһ��
//		PP_GS_NAME s4һ�� s5����Ҳһ��
//		PP_timestamp time
		this.params = new String[8];
		this.params[0] = "";//PP_key keyͨ����ѯ�ó�
		this.params[1] = _param.getPp_gs_ip();//PP_GS_IP
		this.params[2] = _param.getPp_username();//PP_userName PP_source
		this.params[3] = _param.getPp_u_id();//PP_U_ID Password
		this.params[4] = _param.getPp_gs_chat_ip();//PP_GS_CHAT_IP
		this.params[5] = _param.getPp_gs_desc();//PP_GS_DESC
		this.params[6] = _param.getPp_gs_name();//PP_GS_NAME
		this.params[7] = "";//PP_timestamp timeͨ����ѯ�ó�
		this.monitor = new MonitorGrid(this.nick,20,20,this);
	}
	/**
	 * ����3 dos�����µĹ��� ��ֹʹ��monitor
	 * */
	public HttpMysticalcardClientThread(ParamsMysticalcard _param, int arg1){
		this.nick = _param.getNick();
		this.serverhost = _param.getServerhost();
		this.callPara = _param.getCallPara();
		this.params = new String[8];
		this.params[0] = "";//PP_key keyͨ����ѯ�ó�
		this.params[1] = _param.getPp_gs_ip();//PP_GS_IP
		this.params[2] = _param.getPp_username();//PP_userName PP_source
		this.params[3] = _param.getPp_u_id();//PP_U_ID Password
		this.params[4] = _param.getPp_gs_chat_ip();//PP_GS_CHAT_IP
		this.params[5] = _param.getPp_gs_desc();//PP_GS_DESC
		this.params[6] = _param.getPp_gs_name();//PP_GS_NAME
		this.params[7] = "";//PP_timestamp timeͨ����ѯ�ó�
	}
	/**
	 * ����
	 * */
	public void repeated(){
		this.repeat_count = 0;
		this.flags = 1;
		this.flag = false;
		this.showInfo = false;
		this.sblog = new StringBuffer("");
		this.is_end = false;//��־�߳�δ����
	}

	
	/**
	 * ���߳�
	 * */
	public synchronized void run(){//�̰߳�ȫ
		try{
			this.is_end = false;//��־�߳�δ����
			this.flags = 1;//��ʼ
			if(this.monitor!=null)
				this.monitor.setType(this.flags);//�ı���ɫ
			HttpMysticalcardClient client = new HttpMysticalcardClient(this.serverhost);
			this.sblog = client.getSb_log();//���ü����־
			HttpServiceReturnObjs objs = client.mysticalcard_httpService(this.nick, this.callPara);//ͨ��username��callPara��õ�½��key��timestamp
			if(objs!=null){
				this.params[0] = objs.getKey();//�ı�key
				this.params[7] = objs.getTimestamp();//�ı�timestamp
				client.setNick(this.nick);
				System.out.println("Nick:"+this.nick);
				String Cookie = client.mysticalcard_login_gf(this.params);//ͨ��params��õ�½Cookie
				if(Cookie!=null && Cookie.length()>0){
					System.out.println("Cookie:"+Cookie);
					client.mysticalcard_GetUserinfo(Cookie);//ͨ����¼Cookie�������
					client.mysticalcard_GetLoginAwardType(Cookie);//ͨ����¼Cookieǩ��
					client.mysticalcard_GetUserSalary(Cookie);//ͨ����¼Cookie��ѯнˮ
					client.mysticalcard_AwardSalary(Cookie);//ͨ����¼Cookie���нˮ
					List<String> MapStageslist = client.mysticalcard_GetUserMapStages(Cookie);//ͨ����¼Cookie�������
					for(int j=0;j<MapStageslist.size();j++){
						client.mysticalcard_EditUserMapStages(Cookie, MapStageslist.get(j));//ͨ����¼Cookie��������
					}
					client.mysticalcard_dungeon_GetUserDungeon(Cookie);//�ж��Ƿ����ɨ�����³�
					if(client.Energy<2){//�������С��2,�����Ƿ�����������
						client.mysticalcard_GetUserinfo(Cookie);//ͨ����¼Cookie�������
						showInfo = true;
					}
					if(client.isopen8 && client.Energy>=2){//8���Ƿ���
						client.autoBattle("8", Cookie, client);
					}
					if(client.Energy<2 && !showInfo){//�������С��2,�����Ƿ�����������
						client.mysticalcard_GetUserinfo(Cookie);//ͨ����¼Cookie�������
						showInfo = true;
					}
					if(client.isopen7 && client.Energy>=2){//7���Ƿ���
						client.autoBattle("7", Cookie, client);
					}
					if(client.Energy<2 && !showInfo){//�������С��2,�����Ƿ�����������
						client.mysticalcard_GetUserinfo(Cookie);//ͨ����¼Cookie�������
						showInfo = true;
					}
					if(client.isopen6 && client.Energy>=2){//6���Ƿ���
						client.autoBattle("6", Cookie, client);
					}
					if(client.Energy<2 && !showInfo){//�������С��2,�����Ƿ�����������
						client.mysticalcard_GetUserinfo(Cookie);//ͨ����¼Cookie�������
						showInfo = true;
					}
					if(client.isopen5 && client.Energy>=2){//5���Ƿ���
						client.autoBattle("5", Cookie, client);
					}
					if(client.Energy<2 && !showInfo){//�������С��2,�����Ƿ�����������
						client.mysticalcard_GetUserinfo(Cookie);//ͨ����¼Cookie�������
						showInfo = true;
					}
					//̽��
					while(client.Energy>=2 && client.has_next){//while�������has_next ��ֹ�쳣ʱ���޷�ֹͣ
						client.mysticalcard_Explore(Cookie, String.valueOf(client.max_Mapstagedetailid));//̽��
						if(client.Energy<2 && !showInfo){//�������С��2,�����Ƿ�����������
							client.mysticalcard_GetUserinfo(Cookie);//ͨ����¼Cookie�������
							showInfo = true;
						}
					}
					if(client.DevoteActStatus){//�ж������̵��Ƿ񿪷�
						client.mysticalcard_GetActStatus(Cookie);//�����̵�ҽ�
					}
				}
			}
			client.getSb_log().append("һ���Թ�̽����ɡ�");
			client.getSb_log().append("\r\n");
			client.logInfo();//д��־
			this.flag = client.has_next;//ִ�����,���ñ��
			if(!this.flag){
				this.flags = 0;//ʧ��,δ��ʼ
				if(this.monitor!=null)
					this.monitor.setType(3);//�ı���ɫ
			}else{
				this.flags = 2;//�ɹ�
				if(this.monitor!=null)
					this.monitor.setType(this.flags);//�ı���ɫ
			}
			this.is_end = true;//��־�߳̽���
		}catch(Exception e){
			this.flags = 0;//ʧ��,δ��ʼ
			if(this.monitor!=null)
				this.monitor.setType(3);//�ı���ɫ
			e.printStackTrace();
		}
	}
}
