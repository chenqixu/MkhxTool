package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

public class HttpClientTest {
	String bocNo1="6124"; 
    String orderNos1="104110059475569"; 
    String signData1="eeb5705cffb3820d8cf8b6cae5774730_21ec2425-883b-4974-a036-360ddec9fb9a"+
"MIIDuAYJKoZIhvcNAQcCoIIDqTCCA6UCAQExCzAJBgUrDgMCGgUAMAsGCSqGSIb3DQEHAaCCAmww"+
"ggJoMIIB0aADAgECAhB9RyYZTkOnSvrpIjNNqTqUMA0GCSqGSIb3DQEBBQUAMFoxCzAJBgNVBAYT"+
"AkNOMRYwFAYDVQQKEw1CQU5LIE9GIENISU5BMRAwDgYDVQQIEwdCRUlKSU5HMRAwDgYDVQQHEwdC"+
"RUlKSU5HMQ8wDQYDVQQDEwZCT0MgQ0EwHhcNMTEwNjE4MTAyNjI0WhcNMjEwNDI2MTAyNjI0WjBH"+
"MQswCQYDVQQGEwJDTjEWMBQGA1UEChMNQkFOSyBPRiBDSElOQTENMAsGA1UECxMEVEVTVDERMA8G"+
"A1UEAx4IbUuL1VVGYjcwgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAMNF+o1mNobAG60gm9cG"+
"DbOuq5KLKsRF/jjstfjzorz1qQdiY5ibCu3ngk2VHxAf3JV7beDw7OuWjxIaxntsWiOaFhujSlxS"+
"7dyefk4uOwHWuFOoZGIG/scXcjU74NLdSM0ptj42SfdMsbqzcQ8kqvV7MbPqJW8ztlefmafdYpJh"+
"AgMBAAGjQjBAMB8GA1UdIwQYMBaAFHjxtvO9ykQNfC/o0jbI/gawwPmfMB0GA1UdDgQWBBS6HTP9"+
"uBZMvHzqidY/hp7m9hx0zTANBgkqhkiG9w0BAQUFAAOBgQAyLyYGKopiY0fSaTM/fElg/3JRrOcv"+
"8xrNNr5tdym61W44d3Uh53zD+5cOhQnQfYEE6d6QeiRicNi3kwh3mr9BX0+H7uBq4SQ9Gq99gk3E"+
"tdLe4EMIZbE01SPzKViUE2A+7ewffHgFy5i4VweoB9MmppaF1RPj0pGftFp6d0+dWDGCARQwggEQ"+
"AgEBMG4wWjELMAkGA1UEBhMCQ04xFjAUBgNVBAoTDUJBTksgT0YgQ0hJTkExEDAOBgNVBAgTB0JF"+
"SUpJTkcxEDAOBgNVBAcTB0JFSUpJTkcxDzANBgNVBAMTBkJPQyBDQQIQfUcmGU5Dp0r66SIzTak6"+
"lDAJBgUrDgMCGgUAMA0GCSqGSIb3DQEBAQUABIGAa6dnWBArRLTMDYcWeYYLBFRVIeYX0WkQHniU"+
"AN4umk64gC/4r96v5BVm7tuetH2QtqVJIelvHZZKnvQsqAG108TkPR9+12JbxApu/eE5DTXmXqdj"+
"zfrQE7sk7rCBdqbFjqkETzU7oAwfqCuZGa6q+4TDWvdmYkM33ZdmtFJ53a0=";
    
    /**
    java中两种发起POST请求，并接收返回的响应内容的方式  2011-07-22 09:43:29|  分类： 默认分类 |  标签： |字号大
    中
    小 订阅 
    1、利用apache提供的commons-httpclient-3.0.jar包
    代码如下：    
      * 利用HttpClient发起POST请求，并接收返回的响应内容
      * 
      * @param url 请求链接
      * @param type 交易或响应编号
      * @param message 请求内容
      * @return 响应内容
      */
	public void transRequest(String authorizationCode) {
		//	 响应内容
		String result = "";

		// 定义http客户端对象--httpClient
		HttpClient httpClient = new HttpClient();

		// 定义并实例化客户端链接对象-postMethod
		PostMethod postMethod = new PostMethod("http://91passport.mysticalcard.com/mpassport.php?do=plogin&v=9762&phpp=ANDROID_360&phpl=ZH_CN&pvc=1.4.0&pvb=2013-11-22%2009%3A56%3A41");

		try {
			// 设置http的头
			postMethod.setRequestHeader("Host", "91passport.mysticalcard.com");
			postMethod.setRequestHeader("Accept-Encoding", "deflate, gzip");
			postMethod.setRequestHeader("Proxy-Connection", "Keep-Alive");
			postMethod.setRequestHeader("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
			postMethod.setRequestHeader("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
			postMethod.setRequestHeader("x-flash-version", "11,9,900,117");
			postMethod.setRequestHeader("Connection", "Keep-Alive");
			postMethod.setRequestHeader("Cache-Control", "no-cache");
			postMethod.setRequestHeader("Referer", "app:/assets/CardMain.swf");
			postMethod.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
			postMethod.setRequestHeader("Content-Length", "130");
						
			//postMethod.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");
			//String data = "Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&authorizationCode="+authorizationCode
			//+"&plat=safe360&IDFA=&newguide=1";

			// 填入各个表单域的值
			NameValuePair Udid  = new NameValuePair("Udid", "C4%3A6A%3AB7%3A86%3ABE%3A51");
			NameValuePair aCode = new NameValuePair("authorizationCode", authorizationCode);
			NameValuePair plat = new NameValuePair("plat", "safe360");
			NameValuePair IDFA = new NameValuePair("IDFA", "");
			NameValuePair newguide = new NameValuePair("newguide", "1");
			////        postMethod.setRequestBody( new NameValuePair[] {bocNo, orderNos,signData});
			NameValuePair[] data = {IDFA, aCode, plat, IDFA, newguide};

			// 将表单的值放入postMethod中
			postMethod.setRequestBody(data);			

			// 定义访问地址的链接状态
			int statusCode = 0;
			try {
				// 客户端请求url数据
				statusCode = httpClient.executeMethod(postMethod);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 请求成功状态-200
			if (statusCode == HttpStatus.SC_OK) {
				try {
					result = postMethod.getResponseBodyAsString();
					System.out.println(result);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("请求返回状态：" + statusCode);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			// 释放链接
			postMethod.releaseConnection();
			httpClient.getHttpConnectionManager().closeIdleConnections(0);
		}

	       /* GetMethod authpost1 = new GetMethod("http://180.168.146.75:81/PGWPortal/CommonB2BQueryOrder.do" );

	        httpClient.executeMethod(authpost1);
	        result = authpost1.getResponseBodyAsString();
	        System.out.println(result);*/
	      //查看cookie信息

	        /*Cookie[] cookies = httpClient.getState().getCookies();
	        httpClient.getState().addCookies(cookies);

	       if (cookies.length == 0) {

	           System.out.println("None");

	       } else {

	           for (int i = 0; i < cookies.length; i++) {

	               System.out.println(cookies[i].toString());

	           }

	       }*/
	}
	
	public static void main(String[] args) {
		//HttpClientTest test = new HttpClientTest();
		//test.transRequest("", "", "");
		String a = "crerf7kdrgkvkeNqrVsqtKk5XslJKybcNSi1OLVErszWyMDJQK8goKLB19HMJ8vd0iXcJ9fcOBQnl2EZ5xDv7Keko%2BSYWBJckpqd6pihZWego5SXnAQ0xNDBQqgUATtAZPw%3D%3D";
		String b = "crerf7kdrgkvkeNqrVsqtKk5XslJKybcNSi1OLVErszWyMDJQK8goKLB19HMJ8vd0iXcJ9fcOBQnl2EZ5xDv7Keko+SYWBJckpqd6pihZWego5SXnAQ0xNDBQqgUATtAZPw==";
		try {
			System.out.println(java.net.URLDecoder.decode(a, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
