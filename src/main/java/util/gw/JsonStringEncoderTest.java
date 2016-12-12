package util.gw;

//import com.flurry.org.codehaus.jackson.io.JsonStringEncoder;
import com.mobileapptracker.Encryption;

public class JsonStringEncoderTest {
	public static void main(String[] args) throws Exception {
		String a = "jowxl6kaomfleNqrVsqtKk5XslJKybcNzsgvVyuzNbIwtFQryCgosHX0cwny93SJdwn19w4FCeXYRnnEO%2Fsp6Sj5JhYElySmp3qmKFlZ6CjlJecBzTA0MFCqBQAxkhjl";
//		String a = "apache=1&username=pp";
		String str = java.net.URLDecoder.decode(a, "utf-8");
//		String str = java.net.URLEncoder.encode(a, "utf-8");
//		char[] result = null;
		byte[] result1 = null;
		System.out.println(str);
//		result = JsonStringEncoder.getInstance().quoteAsString(str);
//		result1 = JsonStringEncoder.getInstance().quoteAsUTF8(str);
//		result1 = JsonStringEncoder.getInstance().encodeAsUTF8(str);

//		System.out.println("char[]:" + new String(result));
//		System.out.println("utf-8:" + new String(result1, "utf-8"));
		
		Encryption e = new Encryption("79fdc63486df495ab524ed6d08794500", "heF9BATUfWuISyO8");
		Object localObject5 = new StringBuilder(str);
//		result1 = e.encrypt(((StringBuilder)localObject5).toString());
//		result1 = e.hexToBytes(str);
		result1 = e.decrypt(((StringBuilder)localObject5).toString());
		System.out.println("result1:" + new String(result1));
//		localObject5 = new StringBuilder(new String(result1));
//		result1 = e.decrypt(((StringBuilder)localObject5).toString());
//		System.out.println("utf-8:" + new String(result1));
		
//		StringBuilder localStringBuilder5 = new StringBuilder(e.bytesToHex(e.encrypt(((StringBuilder)localObject5).toString())));
//		System.out.println(localStringBuilder5);
	}
}
