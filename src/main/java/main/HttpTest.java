package main;

//参数可以先通过Httpwatch或者HttpLook观察
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import util.StringUtil;

public class HttpTest {
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

	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}
	
	public void test() throws Exception {
		InetAddress addr = InetAddress.getByName("127.0.0.1");
		int port = 80;
		String data = "name="+URLEncoder.encode("五王","utf-8");
		data += "&" + "sex" + "=" + URLEncoder.encode("男", "UTF-8");

		Socket socket = new Socket(addr,port);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		bw.write("POST /test.php HTTP/1.1");
		bw.newLine();
		bw.write("Host: 127.0.0.1");
		bw.newLine();
		bw.write("Content-Type: application/x-www-form-urlencoded");
		bw.newLine();
		bw.write("Accept-Language: zh-cn");
		bw.newLine();
		bw.write("UA: IE7");//设置参数到header
		bw.newLine();
		bw.write("Content-Length: "+data.length());
		bw.newLine();  
		bw.newLine();
		bw.write(data);//前面要空两行。
		bw.flush();
		System.out.println(data);
		String s = null;
		while((s=br.readLine()) != null){
		System.out.println(s);
		}
		bw.close();
		br.close();
		socket.close();
	}
	
	public static void main(String[] args) throws Exception {

		//InetAddress addr = InetAddress.getByName("passport.360.cn");
		
		//int port = 80;
		/*String data = "parad=5CGFFfRgOOgEyt6kx5q1Ih2dfg5W4pcUviKqx2KdBxK9qRdG7ft25uX1JzQICBBTUmYQJmcsYqNMMmGTzxxUzGhuZs%2B2mmZO0vB2o3rpQglXLt7GEIkipJOpTv3V6vwjPAdSLMKFRLN0tMYGyJkdVP1ZYa90aT0CNPjx0LNXmTYlFyNZtI1eX9c6nrL1s5exTJ4T%2Fmr0MJLniPkfp0A7NeDlS3S2fSAxivqDcpVcLxGvOixa8G%2BEX1LNGx5qPDWJxXHa9yM1ojQ%3D";
		data += "&from=mpc_open_ms_200251906";*/

		/*Socket socket = new Socket(addr,port);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));*/
		
		SSLContext context = SSLContext.getInstance("SSL");
        // 初始化
		context.init(null,
				new TrustManager[] { new HttpTest().new TrustAnyTrustManager() },
				new SecureRandom());
		SSLSocketFactory factory = context.getSocketFactory();
		String url = "openapi.360.cn";//passport.360.cn
		SSLSocket socket = (SSLSocket) factory.createSocket(url, 443);
		
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		/*bw.write("GET /api.php?parad=5CGFFfRgOOgEyt6kx5q1Ih2dfg5W4pcUviKqx2KdBxK9qRdG7ft25uX1JzQICBBTUmYQJmcsYqNMMmGTzxxUzGhuZs%2B2mmZO0vB2o3rpQglXLt7GEIkipJOpTv3V6vwjPAdSLMKFRLN0tMYGyJkdVP1ZYa90aT0CNPjx0LNXmTYlFyNZtI1eX9c6nrL1s5exTJ4T%2Fmr0MJLniPkfp0A7NeDlS3S2fSAxivqDcpVcLxGvOixa8G%2BEX1LNGx5qPDWJxXHa9yM1ojQ%3D&from=mpc_open_ms_200251906 HTTP/1.1");
		bw.newLine();
		bw.write("User-Agent: Qhopensdk-0.8.4;card");
		bw.newLine();
		bw.write("Content-Type: application/x-www-form-urlencoded");
		bw.newLine();
		bw.write("Accept-Encoding: gzip, deflate");
		bw.newLine();
		bw.write("Host: passport.360.cn");
		bw.newLine();
		bw.write("Connection: Keep-Alive");*/
		bw.write("GET /oauth2/authorize.json?client_id=006b0eeedfbc8b95c467085d2f6dab3d&response_type=code&redirect_uri=oob&state=test_state111&scope=basic&version=Qhopensdk-0.8.4&mid=4c71efbbc49bc357d304ce115d6cd817&DChannel=card&display=mobile.cli_v1&oauth2_login_type=5 HTTP/1.1");
		bw.newLine();
		bw.write("User-Agent: Qhopensdk-0.8.4;card");
		bw.newLine();
		bw.write("Content-Type: application/x-www-form-urlencoded");
		bw.newLine();
		bw.write("Accept-Encoding: gzip, deflate");
		bw.newLine();
		bw.write("Cookie: Q=u%3Dqyvax169%26n%3D%26r%3D%26qid%3D316608296%26im%3D220255dq9816%26s%3Dquc%26src%3Dmpc_open_ms_200251906-3%26t%3D1%26le%3D;T=s%3Dc540f8ea5a2adfd55a7b8aa08130a923%26t%3D1389793385%26a%3D0%26v%3D1.0%26lm%3D;");
		bw.newLine();
		bw.write("Host: openapi.360.cn");
		bw.newLine();
		bw.write("Connection: Keep-Alive");
		bw.newLine();  
		bw.newLine();
		//bw.write(data);//前面要空两行。
		bw.flush();
		//System.out.println(data);
		String s = null;
		while((s=br.readLine()) != null){
			System.out.println(s);
		}
		bw.close();
		br.close();
		socket.close();
		//String a = "{\"status\":0,\"message\":\"\u8f93\u5165\u9519\u8bef,\u8bf7\u91cd\u65b0\u8f93\u5165!\"}";
		//System.out.println(StringUtil.unicodeToString(a));
		/*String str="";
		str+="{\"status\":1,";
		str+="\"data\":{\"new\":false,";
		str+="\"current\":{\"GS_ID\":4,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.8\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u91d1\u5c5e\u5de8\u9f99\",";
		str+="\"GS_IP\":\"http://s4.mysticalcard.com/\"},";
		str+="\"list\":[{\"GS_ID\":1,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.5\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u65f6\u7a7a\u65c5\u8005\",";
		str+="\"GS_IP\":\"http://s1.mysticalcard.com/\"},{\"GS_ID\":2,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.6\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u7cbe\u7075\u796d\u53f8\",";
		str+="\"GS_IP\":\"http://s2.mysticalcard.com/\"},{\"GS_ID\":3,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.7\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u68ee\u6797\u5973\u795e\",";
		str+="\"GS_IP\":\"http://s3.mysticalcard.com/\"},{\"GS_ID\":4,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.8\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u91d1\u5c5e\u5de8\u9f99\",";
		str+="\"GS_IP\":\"http://s4.mysticalcard.com/\"},{\"GS_ID\":5,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.9\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u8363\u8000\u5de8\u4eba\",";
		str+="\"GS_IP\":\"http://s5.mysticalcard.com/\"},{\"GS_ID\":6,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.75\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u964d\u4e34\u5929\u4f7f\",";
		str+="\"GS_IP\":\"http://s6.mysticalcard.com/\"},{\"GS_ID\":7,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.10\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5149\u660e\u4e4b\u9f99\",";
		str+="\"GS_IP\":\"http://s7.mysticalcard.com/\"},{\"GS_ID\":8,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.11\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u8840\u796d\u6076\u9b54 \",";
		str+="\"GS_IP\":\"http://s8.mysticalcard.com/\"},{\"GS_ID\":9,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.72\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5fb7\u739b\u897f\u4e9a\",";
		str+="\"GS_IP\":\"http://s9.mysticalcard.com/\"},{\"GS_ID\":10,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.74\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u90aa\u7075\u5973\u5deb\",";
		str+="\"GS_IP\":\"http://s10.mysticalcard.com/\"},{\"GS_ID\":11,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.76\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u80cc\u4e3b\u4e4b\u5f71\",";
		str+="\"GS_IP\":\"http://s11.mysticalcard.com/\"},{\"GS_ID\":12,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.77\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u6708\u4eae\u5973\u795e\",";
		str+="\"GS_IP\":\"http://s12.mysticalcard.com/\"},{\"GS_ID\":13,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.3\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u4e16\u754c\u6811\u4e4b\u7075\",";
		str+="\"GS_IP\":\"http://s13.mysticalcard.com/\"},{\"GS_ID\":14,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.73\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5e7d\u7075\u5de8\u9f99\",";
		str+="\"GS_IP\":\"http://s14.mysticalcard.com/\"},{\"GS_ID\":15,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.96\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5200\u950b\u5973\u738b\",";
		str+="\"GS_IP\":\"http://s15.mysticalcard.com/\"},{\"GS_ID\":16,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.97\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u718a\u4eba\u6b66\u58eb\",";
		str+="\"GS_IP\":\"http://s16.mysticalcard.com/\"},{\"GS_ID\":17,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.99\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u6050\u60e7\u4e4b\u738b\",";
		str+="\"GS_IP\":\"http://s17.mysticalcard.com/\"},{\"GS_ID\":18,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.100\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u8fdc\u53e4\u874e\u7687\",";
		str+="\"GS_IP\":\"http://s18.mysticalcard.com/\"},{\"GS_ID\":19,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.101\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u9690\u4e16\u5148\u77e5\",";
		str+="\"GS_IP\":\"http://s19.mysticalcard.com/\"},{\"GS_ID\":20,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.102\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u4e5d\u5934\u5996\u86c7\",";
		str+="\"GS_IP\":\"http://s20.mysticalcard.com/\"},{\"GS_ID\":21,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.98\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5143\u7d20\u7075\u9f99\",";
		str+="\"GS_IP\":\"http://s21.mysticalcard.com/\"},{\"GS_ID\":22,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.103\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u7eaf\u6d01\u5723\u5973\",";
		str+="\"GS_IP\":\"http://s22.mysticalcard.com/\"},{\"GS_ID\":23,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.104\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u6bc1\u706d\u4e4b\u9f99\",";
		str+="\"GS_IP\":\"http://s23.mysticalcard.com/\"},{\"GS_ID\":24,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.105\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5deb\u5996\u9886\u4e3b\",";
		str+="\"GS_IP\":\"http://s24.mysticalcard.com/\"},{\"GS_ID\":25,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.106\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5815\u843d\u5929\u4f7f\",";
		str+="\"GS_IP\":\"http://s25.mysticalcard.com/\"},{\"GS_ID\":26,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.122\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u7fbd\u7ffc\u5316\u86c7\",";
		str+="\"GS_IP\":\"http://s26.mysticalcard.com/\"},{\"GS_ID\":27,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.71\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u590d\u6d3b\u5154\u5973\",";
		str+="\"GS_IP\":\"http://s27.mysticalcard.com/\"},{\"GS_ID\":28,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.78\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u731b\u72b8\u4e4b\u738b\",";
		str+="\"GS_IP\":\"http://s28.mysticalcard.com/\"},{\"GS_ID\":29,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.107\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u8ff7\u9b45\u7075\u72d0\",";
		str+="\"GS_IP\":\"http://s29.mysticalcard.com/\"},{\"GS_ID\":30,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.108\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u72ec\u773c\u5de8\u4eba\",";
		str+="\"GS_IP\":\"http://s30.mysticalcard.com/\"},{\"GS_ID\":31,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.136\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u6df7\u6c8c\u4e4b\u9f99\",";
		str+="\"GS_IP\":\"http://s31.mysticalcard.com/\"},{\"GS_ID\":32,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.137\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5723\u5802\u6b66\u58eb\",";
		str+="\"GS_IP\":\"http://s32.mysticalcard.com/\"},{\"GS_ID\":33,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.138\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u8fdc\u53e4\u6d77\u5996\",";
		str+="\"GS_IP\":\"http://s33.mysticalcard.com/\"},{\"GS_ID\":34,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.139\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u67aa\u70ae\u73ab\u7470\",";
		str+="\"GS_IP\":\"http://s34.mysticalcard.com/\"},{\"GS_ID\":35,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.159\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u65f6\u5149\u5973\u795e\",";
		str+="\"GS_IP\":\"http://s35.mysticalcard.com/\"},{\"GS_ID\":36,";
		str+="\"GS_STATUS\":1,";
		str+="\"GS_CHAT_IP\":\"218.245.7.160\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u7cbe\u7075\u5973\u738b\",";
		str+="\"GS_IP\":\"http://s36.mysticalcard.com/\"},{\"GS_ID\":37,";
		str+="\"GS_STATUS\":2,";
		str+="\"GS_CHAT_IP\":\"218.245.7.161\",";
		str+="\"GS_CHAT_PORT\":8000,";
		str+="\"GS_NAME\":\"\u5723\u8bde\u8001\u4eba\",";
		str+="\"GS_IP\":\"http://s37.mysticalcard.com/\"}],";
		str+="\"uinfo\":{\"access_token\":\"316608296d744a6f073ba5bd4dc92e139bed8f4046ecdb48d\",";
		str+="\"uin\":\"316608296\",";
		str+="\"nick\":\"dlink169\",";
		str+="\"MUid\":\"1012222\",";
		str+="\"time\":1389195570,";
		str+="\"sign\":\"c6cecd229012571bf31ce635b5d85207\",";
		str+="\"ppsign\":\"c56cddec6f00a8d99992235bdbc05d3c\"}}}";
		System.out.println(StringUtil.unicodeToString(str));*/
	}
}
