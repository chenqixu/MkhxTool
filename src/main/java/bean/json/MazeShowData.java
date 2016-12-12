package bean.json;

public class MazeShowData {
	String name;
	int layer;
	int clear;
	int freereset;
	int resetcash;
	public int getClear() {
		return clear;
	}
	public void setClear(int clear) {
		this.clear = clear;
	}
	public int getFreereset() {
		return freereset;
	}
	public void setFreereset(int freereset) {
		this.freereset = freereset;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getResetcash() {
		return resetcash;
	}
	public void setResetcash(int resetcash) {
		this.resetcash = resetcash;
	}
}
