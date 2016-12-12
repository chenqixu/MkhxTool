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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

//�Թ�
@SuppressWarnings("serial")
public class Maze extends JFrame implements ActionListener {
	private JPanel panel;
	private JPanel northPanel;
	private JPanel centerPanel;
	private MazeGrid grid[][];
	private JButton restart;
	private JButton dostart;
	private int rows;// rows ��colsĿǰ�ݶ�ֻ��������
	private int cols;
	private List<String> willVisit;
	private List<String> visited;
	private LinkedList<String> comed;
	private long startTime;
	private long endTime;

	public Maze() {
		rows = 25;
		cols = 25;
		willVisit = new ArrayList<String>();
		visited = new ArrayList<String>();
		comed = new LinkedList<String>();
		init();
		this.setTitle("���ݷ�--���Թ�");
		this.add(panel);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void init() {
		panel = new JPanel();
		northPanel = new JPanel();
		centerPanel = new JPanel();
		panel.setLayout(new BorderLayout());
		restart = new JButton("���������Թ�");
		dostart = new JButton("��ʼ���Թ�");
		grid = new MazeGrid[rows][cols];

		centerPanel.setLayout(new GridLayout(rows, cols, 1, 1));
		centerPanel.setBackground(new Color(0, 0, 0));
		northPanel.add(restart);
		northPanel.add(dostart);

		dostart.addActionListener(this);
		restart.addActionListener(this);
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++) {
				if (j % 2 == 0 && i % 2 == 0)
					grid[i][j] = new MazeGrid(true, 20, 20);
				else
					grid[i][j] = new MazeGrid(false, 20, 20);
			}
		grid[0][0].setVisited(true);
		grid[0][0].setPersonCome(true);
		grid[0][0].setStart(true);
		visited.add("0#0");
		grid[rows - 1][cols - 1].setEnd(true);
		grid = createMap(grid, 0, 0);
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j].repaint();
				centerPanel.add(grid[i][j]);
			}

		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
	}

	/**
	 * �����Թ�
	 * 
	 * @param mazeGrid
	 * @param x
	 * @param y
	 * @return
	 */
	public MazeGrid[][] createMap(MazeGrid mazeGrid[][], int x, int y) {
		int visitX = 0;
		int visitY = 0;
		if (x - 2 >= 0) {
			if (!mazeGrid[x - 2][y].isVisited()) {
				willVisit.add((x - 2) + "#" + y);
			}
		}
		if (x + 2 < cols) {
			if (!mazeGrid[x + 2][y].isVisited()) {
				willVisit.add((x + 2) + "#" + y);
			}
		}
		if (y - 2 >= 0) {
			if (!mazeGrid[x][y - 2].isVisited()) {
				willVisit.add(x + "#" + (y - 2));
			}
		}
		if (y + 2 < rows) {
			if (!mazeGrid[x][y + 2].isVisited()) {
				willVisit.add(x + "#" + (y + 2));
			}
		}
		if (!willVisit.isEmpty()) {
			int visit = (int) (Math.random() * willVisit.size());
			String id = willVisit.get(visit);
			visitX = Integer.parseInt(id.split("#")[0]);
			visitY = Integer.parseInt(id.split("#")[1]);
			mazeGrid[(visitX + x) / 2][(visitY + y) / 2].setMark(true);

			mazeGrid[visitX][visitY].setVisited(true);
			if (!visited.contains(id)) {// �������ӵ��ѷ�����ȥ
				visited.add(id);
			}
			willVisit.clear();
			createMap(mazeGrid, visitX, visitY);
		} else {
			if (!visited.isEmpty()) {
				String id = visited.remove(visited.size() - 1);// ȡ�����һ��Ԫ��
				visitX = Integer.parseInt(id.split("#")[0]);
				visitY = Integer.parseInt(id.split("#")[1]);
				mazeGrid[visitX][visitY].setVisited(true);
				createMap(mazeGrid, visitX, visitY);
			}
		}
		return mazeGrid;
	}

	/**
	 * ���Թ�
	 * 
	 * @param mazeGrid
	 * @param x
	 * @param y
	 */
	public String goMaze(MazeGrid mazeGrid[][], int x, int y) {
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
		if (x + 1 < cols) {
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
		if (y + 1 < rows) {
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

	int comeX = 0;
	int comeY = 0;

	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("���������Թ�")) {
			refreshMap(grid);
		} else if (e.getActionCommand().equals("��ʼ���Թ�")) {
			startTime = System.currentTimeMillis();
			dostart.setVisible(false);
			restart.setText("��ֹˢ��");
			int delay = 1000;
			int period = 500;// ѭ�����
			java.util.Timer timer = new java.util.Timer();
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					if (grid[rows - 1][cols - 1].isPersonCome()) {
						endTime = System.currentTimeMillis();
						JOptionPane.showMessageDialog(null, "�Ѿ��߳��Թ�����ʱ"
								+ (endTime - startTime) / 1000 + "��", "��Ϣ��ʾ",
								JOptionPane.ERROR_MESSAGE);
						this.cancel();
						restart.setText("���������Թ�");
					} else {
						String id = goMaze(grid, comeX, comeY);
						comeX = Integer.parseInt(id.split("#")[0]);
						comeY = Integer.parseInt(id.split("#")[1]);
					}
				}
			}, delay, period);
		}
	}

	/**
	 * ˢ�µ�ͼ
	 */
	public void refreshMap(MazeGrid mazeGrid[][]) {
		comeX = 0;
		comeY = 0;
		willVisit.clear();
		visited.clear();
		comed.clear();
		this.remove(panel);
		init();
		this.add(panel);
		this.pack();
		this.setVisible(true);
	}

	public static void main(String args[]) {
		long start = System.currentTimeMillis();
		new Maze();
		long end = System.currentTimeMillis();
		System.out.println("ʹ��ArrayList�����Թ���ʱ��" + (end - start) + "����");
	}
}
