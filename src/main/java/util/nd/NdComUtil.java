package util.nd;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import util.HexBin;
import net.sf.json.JSONObject;

public class NdComUtil {
	public static void main(String[] args) {
		// String.Format("{0}{1}{2}{3}{4}{5}{6}{7}{8}{9:0.00}{10:0.00}{11}{12}{13:yyyy-MM-dd
		// HH:mm:ss}{14}",
		// ConsumeStreamId, CooOrderSerial, MerchantId, AppId,
		// ProductName, Uin, GoodsId, GoodsInfo, GoodsCount,
		// OriginalMoney, OrderMoney, Note, PayStatus,
		// CreateTime, AppKey).HashToMD5Hex();
		// String proxy_ip = "127.0.0.1";
		// String proxy_port = "8087";
		// Properties prop = System.getProperties();
		// 设置http访问要使用的代理服务器的地址
		// prop.setProperty("http.proxyHost", proxy_ip);
		// 设置http访问要使用的代理服务器的端口
		// prop.setProperty("http.proxyPort", proxy_port);

		// NdComUtil nd = new NdComUtil();
		try {
			// int i =
			// nd.checkUserLogin("443772644","ec2aaeb27e807327b78c9112aa7bb621");
			// System.out.println(i);

//			byte bytes[] = { 0, 0, 0, 1, 0, 0, 0, 4, 98, 97, 99, 52, 0, 0, 0,
//					0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 123, 34,
//					82, 101, 115, 117, 108, 116, 34, 58, 34, -25, -67, -111,
//					-25, -69, -100, -24, -81, -73, -26, -79, -126, -23, -108,
//					-103, -24, -81, -81, -17, -68, -116, -24, -81, -73, -25,
//					-88, -115, -27, -112, -114, -23, -121, -115, -24, -81,
//					-107, 34, 125 };
//			String[] tmp1 = {"02","02","00","00","00","00","00","07","37","66","36","66","00","00","00","00","00","00","00","00","00","00","00","00","00","00",
//					"00","00","00","00","00","00","A9","F1","B3","EB","2B","AD","10","A0","88","B0","7E","CB","66","E7","69","27","4A","FE","7C","DE",
//					"31","59","04","38","0D","7F","F7","D3","F2","A2","01","4E","7E","9B","C2","EF","6B","7B","2E","16","C6","DD","48","80","E9","0F",
//					"F3","47","F6","C0","FE","9E","2B","92","F8","51","27","8C","C9","DA","07","B9","29","3F","0C","5A","A7","1A","7D","DF","A0","74",
//					"CF","70","C8","7A","A3","B2","A9","6C","FE","CD","4B","92","41","BB","EF","8A","52","7D","C0","CC","A3","AD","AA","39","5C","88",
//					"7A","BC","7E","8B","27","1E","4B","D8","B7","E6","69","1E","8E","07","3A","25","BB","65","E4","FE","61","B4","AB","9F","41","68",
//					"AE","E5","46","B1","85","BD","32","C9","B9","E3","78","3A","B5","5C","E1","8C","ED","52","B4","40","5F","5F","D5","1F","94","9F",
//					"43","2C","89","F2","12","4C","55","45","6D","07","63","40","8A","45","0A","8C","3E","57","23","50","D4","6A","D4","F2","36","B5",
//					"F6","6A","BD","C2","17","5F","A7","2F","8E","2C","BF","C3","48","98","2C","A9","DB","83","5A","3E","C2","E3","45","4C","5F","E8",
//					"01","B0","54","93","55","66","C3","47","AE","63","E7","17","52","38","70","93","04","0F","D8","08","FC","DE","86","80","FD","AF",
//					"86","B3","16","39","FE","2A","6D","8F","96","30","33","EF","B4","0F","77","97","64","1C","3A","62","97","82","F1","74","6E","8A",
//					"D0","CE","2E","38","D9","44","A1","E1","94","9E","68","5D","20","9C","B1","9C","6B","E9","FD","29","D1","69","88","94","3D","03",
//					"DB","71","64","F1","C3","E9","E5","1A","37","69","CE","1D","05","52","6A","B7","9B","9A","4A","52","E6","4A","23","A0","8F","D6",
//					"C0","0A","18","FE","95","9E","56","01","29","2B","78","FC","D4","2C"};
//			int[] tmp2 = new int[tmp1.length];
//			for(int i=0;i<tmp1.length;i++){
//				tmp2[i] = Integer.parseInt(tmp1[i], 16);
//			}
//			byte[] tmp3 = new byte[tmp1.length];
//			for(int i=0;i<tmp1.length;i++){
//				tmp3[i] = (byte) tmp2[i];
//			}
//			// com.nd.commplatform.d.c.hv hv = new
//			// com.nd.commplatform.d.c.hv(bytes);
//			// String str1 = hv.a("ResultCode");
//			// org.json.JSONObject localJSONObject = hv.c();
//			// System.out.println(str1);
//			hv hv = new hv(tmp3);
//			System.out.println(hv.b());
//			System.out.println(new String(hv.aa(bytes), "utf-8"));
////			System.out.println("cb85bac41348f8fb4fd3a975019c6a36");
			NdComUtil nu = new NdComUtil();
			System.out.println(nu.md5("MapStageId=8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 这里设置你的APPID
	private String appid = "100010";

	// 这里设置你的APPKEY
	private String appkey = "C28454605B9312157C2F76F27A9BCA2349434E546A6E9C75";

	// 91的服务器地址
	private String goUrl = "http://service.sj.91.com/usercenter/ap.aspx?";

	/**
	 * 查询支付购买结果的API调用
	 * 
	 * @param cooOrderSerial
	 *            商户订单号
	 * @return ERRORCODE的值
	 * @throws Exception
	 *             API调用失败
	 */
	public int queryPayResult(String cooOrderSerial) throws Exception {
		String act = "1";
		StringBuilder strSign = new StringBuilder();
		strSign.append(appid);
		strSign.append(act);
		strSign.append(cooOrderSerial);
		strSign.append(appkey);
		String sign = md5(strSign.toString());
		StringBuilder getUrl = new StringBuilder();
		getUrl.append("Appid=");
		getUrl.append(appid);
		getUrl.append("&Act=");
		getUrl.append(act);
		getUrl.append("&CooOrderSerial=");
		getUrl.append(cooOrderSerial);
		getUrl.append("&Sign=");
		getUrl.append(sign);
		return GetResult(HttpGetGo(getUrl.toString()));
	}

	/**
	 * 检查用户登陆SESSIONID是否有效
	 * 
	 * @param uin
	 *            91账号ID
	 * @param sessionID
	 * @return
	 * @throws Exception
	 */
	public int checkUserLogin(String uin, String sessionID) throws Exception {
		String act = "4";
		StringBuilder strSign = new StringBuilder();
		strSign.append(appid);
		strSign.append(act);
		strSign.append(uin);
		strSign.append(sessionID);
		strSign.append(appkey);
		String sign = md5(strSign.toString());
		StringBuilder getUrl = new StringBuilder();
		getUrl.append("Appid=");
		getUrl.append(appid);
		getUrl.append("&Act=");
		getUrl.append(act);
		getUrl.append("&Uin=");
		getUrl.append(uin);
		getUrl.append("&SessionId=");
		getUrl.append(sessionID);
		getUrl.append("&Sign=");
		getUrl.append(sign);
		System.out.println("getUrl:" + getUrl);
		return GetResult(HttpGetGo(getUrl.toString()));
	}

	/**
	 * 接收支付购买结果
	 * 
	 * @param appid
	 * @param act
	 * @param productName
	 * @param consumeStreamId
	 * @param cooOrderSerial
	 * @param uin
	 * @param goodsId
	 * @param goodsInfo
	 * @param goodsCount
	 * @param originalMoney
	 * @param orderMoney
	 * @param note
	 * @param payStatus
	 * @param createTime
	 * @param fromSign
	 * @return 支付结果
	 * @throws UnsupportedEncodingException
	 */
	public int payResultNotify(String appid, String act, String productName,
			String consumeStreamId, String cooOrderSerial, String uin,
			String goodsId, String goodsInfo, String goodsCount,
			String originalMoney, String orderMoney, String note,
			String payStatus, String createTime, String fromSign)
			throws UnsupportedEncodingException {

		StringBuilder strSign = new StringBuilder();
		strSign.append(appid);
		strSign.append(act);
		strSign.append(productName);
		strSign.append(consumeStreamId);
		strSign.append(cooOrderSerial);
		strSign.append(uin);
		strSign.append(goodsId);
		strSign.append(goodsInfo);
		strSign.append(goodsCount);
		strSign.append(originalMoney);
		strSign.append(orderMoney);
		strSign.append(note);
		strSign.append(payStatus);
		strSign.append(createTime);
		strSign.append(appkey);
		String sign = md5(strSign.toString());

		if (!this.appid.equals(appid)) {
			return 2; // appid无效
		}
		if (!"1".equals(act)) {
			return 3; // Act无效
		}
		if (!sign.toLowerCase().equals(fromSign.toLowerCase())) {
			return 5; // sign无效
		}
		int payResult = -1;
		if ("1".equals(payStatus)) {
			try {
				if (queryPayResult(cooOrderSerial) == 1) {
					payResult = 1; // 有订单
				} else {
					payResult = 11; // 无订单
				}
			} catch (Exception e) {
				payResult = 6; // 自定义：网络问题
			}
			return payResult;
		}
		return 0; // 错误
	}

	/**
	 * 获取91服务器返回的结果
	 * 
	 * @param jsonStr
	 * @return
	 * @throws Exception
	 */
	private int GetResult(String jsonStr) throws Exception {
		// Pattern p = Pattern.compile("(?<=\"ErrorCode\":\")\\d{1,3}(?=\")");
		// Matcher m = p.matcher(jsonStr);
		// m.find();
		// return Integer.parseInt(m.group());

		// 这里需要引入JSON-LIB包内的JAR
		System.out.println("jsonStr:" + jsonStr);
		JSONObject jo = JSONObject.fromObject(jsonStr);
		return Integer.parseInt(jo.getString("ErrorCode"));
	}

	/**
	 * 对字符串进行MD5并返回结果
	 * 
	 * @param sourceStr
	 * @return
	 */
	private String md5(String sourceStr) {
		String signStr = "";
		try {
			byte[] bytes = sourceStr.getBytes("utf-8");
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(bytes);
			byte[] md5Byte = md5.digest();
			if (md5Byte != null) {
				signStr = HexBin.encode(md5Byte);
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println("signStr:" + signStr);
		return signStr;
	}

	/**
	 * 发送GET请求并获取结果
	 * 
	 * @param getUrl
	 * @return
	 * @throws Exception
	 */
	private String HttpGetGo(String getUrl) throws Exception {
		StringBuffer readOneLineBuff = new StringBuffer();
		String content = "";
		System.out.println("goUrl + getUrl:" + goUrl + getUrl);
		URL url = new URL(goUrl + getUrl);
		URLConnection conn = url.openConnection();
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn
				.getInputStream(), "utf-8"));
		String line = "";
		while ((line = reader.readLine()) != null) {
			readOneLineBuff.append(line);
		}
		content = readOneLineBuff.toString();
		reader.close();
		System.out.println("content:" + content);
		return content;
	}
}
