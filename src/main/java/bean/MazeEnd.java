package bean;

public class MazeEnd {
	private int x = 0;
	private int y = 0;
	private boolean flag = false;
	private int item = 4;//2表示箱子,3表示怪,5表示楼梯,都表示未打,6表示箱子怪已打 4表示下楼
	private int itemindex = 1;//迷宫格子从0开始 0的位置在第一个灯台
	
	public MazeEnd(int _x, int _y, int _itemindex, int _item){
		this.x = _x;
		this.y = _y;
		this.itemindex = _itemindex;
		this.item = _item;
	}
	public MazeEnd(MazeEnd _MazeEnd){
		this.x = _MazeEnd.getX();
		this.y = _MazeEnd.getY();
		this.itemindex = _MazeEnd.getItemindex();
		this.item = _MazeEnd.getItem();
	}
	public int getItem() {
		return item;
	}
	public void setItem(int item) {
		this.item = item;
	}
	public int getItemindex() {
		return itemindex;
	}
	public void setItemindex(int itemindex) {
		this.itemindex = itemindex;
	}
	public boolean isFlag() {
		return flag;
	}
	public void setFlag(boolean flag) {
		this.flag = flag;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
}
