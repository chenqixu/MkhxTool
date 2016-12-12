package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Http91Client extends Http360Client {

	/**
	 * 构造
	 * */
	public Http91Client() {
		super();//继承
		this.version = "&phpp=ANDROID&phpl=ZH_CN&pvc=1.4.0&pvb=2013-11-22%2009%3A59%3A27";//版本
	}
	
	public String usercenterUp(String data){
		String responseText = "";
		try {
			String url = "http://service.sj.91.com/usercenter/UP.aspx";
			Map<String, String> headers = new LinkedHashMap<String, String>();
			headers.put("Host", "service.sj.91.com");
//			if(!this.proxy_flag){//没有开启代理
//				headers.put("Accept-Encoding", "deflate, gzip");
//			}
			headers.put("CONNECTION", "Keep-Alive");
			headers.put("ACCEPT", "*/*");
			headers.put("User-Agent", "Dalvik/1.6.0 (Linux; U; Android 4.1.2; MI 1S MIUI/JMACNAH1.0)");
			headers.put("ACCEPT-LANGUAGE", "zh-cn");
			headers.put("ACCEPT-CHARSET", "UTF-8");
			headers.put("CONTENT-TYPE", "application/x-www-form-urlencoded");
			List responselist = null;
			byte[] _data = {0, 0, 0, 1, 0, 0, 0, 4, 55, 55, 102, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 123, 34, 73, 77, 69, 73, 34, 58, 34, 56, 54, 97, 106, 55, 55, 117, 103, 52, 55, 100, 111, 48, 49, 104, 101, 50, 53, 106, 107, 57, 49, 107, 119, 50, 49, 115, 103, 56, 34, 44, 34, 83, 105, 103, 110, 34, 58, 34, 56, 48, 53, 99, 100, 56, 57, 100, 101, 52, 56, 101, 57, 53, 50, 98, 98, 48, 49, 101, 100, 54, 53, 48, 102, 50, 97, 57, 55, 49, 57, 52, 101, 54, 53, 100, 100, 57, 98, 54, 53, 54, 100, 49, 56, 56, 98, 99, 56, 55, 97, 53, 51, 51, 49, 53, 48, 102, 49, 51, 56, 54, 102, 50, 51, 97, 97, 56, 102, 53, 49, 99, 97, 102, 51, 102, 100, 51, 49, 55, 53, 55, 102, 57, 99, 57, 48, 48, 48, 102, 99, 100, 48, 52, 57, 49, 52, 102, 48, 97, 54, 99, 100, 54, 52, 99, 102, 97, 51, 50, 52, 50, 48, 49, 55, 48, 53, 101, 52, 53, 57, 57, 50, 54, 55, 48, 51, 52, 34, 44, 34, 80, 108, 97, 116, 102, 111, 114, 109, 86, 101, 114, 115, 105, 111, 110, 34, 58, 34, 51, 46, 50, 46, 54, 46, 49, 34, 44, 34, 73, 77, 83, 73, 34, 58, 34, 52, 54, 97, 109, 48, 48, 120, 117, 48, 57, 103, 114, 51, 48, 100, 118, 53, 51, 108, 120, 56, 56, 117, 97, 54, 48, 109, 111, 57, 34, 44, 34, 80, 117, 98, 75, 101, 121, 34, 58, 34, 66, 51, 56, 66, 57, 70, 53, 68, 52, 50, 68, 69, 70, 48, 66, 68, 69, 70, 48, 54, 55, 68, 51, 48, 48, 57, 66, 49, 69, 57, 50, 52, 55, 53, 69, 49, 51, 48, 51, 57, 57, 67, 57, 68, 67, 55, 67, 67, 51, 49, 70, 48, 51, 54, 49, 68, 54, 53, 56, 49, 68, 48, 50, 52, 53, 67, 66, 51, 65, 69, 53, 54, 54, 52, 68, 57, 51, 51, 55, 68, 57, 51, 55, 48, 67, 53, 67, 67, 56, 52, 50, 68, 57, 51, 54, 50, 70, 52, 70, 53, 49, 65, 50, 53, 57, 68, 68, 70, 57, 50, 56, 48, 56, 48, 52, 53, 55, 65, 52, 48, 69, 54, 56, 50, 65, 50, 66, 66, 34, 44, 34, 83, 99, 114, 101, 101, 110, 72, 101, 105, 103, 104, 116, 34, 58, 34, 52, 56, 48, 34, 44, 34, 83, 105, 103, 110, 65, 112, 112, 75, 101, 121, 34, 58, 34, 52, 54, 54, 97, 99, 97, 52, 57, 52, 49, 53, 102, 102, 54, 102, 102, 48, 57, 101, 98, 51, 50, 101, 100, 101, 54, 100, 100, 101, 50, 48, 48, 49, 97, 99, 48, 98, 50, 49, 102, 52, 98, 55, 100, 101, 101, 97, 50, 51, 55, 49, 102, 50, 102, 49, 99, 57, 48, 57, 99, 50, 101, 51, 101, 97, 97, 51, 56, 52, 57, 51, 98, 50, 99, 102, 51, 100, 52, 100, 49, 102, 99, 101, 49, 98, 53, 53, 97, 48, 101, 49, 102, 48, 49, 101, 50, 98, 97, 54, 48, 54, 98, 50, 98, 98, 49, 52, 48, 102, 49, 101, 51, 34, 44, 34, 80, 104, 111, 110, 101, 84, 121, 112, 101, 34, 58, 34, 77, 73, 32, 49, 83, 34, 44, 34, 80, 108, 97, 116, 102, 111, 114, 109, 34, 58, 34, 56, 34, 44, 34, 83, 99, 114, 101, 101, 110, 87, 105, 100, 116, 104, 34, 58, 34, 56, 53, 52, 34, 125};
			responselist = openSync(url, _data.toString(), headers);
			if(responselist!=null && responselist.size()>2){
				responseText = responselist.get(2).toString();
				System.out.println(responseText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		System.out.println(java.net.URLDecoder.decode("+++%0B+++%126b2d++++++++++++++++++++"));
		return responseText;
	}
	
	/**
	 * 1.data传入http://91passport.mysticalcard.com/mpassport.php?do
	 * */
	public String mpassport(String data, String _data) {
		String url = "http://91passport.mysticalcard.com/mpassport.php?do=ndlogin&v="+String.valueOf(this.v)+this.version;
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
		
		String Cookie = "";
		if(responselist!=null && responselist.size()>1){
			System.out.println(util.StringUtil.unicodeToString(responselist.get(2).toString()));
			Cookie = ((Map)responselist.get(1)).get("Set-Cookie").toString();
			int ix = Cookie.indexOf(";");
			int jx = Cookie.indexOf("_sid=");
			Cookie = Cookie.substring(jx+"_sid=".length(), ix);
		}
		return Cookie;
	}
	

	/**
	 * 2.正式登陆http://s4.mysticalcard.com/login.php?do获得Cookie
	 * */
	public String mysticalcard_login(String data) throws Exception {
		String Cookie = "";
		String url = "http://s4.mysticalcard.com/login.php?do=NineOneLogin&v="+String.valueOf(this.v+1)+this.version;
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
			System.out.println(util.StringUtil.unicodeToString(responselist.get(2).toString()));
			Cookie = ((Map)responselist.get(1)).get("Set-Cookie").toString();
			int ix = Cookie.indexOf(";");
			Cookie = Cookie.substring(1, ix);
		}
		return Cookie;
	}
	
	public static void main(String[] args) {
		try{
			String proxy_ip = "127.0.0.1";
			String proxy_port = "8087";
			Properties prop = System.getProperties();
			//设置http访问要使用的代理服务器的地址
			prop.setProperty("http.proxyHost", proxy_ip);
			//设置http访问要使用的代理服务器的端口 
			prop.setProperty("http.proxyPort", proxy_port);
			
//			Http91Client client = new Http91Client();
//			client.setNick("dlink163");
//			client.usercenterUp("");
//			client.usercenterUp("                                   b977?                   {\"IMEI\":\"86aj77ug47do01he25jk91kw21sg8\",\"Sign\":\"805cd89de48e952bb01ed650f2a97194e65dd9b656d188bc87a533150f1386f23aa8f51caf3fd31757f9c9000fcd04914f0a6cd64cfa324201705e4599267034\",\"PlatformVersion\":\"3.2.6.1\",\"IMSI\":\"46am00xu09gr30dv53lx88ua60mo9\",\"PubKey\":\"B38B9F5D42DEF0BDEF067D3009B1E92475E130399C9DC7CC31F0361D6581D0245CB3AE5664D9337D9370C5CC842D9362F4F51A259DDF928080457A40E682A2BB\",\"ScreenHeight\":\"480\",\"SignAppKey\":\"466aca49415ff6ff09eb32ede6dde2001ac0b21f4b7deea2371f2f1c909c2e3eaa38493b2cf3d4d1fce1b55a0e1f01e2ba606b2bb140f1e3\",\"PhoneType\":\"MI 1S\",\"Platform\":\"8\",\"ScreenWidth\":\"854\"}");
//			client.usercenterUp("{\"Uin\":\"443772644\",\"GetEmotion\":\"1\",\"GetPoint\":\"1\",\"GetBaseInfo\":\"1\"}");
//			client.mpassport("sessionId=ec2aaeb27e807327b78c9112aa7bb621&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&sign=cc17347c6a4b6466f0b7dd2d1f8b99af&loginUin=443772644&nickName=dlink163", "");
//			String cookie = client.mysticalcard_login("sessionId=3d0b6577fa756753d10f8193bacb0333&newguide=1&Devicetoken=&loginUin=443772644&Origin=91ND&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&nickName=dlink163&IDFA=");
//			System.out.println(cookie);
//			client.logInfo();
//			System.out.println(util.StringUtil.unicodeToString("SessionId\u65e0\u6548"));
			
			String[][] userParad = {
				{"sessionId=0e17057e4081251c75b91571119d8d43&newguide=1&Devicetoken=&loginUin=443772644&Origin=91ND&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&nickName=dlink163&IDFA=","dlink163"}
				,{"sessionId=02e8d67b944a53f83cef84217a2e946d&newguide=1&Devicetoken=&loginUin=443732375&Origin=91ND&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&nickName=dlink164&IDFA=","dlink164"}
			};
			for(int i=0;i<userParad.length
			;i++){
				Http91ClientThread thread = new Http91ClientThread(userParad[i][0], userParad[i][1]);
				new Thread(thread).start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
