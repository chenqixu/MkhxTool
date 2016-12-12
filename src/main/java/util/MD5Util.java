package util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {
	public final static String MD5(String s) {
		char hexDigits[] = 		
//		{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
//				'A', 'B', 'C', 'D', 'E', 'F' };
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			byte[] btInput = s.getBytes();
			// ���MD5ժҪ�㷨�� MessageDigest ����
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽڸ���ժҪ
			mdInst.update(btInput);
			// �������
			byte[] md = mdInst.digest();
			// ������ת����ʮ�����Ƶ��ַ�����ʽ
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public final static String MD5(byte[] paramArrayOfByte) {
		char hexDigits[] = 		
		{ '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			// ���MD5ժҪ�㷨�� MessageDigest ����
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽڸ���ժҪ
			mdInst.update(paramArrayOfByte, 0,paramArrayOfByte.length);
			// �������
			byte[] md = mdInst.digest();
			// ������ת����ʮ�����Ƶ��ַ�����ʽ
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/*** 
	 * MD5���� ����32λmd5�� 
	 */
	public static String string2MD5(String inStr) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			System.out.println(e.toString());
			e.printStackTrace();
			return "";
		}
		char[] charArray = inStr.toCharArray();
		byte[] byteArray = new byte[charArray.length];

		for (int i = 0; i < charArray.length; i++)
			byteArray[i] = (byte) charArray[i];
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = ((int) md5Bytes[i]) & 0xff;
			if (val < 16)
				hexValue.append("0");
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

	/** 
	 * ���ܽ����㷨 ִ��һ�μ��ܣ����ν��� 
	 */
	public static String convertMD5(String inStr) {
		char[] a = inStr.toCharArray();
		for (int i = 0; i < a.length; i++) {
			a[i] = (char) (a[i] ^ 't');
		}
		String s = new String(a);
		return s;
	}

	private static char[] a = { '0', '1', '2', '3', '4', '5', '6', '7', '8',
			'9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static byte[] a(String paramString) {
		if (paramString.length() % 2 != 0)
			throw new IllegalArgumentException();
		char[] arrayOfChar = paramString.toCharArray();
		byte[] arrayOfByte = new byte[paramString.length() / 2];
		int i = 0;
		int j = 0;
		int k = paramString.length();
		while (i < k) {
			String str = String.valueOf(arrayOfChar[(i++)]) + arrayOfChar[i];
			int m = Integer.parseInt(str, 16) & 0xFF;
			arrayOfByte[j] = new Integer(m).byteValue();
			i++;
			j++;
		}
		return arrayOfByte;
	}

	public static String a(byte[] paramArrayOfByte) {
		StringBuilder localStringBuilder = new StringBuilder(
				paramArrayOfByte.length * 2);
		for (int i = 0; i < paramArrayOfByte.length; i++) {
			localStringBuilder.append(a[((paramArrayOfByte[i] & 0xF0) >>> 4)]);
			localStringBuilder.append(a[(paramArrayOfByte[i] & 0xF)]);
		}
		return localStringBuilder.toString();
	}
	
	public static byte[] gbk2utf8(String chenese) {
        char c[] = chenese.toCharArray();
        byte[] fullByte = new byte[3 * c.length];
        for (int i = 0; i < c.length; i++) {
            int m = (int) c[i];
            String word = Integer.toBinaryString(m);
 
            StringBuffer sb = new StringBuffer();
            int len = 16 - word.length();
            for (int j = 0; j < len; j++) {
                sb.append("0");
            }
            sb.append(word);
            sb.insert(0, "1110");
            sb.insert(8, "10");
            sb.insert(16, "10");
 
            String s1 = sb.substring(0, 8);
            String s2 = sb.substring(8, 16);
            String s3 = sb.substring(16);
 
            byte b0 = Integer.valueOf(s1, 2).byteValue();
            byte b1 = Integer.valueOf(s2, 2).byteValue();
            byte b2 = Integer.valueOf(s3, 2).byteValue();
            byte[] bf = new byte[3];
            bf[0] = b0;
            fullByte[i * 3] = bf[0];
            bf[1] = b1;
            fullByte[i * 3 + 1] = bf[1];
            bf[2] = b2;
            fullByte[i * 3 + 2] = bf[2];
 
        }
        return fullByte;
    }
	
	public static String getUTF8StringFromGBKString(String gbkStr) {
		try {
			return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new InternalError();
		}
	}
	
	public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
		int n = gbkStr.length();
		byte[] utfBytes = new byte[3 * n];
		int k = 0;
		for (int i = 0; i < n; i++) {
			int m = gbkStr.charAt(i);
			if (m < 128 && m >= 0) {
				utfBytes[k++] = (byte) m;
				continue;
			}
			utfBytes[k++] = (byte) (0xe0 | (m >> 12));
			utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
			utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
		}
		if (k < utfBytes.length) {
			byte[] tmp = new byte[k];
			System.arraycopy(utfBytes, 0, tmp, 0, k);
			return tmp;
		}
		return utfBytes;
	}

	public static void main(String[] args) {		

		String s = "{\"Result\":\"��������������Ժ�����\"}";
//		System.out.println(MD5Util.MD5(s));
		System.out.println(MD5Util.MD5(getUTF8BytesFromGBKString(s)));
		String s1= "{\"IMEI\":\"86aj77ug47do01he25jk91kw21sg8\",\"Sign\":\"805cd89de48e952bb01ed650f2a97194e65dd9b656d188bc87a533150f1386f23aa8f51caf3fd31757f9c9000fcd04914f0a6cd64cfa324201705e4599267034\",\"PlatformVersion\":\"3.2.6.1\",\"IMSI\":\"46am00xu09gr30dv53lx88ua60mo9\",\"PubKey\":\"B38B9F5D42DEF0BDEF067D3009B1E92475E130399C9DC7CC31F0361D6581D0245CB3AE5664D9337D9370C5CC842D9362F4F51A259DDF928080457A40E682A2BB\",\"ScreenHeight\":\"480\",\"SignAppKey\":\"466aca49415ff6ff09eb32ede6dde2001ac0b21f4b7deea2371f2f1c909c2e3eaa38493b2cf3d4d1fce1b55a0e1f01e2ba606b2bb140f1e3\",\"PhoneType\":\"MI 1S\",\"Platform\":\"8\",\"ScreenWidth\":\"854\"}";
		System.out.println(MD5Util.MD5(getUTF8BytesFromGBKString(s1)));
		byte[] a = {0, 0, 0, 1, 0, 0, 0, 4, 55, 55, 102, 99, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		byte[] b = getUTF8BytesFromGBKString(s1);
		
		byte[] data3 = new byte[a.length+b.length];
		System.arraycopy(a,0,data3,0,a.length);
		System.arraycopy(b,0,data3,a.length,b.length);
		System.out.println(data3.length);
		
		//		System.out.println(MD5Util.MD5("����"));
//		String plainText = "3d0b6577fa756753d10f8193bacb0333";//asdf";
//		try {
//			MessageDigest md = MessageDigest.getInstance("MD5");
//			md.update(plainText.getBytes());
//			byte b[] = md.digest();
//			System.out.println(a(b));
//			System.out.println(a("ca421ec953296e7a8e66b9a28e226e64"));
//			int i;
//			StringBuffer buf = new StringBuffer("");
//			for (int offset = 0; offset < b.length; offset++) {
//				i = b[offset];
//				if (i < 0)
//					i += 256;
//				if (i < 16)
//					buf.append("0");
//				buf.append(Integer.toHexString(i));
//			}
//			System.out.println("result: " + buf.toString());// 32λ�ļ���
//			System.out.println("result: " + buf.toString().substring(8, 24));// 16λ�ļ���
//		} catch (NoSuchAlgorithmException e) {
//			e.printStackTrace();
//		}
		//		String s = new String("tangfuqiang");
		//		System.out.println("ԭʼ��" + s);
		//		System.out.println("MD5��" + string2MD5(s));
		//		System.out.println("���ܵģ�" + convertMD5(s));
		//		System.out.println("���ܵģ�" + convertMD5(convertMD5(s)));
	}
}
