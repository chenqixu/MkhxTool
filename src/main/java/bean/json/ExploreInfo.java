package bean.json;

public class ExploreInfo {
	int status;
	ExploreInfoData data;
	Version version;
	public ExploreInfoData getData() {
		return data;
	}
	public void setData(ExploreInfoData data) {
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
