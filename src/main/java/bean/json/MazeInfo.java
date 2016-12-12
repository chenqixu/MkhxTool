package bean.json;

public class MazeInfo {
	int status;
	MazeInfoData data;
	Version version;
	public MazeInfoData getData() {
		return data;
	}
	public void setData(MazeInfoData data) {
		this.data = data;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Version getVersion() {
		return version;
	}
	public void setVersion(Version version) {
		this.version = version;
	}
}
