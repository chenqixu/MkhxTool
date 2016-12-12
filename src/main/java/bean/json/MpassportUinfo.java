package bean.json;

public class MpassportUinfo {
	String access_token;
	String uin;
	String nick;
	String MUid;
	int time;
	String sign;
	String ppsign;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getMUid() {
		return MUid;
	}
	public void setMUid(String uid) {
		MUid = uid;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
	public String getPpsign() {
		return ppsign;
	}
	public void setPpsign(String ppsign) {
		this.ppsign = ppsign;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getUin() {
		return uin;
	}
	public void setUin(String uin) {
		this.uin = uin;
	}
}
