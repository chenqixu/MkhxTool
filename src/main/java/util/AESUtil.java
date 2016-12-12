package util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
	
//	private final static String c = "AKlMU89D3FchIkhK";
//	private final static String cb = "DESede/CBC/PKCS7Padding";
//	private final static String keys = "DESede/CBC/PKCS7Padding";//"AES";
	private final static String keys = "AES";
	

	/**
	 * 加密
	 */
	public static byte[] encrypt(String content, String password) {

		try {

			KeyGenerator kgen = KeyGenerator.getInstance(keys);
//
			kgen.init(128, new SecureRandom(password.getBytes()));

			SecretKey secretKey = kgen.generateKey();

			byte[] enCodeFormat = secretKey.getEncoded();

			SecretKeySpec key = new SecretKeySpec(enCodeFormat, keys);
			
			Cipher cipher = Cipher.getInstance(keys);// 创建密码器

			byte[] byteContent = content.getBytes("utf-8");

			cipher.init(Cipher.ENCRYPT_MODE, key);// 初始化

			byte[] result = cipher.doFinal(byteContent);

			return result; // 加密

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		} catch (NoSuchPaddingException e) {

			e.printStackTrace();

		} catch (InvalidKeyException e) {

			e.printStackTrace();

		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();

		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();

		} catch (BadPaddingException e) {

			e.printStackTrace();

		}

		return null;

	}

	/**
	 * 解密
	 *
	 * @param content
	 * @param password
	 * @return
	 */
	public static byte[] decrypt(byte[] content, String password) {

		try {

			KeyGenerator kgen = KeyGenerator.getInstance(keys);

			kgen.init(128, new SecureRandom(password.getBytes()));

			SecretKey secretKey = kgen.generateKey();

			byte[] enCodeFormat = secretKey.getEncoded();

			SecretKeySpec key = new SecretKeySpec(enCodeFormat, keys);

			Cipher cipher = Cipher.getInstance(keys);// 创建密码器

			cipher.init(Cipher.DECRYPT_MODE, key);// 初始化

			byte[] result = cipher.doFinal(content);

			return result; // 解密

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();

		} catch (NoSuchPaddingException e) {

			e.printStackTrace();

		} catch (InvalidKeyException e) {

			e.printStackTrace();

		} catch (IllegalBlockSizeException e) {

			e.printStackTrace();

		} catch (BadPaddingException e) {

			e.printStackTrace();

		}
		return null;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String a = "crerf7kdrgkvkeNqrVsqtKk5XslJKybcNSi1OLVErszWyMDJQK8goKLB19HMJ8vd0iXcJ9fcOBQnl2EZ5xDv7Keko%2BSYWBJckpqd6pihZWego5SXnAQ0xNDBQqgUATtAZPw%3D%3D";
		String b = "";
		try {
			System.out.println(java.net.URLDecoder.decode(a, "utf-8"));
			b = java.net.URLDecoder.decode(a, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//需要加密的内容
//		String content = "加密的内容";
//		//加密密码
//		String password = "123456";
//		List p = Arrays.asList(new String[] { "ir", "d", "db", "dm", "ma", "ov", "cc", "l", "an", "pn", "av", "dc", "ad", "android_id_md5", "android_id_sha1", "android_id_sha256", "r", "c", "id", "ua", "tpid", "ar", "ti", "age", "gender", "latitude", "longitude", "altitude", "connection_type", "mobile_country_code", "mobile_network_code", "screen_density", "screen_layout_size", "android_purchase_status", "event_referrer", "app_ad_tracking" });

		//验证用密钥  
//	    byte[] keyx = "000000000000000000000000".getBytes();
	    //private byte[] keyx = Hex.decode("00000000");
//	    byte[] ivs = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7 };
//	    String ivs = "1234567812345678";
	    String hexDigits = 		
//	    	"0123456789ABCDEF";
//	    	"0123456789abcdef";
	    	"d87f8ba0d8681579053cebde3a53d56c";
		
//		byte[] decryptContent = decrypt(b.getBytes(), password);
		try {
//			System.out.println(new String(decryptContent, "utf-8"));			
			Encryption n = new Encryption(hexDigits.toString(), "heF9BATUfWuISyO8");
			Object localObject5 = new StringBuilder(b);
//			System.out.println(n.sha1(b));
//			System.out.println(n.sha256(b));
//			System.out.println(n.decrypt(b));
//			System.out.println(n.encrypt(((StringBuilder)localObject5).toString()));
			byte[] result = n.encrypt(((StringBuilder)localObject5).toString());
//			for(int i=0;i<result.length;i++){
//				System.out.println(result[i]);
//			}
			System.out.println("gbk:" + new String(result, "gbk"));
			System.out.println("utf-8:" + new String(result, "utf-8"));
			System.out.println("ISO-8859-1:" + new String(result, "ISO-8859-1"));
//            StringBuilder localStringBuilder5 = new StringBuilder(n.bytesToHex(n.encrypt(((StringBuilder)localObject5).toString())));
//            System.out.println(localStringBuilder5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
