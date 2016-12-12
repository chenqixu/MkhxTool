package util;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class IORoutines {

	  public static final byte[] LINE_BREAK_BYTES = { 13, 10 };

	  public static String loadAsText(InputStream in, String encoding) throws IOException {
	    return loadAsText(in, encoding, 4096);
	  }

	  public static String loadAsText(InputStream in, String encoding, int bufferSize) throws IOException {
	    InputStreamReader reader = new InputStreamReader(in, encoding);
	    char[] buffer = new char[bufferSize];
	    int offset = 0;
	    while (true) {
	      int remain = buffer.length - offset;
	      if (remain <= 0) {
	        char[] newBuffer = new char[buffer.length * 2];
	        System.arraycopy(buffer, 0, newBuffer, 0, offset);
	        buffer = newBuffer;
	        remain = buffer.length - offset;
	      }
	      int numRead = reader.read(buffer, offset, remain);
	      if (numRead == -1) {
	        break;
	      }
	      offset += numRead;
	    }
	    return new String(buffer, 0, offset);
	  }

	  public static byte[] load(File file) throws IOException {
	    long fileLength = file.length();
	    if (fileLength > 2147483647L) {
	      throw new IOException("File '" + file.getName() + "' too big");
	    }
	    InputStream in = new FileInputStream(file);
	    try {
	      return loadExact(in, (int)fileLength);
	    } finally {
	      in.close();
	    }
	  }

	  public static byte[] load(InputStream in) throws IOException {
	    return load(in, 4096);
	  }
	  public static byte[] load(InputStream in, int initialBufferSize) throws IOException {
	    if (initialBufferSize == 0) {
	      initialBufferSize = 1;
	    }
	    byte[] buffer = new byte[initialBufferSize];
	    int offset = 0;
	    while (true) {
	      int remain = buffer.length - offset;
	      if (remain <= 0) {
	        int newSize = buffer.length * 2;
	        byte[] newBuffer = new byte[newSize];
	        System.arraycopy(buffer, 0, newBuffer, 0, offset);
	        buffer = newBuffer;
	        remain = buffer.length - offset;
	      }
	      int numRead = in.read(buffer, offset, remain);
	      if (numRead == -1) {
	        break;
	      }
	      offset += numRead;
	    }
	    if (offset < buffer.length) {
	      byte[] newBuffer = new byte[offset];
	      System.arraycopy(buffer, 0, newBuffer, 0, offset);
	      buffer = newBuffer;
	    }
	    return buffer;
	  }

	  public static byte[] loadExact(InputStream in, int length) throws IOException {
	    byte[] buffer = new byte[length];
	    int offset = 0;
	    while (true) {
	      int remain = length - offset;
	      if (remain <= 0) {
	        break;
	      }
	      int numRead = in.read(buffer, offset, remain);
	      if (numRead == -1) {
	        throw new IOException("Reached EOF, read " + offset + " expecting " + length);
	      }
	      offset += numRead;
	    }
	    return buffer;
	  }

	  public static boolean equalContent(File file, byte[] content) throws IOException {
	    long length = file.length();
	    if (length > 2147483647L) {
	      throw new IOException("File '" + file + "' too big");
	    }
	    InputStream in = new FileInputStream(file);
	    try {
	      byte[] fileContent = loadExact(in, (int)length);
	      return Arrays.equals(content, fileContent);
	    } finally {
	      in.close();
	    }
	  }

	  public static void save(File file, byte[] content) throws IOException {
	    FileOutputStream out = new FileOutputStream(file);
	    try {
	      out.write(content);
	    } finally {
	      out.close();
	    }
	  }

	  public static String readLine(InputStream in)
	    throws IOException
	  {
	    StringBuffer sb = null;
	    int b;
	    while ((b = in.read()) != -1) {
	      if (sb == null) {
	        sb = new StringBuffer();
	      }
	      switch (b) {
	      case 10:
	        break;
	      case 13:
	        break;
	      default:
	        sb.append((char)b);
	      }
	    }

	    return sb == null ? null : sb.toString();
	  }

	  public static void touch(File file) {
	    file.setLastModified(System.currentTimeMillis());
	  }

	  public static void saveStrings(File file, Collection list) throws IOException {
	    BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(file));
	    try {
	      PrintWriter writer = new PrintWriter(bout);
	      Iterator i = list.iterator();
	      while (i.hasNext()) {
	        String text = (String)i.next();
	        writer.println(text);
	      }
	      writer.flush();
	    } finally {
	      bout.close();
	    }
	  }

	  public static List loadStrings(File file) throws IOException {
	    List list = new LinkedList();
	    InputStream in = new FileInputStream(file);
	    try {
	      BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	      String line;
	      while ((line = reader.readLine()) != null) {
	        list.add(line);
	      }
	      return list;
	    } finally {
	      in.close();
	    }
	  }
}
