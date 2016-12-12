package util;

import com.duoku.platform.util.d;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class DuokuAesUtil {

	    public DuokuAesUtil(){
	        a = null;
	        b = null;
	        c = "AKlMU89D3FchIkhK";
	        try
	        {
	            a = new SecretKeySpec(c.getBytes(), "AES");
	            b = Cipher.getInstance("AES/ECB/PKCS5Padding");
	        }
	        catch(Exception exception)
	        {
	            exception.printStackTrace();
	        }
	    }

	    public String a(String s){
//	        String s1 = "";
	        String s3 = "";
	        try
	        {
	            b.init(1, a);
	            byte abyte0[] = b.doFinal(s.getBytes("UTF-8"));
	            String s2 = new String(d.b(abyte0, 0));
	            s3 = c(s2);
	        }
	        catch(Exception exception)
	        {
	            exception.printStackTrace();
	        }
	        return s3;
	    }

	    public String b(String s) {
	        String s1 = "";
	        try
	        {
	            byte abyte0[] = d.a(s, 0);
	            b.init(2, a);
	            byte abyte1[] = b.doFinal(abyte0);
	            s1 = new String(abyte1, "UTF-8");
	        }
	        catch(Exception exception)
	        {
	            exception.printStackTrace();
	        }
	        return s1;
	    }

	    public static String c(String s) {
	        String s1 = "";
	        StringBuffer stringbuffer = new StringBuffer();
	        for(int i = 0; i < s.length(); i++)
	        {
	            char c1 = s.charAt(i);
	            if(c1 != '\n' && c1 != '\r')
	                stringbuffer.append(s.subSequence(i, i + 1));
	        }

	        s1 = new String(stringbuffer);
	        return s1;
	    }

	    private SecretKey a;
	    private Cipher b;
	    private String c;
}
