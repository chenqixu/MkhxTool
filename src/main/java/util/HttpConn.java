package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;

public class HttpConn {

    public static int MAX_REC_BUFFER = 20480;

    public URL testWebServer(String sUrl, int iTimeout){
        URL url;
        try{
	        url = new URL(sUrl);
	        String ipAddr = url.getHost();
	        //int iPort = url.getPort();
	        int iPort = 80;
	        System.out.println("ipAddr:"+ipAddr);
	        System.out.println("iPort:"+iPort);
	        if(iTimeout > 0){
	            Socket socket = new Socket();
	            InetSocketAddress address = new InetSocketAddress(ipAddr, iPort);
	            socket.connect(address, iTimeout);
	            socket.close();
	        }
	        return url;
        }catch(Exception ex){
	        ex.printStackTrace();
	        return null;
        }
    }
    
    public String doAction(String sMethod, String sUrl, byte aRequestContent[]){
    	String result = "";
    	String strResult = "";
        OutputStream out = null;
        InputStream ins = null;
        URL url = null;
        URLConnection uc = null;
        HttpURLConnection urlcon = null;
        byte aResult[] = null;
        ByteArrayOutputStream os = null;
        try{
        	url = this.testWebServer(sUrl, 0);
        	uc = url.openConnection();
        	if(uc instanceof HttpURLConnection){
        		urlcon = (HttpURLConnection)uc;
                urlcon.setRequestMethod("POST");
                //urlcon.setRequestProperty("Content-Type","text/xml; charset=utf-8");
                urlcon.setRequestProperty("Accept-Encoding", "deflate, gzip");
                //urlcon.setRequestProperty("Accept-Encoding", "gzip");
                //urlcon.setRequestProperty("Accept-Encoding", "deflate");
                urlcon.setRequestProperty("POST", "/mpassport.php?do=plogin&v=9762&phpp=ANDROID_360&phpl=ZH_CN&pvc=1.4.0&pvb=2013-11-22%2009%3A56%3A41 HTTP/1.1");
                urlcon.setRequestProperty("Host", "91passport.mysticalcard.com");
                urlcon.setRequestProperty("Proxy-Connection", "Keep-Alive");
                //urlcon.setRequestProperty("Cookie", "_sid=r4ju04sa3acrjtjvdqtmk58gs7");//没变
                urlcon.setRequestProperty("Accept", "text/xml, application/xml, application/xhtml+xml, text/html;q=0.9, text/plain;q=0.8, text/css, image/png, image/jpeg, image/gif;q=0.8, application/x-shockwave-flash, video/mp4;q=0.9, flv-application/octet-stream;q=0.8, video/x-flv;q=0.7, audio/mp4, application/futuresplash, */*;q=0.5");
                urlcon.setRequestProperty("User-Agent", "Mozilla/5.0 (Android; U; zh-CN) AppleWebKit/533.19.4 (KHTML, like Gecko) AdobeAIR/3.9");
                urlcon.setRequestProperty("x-flash-version", "11,9,900,117");
                urlcon.setRequestProperty("Connection", "Keep-Alive");
                urlcon.setRequestProperty("Cache-Control", "no-cache");
                urlcon.setRequestProperty("Referer", "app:/assets/CardMain.swf");
                urlcon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //urlcon.setRequestProperty("Content-Length", String.valueOf(aRequestContent.length));
                urlcon.setRequestProperty("Content-Length", "130");
                if(aRequestContent != null && aRequestContent.length > 0){
                	urlcon.setDoOutput(true);
                    out = urlcon.getOutputStream();
                    DataOutputStream dos = new DataOutputStream(out);
                    dos.write(aRequestContent);
                    dos.flush();
                    dos.close();
                    
                    /*out = null;
                    ins = urlcon.getInputStream();
                    aResult = null;
                    os = null;
                    os = new ByteArrayOutputStream();
                    int d = 0;
                    //byte bytes[] = new byte[ins.available()];
                    byte bytes[] = new byte[1024];
                    while((d = ins.read(bytes)) != -1) 
                        os.write(bytes, 0, d);
                    aResult = os.toByteArray();*/

        	        /*InputStreamReader isr = new InputStreamReader(urlcon.getInputStream(),"utf-8");
        	        BufferedReader in = new BufferedReader(isr);
        	       
        	        String inputLine;
        	        BufferedWriter bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:/home/result.xml")));//本地生成的xml文档
        	        while ((inputLine = in.readLine()) != null){
        	            System.out.println(inputLine);
        	            bw.write(inputLine);
        	            bw.newLine();
        	        }
        	        bw.close();
        	        in.close();*/
                    
                    //strResult="";  
                    /*byte[] b = new byte[ins.available()];  
                    int i = 0;  
                    while ((i = ins.read(b)) != -1) {  
                        strResult+=new String(b,"UTF-8");  
                        b = new byte[1024];  
                    }*/
                    
                    /*byte buffer[] = new byte[100];  
                    StringBuffer sb = new StringBuffer();  
                    int is = 0;  
                    while ((is = ins.read(buffer)) != -1) {  
                        sb.append(new String(buffer, 0, is));  
                    }
                    strResult = "";
                    strResult = sb.toString();*/
                    
                    //获取响应状态  
                    if (urlcon.getResponseCode() != HttpURLConnection.HTTP_OK) {  
                        System.out.println("connect failed!");  
                         
                    }  
                    //获取响应内容体  
                    String line = "";  
                    BufferedReader in = new BufferedReader(new InputStreamReader(urlcon.getInputStream(), "utf-8"));  
                    while ((line = in.readLine()) != null) {  
                        result += line + "\n";  
                    }  
                    in.close();
                    System.out.println(result);
                }
        	}
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try{
	        	if(os != null)
	        		os.close();
	        	if(out != null)
	                out.close();
	        	if(ins != null)
	                ins.close();
	        	//关闭连接
	        	urlcon.disconnect();
        	}catch(Exception ex){
        		ex.printStackTrace();
        	}
        }
        /*try{
        	result = new String(aResult);
        }catch(Exception e){
        	e.printStackTrace();
        }*/
    	return result;
    }
    
    public String doAction(String serverURL, String param){
    	String result = "";    	

    	
    	return result;
    }
}
