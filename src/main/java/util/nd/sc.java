package util.nd;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.telephony.gsm.SmsManager;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nd.commplatform.d.c.bu;
import com.nd.commplatform.d.c.qm;
import com.nd.commplatform.d.c.qy;
import com.nd.commplatform.d.c.qz;
import com.nd.commplatform.d.c.tj;

public class sc {
	private static HashMap<String, String> a = new HashMap();

	private static HashMap<String, String> b = new HashMap();

	public static HashMap<String, String> a(String paramString) {
		HashMap localHashMap = new HashMap();
		if (paramString != null) {
			String[] arrayOfString1 = paramString.split(";");
			for (String str : arrayOfString1) {
				String[] arrayOfString3 = str.split(",");
				if (arrayOfString3 != null)
					if (arrayOfString3.length == 1)
						localHashMap.put(arrayOfString3[0], "");
					else
						localHashMap.put(arrayOfString3[0], arrayOfString3[1]);
			}
		}
		return localHashMap;
	}

	public static String a(Context paramContext) {
		if (bu.a == null) {
			TelephonyManager localTelephonyManager = (TelephonyManager) paramContext
					.getSystemService("phone");
			bu.a = localTelephonyManager.getSubscriberId();
		}
		return bu.a;
	}

	public static String b(Context paramContext) {
		if (bu.b == null) {
			TelephonyManager localTelephonyManager = (TelephonyManager) paramContext
					.getSystemService("phone");
			bu.b = localTelephonyManager.getDeviceId();
			if ((bu.b == null) || (bu.b.trim().equals("".trim())))
				bu.b = qy.a(paramContext);
		}
		return bu.b;
	}

	public static String c(Context paramContext) {
		TelephonyManager localTelephonyManager = (TelephonyManager) paramContext
				.getSystemService("phone");
		String str = localTelephonyManager.getSimSerialNumber();
		if (TextUtils.isEmpty(str))
			return "";
		return str;
	}

	public static byte[] a() {
		if (bu.c == null) {
			tf localtf = new tf();
			bu.c = localtf.a();
		}
		return bu.c;
	}

	public static RSAPublicKey d(Context paramContext) {
		String str = qm.b(paramContext);
		if (str == null)
			str = "B38B9F5D42DEF0BDEF067D3009B1E92475E130399C9DC7CC31F0361D6581D0245CB3AE5664D9337D9370C5CC842D9362F4F51A259DDF928080457A40E682A2BB";
		return tj.a(str, "10001");
	}

	public static String e(Context paramContext) {
		String str = qm.b(paramContext);
		if (str == null)
			str = "B38B9F5D42DEF0BDEF067D3009B1E92475E130399C9DC7CC31F0361D6581D0245CB3AE5664D9337D9370C5CC842D9362F4F51A259DDF928080457A40E682A2BB";
		return str;
	}

	public static RSAPublicKey a(Context paramContext, byte[] paramArrayOfByte) {
		String str = th.a(paramArrayOfByte);
		qm.b(paramContext, str);
		return tj.a(str, "10001");
	}

	public static RSAPublicKey a(Context paramContext, String paramString) {
		qm.b(paramContext, paramString);
		return tj.a(paramString, "10001");
	}

	public static byte[] a(StringBuffer paramStringBuffer) {
		try {
			byte[] arrayOfByte = paramStringBuffer.toString().getBytes("utf-8");
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(
					localByteArrayOutputStream);
			localGZIPOutputStream.write(arrayOfByte);
			localGZIPOutputStream.flush();
			localGZIPOutputStream.close();
			return localByteArrayOutputStream.toByteArray();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return null;
	}

	public static byte[] a(byte[] paramArrayOfByte) {
		try {
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			GZIPOutputStream localGZIPOutputStream = new GZIPOutputStream(
					localByteArrayOutputStream);
			localGZIPOutputStream.write(paramArrayOfByte);
			localGZIPOutputStream.flush();
			localGZIPOutputStream.close();
			return localByteArrayOutputStream.toByteArray();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return null;
	}

	public static byte[] a(byte[] paramArrayOfByte, Context paramContext) {
		if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0))
			return null;
		byte[] arrayOfByte = tj.a(d(paramContext), paramArrayOfByte);
		return arrayOfByte;
	}

	public static byte[] b(byte[] paramArrayOfByte) {
		try {
			ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(
					paramArrayOfByte);
			GZIPInputStream localGZIPInputStream = new GZIPInputStream(
					localByteArrayInputStream);
			byte[] arrayOfByte1 = new byte[1024];
			int i = 0;
			byte[] arrayOfByte2;
			byte[] localObject = new byte[0];
			for (; (i = localGZIPInputStream
					.read(arrayOfByte1)) != -1; localObject = arrayOfByte2) {
				arrayOfByte2 = new byte[localObject.length + i];
				System.arraycopy(localObject, 0, arrayOfByte2, 0,
						localObject.length);
				System.arraycopy(arrayOfByte1, 0, arrayOfByte2,
						localObject.length, i);
			}
			localGZIPInputStream.close();
			localByteArrayInputStream.close();
			return localObject;
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return null;
	}

	public static void a(String paramString1, String paramString2,
			Context paramContext) {
		if ((paramString2 == null) || (paramString2.trim().equals("".trim())))
			return;
		try {
			paramContext.deleteFile(paramString1);
		} catch (Exception localException1) {
		}
		try {
			FileOutputStream localFileOutputStream = paramContext
					.openFileOutput(paramString1, 0);
			tf localtf = new tf("nd.com.cn");
			localFileOutputStream.write(localtf.a(paramString2));
			localFileOutputStream.flush();
			localFileOutputStream.close();
		} catch (Exception localException2) {
			localException2.printStackTrace();
		}
	}

	public static void a(String paramString1, String paramString2) {
		File localFile = new File(paramString1);
		if (localFile.exists())
			localFile.delete();
		try {
			localFile.getParentFile().mkdir();
			localFile.createNewFile();
			FileOutputStream localFileOutputStream = new FileOutputStream(
					localFile);
			localFileOutputStream.write(paramString2.getBytes("utf-8"));
			localFileOutputStream.flush();
			localFileOutputStream.close();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
	}

	public static TreeSet<String> a(String paramString, Context paramContext) {
		TreeSet localTreeSet = new TreeSet();
		try {
			tf localtf = new tf("nd.com.cn");
			FileInputStream localFileInputStream = paramContext
					.openFileInput(paramString);
			byte[] localObject1 = new byte[0];
			byte[] arrayOfByte = new byte[1024];
			int i = -1;
			while ((i = localFileInputStream.read(arrayOfByte)) != -1) {
				byte[] localObject2 = null;
				localObject2 = new byte[i + localObject1.length];
				if (localObject1.length != 0)
					System.arraycopy(localObject1, 0, localObject2, 0,
							localObject1.length);
				System.arraycopy(arrayOfByte, 0, localObject2,
						localObject1.length, i);
				localObject1 = localObject2;
			}
			Object localObject2 = localtf.c((byte[]) localObject1);
			String[] arrayOfString1 = ((String) localObject2).split("&&");
			for (String str : arrayOfString1)
				localTreeSet.add(str);
		} catch (Exception localException) {
		}
		return localTreeSet;
	}

	public static List<String[]> b(String paramString) {
		ArrayList localArrayList = new ArrayList();
		try {
			FileInputStream localFileInputStream = new FileInputStream(
					paramString);
			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(localFileInputStream));
			String str = null;
			while ((str = localBufferedReader.readLine()) != null) {
				String[] arrayOfString = str.split(",");
				localArrayList.add(arrayOfString);
			}
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return localArrayList;
	}

	public static HashMap<String, String> c(String paramString) {
		HashMap localHashMap = new HashMap();
		try {
			FileInputStream localFileInputStream = new FileInputStream(
					paramString);
			BufferedReader localBufferedReader = new BufferedReader(
					new InputStreamReader(localFileInputStream));
			String str = null;
			while ((str = localBufferedReader.readLine()) != null) {
				String[] arrayOfString = str.split(",");
				localHashMap.put(arrayOfString[0], arrayOfString[1]);
			}
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		}
		return localHashMap;
	}

	private static HashMap<String, String> l(Context paramContext) {
		Uri localUri = Uri.parse("content://contacts/phones");
		Cursor localCursor = paramContext.getContentResolver()
				.query(localUri, new String[] { "_id", "number", "name" },
						null, null, "_id asc");
		while ((localCursor != null) && (localCursor.moveToNext())) {
			String str1 = localCursor.getString(1).replace("-", "".trim());
			String str2 = localCursor.getString(2);
			if ((str1 != null) && (!str1.trim().equals("".trim()))
					&& (str2 != null) && (!str2.trim().equals("".trim()))) {
				str1 = d(str1);
				if (e(str1))
					a.put(str1, str2);
			}
		}
		return a;
	}

	public static HashMap<String, String> a(Map<String, String> paramMap,
			Context paramContext) {
		if ((b == null) || (b.isEmpty())) {
			if ((a == null) || (a.isEmpty()))
				a = l(paramContext);
			if ((paramMap != null) && (!paramMap.isEmpty())) {
				Iterator localIterator = paramMap.entrySet().iterator();
				while (localIterator.hasNext()) {
					Map.Entry localEntry = (Map.Entry) localIterator.next();
					String str = (String) a.get(localEntry.getKey());
					if (str != null)
						b.put((String) localEntry.getValue(), str);
				}
			}
		}
		return b;
	}

	public static String d(String paramString) {
		if ((paramString != null) && (!paramString.trim().equals("".trim())))
			paramString = i(paramString);
		return paramString;
	}

	private static String i(String paramString) {
		if (paramString.startsWith("+86"))
			paramString = paramString.substring(2);
		return paramString;
	}

	public static boolean e(String paramString) {
		return (paramString.startsWith("1")) && (paramString.length() == 11);
	}

	public static void b(String paramString1, String paramString2,
			Context paramContext) {
		qz.a("Regist", "send sms to GSM Modem!", paramContext);
		if (f(paramContext)) {
			SmsManager localSmsManager = SmsManager.getDefault();
			localSmsManager.sendTextMessage(paramString1, null, paramString2,
					null, null);
		}
	}

	public static boolean f(Context paramContext) {
		return paramContext.getPackageManager().checkPermission(
				"android.permission.SEND_SMS", paramContext.getPackageName()) == 0;
	}

	public static int g(Context paramContext) {
		if (bu.A == 0) {
			DisplayMetrics localDisplayMetrics = paramContext
					.getApplicationContext().getResources().getDisplayMetrics();
			bu.A = localDisplayMetrics.widthPixels;
		}
		Log.d("width", String.valueOf(bu.A));
		return bu.A;
	}

	public static int h(Context paramContext) {
		if (bu.B == 0) {
			DisplayMetrics localDisplayMetrics = paramContext
					.getApplicationContext().getResources().getDisplayMetrics();
			bu.B = localDisplayMetrics.heightPixels;
		}
		return bu.B;
	}

	public static String i(Context paramContext) {
		String str = a(paramContext);
		if (str == null)
			return "";
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			localStringBuffer.append(str.charAt(i));
			if (i % 2 == 1) {
				localStringBuffer.append((char) b());
				localStringBuffer.append((char) b());
			}
		}
		return localStringBuffer.toString();
	}

	public static String j(Context paramContext) {
		String str = b(paramContext);
		if (str == null)
			return null;
		StringBuffer localStringBuffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			localStringBuffer.append(str.charAt(i));
			if (i % 2 == 1) {
				localStringBuffer.append((char) b());
				localStringBuffer.append((char) b());
			}
		}
		return localStringBuffer.toString();
	}

	public static int b() {
		Random localRandom = new Random();
		int i = Math.abs(localRandom.nextInt() >>> 1) % 26 + 97;
		return i;
	}

	public static String c() {
		return Build.MODEL;
	}

	public static String d() {
		return Build.VERSION.RELEASE;
	}

	public static int e() {
		return Integer.parseInt(Build.VERSION.SDK);
	}

	public static String a(long paramLong) {
		Date localDate = new Date(paramLong);
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat("yyyyMM");
		String str = localSimpleDateFormat.format(localDate);
		return str;
	}

	public static Bitmap c(byte[] paramArrayOfByte) {
		Bitmap localBitmap = BitmapFactory.decodeStream(d(paramArrayOfByte));
		return localBitmap;
	}

	public static byte[] a(Bitmap paramBitmap) {
		byte[] arrayOfByte = (byte[]) null;
		try {
			ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
			paramBitmap.compress(Bitmap.CompressFormat.JPEG, 75,
					localByteArrayOutputStream);
			arrayOfByte = localByteArrayOutputStream.toByteArray();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return arrayOfByte;
	}

	private static InputStream d(byte[] paramArrayOfByte) {
		if ((paramArrayOfByte == null) || (paramArrayOfByte.length == 0))
			return null;
		return new ByteArrayInputStream(paramArrayOfByte);
	}

	public static int k(Context paramContext) {
		int i = 0;
		String str = paramContext.getPackageName();
		try {
			PackageInfo localPackageInfo = paramContext.getPackageManager()
					.getPackageInfo(str, 0);
			i = localPackageInfo.versionCode;
		} catch (PackageManager.NameNotFoundException localNameNotFoundException) {
			localNameNotFoundException.printStackTrace();
		}
		return i;
	}

	public static void f() {
		String str1 = Environment.getExternalStorageState();
		if (!str1.equals("mounted"))
			return;
		String str2 = Environment.getExternalStorageDirectory().getPath();
		if ((str2 == null) || (str2.equals("")))
			str2 = "/sdcard";
		StringBuilder localStringBuilder1 = new StringBuilder(str2);
		localStringBuilder1.append(File.separator);
		localStringBuilder1.append("ndcommplatform");
		localStringBuilder1.append(File.separator);
		localStringBuilder1.append("downapp");
		localStringBuilder1.append(File.separator);
		String str3 = localStringBuilder1.toString();
		File localFile = new File(str3);
		String[] arrayOfString1 = localFile.list();
		if (arrayOfString1 == null)
			return;
		for (String str4 : arrayOfString1) {
			StringBuilder localStringBuilder2 = new StringBuilder(str3);
			localStringBuilder2.append(str4);
			j(localStringBuilder2.toString());
		}
	}

	private static boolean j(String paramString) {
		long l1 = 15L;
		long l2 = 86400000L;
		try {
			File localFile = new File(paramString);
			if (!localFile.exists())
				return true;
			if (localFile.isDirectory())
				return false;
			long l3 = localFile.lastModified();
			long l4 = System.currentTimeMillis();
			if (l4 > l3) {
				long l5 = 0;
				l5 = (l4 - l3) / 86400000L;
				if (l5 > 15L) {
					localFile.delete();
					return true;
				}
			}
			long l5 = localFile.length();
			if (l5 <= 0L) {
				localFile.delete();
				return true;
			}
		} catch (Exception localException) {
		}
		return false;
	}

	public static String f(String paramString) {
		if (paramString == null)
			return null;
		byte[] arrayOfByte = paramString.getBytes();
		for (int i = 0; i < arrayOfByte.length; i++)
			arrayOfByte[i] = ((byte) (arrayOfByte[i] ^ 0xFF));
		return th.a(arrayOfByte);
	}

	public static String g(String paramString) {
		byte[] arrayOfByte = th.a(paramString);
		for (int i = 0; i < arrayOfByte.length; i++)
			arrayOfByte[i] = ((byte) (arrayOfByte[i] ^ 0xFFFFFFFF));
		return new String(arrayOfByte);
	}

	public static byte[] h(String paramString) {
		try {
			FileInputStream localFileInputStream = new FileInputStream(
					new File(paramString));
			byte[] localObject = (byte[]) null;
			byte[] arrayOfByte1 = new byte[1024];
			int i = 0;
			while ((i = localFileInputStream.read(arrayOfByte1)) != -1)
				if (localObject == null) {
					localObject = new byte[i];
					System.arraycopy(arrayOfByte1, 0, localObject, 0, i);
				} else {
					byte[] arrayOfByte2 = new byte[localObject.length + i];
					System.arraycopy(localObject, 0, arrayOfByte2, 0,
							localObject.length);
					System.arraycopy(arrayOfByte1, 0, arrayOfByte2,
							localObject.length, i);
					localObject = arrayOfByte2;
				}
			return localObject;
		} catch (FileNotFoundException localFileNotFoundException) {
			localFileNotFoundException.printStackTrace();
		} catch (IOException localIOException) {
			localIOException.printStackTrace();
		} catch (Exception localException) {
			localException.printStackTrace();
		}
		return null;
	}

	public static int a(JSONObject paramJSONObject, String paramString) {
		if ((paramJSONObject != null) && (!paramJSONObject.isNull(paramString)))
			try {
				return paramJSONObject.getInt(paramString);
			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
			}
		return 0;
	}

	public static String b(JSONObject paramJSONObject, String paramString) {
		if ((paramJSONObject != null) && (!paramJSONObject.isNull(paramString)))
			try {
				return paramJSONObject.getString(paramString);
			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
			}
		return null;
	}

	public static JSONArray c(JSONObject paramJSONObject, String paramString) {
		if ((paramJSONObject != null) && (!paramJSONObject.isNull(paramString)))
			try {
				return paramJSONObject.getJSONArray(paramString);
			} catch (JSONException localJSONException) {
				localJSONException.printStackTrace();
			}
		return null;
	}

	public static Date b(String paramString1, String paramString2) {
		if ((paramString1 == null) || (paramString1.trim().equals("")))
			return null;
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				paramString2);
		try {
			return localSimpleDateFormat.parse(paramString1);
		} catch (ParseException localParseException) {
		}
		return null;
	}

	public static boolean a(Context paramContext, boolean paramBoolean,
			String paramString1, String paramString2, String paramString3,
			String paramString4) {
		try {
			ConnectivityManager localConnectivityManager = (ConnectivityManager) paramContext
					.getSystemService("connectivity");
			if (localConnectivityManager != null) {
				NetworkInfo localNetworkInfo = localConnectivityManager
						.getActiveNetworkInfo();
				if ((localNetworkInfo != null)
						&& (localNetworkInfo.isConnected())
						&& (localNetworkInfo.getState() == NetworkInfo.State.CONNECTED))
					return true;
			}
		} catch (Exception localException) {
			localException.printStackTrace();
			if (paramBoolean)
				new AlertDialog.Builder(paramContext).setTitle(paramString1)
						.setMessage(paramString2).setPositiveButton(
								paramString3,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										Intent localIntent = new Intent("/");
										ComponentName localComponentName = new ComponentName(
												"com.android.settings",
												"com.android.settings.Settings");
										localIntent
												.setComponent(localComponentName);
										localIntent
												.setAction("android.intent.action.VIEW");
//										sc.this.startActivity(localIntent);
									}
								}).setNegativeButton(paramString4, null).show();
		}
		return false;
	}
}