package util.nd;

public class th {

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
			String str = String.valueOf(arrayOfChar[(i++)] + arrayOfChar[i]);
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
}
