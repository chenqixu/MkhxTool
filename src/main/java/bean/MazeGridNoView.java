package bean;

public class MazeGridNoView {
	private boolean mark;// 标记是否是通路，TRUE为通路，FALSE为不通
	private boolean isVisited;// 标记是否是访问过的,这是在生成迷宫的时候判断的。
	private boolean isPersonCome;// 标记是否已经走过
	private boolean isStart;// 判断是否为入口
	private boolean isEnd;// 判断是否为出口

	public MazeGridNoView() {
	}

	public MazeGridNoView(boolean mark) {
		this.mark = mark;
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
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
