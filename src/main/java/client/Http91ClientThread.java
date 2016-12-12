package client;

import java.util.List;

public class Http91ClientThread implements Runnable {

	protected boolean flag = false;//��־
	protected String userParad = "";
	protected String nick = "";
	protected int repeat_count = 0;//���Դ���
	protected int flags = 1;//��־ 0ʧ�� 1��ʼ 2�ɹ�
	protected boolean showInfo = false;//����Ϊ0 ���²�ѯ���

	/**
	 * ����1
	 * */
	public Http91ClientThread(String _userParad, String _nick){
		this.userParad = _userParad;
		this.nick = _nick;
	}
	
	/**
	 * ���߳�
	 * */
	public synchronized void run(){//�̰߳�ȫ
		try{
			this.flags = 1;//��ʼ
			Http91Client client = new Http91Client();
			client.setNick(this.nick);
			String Cookie = client.mysticalcard_login(this.userParad);//ͨ��userParad��õ�½Cookie
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
				while(client.Energy>=2){
					client.mysticalcard_Explore(Cookie, String.valueOf(client.max_Mapstagedetailid));//̽��
					if(client.Energy<2 && !showInfo){//�������С��2,�����Ƿ�����������
						client.mysticalcard_GetUserinfo(Cookie);//ͨ����¼Cookie�������
						showInfo = true;
					}
				}
			}				
			client.logInfo();//д��־
			this.flag = client.has_next;//ִ�����,���ñ��
			if(!this.flag){
				this.flags = 0;//ʧ��,δ��ʼ
			}else{
				this.flags = 2;//�ɹ�
			}
		}catch(Exception e){
			this.flags = 0;//ʧ��,δ��ʼ
			e.printStackTrace();
		}
	}
}
