package main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import util.DeflateUtil;
import util.HttpConn;
import util.StringUtil;
import util.ZipUtil2;

public class Main360 {
	public static void main(String[] args) throws Exception {
		Properties prop = System.getProperties();
		//����http����Ҫʹ�õĴ���������ĵ�ַ 
		prop.setProperty("http.proxyHost", "127.0.0.1");
		//����http����Ҫʹ�õĴ���������Ķ˿� 
		prop.setProperty("http.proxyPort", "8087");
		//����http����Ҫʹ�õĴ�����������û��� 
		//prop.setProperty("http.proxyUser", "�û���");
		//����http����Ҫʹ�õĴ�������������� 
		//prop.setProperty("http.proxyPassword", "����");
		
		
		
		HttpConn hc = new HttpConn();
		String sUrl = "";//"http://s4.mysticalcard.com/login.php?do=mpLogin";
		sUrl = "http://s4.mysticalcard.com/login.php?do=mpLogin&v=6487&phpp=ANDROID_360&phpl=ZH_CN&pvc=1.4.0&pvb=2013-11-22%2009%3A56%3A41";
		//String sParam = "{'Udid'='C4%3A6A%3AB7%3A86%3ABE%3A51','nick'='dlink169','Origin'='safe360','IDFA'='','time'='1388669696','sign'='c0c8da64e11ef65a8a72a4b61c04c88e','plat'='safe360','uin'='316608296','MUid'='1012222','access_token'='3166082964248ca6ddfbbe925ed6d547c65d0acd9b6583942','ppsign'='0d0f4c3b7275f0bc1e701a539bc64882','newguide'='1','Devicetoken'=''}";
		JSONObject jsonParam = new JSONObject();
		//����
		/*jsonParam.put("Udid", "");//"C4:6A:B7:86:BE:51");//û��
		jsonParam.put("nick", "");//"dlink169");//û��
		jsonParam.put("Origin", "");//""safe360");//û��
		jsonParam.put("IDFA", "");//û��
		jsonParam.put("time", "");//"1388669696");//��
		jsonParam.put("sign", "");//"c0c8da64e11ef65a8a72a4b61c04c88e");//��
		jsonParam.put("plat", "");//"safe360");//û��
		jsonParam.put("uin", "");//"316608296");//û��
		jsonParam.put("MUid", "");//"1012222");//û��
		jsonParam.put("access_token", "");//"3166082964248ca6ddfbbe925ed6d547c65d0acd9b6583942");//��
		jsonParam.put("ppsign", "");//"0d0f4c3b7275f0bc1e701a539bc64882");//��
		jsonParam.put("newguide", "");//"1");//û��
		jsonParam.put("Devicetoken", "");//û��*/
		//ʵ��
		jsonParam.put("Udid", "C4:6A:B7:86:BE:51");
		jsonParam.put("nick", "dlink169");
		jsonParam.put("Origin", "safe360");
		jsonParam.put("IDFA", "");
		jsonParam.put("time", "1388669696");
		jsonParam.put("sign", "2625e656cfa497f1912b7a7a2d2df120");
		jsonParam.put("plat", "safe360");
		jsonParam.put("uin", "316608296");
		jsonParam.put("MUid", "1012222");
		jsonParam.put("access_token", "3166082965f53af6bc60c49b4088054806923a2877cf7616c");
		jsonParam.put("ppsign", "63d41ef3f194513b43dc290996be3f8b");
		jsonParam.put("newguide", "1");
		jsonParam.put("Devicetoken", "");
		XMLSerializer xmlSerializer = new XMLSerializer();
		String xmlparam = xmlSerializer.write(jsonParam);
		System.out.println(xmlparam);
		/*String xmlparams = "<?xml version='1.0' encoding='UTF-8'?><o>";
		xmlparams += "<Udid type='string'>C4%3A6A%3AB7%3A86%3ABE%3A51</Udid>";
		xmlparams += "<nick type='string'>dlink169</nick>";
		xmlparams += "<Origin type='string'>safe360</Origin>";
		xmlparams += "<IDFA type='string'></IDFA>";
		xmlparams += "<time type='string'>1388669696</time>";
		xmlparams += "<sign type='string'>c0c8da64e11ef65a8a72a4b61c04c88e</sign>";
		xmlparams += "<plat type='string'>safe360</plat>";
		xmlparams += "<uin type='string'>316608296</uin>";
		xmlparams += "<MUid type='string'>1012222</MUid>";
		xmlparams += "<access_token type='string'>3166082964248ca6ddfbbe925ed6d547c65d0acd9b6583942</access_token>";
		xmlparams += "<ppsign type='string'>0d0f4c3b7275f0bc1e701a539bc64882</ppsign>";
		xmlparams += "<newguide type='string'>1</newguide>";
		xmlparams += "<Devicetoken type='string'></Devicetoken>";
		xmlparams += "</o>";*/
		String result = hc.doAction("POST", sUrl, xmlparam.getBytes());//xmlparam.getBytes());
		System.out.println(StringUtil.unicodeToString(result));
		//System.out.println(DeflateUtil.uncompress(result));
		
		//System.out.println(new Date(1388986561));
		
		//System.out.println(System.currentTimeMillis());
		//System.out.println(Calendar.getInstance().getTimeInMillis());
		//System.out.println(new Date().getTime());
		
		/*Date date1 = new Date();
        Date date2 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("1970/01/01 08:00:00");
        //Date date3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse("1970/01/01 00:00:00");
        long z = date1.getTime() - date2.getTime();
        //long z1 = date1.getTime() - date3.getTime();
        System.out.println(z);
        System.out.println(z/1000);
        //System.out.println(z1);
        //System.out.println(z1/1000);
        SimpleDateFormat fm2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
        String date = fm2.format(1388986561);
        System.out.println(date);
        date = fm2.format(z/1000);
        System.out.println(date);*/
	}
}
