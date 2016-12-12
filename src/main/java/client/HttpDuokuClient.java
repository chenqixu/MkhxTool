package client;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.frame.Constants;

import util.DuokuAesUtil;

import net.sf.json.JSONObject;

import bean.json.Mpassport;
import bean.json.MpassportUinfo;

public class HttpDuokuClient extends Http360Client {	
	
	/**
	 * 构造
	 * */
	public HttpDuokuClient() {
		super();//继承
		//this.version = "&phpp=ANDROID_DUOKU&phpl=ZH_CN&pvc=1.4.0&pvb=2013-12-02%2011%3A19%3A12";//版本
		this.version = Constants.getInstance().getVersion_duoku();
	}
	
	/**
	 * 重新获得sessionid
	 * */
	public String gameSDKLogin(String data){
		String sessionid = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://gamesdk.m.duoku.com/gamesdk/login";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", "gamesdk.m.duoku.com");
				headers.put("Accept-Encoding", "");
				headers.put("Accept", "*/*");
				headers.put("Content-Type", "text/plain");
				headers.put("encrypttype", "1");
				headers.put("Connection", "Keep-Alive");
				headers.put("Expect", "100-continue");
				List responselist = null;
				responselist = openSync(url, data, headers);
				String responseText = "";				
				if(responselist!=null && responselist.size()>2){
					responseText = responselist.get(2).toString();
					System.out.println("gameSDKLogin responseText:"+responseText);
					DuokuAesUtil duokuaes = new DuokuAesUtil();
					String real_responseText = duokuaes.b(responseText);
					System.out.println("gameSDKLogin real_responseText:"+real_responseText);
					JSONObject duoku_obj = JSONObject.fromObject(real_responseText);
			        if(duoku_obj.getString("errorcode").equals("0")){
			        	sessionid = duoku_obj.getString("sessionid");
			        	System.out.println("gameSDKLogin sessionid:"+sessionid);
			        }
				}
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return sessionid;
	}

	/**
	 * 1.data传入http://91passport.mysticalcard.com/mpassport.php?do
	 * */
	public Mpassport mpassport(String data) {
		Mpassport obj = null;
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://91passport.mysticalcard.com/mpassport.php?do=plogin&v="+String.valueOf(this.v)+this.version;
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
	 * 2.正式登陆http://s4.mysticalcard.com/login.php?do获得Cookie
	 * */
	public String mysticalcard_login(MpassportUinfo uinfo) throws Exception {
		this.v = this.v + 1;
		String Cookie = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://s4.mysticalcard.com/login.php?do=mpLogin&v="+String.valueOf(this.v)+this.version;
				String data = "nick="+uinfo.getNick()
					+"&IDFA="
					+"&uin="+uinfo.getUin()
					+"&MUid="+uinfo.getMUid()
					+"&plat=ANDROID%5FDUOKU"
					+"&Devicetoken="
					+"&newguide=1"
					+"&access_token="
					+"&Origin=duoku"
					+"&time="+uinfo.getTime()
					+"&sign="+uinfo.getSign()
					+"&ppsign="+uinfo.getPpsign()
					+"&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51";
				Map<String, String> headers = new LinkedHashMap<String, String>();
				headers.put("Host", "s4.mysticalcard.com");
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
	
	public static void main(String[] args) {
//		String[][] userParad = {
//				{"sessionId=71753CDE3B1D46C50CD7A9F6619BD8C0&userName=dlink160&IDFA=&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&plat=ANDROID%5FDUOKU&uid=33984369&newguide=1","dlink160"}
//				,{"sessionId=DBA2A473B0D7BA696E81E274769D357F&userName=dlink162&IDFA=&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&plat=ANDROID%5FDUOKU&uid=34211062&newguide=1","dlink162"}
//			};
//		try{
//			for(int i=0;i<userParad.length
//			;i++){
//				HttpDuokuClientThread thread = new HttpDuokuClientThread(userParad[i][0], userParad[i][1]);
//				new Thread(thread).start();
//			}
//		}catch(Exception e){
//			e.printStackTrace();
//		}
//		HttpDuokuClient dk = new HttpDuokuClient();
//		String session = dk.gameSDKLogin("7NrbOSd/AMBFZHHCzQk8ZoabP1r3O7jEaqlZTymvLJDHO6aF3KqzzqkCiGN4vMNsH861Tb1KT2NHhkJ43ZaSnSKe0Cf3EbTBqNq+h2nuj714Zr/m9bttcJoom3n15nqw6G9vz7v8+9kaUNzyHc7Dj0osF/zU3Sm6tC61FDhjNq9veOLPBnmZyM6hqNsKKJ6bz58dJWedWFzRB7ZNe0lIAH7dhv1tyJNV4Er6/hDS8D26g98/QG/uuQBjE5ZVWZIYFCUqMYo1pynihwk72lffmgoS8+FP+N1fwmb/fpzP1mKajJLjtj6xJ9TcMlGuARvOgtUWopuQ5ArMS1E5cBQAQDxUQQo0PDQot93CGVZ0FcXiabO2CC2pKl4BQ2XtFXn4kyS2io+j2kAQOM31CHsyNoxSz0qfLpg2EMOpGxIyAGCRAfW0DlEdi+dlo6GHw4rvj6TrreqFclisMrCnZrdL7gBsPMlzcP75DULa+RkbVCbXfVfmh9b3IJVNVhwlYTBD5HiHh9wp3OU6V8X9SpD52RCXSvgFDZqy62Y5TCe2rm0yV6AxvZ9FD4sJdWpisdCF");//dlink160
//		String session = dk.gameSDKLogin("7NrbOSd/AMBFZHHCzQk8ZoabP1r3O7jEaqlZTymvLJDHO6aF3KqzzqkCiGN4vMNsH861Tb1KT2NHhkJ43ZaSnSKe0Cf3EbTBqNq+h2nuj714Zr/m9bttcJoom3n15nqw6G9vz7v8+9kaUNzyHc7Dj0osF/zU3Sm6tC61FDhjNq9veOLPBnmZyM6hqNsKKJ6bz58dJWedWFzRB7ZNe0lIAH7dhv1tyJNV4Er6/hDS8D26g98/QG/uuQBjE5ZVWZIYFCUqMYo1pynihwk72lffmgoS8+FP+N1fwmb/fpzP1mKajJLjtj6xJ9TcMlGuARvOgtUWopuQ5ArMS1E5cBQAQDxUQQo0PDQot93CGVZ0FcWBPQxxYHqw9Pswv89QSXvikyS2io+j2kAQOM31CHsyNoxSz0qfLpg2EMOpGxIyAGCRAfW0DlEdi+dlo6GHw4rvj6TrreqFclisMrCnZrdL7gBsPMlzcP75DULa+RkbVCbXfVfmh9b3IJVNVhwlYTBD5HiHh9wp3OU6V8X9SpD52RCXSvgFDZqy62Y5TCe2rm0yV6AxvZ9FD4sJdWpisdCF");//dlink162
//		System.out.println(session);
//		String s = "7NrbOSd/AMBFZHHCzQk8ZoabP1r3O7jEaqlZTymvLJDHO6aF3KqzzqkCiGN4vMNsH861Tb1KT2NHhkJ43ZaSnSKe0Cf3EbTBqNq+h2nuj714Zr/m9bttcJoom3n15nqw6G9vz7v8+9kaUNzyHc7Dj0osF/zU3Sm6tC61FDhjNq9veOLPBnmZyM6hqNsKKJ6bz58dJWedWFzRB7ZNe0lIAH7dhv1tyJNV4Er6/hDS8D26g98/QG/uuQBjE5ZVWZIYFCUqMYo1pynihwk72lffmgoS8+FP+N1fwmb/fpzP1mKajJLjtj6xJ9TcMlGuARvOgtUWopuQ5ArMS1E5cBQAQDxUQQo0PDQot93CGVZ0FcWBPQxxYHqw9Pswv89QSXvikyS2io+j2kAQOM31CHsyNoxSz0qfLpg2EMOpGxIyAGCRAfW0DlEdi+dlo6GHw4rvj6TrreqFclisMrCnZrdL7gBsPMlzcP75DULa+RkbVCbXfVfmh9b3IJVNVhwlYTBD5HiHh9wp3OU6V8X9SpD52RCXSvgFDZqy62Y5TCe2rm0yV6AxvZ9FD4sJdWpisdCF";//dlink162
//		DuokuAesUtil d = new DuokuAesUtil();
//		System.out.println(d.b(s));
	}
}
