package client;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.frame.Constants;

import net.sf.json.JSONObject;
import bean.json.HttpService;
import bean.json.HttpServiceReturnObjs;

public class HttpMysticalcardClient extends Http360Client {

	/**
	 * 构造
	 * */
	public HttpMysticalcardClient(String _mysticalcardhost) {
		super();//继承
		//this.version = "&phpp=ANDROID&phpl=ZH_CN&pvc=1.4.1&pvb=2014-01-10%2017%3A39%3A16";//版本
		this.version = Constants.getInstance().getVersion_gw();
		this.mysticalcardhost = _mysticalcardhost;//服务器
	}
	
	/**
	 * 1.通过username和callPara获得登陆的key和timestamp
	 * */
	public HttpServiceReturnObjs mysticalcard_httpService(String username, String callPara){
		HttpServiceReturnObjs objs = null;
		try{
			String url = "http://bj.muhepp.com/pp/httpService.do";
			String data = "{\"serviceName\":\"checkUserActivedBase64Json\",\"callPara\":\""+callPara+"\"}";
			Map<String, String> headers = new LinkedHashMap<String, String>();
			headers.put("Host", "bj.muhepp.com");
			if(!this.proxy_flag){//没有开启代理
				headers.put("Accept-Encoding", "deflate, gzip");
			}
			headers.put("Proxy-Connection", "Keep-Alive");
			headers.put("Referer", "http://bj.muhepp.com/pp/start.do?udid=C4:6A:B7:86:BE:51&idfa=&locale=CHS&gameName=CARD-ANDROID-CHS&client=flash");
			headers.put("Origin","http://bj.muhepp.com");
			headers.put("X-Requested-With","XMLHttpRequest");
			headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			headers.put("Accept", "text/plain, */*; q=0.01");
			headers.put("User-Agent", "Mozilla/5.0 (Linux; U; Android 4.1.2; zh-cn; MI 1S Build/JZO54K) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
			headers.put("Accept-Language", "zh-CN, en-US");
			headers.put("Accept-Charset", "utf-8, iso-8859-1, utf-16, *;q=0.7");
			headers.put("Cookie", "mh_nickname="+username+"; mh_password=12345ddd; org.springframework.web.servlet.i18n.CookieLocaleResolver.LOCALE=chs");
			List responselist = null;
			responselist = openSync(url, data, headers);	
			String responseText = "";
			if(responselist!=null && responselist.size()==3 && responselist.get(2)!=null){
				responseText = responselist.get(2).toString();
				JSONObject jobjresponseText = JSONObject.fromObject(responseText.toLowerCase());
				HttpService obj = (HttpService)JSONObject.toBean(jobjresponseText, HttpService.class);
				objs = obj.getReturnobjs();
//				System.out.println(obj.getReturnobjs().getKey());
//				System.out.println(obj.getReturnobjs().getTimestamp());
			}
		}catch(Exception e){
			this.sb_log.append(e.toString());
			this.sb_log.append("\r\n");
			this.logInfo();
			this.has_next = false;
		}
		return objs;
	}
	
	/**
	 * 2.正式登陆http://s4.mysticalcard.com/login.php?do获得Cookie
	 * */
	public String mysticalcard_login_gf(String[] params) throws Exception {
		this.v = this.v + 1;
		String Cookie = "";
		try{
			if(this.has_next) {//能否继续执行
				String url = "http://"+this.mysticalcardhost+"/login.php?do=PassportLogin&v="+String.valueOf(this.v)+this.version;
				String data = /*"PP_key="+params[0]
				+"&PP_GS_IP="+params[1]
				+"&PP_userName="+params[2]
				+"&PP_source="+params[2]
				+"&PP_U_ID="+params[3]
				+"&PP_GS_CHAT_IP="+params[4]
				+"&PP_GS_DESC="+params[5]
				+"&PP_GS_NAME="+params[6]
				+"&PP_timestamp="+params[7]
				+"&UserName="+params[2]
				+"&Password="+params[3]
				+"&key="+params[0]
				+"&time="+params[7]
 				+"&PP_uEmailState=1"
				+"&PP_friendCode=null"
				+"&PP_G_TYPE=1"
				+"&PP_GS_CHAT_PORT=8000"
				+"&PP_GS_PORT=80"
				+"&newguide=1"
				+"&Devicetoken="
				+"&Origin=com"
				+"&IDFA="
				+"&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51";*/
				"PP%5FU%5FID="+params[3]
	                 +"&PP%5FfriendCode=null"
	                 +"&PP%5FGS%5FNAME="+params[6]
	                 +"&PP%5FuEmailState=1"
	                 +"&PP%5FGS%5FDESC="+params[5]
	                 +"&time="+params[7]
	                 +"&PP%5Ftimestamp="+params[7]
	                 +"&Password="+params[3]
	                 +"&PP%5Fkey="+params[0]
	                 +"&key="+params[0]
	                 +"&newguide=1"
	                 +"&PP%5FGS%5FCHAT%5FPORT=8000"
	                 +"&Origin=com"
	                 +"&IDFA="
	                 +"&PP%5FGS%5FPORT=80"
	                 +"&PP%5FGS%5FCHAT%5FIP="+params[4]
	                 +"&UserName="+params[2]
	                 +"&Devicetoken="
	                 +"&PP%5FuserName="+params[2]
	                 +"&PP%5FG%5FTYPE=1"
	                 +"&PP%5Fsource="+params[2]
	                 +"&Udid=C4%3A6A%3AB7%3A86%3ABE%3A51"
	                 +"&PP%5FGS%5FIP="+params[1];
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
	
	public static void main(String[] args) {
		String[][] params = {
				//coutcin2004x@qq.com
				{"69730760caf6c350feed7de2e00d3d7f"//PP_key key
					,"http%3A%2F%2Fs4%2Emysticalcard%2Ecom%2F"//PP_GS_IP s4一样 s5估计也一样
					,"c4%3A6a%3Ab7%3A86%3Abe%3A51"//PP_userName PP_source UserName
					,"1740474"//PP_U_ID Password
					,"218%2E245%2E7%2E8"//PP_GS_CHAT_IP s4一样 s5估计也一样
					,"%E9%87%91%E5%B1%9E%E5%B7%A8%E9%BE%99"//PP_GS_DESC s4一样 s5估计也一样
					,"server4"//PP_GS_NAME s4一样 s5估计也一样
					,"1390918291211"}//PP_timestamp time
				//13959195930@193.com
				,{"572447c6ada0eacd67cb7d2efe658e19"//PP_key key
					,"http%3A%2F%2Fs4%2Emysticalcard%2Ecom%2F"//PP_GS_IP s4一样 s5估计也一样
					,"3c%3Ae0%3A72%3Ab9%3Adc%3A24"//PP_userName PP_source UserName
					,"1706866"//PP_U_ID Password
					,"218%2E245%2E7%2E8"//PP_GS_CHAT_IP s4一样 s5估计也一样
					,"%E9%87%91%E5%B1%9E%E5%B7%A8%E9%BE%99"//PP_GS_DESC s4一样 s5估计也一样
					,"server4"//PP_GS_NAME s4一样 s5估计也一样
					,"1390834844564"}//PP_timestamp time
				//dlink145
				,{"9fd2ec97cc87b599f20d308f42bb91c6"//PP_key key
					,"http%3A%2F%2Fs4%2Emysticalcard%2Ecom%2F"//PP_GS_IP s4一样 s5估计也一样
					,"dlink145"//PP_userName PP_source UserName
					,"2124410"//PP_U_ID Password
					,"218%2E245%2E7%2E8"//PP_GS_CHAT_IP s4一样 s5估计也一样
					,"%E9%87%91%E5%B1%9E%E5%B7%A8%E9%BE%99"//PP_GS_DESC s4一样 s5估计也一样
					,"server4"//PP_GS_NAME s4一样 s5估计也一样
					,"1390834933606"}//PP_timestamp time
				//coutcin2004@sina.com
				,{"e585bda1e688247cb8dab149f0c62863"//PP_key key
					,"http%3A%2F%2Fs5%2Emysticalcard%2Ecom%2F"//PP_GS_IP s4一样 s5估计也一样
					,"72%3A0e%3Ac3%3A6d%3A75%3A84"//PP_userName PP_source UserName
					,"2143811"//PP_U_ID Password
					,"218%2E245%2E7%2E9"//PP_GS_CHAT_IP s4一样 s5估计也一样
					,"%E8%8D%A3%E8%80%80%E5%B7%A8%E4%BA%BA"//PP_GS_DESC s4一样 s5估计也一样
					,"server5"//PP_GS_NAME s4一样 s5估计也一样
					,"1390921015529"}//PP_timestamp time
				//dlinkoxx331
				,{"f989d2281725bdf162063b26bc0acfb8"//PP_key key
					,"http%3A%2F%2Fs5%2Emysticalcard%2Ecom%2F"//PP_GS_IP s4一样 s5估计也一样
					,"dlinkooxx331"//PP_userName PP_source UserName
					,"1937511"//PP_U_ID Password
					,"218%2E245%2E7%2E9"//PP_GS_CHAT_IP s4一样 s5估计也一样
					,"%E8%8D%A3%E8%80%80%E5%B7%A8%E4%BA%BA"//PP_GS_DESC s4一样 s5估计也一样
					,"server5"//PP_GS_NAME s4一样 s5估计也一样
					,"1390834905776"}//PP_timestamp time
				//dlink151
				,{"3c3ab3c0e8818ac1caa6f93c29c75392"//PP_key key
					,"http%3A%2F%2Fs5%2Emysticalcard%2Ecom%2F"//PP_GS_IP s4一样 s5估计也一样
					,"dlink151"//PP_userName PP_source UserName
					,"2314486"//PP_U_ID Password
					,"218%2E245%2E7%2E9"//PP_GS_CHAT_IP s4一样 s5估计也一样
					,"%E8%8D%A3%E8%80%80%E5%B7%A8%E4%BA%BA"//PP_GS_DESC s4一样 s5估计也一样
					,"server5"//PP_GS_NAME s4一样 s5估计也一样
					,"1390834954064"}//PP_timestamp time
				//dlink152
				,{"d7b64de81c36b0b4315f977f72e20817"//PP_key key
					,"http%3A%2F%2Fs5%2Emysticalcard%2Ecom%2F"//PP_GS_IP s4一样 s5估计也一样
					,"dlink152"//PP_userName PP_source UserName
					,"4554825"//PP_U_ID Password
					,"218%2E245%2E7%2E9"//PP_GS_CHAT_IP s4一样 s5估计也一样
					,"%E8%8D%A3%E8%80%80%E5%B7%A8%E4%BA%BA"//PP_GS_DESC s4一样 s5估计也一样
					,"server5"//PP_GS_NAME s4一样 s5估计也一样
					,"1390834971410"}//PP_timestamp time
			};
		String[][] userParad = {
				{"coutcin2004x@qq.com","s4.mysticalcard.com","eyJ1c2VyTmFtZSI6ImNvdXRjaW4yMDA0eEBxcS5jb20iLCJ1c2VyUGFzc3dvcmQiOiIxMjM0NWRkZCIsImdhbWVOYW1lIjoiQ0FSRC1BTkRST0lELUNIUyIsInVkaWQiOiJDNDo2QTpCNzo4NjpCRTo1MSIsImlkZmEiOiIiLCJjbGllbnRUeXBlIjoiZmxhc2giLCJyZWxlYXNlQ2hhbm5lbCI6IiIsImxvY2FsZSI6ImNocyJ9"}
				,{"13959195930@139.com","s4.mysticalcard.com","eyJ1c2VyTmFtZSI6IjEzOTU5MTk1OTMwQDEzOS5jb20iLCJ1c2VyUGFzc3dvcmQiOiIxMjM0NWRkZCIsImdhbWVOYW1lIjoiQ0FSRC1BTkRST0lELUNIUyIsInVkaWQiOiJDNDo2QTpCNzo4NjpCRTo1MSIsImlkZmEiOiIiLCJjbGllbnRUeXBlIjoiZmxhc2giLCJyZWxlYXNlQ2hhbm5lbCI6IiIsImxvY2FsZSI6ImNocyJ9"}
				,{"dlink145","s4.mysticalcard.com","eyJ1c2VyTmFtZSI6ImRsaW5rMTQ1IiwidXNlclBhc3N3b3JkIjoiMTIzNDVkZGQiLCJnYW1lTmFtZSI6IkNBUkQtQU5EUk9JRC1DSFMiLCJ1ZGlkIjoiQzQ6NkE6Qjc6ODY6QkU6NTEiLCJpZGZhIjoiIiwiY2xpZW50VHlwZSI6ImZsYXNoIiwicmVsZWFzZUNoYW5uZWwiOiIiLCJsb2NhbGUiOiJjaHMifQ=="}
				,{"coutcin2004@sina.com","s5.mysticalcard.com","eyJ1c2VyTmFtZSI6ImNvdXRjaW4yMDA0QHNpbmEuY29tIiwidXNlclBhc3N3b3JkIjoiMTIzNDVkZGQiLCJnYW1lTmFtZSI6IkNBUkQtQU5EUk9JRC1DSFMiLCJ1ZGlkIjoiQzQ6NkE6Qjc6ODY6QkU6NTEiLCJpZGZhIjoiIiwiY2xpZW50VHlwZSI6ImZsYXNoIiwicmVsZWFzZUNoYW5uZWwiOiIiLCJsb2NhbGUiOiJjaHMifQ=="}
				,{"dlinkooxx331","s5.mysticalcard.com","eyJ1c2VyTmFtZSI6ImRsaW5rb294eDMzMSIsInVzZXJQYXNzd29yZCI6IjEyMzQ1ZGRkIiwiZ2FtZU5hbWUiOiJDQVJELUFORFJPSUQtQ0hTIiwidWRpZCI6IkM0OjZBOkI3Ojg2OkJFOjUxIiwiaWRmYSI6IiIsImNsaWVudFR5cGUiOiJmbGFzaCIsInJlbGVhc2VDaGFubmVsIjoiIiwibG9jYWxlIjoiY2hzIn0="}
				,{"dlink151","s5.mysticalcard.com","eyJ1c2VyTmFtZSI6ImRsaW5rMTUxIiwidXNlclBhc3N3b3JkIjoiMTIzNDVkZGQiLCJnYW1lTmFtZSI6IkNBUkQtQU5EUk9JRC1DSFMiLCJ1ZGlkIjoiQzQ6NkE6Qjc6ODY6QkU6NTEiLCJpZGZhIjoiIiwiY2xpZW50VHlwZSI6ImZsYXNoIiwicmVsZWFzZUNoYW5uZWwiOiIiLCJsb2NhbGUiOiJjaHMifQ=="}
				,{"dlink152","s5.mysticalcard.com","eyJ1c2VyTmFtZSI6ImRsaW5rMTUyIiwidXNlclBhc3N3b3JkIjoiMTIzNDVkZGQiLCJnYW1lTmFtZSI6IkNBUkQtQU5EUk9JRC1DSFMiLCJ1ZGlkIjoiQzQ6NkE6Qjc6ODY6QkU6NTEiLCJpZGZhIjoiIiwiY2xpZW50VHlwZSI6ImZsYXNoIiwicmVsZWFzZUNoYW5uZWwiOiIiLCJsb2NhbGUiOiJjaHMifQ=="}
			};
		try{
			for(int i=0;i<params.length
			;i++){
				HttpMysticalcardClientThread thread = new HttpMysticalcardClientThread(params[i],userParad[i][0],userParad[i][1],userParad[i][2]);
				new Thread(thread).start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
