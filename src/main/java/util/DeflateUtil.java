package util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class DeflateUtil {
	
	private final static byte[] EMPTY_ARRAY = new byte[0];
	
	//解压缩
	public static String uncompress(String str) throws Exception {
	    InflaterInputStream iis = new InflaterInputStream(new ByteArrayInputStream(str.getBytes()));
	    int contentLength = 0;
	    contentLength = (contentLength <= 0) ? 1024 : contentLength;
	    ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    byte[] buffer = new byte[contentLength];
	    int read = iis.read(buffer);
	    
	    while (-1 != read) {
	        bos.write(buffer, 0, read);
	        read = iis.read(buffer);
	    }
	    
	    byte[] _bytes = (contentLength == 0) ? EMPTY_ARRAY : bos.toByteArray();
	    ByteArrayInputStream _is = new ByteArrayInputStream(_bytes);
	    
	    StringBuffer out = new StringBuffer();
	    byte[] b = new byte[4096];
	    for (int n; (n = _is.read(b)) != -1;) {
	        out.append(new String(b, 0, n));
	    }
	    return out.toString();
	}
	
	public static String compress(String str) throws Exception {
		// 用deflate方式压缩请求体并赋给request
		ByteArrayInputStream bis = new ByteArrayInputStream(str.getBytes());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		DeflaterOutputStream dos = new DeflaterOutputStream(bos);
		for (int c = bis.read(); c != -1; c = bis.read()) {
			dos.write(c);
		}
		dos.close();
		return new ByteArrayInputStream(bos.toByteArray()).toString();
		//post.setRequestEntity(entity);
		
	}
}
