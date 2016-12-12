package util.nd;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class tf {
	private KeySpec a;

	private String b = "DESede/CBC/PKCS7Padding";

	private SecretKey c;

	private SecretKeyFactory d;

	private String e = "utf-8";

	public tf() {
		try {
			KeyGenerator localKeyGenerator = KeyGenerator.getInstance("DESede");
			this.c = localKeyGenerator.generateKey();
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
	}

	public tf(byte[] paramArrayOfByte) {
		d(paramArrayOfByte);
	}

	public tf(String paramString) {
		try {
			byte[] arrayOfByte = paramString.getBytes(this.e);
			d(arrayOfByte);
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			localUnsupportedEncodingException.printStackTrace();
		}
	}

	private void d(byte[] paramArrayOfByte) {
		try {
			this.d = SecretKeyFactory.getInstance("DESede");
			this.a = new DESedeKeySpec(e(paramArrayOfByte));
			this.c = this.d.generateSecret(this.a);
		} catch (InvalidKeyException localInvalidKeyException) {
			localInvalidKeyException.printStackTrace();
		} catch (InvalidKeySpecException localInvalidKeySpecException) {
			localInvalidKeySpecException.printStackTrace();
		} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
			localNoSuchAlgorithmException.printStackTrace();
		}
	}

	private byte[] e(byte[] paramArrayOfByte) {
		int i = paramArrayOfByte.length;
		if (paramArrayOfByte.length < 24) {
			byte[] arrayOfByte1 = new byte[24];
			byte[] arrayOfByte2 = new byte[24 - i];
			for (int j = 0; j < 24 - i; j++)
				arrayOfByte2[j] = 0;
			System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, 0, i);
			System.arraycopy(arrayOfByte2, 0, arrayOfByte1, i, 24 - i);
			paramArrayOfByte = arrayOfByte1;
		}
		return paramArrayOfByte;
	}

	public byte[] a(byte[] paramArrayOfByte) {
		IvParameterSpec localIvParameterSpec = f(this.c.getEncoded());
		try{
			Cipher localCipher = Cipher.getInstance(this.b);
			localCipher.init(1, this.c, localIvParameterSpec);
			return localCipher.doFinal(paramArrayOfByte);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public byte[] a(String paramString) {
		byte[] arrayOfByte = null;
		try {
			arrayOfByte = paramString.getBytes(this.e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return a(arrayOfByte);
	}

	private static IvParameterSpec f(byte[] paramArrayOfByte) {
		byte[] arrayOfByte = new byte[8];
		System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, 8);
		IvParameterSpec localIvParameterSpec = new IvParameterSpec(arrayOfByte);
		return localIvParameterSpec;
	}

	public byte[] b(byte[] paramArrayOfByte) {
		IvParameterSpec localIvParameterSpec = f(this.c.getEncoded());
		try{		
			Cipher localCipher = Cipher.getInstance(this.b);// ´´½¨ÃÜÂëÆ÷
			localCipher.init(2, this.c, localIvParameterSpec);
			return localCipher.doFinal(paramArrayOfByte);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public String c(byte[] paramArrayOfByte) {
		byte[] arrayOfByte = b(paramArrayOfByte);
		int i = 30;
		try {
			return new String(arrayOfByte, this.e);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public byte[] a() {
		return this.c.getEncoded();
	}

	public String b() {
		return this.e;
	}

	public void b(String paramString) {
		this.e = paramString;
	}
}