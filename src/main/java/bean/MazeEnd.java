package bean;

public class MazeEnd {
	private int x = 0;
	private int y = 0;
	private boolean flag = false;
	private int item = 4;//2��ʾ����,3��ʾ��,5��ʾ¥��,����ʾδ��,6��ʾ���ӹ��Ѵ� 4��ʾ��¥
	private int itemindex = 1;//�Թ����Ӵ�0��ʼ 0��λ���ڵ�һ����̨
	
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
