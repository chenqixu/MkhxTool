package bean.json;

public class Passport {
	String errno;
	String errmsg;
	Integer consume;
	PassportUser user;
	public String getErrno() {
		return errno;
	}
	public void setErrno(String errno) {
		this.errno = errno;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public Integer getConsume() {
		return consume;
	}
	public void setConsume(Integer consume) {
		this.consume = consume;
	}
	public PassportUser getUser() {
		return user;
	}
	public void setUser(PassportUser user) {
		this.user = user;
	}
	
}
