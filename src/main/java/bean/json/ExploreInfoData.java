package bean.json;

public class ExploreInfoData {
	String[] bonus;
	String userlevel;
	int exp;
	String prevexp;
	String nextexp;
	ThievesInfo thievesinfo;
	int countdown;
	public int getCountdown() {
		return countdown;
	}
	public void setCountdown(int countdown) {
		this.countdown = countdown;
	}
	public String[] getBonus() {
		return bonus;
	}
	public void setBonus(String[] bonus) {
		this.bonus = bonus;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public String getNextexp() {
		return nextexp;
	}
	public void setNextexp(String nextexp) {
		this.nextexp = nextexp;
	}
	public String getPrevexp() {
		return prevexp;
	}
	public void setPrevexp(String prevexp) {
		this.prevexp = prevexp;
	}
	public ThievesInfo getThievesinfo() {
		return thievesinfo;
	}
	public void setThievesinfo(ThievesInfo thievesinfo) {
		this.thievesinfo = thievesinfo;
	}
	public String getUserlevel() {
		return userlevel;
	}
	public void setUserlevel(String userlevel) {
		this.userlevel = userlevel;
	}
}
