package bean.json;

public class HttpService {
	String returncode;
	String returnmsg;
	HttpServiceReturnObjs returnobjs;
	public String getReturncode() {
		return returncode;
	}
	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}
	public String getReturnmsg() {
		return returnmsg;
	}
	public void setReturnmsg(String returnmsg) {
		this.returnmsg = returnmsg;
	}
	public HttpServiceReturnObjs getReturnobjs() {
		return returnobjs;
	}
	public void setReturnobjs(HttpServiceReturnObjs returnobjs) {
		this.returnobjs = returnobjs;
	}
}
