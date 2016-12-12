package bean;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;

import org.apache.http.entity.StringEntity;

import client.Http360Client;

import util.DuokuAesUtil;
import util.IORoutines;
import util.StringUtil;

import net.sf.json.JSONObject;
import bean.json.ActGoods;
import bean.json.ExploreInfo;
import bean.json.Goods;
import bean.json.HttpService;
import bean.json.MazeInfo;
import bean.json.MazeShow;
import bean.json.Mpassport;
import bean.json.MpassportUinfo;
import bean.json.Passport;
import bean.json.UserMapStages;


/**
 * 使用代理
 * */
public class JsonTest {
	
	private int v = 1000;
	
	public JsonTest() {
		//Properties prop = System.getProperties();
		//设置http访问要使用的代理服务器的地址 
		//prop.setProperty("http.proxyHost", "127.0.0.1");
		//设置http访问要使用的代理服务器的端口 
		//prop.setProperty("http.proxyPort", "8087");		
		this.v = new StringUtil().radom_four();
	}
	
	/**
	 * 使用代理 httpget
	 * */
	public String httpGetProxy(String _url) throws Exception {
		System.out.println("url:"+_url);
		URL getUrl = new URL(_url);
		HttpURLConnection connection = (HttpURLConnection) getUrl.openConnection();
		connection.connect();
	    // 取得输入流，并使用Reader读取
	    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	    String lines = "";
	    String result = "";
	    while ((lines = reader.readLine()) != null) {
	        System.out.println(lines);
	        result += lines;
	    }
	    reader.close();
	    // 断开连接
	    connection.disconnect();
	    return result;
	}
	
	/**
	 * 使用代理 httppost
	 * */
	public List httpPostProxy(String _url, String _data, Map _requestheaders) throws Exception {
		System.out.println("url:"+_url);
		System.out.println("data:"+_data);
		URL url = new URL(_url);
		URLConnection c = url.openConnection();
		HttpURLConnection urlcon = null;
		String data = new String(_data.getBytes(), "ISO-8859-1");
		String responseText = "";
		InputStream in = null;
		DataOutputStream dos = null;
		OutputStream out = null;
		List list = new Vector();
		try{
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
				urlcon.setDoOutput(true);
		        out = urlcon.getOutputStream();
		        dos = new DataOutputStream(out);
		        dos.write(data.getBytes());
		        dos.flush();
		        dos.close();
		        
		        in = urlcon.getInputStream();
				int contentLength = urlcon.getContentLength();
				byte[] bytes = IORoutines.load(in, contentLength == -1 ? 4096 : contentLength);
				Http360Client bean = new Http360Client();
				String responseHeaders = bean.getAllResponseHeaders(c);
				System.out.println("responseHeaders:"+responseHeaders);
				Map responseHeadersMap = urlcon.getHeaderFields();
				System.out.println("responseHeadersMap:"+responseHeadersMap);
				Object obj = responseHeadersMap.get("Content-Encoding");
				String Encoding = "";
				if(obj!=null){
					Encoding = obj.toString();
				}
				System.out.println("Content-Encoding:"+Encoding);
				if(Encoding.equals("[gzip]")){
					responseText = util.ZipUtil2.uncompress(bean.getResponseText(bytes));
				}else if(Encoding.equals("[deflate]")){
					responseText = util.DeflateUtil.uncompress(bean.getResponseText(bytes));
				}else{
					responseText = bean.getResponseText(bytes);
				}			
				System.out.println("responseText:"+responseText);
				in.close();
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
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			synchronized (this) {
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
		return list;
	}
	
	public static boolean isOdd(int i) {
		return i % 2 != 0;
	}
	

    static private final int  BASELENGTH   = 128;
    static final private byte [] hexNumberTable    = new byte[BASELENGTH];
	
    /***
     * Decode hex string to a byte array
     *
     * @param encoded encoded string
     * @return return array of byte to encode
     */
    static public byte[] decode(String encoded) {
        if (encoded == null)
            return null;
        int lengthData = encoded.length();
        if (lengthData % 2 != 0)
            return null;

        char[] binaryData = encoded.toCharArray();
        int lengthDecode = lengthData / 2;
        byte[] decodedData = new byte[lengthDecode];
        byte temp1, temp2;
        char tempChar;
        for( int i = 0; i<lengthDecode; i++ ){
            tempChar = binaryData[i*2];
            temp1 = (tempChar < BASELENGTH) ? hexNumberTable[tempChar] : -1;
            if (temp1 == -1)
                return null;
            tempChar = binaryData[i*2+1];
            temp2 = (tempChar < BASELENGTH) ? hexNumberTable[tempChar] : -1;
            if (temp2 == -1)
                return null;
            decodedData[i] = (byte)((temp1 << 4) | temp2);
        }
        return decodedData;
    }
	
	public static void main(String[] args) throws Exception {
//		String responseText = "{\"status\":1,\"data\":{\"1\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"1\",\"Type\":\"1\",\"MapStageId\":\"1\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-06-25 15:07:05\",\"CounterAttackTime\":\"1390408501\"},\"2\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"2\",\"Type\":\"1\",\"MapStageId\":\"1\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-06-25 15:16:49\",\"CounterAttackTime\":\"0\"},\"3\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"3\",\"Type\":\"1\",\"MapStageId\":\"1\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-06-25 15:20:40\",\"CounterAttackTime\":\"1390408501\"},\"4\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"4\",\"Type\":\"1\",\"MapStageId\":\"1\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-06-25 15:22:21\",\"CounterAttackTime\":\"0\"},\"5\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"5\",\"Type\":\"1\",\"MapStageId\":\"1\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-06-25 15:25:52\",\"CounterAttackTime\":\"0\"},\"6\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"6\",\"Type\":\"2\",\"MapStageId\":\"1\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-06-25 15:29:09\",\"CounterAttackTime\":\"0\"},\"7\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"7\",\"Type\":\"1\",\"MapStageId\":\"2\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-26 15:11:22\",\"CounterAttackTime\":\"0\"},\"8\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"8\",\"Type\":\"1\",\"MapStageId\":\"2\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-06-25 15:34:09\",\"CounterAttackTime\":\"1390235705\"},\"9\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"9\",\"Type\":\"1\",\"MapStageId\":\"2\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-26 15:01:41\",\"CounterAttackTime\":\"0\"},\"10\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"10\",\"Type\":\"1\",\"MapStageId\":\"2\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-26 15:03:44\",\"CounterAttackTime\":\"0\"},\"11\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"11\",\"Type\":\"1\",\"MapStageId\":\"2\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-26 15:12:00\",\"CounterAttackTime\":\"0\"},\"12\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"12\",\"Type\":\"2\",\"MapStageId\":\"2\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-26 15:05:55\",\"CounterAttackTime\":\"0\"},\"14\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"14\",\"Type\":\"1\",\"MapStageId\":\"3\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-28 14:20:20\",\"CounterAttackTime\":\"0\"},\"15\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"15\",\"Type\":\"1\",\"MapStageId\":\"3\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-28 14:15:47\",\"CounterAttackTime\":\"0\"},\"16\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"16\",\"Type\":\"1\",\"MapStageId\":\"3\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-09-02 19:36:18\",\"CounterAttackTime\":\"0\"},\"17\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"17\",\"Type\":\"1\",\"MapStageId\":\"3\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-28 14:22:39\",\"CounterAttackTime\":\"0\"},\"18\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"18\",\"Type\":\"1\",\"MapStageId\":\"3\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-28 14:25:08\",\"CounterAttackTime\":\"0\"},\"19\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"19\",\"Type\":\"1\",\"MapStageId\":\"3\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-28 14:27:10\",\"CounterAttackTime\":\"0\"},\"20\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"20\",\"Type\":\"2\",\"MapStageId\":\"3\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-09-02 19:45:15\",\"CounterAttackTime\":\"0\"},\"21\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"21\",\"Type\":\"0\",\"MapStageId\":\"3\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-09-02 19:40:48\",\"CounterAttackTime\":\"0\"},\"23\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"23\",\"Type\":\"1\",\"MapStageId\":\"4\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-09-02 19:51:19\",\"CounterAttackTime\":\"0\"},\"24\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"24\",\"Type\":\"1\",\"MapStageId\":\"4\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 10:30:14\",\"CounterAttackTime\":\"0\"},\"25\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"25\",\"Type\":\"1\",\"MapStageId\":\"4\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 10:16:58\",\"CounterAttackTime\":\"0\"},\"26\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"26\",\"Type\":\"1\",\"MapStageId\":\"4\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-09-02 19:53:51\",\"CounterAttackTime\":\"0\"},\"27\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"27\",\"Type\":\"1\",\"MapStageId\":\"4\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 10:33:35\",\"CounterAttackTime\":\"0\"},\"28\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"28\",\"Type\":\"1\",\"MapStageId\":\"4\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-09-02 19:56:01\",\"CounterAttackTime\":\"0\"},\"29\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"29\",\"Type\":\"2\",\"MapStageId\":\"4\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-09-02 19:58:57\",\"CounterAttackTime\":\"1390149263\"},\"30\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"30\",\"Type\":\"0\",\"MapStageId\":\"4\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 10:39:04\",\"CounterAttackTime\":\"0\"},\"32\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"32\",\"Type\":\"1\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-09 23:16:49\",\"CounterAttackTime\":\"0\"},\"33\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"33\",\"Type\":\"1\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 11:01:43\",\"CounterAttackTime\":\"0\"},\"34\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"34\",\"Type\":\"1\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 11:17:55\",\"CounterAttackTime\":\"0\"},\"35\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"35\",\"Type\":\"1\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 11:25:24\",\"CounterAttackTime\":\"0\"},\"36\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"36\",\"Type\":\"1\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 11:20:04\",\"CounterAttackTime\":\"0\"},\"37\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"37\",\"Type\":\"1\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 11:42:45\",\"CounterAttackTime\":\"0\"},\"38\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"38\",\"Type\":\"1\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 10:43:16\",\"CounterAttackTime\":\"0\"},\"39\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"39\",\"Type\":\"2\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 10:54:14\",\"CounterAttackTime\":\"0\"},\"40\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"40\",\"Type\":\"0\",\"MapStageId\":\"5\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 11:39:18\",\"CounterAttackTime\":\"0\"},\"42\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"42\",\"Type\":\"1\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-07-13 10:27:37\",\"CounterAttackTime\":\"0\"},\"43\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"43\",\"Type\":\"1\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 15:06:15\",\"CounterAttackTime\":\"0\"},\"44\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"44\",\"Type\":\"1\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 15:10:58\",\"CounterAttackTime\":\"1390322083\"},\"45\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"45\",\"Type\":\"1\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 16:18:57\",\"CounterAttackTime\":\"0\"},\"46\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"46\",\"Type\":\"1\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 15:19:50\",\"CounterAttackTime\":\"1390322083\"},\"47\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"47\",\"Type\":\"1\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 16:24:58\",\"CounterAttackTime\":\"0\"},\"48\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"48\",\"Type\":\"1\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 16:22:12\",\"CounterAttackTime\":\"0\"},\"49\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"49\",\"Type\":\"2\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-09-02 19:26:27\",\"CounterAttackTime\":\"0\"},\"50\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"50\",\"Type\":\"0\",\"MapStageId\":\"6\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-11 16:10:16\",\"CounterAttackTime\":\"0\"},\"52\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"52\",\"Type\":\"1\",\"MapStageId\":\"7\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-23 22:59:30\",\"CounterAttackTime\":\"0\"},\"53\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"53\",\"Type\":\"1\",\"MapStageId\":\"7\",\"FinishedStage\":\"2\",\"LastFinishedTime\":\"2013-10-25 10:09:30\",\"CounterAttackTime\":\"0\"},\"54\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"54\",\"Type\":\"1\",\"MapStageId\":\"7\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-25 09:58:34\",\"CounterAttackTime\":\"0\"},\"55\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"55\",\"Type\":\"1\",\"MapStageId\":\"7\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-25 10:03:57\",\"CounterAttackTime\":\"0\"},\"56\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"56\",\"Type\":\"1\",\"MapStageId\":\"7\",\"FinishedStage\":\"3\",\"LastFinishedTime\":\"2013-10-25 10:20:10\",\"CounterAttackTime\":\"0\"},\"57\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"57\",\"Type\":\"1\",\"MapStageId\":\"7\",\"FinishedStage\":\"2\",\"LastFinishedTime\":\"2013-10-25 10:30:27\",\"CounterAttackTime\":\"0\"},\"58\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"58\",\"Type\":\"1\",\"MapStageId\":\"7\",\"FinishedStage\":\"2\",\"LastFinishedTime\":\"2013-10-25 10:24:22\",\"CounterAttackTime\":\"0\"},\"59\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"59\",\"Type\":\"1\",\"MapStageId\":\"7\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-10-03 15:35:37\",\"CounterAttackTime\":\"0\"},\"60\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"60\",\"Type\":\"2\",\"MapStageId\":\"7\",\"FinishedStage\":\"2\",\"LastFinishedTime\":\"2013-10-05 14:21:54\",\"CounterAttackTime\":\"0\"},\"63\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"63\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:14:39\",\"CounterAttackTime\":\"0\"},\"64\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"64\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:16:16\",\"CounterAttackTime\":\"0\"},\"65\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"65\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:18:20\",\"CounterAttackTime\":\"0\"},\"66\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"66\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:22:37\",\"CounterAttackTime\":\"0\"},\"67\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"67\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:25:52\",\"CounterAttackTime\":\"0\"},\"68\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"68\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:29:49\",\"CounterAttackTime\":\"1390494865\"},\"69\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"69\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:33:01\",\"CounterAttackTime\":\"0\"},\"70\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"70\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:40:42\",\"CounterAttackTime\":\"0\"},\"71\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"71\",\"Type\":\"1\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 11:43:26\",\"CounterAttackTime\":\"1390494865\"},\"72\":{\"Uid\":\"630786\",\"MapStageDetailId\":\"72\",\"Type\":\"2\",\"MapStageId\":\"8\",\"FinishedStage\":\"1\",\"LastFinishedTime\":\"2013-11-29 13:24:21\",\"CounterAttackTime\":\"0\"}},\"version\":{\"http\":\"201302272\",\"stop\":\"\",\"appversion\":\"version_1\",\"appurl\":\"ios://xxx\"}}";
//		JSONObject jbect_responseText = JSONObject.fromObject(responseText);
//		List<MapStageDetail> resultlist = new Vector<MapStageDetail>();
//		String jbect_responseText_data = jbect_responseText.get("data").toString().toLowerCase();//小写
//		jbect_responseText_data = "{\"data\":[" + jbect_responseText_data.substring(1,jbect_responseText_data.length()-1) + "]}";
//		for(int i=1;i<200;i++){
//			jbect_responseText_data = jbect_responseText_data.replace("\""+i+"\":{", "{");
//		}
//		System.out.println(jbect_responseText_data);
//		JSONObject jbect_data = JSONObject.fromObject(jbect_responseText_data);
//		UserMapStages obj_data = (UserMapStages)JSONObject.toBean(jbect_data, UserMapStages.class);
//		for(int j=0;j<obj_data.getData().length;j++){
//			//判断3星最大关卡
//			if(obj_data.getData()[j].getFinishedstage().equals("3")){
//				String Mapstagedetailid = obj_data.getData()[j].getMapstagedetailid();
//				MapStageDetail md = new MapStageDetail(Integer.valueOf(Mapstagedetailid));
//				resultlist.add(md);
//				//System.out.println(Mapstagedetailid);
//			}
//			//判断开塔
//			if(obj_data.getData()[j].getMapstagedetailid().equals("39")){
//				System.out.println("5塔");
//			}
//			if(obj_data.getData()[j].getMapstagedetailid().equals("49")){
//				System.out.println("6塔");				
//			}
//			if(obj_data.getData()[j].getMapstagedetailid().equals("60")){
//				System.out.println("7塔");
//			}
//			if(obj_data.getData()[j].getMapstagedetailid().equals("72")){
//				System.out.println("8塔");
//			}
//		}
//		//jdk1.7版本和旧版本调用排序方法不同,需要加入如下系统设置
//		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
//		Collections.sort(resultlist, new ComparatorMapStageDetail());
//		System.out.println(resultlist.get(0).getMapStageDetailId());
		
//		不开隐藏也有塔
//		1图 6 (1,2,3,4,5,6)
//		2图 6 (7,8,9,10,11,12) 13是2塔
//		3图 7+1 (14,15,16,17,18,19,20,21) 22是3塔
//		4图 7+1 (23,24,25,26,27,28,29,30) 31是4塔
//		5图 8+1 (32,33,34,35,36,37,38,39,40) 41是5塔
//		6图 8+1 (42,43,44,45,46,47,48,49,50) 51是6塔
//		7图 9+1 (52,53,54,55,56,57,58,59,60,61) 62是7塔
//		8图 10+1 (63,64,65,66,67,68,69,70,71,72,73) 74是8塔
		//5塔判断39		
		//6塔判断49		
		//7塔判断60		
		//8塔判断72
		
		String responseText = "{\"status\":1,\"data\":{\"BattleId\":\"7799f5aace4b514bf1d97968a0254c7d52ede7622c764\",\"Win\":1,\"ExtData\":{\"Award\":{\"Coins\":5140,\"Exp\":4030,\"CardId\":0},\"Clear\":{\"IsClear\":1,\"CardId\":159,\"Coins\":11000,\"SecondDropCard\":[{\"CardId\":\"7003\"}]},\"User\":{\"Level\":51,\"Exp\":6913340,\"PrevExp\":6263700,\"NextExp\":7364600}},\"prepare\":null,\"AttackPlayer\":{\"Uid\":\"630786\",\"NickName\":\"hui165\",\"Avatar\":8113,\"Sex\":0,\"Level\":51,\"HP\":\"7000\",\"Cards\":[{\"UUID\":\"atk_1\",\"CardId\":\"139\",\"UserCardId\":\"14847607\",\"Attack\":640,\"HP\":1500,\"Wait\":\"6\",\"Level\":\"10\",\"SkillNew\":\"0\",\"Evolution\":\"0\",\"WashTime\":\"0\"},{\"UUID\":\"atk_2\",\"CardId\":\"179\",\"UserCardId\":20370054,\"Attack\":660,\"HP\":1600,\"Wait\":\"4\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_3\",\"CardId\":\"88\",\"UserCardId\":\"19303913\",\"Attack\":495,\"HP\":1220,\"Wait\":\"6\",\"Level\":10,\"SkillNew\":\"0\",\"Evolution\":\"0\",\"WashTime\":\"0\"},{\"UUID\":\"atk_4\",\"CardId\":\"169\",\"UserCardId\":20367934,\"Attack\":620,\"HP\":1460,\"Wait\":\"6\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_5\",\"CardId\":\"226\",\"UserCardId\":\"15655987\",\"Attack\":485,\"HP\":1090,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":\"0\",\"Evolution\":\"0\",\"WashTime\":\"0\"},{\"UUID\":\"atk_6\",\"CardId\":\"59\",\"UserCardId\":21001769,\"Attack\":605,\"HP\":2040,\"Wait\":\"4\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_7\",\"CardId\":\"74\",\"UserCardId\":\"16003872\",\"Attack\":400,\"HP\":885,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":\"0\",\"Evolution\":\"0\",\"WashTime\":\"0\"},{\"UUID\":\"atk_8\",\"CardId\":\"56\",\"UserCardId\":20703557,\"Attack\":450,\"HP\":1160,\"Wait\":\"6\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_9\",\"CardId\":\"246\",\"UserCardId\":21237181,\"Attack\":525,\"HP\":1370,\"Wait\":\"4\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null}],\"Runes\":[{\"UUID\":\"atkrune_10\",\"RuneId\":\"35\",\"UserRuneId\":\"2249266\",\"Level\":\"3\"},{\"UUID\":\"atkrune_11\",\"RuneId\":\"6\",\"UserRuneId\":\"2150850\",\"Level\":\"2\"},{\"UUID\":\"atkrune_12\",\"RuneId\":\"22\",\"UserRuneId\":\"2249264\",\"Level\":\"0\"},{\"UUID\":\"atkrune_13\",\"RuneId\":\"12\",\"UserRuneId\":\"2151295\",\"Level\":\"0\"}],\"RemainHP\":6569},\"DefendPlayer\":{\"Uid\":0,\"NickName\":\"\u8ff7\u5bab\u5bfb\u5b9d\u8005\",\"Avatar\":89,\"Sex\":1,\"Level\":54,\"HP\":\"7360\",\"Cards\":[{\"UUID\":\"def_1\",\"CardId\":60,\"UserCardId\":0,\"Attack\":406,\"HP\":1506,\"Wait\":\"6\",\"Level\":2,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"def_2\",\"CardId\":86,\"UserCardId\":1,\"Attack\":320,\"HP\":1120,\"Wait\":\"6\",\"Level\":7,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"def_3\",\"CardId\":53,\"UserCardId\":2,\"Attack\":431,\"HP\":1041,\"Wait\":\"4\",\"Level\":7,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"def_4\",\"CardId\":57,\"UserCardId\":3,\"Attack\":416,\"HP\":1396,\"Wait\":\"6\",\"Level\":7,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"def_5\",\"CardId\":24,\"UserCardId\":4,\"Attack\":356,\"HP\":1073,\"Wait\":\"4\",\"Level\":7,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"def_6\",\"CardId\":88,\"UserCardId\":5,\"Attack\":420,\"HP\":1124,\"Wait\":\"6\",\"Level\":7,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"def_7\",\"CardId\":119,\"UserCardId\":6,\"Attack\":418,\"HP\":1286,\"Wait\":\"6\",\"Level\":2,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"def_8\",\"CardId\":29,\"UserCardId\":7,\"Attack\":415,\"HP\":1526,\"Wait\":\"6\",\"Level\":2,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"def_9\",\"CardId\":89,\"UserCardId\":8,\"Attack\":380,\"HP\":1190,\"Wait\":\"6\",\"Level\":2,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null}],\"Runes\":[{\"UUID\":\"defrune_10\",\"RuneId\":7,\"UserRuneId\":0,\"Level\":3},{\"UUID\":\"defrune_11\",\"RuneId\":19,\"UserRuneId\":1,\"Level\":3},{\"UUID\":\"defrune_12\",\"RuneId\":29,\"UserRuneId\":2,\"Level\":2},{\"UUID\":\"defrune_13\",\"RuneId\":39,\"UserRuneId\":3,\"Level\":3}],\"RemainHP\":0},\"Battle\":[{\"Round\":1,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_8\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":2,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_3\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":3,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_2\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":4,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_9\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":5,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_7\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":6,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_6\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"def_3\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_3\",\"Opp\":1030,\"Target\":[\"atk\"],\"Value\":431},{\"UUID\":\"def_3\",\"Opp\":1022,\"Target\":[\"atk\"],\"Value\":-431},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":7,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_3\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_8\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_2\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_2\",\"Opp\":31,\"Target\":[\"atk_8\"],\"Value\":175},{\"UUID\":\"atk_2\",\"Opp\":1020,\"Target\":[\"atk_8\"],\"Value\":175},{\"UUID\":\"atk_2\",\"Opp\":63,\"Target\":[\"def_6\"],\"Value\":0},{\"UUID\":\"def_6\",\"Opp\":1003,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_8\",\"Opp\":19,\"Target\":[\"def_3\"],\"Value\":125},{\"UUID\":\"atk_8\",\"Opp\":1040,\"Target\":[\"def_3\"],\"Value\":-236},{\"UUID\":\"atk_8\",\"Opp\":1030,\"Target\":[\"def_3\"],\"Value\":625},{\"UUID\":\"atk_8\",\"Opp\":25,\"Target\":[\"def_3\"],\"Value\":-140},{\"UUID\":\"atk_8\",\"HP\":805,\"Opp\":1040,\"Target\":[\"def_3\"],\"Value\":-485},{\"UUID\":\"atk_2\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":660},{\"UUID\":\"atk_2\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-660},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":8,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_2\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"defrune_13\",\"Opp\":61,\"Target\":[\"atk_8\",\"atk_2\"],\"Value\":40},{\"UUID\":\"defrune_13\",\"Opp\":1020,\"Target\":[\"atk_8\"],\"Value\":-40},{\"UUID\":\"defrune_13\",\"Opp\":1040,\"Target\":[\"atk_8\"],\"Value\":-40},{\"UUID\":\"defrune_13\",\"Opp\":1020,\"Target\":[\"atk_2\"],\"Value\":-40},{\"UUID\":\"defrune_13\",\"Opp\":1040,\"Target\":[\"atk_2\"],\"Value\":-40},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_3\",\"Opp\":94,\"Target\":[\"def_3\"],\"Value\":100},{\"UUID\":\"def_3\",\"Opp\":1020,\"Target\":[\"def_3\"],\"Value\":431},{\"UUID\":\"def_3\",\"Opp\":1030,\"Target\":[\"atk_8\"],\"Value\":862},{\"UUID\":\"def_3\",\"HP\":1120,\"Opp\":1040,\"Target\":[\"atk_8\"],\"Value\":-862},{\"UUID\":\"def_3\",\"Opp\":1020,\"Target\":[\"def_3\"],\"Value\":-431},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":9,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_1\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_7\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_8\",\"Opp\":19,\"Target\":[\"def_3\"],\"Value\":125},{\"UUID\":\"atk_8\",\"Opp\":1040,\"Target\":[\"def_3\"],\"Value\":-228},{\"UUID\":\"atk_8\",\"Opp\":1030,\"Target\":[\"def_3\"],\"Value\":585},{\"UUID\":\"atk_8\",\"Opp\":25,\"Target\":[\"def_3\"],\"Value\":-140},{\"UUID\":\"atk_8\",\"HP\":92,\"Opp\":1040,\"Target\":[\"def_3\"],\"Value\":-92},{\"UUID\":\"def_3\",\"Opp\":1003,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_2\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":620},{\"UUID\":\"atk_2\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-620},{\"UUID\":\"atk_7\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":400},{\"UUID\":\"atk_7\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-400},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":10,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_5\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"def_9\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"defrune_13\",\"Opp\":61,\"Target\":[\"atk_8\",\"atk_2\",\"atk_7\"],\"Value\":40},{\"UUID\":\"defrune_13\",\"Opp\":1020,\"Target\":[\"atk_8\"],\"Value\":-40},{\"UUID\":\"defrune_13\",\"Opp\":1040,\"Target\":[\"atk_8\"],\"Value\":-40},{\"UUID\":\"defrune_13\",\"Opp\":1020,\"Target\":[\"atk_2\"],\"Value\":-40},{\"UUID\":\"defrune_13\",\"Opp\":1040,\"Target\":[\"atk_2\"],\"Value\":-40},{\"UUID\":\"defrune_13\",\"Opp\":1020,\"Target\":[\"atk_7\"],\"Value\":-40},{\"UUID\":\"defrune_13\",\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":-40},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_9\",\"Opp\":14,\"Target\":[\"atk_8\",\"atk_2\",\"atk_7\"],\"Value\":175},{\"UUID\":\"def_9\",\"Opp\":1102,\"Target\":[\"atk_8\"],\"Value\":1},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_8\"],\"Value\":-175},{\"UUID\":\"atk_2\",\"Opp\":75,\"Target\":[\"atk_2\"],\"Value\":70},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_2\"],\"Value\":-70},{\"UUID\":\"def_9\",\"Opp\":1102,\"Target\":[\"atk_7\"],\"Value\":1},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":-175},{\"UUID\":\"def_9\",\"Opp\":1030,\"Target\":[\"atk_8\"],\"Value\":380},{\"UUID\":\"def_9\",\"HP\":43,\"Opp\":1040,\"Target\":[\"atk_8\"],\"Value\":-43},{\"UUID\":\"atk_8\",\"Opp\":1003,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":11,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_4\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_2\",\"Opp\":1030,\"Target\":[\"def_9\"],\"Value\":580},{\"UUID\":\"atk_2\",\"HP\":1190,\"Opp\":1040,\"Target\":[\"def_9\"],\"Value\":-580},{\"UUID\":\"atk_7\",\"Opp\":23,\"Target\":[\"def_9\"],\"Value\":40},{\"UUID\":\"atk_7\",\"Opp\":1040,\"Target\":[\"def_9\"],\"Value\":-40},{\"UUID\":\"atk_7\",\"Opp\":1104,\"Target\":[\"def_9\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":11,\"Target\":[\"def_9\"],\"Value\":null},{\"UUID\":\"atk_7\",\"Opp\":1102,\"Target\":[\"atk_7\"],\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":12,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_1\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_9\",\"Opp\":14,\"Target\":[\"atk_2\",\"atk_7\"],\"Value\":175},{\"UUID\":\"atk_2\",\"Opp\":75,\"Target\":[\"atk_2\"],\"Value\":70},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_2\"],\"Value\":-70},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":-175},{\"UUID\":\"def_9\",\"Opp\":1030,\"Target\":[\"atk_2\"],\"Value\":380},{\"UUID\":\"def_9\",\"HP\":1380,\"Opp\":1040,\"Target\":[\"atk_2\"],\"Value\":-380},{\"UUID\":\"def_9\",\"Opp\":1104,\"Target\":[\"def_9\"],\"Value\":0},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"def_9\"],\"Value\":-40},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":13,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_9\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_3\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atkrune_10\",\"Opp\":26,\"Target\":[],\"Value\":243},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_2\",\"Opp\":1030,\"Target\":[\"def_9\"],\"Value\":580},{\"UUID\":\"atk_2\",\"HP\":530,\"Opp\":1040,\"Target\":[\"def_9\"],\"Value\":-530},{\"UUID\":\"def_9\",\"Opp\":1003,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_7\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":360},{\"UUID\":\"atk_7\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-360},{\"UUID\":\"atk_3\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":495},{\"UUID\":\"atk_3\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-495},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":14,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_8\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"def_2\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"def_2\",\"Opp\":45,\"Target\":[\"atk_2\",\"atk_7\",\"atk_3\"],\"Value\":-25},{\"UUID\":\"def_2\",\"Opp\":1020,\"Target\":[\"atk_2\"],\"Value\":-25},{\"UUID\":\"def_2\",\"Opp\":1020,\"Target\":[\"atk_7\"],\"Value\":-25},{\"UUID\":\"def_2\",\"Opp\":1020,\"Target\":[\"atk_3\"],\"Value\":-25},{\"UUID\":\"def_5\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_2\",\"Opp\":30,\"Target\":[\"atk_2\",\"atk_7\"],\"Value\":320},{\"UUID\":\"def_2\",\"Opp\":25,\"Target\":[\"atk_2\"],\"Value\":-160},{\"UUID\":\"def_2\",\"HP\":1000,\"Opp\":1040,\"Target\":[\"atk_2\"],\"Value\":-160},{\"UUID\":\"def_2\",\"Opp\":25,\"Target\":[\"atk_7\"],\"Value\":-160},{\"UUID\":\"def_2\",\"HP\":495,\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":0},{\"UUID\":\"def_5\",\"Opp\":15,\"Target\":[\"atk_3\"],\"Value\":80},{\"UUID\":\"def_5\",\"Opp\":1103,\"Target\":[\"atk_3\"],\"Value\":1},{\"UUID\":\"def_5\",\"Opp\":1040,\"Target\":[\"atk_3\"],\"Value\":-80},{\"UUID\":\"def_5\",\"Opp\":1030,\"Target\":[\"atk_7\"],\"Value\":356},{\"UUID\":\"def_5\",\"Opp\":25,\"Target\":[\"atk_7\"],\"Value\":-160},{\"UUID\":\"def_5\",\"HP\":495,\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":-196},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":15,\"isAttack\":true,\"Opps\":[{\"UUID\":\"atkrune_10\",\"Opp\":26,\"Target\":[],\"Value\":-243},{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_6\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_1\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atkrune_10\",\"Opp\":26,\"Target\":[],\"Value\":243},{\"UUID\":\"atkrune_13\",\"Opp\":16,\"Target\":[\"def_2\",\"def_5\"],\"Value\":20},{\"UUID\":\"atkrune_13\",\"Opp\":1040,\"Target\":[\"def_2\"],\"Value\":-20},{\"UUID\":\"atkrune_13\",\"Opp\":1103,\"Target\":[\"def_5\"],\"Value\":1},{\"UUID\":\"atkrune_13\",\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-20},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_2\",\"Opp\":1030,\"Target\":[\"def_2\"],\"Value\":555},{\"UUID\":\"atk_2\",\"HP\":1100,\"Opp\":1040,\"Target\":[\"def_2\"],\"Value\":-555},{\"UUID\":\"atk_7\",\"Opp\":23,\"Target\":[\"def_2\",\"def_5\"],\"Value\":40},{\"UUID\":\"atk_7\",\"Opp\":1040,\"Target\":[\"def_2\"],\"Value\":-40},{\"UUID\":\"atk_7\",\"Opp\":1104,\"Target\":[\"def_2\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-40},{\"UUID\":\"atk_7\",\"Opp\":1104,\"Target\":[\"def_5\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":11,\"Target\":[\"def_2\",\"def_5\"],\"Value\":null},{\"UUID\":\"atk_7\",\"Opp\":1101,\"Target\":[\"def_2\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":1101,\"Target\":[\"def_5\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":1030,\"Target\":[\"def_5\"],\"Value\":335},{\"UUID\":\"atk_7\",\"HP\":1013,\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-335},{\"UUID\":\"atk_3\",\"Opp\":1103,\"Target\":[\"atk_3\"],\"Value\":0},{\"UUID\":\"atk_1\",\"Opp\":61,\"Target\":[\"def_2\",\"def_5\"],\"Value\":40},{\"UUID\":\"atk_1\",\"Opp\":1020,\"Target\":[\"def_2\"],\"Value\":-40},{\"UUID\":\"atk_1\",\"Opp\":1040,\"Target\":[\"def_2\"],\"Value\":-40},{\"UUID\":\"atk_1\",\"Opp\":1020,\"Target\":[\"def_5\"],\"Value\":-40},{\"UUID\":\"atk_1\",\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-40},{\"UUID\":\"atk_1\",\"Opp\":22,\"Target\":[\"def_5\"],\"Value\":160},{\"UUID\":\"atk_1\",\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-160},{\"UUID\":\"atk_1\",\"Opp\":1104,\"Target\":[\"def_5\"],\"Value\":1},{\"UUID\":\"atk_1\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":640},{\"UUID\":\"atk_1\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-640},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":16,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_4\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_2\",\"Opp\":1101,\"Target\":[\"def_2\"],\"Value\":0},{\"UUID\":\"def_2\",\"Opp\":1104,\"Target\":[\"def_2\"],\"Value\":0},{\"UUID\":\"def_2\",\"Opp\":1040,\"Target\":[\"def_2\"],\"Value\":-40},{\"UUID\":\"def_5\",\"Opp\":1103,\"Target\":[\"def_5\"],\"Value\":0},{\"UUID\":\"def_5\",\"Opp\":1101,\"Target\":[\"def_5\"],\"Value\":0},{\"UUID\":\"def_5\",\"Opp\":1104,\"Target\":[\"def_5\"],\"Value\":0},{\"UUID\":\"def_5\",\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-40},{\"UUID\":\"def_5\",\"Opp\":1104,\"Target\":[\"def_5\"],\"Value\":0},{\"UUID\":\"def_5\",\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-160},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":17,\"isAttack\":true,\"Opps\":[{\"UUID\":\"atkrune_10\",\"Opp\":26,\"Target\":[],\"Value\":-243},{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_5\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_4\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_9\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_9\",\"Opp\":34,\"Target\":[\"atk_7\",\"atk_3\",\"atk_1\",\"atk_4\"],\"Value\":150},{\"UUID\":\"atk_9\",\"Opp\":1020,\"Target\":[\"atk_7\"],\"Value\":150},{\"UUID\":\"atk_9\",\"Opp\":1020,\"Target\":[\"atk_3\"],\"Value\":150},{\"UUID\":\"atk_9\",\"Opp\":1020,\"Target\":[\"atk_1\"],\"Value\":150},{\"UUID\":\"atk_9\",\"Opp\":1020,\"Target\":[\"atk_4\"],\"Value\":150},{\"UUID\":\"atk_9\",\"Opp\":39,\"Target\":[\"atk_7\",\"atk_3\",\"atk_1\",\"atk_4\"],\"Value\":350},{\"UUID\":\"atk_9\",\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":350},{\"UUID\":\"atk_9\",\"Opp\":1040,\"Target\":[\"atk_3\"],\"Value\":350},{\"UUID\":\"atk_9\",\"Opp\":1040,\"Target\":[\"atk_1\"],\"Value\":350},{\"UUID\":\"atk_9\",\"Opp\":1040,\"Target\":[\"atk_4\"],\"Value\":350},{\"UUID\":\"atkrune_10\",\"Opp\":26,\"Target\":[],\"Value\":243},{\"UUID\":\"atkrune_13\",\"Opp\":16,\"Target\":[\"def_2\",\"def_5\"],\"Value\":20},{\"UUID\":\"atkrune_13\",\"Opp\":1040,\"Target\":[\"def_2\"],\"Value\":-20},{\"UUID\":\"atkrune_13\",\"Opp\":1103,\"Target\":[\"def_5\"],\"Value\":1},{\"UUID\":\"atkrune_13\",\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-20},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_2\",\"Opp\":1030,\"Target\":[\"def_2\"],\"Value\":555},{\"UUID\":\"atk_2\",\"HP\":405,\"Opp\":1040,\"Target\":[\"def_2\"],\"Value\":-405},{\"UUID\":\"def_2\",\"Opp\":1003,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_7\",\"Opp\":23,\"Target\":[\"def_5\"],\"Value\":40},{\"UUID\":\"atk_7\",\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-40},{\"UUID\":\"atk_7\",\"Opp\":1104,\"Target\":[\"def_5\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":11,\"Target\":[\"def_5\"],\"Value\":null},{\"UUID\":\"atk_7\",\"Opp\":1101,\"Target\":[\"def_5\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":1030,\"Target\":[\"def_5\"],\"Value\":485},{\"UUID\":\"atk_7\",\"HP\":218,\"Opp\":1040,\"Target\":[\"def_5\"],\"Value\":-218},{\"UUID\":\"def_5\",\"Opp\":1003,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_3\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":620},{\"UUID\":\"atk_3\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-620},{\"UUID\":\"atk_1\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":790},{\"UUID\":\"atk_1\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-790},{\"UUID\":\"atk_4\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":770},{\"UUID\":\"atk_4\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-770},{\"UUID\":\"atk_9\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":525},{\"UUID\":\"atk_9\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-525},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":18,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_7\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"def_1\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_1\",\"Opp\":1030,\"Target\":[\"atk_2\"],\"Value\":406},{\"UUID\":\"def_1\",\"Opp\":25,\"Target\":[\"atk_2\"],\"Value\":-160},{\"UUID\":\"def_1\",\"HP\":840,\"Opp\":1040,\"Target\":[\"atk_2\"],\"Value\":-246},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":19,\"isAttack\":true,\"Opps\":[{\"UUID\":\"atkrune_10\",\"Opp\":26,\"Target\":[],\"Value\":-243},{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_6\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_2\",\"Opp\":31,\"Target\":[\"atk_6\"],\"Value\":175},{\"UUID\":\"atk_2\",\"Opp\":1020,\"Target\":[\"atk_6\"],\"Value\":175},{\"UUID\":\"atkrune_10\",\"Opp\":26,\"Target\":[],\"Value\":243},{\"UUID\":\"atkrune_13\",\"Opp\":16,\"Target\":[\"def_1\"],\"Value\":20},{\"UUID\":\"atkrune_13\",\"Opp\":1103,\"Target\":[\"def_1\"],\"Value\":1},{\"UUID\":\"atkrune_13\",\"Opp\":1040,\"Target\":[\"def_1\"],\"Value\":-20},{\"UUID\":\"atkrune_13\",\"Opp\":1050,\"Target\":[\"atkrune_13\"],\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_2\",\"Opp\":1030,\"Target\":[\"def_1\"],\"Value\":555},{\"UUID\":\"atk_2\",\"HP\":1486,\"Opp\":1040,\"Target\":[\"def_1\"],\"Value\":-555},{\"UUID\":\"atk_7\",\"Opp\":23,\"Target\":[\"def_1\"],\"Value\":40},{\"UUID\":\"atk_7\",\"Opp\":1040,\"Target\":[\"def_1\"],\"Value\":-40},{\"UUID\":\"atk_7\",\"Opp\":1104,\"Target\":[\"def_1\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":11,\"Target\":[\"def_1\"],\"Value\":null},{\"UUID\":\"atk_7\",\"Opp\":1101,\"Target\":[\"def_1\"],\"Value\":1},{\"UUID\":\"atk_7\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":485},{\"UUID\":\"atk_7\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-485},{\"UUID\":\"atk_3\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":620},{\"UUID\":\"atk_3\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-620},{\"UUID\":\"atk_1\",\"Opp\":61,\"Target\":[\"def_1\"],\"Value\":40},{\"UUID\":\"atk_1\",\"Opp\":1020,\"Target\":[\"def_1\"],\"Value\":-40},{\"UUID\":\"atk_1\",\"Opp\":1040,\"Target\":[\"def_1\"],\"Value\":-40},{\"UUID\":\"atk_1\",\"Opp\":22,\"Target\":[\"def_1\"],\"Value\":160},{\"UUID\":\"atk_1\",\"Opp\":1040,\"Target\":[\"def_1\"],\"Value\":-160},{\"UUID\":\"atk_1\",\"Opp\":1104,\"Target\":[\"def_1\"],\"Value\":1},{\"UUID\":\"atk_1\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":790},{\"UUID\":\"atk_1\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-375}]}]},\"version\":{\"http\":\"201302278\",\"stop\":\"\",\"appversion\":\"version_1\",\"appurl\":\"ios://xxx\"}}";
		JSONObject jbect_responseText = JSONObject.fromObject(responseText.toLowerCase());
		String responseText_data = jbect_responseText.get("data").toString().toLowerCase();//小写
		JSONObject jbect_data = JSONObject.fromObject(responseText_data.toLowerCase());
		String data_win = jbect_data.get("win").toString();
		String data_extData = jbect_data.get("extdata").toString();
		JSONObject jbect_data_extData = JSONObject.fromObject(data_extData.toLowerCase());
		String data_extData_Clear = jbect_data_extData.get("clear").toString();
		JSONObject jbect_data_extData_Clear = JSONObject.fromObject(data_extData_Clear.toLowerCase());
		String data_extData_Clear_IsClear = jbect_data_extData_Clear.get("isclear").toString();
		System.out.println(data_win);
		System.out.println(data_extData_Clear_IsClear);
		
		String s = "{\"status\":1,\"version\":{\"http\":\"201302278\",\"stop\":\"\",\"appversion\":\"version_1\",\"appurl\":\"ios://xxx\"}}";
		JSONObject jb1 = JSONObject.fromObject(s.toLowerCase());
		System.out.println(jb1.getString("status"));
		
		String sss = "{\"returnCode\":\"0\",\"returnMsg\":\"No error.\",\"returnObjs\":{\"GS_NAME\":\"server4\",\"GS_IP\":\"http://s4.mysticalcard.com/\",\"friendCode\":\"null\",\"GS_PORT\":\"80\",\"timestamp\":\"1391605577342\",\"GS_CHAT_PORT\":\"8000\",\"source\":\"3c:e0:72:b9:dc:24\",\"userName\":\"3c:e0:72:b9:dc:24\",\"GS_DESC\":\"é???±??·¨é??\",\"U_ID\":\"1706866\",\"uEmailState\":\"1\",\"G_TYPE\":\"1\",\"key\":\"24ee01596433bbee5888d79b5b5f95c9\",\"GS_CHAT_IP\":\"218.245.7.8\"}}";
		JSONObject sssjb1 = JSONObject.fromObject(sss.toLowerCase());
		HttpService obj = (HttpService)JSONObject.toBean(sssjb1, HttpService.class);
//		System.out.println(obj.getReturnobjs().getGs_desc());

		//JSONObject jbect_responseText = JSONObject.fromObject(ss.toLowerCase());
		//String responseText_data = jbect_responseText.get("data").toString().toLowerCase();//小写
		//System.out.println(responseText_data);
		//JSONObject jbect_responseText_data = JSONObject.fromObject(responseText_data);
		//MazeInfo obj_data = (MazeInfo)JSONObject.toBean(jbect_responseText, MazeInfo.class);
		//System.out.println(obj_data.getData().getName());
		//for(int i=0;i<obj_data.getData().getMap().getItems().length;i++)
		//System.out.println(obj_data.getData().getMap().getItems().length);
		//System.out.println(obj_data.getData().getMap().getItems()[3]);
		//System.out.println(util.StringUtil.unicodeToString(ss));
		//JSONObject jbect_responseText1 = JSONObject.fromObject(jbect_responseText.get("data").toString());
		//System.out.println(jbect_responseText1.get("win"));
		/*
		List a = new Vector();
		UserMapStages u1 = new UserMapStages();
		u1.setIs_win(true);
		u1.setMapstagedetailid("1");
		a.add(u1);
		UserMapStages u2 = new UserMapStages();
		u2.setIs_win(false);
		u2.setMapstagedetailid("2");
		a.add(u2);
		UserMapStages u3 = new UserMapStages();
		u3.setIs_win(true);
		u3.setMapstagedetailid("3");
		a.add(u3);
		//Iterator it = a.iterator();
		int i = 0;
		int Energy = 50;//体力
		while(Energy>1 && a.size()>0){
			//Thread.sleep(1000);
			i++;
			for(int j=0;j<a.size();j++){
				UserMapStages obj = (UserMapStages)a.get(j);
				System.out.print("i:"+i+" ");
				System.out.print("Mapstagedetailid:"+obj.getMapstagedetailid()+" ");
				System.out.print("Is_win:"+obj.isIs_win()+" ");
				System.out.print("Energy:"+Energy+" ");
				System.out.println("a.size():"+a.size());
				Energy = Energy - 2;
				if(obj.isIs_win()){
					a.remove(obj);
					//it = a.iterator();
				}
			}			
		}*/
		System.out.println(util.StringUtil.unicodeToString("passport\u7b7e\u540d\u9a8c\u8bc1\u5931\u8d25"));
		System.out.println(util.StringUtil.unicodeToString("\u7f51\u7edc\u94fe\u63a5\u5df2\u65ad\u5f00 \u8bf7\u91cd\u65b0\u767b\u5f55"));
		System.out.println(util.StringUtil.unicodeToString("\u6ca1\u6709\u6218\u6597\u4fe1\u606f"));
		System.out.println(util.StringUtil.unicodeToString("\u5361\u7ec4\u5360\u7528\u7684COST\u503c\u4e0d\u80fd\u9ad8\u4e8e125"));
		System.out.println(util.StringUtil.unicodeToString("\u6bcf\u65e5\u665a23\u70b9\u81f3\u6b21\u65e58:00,\u5730\u4e0b\u57ce\u4f11\u6574\uff0c\u6682\u8bf7\u517b\u7cbe\u84c4\u9510"));
		System.out.println(util.StringUtil.unicodeToString("\u83b7\u53d6access_token\u5931\u8d25\uff0c\u8bf7\u91cd\u8bd5\uff01"));
		System.out.println(util.StringUtil.unicodeToString("\u4FEE\u6539\u7ED1\u5B9A\u624B\u673A\u6210\u529F\uFF01"));
		System.out.println(java.net.URLEncoder.encode("71753CDE3B1D46C50CD7A9F6619BD8C0"));
		System.out.println(java.net.URLDecoder.decode("http%3A%2F%2Fs4%2Emysticalcard%2Ecom%2F"));
		System.out.println(java.net.URLDecoder.decode("c4%3A6a%3Ab7%3A86%3Abe%3A51"));
		System.out.println(java.net.URLDecoder.decode("218%2E245%2E7%2E8"));
		System.out.println(new String(java.net.URLDecoder.decode("%E9%87%91%E5%B1%9E%E5%B7%A8%E9%BE%99").getBytes(), "utf-8"));
		System.out.println(new String(java.net.URLDecoder.decode("%E8%8D%A3%E8%80%80%E5%B7%A8%E4%BA%BA").getBytes(), "utf-8"));

		System.out.println(java.net.URLEncoder.encode("http://s4.mysticalcard.com/"));
		System.out.println(java.net.URLEncoder.encode("c4:6a:b7:86:be:51"));
		System.out.println(java.net.URLEncoder.encode("金属巨龙"));
		System.out.println(java.net.URLEncoder.encode("荣誉巨人"));
		
		System.out.println(java.net.URLEncoder.encode("RV5jqKfjVUL5K7KDOQTTPVmQVwv1RmL82rBZOpMrfKhT2TmiYnkEAw/FtvCpEr1HY7o0SyFEtjFUbZpgm7/mD4b7WGLEFml2KvZdA1xD2ubwNwKT9GVAzwZaaZKkbT21/MXlmzOeQKJaipsNpmW9Wg=="));
		
//		StringEntity reqEntity = new StringEntity("username=test&password=test");		
//		InputStream inputStream = reqEntity.getContent();
//        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//        BufferedReader reader = new BufferedReader(inputStreamReader);// 读字符串用的。
//        String sss123;
//        while (((sss123 = reader.readLine()) != null)) {
//            System.out.println(sss123);
//        }
//        reader.close();// 关闭输入流
//        inputStream.close();
//        inputStreamReader.close();
        
        String av = "{\"status\":1,\"data\":{\"actStatus\":{\"time\":{\"start_time\":\"2014-02-01 00:00:00\",\"end_time\":\"2014-02-10 23:59:59\"},\"titleIcon\":\"http://s5.mysticalcard.com/public/swf/devote/ZH_CN/998_1.png\",\"actInfo\":{\"doing\":\"\u65b0\u5e74\u597d\uff01\u5148\u795d\u60a8\u9a6c\u5e74\u5409\u7965\uff0c\u4e07\u4e8b\u5982\u610f\uff01\u73b0\u5728\u8ff7\u5bab\u5b9d\u7bb1\u53ef\u4ee5\u6389\u843d\u3010\u65b0\u5e74\u70df\u706b\u3011\u548c\u3010\u65b0\u5e74\u5230\u798f\u3011,\u6536\u96c6\u5230\u7684\u4eba\u53ef\u4ee5\u4ece\u6211\u8fd9\u6362\u53d6\u597d\u4e1c\u897f\u54e6\uff01\u8fce\u6765\u65b0\u5e74\u597d\u5f69\u5934\uff01\uff01\",\"end\":\"\"},\"recordPoint\":465,\"exchangeStatus\":[{\"status\":1,\"actPoint\":1,\"reward\":{\"ticket\":1},\"needPoint\":0},{\"status\":1,\"actPoint\":10,\"reward\":{\"coins\":50000},\"needPoint\":0},{\"status\":1,\"actPoint\":30,\"reward\":{\"card\":[\"8101\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":50,\"reward\":{\"card\":[\"8002\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":120,\"reward\":{\"cash\":100},\"needPoint\":0},{\"status\":1,\"actPoint\":150,\"reward\":{\"coins\":100000},\"needPoint\":0},{\"status\":1,\"actPoint\":200,\"reward\":{\"card\":[\"142\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":240,\"reward\":{\"ticket\":5},\"needPoint\":0},{\"status\":1,\"actPoint\":280,\"reward\":{\"card\":[\"8102\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":340,\"reward\":{\"cash\":200},\"needPoint\":0},{\"status\":1,\"actPoint\":400,\"reward\":{\"card\":[\"8125\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":460,\"reward\":{\"cash\":300},\"needPoint\":0},{\"status\":3,\"actPoint\":540,\"reward\":{\"card\":[\"8001\"]},\"needPoint\":75},{\"status\":3,\"actPoint\":650,\"reward\":{\"card\":[\"280\"]},\"needPoint\":185},{\"status\":3,\"actPoint\":800,\"reward\":{\"cardChips\":[{\"chipId\":\"6003\",\"num\":\"10\"}]},\"needPoint\":335},{\"status\":3,\"actPoint\":900,\"reward\":{\"card\":[\"280\"]},\"needPoint\":435},{\"status\":3,\"actPoint\":1100,\"reward\":{\"card\":[\"8103\"]},\"needPoint\":635},{\"status\":3,\"actPoint\":1300,\"reward\":{\"card\":[\"305\"]},\"needPoint\":835},{\"status\":3,\"actPoint\":1500,\"reward\":{\"card\":[\"189\"]},\"needPoint\":1035}],\"headIcon\":\"http://s5.mysticalcard.com/public/swf/devote/ZH_CN/999_1.png\",\"dialog\":{\"goods\":\"hoho~\u8c8c\u4f3c\u6536\u96c6\u5230\u6211\u60f3\u8981\u7684\u4e1c\u897f\u4e86\u5462\uff01\u73b0\u5728\u70b9\u51fb\u3010\u5151\u6362\u3011\u6309\u94ae\u5427\u3002\",\"point\":\"\"},\"goods\":[{\"cardId\":7003,\"num\":56,\"point\":\"1\",\"totalPoint\":56},{\"cardId\":7004,\"num\":20,\"point\":\"3\",\"totalPoint\":60}]}},\"version\":{\"http\":\"201302279\",\"stop\":\"\",\"appversion\":\"version_1\",\"appurl\":\"ios://xxx\"}}";
        String goods = "{\"goods\":"+JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(av).getString("data")).getString("actStatus")).getString("goods")+"}";
        System.out.println(goods);
        JSONObject goods_jbect = JSONObject.fromObject(goods);
        Goods actgoods = (Goods)JSONObject.toBean(goods_jbect, Goods.class);
        int num = 0;
        for(int i=0;i<actgoods.getGoods().length;i++){
        	num += actgoods.getGoods()[i].getNum();
        }
        System.out.println(num);
        
        String av1 = "{\"status\":1,\"data\":{\"actStatus\":{\"reward\":{\"Coins\":0,\"Cash\":0,\"Ticket\":0,\"card\":[],\"cardChips\":[]},\"exchangeStatus\":[{\"status\":1,\"actPoint\":1,\"reward\":{\"ticket\":1},\"needPoint\":0},{\"status\":1,\"actPoint\":10,\"reward\":{\"coins\":50000},\"needPoint\":0},{\"status\":1,\"actPoint\":30,\"reward\":{\"card\":[\"8101\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":50,\"reward\":{\"card\":[\"8002\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":120,\"reward\":{\"cash\":100},\"needPoint\":0},{\"status\":1,\"actPoint\":150,\"reward\":{\"coins\":100000},\"needPoint\":0},{\"status\":1,\"actPoint\":200,\"reward\":{\"card\":[\"142\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":240,\"reward\":{\"ticket\":5},\"needPoint\":0},{\"status\":1,\"actPoint\":280,\"reward\":{\"card\":[\"8102\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":340,\"reward\":{\"cash\":200},\"needPoint\":0},{\"status\":1,\"actPoint\":400,\"reward\":{\"card\":[\"8125\"]},\"needPoint\":0},{\"status\":1,\"actPoint\":460,\"reward\":{\"cash\":300},\"needPoint\":0},{\"status\":1,\"actPoint\":540,\"reward\":{\"card\":[\"8001\"]},\"needPoint\":0},{\"status\":3,\"actPoint\":650,\"reward\":{\"card\":[\"280\"]},\"needPoint\":22},{\"status\":3,\"actPoint\":800,\"reward\":{\"cardChips\":[{\"chipId\":\"6003\",\"num\":\"10\"}]},\"needPoint\":172},{\"status\":3,\"actPoint\":900,\"reward\":{\"card\":[\"280\"]},\"needPoint\":272},{\"status\":3,\"actPoint\":1100,\"reward\":{\"card\":[\"8103\"]},\"needPoint\":472},{\"status\":3,\"actPoint\":1300,\"reward\":{\"card\":[\"305\"]},\"needPoint\":672},{\"status\":3,\"actPoint\":1500,\"reward\":{\"card\":[\"189\"]},\"needPoint\":872}],\"headIcon\":\"http://s4.mysticalcard.com/public/swf/devote/ZH_CN/999_1.png\",\"dialog\":{\"goods\":\"\",\"point\":\"下个目标是650分。如把剩余的22分的活动道具给我话，可以交换赤炎鬼武士卡牌。等你哦！\",\"reward\":\"加上这次收集到的积分71你一共获得了<font color = '#FF0000'>（合计积分）</font>628分呢！但是这次并没有达成目标奖励所需的积分。\",\"finish\":\"\"}}},\"version\":{\"http\":\"201302279\",\"stop\":\"\",\"appversion\":\"version_1\",\"appurl\":\"ios://xxx\"}}";
        System.out.println(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(av1).getString("data")).getString("actStatus")).getString("dialog"));

        DuokuAesUtil pd = new DuokuAesUtil();
        String pd_source = "{\"os\":\"android4.1.2\",\"connecttype\":\"wifi\",\"tag\":\"100\",\"gameversion\":\"1.4.0\",\"imei\":\"867747012591218\",\"appkey\":\"de2492517d570e827a8b959b17d35aac\",\"appid\":\"160\",\"ua\":\"MI 1S\",\"app_secret\":\"5e3a286ed28430c05001421099530f5a\",\"udid\":\"DF1B11F419EA2F3CCCC1677540325788\",\"sessionid\":\"71753CDE3B1D46C50CD7A9F6619BD8C0\",\"channel\":\"700\",\"version\":\"1.1.0\"}";
        System.out.println("pd_source:"+pd_source);
        String pda = pd.a(pd_source);
        System.out.println("pad:"+pda);
        String pdb = pd.b(pda);
        System.out.println("pdb:"+pdb);
        
        String userinfo1 = "{\"status\":1,\"data\":{\"Uid\":\"630786\",\"PwdLockMessage\":\"\",\"Sex\":\"0\",\"NickName\":\"hui165\",\"Avatar\":8113,\"Win\":\"0\",\"Lose\":\"0\",\"Level\":53,\"Exp\":8555390,\"Coins\":3253650,\"Cash\":48,\"Ticket\":2,\"FreshStep\":{\"1\":\"21\",\"2\":\"17\",\"12\":\"6\",\"4\":\"13\",\"3\":\"17\",\"9\":\"9\",\"7\":\"7\",\"8\":\"3\",\"5\":\"16\",\"11\":\"6\",\"6\":\"5\",\"14\":\"7\"},\"Energy\":0,\"EnergyLastTime\":1392040610,\"EnergyBuyTime\":\"0\",\"EnergyBuyCount\":\"0\",\"EnergyMax\":50,\"LeaderShip\":\"133\",\"FriendApplyNum\":1,\"FriendNumMax\":\"40\",\"DefaultGroupId\":82101,\"RankWin\":\"0\",\"RankLost\":\"0\",\"RankTimes\":\"15\",\"ThievesTimes\":5,\"Fragment_5\":2,\"Fragment_4\":4,\"Fragment_3\":18,\"InviteCode\":\"45dhe4\",\"InviteNum\":\"0\",\"Udid\":\"C4:6A:B7:86:BE:51\",\"LostPoint\":\"12\",\"Origin\":\"safe360\",\"Platform\":\"ANDROID_360\",\"Language\":\"ZH_CN\",\"Idfa\":\"\",\"UserName\":\"safe360_301174488\",\"NewFreshStep\":[],\"HP\":\"7240\",\"PrevExp\":\"7971700\",\"NextExp\":\"8619400\",\"MonthCardDay\":0,\"NewEmail\":213,\"NewGoods\":0,\"SalaryCount\":0,\"LoginContinueTimesXX\":10,\"Boss\":0,\"isMinor\":-1,\"invite\":true,\"gscode\":true,\"appdriver\":false,\"melee\":false,\"legionfight\":false,\"bindid\":false,\"evolution_on\":null,\"minor\":false,\"DevoteActStatus\":{\"maze\":true},\"JourneyActStatus\":{\"isOpen\":false,\"countDown\":-1},\"actStatus\":[]},\"version\":{\"http\":\"201302279\",\"stop\":\"\",\"appversion\":\"version_1\",\"appurl\":\"ios://xxx\"}}";
        System.out.println(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(userinfo1).getString("data")).getString("DevoteActStatus")).getString("maze"));
        System.out.println(JSONObject.fromObject(JSONObject.fromObject(JSONObject.fromObject(userinfo1).getString("data")).getString("JourneyActStatus")).getString("isOpen"));
        
        String drop = "{\"status\":1,\"data\":{\"BattleId\":\"04446e7a395bb9bb8f9812858728bb445301684664072\",\"Win\":1,\"ExtData\":{\"Award\":{\"Coins\":1160,\"Exp\":980,\"Anger\":15},\"AwardChips\":[],\"User\":{\"Level\":51,\"Exp\":6943970,\"PrevExp\":6263700,\"NextExp\":7364600}},\"prepare\":null,\"AttackPlayer\":{\"Uid\":\"631845\",\"NickName\":\"hui167\",\"Avatar\":\"203\",\"Sex\":0,\"Level\":51,\"HP\":\"7000\",\"Cards\":[{\"UUID\":\"atk_1\",\"CardId\":\"203\",\"UserCardId\":\"17432322\",\"Attack\":510,\"HP\":1160,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":\"0\",\"Evolution\":\"0\",\"WashTime\":\"0\"},{\"UUID\":\"atk_2\",\"CardId\":\"88\",\"UserCardId\":\"19901946\",\"Attack\":495,\"HP\":1220,\"Wait\":\"6\",\"Level\":\"10\",\"SkillNew\":\"0\",\"Evolution\":\"0\",\"WashTime\":\"0\"},{\"UUID\":\"atk_3\",\"CardId\":\"138\",\"UserCardId\":22948610,\"Attack\":750,\"HP\":1500,\"Wait\":\"4\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_4\",\"CardId\":\"179\",\"UserCardId\":23339734,\"Attack\":660,\"HP\":1600,\"Wait\":\"4\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_5\",\"CardId\":\"59\",\"UserCardId\":20871354,\"Attack\":605,\"HP\":2040,\"Wait\":\"4\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_6\",\"CardId\":\"246\",\"UserCardId\":21073773,\"Attack\":525,\"HP\":1370,\"Wait\":\"4\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_7\",\"CardId\":\"74\",\"UserCardId\":\"14817084\",\"Attack\":400,\"HP\":885,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":\"0\",\"Evolution\":\"0\",\"WashTime\":\"0\"},{\"UUID\":\"atk_8\",\"CardId\":\"189\",\"UserCardId\":20378358,\"Attack\":675,\"HP\":1760,\"Wait\":\"4\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null},{\"UUID\":\"atk_9\",\"CardId\":\"169\",\"UserCardId\":20354085,\"Attack\":620,\"HP\":1460,\"Wait\":\"6\",\"Level\":10,\"SkillNew\":null,\"Evolution\":null,\"WashTime\":null}],\"Runes\":[{\"UUID\":\"atkrune_10\",\"RuneId\":\"6\",\"UserRuneId\":2355690,\"Level\":1},{\"UUID\":\"atkrune_11\",\"RuneId\":\"35\",\"UserRuneId\":\"2300509\",\"Level\":4},{\"UUID\":\"atkrune_12\",\"RuneId\":\"20\",\"UserRuneId\":2536759,\"Level\":4},{\"UUID\":\"atkrune_13\",\"RuneId\":\"19\",\"UserRuneId\":2536757,\"Level\":3}],\"RemainHP\":5523},\"DefendPlayer\":{\"Uid\":1,\"NickName\":\"\u7b2c36\u5c42\u5b88\u536b\",\"Avatar\":127,\"Sex\":1,\"Level\":\"50\",\"HP\":\"6390\",\"Cards\":[{\"UUID\":\"def_1\",\"CardId\":\"156\",\"UserCardId\":1,\"Attack\":300,\"HP\":1050,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_2\",\"CardId\":\"156\",\"UserCardId\":2,\"Attack\":300,\"HP\":1050,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_3\",\"CardId\":\"156\",\"UserCardId\":3,\"Attack\":300,\"HP\":1050,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_4\",\"CardId\":\"128\",\"UserCardId\":4,\"Attack\":405,\"HP\":1450,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_5\",\"CardId\":\"128\",\"UserCardId\":5,\"Attack\":369,\"HP\":1350,\"Wait\":\"4\",\"Level\":\"8\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_6\",\"CardId\":\"131\",\"UserCardId\":6,\"Attack\":310,\"HP\":760,\"Wait\":\"4\",\"Level\":\"8\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_7\",\"CardId\":\"131\",\"UserCardId\":7,\"Attack\":350,\"HP\":800,\"Wait\":\"4\",\"Level\":\"10\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_8\",\"CardId\":\"89\",\"UserCardId\":8,\"Attack\":500,\"HP\":1460,\"Wait\":\"6\",\"Level\":\"8\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_9\",\"CardId\":\"139\",\"UserCardId\":9,\"Attack\":640,\"HP\":1500,\"Wait\":\"6\",\"Level\":\"10\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0},{\"UUID\":\"def_10\",\"CardId\":\"127\",\"UserCardId\":10,\"Attack\":360,\"HP\":1360,\"Wait\":\"6\",\"Level\":\"10\",\"SkillNew\":0,\"Evolution\":0,\"WashTime\":0}],\"Runes\":[{\"UUID\":\"defrune_11\",\"RuneId\":\"35\",\"UserRuneId\":1,\"Level\":\"2\"},{\"UUID\":\"defrune_12\",\"RuneId\":\"6\",\"UserRuneId\":2,\"Level\":\"2\"},{\"UUID\":\"defrune_13\",\"RuneId\":\"37\",\"UserRuneId\":3,\"Level\":\"2\"},{\"UUID\":\"defrune_14\",\"RuneId\":\"40\",\"UserRuneId\":4,\"Level\":\"2\"}],\"RemainHP\":0},\"Battle\":[{\"Round\":1,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_7\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":2,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_9\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":3,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_2\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":4,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_8\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":5,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_3\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_7\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_7\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":400},{\"UUID\":\"atk_7\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-400},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":6,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_2\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":7,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_8\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_7\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":400},{\"UUID\":\"atk_7\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-400},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":8,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_4\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"def_9\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_9\",\"Opp\":61,\"Target\":[\"atk_7\"],\"Value\":40},{\"UUID\":\"def_9\",\"Opp\":1020,\"Target\":[\"atk_7\"],\"Value\":-40},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":-40},{\"UUID\":\"def_9\",\"Opp\":22,\"Target\":[\"atk_7\"],\"Value\":160},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":-160},{\"UUID\":\"def_9\",\"Opp\":1104,\"Target\":[\"atk_7\"],\"Value\":1},{\"UUID\":\"def_9\",\"Opp\":1030,\"Target\":[\"atk_7\"],\"Value\":640},{\"UUID\":\"def_9\",\"HP\":685,\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":-640},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":9,\"isAttack\":true,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"atk_6\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_2\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_3\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"atkrune_11\",\"Opp\":26,\"Target\":[],\"Value\":244},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"atk_7\",\"Opp\":23,\"Target\":[\"def_9\"],\"Value\":40},{\"UUID\":\"def_9\",\"Opp\":78,\"Target\":[\"def_9\"],\"Value\":0},{\"UUID\":\"atk_7\",\"Opp\":11,\"Target\":[\"def_9\"],\"Value\":null},{\"UUID\":\"def_9\",\"Opp\":78,\"Target\":[\"def_9\"],\"Value\":0},{\"UUID\":\"atk_7\",\"Opp\":1030,\"Target\":[\"def_9\"],\"Value\":360},{\"UUID\":\"atk_7\",\"HP\":1500,\"Opp\":1040,\"Target\":[\"def_9\"],\"Value\":-360},{\"UUID\":\"atk_7\",\"Opp\":1104,\"Target\":[\"atk_7\"],\"Value\":0},{\"UUID\":\"atk_7\",\"Opp\":1040,\"Target\":[\"atk_7\"],\"Value\":-45},{\"UUID\":\"atk_7\",\"Opp\":1003,\"Target\":null,\"Value\":0},{\"UUID\":\"atk_2\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":495},{\"UUID\":\"atk_2\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-495},{\"UUID\":\"atk_3\",\"Opp\":1030,\"Target\":[\"def\"],\"Value\":750},{\"UUID\":\"atk_3\",\"Opp\":1022,\"Target\":[\"def\"],\"Value\":-750},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3}]},{\"Round\":10,\"isAttack\":false,\"Opps\":[{\"UUID\":\"\",\"Opp\":1021,\"Target\":null,\"Value\":-1},{\"UUID\":\"def_1\",\"Opp\":1001,\"Target\":null,\"Value\":0},{\"UUID\":\"def_8\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"def_2\",\"Opp\":1002,\"Target\":null,\"Value\":0},{\"UUID\":\"defrune_11\",\"Opp\":26,\"Target\":[],\"Value\":242},{\"UUID\":\"\",\"Opp\":1060,\"Target\":[],\"Value\":3},{\"UUID\":\"def_9\",\"Opp\":61,\"Target\":[\"atk_2\",\"atk_3\"],\"Value\":40},{\"UUID\":\"def_9\",\"Opp\":1020,\"Target\":[\"atk_2\"],\"Value\":-40},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_2\"],\"Value\":-40},{\"UUID\":\"def_9\",\"Opp\":1020,\"Target\":[\"atk_3\"],\"Value\":-40},{\"UUID\":\"def_9\",\"Opp\":1040,\"Target\":[\"atk_3\"],\"Value\":-40},{\"UUID\":\"def_9\",\"Opp\":22,\"Target\":[\"atk_3\"],\"Value\":160},{\"UUID\":{\"status\":0,\"message\":\"\u6b64\u5c42\u5df2\u901a\u8fc7\uff0c\u65e0\u9700\u518d\u6b21\u6218\u6597\uff01\"}";
        System.out.println(util.StringUtil.unicodeToString(drop));
//        int win = JSONObject.fromObject(JSONObject.fromObject(drop).getString("data")).getInt("Win");		
//		System.out.println(win);
		
//		System.out.println(new String(decode("fa14be8e12ac93121efc9f5d0711efe5"), "iso-8859-1"));
		
        DuokuAesUtil duokuaes = new DuokuAesUtil();
//        System.out.println(duokuaes.b("7NrbOSd/AMBFZHHCzQk8ZnYky2ZEoB/C5hwwxOXpvckGHHOO34BXmVgFwaN6EDdA8KqAa4kGA+52xTv2LMlVeLxsdJuJDUP4wH9wOZOZNXZkrnUkPWa9e3/iZR6x2u7VKyMe7aJe5+RtrxRPmoXsv08pmPXi4yXTljAOqsp2zX/su9mowPW0yAMXdRksnIA3/w94xtV//0fCpfiDCgDZ14NTHJUXcecC4By0gNwhgASpPmpmu+2CWYLT4nZTtFqsSfidoWzUDYwclOBmBTkPJlOdqLSU8UVytM112uwvvF/oPnrdMZPY8wZYcLua1fZOjwaJcUx6ugS5uNW20oEZ8kGOQcaVa1hzh/aZ47215A46HpfhGb3AIGkReXSy4eKQvMUA/GnLFmEpsMj5v8els/YreUgqJ2P+3jFHNGLlifQFn2jihsRrxkFkewRel5Uf+yg1LlDUZMAsHmOmIPt4Aw=="));
        System.out.println(duokuaes.b("7NrbOSd/AMBFZHHCzQk8ZjUKkhc9Dal0Bu5sIpyYZDrHO6aF3KqzzqkCiGN4vMNsH861Tb1KT2NHhkJ43ZaSnTOs+e9BhVORRmpeZzPoOR0K7rjPI5DEpEGfSmX5MONB+pJiNKrs1ZS7brI5sprntcD/YLBSmL8ZUrdCnjm/PYfCduYtmxfIAFtSVqFGPCYy/eIaFjTlrOeJOT/11tV5kOg+et0xk9jzBlhwu5rV9k6PBolxTHq6BLm41bbSgRnyQY5BxpVrWHOH9pnjvbXkDq/Pl6+/KOXOHVEVg3BGBG8xvhHfk7L3oJ551d1XSg9wFbaUQhsFGpRAPGZFyXygD2yAdvpKlTSVkbw3yDZTcMvg3mBTotYtm7qqqrOdecoJu6X+8zx8hqrLWmH09djWwJPsHGv6mX+fejeXCIsDqos1Akir9aZRh8meYc11IcIfcva3MlABGys4eIuLMRebFf9G0Xw6gp5P+yCc5ZoL9sM="));
//        System.out.println(duokuaes.b("7NrbOSd/AMBFZHHCzQk8ZoabP1r3O7jEaqlZTymvLJDHO6aF3KqzzqkCiGN4vMNsH861Tb1KT2NHhkJ43ZaSnSKe0Cf3EbTBqNq+h2nuj714Zr/m9bttcJoom3n15nqw6G9vz7v8+9kaUNzyHc7Dj0osF/zU3Sm6tC61FDhjNq9veOLPBnmZyM6hqNsKKJ6bz58dJWedWFzRB7ZNe0lIAH7dhv1tyJNV4Er6/hDS8D26g98/QG/uuQBjE5ZVWZIYFCUqMYo1pynihwk72lffmgoS8+FP+N1fwmb/fpzP1mKajJLjtj6xJ9TcMlGuARvOgtUWopuQ5ArMS1E5cBQAQDxUQQo0PDQot93CGVZ0FcXiabO2CC2pKl4BQ2XtFXn4kyS2io+j2kAQOM31CHsyNoxSz0qfLpg2EMOpGxIyAGCRAfW0DlEdi+dlo6GHw4rvj6TrreqFclisMrCnZrdL7gBsPMlzcP75DULa+RkbVCbXfVfmh9b3IJVNVhwlYTBD5HiHh9wp3OU6V8X9SpD52RCXSvgFDZqy62Y5TCe2rm0yV6AxvZ9FD4sJdWpisdCF"));
//        System.out.println(duokuaes.b("7NrbOSd/AMBFZHHCzQk8ZoabP1r3O7jEaqlZTymvLJDHO6aF3KqzzqkCiGN4vMNsH861Tb1KT2NHhkJ43ZaSnSKe0Cf3EbTBqNq+h2nuj714Zr/m9bttcJoom3n15nqw6G9vz7v8+9kaUNzyHc7Dj0osF/zU3Sm6tC61FDhjNq9veOLPBnmZyM6hqNsKKJ6bz58dJWedWFzRB7ZNe0lIAH7dhv1tyJNV4Er6/hDS8D26g98/QG/uuQBjE5ZVWZIYFCUqMYo1pynihwk72lffmgoS8+FP+N1fwmb/fpzP1mKajJLjtj6xJ9TcMlGuARvOgtUWopuQ5ArMS1E5cBQAQDxUQQo0PDQot93CGVZ0FcWBPQxxYHqw9Pswv89QSXvikyS2io+j2kAQOM31CHsyNoxSz0qfLpg2EMOpGxIyAGCRAfW0DlEdi+dlo6GHw4rvj6TrreqFclisMrCnZrdL7gBsPMlzcP75DULa+RkbVCbXfVfmh9b3IJVNVhwlYTBD5HiHh9wp3OU6V8X9SpD52RCXSvgFDZqy62Y5TCe2rm0yV6AxvZ9FD4sJdWpisdCF"));
//        System.out.println(duokuaes.a("{\"os\":\"android4.1.2\",\"tag\":\"4\",\"gameversion\":\"1.4.0\",\"imei\":\"867747012591218\",\"appkey\":\"de2492517d570e827a8b959b17d35aac\",\"bdbdstoken\":\"\",\"appid\":\"160\",\"udid\":\"DF1B11F419EA2F3CCCC1677540325788\",\"sessionid\":\"\",\"password\":\"12345ddd\",\"bdtime\":\"\",\"version\":\"1.1.0\",\"username\":\"dlink162\",\"connecttype\":\"wifi\",\"verifycode\":\"\",\"ua\":\"MI 1S\",\"logintype\":1,\"app_secret\":\"5e3a286ed28430c05001421099530f5a\",\"channel\":\"700\",\"bdvcodestring\":\"\"}"));
//        String duos2 = duokuaes.a("{\"os\":\"android4.1.2\",\"tag\":\"4\",\"gameversion\":\"1.4.0\",\"imei\":\"867747012591218\",\"appkey\":\"de2492517d570e827a8b959b17d35aac\",\"bdbdstoken\":\"\",\"appid\":\"160\",\"udid\":\"DF1B11F419EA2F3CCCC1677540325788\",\"sessionid\":\"\",\"password\":\"12345ddd\",\"bdtime\":\"\",\"version\":\"1.1.0\",\"username\":\"dlink162\",\"connecttype\":\"wifi\",\"verifycode\":\"\",\"ua\":\"MI 1S\",\"logintype\":1,\"app_secret\":\"5e3a286ed28430c05001421099530f5a\",\"channel\":\"700\",\"bdvcodestring\":\"\"}");
//        String duoss1 = duokuaes.b(duos2);
//        System.out.println("duos2:"+duos2);
//        System.out.println("duoss1:"+duoss1);
        String Thieves = "{\"status\":1,\"data\":{\"Bonus\":[\"Exp_1710\",\"Coins_1790\"],\"UserLevel\":\"64\",\"Exp\":18395220,\"PrevExp\":\"17968000\",\"NextExp\":\"19265600\",\"ThievesInfo\":{\"Uid\":630786,\"NickName\":\"hui165\",\"Avatar\":\"8113\",\"Sex\":\"0\",\"ThievesId\":6,\"Time\":1395666666,\"Status\":0,\"Attackers\":[],\"Awards\":[],\"HPCount\":2255,\"HPCurrent\":2255,\"Type\":1,\"UserThievesId\":3608438,\"Round\":0,\"FleeTime\":7200},\"Countdown\":-1395666666},\"version\":{\"http\":\"201302294\",\"stop\":\"\",\"appversion\":\"version_1\",\"appurl\":\"ios://xxx\",\"encrypt\":0}}";
        JSONObject jj = JSONObject.fromObject(Thieves.toLowerCase());
        ExploreInfo einfo = (ExploreInfo)JSONObject.toBean(jj, ExploreInfo.class);
        System.out.println(einfo.getData().getThievesinfo());
//        String jj_data = jj.getString("data");
//        JSONObject json_data = JSONObject.fromObject(jj_data);
//        String jj_data_thieves = json_data.getString("ThievesInfo");
//        System.out.println(jj_data_thieves);

        String Thieves1 = "{\"status\":1,\"data\":{\"Bonus\":[\"Exp_1710\",\"Coins_2020\"],\"UserLevel\":\"64\",\"Exp\":18396930,\"PrevExp\":\"17968000\",\"NextExp\":\"19265600\"},\"version\":{\"http\":\"201302294\",\"stop\":\"\",\"appversion\":\"version_1\",\"appurl\":\"ios://xxx\",\"encrypt\":0}}";
        JSONObject jj1 = JSONObject.fromObject(Thieves1.toLowerCase());
        ExploreInfo einfo1 = (ExploreInfo)JSONObject.toBean(jj1, ExploreInfo.class);
        System.out.println(einfo1.getData().getThievesinfo());
//        String jj_data1 = jj1.getString("data");
//        JSONObject json_data1 = JSONObject.fromObject(jj_data1);
//        String jj_data_thieves1 = json_data1.getString("ThievesInfo");
//        System.out.println(jj_data_thieves1);
	}
}
