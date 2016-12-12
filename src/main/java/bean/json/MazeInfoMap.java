package bean.json;

public class MazeInfoMap {
	boolean isfinish;
	int[] wallrows;
	int[] wallcols;
	int[] items;
	public boolean isIsfinish() {
		return isfinish;
	}
	public void setIsfinish(boolean isfinish) {
		this.isfinish = isfinish;
	}
	public int[] getItems() {
		return items;
	}
	public void setItems(int[] items) {
		this.items = items;
	}
	public int[] getWallcols() {
		return wallcols;
	}
	public void setWallcols(int[] wallcols) {
		this.wallcols = wallcols;
	}
	public int[] getWallrows() {
		return wallrows;
	}
	public void setWallrows(int[] wallrows) {
		this.wallrows = wallrows;
	}
}
