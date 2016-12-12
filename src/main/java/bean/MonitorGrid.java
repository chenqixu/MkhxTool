package bean;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import com.frame.LogThread;
import com.frame.MainClient;

import client.Http360ClientThread;
import client.HttpDuokuClientThread;
import client.HttpMysticalcardClientThread;

public class MonitorGrid extends Canvas implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5199647754964447688L;
	private int type = 0;//0:��ɫ ��ʼ�� 1:������ ��ɫ 2:�ɹ� ��ɫ 3:ʧ�� ��ɫ
	private String nick = "";//�û���
	private StringBuffer sblog = null;
	private Object obj = null;
	private LogThread lt = null;
	private MainClient mc = null;

	/**
	 * ����
	 * */
	public MonitorGrid(String _nick, int width, int height, Object _obj){
		this.nick = _nick;
		this.setType(0);
		this.setSize(width, height);
		this.addMouseListener(this);
		this.obj = _obj;
	}

	/**
	 * �ı�״̬
	 * */
	public void setType(int type) {
		this.type = type;
		switch (type) {
		case 0:
			this.setBackground(Color.DARK_GRAY);
			break;
		case 1:
			this.setBackground(Color.YELLOW);
			break;
		case 2:
			this.setBackground(Color.GREEN);
			break;
		case 3:
			this.setBackground(Color.RED);
			break;
		default:
			break;
		}
	}
	
	/**
	 * �����˺���
	 * */
	public void paint(Graphics g) {
		g.setColor(Color.WHITE);
		g.drawString(this.nick, 0, this.getHeight()/2);
	}

	/**
	 * ������¼�
	 * */
	public void mouseClicked(MouseEvent e) {
		this.mc.setLogNick(this.nick);
		if(obj instanceof Http360ClientThread){
			this.sblog = ((Http360ClientThread)obj).getSblog();
		}else if(obj instanceof HttpDuokuClientThread){
			this.sblog = ((HttpDuokuClientThread)obj).getSblog();
		}else if(obj instanceof HttpMysticalcardClientThread){
			this.sblog = ((HttpMysticalcardClientThread)obj).getSblog();
		}
		if(this.lt!=null){
			this.lt.setThreadobj(this.obj);
			this.lt.setSblog(this.sblog);
			if(!this.lt.isFlag()){//�����߳̽���
				System.out.println("�����߳̽��� ׼������");
				this.lt.setFlag(true);
				new Thread(lt).start();
			}
		}
	}

	public void mouseEntered(MouseEvent e) {		
	}

	public void mouseExited(MouseEvent e) {		
	}

	public void mousePressed(MouseEvent e) {		
	}

	public void mouseReleased(MouseEvent e) {		
	}
	
	/**
	 * ���ü���߳�
	 * */
	public void setLog(LogThread _lt){
		this.lt = _lt;
	}
	
	/**
	 * ����������,����������־������
	 * */
	public void setMainClient(MainClient _mc){
		this.mc = _mc;
	}
}
