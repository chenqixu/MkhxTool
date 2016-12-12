package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

//将一个字符串按照zip方式压缩和解压缩
public class ZipUtil2 {
	//压缩  
	public static String compress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		gzip.write(str.getBytes());
		gzip.close();
		return out.toString("ISO-8859-1");
	}

	//解压缩  
	public static String uncompress(String str) throws IOException {
		if (str == null || str.length() == 0) {
			return str;
		}
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		//toString()使用平台默认编码，也可以显式的指定如toString(&quot;GBK&quot;)  
		return out.toString();
	}

	public static byte[] b(byte[] paramArrayOfByte) {
		try {
			ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
			GZIPInputStream localGZIPInputStream = new GZIPInputStream(localByteArrayInputStream);
			byte[] arrayOfByte1 = new byte[1024];
			int i = 0;
			byte[] arrayOfByte2;
			Object localObject = new byte[0];
			for (; (i = localGZIPInputStream.read(arrayOfByte1)) != -1; localObject = arrayOfByte2) {
				arrayOfByte2 = new byte[localObject.toString().length() + i];
				System.arraycopy(localObject, 0, arrayOfByte2, 0, localObject.toString().length());
				System.arraycopy(arrayOfByte1, 0, arrayOfByte2, localObject.toString().length(), i);
			}
			localGZIPInputStream.close();
			localByteArrayInputStream.close();
			return (byte[]) localObject;
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return null;
	}

	//测试方法  
	public static void main(String[] args) throws IOException {

		//测试字符串  
		//		String str = "{'status':1,'data':{'new':false,'current':{'GS_ID':4,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.8','GS_CHAT_PORT':8000,'GS_NAME':'\u91d1\u5c5e\u5de8\u9f99','GS_IP':'http://s4.mysticalcard.com/'},'list':[{'GS_ID':1,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.5','GS_CHAT_PORT':8000,'GS_NAME':'\u65f6\u7a7a\u65c5\u8005','GS_IP':'http://s1.mysticalcard.com/'},{'GS_ID':2,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.6','GS_CHAT_PORT':8000,'GS_NAME':'\u7cbe\u7075\u796d\u53f8','GS_IP':'http://s2.mysticalcard.com/'},{'GS_ID':3,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.7','GS_CHAT_PORT':8000,'GS_NAME':'\u68ee\u6797\u5973\u795e','GS_IP':'http://s3.mysticalcard.com/'},{'GS_ID':4,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.8','GS_CHAT_PORT':8000,'GS_NAME':'\u91d1\u5c5e\u5de8\u9f99','GS_IP':'http://s4.mysticalcard.com/'},{'GS_ID':5,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.9','GS_CHAT_PORT':8000,'GS_NAME':'\u8363\u8000\u5de8\u4eba','GS_IP':'http://s5.mysticalcard.com/'},{'GS_ID':6,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.75','GS_CHAT_PORT':8000,'GS_NAME':'\u964d\u4e34\u5929\u4f7f','GS_IP':'http://s6.mysticalcard.com/'},{'GS_ID':7,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.10','GS_CHAT_PORT':8000,'GS_NAME':'\u5149\u660e\u4e4b\u9f99','GS_IP':'http://s7.mysticalcard.com/'},{'GS_ID':8,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.11','GS_CHAT_PORT':8000,'GS_NAME':'\u8840\u796d\u6076\u9b54 ','GS_IP':'http://s8.mysticalcard.com/'},{'GS_ID':9,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.72','GS_CHAT_PORT':8000,'GS_NAME':'\u5fb7\u739b\u897f\u4e9a','GS_IP':'http://s9.mysticalcard.com/'},{'GS_ID':10,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.74','GS_CHAT_PORT':8000,'GS_NAME':'\u90aa\u7075\u5973\u5deb','GS_IP':'http://s10.mysticalcard.com/'},{'GS_ID':11,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.76','GS_CHAT_PORT':8000,'GS_NAME':'\u80cc\u4e3b\u4e4b\u5f71','GS_IP':'http://s11.mysticalcard.com/'},{'GS_ID':12,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.77','GS_CHAT_PORT':8000,'GS_NAME':'\u6708\u4eae\u5973\u795e','GS_IP':'http://s12.mysticalcard.com/'},{'GS_ID':13,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.3','GS_CHAT_PORT':8000,'GS_NAME':'\u4e16\u754c\u6811\u4e4b\u7075','GS_IP':'http://s13.mysticalcard.com/'},{'GS_ID':14,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.73','GS_CHAT_PORT':8000,'GS_NAME':'\u5e7d\u7075\u5de8\u9f99','GS_IP':'http://s14.mysticalcard.com/'},{'GS_ID':15,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.96','GS_CHAT_PORT':8000,'GS_NAME':'\u5200\u950b\u5973\u738b','GS_IP':'http://s15.mysticalcard.com/'},{'GS_ID':16,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.97','GS_CHAT_PORT':8000,'GS_NAME':'\u718a\u4eba\u6b66\u58eb','GS_IP':'http://s16.mysticalcard.com/'},{'GS_ID':17,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.99','GS_CHAT_PORT':8000,'GS_NAME':'\u6050\u60e7\u4e4b\u738b','GS_IP':'http://s17.mysticalcard.com/'},{'GS_ID':18,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.100','GS_CHAT_PORT':8000,'GS_NAME':'\u8fdc\u53e4\u874e\u7687','GS_IP':'http://s18.mysticalcard.com/'},{'GS_ID':19,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.101','GS_CHAT_PORT':8000,'GS_NAME':'\u9690\u4e16\u5148\u77e5','GS_IP':'http://s19.mysticalcard.com/'},{'GS_ID':20,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.102','GS_CHAT_PORT':8000,'GS_NAME':'\u4e5d\u5934\u5996\u86c7','GS_IP':'http://s20.mysticalcard.com/'},{'GS_ID':21,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.98','GS_CHAT_PORT':8000,'GS_NAME':'\u5143\u7d20\u7075\u9f99','GS_IP':'http://s21.mysticalcard.com/'},{'GS_ID':22,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.103','GS_CHAT_PORT':8000,'GS_NAME':'\u7eaf\u6d01\u5723\u5973','GS_IP':'http://s22.mysticalcard.com/'},{'GS_ID':23,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.104','GS_CHAT_PORT':8000,'GS_NAME':'\u6bc1\u706d\u4e4b\u9f99','GS_IP':'http://s23.mysticalcard.com/'},{'GS_ID':24,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.105','GS_CHAT_PORT':8000,'GS_NAME':'\u5deb\u5996\u9886\u4e3b','GS_IP':'http://s24.mysticalcard.com/'},{'GS_ID':25,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.106','GS_CHAT_PORT':8000,'GS_NAME':'\u5815\u843d\u5929\u4f7f','GS_IP':'http://s25.mysticalcard.com/'},{'GS_ID':26,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.122','GS_CHAT_PORT':8000,'GS_NAME':'\u7fbd\u7ffc\u5316\u86c7','GS_IP':'http://s26.mysticalcard.com/'},{'GS_ID':27,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.71','GS_CHAT_PORT':8000,'GS_NAME':'\u590d\u6d3b\u5154\u5973','GS_IP':'http://s27.mysticalcard.com/'},{'GS_ID':28,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.78','GS_CHAT_PORT':8000,'GS_NAME':'\u731b\u72b8\u4e4b\u738b','GS_IP':'http://s28.mysticalcard.com/'},{'GS_ID':29,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.107','GS_CHAT_PORT':8000,'GS_NAME':'\u8ff7\u9b45\u7075\u72d0','GS_IP':'http://s29.mysticalcard.com/'},{'GS_ID':30,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.108','GS_CHAT_PORT':8000,'GS_NAME':'\u72ec\u773c\u5de8\u4eba','GS_IP':'http://s30.mysticalcard.com/'},{'GS_ID':31,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.136','GS_CHAT_PORT':8000,'GS_NAME':'\u6df7\u6c8c\u4e4b\u9f99','GS_IP':'http://s31.mysticalcard.com/'},{'GS_ID':32,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.137','GS_CHAT_PORT':8000,'GS_NAME':'\u5723\u5802\u6b66\u58eb','GS_IP':'http://s32.mysticalcard.com/'},{'GS_ID':33,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.138','GS_CHAT_PORT':8000,'GS_NAME':'\u8fdc\u53e4\u6d77\u5996','GS_IP':'http://s33.mysticalcard.com/'},{'GS_ID':34,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.139','GS_CHAT_PORT':8000,'GS_NAME':'\u67aa\u70ae\u73ab\u7470','GS_IP':'http://s34.mysticalcard.com/'},{'GS_ID':35,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.159','GS_CHAT_PORT':8000,'GS_NAME':'\u65f6\u5149\u5973\u795e','GS_IP':'http://s35.mysticalcard.com/'},{'GS_ID':36,'GS_STATUS':1,'GS_CHAT_IP':'218.245.7.160','GS_CHAT_PORT':8000,'GS_NAME':'\u7cbe\u7075\u5973\u738b','GS_IP':'http://s36.mysticalcard.com/'},{'GS_ID':37,'GS_STATUS':2,'GS_CHAT_IP':'218.245.7.161','GS_CHAT_PORT':8000,'GS_NAME':'\u5723\u8bde\u8001\u4eba','GS_IP':'http://s37.mysticalcard.com/'}],'uinfo':{'access_token':'301174488df33225ed6167b8cdbdfd9e5d7b73f602b97d3d6','uin':'301174488','nick':'dlink165','MUid':'895174','time':1389890296,'sign':'c530b57f53f56eb4431443fad8b13410','ppsign':'fba54250934ceaf5afd682090fd203a7'}}}";
		//
		//		System.out.println("原长度：" + str.length());
		//
		//		System.out.println("压缩后：" + ZipUtil2.compress(str));
		//		
		//		System.out.println("压缩后长度：" + ZipUtil2.compress(str).length());
		//
		//		System.out.println("解压缩：" + ZipUtil2.uncompress(ZipUtil2.compress(str)));

		byte bytes[] = { 0, 0, 0, 1, 0, 0, 0, 4, 98, 97, 99, 52, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 123, 34, 82, 101,
				115, 117, 108, 116, 34, 58, 34, -25, -67, -111, -25, -69, -100,
				-24, -81, -73, -26, -79, -126, -23, -108, -103, -24, -81, -81,
				-17, -68, -116, -24, -81, -73, -25, -88, -115, -27, -112, -114,
				-23, -121, -115, -24, -81, -107, 34, 125 };
		System.out.println(new String(bytes, "utf-8"));
//		ZipUtil2.uncompress(new String(bytes, "utf-8"));
//		ZipUtil2.b(bytes);
	}
}
