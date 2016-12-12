package client;

import java.util.List;

import bean.MonitorGrid;
import bean.ParamsMysticalcard;
import bean.json.Mpassport;
import bean.json.MpassportUinfo;

public class HttpDuokuClientThread implements Runnable {

	protected boolean flag = false;//��־
	protected String userParad = "";
	protected String nick = "";
	protected int repeat_count = 0;//���Դ���
	protected int flags = 1;//��־ 0ʧ�� 1��ʼ 2�ɹ�
	protected boolean showInfo = false;//����Ϊ0 ���²�ѯ���
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
	public HttpDuokuClientThread(String _userParad, String _nick){
		this.userParad = _userParad;
		this.nick = _nick;
	}
	/**
	 * ����2
	 * */
	public HttpDuokuClientThread(ParamsMysticalcard _Params){
		this.userParad = _Params.getUserParad();
		this.nick = _Params.getNick();
		this.monitor = new MonitorGrid(this.nick,20,20,this);
	}
	/**
	 * ����3 dos�����µĹ��� ��ֹʹ��monitor
	 * */
	public HttpDuokuClientThread(ParamsMysticalcard _Params, int arg1){
		this.userParad = _Params.getUserParad();
		this.nick = _Params.getNick();
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
			HttpDuokuClient client = new HttpDuokuClient();
			this.sblog = client.getSb_log();//���ü����־
			client.setNick(this.nick);
			Mpassport obj = client.mpassport(this.userParad);
			if(obj!=null){
				MpassportUinfo uinfo = obj.getData().getUinfo();//unifo����
				System.out.println("Nick:"+obj.getData().getUinfo().getNick());
				String Cookie = client.mysticalcard_login(uinfo);//ͨ��unifo��õ�½Cookie
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
