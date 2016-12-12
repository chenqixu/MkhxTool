package bean;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class MazeTest extends JFrame 
//implements ActionListener 
{
	private JPanel panel;
	private JPanel northPanel;
	private JPanel centerPanel;
	private MazeGrid grid[][];
	private JButton restart;
	private JButton dostart;
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
	
	private boolean is_visible = false;//用于调试 是否可见
	
	private MazeTest MazeTestobj =  this;
	
	public MazeTest(int[] _WallRows, int[] _WallCols, int[] _Items) {
		rows = 8;
		cols = 16;
		WallRows = _WallRows;
		WallCols = _WallCols;
		Items = _Items;
		willVisit = new ArrayList<String>();
		visited = new ArrayList<String>();
		comed = new LinkedList<String>();
		init();
		this.setTitle("回溯法--走迷宫");
		this.add(panel);
		this.pack();//pack(): 调整此窗口的大小，以适合其子组件的首选大小和布局。
		//如果该窗口和/或其所有者仍不可显示，则两者在计算首选大小之前变得可显示。在计算首选大小之后，将会验证该 Window。
//		this.setVisible(true);
//		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void setIs_visible(boolean is_visible) {
		this.is_visible = is_visible;
		this.setVisible(is_visible);
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
		
		panel = new JPanel();
		northPanel = new JPanel();
		centerPanel = new JPanel();
		panel.setLayout(new BorderLayout());
		restart = new JButton("重新生成迷宫");
		dostart = new JButton("开始走迷宫");
		grid = new MazeGrid[rows][cols];

		centerPanel.setLayout(new GridLayout(rows, cols, 1, 1));
		centerPanel.setBackground(new Color(0, 0, 0));
		northPanel.add(restart);
		northPanel.add(dostart);

//		dostart.addActionListener(this);
		//restart.addActionListener(this);
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
					grid[i][j] = new MazeGrid(false, 20, 20);
					centerPanel.add(grid[i][j]);
				}else{
					if(i % 2 == 0 && isOdd(j) && j<15){
						if(_rows<WallRows.length && WallRows[_rows] == 1){
							grid[i][j] = new MazeGrid(false, 20, 20);
						}else{
							grid[i][j] = new MazeGrid(true, 20, 20);
						}
						_rows++;
					}else if(isOdd(i) && j % 2 ==0 && i<=5){
						if(change_cols[_colsx] == 1){
							grid[i][j] = new MazeGrid(false, 20, 20);
						}else{
							grid[i][j] = new MazeGrid(true, 20, 20);
						}
						_colsx++;
					}else if(i % 2 == 0 && j % 2 == 0){
						//2表示箱子,3表示怪,5表示楼梯,都表示未打,6表示箱子怪已打 4表示下楼
						//上楼下楼只在[6,0] [0,14]之间
						if(Items[_itemsx]==2 || Items[_itemsx]==3 || Items[_itemsx]==4 || Items[_itemsx]==5){
							grid[i][j] = new MazeGrid(true, 20, 20);
							grid[i][j].setEnd(true);
							MazeEnd me = new MazeEnd(i, j, _itemsx, Items[_itemsx]);
							endj.add(me);
						}else{
							grid[i][j] = new MazeGrid(true, 20, 20);
						}
						_itemsx++;
					}else{
						grid[i][j] = new MazeGrid(true, 20, 20);
					}
					centerPanel.add(grid[i][j]);
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
		
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
	}

	/**
	 * 走迷宫
	 * 
	 * @param mazeGrid
	 * @param x
	 * @param y
	 */
	private String goMaze(MazeGrid mazeGrid[][], int x, int y) {
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
			mazeGrid[x][y].repaint();
			mazeGrid[comeX][comeY].repaint();
			willVisit.clear();
			comed.add(x + "#" + y);
		} else {
			if (!comed.isEmpty()) {
				String id = comed.removeLast();
				comeX = Integer.parseInt(id.split("#")[0]);
				comeY = Integer.parseInt(id.split("#")[1]);
				mazeGrid[x][y].setPersonCome(false);
				mazeGrid[comeX][comeY].setPersonCome(true);
				mazeGrid[x][y].repaint();
				mazeGrid[comeX][comeY].repaint();
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
		dostart.setVisible(false);
		restart.setText("禁止刷新");
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
							this.cancel();
							restart.setText("重新生成迷宫");
//							System.out.println("迷宫走完");
							//迷宫走完
							if(!is_visible){//如果可见,就不做dispose动作
								MazeTestobj.dispose();
								System.gc();
							}
							/* dispose:
							 * 释放由此 Window、其子组件及其拥有的所有子组件所使用的所有本机屏幕资源。
							 * 即这些 Component 的资源将被破坏，它们使用的所有内存都将返回到操作系统，
							 * 并将它们标记为不可显示。 通过随后调用 pack 或 show 重新构造本机资源，
							 * 可以再次显示 Window 及其子组件。重新创建的 Window 及其子组件的状态
							 * 与释放 Window 时这些对象的状态一致（不考虑这些操作之间的其他更改）。
							 * */
						}
					}
				}
//				if(!it.hasNext()){
//					
//				}
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
		MazeTest obj = new MazeTest(WallRows, WallCols, Items);
		obj.setIs_visible(true);
		long end = System.currentTimeMillis();
		System.out.println("使用ArrayList生成迷宫耗时：" + (end - start) + "毫秒");
		//开始走迷宫
		System.out.println("obj.getEndjSize:"+obj.getEndjSize());
		if(obj.getEndjSize()>0)
			obj.StartMaze();
//		while(obj!=null && obj.getEndjSize()>0){
//			try {
//				Thread.sleep(500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//			if(obj.getEndjSize()==0)break;
//		}
//		System.out.println(obj.getUpLayerObj());
//		System.out.println(obj.getOrderlist().size());
//		System.out.println("obj.getEndjSize:"+obj.getEndjSize());
	}

}
