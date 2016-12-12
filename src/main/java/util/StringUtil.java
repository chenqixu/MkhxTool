package util;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	/**  
	 * 把十六进制Unicode编码字符串转换为中文字符串 
	 * */
	public static String unicodeToString(String str) {
		Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
		Matcher matcher = pattern.matcher(str);
		char ch;
		while (matcher.find()) {
			ch = (char) Integer.parseInt(matcher.group(2), 16);
			str = str.replace(matcher.group(1), ch + "");
		}
		return str;
	}
	

	/**
	 * 随机一个4位数
	 * */
	public int radom_four(){
		StringBuffer sb = new StringBuffer();
		String s = "0123456789";
		char[] c = s.toCharArray();
		Random random = new Random();
		for (int i = 0; i < 4; i++) {
			sb.append(c[random.nextInt(c.length)]);
		}
		return Integer.valueOf(sb.toString());
	}
}
