package bean;

public class MazeGridNoView {
	private boolean mark;// ����Ƿ���ͨ·��TRUEΪͨ·��FALSEΪ��ͨ
	private boolean isVisited;// ����Ƿ��Ƿ��ʹ���,�����������Թ���ʱ���жϵġ�
	private boolean isPersonCome;// ����Ƿ��Ѿ��߹�
	private boolean isStart;// �ж��Ƿ�Ϊ���
	private boolean isEnd;// �ж��Ƿ�Ϊ����

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
