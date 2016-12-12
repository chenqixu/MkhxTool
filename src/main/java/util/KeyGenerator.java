package util;

public class KeyGenerator {
	private String key = "hongsoft";

	private String encKeyStr = "9F8C243AEE347183B39DD81B20941E86BC11529B034C8842";
	
	public KeyGenerator(){		
	}

	public String getEncKeyStr() {
		return encKeyStr;
	}

	public void setEncKeyStr(String encKeyStr) {
		this.encKeyStr = encKeyStr;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
