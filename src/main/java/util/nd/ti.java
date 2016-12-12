package util.nd;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ti {
	public static byte[] a(byte[] paramArrayOfByte) {
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			localMessageDigest.update(paramArrayOfByte, 0,
					paramArrayOfByte.length);
			// 获得密文[没有进行 把密文转换成十六进制的字符串形式 的转换]
			return localMessageDigest.digest();
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
		return null;
	}

	public static String b(byte[] paramArrayOfByte) {
		byte[] arrayOfByte = a(paramArrayOfByte);
		if (arrayOfByte != null)
			return th.a(arrayOfByte);
		return null;
	}
}
