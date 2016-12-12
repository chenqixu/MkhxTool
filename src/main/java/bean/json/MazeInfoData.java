package bean.json;

public class MazeInfoData {
	String name;
	int boxnum;
	int monsternum;
	int remainboxnum;
	int remainmonsternum;
	int layer;
	int totallayer;
	MazeInfoMap map;
	public int getBoxnum() {
		return boxnum;
	}
	public void setBoxnum(int boxnum) {
		this.boxnum = boxnum;
	}
	public int getLayer() {
		return layer;
	}
	public void setLayer(int layer) {
		this.layer = layer;
	}
	public MazeInfoMap getMap() {
		return map;
	}
	public void setMap(MazeInfoMap map) {
		this.map = map;
	}
	public int getMonsternum() {
		return monsternum;
	}
	public void setMonsternum(int monsternum) {
		this.monsternum = monsternum;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getRemainboxnum() {
		return remainboxnum;
	}
	public void setRemainboxnum(int remainboxnum) {
		this.remainboxnum = remainboxnum;
	}
	public int getRemainmonsternum() {
		return remainmonsternum;
	}
	public void setRemainmonsternum(int remainmonsternum) {
		this.remainmonsternum = remainmonsternum;
	}
	public int getTotallayer() {
		return totallayer;
	}
	public void setTotallayer(int totallayer) {
		this.totallayer = totallayer;
	}
}
