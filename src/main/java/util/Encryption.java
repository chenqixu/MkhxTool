package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
	private IvParameterSpec a;

	private SecretKeySpec b;

	private Cipher c;

	public Encryption(String paramString1, String paramString2) {
		this.a = new IvParameterSpec(paramString2.getBytes());
		this.b = new SecretKeySpec(paramString1.getBytes(), "AES");
		try {
			this.c = Cipher.getInstance("AES/CBC/NoPadding");
//			this.c = Cipher.getInstance("DESede/CBC/PKCS7Padding");
//			this.c = Cipher.getInstance("AES");
			return;
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
			return;
		} catch (NoSuchPaddingException localNoSuchPaddingException) {
			localNoSuchPaddingException.printStackTrace();
		}
	}

	private static String a(String paramString) {
		int i = 16 - paramString.length() % 16;
		int j = 0;
		String str = paramString;
		while (true) {
			if (j >= i)
				return str;
			str = str + ' ';
			j++;
		}
	}

	public static byte[] hexToBytes(String paramString) {
		if (paramString == null)
			return null;
		if (paramString.length() < 2)
			return null;
		int i = paramString.length() / 2;
		byte[] arrayOfByte = new byte[i];
		for (int j = 0;; j++) {
			if (j >= i)
				return arrayOfByte;
			arrayOfByte[j] = ((byte) Integer.parseInt(paramString.substring(
					j * 2, 2 + j * 2), 16));
		}
	}

	public String bytesToHex(byte[] paramArrayOfByte) {
		if (paramArrayOfByte == null)
			return null;
		int i = paramArrayOfByte.length;
		String str = "";
		int j = 0;
		if (j >= i)
			return str;
		if ((0xFF & paramArrayOfByte[j]) < 16)
			;
		for (str = str + "0" + Integer.toHexString(0xFF & paramArrayOfByte[j]);; str = str
				+ Integer.toHexString(0xFF & paramArrayOfByte[j])) {
			j++;
			break;
		}
		return str;
	}

	public byte[] decrypt(String paramString) throws Exception {
		if ((paramString == null) || (paramString.length() == 0))
			throw new Exception("Empty string");
		try {
			this.c.init(2, this.b, this.a);
			byte[] arrayOfByte = this.c.doFinal(hexToBytes(paramString));
			return arrayOfByte;
		} catch (Exception localException) {
			throw new Exception("[decrypt] " + localException.getMessage());
		}
	}

	public byte[] encrypt(String paramString) throws Exception {
		if ((paramString == null) || (paramString.length() == 0))
			throw new Exception("Empty string");
		try {
			this.c.init(1, this.b, this.a);
			byte[] arrayOfByte = this.c.doFinal(a(paramString).getBytes());
			return arrayOfByte;
		} catch (Exception localException) {
			throw new Exception("[encrypt] " + localException.getMessage());
		}
	}

	public String md5(String paramString) {
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			String str = bytesToHex(localMessageDigest.digest());
			return str;
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
		return "";
	}

	public String sha1(String paramString) {
		try {
			MessageDigest localMessageDigest = MessageDigest
					.getInstance("SHA-1");
			localMessageDigest.update(paramString.getBytes());
			String str = bytesToHex(localMessageDigest.digest());
			return str;
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
		return "";
	}

	public String sha256(String paramString) {
		try {
			MessageDigest localMessageDigest = MessageDigest
					.getInstance("SHA-256");
			localMessageDigest.update(paramString.getBytes());
			String str = bytesToHex(localMessageDigest.digest());
			return str;
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
		return "";
	}
}
