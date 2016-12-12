package util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

public class DESCode {
	private String algorithm = "DESede/CBC/PKCS7Padding";//加密方法／运算模式／填充模式

	private String charset = "UTF-8";//编码

	private Cipher encCipher;//加密cipher

	private Cipher decCipher;//解密cipher

	private String encStrKey = null;//密钥

	private String encSourceSpliter = "$";

	private String hashSourceSpliter = "_";

	private MessageDigest digest;//计算摘要对象

	private KeyGenerator keyGene;

	public String getKey() {
		return this.keyGene.getKey();
	}

	public DESCode(KeyGenerator keyGene) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, DecoderException {
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());//添加jce支持(sun有其默认实现)

		this.keyGene = keyGene;

		digest = MessageDigest.getInstance("MD5");
		this.encStrKey = keyGene.getEncKeyStr();
		init();
	}

	private void init() throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, DecoderException {
		encCipherInit();
		decCipherInit();
	}

	private void encCipherInit() throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, DecoderException {
		DESedeKeySpec encKey = new DESedeKeySpec(Hex.decodeHex(this.encStrKey.toCharArray()));
		SecretKey secrekey = new SecretKeySpec(encKey.getKey(), algorithm);
		this.encCipher = Cipher.getInstance(this.algorithm);
		IvParameterSpec iv = new IvParameterSpec(new byte[] { 1, 2, 3, 4, 5, 6,
				7, 8 });//iv向量(加密解密双方要一致)
		encCipher.init(Cipher.ENCRYPT_MODE, secrekey, iv);//初始化cipher对象
	}

	private void decCipherInit() throws InvalidKeyException, DecoderException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidAlgorithmParameterException {
		DESedeKeySpec encKey = new DESedeKeySpec(Hex.decodeHex(this.encStrKey
				.toCharArray()));
		SecretKey secrekey = new SecretKeySpec(encKey.getKey(), algorithm);
		this.decCipher = Cipher.getInstance(this.algorithm);
		IvParameterSpec iv = new IvParameterSpec(new byte[] { 1, 2, 3, 4, 5, 6,
				7, 8 });
		decCipher.init(Cipher.DECRYPT_MODE, secrekey, iv);//初始化cipher对象

	}

	public String encrypt(String sourceMsg)
			//加密数据
			throws UnsupportedEncodingException, IllegalBlockSizeException,
			BadPaddingException, InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			DecoderException {
		if (!this.encStrKey.equals(this.keyGene.getEncKeyStr())) {
			this.encStrKey = keyGene.getEncKeyStr();
			init();
		}
		byte[] msg = sourceMsg.getBytes(this.charset);
		byte[] value = this.encCipher.doFinal(msg);
		return new String(Base64.encodeBase64(value));

	}

	public String decrypt(String msgStr) throws IllegalBlockSizeException,
			BadPaddingException, UnsupportedEncodingException,
			InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidAlgorithmParameterException,
			DecoderException {//解密数据
		if (!this.encStrKey.equals(this.keyGene.getEncKeyStr())) {
			this.encStrKey = keyGene.getEncKeyStr();
			init();
		}
		byte[] temp = Base64.decodeBase64(msgStr);
		byte[] value = this.decCipher.doFinal(temp);
		String result = new String(value, this.charset);
		return URLDecoder.decode(result, this.charset);
	}

	public String md5EnCode(String src, boolean replace) {
		if (replace) {
			src = src.replace(this.encSourceSpliter, this.hashSourceSpliter);
		}
		return Hex.encodeHexString(digest.digest(src.getBytes()));

	}

	public String urlEncode(String... str) throws UnsupportedEncodingException {
		StringBuilder sb = new StringBuilder();
		for (String s : str) {
			sb.append(URLEncoder.encode(s, this.charset));
			sb.append(this.encSourceSpliter);

		}
		int len = sb.length();
		if (len > 0) {

			sb.delete(len - 1, len);
		}
		return sb.toString();

	}

	public static void main(String[] args) throws Exception {
		DESCode descode = new DESCode(new KeyGenerator());
		MessageDigest dig = MessageDigest.getInstance("MD5");
		String msg1 = "ybcola$"
				+ Hex.encodeHexString(dig.digest("pppppppp".getBytes()))
				+ "$www.yltch.net";
		String msg2 = descode.urlEncode("ybcola", Hex.encodeHexString(dig
				.digest("pppppppp".getBytes())), "www.yltch.net");
		String msg3 = descode.urlEncode("弟弟", "1000", "7", "根据推广逻辑，增加的奖励",
				"dz_02156356548", "www.yltch.net");
		System.out.println(msg1);
		System.out.println(msg2);
		System.out.println(msg3);
		System.out.println("-------encode------");
		msg1 = descode.encrypt(msg1);
		msg2 = descode.encrypt(msg2);
		msg3 = descode.encrypt(msg3);

		System.out.println(msg1);
		System.out.println(msg2);
		System.out.println(msg3);
		System.out.println("------decode--------");
		// String msg4 =
		// "hOWzn/a38v2idQegaPnBUqJL3jsF9uh8OBSQ+LKoCeXGNkXX2ceCIn14g6FbFF4StrBoOfBH6UPRoHvO/Ftwp3qL4FHf/1+3aCU3K0esqqQ=";
		System.out.println(descode.decrypt(msg1));
		System.out.println(descode.decrypt(msg2));
		System.out.println(descode.decrypt(msg3));
	}
}
