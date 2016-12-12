package bean;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;

public class MazeTestNoView {
	private MazeGridNoView grid[][];
	private int rows;
	private int cols;
	private List<String> willVisit;
	private List<String> visited;
	private LinkedList<String> comed;
	private long startTime;
	private long endTime;
	private int[] WallRows = null;
	private int[] WallCols = null;
	private int[] Items = null;
	private List<MazeEnd> endj;
	private List<MazeEnd> orderlist;
	private MazeEnd upLayerObj = null;//用于上楼

	private int comeX = 6;
	private int comeY = 0;
	
	public MazeTestNoView(int[] _WallRows, int[] _WallCols, int[] _Items) {
		rows = 8;
		cols = 16;
		WallRows = _WallRows;
		WallCols = _WallCols;
		Items = _Items;
		willVisit = new ArrayList<String>();
		visited = new ArrayList<String>();
		comed = new LinkedList<String>();
		init();
	}

	public MazeEnd getUpLayerObj() {
		return upLayerObj;
	}

	public List<MazeEnd> getOrderlist() {
		return orderlist;
	}

	public int getEndjSize() {
		return endj.size();
	}

	private boolean isOdd(int i) {
		return i % 2 != 0;
	}

	private void init() {
		endj = new Vector<MazeEnd>();
		orderlist = new Vector<MazeEnd>();		
		grid = new MazeGridNoView[rows][cols];
		int[] change_cols = new int[24];
		int _cols = 0;
		for(int ix=0;ix<8;ix++){
			change_cols[ix] = WallCols[_cols];
			_cols++;
			change_cols[ix+8] = WallCols[_cols];
			_cols++;
			change_cols[ix+16] = WallCols[_cols];
			_cols++;
		}
		
		int _rows = 0;
		int _colsx = 0;
		int _itemsx = 0;
		for (int i = 0; i < grid.length; i++){
			for (int j = 0; j < grid[i].length; j++) {
				if (isOdd(i) && isOdd(j)){
					grid[i][j] = new MazeGridNoView(false);
				}else{
					if(i % 2 == 0 && isOdd(j) && j<15){
						if(_rows<WallRows.length && WallRows[_rows] == 1){
							grid[i][j] = new MazeGridNoView(false);
						}else{
							grid[i][j] = new MazeGridNoView(true);
						}
						_rows++;
					}else if(isOdd(i) && j % 2 ==0 && i<=5){
						if(change_cols[_colsx] == 1){
							grid[i][j] = new MazeGridNoView(false);
						}else{
							grid[i][j] = new MazeGridNoView(true);
						}
						_colsx++;
					}else if(i % 2 == 0 && j % 2 == 0){
						//2表示箱子,3表示怪,5表示楼梯,都表示未打,6表示箱子怪已打 4表示下楼
						//上楼下楼只在[6,0] [0,14]之间
						if(Items[_itemsx]==2 || Items[_itemsx]==3 || Items[_itemsx]==4 || Items[_itemsx]==5){
							grid[i][j] = new MazeGridNoView(true);
							grid[i][j].setEnd(true);
							MazeEnd me = new MazeEnd(i, j, _itemsx, Items[_itemsx]);
							endj.add(me);
						}else{
							grid[i][j] = new MazeGridNoView(true);
						}
						_itemsx++;
					}else{
						grid[i][j] = new MazeGridNoView(true);
					}
				}
			}
		}
		
		java.util.Iterator it = endj.iterator();
		while(it.hasNext()){
			MazeEnd it_obj = (MazeEnd) it.next();
			//本层迷宫起点
			if(it_obj.getX()==6 && it_obj.getY()==0 && it_obj.getItem()==4){
				grid[6][0].setVisited(true);
				grid[6][0].setPersonCome(true);
				grid[6][0].setStart(true);
				comeX = 6;
				comeY = 0;
				visited.add("0#0");
				endj.remove(it_obj);//从终点列表移除
				it = endj.iterator();
			}else if(it_obj.getX()==0 && it_obj.getY()==14 && it_obj.getItem()==4){
				grid[0][14].setVisited(true);
				grid[0][14].setPersonCome(true);
				grid[0][14].setStart(true);
				comeX = 0;
				comeY = 14;
				visited.add("0#0");
				endj.remove(it_obj);//从终点列表移除
				it = endj.iterator();
			}			
			//本层迷宫终点
			if(it_obj.getX()==6 && it_obj.getY()==0 && it_obj.getItem()==5){
				grid[6][0].setEnd(true);
				upLayerObj = new MazeEnd(it_obj);//终点
				endj.remove(it_obj);//从终点列表移除
				it = endj.iterator();
			}else if(it_obj.getX()==0 && it_obj.getY()==14 && it_obj.getItem()==5){
				grid[0][14].setEnd(true);
				upLayerObj = new MazeEnd(it_obj);//终点
				endj.remove(it_obj);//从终点列表移除
				it = endj.iterator();
			}
		}
	}

	/**
	 * 走迷宫
	 * 
	 * @param mazeGrid
	 * @param x
	 * @param y
	 */
	private String goMaze(MazeGridNoView mazeGrid[][], int x, int y) {
		int comeX = 0;
		int comeY = 0;
		// left
		if (x - 1 >= 0) {
			if (mazeGrid[x - 1][y].isMark()) {
				if (!comed.contains((x - 1) + "#" + y))
					willVisit.add((x - 1) + "#" + y);
			}
		}
		// right
		if (x + 1 < rows) {
			if (mazeGrid[x + 1][y].isMark()) {
				if (!comed.contains((x + 1) + "#" + y))
					willVisit.add((x + 1) + "#" + y);
			}
		}
		// up
		if (y - 1 >= 0) {
			if (mazeGrid[x][y - 1].isMark()) {
				if (!comed.contains(x + "#" + (y - 1)))
					willVisit.add(x + "#" + (y - 1));
			}
		}
		// down
		if (y + 1 < cols) {
			if (mazeGrid[x][y + 1].isMark()) {
				if (!comed.contains(x + "#" + (y + 1)))
					willVisit.add(x + "#" + (y + 1));
			}
		}
		if (!willVisit.isEmpty()) {
			int visit = (int) (Math.random() * willVisit.size());
			String id = willVisit.get(visit);
			comeX = Integer.parseInt(id.split("#")[0]);
			comeY = Integer.parseInt(id.split("#")[1]);
			mazeGrid[x][y].setPersonCome(false);
			mazeGrid[comeX][comeY].setPersonCome(true);
			willVisit.clear();
			comed.add(x + "#" + y);
		} else {
			if (!comed.isEmpty()) {
				String id = comed.removeLast();
				comeX = Integer.parseInt(id.split("#")[0]);
				comeY = Integer.parseInt(id.split("#")[1]);
				mazeGrid[x][y].setPersonCome(false);
				mazeGrid[comeX][comeY].setPersonCome(true);
				comed.addFirst(x + "#" + y);
			}
		}
		return comeX + "#" + comeY;
	}
	
	/**
	 * 开始走迷宫
	 * */
	public void StartMaze(){
		startTime = System.currentTimeMillis();
		int delay = 100;
		int period = 50;// 循环间隔
		java.util.Timer timer = new java.util.Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				MazeEnd obj = null;
				boolean flag = true;
				java.util.Iterator it = endj.iterator();
				while(it.hasNext()){
					obj = (MazeEnd) it.next();
					if (grid[obj.getX()][obj.getY()].isPersonCome()) {
						endTime = System.currentTimeMillis();
//						System.out.print("comeX:"+comeX+" comeY:"+comeY+" ");
//						System.out.print(" item:"+obj.getItem()+" itemindex:"+obj.getItemindex()+" ");
//						System.out.println("找到1个节点，耗时"
//								+ (endTime - startTime) / 1000 + "秒");
						endj.remove(obj);
						it = endj.iterator();
						orderlist.add(obj);
						if(endj.size()==0){
							flag = false;
//							System.out.println("迷宫走完");
							this.cancel();
							System.gc();
						}
					}
				}
				if(flag) {
					String id = goMaze(grid, comeX, comeY);
					comeX = Integer.parseInt(id.split("#")[0]);
					comeY = Integer.parseInt(id.split("#")[1]);
				}
			}
		}, delay, period);
	}

	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int[] WallRows = {0,0,0,0,1,0,1,0,1,1,1,0,1,1,1,1,1,1,1,0,0,0,1,0,0,0,0,0};//竖条 7*4 顺序横
		int[] WallCols = {0,0,0,0,0,0,0,0,1,0,0,0,1,0,0,0,0,1,0,1,1,0,0,0};//横条 8*3 顺序竖
		int[] Items = {1,2,2,1,1,1,3,4,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,5,1,1,1,1,1,1,1};//怪物 箱子		
		MazeTestNoView obj = new MazeTestNoView(WallRows, WallCols, Items);
		long end = System.currentTimeMillis();
		System.out.println("使用ArrayList生成迷宫耗时：" + (end - start) + "毫秒");
		//开始走迷宫
		System.out.println("obj.getEndjSize:"+obj.getEndjSize());
		if(obj.getEndjSize()>0)
			obj.StartMaze();
		
	}
}
