package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import sun.misc.BASE64Encoder;

/**
 * ����ʹ��Socket���ӵ��ʼ��������� ��ʵ������ָ�����䷢���ʼ��������Ĺ��ܡ�
 *
 */
public class Mail {
	/**
	 * ���ͱ�־
	 * */
	private static int send_tag1 = 0; //�����ϰ� 0.δ���� 1.������
	private static int send_tag2 = 0; //�������ϰ� 0.δ���� 1.������
	private static int send_tag3 = 0; //���ϼӰ࿪ʼ 0.δ���� 1.������
	private static int send_tag4 = 0; //���ϼӰ���� 0.δ���� 1.������
	
	private static int lmain = 0;
	private static int l1 = 14;
	private static int l2 = 9;
	private static int l3 = 44;
	private static int l4 = 15;
	private static int lmain1 = 0;
	private static Random r = new Random(15); //�����
	
	/**
	 * ���з�
	 */
	private static final String LINE_END = "\r\n";

	/**
	 * ֵΪ��true������߶���Ϣ��������������Ӧ��Ϣ����ֵΪ�� false�������������Ϣ��
	 */
	private boolean isDebug = true;

	/**
	 * ֵΪ��true�����ڷ����ʼ�{@link Mail#send()} �����л��ȡ�������˷��ص���Ϣ�� �����ʼ�������Ϻ���Щ��Ϣ���ظ��û���
	 */
	private boolean isAllowReadSocketInfo = true;

	/**
	 * �ʼ���������ַ
	 */
	private String host;

	/**
	 * �����������ַ
	 */
	private String from;

	/**
	 * �ռ��������ַ
	 */
	private List<String> to;

	/**
	 * ���͵�ַ
	 */
	private List<String> cc;

	/**
	 * ���͵�ַ
	 */
	private List<String> bcc;

	/**
	 * �ʼ�����
	 */
	private String subject;

	/**
	 * �û���
	 */
	private String user;

	/**
	 * ����
	 */
	private String password;

	/**
	 * MIME�ʼ�����
	 */
	private String contentType;

	/**
	 * �����󶨶���ʼ���Ԫ{@link #partSet} �ķָ���ʶ�����ǿ��Խ��ʼ������ļ�ÿһ��������������һ���ʼ���Ԫ ��
	 */
	private String boundary;

	/**
	 * �ʼ���Ԫ�ָ���ʶ���������Խ��������ʼ�����Ϊ�ָ�����ʼ���Ԫ�ı�ʶ ��
	 */
	private String boundaryNextPart;

	/**
	 * �����ʼ������õı���
	 */
	private String contentTransferEncoding;

	/**
	 * �����ʼ��������õ��ַ���
	 */
	private String charset;

	/**
	 * ��������
	 */
	private String contentDisposition;

	/**
	 * �ʼ�����
	 */
	private String content;

	/**
	 * �����ʼ����ڵ���ʾ��ʽ
	 */
	private String simpleDatePattern;

	/**
	 * ������Ĭ��MIME����
	 */
	private String defaultAttachmentContentType;

	/**
	 * �ʼ���Ԫ�ļ��ϣ�����������ĵ�Ԫ�����еĸ�����Ԫ��
	 */
	private List<MailPart> partSet;

	private List<MailPart> alternativeList;

	private String mixedBoundary;

	private String mixedBoundaryNextPart;

	/**
	 * ��ͬ�����ļ���Ӧ��{@link MIME} ����ӳ�䡣����Ӹ��� {@link #addAttachment(String)}
	 * ʱ������������ӳ���в��Ҷ�Ӧ�ļ��� {@link MIME} ���ͣ����û�У� ��ʹ��
	 * {@link #defaultAttachmentContentType} ����������͡�
	 */
	private static Map<String, String> contentTypeMap;

	private static enum TextType {
		PLAIN("plain"), HTML("html");

		private String v;

		private TextType(String v) {
			this.v = v;
		}

		public String getValue() {
			return this.v;
		}
	}

	static {
		// MIME Media Types
		contentTypeMap = new HashMap<String, String>();
		contentTypeMap.put("xls", "application/vnd.ms-excel");
		contentTypeMap.put("xlsx", "application/vnd.ms-excel");
		contentTypeMap.put("xlsm", "application/vnd.ms-excel");
		contentTypeMap.put("xlsb", "application/vnd.ms-excel");
		contentTypeMap.put("doc", "application/msword");
		contentTypeMap.put("dot", "application/msword");
		contentTypeMap.put("docx", "application/msword");
		contentTypeMap.put("docm", "application/msword");
		contentTypeMap.put("dotm", "application/msword");
	}

	/**
	 * ��������ʵ����һ�����ĵ�Ԫ�򸽼���Ԫ�������̳��� {@link Mail} ���������������������Ҫ��Ϊ�������ʼ���Ԫ������ʼ��������
	 * ��ʹ�����׶�һЩ�� ��Щ�ʼ���Ԫȫ����ŵ�partSet �У��ڷ����ʼ� {@link #send()}ʱ, ��������
	 * {@link #getAllParts()} ���������еĵ�Ԫ�ϲ���һ������MIME��ʽ���ַ�����
	 * 
	 */
	private class MailPart extends Mail {
		public MailPart() {
		}
	}
	
	/**
	 * ��������
	 * */
	private static Mail instance = null;
	
	/**
	 * �����캯��
	 * @return synchronized Mail
	 */
	public static synchronized Mail getInstance() {
	    if (instance == null) {
	      instance = new Mail();
	    }
	    return instance;
	  }

	/**
	 * Ĭ�Ϲ��캯��
	 */
	private Mail() {
		//send_tag1 = 0;//δ����
		//send_tag2 = 0;//δ����
		//send_tag3 = 0;//δ����
		//send_tag4 = 0;//δ����
		
		defaultAttachmentContentType = "application/octet-stream";
		simpleDatePattern = "yyyy-MM-dd HH:mm:ss";
		boundary = "--=_NextPart_foxmail_" + System.currentTimeMillis();
		boundaryNextPart = "--" + boundary;
		contentTransferEncoding = "base64";
		contentType = "multipart/mixed";
		charset = Charset.defaultCharset().name();
		partSet = new ArrayList<MailPart>();
		alternativeList = new ArrayList<MailPart>();
		to = new ArrayList<String>();
		cc = new ArrayList<String>();
		bcc = new ArrayList<String>();
		mixedBoundary = "=NextAttachment_foxmail_" + System.currentTimeMillis();
		mixedBoundaryNextPart = "--" + mixedBoundary;
	}

	/**
	 * ����ָ���������ļ����� {@link #contentTypeMap} �в�������Ӧ��MIME���ͣ� ���û�ҵ����򷵻�
	 * {@link #defaultAttachmentContentType} ��ָ����Ĭ�����͡�
	 * 
	 * @param fileName
	 *            �ļ���
	 * @return �����ļ���Ӧ��MIME���͡�
	 */
	private String getPartContentType(String fileName) {
		String ret = null;
		if (null != fileName) {
			int flag = fileName.lastIndexOf(".");
			if (0 <= flag && flag < fileName.length() - 1) {
				fileName = fileName.substring(flag + 1);
			}
			ret = contentTypeMap.get(fileName);
		}

		if (null == ret) {
			ret = defaultAttachmentContentType;
		}
		return ret;
	}

	/**
	 * �������ַ���ת��Ϊbase64������ַ���
	 * 
	 * @param str
	 *            ��Ҫת����ַ���
	 * @param charset
	 *            ԭ�ַ����ı����ʽ
	 * @return base64�����ʽ���ַ�
	 */
	private String toBase64(String str, String charset) {
		if (null != str) {
			try {
				return toBase64(str.getBytes(charset));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return "";
	}

	/**
	 * ��ָ�����ֽ�����ת��Ϊbase64��ʽ���ַ���
	 * 
	 * @param bs
	 *            ��Ҫת����ֽ�����
	 * @return base64�����ʽ���ַ�
	 */
	private String toBase64(byte[] bs) {
		return new BASE64Encoder().encode(bs);
	}

	/**
	 * �������ַ���ת��Ϊbase64������ַ���
	 * 
	 * @param str
	 *            ��Ҫת����ַ���
	 * @return base64�����ʽ���ַ�
	 */
	private String toBase64(String str) {
		return toBase64(str, Charset.defaultCharset().name());
	}

	/**
	 * �����е��ʼ���Ԫ���ձ�׼��MIME��ʽҪ��ϲ���
	 * 
	 * @return ����һ�����е�Ԫ�ϲ�����ַ�����
	 */
	private String getAllParts() {

		StringBuilder sbd = new StringBuilder(LINE_END);
		sbd.append(mixedBoundaryNextPart);
		sbd.append(LINE_END);
		sbd.append("Content-Type: ");
		sbd.append("multipart/alternative");
		sbd.append(";");
		sbd.append("boundary=\"");
		sbd.append(boundary).append("\""); // �ʼ���������
		sbd.append(LINE_END);
		sbd.append(LINE_END);
		sbd.append(LINE_END);
		addPartsToString(alternativeList, sbd, getBoundaryNextPart());
		sbd.append(getBoundaryNextPart()).append("--");
		sbd.append(LINE_END);

		addPartsToString(partSet, sbd, mixedBoundaryNextPart);

		sbd.append(LINE_END);
		sbd.append(mixedBoundaryNextPart).append("--");
		sbd.append(LINE_END);
		// sbd.append(boundaryNextPart).
		// append(LINE_END);
		alternativeList.clear();
		partSet.clear();
		return sbd.toString();
	}

	private void addPartsToString(List<MailPart> list, StringBuilder sbd,
			String nextPartString) {
		int partCount = list.size();
		for (int i = 0; i < partCount; i++) {
			Mail attachment = list.get(i);
			String attachmentContent = attachment.getContent();
			if (null != attachmentContent && 0 < attachmentContent.length()) {
				sbd.append(nextPartString).append(LINE_END);
				sbd.append("Content-Type: ");
				sbd.append(attachment.getContentType());
				sbd.append(LINE_END);
				sbd.append("Content-Transfer-Encoding: ");
				sbd.append(attachment.getContentTransferEncoding());
				sbd.append(LINE_END);
				String cd = attachment.getContentDisposition();
				if (null != cd) {
					sbd.append("Content-Disposition: ");
					sbd.append(cd);
					sbd.append(LINE_END);
				}

				sbd.append(LINE_END);
				sbd.append(attachmentContent);
				sbd.append(LINE_END);
			}
		}
	}

	/**
	 * ����ʼ����ĵ�Ԫ
	 */
	private void addContent() {
		if (null != content) {
			MailPart part = new MailPart();
			part.setContent(toBase64(content));
			part.setContentType("text/plain;charset=\"" + charset + "\"");
			alternativeList.add(part);
		}
	}

	private String listToMailString(List<String> mailAddressList) {
		StringBuilder sbd = new StringBuilder();
		if (null != mailAddressList) {
			int listSize = mailAddressList.size();
			for (int i = 0; i < listSize; i++) {
				if (0 != i) {
					sbd.append(";");
				}
				sbd.append("<").append(mailAddressList.get(i)).append(">");
			}
		}
		return sbd.toString();
	}

	private List<String> getrecipient() {
		List<String> list = new ArrayList<String>();
		list.addAll(to);
		list.addAll(cc);
		list.addAll(bcc);
		return list;
	}

	/**
	 * ��ӳ��ı�����
	 * 
	 * @param text
	 */
	public void addHtmlContent(String text) {
		addContent(text, TextType.HTML);
	}

	/**
	 * ��Ӵ��ı�����
	 * 
	 * @param text
	 */
	public void addTextContent(String text) {
		addContent(text, TextType.PLAIN);
	}

	private void addContent(String text, TextType type) {
		if (null != text) {
			MailPart part = new MailPart();
			part.setContent(toBase64(text));
			part.setContentType("text/" + type.getValue() + ";charset=\""
					+ charset + "\"");
			alternativeList.add(part);
		}
	}

	/**
	 * ���һ��������Ԫ
	 * 
	 * @param filePath
	 *            �ļ�·��
	 */
	public void addAttachment(String filePath) {
		addAttachment(filePath, null);
	}

	public void addTo(String mailAddress) {
		this.to.add(mailAddress);
	}

	public void addCc(String mailAddress) {
		this.cc.add(mailAddress);
	}

	public void addBcc(String mailAddress) {
		this.bcc.add(mailAddress);
	}

	/**
	 * ���һ��������Ԫ
	 * 
	 * @param filePath
	 *            �ļ�·��
	 * @param charset
	 *            �ļ������ʽ
	 */
	public void addAttachment(String filePath, String charset) {
		if (null != filePath && filePath.length() > 0) {
			File file = new File(filePath);
			try {
				addAttachment(file.getName(), new FileInputStream(file),
						charset);
			} catch (FileNotFoundException e) {
				System.out.println("����" + e.getMessage());
				System.exit(1);
			}
		}
	}

	/**
	 * ���һ��������Ԫ
	 * 
	 * @param fileName
	 *            �ļ���
	 * @param attachmentStream
	 *            �ļ���
	 * @param charset
	 *            �ļ������ʽ
	 */
	public void addAttachment(String fileName, InputStream attachmentStream,
			String charset) {
		try {

			byte[] bs = null;
			if (null != attachmentStream) {
				int buffSize = 1024;
				byte[] buff = new byte[buffSize];
				byte[] temp;
				bs = new byte[0];
				int readTotal = 0;
				while (-1 != (readTotal = attachmentStream.read(buff))) {
					temp = new byte[bs.length];
					System.arraycopy(bs, 0, temp, 0, bs.length);
					bs = new byte[temp.length + readTotal];
					System.arraycopy(temp, 0, bs, 0, temp.length);
					System.arraycopy(buff, 0, bs, temp.length, readTotal);
				}
			}

			if (null != bs) {
				MailPart attachmentPart = new MailPart();
				charset = null != charset ? charset : Charset.defaultCharset()
						.name();
				String contentType = getPartContentType(fileName)
						+ ";name=\"=?" + charset + "?B?" + toBase64(fileName)
						+ "?=\"";
				attachmentPart.setCharset(charset);
				attachmentPart.setContentType(contentType);
				attachmentPart.setContentDisposition("attachment;filename=\"=?"
						+ charset + "?B?" + toBase64(fileName) + "?=\"");
				attachmentPart.setContent(toBase64(bs));
				partSet.add(attachmentPart);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != attachmentStream) {
				try {
					attachmentStream.close();
					attachmentStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			Runtime.getRuntime().gc();
			Runtime.getRuntime().runFinalization();
		}
	}

	/**
	 * �����ʼ�
	 * 
	 * @return �ʼ����������ص���Ϣ
	 */
	public String send() {

		// ��������
		// ���ʼ�������Ϻ�������������Socket��
		// PrintWriter,
		// BufferedReader����Ҫ�رա�
		Socket socket = null;
		PrintWriter pw = null;
		BufferedReader br = null;

		try {
			socket = new Socket(host, 25);
			pw = new PrintWriter(socket.getOutputStream());
			br = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));

			StringBuilder infoBuilder = new StringBuilder(
					"\nServer info: \n------------\n");

			// ���������������
			pw.write("HELO ".concat(host).concat(LINE_END)); // ���ӵ��ʼ�����
			if (!readResponse(pw, br, infoBuilder, "220"))
				return infoBuilder.toString();

			pw.write("AUTH LOGIN".concat(LINE_END)); // ��¼
			if (!readResponse(pw, br, infoBuilder, "250"))
				return infoBuilder.toString();

			pw.write(toBase64(user).concat(LINE_END)); // �����û���
			if (!readResponse(pw, br, infoBuilder, "334"))
				return infoBuilder.toString();

			pw.write(toBase64(password).concat(LINE_END)); // ��������
			if (!readResponse(pw, br, infoBuilder, "334"))
				return infoBuilder.toString();			
			
			pw.write("MAIL FROM:<" + from + ">" + LINE_END); // �����������ַ
			if (!readResponse(pw, br, infoBuilder, "235"))
				return infoBuilder.toString();
			
			List<String> recipientList = getrecipient();
			// �ռ������ַ
			for (int i = 0; i < recipientList.size(); i++) {
				pw.write("RCPT TO:<" + recipientList.get(i) + ">" + LINE_END);
				if (!readResponse(pw, br, infoBuilder, "250"))
					return infoBuilder.toString();
			}
			//System.out.println(getAllSendAddress());

			pw.write("DATA" + LINE_END); // ��ʼ�����ʼ�
			if (!readResponse(pw, br, infoBuilder, "250"))
				return infoBuilder.toString();
			
			flush(pw);

			// �����ʼ�ͷ��Ϣ
			StringBuffer sbf = new StringBuffer("From: <" + from + ">"
					+ LINE_END); // ������
			sbf.append("To: " + listToMailString(to) + LINE_END);// �ռ���
			sbf.append("Cc: " + listToMailString(cc) + LINE_END);// �ռ���
			sbf.append("Bcc: " + listToMailString(bcc) + LINE_END);// �ռ���
			sbf.append("Subject: " + subject + LINE_END);// �ʼ�����
			SimpleDateFormat sdf = new SimpleDateFormat(simpleDatePattern);
			sbf.append("Date: ").append(sdf.format(new Date()));
			sbf.append(LINE_END); // ����ʱ��
			sbf.append("Content-Type: ");
			sbf.append(contentType);
			sbf.append(";");
			sbf.append("boundary=\"");
			sbf.append(mixedBoundary).append("\""); // �ʼ���������
			sbf.append(LINE_END);
			// sbf.append(
			// "This is a multi-part message in MIME format."
			// );
			// sbf.append(LINE_END);

			// ����ʼ����ĵ�Ԫ
			addContent();

			// �ϲ����е�Ԫ�����ĺ͸�����
			sbf.append(getAllParts());

			// System.out.println(
			// "///////////\n" +
			// sbf.toString() +
			// "///////////////\n");

			// ����
			sbf.append(LINE_END).append(".").append(LINE_END);
			pw.write(sbf.toString());
			readResponse(pw, br, infoBuilder, "354");
			flush(pw);

			// QUIT�˳�
			pw.write("QUIT" + LINE_END);
			if (!readResponse(pw, br, infoBuilder, "250"))
				return infoBuilder.toString();
			flush(pw);

			return infoBuilder.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Exception:>" + e.getMessage();
		} finally {
			// �ͷ���Դ
			try {
				if (null != socket)
					socket.close();
				if (null != pw)
					pw.close();
				if (null != br)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// this.to.clear();
			// this.cc.clear();
			// this.bcc.clear();
			this.partSet.clear();
		}

	}

	/**
	 * ��SMTP����͵��ʼ�������
	 * 
	 * @param pw
	 *            �ʼ�������������
	 */
	private void flush(PrintWriter pw) {
		if (!isAllowReadSocketInfo) {
			pw.flush();
		}
	}

	/**
	 * ��ȡ�ʼ�����������Ӧ��Ϣ
	 * 
	 * @param pw
	 *            �ʼ�������������
	 * @param br
	 *            �ʼ������������
	 * @param infoBuilder
	 *            ������ŷ�������Ӧ��Ϣ���ַ�������
	 * @param msgCode
	 * @return
	 * @throws IOException
	 */
	private boolean readResponse(PrintWriter pw, BufferedReader br,
			StringBuilder infoBuilder, String msgCode) throws IOException {
		if (isAllowReadSocketInfo) {
			pw.flush();
			String message = br.readLine();
			infoBuilder.append("SERVER:/>");
			infoBuilder.append(message).append(LINE_END);
			if (null == message || 0 > message.indexOf(msgCode)) {
				System.out.println("ERROR: " + message);
				pw.write("QUIT".concat(LINE_END));
				pw.flush();
				return false;
			}
			if (isDebug) {
				System.out.println("DEBUG:/>" + msgCode + "/" + message);
			}
		}
		return true;
	}

	public String getBoundaryNextPart() {
		return boundaryNextPart;
	}

	public void setBoundaryNextPart(String boundaryNextPart) {
		this.boundaryNextPart = boundaryNextPart;
	}

	public String getDefaultAttachmentContentType() {
		return defaultAttachmentContentType;
	}

	public void setDefaultAttachmentContentType(
			String defaultAttachmentContentType) {
		this.defaultAttachmentContentType = defaultAttachmentContentType;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public List<String> getTo() {
		return to;
	}

	public void setTo(List<String> to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getBoundary() {
		return boundary;
	}

	public void setBoundary(String boundary) {
		this.boundary = boundary;
	}

	public String getContentTransferEncoding() {
		return contentTransferEncoding;
	}

	public void setContentTransferEncoding(String contentTransferEncoding) {
		this.contentTransferEncoding = contentTransferEncoding;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getContentDisposition() {
		return contentDisposition;
	}

	public void setContentDisposition(String contentDisposition) {
		this.contentDisposition = contentDisposition;
	}

	public String getSimpleDatePattern() {
		return simpleDatePattern;
	}

	public void setSimpleDatePattern(String simpleDatePattern) {
		this.simpleDatePattern = simpleDatePattern;
	}

	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 * @deprecated ��μ� {@link #addTextContent(String)} ��
	 *             {@link #addHtmlContent(String)}
	 */
	public void setContent(String content) {
		this.content = content;
	}

	public boolean isAllowReadSocketInfo() {
		return isAllowReadSocketInfo;
	}

	public void setAllowReadSocketInfo(boolean isAllowReadSocketInfo) {
		this.isAllowReadSocketInfo = isAllowReadSocketInfo;
	}
	
	private class OtherTask extends TimerTask {
	    public synchronized void run() {
	    	//synchronized(this){
	        //�ж��Ƿ���ʱ��㣬�Ǿ�ִ�У����Ǿ�pass
	        GregorianCalendar d = new GregorianCalendar();
			Date now = new Date();
			Date d1 = new Date(now.getYear(),now.getMonth(),now.getDate(),8,l1);
			Date d2 = new Date(now.getYear(),now.getMonth(),now.getDate(),8,31);
			Date d3 = new Date(now.getYear(),now.getMonth(),now.getDate(),13,l2);
			Date d4 = new Date(now.getYear(),now.getMonth(),now.getDate(),13,26);
			
			Date d5 = new Date(now.getYear(),now.getMonth(),now.getDate(),17,l3);//�Ӱ�ǩ����ʼ
			Date d6 = new Date(now.getYear(),now.getMonth(),now.getDate(),18,1);
			Date d7 = new Date(now.getYear(),now.getMonth(),now.getDate(),20,l4);//�Ӱ�ǩ������
			Date d8 = new Date(now.getYear(),now.getMonth(),now.getDate(),20,22);
			//����һ�������壬�ϰ�ʱ����ִ��
			if((d.get(Calendar.DAY_OF_WEEK)-1 >=1) && (d.get(Calendar.DAY_OF_WEEK)-1) <=5){
				
				if(now.after(d1) && now.before(d2)){
					if(Mail.getInstance().send_tag1==0){
						Mail.getInstance().send_tag1 = 1;//��Ƿ�����...

						System.out.println("�����ϰࣺ"+now);
						//�����ʼ�����
						Mail mail = Mail.getInstance(); // ��ʼ��
						System.out.println("SENDER-" + ":/>"
								+ "��ʼ�����ʼ�...");
						Mail.getInstance().setSubject("CSG�������ǩ��"); // �ʼ�����
						// �ʼ�����*
						Mail.getInstance().addHtmlContent("RT.<br><br>----------------------------<br>������<br>");
						System.out.println(mail.send()); // ����

						System.out.println("SENDER-" + ":/>"
								+ "�ʼ��ѷ�����ϣ�");						

						Mail.getInstance().send_tag2 = 0;//δ����
						Mail.getInstance().send_tag3 = 0;//δ����
						Mail.getInstance().send_tag4 = 0;//δ����
					}
				}
				
				if(now.after(d3) && now.before(d4)){
					if(Mail.getInstance().send_tag2==0){
						Mail.getInstance().send_tag2 = 1;//��Ƿ�����...

						System.out.println("�����ϰࣺ"+now);						
						//�����ʼ�����
						Mail mail = Mail.getInstance(); // ��ʼ��
						System.out.println("SENDER-" + ":/>"
								+ "��ʼ�����ʼ�...");
						Mail.getInstance().setSubject("CSG�������ǩ��"); // �ʼ�����
						// �ʼ�����*
						Mail.getInstance().addHtmlContent("RT.<br><br>----------------------------<br>������<br>");
						System.out.println(mail.send()); // ����

						System.out.println("SENDER-" + ":/>"
								+ "�ʼ��ѷ�����ϣ�");
						
						Mail.getInstance().send_tag1 = 0;//δ����
						Mail.getInstance().send_tag3 = 0;//δ����
						Mail.getInstance().send_tag4 = 0;//δ����
					}
				}
				
				//�ܶ������ļӰ�
				if((d.get(Calendar.DAY_OF_WEEK)-1 ==2) || (d.get(Calendar.DAY_OF_WEEK)-1) ==4){				
					if(now.after(d5) && now.before(d6)){
						if(Mail.getInstance().send_tag3==0){
							Mail.getInstance().send_tag3 = 1;//��Ƿ�����...
	
							System.out.println("���ϼӰ࿪ʼ��"+now);
							//�����ʼ�����
							Mail mail = Mail.getInstance(); // ��ʼ��
							System.out.println("SENDER-" + ":/>"
									+ "��ʼ�����ʼ�...");
							Mail.getInstance().setSubject("CSG�������Ӱ�ǩ����ʼ"); // �ʼ�����
							// �ʼ�����*
							Mail.getInstance().addHtmlContent("RT.<br><br>----------------------------<br>������<br>");
							System.out.println(mail.send()); // ����

							System.out.println("SENDER-" + ":/>"
									+ "�ʼ��ѷ�����ϣ�");
							
							Mail.getInstance().send_tag1 = 0;//δ����
							Mail.getInstance().send_tag2 = 0;//δ����
							Mail.getInstance().send_tag4 = 0;//δ����
						}
					}
					
					if(now.after(d7) && now.before(d8)){
						if(Mail.getInstance().send_tag4==0){
							Mail.getInstance().send_tag4 = 1;//��Ƿ�����...
	
							System.out.println("���ϼӰ������"+now);
							//�����ʼ�����
							Mail mail = Mail.getInstance(); // ��ʼ��
							System.out.println("SENDER-" + ":/>"
									+ "��ʼ�����ʼ�...");
							Mail.getInstance().setSubject("CSG�������Ӱ�ǩ������"); // �ʼ�����
							// �ʼ�����*
							Mail.getInstance().addHtmlContent("RT.<br><br>----------------------------<br>������<br>");
							System.out.println(mail.send()); // ����

							System.out.println("SENDER-" + ":/>"
									+ "�ʼ��ѷ�����ϣ�");

							Mail.getInstance().send_tag1 = 0;//δ����
							Mail.getInstance().send_tag2 = 0;//δ����
							Mail.getInstance().send_tag3 = 0;//δ����
						}
					}
				}
			}//}
	    }
	}
	
	private class Other1Task extends TimerTask {
	    public synchronized void run() {
	    	//synchronized(this){
	    	Mail.getInstance().lmain = Mail.getInstance().r.nextInt(15);//0-15
	    	Mail.getInstance().l1 = Mail.getInstance().lmain + 14;//14-29
	    	Mail.getInstance().l2 = Mail.getInstance().lmain + 9;//9-24
	    	Mail.getInstance().l3 = Mail.getInstance().lmain + 44;//45-59
	    	Mail.getInstance().lmain1 = Mail.getInstance().r.nextInt(5);//0-5
	    	Mail.getInstance().l4 = Mail.getInstance().lmain1 + 15;//15-20
	    	System.out.println("l1:"+l1+";l2:"+l2+";l3:"+l3+";l4:"+l4);
	    }//}
	}
	
	/**
	 * ����
	 * */
	public void init(){
		Timer timer1 = new Timer();
		Timer timer2 = new Timer();
        int seconds = 1;
        timer2.schedule(new Other1Task(), 15, seconds * 1000 * 24*60*60);//1��=24Сʱ=24*60����=24*60*60��        
        timer1.schedule(new OtherTask(), 15, seconds * 1000 * 60);//1���� * 60
	}
	
	public void SendMailByParam(String title, String contents, List fujianlist){
		Mail mail = Mail.getInstance(); // ��ʼ��
		
		mail.setHost("smtp.139.com"); // �ʼ���������ַsmtp
		mail.setFrom("13509323824@139.com"); // ����������
		mail.addTo("13509323824@139.com"); // �ռ�������
		mail.setUser("13509323824"); // �û���
		mail.setPassword("12345674a"); // ����
		
		mail.setSubject(title); // �ʼ�����		
		mail.addHtmlContent(contents);// �ʼ�����
		if(fujianlist!=null && fujianlist.size()>0){//�и���
			for(int i=0;i<fujianlist.size();i++){
				mail.addAttachment(fujianlist.get(i).toString());//�Ѹ�����·������
			}
		}
		
		System.out.println("SENDER-" + ":/>" + "��ʼ�����ʼ�...");
		System.out.println(mail.send()); // ����
		System.out.println("SENDER-" + ":/>" + "�ʼ��ѷ�����ϣ�");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Mail mail = Mail.getInstance(); // ��ʼ��
//		
//		mail.setHost("smtp.139.com"); // �ʼ���������ַsmtp
//		mail.setFrom("13509323824@139.com"); // ����������
//		mail.addTo("13509323824@139.com"); // �ռ�������
//		mail.setUser("13509323824"); // �û���
//		mail.setPassword("12345674a"); // ����
//		
//		mail.setSubject("����"); // �ʼ�����		
//		mail.addHtmlContent("RT.<br>%DATE<br>----------------------------<br>cqx<br>");// �ʼ�����
//		mail.addAttachment("D:\\home\\mkhx\\ALL-LOGS\\dlink163.txt"); // ��Ӹ���1
//		mail.addAttachment("D:\\home\\mkhx\\ALL-LOGS\\dlink165.txt"); // ��Ӹ���2
//		
//		System.out.println("SENDER-" + ":/>" + "��ʼ�����ʼ�...");
//		System.out.println(mail.send()); // ����
//		System.out.println("SENDER-" + ":/>" + "�ʼ��ѷ�����ϣ�");
		
//		Mail.getInstance().SendMailByParam("ħ�����븨�����߱���[test]", "test", null);
	}
}
