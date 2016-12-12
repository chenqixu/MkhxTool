package bean.json;

public class MpassportData {
	boolean new_status;
	MpassportCurrent current;
	MpassportCurrent[] list;
	MpassportUinfo uinfo;
	public MpassportCurrent[] getList() {
		return list;
	}
	public void setList(MpassportCurrent[] list) {
		this.list = list;
	}
	public MpassportCurrent getCurrent() {
		return current;
	}
	public void setCurrent(MpassportCurrent current) {
		this.current = current;
	}
	public boolean getNew_status() {
		return new_status;
	}
	public void setNew_status(boolean new_status) {
		this.new_status = new_status;
	}
	public MpassportUinfo getUinfo() {
		return uinfo;
	}
	public void setUinfo(MpassportUinfo uinfo) {
		this.uinfo = uinfo;
	}
}
