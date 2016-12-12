package bean.json;

public class ThievesInfo {
	int uid;
	String nickname;
	String avatar;
	String sex;
	int thievesid;
	int time;
	int status;
	String[] attackers;
	String[] awards;
	int hpcount;
	int hpcurrent;
	int type;
	int userthievesid;
	int round;
	int fleetime;
	public String[] getAttackers() {
		return attackers;
	}
	public void setAttackers(String[] attackers) {
		this.attackers = attackers;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String[] getAwards() {
		return awards;
	}
	public void setAwards(String[] awards) {
		this.awards = awards;
	}
	public int getFleetime() {
		return fleetime;
	}
	public void setFleetime(int fleetime) {
		this.fleetime = fleetime;
	}
	public int getHpcount() {
		return hpcount;
	}
	public void setHpcount(int hpcount) {
		this.hpcount = hpcount;
	}
	public int getHpcurrent() {
		return hpcurrent;
	}
	public void setHpcurrent(int hpcurrent) {
		this.hpcurrent = hpcurrent;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getRound() {
		return round;
	}
	public void setRound(int round) {
		this.round = round;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getThievesid() {
		return thievesid;
	}
	public void setThievesid(int thievesid) {
		this.thievesid = thievesid;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getUserthievesid() {
		return userthievesid;
	}
	public void setUserthievesid(int userthievesid) {
		this.userthievesid = userthievesid;
	}
}
