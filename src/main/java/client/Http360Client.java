package client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.frame.Constants;

import util.IORoutines;
import util.StringUtil;

import bean.ComparatorMapStageDetail;
import bean.MapStageDetail;
import bean.MazeEnd;
import bean.MazeTestNoView;
import bean.json.Goods;
import bean.json.MazeInfo;
import bean.json.MazeShow;
import bean.json.Mpassport;
import bean.json.MpassportUinfo;
import bean.json.Passport;
import bean.json.UserMapStages;

import net.sf.json.JSONObject;

/**
 * 360ħ������
 * */
public class Http360Client {
	private class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	@SuppressWarnings("unused")
	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
	protected int v = 1000;
	protected int Energy = 0;//����
	protected String Nick = "";//�˺���
	protected boolean has_next = true;//��һ������ִ�еı��	
	protected StringBuffer sb_log = new StringBuffer("");//��¼��־
	protected String version = "";//�汾	
	protected String mysticalcardhost = "s4.mysticalcard.com";//��������ַ ������s5
	protected int max_Mapstagedetailid = 1;//���̽����ͼ�ؿ�id
	protected boolean isopen5 = false;//5���Ƿ���
	protected boolean isopen6 = false;//6���Ƿ���
	protected boolean isopen7 = false;//7���Ƿ���
	protected boolean isopen8 = false;//8���Ƿ���
	protected boolean DevoteActStatus = false;//�����̵�
	protected boolean JourneyActStatus = false;//ȼ�յ�Զ��

	protected boolean proxy_flag = false;//�Ƿ�������
	
	public void setProxy_flag(boolean proxy_flag) {
		this.proxy_flag = proxy_flag;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getNick() {
		return Nick;
	}

	public void setNick(String nick) {
		Nick = nick;
	}

	public int getEnergy() {
		return Energy;
	}

	public void setEnergy(int energy) {
		Energy = energy;
	}

	public String getMysticalcardhost() {
		return mysticalcardhost;
	}

	public void setMysticalcardhost(String mysticalcardhost) {
		this.mysticalcardhost = mysticalcardhost;
	}

	public StringBuffer getSb_log() {
		return sb_log;
	}

	/**
	 * ���캯��
	 * */
	public Http360Client(){
		this.v = new StringUtil().radom_four();
		//this.version = "&phpp=ANDROID_360&phpl=ZH_CN&pvc=1.4.1&pvb=2014-01-13%2015%3A23%3A47";//�汾
		this.version = Constants.getInstance().getVersion_360();
		//�ж��Ƿ�������
		Properties prop = System.getProperties();
		String prop_ip = prop.getProperty("http.proxyHost");
		if(prop_ip!=null && prop_ip.trim().length()>0){//�п�������
			this.proxy_flag = true;
		}else{
			this.proxy_flag = false;
		}
		//��ʱ
		System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(Constants.connectTimeout));//����������ʱ
		System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(Constants.connectTimeout));//��������ȡ���ݳ�ʱ
	}
	
	/**
	 * 1.�ȴ�https://passport.360.cn/api.php?parad=���Cookie
	 * @param parad
	 * @return ���Cookie S��T
	 * */
	public Passport passport360GetCookie(String parad) {
		List<String> result_list = new ArrayList<String>();
		Passport resultObj = null;
		try{
			if(this.has_next) {//�ܷ����ִ��
				SSLContext context = SSLContext.getInstance("SSL");
		        // ��ʼ��
				context.init(null,
						new TrustManager[] { new Http360Client().new TrustAnyTrustManager() },
						new SecureRandom());
				SSLSocketFactory factory = context.getSocketFactory();
				String url = "passport.360.cn";
				SSLSocket socket = (SSLSocket) factory.createSocket(url, 443);
				
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				bw.write("GET /api.php?parad=5CGFFfRgOOgEyt6kx5q1Ih2dfg5W4pcUviKqx2KdBxK9qRdG7ft25uX1JzQICBBTUmYQJmcsYqNMMmGTzxxUzGhuZs%2B2mmZO0vB2o3rpQglXLt7GEIkipJOpTv3V6vwjPAdSLMKFRLN0tMYGyJkdVP1ZYa90aT0CNPjx0LNXmTYlFyNZtI1eX"
						+parad
						+"%3D&from=mpc_open_ms_200251906 HTTP/1.1");
				bw.newLine();
				bw.write("User-Agent: Qhopensdk-0.8.4;card");
				bw.newLine();
				bw.write("Content-Type: application/x-www-form-urlencoded");
				bw.newLine();
				//bw.write("Accept-Encoding: gzip, deflate");
				//bw.newLine();
				bw.write("Host: passport.360.cn");
				bw.newLine();
				bw.write("Connection: Keep-Alive");
				bw.newLine();  
				bw.newLine();
				bw.flush();
				String s = null;
				while((s=br.readLine()) != null){
					result_list.add(s);
					//System.out.println(s);
				}
				bw.close();
				br.close();
				socket.close();
				
				if(result_list.size()>0){
					String jsonString = result_list.get(result_list.size()-1).toString();//���һ�з���json��ʽ���
					//System.out.println("jsonString:"+jsonString);
					JSONObject jbect = JSONObject.fromObject(jsonString);
					resultObj = (Passport)JSONObject.toBean(jbect, Passport.class);			
				}
				String CookieQ = resultObj.getUser().getQ();
				String CookieT = resultObj.getUser().getT();
//				System.out.println("CookieQ:"+CookieQ);
//				System.out.println("CookieT:"+CookieT);
				this.sb_log.append("CookieQ:"+CookieQ);
				this.sb_log.append("\r\n");
				this.sb_log.append("CookieT:"+CookieT);
				this.sb_log.append("\r\n");
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return resultObj;
	}

	/**
	 * 2.��https://openapi.360.cn/oauth2/authorize.json?client_id=����Cookie���authorizationCode��֤��
	 * */
	public String openapi360GetAuthorizationCode(String Q, String T) {
		String authorizationCode = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				SSLContext context = SSLContext.getInstance("SSL");
		        // ��ʼ��
				context.init(null,
						new TrustManager[] { new Http360Client().new TrustAnyTrustManager() },
						new SecureRandom());
				SSLSocketFactory factory = context.getSocketFactory();
				String url = "openapi.360.cn";
				SSLSocket socket = (SSLSocket) factory.createSocket(url, 443);
				
				BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				
				bw.write("GET /oauth2/authorize.json?client_id=006b0eeedfbc8b95c467085d2f6dab3d&response_type=code&redirect_uri=oob&state=test_state111&scope=basic&version=Qhopensdk-0.8.4&mid=4c71efbbc49bc357d304ce115d6cd817&DChannel=card&display=mobile.cli_v1&oauth2_login_type=5 HTTP/1.1");
				bw.newLine();
				bw.write("User-Agent: Qhopensdk-0.8.4;card");
				bw.newLine();
				bw.write("Content-Type: application/x-www-form-urlencoded");
				bw.newLine();
				bw.write("Accept-Encoding: gzip, deflate");
				bw.newLine();
				bw.write("Cookie: Q="+Q+";T="+T+";");
				bw.newLine();
				bw.write("Host: openapi.360.cn");
				bw.newLine();
				bw.write("Connection: Keep-Alive");
				bw.newLine();  
				bw.newLine();
				bw.flush();
				String s = null;
				while((s=br.readLine()) != null){
					//System.out.println(s);
					if(s.indexOf("Location")==0){
						int first = s.indexOf("&code=");
						authorizationCode = s.substring(first+"&code=".length(), s.length());
					}
				}
				bw.close();
				br.close();
				socket.close();
//				System.out.println("authorizationCode:"+authorizationCode);
				this.sb_log.append("authorizationCode:"+authorizationCode);
				this.sb_log.append("\r\n");
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return authorizationCode;
	}

	/**
	 * 3.��֤�봫��http://91passport.mysticalcard.com/mpassport.php?do���
	 * uinfo{access_token,uin,nick,MUid,time,sign,ppsign}
	 * */
	public Mpassport mpassport(String authorizationCode) {
		Mpassport obj = null;
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://91passport.mysticalcard.com/mpassport.php?do=plogin&v="+String.valueOf(this.v)+"&";
				String data = "Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&authorizationCode="+authorizationCode
					+"&plat=safe360&IDFA=&newguide=1";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", "91passport.mysticalcard.com");
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				
				String responseText = "";
				if(responselist!=null && responselist.size()>2){
					responseText = responselist.get(2).toString().replaceAll("new", "new_status");		
				}
				if(responseText.length()>0){
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
						JSONObject jbect = JSONObject.fromObject(responseText);
						obj = (Mpassport)JSONObject.toBean(jbect, Mpassport.class);
						if(obj!=null){
							this.setNick(obj.getData().getUinfo().getNick());//�˺���
						}
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return obj;
	}
	
	/**
	 * 4.��ʽ��½http://s4.mysticalcard.com/login.php?do���Cookie
	 * */
	public String mysticalcard_login(MpassportUinfo uinfo) throws Exception {
		this.v = this.v + 1;
		String Cookie = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/login.php?do=mpLogin&v="+String.valueOf(this.v)+this.version;
				String data = "Udid=C4%3A6A%3AB7%3A86%3ABE%3A51"
					+"&Origin=safe360"
					+"&IDFA="
					+"&MUid="+uinfo.getMUid()
					+"&sign="+uinfo.getSign()
					+"&plat=safe360"
					+"&uin="+uinfo.getUin()
					+"&time="+uinfo.getTime()
					+"&access%5Ftoken="+uinfo.getAccess_token()
					+"&nick="+uinfo.getNick()
					+"&ppsign="+uinfo.getPpsign()
					+"&newguide=1"
					+"&Devicetoken=";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				
				if(responselist!=null && responselist.size()>1){
					Cookie = ((Map)responselist.get(1)).get("Set-Cookie").toString();
					int ix = Cookie.indexOf(";");
					Cookie = Cookie.substring(1, ix);
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return Cookie;
	}
	
	/**
	 * 5.ͨ��Cookie��ѯ�û�״̬http://s4.mysticalcard.com/user.php?do=GetUserinfo
	 * */
	public String mysticalcard_GetUserinfo(String Cookie) throws Exception {
		this.v = this.v + 1;
		String Energy = "";//����
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/user.php?do=GetUserinfo&OpenCardChip=1&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				
				String responseText = "";
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
						JSONObject jbect_responseText = JSONObject.fromObject(responseText);
						String jbect_responseTextdata = jbect_responseText.get("data").toString();
						JSONObject jbect_data = JSONObject.fromObject(jbect_responseTextdata);
						Energy = jbect_data.get("Energy").toString();
//						System.out.println("����:"+Energy);
						this.sb_log.append("����:"+Energy);
						this.sb_log.append("\r\n");
						this.Energy = Integer.valueOf(Energy);//��������
						
						String DevoteActStatus_str = JSONObject.fromObject(JSONObject.fromObject(jbect_responseTextdata).getString("DevoteActStatus")).getString("maze");
				        String JourneyActStatus_str = JSONObject.fromObject(JSONObject.fromObject(jbect_responseTextdata).getString("JourneyActStatus")).getString("isOpen");
				        if(DevoteActStatus_str.equals("true")){
				        	this.DevoteActStatus = true;//�����̵�
//				        	System.out.println("�����̵꿪����");
							this.sb_log.append("�����̵꿪����");
							this.sb_log.append("\r\n");
				        }
				        if(JourneyActStatus_str.equals("true")){
				        	this.JourneyActStatus = true;//ȼ�յ�Զ��
//				        	System.out.println("ȼ�յ�Զ��������");
							this.sb_log.append("ȼ�յ�Զ��������");
							this.sb_log.append("\r\n");
				    	}
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return Energy;
	}
	
	/**
	 * 6.ͨ��Cookie��ѯ�û�����ͼhttp://s4.mysticalcard.com/user.php?do=GetBackGround
	 * */
	public void mysticalcard_GetBackGround(String Cookie) throws Exception {
		
	}
	
	/**
	 * 7.ͨ��Cookie��ѯ�û�ǩ��״̬http://s4.mysticalcard.com/user.php?do=GetLoginAwardType
	 * */
	public String mysticalcard_GetLoginAwardType(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/user.php?do=GetLoginAwardType&OpenCardChip=1&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����						
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}
	
	/**
	 * 8.ͨ��Cookie��ѯ���� http://s4.mysticalcard.com/arena.php?do=GetThieves
	 * */
	public String mysticalcard_GetThieves(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 9.ͨ��Cookie��ѯ� http://s4.mysticalcard.com/activity.php?do=ActivityInfo
	 * */
	public String mysticalcard_ActivityInfo(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 10.ͨ��Cookie��ѯħ�� http://s4.mysticalcard.com/boss.php?do=GetBoss
	 * */
	public String mysticalcard_GetBoss(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 11.ͨ��Cookie��ѯ�̵� http://s4.mysticalcard.com/shop.php?do=GetWelfare
	 * */
	public String mysticalcard_GetWelfare(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 12.ͨ��Cookie��ѯ���� http://s4.mysticalcard.com/legion.php?do=GetUserLegion
	 * */
	public String mysticalcard_GetUserLegion(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 13.ͨ��Cookie��ѯ�û�нˮ http://s4.mysticalcard.com/user.php?do=GetUserSalary
	 * */
	public String mysticalcard_GetUserSalary(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/user.php?do=GetUserSalary&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����						
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}
	
	/**
	 * 14.ͨ��Cookie ���нˮ http://s4.mysticalcard.com/user.php?do=AwardSalary
	 * */
	public String mysticalcard_AwardSalary(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/user.php?do=AwardSalary&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����						
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}
	
	/**
	 * 15.���ͼ�Ƿ����� /mapstage.php?do=GetUserMapStages
	 * ��������Ҳ����
	 * 1ͼ 6 (1,2,3,4,5,6)
	 * 2ͼ 6 (7,8,9,10,11,12) 13��2��
	 * 3ͼ 7+1 (14,15,16,17,18,19,20,21) 22��3��
	 * 4ͼ 7+1 (23,24,25,26,27,28,29,30) 31��4��
	 * 5ͼ 8+1 (32,33,34,35,36,37,38,39,40) 41��5��
	 * 6ͼ 8+1 (42,43,44,45,46,47,48,49,50) 51��6��
	 * 7ͼ 9+1 (52,53,54,55,56,57,58,59,60,61) 62��7��
	 * 8ͼ 10+1 (63,64,65,66,67,68,69,70,71,72,73) 74��8��
	 * */
	@SuppressWarnings("unchecked")
	public List<String> mysticalcard_GetUserMapStages(String Cookie) throws Exception {
		this.v = this.v + 1;
		List<String> resultlist = new Vector<String>();
		List<MapStageDetail> maxlist = new Vector<MapStageDetail>();
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/mapstage.php?do=GetUserMapStages&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				
				String responseText = "";
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����						
						JSONObject jbect_responseText = JSONObject.fromObject(responseText);
						String jbect_responseText_data = jbect_responseText.get("data").toString().toLowerCase();//Сд
						jbect_responseText_data = "{\"data\":[" + jbect_responseText_data.substring(1,jbect_responseText_data.length()-1) + "]}";
						for(int i=1;i<200;i++){
							jbect_responseText_data = jbect_responseText_data.replace("\""+i+"\":{", "{");
						}
						JSONObject jbect_data = JSONObject.fromObject(jbect_responseText_data);
						UserMapStages obj_data = (UserMapStages)JSONObject.toBean(jbect_data, UserMapStages.class);
						for(int j=0;j<obj_data.getData().length;j++){
							//�ж�����
							if(!obj_data.getData()[j].getCounterattacktime().equals("0")){
								String Mapstagedetailid = obj_data.getData()[j].getMapstagedetailid();
//								System.out.println("����:"+Mapstagedetailid);
								this.sb_log.append("����:"+Mapstagedetailid);
								this.sb_log.append("\r\n");
								resultlist.add(Mapstagedetailid);
							}
							//�ж����3�ǹؿ�id
							if(obj_data.getData()[j].getFinishedstage().equals("3")){
								String Mapstagedetailid = obj_data.getData()[j].getMapstagedetailid();
								MapStageDetail md = new MapStageDetail(Integer.valueOf(Mapstagedetailid));
								maxlist.add(md);
							}
							//�жϿ���
							if(obj_data.getData()[j].getMapstagedetailid().equals("39")){
								//System.out.println("5���ѿ�");
								this.isopen5 = true;
							}
							if(obj_data.getData()[j].getMapstagedetailid().equals("49")){
								//System.out.println("6���ѿ�");
								this.isopen6 = true;		
							}
							if(obj_data.getData()[j].getMapstagedetailid().equals("60")){
								//System.out.println("7���ѿ�");
								this.isopen7 = true;
							}
							if(obj_data.getData()[j].getMapstagedetailid().equals("72")){
								//System.out.println("8���ѿ�");
								this.isopen8 = true;
							}
						}
						if(maxlist.size()>0) {
							ComparatorMapStageDetail comparatorMapStageDetail = new ComparatorMapStageDetail();
							//jdk1.7�汾�;ɰ汾�������򷽷���ͬ,��Ҫ��������ϵͳ����,û����,��Ҫ�޸����򷽷�
							//System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
							Collections.sort(maxlist, comparatorMapStageDetail);
							this.max_Mapstagedetailid = maxlist.get(0).getMapStageDetailId();
//							System.out.println("���3�ǹؿ�id:"+this.max_Mapstagedetailid);
							this.sb_log.append("���3�ǹؿ�id:"+this.max_Mapstagedetailid);
							this.sb_log.append("\r\n");
						}			
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return resultlist;
	}
	
	/**
	 * 16.�������� /mapstage.php?do=EditUserMapStages
	 * */
	public String mysticalcard_EditUserMapStages(String Cookie, String MapStageDetailId) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/mapstage.php?do=EditUserMapStages&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageDetailId="+MapStageDetailId+"&isManual=0";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				if(this.Energy>=2) {//���������
					responselist = openSync(url, data, headers);
					if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
						responseText = responselist.get(2).toString();
						int status = (Integer)JSONObject.fromObject(responseText).get("status");
						if(status==1){//����
							this.Energy =  this.Energy -2;//���ܳɹ�ʧ��,�����ܻ����
						}else{//�쳣
							this.logInfo();
							this.has_next = false;
						}
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}
	
	/**
	 * 17.����(����,�Ƿ��Ѵ�,�Ƿ��������) /maze.php?do=Show
	 * 'Name':'���֮��','Layer':1,'Clear':0,'FreeReset':1
	 * */
	public MazeShow mysticalcard_maze_Show(String Cookie, String MapStageId) throws Exception {
		this.v = this.v + 1;
		MazeShow obj_data = null;
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/maze.php?do=Show&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageId="+MapStageId;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				String responseText = "";
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
						JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());//Сд
						obj_data = (MazeShow)JSONObject.toBean(jbect_responseText, MazeShow.class);						
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return obj_data;
	}
	
	/**
	 * 18.����״̬ /maze.php?do=Info
	 * 'Name':'���֮����1��','BoxNum':2,'MonsterNum':1,'RemainBoxNum':2,
	 * 'RemainMonsterNum':1,'Layer':1,'TotalLayer':5,'Map':{'IsFinish':false
	 * */
	public MazeInfo mysticalcard_maze_Info(String Cookie, String MapStageId, String Layer) throws Exception {
		this.v = this.v + 1;
		MazeInfo obj_data = null;
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/maze.php?do=Info&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageId="+MapStageId+"&Layer="+Layer;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				String responseText = "";
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
						JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());//Сд
						obj_data = (MazeInfo)JSONObject.toBean(jbect_responseText, MazeInfo.class);			
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return obj_data;
	}
	
	/**
	 * 19.���� /maze.php?do=Battle
	 * MapStageId=8&manual=0&OpenCardChip=1&ItemIndex=22&Layer=1
	 * MapStageId=8&manual=0&OpenCardChip=1&ItemIndex=17&Layer=2
	 * һ����4*8=32�񣬳�ȥ���Ͻ����½ǵĵ�̨�������30
	 * */
	public boolean mysticalcard_maze_Battle(String Cookie, String MapStageId, String ItemIndex, String Layer) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		boolean battleflag = false;
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/maze.php?do=Battle&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageId="+MapStageId+"&manual=0&OpenCardChip=1&ItemIndex="+ItemIndex+"&Layer="+Layer;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				if(this.Energy>=2){
					responselist = openSync(url, data, headers);
					if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
						responseText = responselist.get(2).toString();
						int status = (Integer)JSONObject.fromObject(responseText).get("status");
						if(status==1){//����
							JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());
							String responseText_data = jbect_responseText.get("data").toString().toLowerCase();//Сд
							JSONObject jbect_data = JSONObject.fromObject(responseText_data.toLowerCase());
							String data_win = jbect_data.get("win").toString();
							if(data_win.equals("1")){
								battleflag = true;//�ɹ�
							}
							this.Energy =  this.Energy -2;//���ܳɹ�ʧ��,�����ܻ����
						}else{//�쳣
							this.logInfo();
							this.has_next = false;
						}
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return battleflag;
	}
	
	/**
	 * 20.������ /maze.php?do=Reset
	 * */
	public boolean mysticalcard_maze_Reset(String Cookie, String MapStageId) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		boolean resetflag = false;
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/maze.php?do=Reset&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageId="+MapStageId;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
						JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());
						if(jbect_responseText.getString("status").equals("1")){
							resetflag = true;//���óɹ�
						}
					}else{//�쳣
						this.logInfo();
						this.has_next = false;
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return resetflag;
	}

	
	/**
	 * 21.���³� dungeon.php?do=GetUserDungeon �ж�
	 * RaidsLayer > 0 �Ϳ���ɨ��
	 * RaidsStatus 0��ʾû��ɨ���� 1��ʾɨ����
	 * get(data).get(UserDungeon).get(RaidsLayer)
	 * get(data).get(UserDungeon).get(RaidsStatus)
	 * Resurrection ��Ѹ������ -1��ʾû��
	 * get(data).get(UserDungeon).get(Resurrection)
	 * CurrentLayer ��ǰ���µĲ���
	 * get(data).get(UserDungeon).get(CurrentLayer)
	 * */
	public String mysticalcard_dungeon_GetUserDungeon(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/dungeon.php?do=GetUserDungeon&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
						int RaidsLayer = Integer.valueOf(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).getString("UserDungeon")).getString("RaidsLayer"));
						int RaidsStatus = Integer.valueOf(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).getString("UserDungeon")).getString("RaidsStatus"));
						int Resurrection = Integer.valueOf(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).getString("UserDungeon")).getString("Resurrection"));
						int CurrentLayer = Integer.valueOf(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).getString("UserDungeon")).getString("CurrentLayer"));
						this.sb_log.append("RaidsLayer:"+RaidsLayer+" RaidsStatus:"+RaidsStatus+" Resurrection:"+Resurrection+" CurrentLayer:"+CurrentLayer);
						this.sb_log.append("\r\n");
						//���ʱ����8��23��֮��
						Date n1 = new Date();
						SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
						SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHH");
						String nn1 = format2.format(n1);
						String nn2 = nn1+"08";
						String nn3 = nn1+"23";
						//8�㵽23��֮��
						if(n1.after(format1.parse(nn2)) && n1.before(format1.parse(nn3))){
							if(RaidsLayer>0 && RaidsStatus==0){//����ɨ�� ����û��ɨ����							
								mysticalcard_dungeon_Sweep(Cookie);//����ɨ��
							}else if(Resurrection>-1){//���и������ ������³�
								mysticalcard_dungeon_Fight(Cookie, CurrentLayer+1);//������³�
							}else{
//								System.out.println(this.Nick+"�Ѿ�ɨ���� ���³Ǹ������Ϊ0");
								this.sb_log.append(this.Nick+"�Ѿ�ɨ���� ���³Ǹ������Ϊ0");
								this.sb_log.append("\r\n");
							}
						}else{
//							System.out.println("ʱ�䲻��8�㵽23��֮��,���ܹ�����³�");
							this.sb_log.append("ʱ�䲻��8�㵽23��֮��,���ܹ�����³�");
							this.sb_log.append("\r\n");
						}
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}
	
	/**
	 * 22.���³� dungeon.php?do=Sweep ɨ��
	 * */
	public String mysticalcard_dungeon_Sweep(String Cookie) throws Exception {

		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/dungeon.php?do=Sweep&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
//						System.out.println(this.Nick+"ɨ�����");
						this.sb_log.append(this.Nick+"ɨ�����");
						this.sb_log.append("\r\n");
						mysticalcard_dungeon_GetUserDungeon(Cookie);//�鿴���³� �Ƿ��ܹ���
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}
	
	/**
	 * 23.���³� dungeon.php?do=Fight ������³�
	 * isManual=0&Layer=21
	 * */
	public String mysticalcard_dungeon_Fight(String Cookie, int Layer) throws Exception {

		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/dungeon.php?do=Fight&v="+String.valueOf(this.v)+this.version;
				String data = "isManual=0&Layer="+Layer;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
						int win = (Integer)JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).get("Win");
						this.sb_log.append(this.Nick+"������³ǵ�"+Layer+"�� win:"+win);
						this.sb_log.append("\r\n");
						mysticalcard_dungeon_GetUserDungeon(Cookie);//�鿴���³� �Ƿ��ܹ���
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}
	
	/**
	 * 24.̽�� mapstage.php?do=Explore
	 * */
	public String mysticalcard_Explore(String Cookie, String MapStageDetailId) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/mapstage.php?do=Explore&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageDetailId="+MapStageDetailId;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				if(this.Energy>=2) {//���������
					responselist = openSync(url, data, headers);
					if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
						responseText = responselist.get(2).toString();
						int status = (Integer)JSONObject.fromObject(responseText).get("status");
						if(status==1){//����
							this.Energy =  this.Energy -2;//���ܳɹ�ʧ��,�����ܻ����
						}else{//�쳣
							this.logInfo();
							this.has_next = false;
						}
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}

	/**
	 * 25.���� arena.php?do=ThievesFight
	 * */
	public String mysticalcard_ThievesFight(String Cookie) throws Exception {
		return null;
	}

	/**
	 * 26.�鿴�����̵� devoteMazeActivity.php?do=GetActStatus
	 * */
	public String mysticalcard_GetActStatus(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/devoteMazeActivity.php?do=GetActStatus&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
						String goods = "{\"goods\":"+JSONObject.fromObject(JSONObject.fromObject(
								JSONObject.fromObject(responseText).getString("data"))
								.getString("actStatus")).getString("goods")+"}";
//				        System.out.println(this.Nick+goods);
						this.sb_log.append(this.Nick+goods);
						this.sb_log.append("\r\n");
				        JSONObject goods_jbect = JSONObject.fromObject(goods);
				        Goods actgoods = (Goods)JSONObject.toBean(goods_jbect, Goods.class);
				        int num = 0;
				        for(int i=0;i<actgoods.getGoods().length;i++){
				        	num += actgoods.getGoods()[i].getNum();
				        }
				        if(num>0){//���ж���δ�һ�
				        	mysticalcard_GetReward(Cookie);//�ҽ�
				        }
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}
	

	/**
	 * 27.��ȡ�����̵꽱�� devoteMazeActivity.php?do=GetReward
	 * */
	public String mysticalcard_GetReward(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//�ܷ����ִ��
				String url = "http://"+this.mysticalcardhost+"/devoteMazeActivity.php?do=GetReward&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//û�п�������
					headers.put("Accept-Encoding", "deflate, gzip");
				}
				headers.put("Proxy-Connection", "Keep-Alive");
				headers.put("Cookie", Cookie);
				headers.put("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
				headers.put("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
				headers.put("x-flash-version", "11,9,900,117");
				headers.put("Connection", "Keep-Alive");
				headers.put("Cache-Control", "no-cache");
				headers.put("Referer", "app:/assets/CardMain.swf");
				headers.put("Content-Type", "application/x-www-form-urlencoded");
				List responselist = null;
				responselist = openSync(url, data, headers);
				if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
					responseText = responselist.get(2).toString();
					int status = (Integer)JSONObject.fromObject(responseText).get("status");
					if(status==1){//����
//						System.out.println(this.Nick+util.StringUtil.unicodeToString(
//								JSONObject.fromObject(JSONObject.fromObject(
//										JSONObject.fromObject(responseText).getString("data"))
//										.getString("actStatus")).getString("dialog")));
						this.sb_log.append(this.Nick+util.StringUtil.unicodeToString(
								JSONObject.fromObject(JSONObject.fromObject(
										JSONObject.fromObject(responseText).getString("data"))
										.getString("actStatus")).getString("dialog")));
						this.sb_log.append("\r\n");
					}
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return responseText;
	}

	/**
	 * 
	 * */

	/**
	 * 
	 * */
	

	
	/**
	 * ��÷���ͷ��
	 * */
	public String getAllResponseHeaders(URLConnection c) {
		int idx = 0;

		StringBuffer buf = new StringBuffer();
		String value;
		while ((value = c.getHeaderField(idx)) != null) {
			String key = c.getHeaderFieldKey(idx);
			buf.append(key); buf.append(": "); buf.append(value);
			idx++;
		}
		return buf.toString();
	}
	
	/**
	 * ��÷��������ı�
	 * */
	public synchronized String getResponseText(byte[] responseBytes) {
		byte[] bytes = responseBytes;
		try {
			return bytes == null ? null : new String(bytes, "ISO-8859-1");
		} catch (UnsupportedEncodingException uee) {
			System.out.println(this.Nick+":"+uee.toString());
			this.sb_log.append(uee.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return null;
	}
	
	/**
	 * ��÷�������xml
	 * */
//	public synchronized Document getResponseXML(byte[] responseBytes) {
//		byte[] bytes = responseBytes;
//	    if (bytes == null) {
//	    	return null;
//	    }
//	    InputStream in = new ByteArrayInputStream(bytes);
//	    try {
//	    	return DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
//	    }
//	    catch (Exception err) {
//	    }
//	    return null;
//	}
	
	/**
	 * post��ʽ���ͱ���
	 * @param _url ��ַ
	 * @param _data ����
	 * @param _requestheaders ͷ
	 * @return responselist(responseHeaders,responseHeadersMap,responseText)
	 * */
	public List openSync(String _url, String _data, Map _requestheaders) {
		this.sb_log.append("\r\n");
		List list = new Vector();
		OutputStream out = null;
        DataOutputStream dos = null;
        InputStream in = null;
		try {
			this.sb_log.append("url:"+_url);
			this.sb_log.append("\r\n");
			this.sb_log.append("data:"+_data);
			this.sb_log.append("\r\n");
			URL url = new URL(_url);
			String data = new String(_data.getBytes(), "ISO-8859-1");
			String responseHeaders;
			Map responseHeadersMap;
			String responseText;
			URLConnection c = url.openConnection();
			HttpURLConnection urlcon = null;
			try {
				if(c instanceof HttpURLConnection){
					urlcon = (HttpURLConnection)c;
					urlcon.setRequestMethod("POST");
			
					if(_requestheaders.size()>0){
						Set keys = _requestheaders.entrySet();
						Iterator it = keys.iterator();
						while(it.hasNext()){
							Entry me = (Entry) it.next();
							urlcon.setRequestProperty(me.getKey().toString(), me.getValue().toString());
						}
					}
					urlcon.setRequestProperty("Content-Length", data.length()+"");
					urlcon.setConnectTimeout(Constants.connectTimeout);//����������ʱ(��λ����)
					urlcon.setReadTimeout(Constants.connectTimeout);//��������ȡ���ݳ�ʱ(��λ����)
					urlcon.setDoOutput(true);
	                out = urlcon.getOutputStream();
	                dos = new DataOutputStream(out);
	                dos.write(data.getBytes());
	                dos.flush();
	                dos.close();
	                
					in = urlcon.getInputStream();
					int contentLength = urlcon.getContentLength();
					byte[] bytes = IORoutines.load(in, contentLength == -1 ? 4096 : contentLength);
					int status = 0;
					String statusText = "";
					status = urlcon.getResponseCode();
					statusText = urlcon.getResponseMessage();
					this.sb_log.append("status:"+status);
					this.sb_log.append("\r\n");
					this.sb_log.append("statusText:"+statusText);
					this.sb_log.append("\r\n");
						
					synchronized (this) {
						responseHeaders = getAllResponseHeaders(urlcon);
						responseHeadersMap = urlcon.getHeaderFields();
						this.sb_log.append("responseHeaders:"+responseHeaders);
						this.sb_log.append("\r\n");
						this.sb_log.append("responseHeadersMap:"+responseHeadersMap);
						this.sb_log.append("\r\n");
						Object obj = responseHeadersMap.get("Content-Encoding");
						String Encoding = "";
						if(obj!=null){
							Encoding = obj.toString();
						}
						this.sb_log.append("Content-Encoding:"+Encoding);
						this.sb_log.append("\r\n");
						if(Encoding.equals("[gzip]")){
							responseText = util.ZipUtil2.uncompress(getResponseText(bytes));
						}else if(Encoding.equals("[deflate]")){
							responseText = util.DeflateUtil.uncompress(getResponseText(bytes));
						}else{
							responseText = getResponseText(bytes);
//							responseText = new String(bytes, "utf-8");
						}						
						this.sb_log.append("ResponseText:"+responseText);
						this.sb_log.append("\r\n");
						if(responseHeaders!=null && responseHeaders.length()>0){
							list.add(responseHeaders);
						}
						if(responseHeadersMap!=null && responseHeadersMap.size()>0){
							list.add(responseHeadersMap);
						}
						if(responseText!=null && responseText.length()>0){
							list.add(responseText);
						}
					}
					in.close();				
				}
			} catch (Exception ee) {
				System.out.println(this.Nick+":"+ee.toString());
				this.sb_log.append(ee.toString());
				this.sb_log.append("\r\n");
				this.logInfo();
				this.has_next = false;
			} finally {
				synchronized (this) {
					c = null;
					urlcon.disconnect();
					if(in!=null){
						in.close();
					}
					if(out!=null){
						out.close();
					}
					if(dos!=null){
						dos.close();
					}
				}
			}
		} catch (Exception err) {
			System.out.println(this.Nick+":"+err.toString());
			this.sb_log.append(err.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return list;
	}
	
	/**
	 * д��־
	 * */
	public void logInfo() {
		FileWriter fw = null;
    	BufferedWriter bw = null;	
//      FileReader fr = null;
//    	BufferedReader br = null;
    	String filename = Constants.rootPath+Constants.spltstr+"ALL-LOGS"+Constants.spltstr+this.Nick+".txt";
    	if(this.has_next){
			try {
				SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//ʱ���ʽ				
				File f = new File(filename);	    	
		    	StringBuffer sb = new StringBuffer("");
//		        fr = new FileReader(f);
//		        br = new BufferedReader(fr);
//		        String tmp = "";
//		        while((tmp = br.readLine()) != null){
//		        	sb.append(tmp);
//		        	sb.append("\r\n");
//		        }
		        sb.append("--------------------------------------------------------");
	        	sb.append("\r\n");
	        	sb.append(sim.format(new Date()));
	        	sb.append("\r\n");
	        	
	        	sb.append(this.sb_log);
		        sb.append("--------------------------------------------------------");
	        	sb.append("\r\n");
	        	
	        	fw = new FileWriter(f);
	            bw = new BufferedWriter(fw);            
	            bw.write(sb.toString());
	            bw.flush();
	
//	            br.close();
//	            fr.close();
	            fw.close();
	            bw.close();	        
			} catch (Exception e) {
				System.out.println(this.Nick+":"+e.toString());
				this.has_next = false;
			} finally {
//				if(br!=null){
//					try {
//						br.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
//				if(fr!=null){
//					try {
//						fr.close();
//					} catch (IOException e) {
//						e.printStackTrace();
//					}
//				}
				if(fw!=null){
					try {
						fw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(bw!=null){
					try {
						bw.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * �鿴����״̬ˢ�� Show ������״̬����ˢ��������
	 * @param MapStageId ��id(6,7,8)��
	 * */
	protected void autoBattle(String MapStageId,String Cookie, Object beans) {
		Http360Client bean = null;	
		if(beans instanceof Http360Client){
			bean = (Http360Client)beans;
		}else if(beans instanceof HttpDuokuClient){
			bean = (HttpDuokuClient)beans;			
		}else if(beans instanceof HttpMysticalcardClient){
			bean = (HttpMysticalcardClient)beans;			
		}
		if(this.has_next){
//			System.out.println(this.Nick+" "+"��ʼ�Զ�ˢ"+MapStageId+"��");
			this.sb_log.append("��ʼ�Զ�ˢ"+MapStageId+"��");
			this.sb_log.append("\r\n");
			try{
				//show
				MazeShow mazeshow_obj = bean.mysticalcard_maze_Show(Cookie, MapStageId);
				if(mazeshow_obj!=null){
					int Layer = mazeshow_obj.getData().getLayer();//��ǰ����
					int clear = mazeshow_obj.getData().getClear();//�������Ƿ����
					int freereset = mazeshow_obj.getData().getFreereset();//������ô���
					this.sb_log.append("��ǰ����Layer:"+Layer);
					this.sb_log.append("\r\n");
					this.sb_log.append("���Ƿ����clear:"+clear);
					this.sb_log.append("\r\n");
					this.sb_log.append("���Ƿ����������freereset:"+freereset);
					this.sb_log.append("\r\n");
					//info
					if(clear==0){//����û����
						this.sb_log.append(MapStageId+"����û����");
						this.sb_log.append("\r\n");
						autoBattleByLayer(MapStageId, Layer, Cookie, bean);
					}else if(clear==1 && freereset==1){//��������,�������������
//						System.out.println(this.Nick+" "+MapStageId+"��������,�������������");
						this.sb_log.append(MapStageId+"��������,�������������");
						this.sb_log.append("\r\n");
						boolean reset_flag = bean.mysticalcard_maze_Reset(Cookie, MapStageId);//����
						if(reset_flag){//���óɹ�
							autoBattle(MapStageId, Cookie, bean);//��������������д���
						}
					}else{
						this.sb_log.append(MapStageId+"������,����������ù���");
						this.sb_log.append("\r\n");
					}
				}
			}catch(Exception e){
				System.out.println(this.Nick+":"+e.toString());
				this.sb_log.append(e.toString());
				this.sb_log.append("\r\n");
				this.logInfo();
				this.has_next = false;
			}
		}
	}
	
	/**
	 * ���ݲ����Զ�ˢ�� Info Battle
	 * @param MapStageId ��id(6,7,8)��
	 * @param Layer ����id
	 * */
	protected void autoBattleByLayer(String MapStageId, int Layer, String Cookie, Object beans) {
		Http360Client bean = null;	
		if(beans instanceof Http360Client){
			bean = (Http360Client)beans;
		}else if(beans instanceof HttpDuokuClient){
			bean = (HttpDuokuClient)beans;			
		}else if(beans instanceof HttpMysticalcardClient){
			bean = (HttpMysticalcardClient)beans;			
		}
		if(this.has_next){
//			System.out.println(this.Nick+" "+"��ʼ�Զ�ˢ"+MapStageId+"�� ��"+Layer+"��");
			this.sb_log.append("��ʼ�Զ�ˢ"+MapStageId+"�� ��"+Layer+"��");
			this.sb_log.append("\r\n");
			try{
				MazeInfo mazeinfo_obj = bean.mysticalcard_maze_Info(Cookie, MapStageId, String.valueOf(Layer));
				if(mazeinfo_obj!=null){
					boolean isIsfinish = mazeinfo_obj.getData().getMap().isIsfinish();//��ǰ���Ƿ����
					int totalLayer = mazeinfo_obj.getData().getTotallayer();//��ǰ�����ܲ���
					if(!isIsfinish){//�����Թ�δ��
						this.sb_log.append("�����Թ�δ����");
						this.sb_log.append("\r\n");
						int[] WallRows = mazeinfo_obj.getData().getMap().getWallrows();//���� 7*4 ˳���
						int[] WallCols = mazeinfo_obj.getData().getMap().getWallcols();//���� 8*3 ˳����
						int[] Items = mazeinfo_obj.getData().getMap().getItems();//���� ���� ��¥ ��¥
						MazeTestNoView mazeObj = new MazeTestNoView(WallRows, WallCols, Items);
						boolean is_win = false;//��������ӵ�ս�����
						//����й�������� ��ʼ���Թ�
						if(mazeObj.getEndjSize()>0){
							mazeObj.StartMaze();//��ʼ���Թ�
							while(mazeObj!=null && mazeObj.getEndjSize()>0 && this.has_next){//while�������has_next ���������쳣
								Thread.sleep(500);
								//����Թ����� �յ��б�����ݾͶ����Ƴ���
								if(mazeObj.getEndjSize()==0)break;
							}
							//�Թ�����,׼������
							for(int im=0;im<mazeObj.getOrderlist().size();im++){
								Thread.sleep(500);
								int ItemIndex = mazeObj.getOrderlist().get(im).getItemindex();
								this.sb_log.append("Item:"+mazeObj.getOrderlist().get(im).getItem()+" ");
								this.sb_log.append("Itemindex:"+ItemIndex+" ");
								this.sb_log.append("��˳��ʼ������������ ");
								this.sb_log.append("\r\n");
								//��˳��ʼ������������
								is_win = bean.mysticalcard_maze_Battle(Cookie, MapStageId, String.valueOf(ItemIndex), String.valueOf(Layer));
								this.sb_log.append("ս�����:"+is_win);
								this.sb_log.append("\r\n");
								while(!is_win && bean.Energy>=2 && this.has_next){//û��ս���ɹ� ���������� ���������쳣�������쳣��Ҫ����has_next
									this.sb_log.append("û��ս���ɹ� ���������� ");
									this.sb_log.append("\r\n");
									Thread.sleep(500);
									//��������������,ֱ���ɹ�����û������
									is_win = bean.mysticalcard_maze_Battle(Cookie, MapStageId, String.valueOf(ItemIndex), String.valueOf(Layer));
									this.sb_log.append("ս�����:"+is_win);
									this.sb_log.append("\r\n");
								}
								if(!is_win){//û������
//									System.out.println(this.Nick+" "+"�˳����������ѭ�� ��Ϊû������");
									this.sb_log.append("�˳����������ѭ�� ��Ϊû������");
									this.sb_log.append("\r\n");
									break;//�˳� ��Ϊû������
								}
							}
						}else{//û�й�������� ֻʣ¥��
							this.sb_log.append("û�й�������� ֻʣ¥��");
							this.sb_log.append("\r\n");
							is_win = true;
						}
						//������������ ׼����¥
						boolean is_up_win = false;//��¥ս���Ƿ�ɹ�
						if(is_win && bean.Energy>=2){//ս���ɹ�����������
							MazeEnd UpLayerObj = mazeObj.getUpLayerObj();//��¥����
							if(UpLayerObj!=null){
								this.sb_log.append("����¥����,������¥ ������������ ս���ɹ����������� ׼����¥");
								this.sb_log.append("\r\n");
								int up_ItemIndex = mazeObj.getUpLayerObj().getItemindex();								
								this.sb_log.append("��¥��λ��:"+up_ItemIndex);
								this.sb_log.append("\r\n");
								is_up_win = bean.mysticalcard_maze_Battle(Cookie, MapStageId, String.valueOf(up_ItemIndex), String.valueOf(Layer));
								while(!is_up_win && bean.Energy>=2 && this.has_next){//û��ս���ɹ� ���������� ���������쳣�������쳣��Ҫ����has_next
									this.sb_log.append("��¥û��ս���ɹ� ����������");
									this.sb_log.append("\r\n");
									Thread.sleep(500);
									//������¥,ֱ���ɹ�����û������
									is_up_win = bean.mysticalcard_maze_Battle(Cookie, MapStageId, String.valueOf(up_ItemIndex), String.valueOf(Layer));
									this.sb_log.append("��¥ս�����:"+is_up_win);
									this.sb_log.append("\r\n");
								}
								if(is_up_win && bean.Energy>=2){//��¥ս���ɹ�����������
									this.sb_log.append("��¥ս���ɹ����������� ��¥");
									this.sb_log.append("\r\n");
									if(Layer<totalLayer){//�����ǰ��С���ܲ���,����¥
										int upLayer = Layer+1;
										this.sb_log.append("��ǰ"+Layer+"��С���ܲ���("+totalLayer+"),����"+upLayer+"¥");
										this.sb_log.append("\r\n");
										autoBattleByLayer(MapStageId, upLayer, Cookie, bean);//��������������д���
									}
								}
							}else{
								this.sb_log.append("û����¥����,�鿴����״̬");
								this.sb_log.append("\r\n");
								//û����¥�����ʱ����Բ鿴����״̬
								autoBattle(MapStageId, Cookie, bean);
							}
						}else{
//							System.out.println(this.Nick+" "+"û������ û����¥");
							this.sb_log.append(this.Nick+" "+"û������ û����¥");
							this.sb_log.append("\r\n");
						}
					}else{//��¥����¥
						this.sb_log.append("��¥����¥");
						this.sb_log.append("\r\n");
						if(Layer<totalLayer){//�����ǰ��С���ܲ���,��¥
							int upLayer = Layer+1;
							this.sb_log.append("��ǰ"+Layer+"��С���ܲ���("+totalLayer+"),��"+upLayer+"¥");
							this.sb_log.append("\r\n");
							autoBattleByLayer(MapStageId, upLayer, Cookie, bean);//��������������д���
						}else{//��ǰ���������ܲ��� ��¥
							//��¥��Ҫ���ø���¥�ź� ��show��ʱ����������ź�Ϊ��¥����
							this.sb_log.append("��ǰ���������ܲ��� ��Ҫ��¥?");
							this.sb_log.append("\r\n");
						}
					}
				}
			}catch(Exception e){
				System.out.println(this.Nick+":"+e.toString());
				this.sb_log.append(e.toString());
				this.sb_log.append("\r\n");
				this.logInfo();
				this.has_next = false;
			}
		}
	}
		
	public static void main(String[] args) {
		String[][] userParad = {
				{"wMYVABEuHR2%2Fo0TBU%2F5%2FmZdQs2yrKHF%2B6%2FZZkVrOXmxnzMgN49WQUFKCwkOYco5m2%2ByJv5P3mtOT%2FbaVmmUDX0","dlink165"}
				,{"%2B2ugS5rmG%2FOWFH7Whb3DMExpStJj3t7282Q07MLpqGnsuYQiW1RUCwQIK9Bjx7E%2FS8nLYaAU1lyl66Gt7s4ouQ","dlink166"}
				,{"9Anm05dQ%2B%2Fcgf%2F4kAGOP55UbIcLcMw%2BhLATSQKI1A4QVYu5UiWdxSudj2%2BPylxhwQzttuNDoubHqL1%2F0Ky8l0I","dlink167"}
				,{"9FQhqfy6g1uIDaLi3Ur%2B5jQDyP7gwV6nFQdg65QKjBT9Bd8N%2F08x%2Bv7%2BLfociQT6AkbAJBHhohP5MH5ym6vfxI","dlink168"}
				,{"9c6nrL1s5exTJ4T%2Fmr0MJLniPkfp0A7NeDlS3S2fSAxivqDcpVcLxGvOixa8G%2BEX1LNGx5qPDWJxXHa9yM1ojQ","dlink169"}
				,{"%2F7g4hc3xrX%2BR7nTyPUnjLVpbmV2WZPuAPA%2Fm9X4uqVpakfA9CoLv%2FjA11%2B8Rl%2BQ0mMyg7X931bek%2FTp7Za0d04","dlink170"}
				,{"x8nGdrUqmc3Yjw1dbCAxBik4efq0IosuMVLI%2Bf86FQ4Z1SHV1oSMUndqEROOSiJLCLkIb4HI7cUzYd34LpStTQ","dlink171"}
				,{"wsIpxbw21NCD1%2BC2U8wOt8Y8Q7ey6v07moxWdV8m%2FU0Kfn%2Bj1qpyHR70EsrFJe4PYzHFaRBFHf2aeAaAB1I6I8","dlink172"}
				,{"4Ns4ya%2B%2FOYJ%2Bp1LeNQLeXfgHgMS7uuA4FvIv0aQ7dwwH5zJb2VDXBkMhIuDBtmZX%2FunAmf2YocliC6e3Ld611c","dlink173"}
				,{"8W%2BANRQ2%2BLplb%2FXdT3QE6XEFmQ6xK90ETOrEWiR1995zn5rswveIhEfZaS%2FeUiKfazFBkzzlmuf5xpNXeEkufA","dlink174"}
				,{"28iMraS6rgDjw4xyo8rXUYl4okUb1tzr7G%2Bceiu3%2FLp6iTMaBYn4L6GXwjUebjMYF1g8Wk6Jx%2BH5JbfsAwQw9E","dlink175"}
				,{"4gSiFnXhbeILCvWSiruX%2FRZySbtjlRQPQUpzaAOu6M698tEPnOC%2FxOdsDFT0fwIn6Q9rznp9I3TBbhxGsB56Ek","dlink176"}
				,{"%2B1TtVCX52eG%2BcdEXDj5T1iYYqh5yNJheYrBgOpXP5DWgBs%2BtA7Mj%2BkEoSe7DTrLYVwUhF3brNT7FeY85ViUTPg","dlink177"}
				,{"%2FpYyXhf04WE8vsAAe8xQPqdaEZSMmWbReDcd5W8Nayop3E9dESAZuh4T3QQUd5Xz1rsZOANXGfanjakIFd0%2BO0","dlink178"}
				,{"wZmzADsOCznExYReZVHTGS89HAyNe01QVuAjoOyuJhtCBfaF6AAWxJ1PR%2B78YVPvhVajLCJGH6BrTLGI724oqE","dlink179"}
				,{"1knFfkVHsh2HOw60mYc5xtrBiBc95LW3ZqZRaM%2FakyP4TTMbAhpEQSZ98WFVWxP1LgakMxpln8tFpH3KQAEcx8","dlink180"}
			};
		try{
			for(int i=0; i<userParad.length
			; i++){
				Http360ClientThread thread = new Http360ClientThread(userParad[i][0], userParad[i][1]);
				new Thread(thread).start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
