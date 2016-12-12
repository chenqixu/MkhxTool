package util.nd;

import java.io.UnsupportedEncodingException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.nd.commplatform.d.c.sc;
//import com.nd.commplatform.d.c.tf;
//import com.nd.commplatform.d.c.tg;
//import com.nd.commplatform.d.c.th;
//import com.nd.commplatform.d.c.ti;

public class hv {

	private byte[] a = new byte[32];

	private byte[] b = null;

	private byte c;

	private short d;

	private int e;

	private String f;

	private byte g;

	private boolean h;

	private JSONObject i = null;

	public hv(byte[] paramArrayOfByte) {
		a(paramArrayOfByte);
	}

	public byte[] a() {
		if ((this.b == null) || (this.b.length == 0))
			return this.b;
		if (this.c == 0)
			return this.b;
		try {
			tf localtf = new tf(sc.a());
			if (this.c == 1)
				return sc.b(this.b);
			if (this.c == 2)
				return localtf.b(this.b);
			if (this.c == 3)
				return sc.b(localtf.b(this.b));
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return this.b;
	}

	public String b() {
		try {
			byte[] arrayOfByte = a();
			if ((arrayOfByte == null) || (arrayOfByte.length == 0))
				return null;
			String str = new String(arrayOfByte, "utf-8");
			return str;
		} catch (UnsupportedEncodingException localUnsupportedEncodingException) {
			localUnsupportedEncodingException.printStackTrace();
		}
		return null;
	}

	public JSONObject c() {
		String str = b();
		if (str == null)
			return null;
		try {
			this.i = new JSONObject(str);
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return this.i;
	}

	public String d() {
		String str = b();
		if (str == null)
			return null;
		return str;
	}

	public String a(String paramString) {
		try {
			if (this.i == null)
				c();
			if ((this.i != null) && (!this.i.isNull(paramString)))
				return this.i.getString(paramString);
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return null;
	}

	public JSONArray b(String paramString) {
		try {
			if (this.i == null)
				c();
			if ((this.i != null) && (!this.i.isNull(paramString)))
				return this.i.getJSONArray(paramString);
		} catch (JSONException localJSONException) {
			localJSONException.printStackTrace();
		}
		return null;
	}

	public byte e() {
		return this.c;
	}

	public int f() {
		return this.d;
	}

	public int g() {
		return this.e;
	}

	public String h() {
		return this.f;
	}

	public byte i() {
		return this.g;
	}

	public boolean j() {
		return this.h;
	}

	private void a(byte[] paramArrayOfByte) {
//		try {
//			System.out.println(new String(paramArrayOfByte, "utf-8"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		this.b = new byte[paramArrayOfByte.length - 32];
		System.arraycopy(paramArrayOfByte, 0, this.a, 0, 32);
		System.arraycopy(paramArrayOfByte, 32, this.b, 0, this.b.length);
//		try {
//			System.out.println(new String(this.b, "iso-8859-1"));
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
		k();
		l();
		m();
		n();
		o();
	}
	
	public byte[] aa(byte[] paramArrayOfByte) {
		byte aa[] = null;
		aa = new byte[32];
		System.arraycopy(paramArrayOfByte, 0, aa, 0, 32);
		return aa;
	}

	private void k() {
		this.c = this.a[0];
		System.out.println("k:this.c:"+this.c);
	}

	private void l() {
		byte[] arrayOfByte = new byte[2];
		System.arraycopy(this.a, 1, arrayOfByte, 0, 2);
		this.d = tg.e(arrayOfByte);
		System.out.println("l:this.d:"+this.d);
	}

	private void m() {
		byte[] arrayOfByte = new byte[4];
		System.arraycopy(this.a, 3, arrayOfByte, 0, 4);
		this.e = tg.c(arrayOfByte);
		System.out.println("m:this.e:"+this.e);
	}

	private void n() {
		this.g = this.a[7];
		System.out.println("n:this.g:"+this.g);
	}

	private void o() {
		if ((this.b == null) || (this.b.length == 0))
			this.h = true;
		String str1 = p();
		byte[] arrayOfByte = new byte[4];
		arrayOfByte[0] = this.a[8];
		arrayOfByte[1] = this.a[9];
		arrayOfByte[2] = this.a[10];
		arrayOfByte[3] = this.a[11];
		String str2 = new String(arrayOfByte);
		System.out.println("o:str1:"+str1+" str2:"+str2);
		if (str2.equalsIgnoreCase(str1))
			this.h = true;
	}

	private String p() {
		try {
			System.out.println("p:this.b:"+new String(this.b,"utf-8"));
			System.out.println("p:ti.a(this.b):获得this.b的md5密文,没有转成十六进制的字符串形式");
			byte[] tmp1 = ti.a(this.b);
			String str = th.a(tmp1);
			System.out.println("p:str 把this.b的md5密文转成十六进制的字符串格式:"+str);
			System.out.println("p:str.substring(this.g, this.g + 4):"+str.substring(this.g, this.g + 4));
			return str.substring(this.g, this.g + 4);
		} catch (Exception localException) {
		}
		return null;
	}
}
