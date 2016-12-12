package util;

import java.io.UnsupportedEncodingException;  
import org.apache.commons.codec.binary.Base64; 

public class Base64Util {
	public static void main(String[] args) {
		String str = "Hello World";
		try {
			byte[] encodeBase64 = Base64.encodeBase64(str.getBytes("UTF-8"));
			byte[] decodeBase64 = Base64.decodeBase64(new String(encodeBase64).getBytes());
			System.out.println("encode RESULT: " + new String(encodeBase64));
			System.out.println("decode RESULT: " + new String(decodeBase64));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
