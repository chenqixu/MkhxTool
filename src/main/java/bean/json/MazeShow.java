package bean.json;

public class MazeShow {
	int status;
	MazeShowData data;
	Version version;
	public MazeShowData getData() {
		return data;
	}
	public void setData(MazeShowData data) {
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
