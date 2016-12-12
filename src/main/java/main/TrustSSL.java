package main;

import java.io.*;
import java.net.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Vector;

import javax.net.ssl.*;

import net.sf.json.JSONObject;

public class TrustSSL {
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
	
	public String doAction(String urls, String method, List headList, String datas){
		InputStream in = null;
		OutputStream out = null;
		String str_return = "";
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
			URL console = new URL(urls);
			HttpsURLConnection conn = (HttpsURLConnection) console.openConnection();
			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
			conn.setRequestMethod(method);
			if(headList != null && headList.size() > 0){
				for(int hi=0;hi<headList.size();hi++){
					conn.setRequestProperty(((List)headList.get(hi)).get(0).toString(), ((List)headList.get(hi)).get(1).toString());
				}
			}
			if(datas != null && datas.length() > 0){
				conn.setDoOutput(true);
                out = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(out);
                dos.write(datas.getBytes());
                dos.flush();
                dos.close();
			}			
			conn.connect();
            //获取响应状态  
            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {  
                System.out.println("connect failed!");
            }  
            //获取响应内容体  
			InputStream is = conn.getInputStream();
			DataInputStream indata = new DataInputStream(is);
			String ret = "";
			while (ret != null) {
				ret = indata.readLine();
				if (ret != null && !ret.trim().equals("")) {
					str_return = str_return + new String(ret.getBytes("utf-8"), "GBK");
				}
			}
			conn.disconnect();
		} catch (ConnectException e) {
			System.out.println("ConnectException");
			e.printStackTrace();

		} catch (IOException e1) {
			System.out.println("IOException");
			e1.printStackTrace();

		} catch (Exception e2) {
			System.out.println("Exception");
			e2.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (Exception e) {
			}
			try {
				out.close();
			} catch (Exception e) {
			}
		}
		/*System.out.println(str_return);
		JSONObject jbect = JSONObject.fromObject(str_return);
		String str_user = jbect.get("user").toString();
		JSONObject user = JSONObject.fromObject(str_user);
		System.out.println(jbect.get("user"));
		System.out.println(user.get("q"));
		System.out.println(user.get("t"));*/
		return str_return;
	}

	public static void main(String[] args) throws Exception {
		TrustSSL tssl = new TrustSSL();
		List<Object> headList = null;
		List<String> headChildList = null;
		String datas = "";
		String return_str = "";
		return_str = tssl.doAction("https://passport.360.cn/api.php?parad=5CGFFfRgOOgEyt6kx5q1Ih2dfg5W4pcUviKqx2KdBxK9qRdG7ft25uX1JzQICBBTUmYQJmcsYqNMMmGTzxxUzGhuZs%2B2mmZO0vB2o3rpQglXLt7GEIkipJOpTv3V6vwjPAdSLMKFRLN0tMYGyJkdVP1ZYa90aT0CNPjx0LNXmTYlFyNZtI1eX9c6nrL1s5exTJ4T%2Fmr0MJLniPkfp0A7NeDlS3S2fSAxivqDcpVcLxGvOixa8G%2BEX1LNGx5qPDWJxXHa9yM1ojQ%3D&from=mpc_open_ms_200251906","GET",null,datas);
		System.out.println(return_str);
		
		JSONObject jbect = JSONObject.fromObject(return_str);
		String str_user = jbect.get("user").toString();
		JSONObject user = JSONObject.fromObject(str_user);
		String q = user.get("q").toString();
		String t = user.get("t").toString();
		
		headList = new Vector<Object>();
		headChildList = new Vector<String>();
		headChildList.add("User-Agent");
		headChildList.add("Qhopensdk-0.8.4;card");
		headList.add(headChildList);
		headChildList = new Vector<String>();
		headChildList.add("Content-Type");
		headChildList.add("application/x-www-form-urlencoded");
		headList.add(headChildList);
		headChildList = new Vector<String>();
		headChildList.add("Host");
		headChildList.add("openapi.360.cn");
		headList.add(headChildList);
		headChildList = new Vector<String>();
		headChildList.add("Connection");
		headChildList.add("Keep-Alive");
		headList.add(headChildList);
		headChildList = new Vector<String>();
		headChildList.add("Cookie");
		headChildList.add("Q="+q+";T="+t+";");
		headList.add(headChildList);
		headChildList = new Vector<String>();
		headChildList.add("Accept-Encoding");
		headChildList.add("utf-8");
		headList.add(headChildList);
		headChildList = new Vector<String>();
		headChildList.add("GET");
		headChildList.add("/oauth2/authorize.json?client_id=006b0eeedfbc8b95c467085d2f6dab3d&response_type=code&redirect_uri=oob&state=test_state111&scope=basic&version=Qhopensdk-0.8.4&mid=4c71efbbc49bc357d304ce115d6cd817&DChannel=card&display=mobile.cli_v1&oauth2_login_type=5");
		headList.add(headChildList);		
		
		return_str = tssl.doAction("https://openapi.360.cn/oauth2/authorize.json?client_id=006b0eeedfbc8b95c467085d2f6dab3d&response_type=code&redirect_uri=oob&state=test_state111&scope=basic&version=Qhopensdk-0.8.4&mid=4c71efbbc49bc357d304ce115d6cd817&DChannel=card&display=mobile.cli_v1&oauth2_login_type=5","GET",headList,datas);
		System.out.println(return_str);
		
	}
}
