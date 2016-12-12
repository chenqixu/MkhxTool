package util.nd;

public class tg {

	public static byte[] a(int paramInt) {
		byte[] arrayOfByte = new byte[4];
		arrayOfByte[0] = ((byte) (paramInt & 0xFF));
		arrayOfByte[1] = ((byte) (paramInt >> 8 & 0xFF));
		arrayOfByte[2] = ((byte) (paramInt >> 16 & 0xFF));
		arrayOfByte[3] = ((byte) (paramInt >> 24 & 0xFF));
		return arrayOfByte;
	}

	public static byte[] b(int paramInt) {
		byte[] arrayOfByte = new byte[4];
		arrayOfByte[3] = ((byte) (paramInt & 0xFF));
		arrayOfByte[2] = ((byte) (paramInt >> 8 & 0xFF));
		arrayOfByte[1] = ((byte) (paramInt >> 16 & 0xFF));
		arrayOfByte[0] = ((byte) (paramInt >> 24 & 0xFF));
		return arrayOfByte;
	}

	public static byte[] a(short paramShort) {
		byte[] arrayOfByte = new byte[2];
		arrayOfByte[0] = ((byte) (paramShort & 0xFF));
		arrayOfByte[1] = ((byte) (paramShort >> 8 & 0xFF));
		return arrayOfByte;
	}

	public static byte[] b(short paramShort) {
		byte[] arrayOfByte = new byte[2];
		arrayOfByte[1] = ((byte) (paramShort & 0xFF));
		arrayOfByte[0] = ((byte) (paramShort >> 8 & 0xFF));
		return arrayOfByte;
	}

	public static byte[] a(float paramFloat) {
		return a(Float.floatToRawIntBits(paramFloat));
	}

	public static byte[] b(float paramFloat) {
		return b(Float.floatToRawIntBits(paramFloat));
	}

	public static byte[] a(String paramString, int paramInt) {
		while (paramString.getBytes().length < paramInt)
			paramString = paramString + " ";
		return paramString.getBytes();
	}

	public static String a(byte[] paramArrayOfByte) {
		StringBuffer localStringBuffer = new StringBuffer("");
		int i = paramArrayOfByte.length;
		for (int j = 0; j < i; j++)
			localStringBuffer.append((char) (paramArrayOfByte[j] & 0xFF));
		return localStringBuffer.toString();
	}

	public static byte[] a(String paramString) {
		return paramString.getBytes();
	}

	public static int b(byte[] paramArrayOfByte) {
		int i = 0;
		for (int j = 0; j < 3; j++) {
			if (paramArrayOfByte[j] >= 0)
				i += paramArrayOfByte[j];
			else
				i = i + 256 + paramArrayOfByte[j];
			i *= 256;
		}
		if (paramArrayOfByte[3] >= 0)
			i += paramArrayOfByte[3];
		else
			i = i + 256 + paramArrayOfByte[3];
		return i;
	}

	public static int c(byte[] paramArrayOfByte) {
		int i = 0;
		for (int j = 0; j < 3; j++) {
			if (paramArrayOfByte[(3 - j)] >= 0)
				i += paramArrayOfByte[(3 - j)];
			else
				i = i + 256 + paramArrayOfByte[(3 - j)];
			i *= 256;
		}
		if (paramArrayOfByte[0] >= 0)
			i += paramArrayOfByte[0];
		else
			i = i + 256 + paramArrayOfByte[0];
		return i;
	}

	public static short d(byte[] paramArrayOfByte) {
		int i = 0;
		if (paramArrayOfByte[0] >= 0)
			i += paramArrayOfByte[0];
		else
			i = i + 256 + paramArrayOfByte[0];
		i *= 256;
		if (paramArrayOfByte[1] >= 0)
			i += paramArrayOfByte[1];
		else
			i = i + 256 + paramArrayOfByte[1];
		short s = (short) i;
		return s;
	}

	public static short e(byte[] paramArrayOfByte) {
		int i = 0;
		if (paramArrayOfByte[1] >= 0)
			i += paramArrayOfByte[1];
		else
			i = i + 256 + paramArrayOfByte[1];
		i *= 256;
		if (paramArrayOfByte[0] >= 0)
			i += paramArrayOfByte[0];
		else
			i = i + 256 + paramArrayOfByte[0];
		short s = (short) i;
		return s;
	}

	public static float f(byte[] paramArrayOfByte) {
		int i = 0;
		Float localFloat = new Float(0.0D);
		i = (((paramArrayOfByte[0] & 0xFF) << 8 | paramArrayOfByte[1] & 0xFF) << 8 | paramArrayOfByte[2] & 0xFF) << 8
				| paramArrayOfByte[3] & 0xFF;
		return Float.intBitsToFloat(i);
	}

	public static float g(byte[] paramArrayOfByte) {
		int i = 0;
		Float localFloat = new Float(0.0D);
		i = (((paramArrayOfByte[3] & 0xFF) << 8 | paramArrayOfByte[2] & 0xFF) << 8 | paramArrayOfByte[1] & 0xFF) << 8
				| paramArrayOfByte[0] & 0xFF;
		return Float.intBitsToFloat(i);
	}

	public static byte[] h(byte[] paramArrayOfByte) {
		int i = paramArrayOfByte.length;
		byte[] arrayOfByte = new byte[i];
		for (int j = 0; j < i; j++)
			arrayOfByte[(i - j - 1)] = paramArrayOfByte[j];
		return arrayOfByte;
	}

	public static void i(byte[] paramArrayOfByte) {
		int i = paramArrayOfByte.length;
		for (int j = 0; j < i; j++)
			System.out.print(paramArrayOfByte + " ");
		System.out.println("");
	}

	public static void j(byte[] paramArrayOfByte) {
		int i = paramArrayOfByte.length;
		String str = "";
		for (int j = 0; j < i; j++)
			str = str + paramArrayOfByte + " ";
	}

	public static int c(int paramInt) {
		int i = b(a(paramInt));
		return i;
	}

	public static short c(short paramShort) {
		short s = d(a(paramShort));
		return s;
	}

	public static float c(float paramFloat) {
		float f = f(a(paramFloat));
		return f;
	}
}
