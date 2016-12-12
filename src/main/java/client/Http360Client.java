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
 * 360魔卡幻想
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
	protected int Energy = 0;//体力
	protected String Nick = "";//账号名
	protected boolean has_next = true;//下一步可以执行的标记	
	protected StringBuffer sb_log = new StringBuffer("");//记录日志
	protected String version = "";//版本	
	protected String mysticalcardhost = "s4.mysticalcard.com";//服务器地址 可能是s5
	protected int max_Mapstagedetailid = 1;//最大探索地图关卡id
	protected boolean isopen5 = false;//5塔是否开启
	protected boolean isopen6 = false;//6塔是否开启
	protected boolean isopen7 = false;//7塔是否开启
	protected boolean isopen8 = false;//8塔是否开启
	protected boolean DevoteActStatus = false;//兽人商店
	protected boolean JourneyActStatus = false;//燃烧的远征

	protected boolean proxy_flag = false;//是否开启代理
	
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
	 * 构造函数
	 * */
	public Http360Client(){
		this.v = new StringUtil().radom_four();
		//this.version = "&phpp=ANDROID_360&phpl=ZH_CN&pvc=1.4.1&pvb=2014-01-13%2015%3A23%3A47";//版本
		this.version = Constants.getInstance().getVersion_360();
		//判断是否开启代理
		Properties prop = System.getProperties();
		String prop_ip = prop.getProperty("http.proxyHost");
		if(prop_ip!=null && prop_ip.trim().length()>0){//有开启代理
			this.proxy_flag = true;
		}else{
			this.proxy_flag = false;
		}
		//超时
		System.setProperty("sun.net.client.defaultConnectTimeout", String.valueOf(Constants.connectTimeout));//连接主机超时
		System.setProperty("sun.net.client.defaultReadTimeout", String.valueOf(Constants.connectTimeout));//从主机读取数据超时
	}
	
	/**
	 * 1.先从https://passport.360.cn/api.php?parad=获得Cookie
	 * @param parad
	 * @return 获得Cookie S和T
	 * */
	public Passport passport360GetCookie(String parad) {
		List<String> result_list = new ArrayList<String>();
		Passport resultObj = null;
		try{
			if(this.has_next) {//能否继续执行
				SSLContext context = SSLContext.getInstance("SSL");
		        // 初始化
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
					String jsonString = result_list.get(result_list.size()-1).toString();//最后一行返回json格式结果
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
	 * 2.从https://openapi.360.cn/oauth2/authorize.json?client_id=传入Cookie获得authorizationCode认证码
	 * */
	public String openapi360GetAuthorizationCode(String Q, String T) {
		String authorizationCode = "";
		try{
			if(this.has_next) {//能否继续执行
				SSLContext context = SSLContext.getInstance("SSL");
		        // 初始化
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
	 * 3.认证码传入http://91passport.mysticalcard.com/mpassport.php?do获得
	 * uinfo{access_token,uin,nick,MUid,time,sign,ppsign}
	 * */
	public Mpassport mpassport(String authorizationCode) {
		Mpassport obj = null;
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://91passport.mysticalcard.com/mpassport.php?do=plogin&v="+String.valueOf(this.v)+"&";
				String data = "Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&authorizationCode="+authorizationCode
					+"&plat=safe360&IDFA=&newguide=1";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", "91passport.mysticalcard.com");
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
						JSONObject jbect = JSONObject.fromObject(responseText);
						obj = (Mpassport)JSONObject.toBean(jbect, Mpassport.class);
						if(obj!=null){
							this.setNick(obj.getData().getUinfo().getNick());//账号名
						}
					}else{//异常
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
	 * 4.正式登陆http://s4.mysticalcard.com/login.php?do获得Cookie
	 * */
	public String mysticalcard_login(MpassportUinfo uinfo) throws Exception {
		this.v = this.v + 1;
		String Cookie = "";
		try{
			if(this.has_next) {//能否继续执行
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
				if(!this.proxy_flag){//没有开启代理
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
	 * 5.通过Cookie查询用户状态http://s4.mysticalcard.com/user.php?do=GetUserinfo
	 * */
	public String mysticalcard_GetUserinfo(String Cookie) throws Exception {
		this.v = this.v + 1;
		String Energy = "";//体力
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/user.php?do=GetUserinfo&OpenCardChip=1&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
						JSONObject jbect_responseText = JSONObject.fromObject(responseText);
						String jbect_responseTextdata = jbect_responseText.get("data").toString();
						JSONObject jbect_data = JSONObject.fromObject(jbect_responseTextdata);
						Energy = jbect_data.get("Energy").toString();
//						System.out.println("体力:"+Energy);
						this.sb_log.append("体力:"+Energy);
						this.sb_log.append("\r\n");
						this.Energy = Integer.valueOf(Energy);//设置体力
						
						String DevoteActStatus_str = JSONObject.fromObject(JSONObject.fromObject(jbect_responseTextdata).getString("DevoteActStatus")).getString("maze");
				        String JourneyActStatus_str = JSONObject.fromObject(JSONObject.fromObject(jbect_responseTextdata).getString("JourneyActStatus")).getString("isOpen");
				        if(DevoteActStatus_str.equals("true")){
				        	this.DevoteActStatus = true;//兽人商店
//				        	System.out.println("兽人商店开启。");
							this.sb_log.append("兽人商店开启。");
							this.sb_log.append("\r\n");
				        }
				        if(JourneyActStatus_str.equals("true")){
				        	this.JourneyActStatus = true;//燃烧的远征
//				        	System.out.println("燃烧的远征开启。");
							this.sb_log.append("燃烧的远征开启。");
							this.sb_log.append("\r\n");
				    	}
					}else{//异常
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
	 * 6.通过Cookie查询用户背景图http://s4.mysticalcard.com/user.php?do=GetBackGround
	 * */
	public void mysticalcard_GetBackGround(String Cookie) throws Exception {
		
	}
	
	/**
	 * 7.通过Cookie查询用户签到状态http://s4.mysticalcard.com/user.php?do=GetLoginAwardType
	 * */
	public String mysticalcard_GetLoginAwardType(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/user.php?do=GetLoginAwardType&OpenCardChip=1&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常						
					}else{//异常
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
	 * 8.通过Cookie查询盗贼 http://s4.mysticalcard.com/arena.php?do=GetThieves
	 * */
	public String mysticalcard_GetThieves(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 9.通过Cookie查询活动 http://s4.mysticalcard.com/activity.php?do=ActivityInfo
	 * */
	public String mysticalcard_ActivityInfo(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 10.通过Cookie查询魔神 http://s4.mysticalcard.com/boss.php?do=GetBoss
	 * */
	public String mysticalcard_GetBoss(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 11.通过Cookie查询商店 http://s4.mysticalcard.com/shop.php?do=GetWelfare
	 * */
	public String mysticalcard_GetWelfare(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 12.通过Cookie查询军团 http://s4.mysticalcard.com/legion.php?do=GetUserLegion
	 * */
	public String mysticalcard_GetUserLegion(String Cookie) throws Exception {
		return null;
	}
	
	/**
	 * 13.通过Cookie查询用户薪水 http://s4.mysticalcard.com/user.php?do=GetUserSalary
	 * */
	public String mysticalcard_GetUserSalary(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/user.php?do=GetUserSalary&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常						
					}else{//异常
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
	 * 14.通过Cookie 获得薪水 http://s4.mysticalcard.com/user.php?do=AwardSalary
	 * */
	public String mysticalcard_AwardSalary(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/user.php?do=AwardSalary&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常						
					}else{//异常
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
	 * 15.查地图是否被入侵 /mapstage.php?do=GetUserMapStages
	 * 不开隐藏也有塔
	 * 1图 6 (1,2,3,4,5,6)
	 * 2图 6 (7,8,9,10,11,12) 13是2塔
	 * 3图 7+1 (14,15,16,17,18,19,20,21) 22是3塔
	 * 4图 7+1 (23,24,25,26,27,28,29,30) 31是4塔
	 * 5图 8+1 (32,33,34,35,36,37,38,39,40) 41是5塔
	 * 6图 8+1 (42,43,44,45,46,47,48,49,50) 51是6塔
	 * 7图 9+1 (52,53,54,55,56,57,58,59,60,61) 62是7塔
	 * 8图 10+1 (63,64,65,66,67,68,69,70,71,72,73) 74是8塔
	 * */
	@SuppressWarnings("unchecked")
	public List<String> mysticalcard_GetUserMapStages(String Cookie) throws Exception {
		this.v = this.v + 1;
		List<String> resultlist = new Vector<String>();
		List<MapStageDetail> maxlist = new Vector<MapStageDetail>();
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/mapstage.php?do=GetUserMapStages&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常						
						JSONObject jbect_responseText = JSONObject.fromObject(responseText);
						String jbect_responseText_data = jbect_responseText.get("data").toString().toLowerCase();//小写
						jbect_responseText_data = "{\"data\":[" + jbect_responseText_data.substring(1,jbect_responseText_data.length()-1) + "]}";
						for(int i=1;i<200;i++){
							jbect_responseText_data = jbect_responseText_data.replace("\""+i+"\":{", "{");
						}
						JSONObject jbect_data = JSONObject.fromObject(jbect_responseText_data);
						UserMapStages obj_data = (UserMapStages)JSONObject.toBean(jbect_data, UserMapStages.class);
						for(int j=0;j<obj_data.getData().length;j++){
							//判断入侵
							if(!obj_data.getData()[j].getCounterattacktime().equals("0")){
								String Mapstagedetailid = obj_data.getData()[j].getMapstagedetailid();
//								System.out.println("入侵:"+Mapstagedetailid);
								this.sb_log.append("入侵:"+Mapstagedetailid);
								this.sb_log.append("\r\n");
								resultlist.add(Mapstagedetailid);
							}
							//判断最大3星关卡id
							if(obj_data.getData()[j].getFinishedstage().equals("3")){
								String Mapstagedetailid = obj_data.getData()[j].getMapstagedetailid();
								MapStageDetail md = new MapStageDetail(Integer.valueOf(Mapstagedetailid));
								maxlist.add(md);
							}
							//判断开塔
							if(obj_data.getData()[j].getMapstagedetailid().equals("39")){
								//System.out.println("5塔已开");
								this.isopen5 = true;
							}
							if(obj_data.getData()[j].getMapstagedetailid().equals("49")){
								//System.out.println("6塔已开");
								this.isopen6 = true;		
							}
							if(obj_data.getData()[j].getMapstagedetailid().equals("60")){
								//System.out.println("7塔已开");
								this.isopen7 = true;
							}
							if(obj_data.getData()[j].getMapstagedetailid().equals("72")){
								//System.out.println("8塔已开");
								this.isopen8 = true;
							}
						}
						if(maxlist.size()>0) {
							ComparatorMapStageDetail comparatorMapStageDetail = new ComparatorMapStageDetail();
							//jdk1.7版本和旧版本调用排序方法不同,需要加入如下系统设置,没有用,需要修改排序方法
							//System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
							Collections.sort(maxlist, comparatorMapStageDetail);
							this.max_Mapstagedetailid = maxlist.get(0).getMapStageDetailId();
//							System.out.println("最大3星关卡id:"+this.max_Mapstagedetailid);
							this.sb_log.append("最大3星关卡id:"+this.max_Mapstagedetailid);
							this.sb_log.append("\r\n");
						}			
					}else{//异常
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
	 * 16.消灭入侵 /mapstage.php?do=EditUserMapStages
	 * */
	public String mysticalcard_EditUserMapStages(String Cookie, String MapStageDetailId) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/mapstage.php?do=EditUserMapStages&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageDetailId="+MapStageDetailId+"&isManual=0";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
				if(this.Energy>=2) {//如果有体力
					responselist = openSync(url, data, headers);
					if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
						responseText = responselist.get(2).toString();
						int status = (Integer)JSONObject.fromObject(responseText).get("status");
						if(status==1){//正常
							this.Energy =  this.Energy -2;//不管成功失败,体力总会减少
						}else{//异常
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
	 * 17.入塔(打到哪,是否已打,是否免费重置) /maze.php?do=Show
	 * 'Name':'天空之塔','Layer':1,'Clear':0,'FreeReset':1
	 * */
	public MazeShow mysticalcard_maze_Show(String Cookie, String MapStageId) throws Exception {
		this.v = this.v + 1;
		MazeShow obj_data = null;
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/maze.php?do=Show&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageId="+MapStageId;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
						JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());//小写
						obj_data = (MazeShow)JSONObject.toBean(jbect_responseText, MazeShow.class);						
					}else{//异常
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
	 * 18.入塔状态 /maze.php?do=Info
	 * 'Name':'天空之塔第1层','BoxNum':2,'MonsterNum':1,'RemainBoxNum':2,
	 * 'RemainMonsterNum':1,'Layer':1,'TotalLayer':5,'Map':{'IsFinish':false
	 * */
	public MazeInfo mysticalcard_maze_Info(String Cookie, String MapStageId, String Layer) throws Exception {
		this.v = this.v + 1;
		MazeInfo obj_data = null;
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/maze.php?do=Info&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageId="+MapStageId+"&Layer="+Layer;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
						JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());//小写
						obj_data = (MazeInfo)JSONObject.toBean(jbect_responseText, MazeInfo.class);			
					}else{//异常
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
	 * 19.打塔 /maze.php?do=Battle
	 * MapStageId=8&manual=0&OpenCardChip=1&ItemIndex=22&Layer=1
	 * MapStageId=8&manual=0&OpenCardChip=1&ItemIndex=17&Layer=2
	 * 一层有4*8=32格，除去左上角右下角的灯台以外就有30
	 * */
	public boolean mysticalcard_maze_Battle(String Cookie, String MapStageId, String ItemIndex, String Layer) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		boolean battleflag = false;
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/maze.php?do=Battle&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageId="+MapStageId+"&manual=0&OpenCardChip=1&ItemIndex="+ItemIndex+"&Layer="+Layer;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
						if(status==1){//正常
							JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());
							String responseText_data = jbect_responseText.get("data").toString().toLowerCase();//小写
							JSONObject jbect_data = JSONObject.fromObject(responseText_data.toLowerCase());
							String data_win = jbect_data.get("win").toString();
							if(data_win.equals("1")){
								battleflag = true;//成功
							}
							this.Energy =  this.Energy -2;//不管成功失败,体力总会减少
						}else{//异常
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
	 * 20.重置塔 /maze.php?do=Reset
	 * */
	public boolean mysticalcard_maze_Reset(String Cookie, String MapStageId) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		boolean resetflag = false;
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/maze.php?do=Reset&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageId="+MapStageId;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
						JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());
						if(jbect_responseText.getString("status").equals("1")){
							resetflag = true;//重置成功
						}
					}else{//异常
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
	 * 21.地下城 dungeon.php?do=GetUserDungeon 判断
	 * RaidsLayer > 0 就可以扫荡
	 * RaidsStatus 0表示没有扫荡过 1表示扫荡过
	 * get(data).get(UserDungeon).get(RaidsLayer)
	 * get(data).get(UserDungeon).get(RaidsStatus)
	 * Resurrection 免费复活次数 -1表示没有
	 * get(data).get(UserDungeon).get(Resurrection)
	 * CurrentLayer 当前打下的层数
	 * get(data).get(UserDungeon).get(CurrentLayer)
	 * */
	public String mysticalcard_dungeon_GetUserDungeon(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/dungeon.php?do=GetUserDungeon&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
						int RaidsLayer = Integer.valueOf(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).getString("UserDungeon")).getString("RaidsLayer"));
						int RaidsStatus = Integer.valueOf(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).getString("UserDungeon")).getString("RaidsStatus"));
						int Resurrection = Integer.valueOf(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).getString("UserDungeon")).getString("Resurrection"));
						int CurrentLayer = Integer.valueOf(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).getString("UserDungeon")).getString("CurrentLayer"));
						this.sb_log.append("RaidsLayer:"+RaidsLayer+" RaidsStatus:"+RaidsStatus+" Resurrection:"+Resurrection+" CurrentLayer:"+CurrentLayer);
						this.sb_log.append("\r\n");
						//如果时间在8到23点之间
						Date n1 = new Date();
						SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMdd");
						SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMddHH");
						String nn1 = format2.format(n1);
						String nn2 = nn1+"08";
						String nn3 = nn1+"23";
						//8点到23点之间
						if(n1.after(format1.parse(nn2)) && n1.before(format1.parse(nn3))){
							if(RaidsLayer>0 && RaidsStatus==0){//可以扫荡 并且没有扫荡过							
								mysticalcard_dungeon_Sweep(Cookie);//进行扫荡
							}else if(Resurrection>-1){//还有复活次数 攻打地下城
								mysticalcard_dungeon_Fight(Cookie, CurrentLayer+1);//攻打地下城
							}else{
//								System.out.println(this.Nick+"已经扫荡过 地下城复活次数为0");
								this.sb_log.append(this.Nick+"已经扫荡过 地下城复活次数为0");
								this.sb_log.append("\r\n");
							}
						}else{
//							System.out.println("时间不在8点到23点之间,不能攻打地下城");
							this.sb_log.append("时间不在8点到23点之间,不能攻打地下城");
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
	 * 22.地下城 dungeon.php?do=Sweep 扫荡
	 * */
	public String mysticalcard_dungeon_Sweep(String Cookie) throws Exception {

		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/dungeon.php?do=Sweep&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
//						System.out.println(this.Nick+"扫荡完成");
						this.sb_log.append(this.Nick+"扫荡完成");
						this.sb_log.append("\r\n");
						mysticalcard_dungeon_GetUserDungeon(Cookie);//查看地下城 是否还能攻打
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
	 * 23.地下城 dungeon.php?do=Fight 攻打地下城
	 * isManual=0&Layer=21
	 * */
	public String mysticalcard_dungeon_Fight(String Cookie, int Layer) throws Exception {

		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/dungeon.php?do=Fight&v="+String.valueOf(this.v)+this.version;
				String data = "isManual=0&Layer="+Layer;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
						int win = (Integer)JSONObject.fromObject(JSONObject.fromObject(responseText).getString("data")).get("Win");
						this.sb_log.append(this.Nick+"攻打地下城第"+Layer+"层 win:"+win);
						this.sb_log.append("\r\n");
						mysticalcard_dungeon_GetUserDungeon(Cookie);//查看地下城 是否还能攻打
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
	 * 24.探索 mapstage.php?do=Explore
	 * */
	public String mysticalcard_Explore(String Cookie, String MapStageDetailId) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/mapstage.php?do=Explore&v="+String.valueOf(this.v)+this.version;
				String data = "MapStageDetailId="+MapStageDetailId;
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
				if(this.Energy>=2) {//如果有体力
					responselist = openSync(url, data, headers);
					if(responselist!=null && responselist.size()>2 && responselist.get(2).toString().length()>0){
						responseText = responselist.get(2).toString();
						int status = (Integer)JSONObject.fromObject(responseText).get("status");
						if(status==1){//正常
							this.Energy =  this.Energy -2;//不管成功失败,体力总会减少
						}else{//异常
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
	 * 25.打贼 arena.php?do=ThievesFight
	 * */
	public String mysticalcard_ThievesFight(String Cookie) throws Exception {
		return null;
	}

	/**
	 * 26.查看兽人商店 devoteMazeActivity.php?do=GetActStatus
	 * */
	public String mysticalcard_GetActStatus(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/devoteMazeActivity.php?do=GetActStatus&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
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
				        if(num>0){//还有东西未兑换
				        	mysticalcard_GetReward(Cookie);//兑奖
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
	 * 27.领取兽人商店奖励 devoteMazeActivity.php?do=GetReward
	 * */
	public String mysticalcard_GetReward(String Cookie) throws Exception {
		this.v = this.v + 1;
		String responseText = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/devoteMazeActivity.php?do=GetReward&v="+String.valueOf(this.v)+this.version;
				String data = "";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", this.mysticalcardhost);
				if(!this.proxy_flag){//没有开启代理
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
					if(status==1){//正常
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
	 * 获得返回头部
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
	 * 获得返回内容文本
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
	 * 获得返回内容xml
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
	 * post方式发送报文
	 * @param _url 地址
	 * @param _data 数据
	 * @param _requestheaders 头
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
					urlcon.setConnectTimeout(Constants.connectTimeout);//连接主机超时(单位毫秒)
					urlcon.setReadTimeout(Constants.connectTimeout);//从主机读取数据超时(单位毫秒)
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
	 * 写日志
	 * */
	public void logInfo() {
		FileWriter fw = null;
    	BufferedWriter bw = null;	
//      FileReader fr = null;
//    	BufferedReader br = null;
    	String filename = Constants.rootPath+Constants.spltstr+"ALL-LOGS"+Constants.spltstr+this.Nick+".txt";
    	if(this.has_next){
			try {
				SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间格式				
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
	 * 查看塔的状态刷塔 Show 并根据状态进行刷塔或重置
	 * @param MapStageId 塔id(6,7,8)塔
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
//			System.out.println(this.Nick+" "+"开始自动刷"+MapStageId+"塔");
			this.sb_log.append("开始自动刷"+MapStageId+"塔");
			this.sb_log.append("\r\n");
			try{
				//show
				MazeShow mazeshow_obj = bean.mysticalcard_maze_Show(Cookie, MapStageId);
				if(mazeshow_obj!=null){
					int Layer = mazeshow_obj.getData().getLayer();//当前层数
					int clear = mazeshow_obj.getData().getClear();//整个塔是否打完
					int freereset = mazeshow_obj.getData().getFreereset();//免费重置次数
					this.sb_log.append("当前层数Layer:"+Layer);
					this.sb_log.append("\r\n");
					this.sb_log.append("塔是否清空clear:"+clear);
					this.sb_log.append("\r\n");
					this.sb_log.append("塔是否还能免费重置freereset:"+freereset);
					this.sb_log.append("\r\n");
					//info
					if(clear==0){//塔还没清完
						this.sb_log.append(MapStageId+"塔还没清完");
						this.sb_log.append("\r\n");
						autoBattleByLayer(MapStageId, Layer, Cookie, bean);
					}else if(clear==1 && freereset==1){//塔清完了,但还能免费重置
//						System.out.println(this.Nick+" "+MapStageId+"塔清完了,但还能免费重置");
						this.sb_log.append(MapStageId+"塔清完了,但还能免费重置");
						this.sb_log.append("\r\n");
						boolean reset_flag = bean.mysticalcard_maze_Reset(Cookie, MapStageId);//重置
						if(reset_flag){//重置成功
							autoBattle(MapStageId, Cookie, bean);//继续调用自身进行打塔
						}
					}else{
						this.sb_log.append(MapStageId+"塔清完,并且免费重置过了");
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
	 * 根据层数自动刷塔 Info Battle
	 * @param MapStageId 塔id(6,7,8)塔
	 * @param Layer 层数id
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
//			System.out.println(this.Nick+" "+"开始自动刷"+MapStageId+"塔 第"+Layer+"层");
			this.sb_log.append("开始自动刷"+MapStageId+"塔 第"+Layer+"层");
			this.sb_log.append("\r\n");
			try{
				MazeInfo mazeinfo_obj = bean.mysticalcard_maze_Info(Cookie, MapStageId, String.valueOf(Layer));
				if(mazeinfo_obj!=null){
					boolean isIsfinish = mazeinfo_obj.getData().getMap().isIsfinish();//当前层是否打完
					int totalLayer = mazeinfo_obj.getData().getTotallayer();//当前塔的总层数
					if(!isIsfinish){//本层迷宫未打
						this.sb_log.append("本层迷宫未打完");
						this.sb_log.append("\r\n");
						int[] WallRows = mazeinfo_obj.getData().getMap().getWallrows();//竖条 7*4 顺序横
						int[] WallCols = mazeinfo_obj.getData().getMap().getWallcols();//横条 8*3 顺序竖
						int[] Items = mazeinfo_obj.getData().getMap().getItems();//怪物 箱子 上楼 下楼
						MazeTestNoView mazeObj = new MazeTestNoView(WallRows, WallCols, Items);
						boolean is_win = false;//怪物和箱子的战斗结果
						//如果有怪物和箱子 开始走迷宫
						if(mazeObj.getEndjSize()>0){
							mazeObj.StartMaze();//开始走迷宫
							while(mazeObj!=null && mazeObj.getEndjSize()>0 && this.has_next){//while必须加上has_next 可能其他异常
								Thread.sleep(500);
								//如果迷宫走完 终点列表的数据就都被移除了
								if(mazeObj.getEndjSize()==0)break;
							}
							//迷宫走完,准备开打
							for(int im=0;im<mazeObj.getOrderlist().size();im++){
								Thread.sleep(500);
								int ItemIndex = mazeObj.getOrderlist().get(im).getItemindex();
								this.sb_log.append("Item:"+mazeObj.getOrderlist().get(im).getItem()+" ");
								this.sb_log.append("Itemindex:"+ItemIndex+" ");
								this.sb_log.append("按顺序开始消灭怪物和箱子 ");
								this.sb_log.append("\r\n");
								//按顺序开始消灭怪物和箱子
								is_win = bean.mysticalcard_maze_Battle(Cookie, MapStageId, String.valueOf(ItemIndex), String.valueOf(Layer));
								this.sb_log.append("战斗结果:"+is_win);
								this.sb_log.append("\r\n");
								while(!is_win && bean.Energy>=2 && this.has_next){//没有战斗成功 并且有体力 可能网络异常或其他异常需要加上has_next
									this.sb_log.append("没有战斗成功 并且有体力 ");
									this.sb_log.append("\r\n");
									Thread.sleep(500);
									//继续打怪物或箱子,直到成功或者没有体力
									is_win = bean.mysticalcard_maze_Battle(Cookie, MapStageId, String.valueOf(ItemIndex), String.valueOf(Layer));
									this.sb_log.append("战斗结果:"+is_win);
									this.sb_log.append("\r\n");
								}
								if(!is_win){//没体力了
//									System.out.println(this.Nick+" "+"退出怪物和箱子循环 因为没体力了");
									this.sb_log.append("退出怪物和箱子循环 因为没体力了");
									this.sb_log.append("\r\n");
									break;//退出 因为没体力了
								}
							}
						}else{//没有怪物和箱子 只剩楼梯
							this.sb_log.append("没有怪物和箱子 只剩楼梯");
							this.sb_log.append("\r\n");
							is_win = true;
						}
						//打完怪物和箱子 准备上楼
						boolean is_up_win = false;//上楼战斗是否成功
						if(is_win && bean.Energy>=2){//战斗成功并且有体力
							MazeEnd UpLayerObj = mazeObj.getUpLayerObj();//上楼对象
							if(UpLayerObj!=null){
								this.sb_log.append("有上楼对象,可以上楼 打完怪物和箱子 战斗成功并且有体力 准备上楼");
								this.sb_log.append("\r\n");
								int up_ItemIndex = mazeObj.getUpLayerObj().getItemindex();								
								this.sb_log.append("上楼的位置:"+up_ItemIndex);
								this.sb_log.append("\r\n");
								is_up_win = bean.mysticalcard_maze_Battle(Cookie, MapStageId, String.valueOf(up_ItemIndex), String.valueOf(Layer));
								while(!is_up_win && bean.Energy>=2 && this.has_next){//没有战斗成功 并且有体力 可能网络异常或其他异常需要加上has_next
									this.sb_log.append("上楼没有战斗成功 并且有体力");
									this.sb_log.append("\r\n");
									Thread.sleep(500);
									//继续上楼,直到成功或者没有体力
									is_up_win = bean.mysticalcard_maze_Battle(Cookie, MapStageId, String.valueOf(up_ItemIndex), String.valueOf(Layer));
									this.sb_log.append("上楼战斗结果:"+is_up_win);
									this.sb_log.append("\r\n");
								}
								if(is_up_win && bean.Energy>=2){//上楼战斗成功并且有体力
									this.sb_log.append("上楼战斗成功并且有体力 上楼");
									this.sb_log.append("\r\n");
									if(Layer<totalLayer){//如果当前层小于总层数,就上楼
										int upLayer = Layer+1;
										this.sb_log.append("当前"+Layer+"层小于总层数("+totalLayer+"),就上"+upLayer+"楼");
										this.sb_log.append("\r\n");
										autoBattleByLayer(MapStageId, upLayer, Cookie, bean);//继续调用自身进行打塔
									}
								}
							}else{
								this.sb_log.append("没有上楼对象,查看塔的状态");
								this.sb_log.append("\r\n");
								//没有上楼对象的时候可以查看塔的状态
								autoBattle(MapStageId, Cookie, bean);
							}
						}else{
//							System.out.println(this.Nick+" "+"没有体力 没法上楼");
							this.sb_log.append(this.Nick+" "+"没有体力 没法上楼");
							this.sb_log.append("\r\n");
						}
					}else{//上楼或下楼
						this.sb_log.append("上楼或下楼");
						this.sb_log.append("\r\n");
						if(Layer<totalLayer){//如果当前层小于总层数,上楼
							int upLayer = Layer+1;
							this.sb_log.append("当前"+Layer+"层小于总层数("+totalLayer+"),上"+upLayer+"楼");
							this.sb_log.append("\r\n");
							autoBattleByLayer(MapStageId, upLayer, Cookie, bean);//继续调用自身进行打塔
						}else{//当前层数等于总层数 下楼
							//下楼需要设置个下楼信号 当show的时候重置这个信号为上楼即可
							this.sb_log.append("当前层数等于总层数 需要下楼?");
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
