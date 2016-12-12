package main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;

public class Test1 {
	private final byte[] EMPTY_ARRAY = new byte[0];
	
	public void test1(String authorizationCode) throws Exception {
		Properties prop = System.getProperties();
		//设置http访问要使用的代理服务器的地址 
		prop.setProperty("http.proxyHost", "127.0.0.1");
		//设置http访问要使用的代理服务器的端口 
		prop.setProperty("http.proxyPort", "8087");
		
		String url = "http://91passport.mysticalcard.com/mpassport.php?do=plogin&v=9762&phpp=ANDROID_360&phpl=ZH_CN&pvc=1.4.0&pvb=2013-11-22%2009%3A56%3A41";  
		PostMethod post = new PostMethod(url);
		// 请求体内容
		String body = "Udid=C4%3A6A%3AB7%3A86%3ABE%3A51&authorizationCode="+authorizationCode
			+"&plat=safe360&IDFA=&newguide=1";
		// 用deflate方式压缩请求体并赋给request
		ByteArrayInputStream bis = new ByteArrayInputStream(body.getBytes());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(bos);
		for (int c = bis.read(); c != -1; c = bis.read()) {
			dos.write(c);
		}
		dos.close();
		InputStreamRequestEntity entity = new InputStreamRequestEntity(new ByteArrayInputStream(bos.toByteArray()), "text/html");
		post.setRequestEntity(entity);
		//post.addRequestHeader("Content-Encoding", "deflate");
		 
		post.setRequestHeader("Host", "91passport.mysticalcard.com");
		post.setRequestHeader("Accept-Encoding", "deflate, gzip");
		post.setRequestHeader("Proxy-Connection", "Keep-Alive");
		post.setRequestHeader("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
		post.setRequestHeader("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
		post.setRequestHeader("x-flash-version", "11,9,900,117");
		post.setRequestHeader("Connection", "Keep-Alive");
		post.setRequestHeader("Cache-Control", "no-cache");
		post.setRequestHeader("Referer", "app:/assets/CardMain.swf");
		post.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		post.setRequestHeader("Content-Length", "130");
		 
		HttpClient client = new HttpClient();
		client.setConnectionTimeout(1000 * 60);
		int status=0;
		status = client.executeMethod(post);
		if(status==HttpStatus.SC_OK) {
			// System.out.println(makeResponseObject(post.getResponseBodyAsStream()));			 
			System.out.println(post.getResponseBodyAsString());
		} else {
		} 
		post.releaseConnection(); 
	}
	
	// 根据HTTP请求后返回的字串生成结果封装类的实例 
	private String makeResponseObject(InputStream is) throws Exception {
		//System.out.println("-------------------"+sResponse);
		 InflaterInputStream iis = new InflaterInputStream(is);
		 int contentLength = 0;
		 contentLength = (contentLength <= 0) ? 1024 : contentLength;
		 ByteArrayOutputStream bos = new ByteArrayOutputStream();
		 byte[] buffer = new byte[contentLength];
		 int read = iis.read(buffer);

		 while (-1 != read) {
		     bos.write(buffer, 0, read);
		     read = iis.read(buffer);
		 }

		 byte[] _bytes = (contentLength == 0) ? EMPTY_ARRAY : bos.toByteArray();
		 ByteArrayInputStream _is = new ByteArrayInputStream(_bytes);

		 StringBuffer out = new StringBuffer();
		 byte[] b = new byte[4096];
		 for (int n; (n = _is.read(b)) != -1;) {
		     out.append(new String(b, 0, n));
		 }
		 return out.toString();
	}
	
	public static void main(String[] args) {
		//System.out.println(util.StringUtil.unicodeToString("\u83b7\u53d6access_token\u5931\u8d25\uff0c\u8bf7\u91cd\u8bd5\uff01"));
	}
}
