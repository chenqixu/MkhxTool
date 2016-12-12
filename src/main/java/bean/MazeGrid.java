package bean;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class MazeGrid extends Canvas {
	private boolean mark;// ����Ƿ���ͨ·��TRUEΪͨ·��FALSEΪ��ͨ
	private boolean isVisited;// ����Ƿ��Ƿ��ʹ���,�����������Թ���ʱ���жϵġ�
	private boolean isPersonCome;// ����Ƿ��Ѿ��߹�
	private boolean isStart;// �ж��Ƿ�Ϊ���
	private boolean isEnd;// �ж��Ƿ�Ϊ����

	public MazeGrid() {
		this.setBackground(new Color(120, 0, 0));
		this.setSize(25, 25);
	}

	public MazeGrid(boolean mark, int width, int height) {
		this.mark = mark;
		this.setSize(width, height);
		if (mark == true) {
			this.setBackground(new Color(255, 255, 255));
		} else {
			this.setBackground(new Color(120, 0, 0));
		}
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

	public void paint(Graphics g) {
		if (this.mark) {
			if (this.isStart || this.isEnd) {
				this.setBackground(new Color(255,0,0));
			} else
				this.setBackground(new Color(255, 255, 255));
		} else {
			this.setBackground(new Color(120, 0, 0));
		}
		if (this.isPersonCome) {
			g.setColor(Color.BLACK);
			g.fillOval(2, 2, 15, 15);
//			g.drawString("ab", 0, 15);
		}

	}

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public boolean isPersonCome() {
		return isPersonCome;
	}

	public void setPersonCome(boolean isPersonCome) {
		this.isPersonCome = isPersonCome;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}
}
