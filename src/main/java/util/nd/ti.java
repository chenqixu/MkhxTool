package util.nd;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ti {
	public static byte[] a(byte[] paramArrayOfByte) {
		try {
			// ���MD5ժҪ�㷨�� MessageDigest ����
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			// ʹ��ָ�����ֽڸ���ժҪ
			localMessageDigest.update(paramArrayOfByte, 0,
					paramArrayOfByte.length);
			// �������[û�н��� ������ת����ʮ�����Ƶ��ַ�����ʽ ��ת��]
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
