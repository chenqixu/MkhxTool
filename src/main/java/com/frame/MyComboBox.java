package com.frame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

public class MyComboBox extends JComboBox implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MyComboBox(){
		addItem(new MyCheckBox(false, "Select All"));
        this.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                itemSelected();
            }
        });
	}
	
	private void itemSelected(){
		if (getSelectedItem() instanceof MyCheckBox) {
			if (getSelectedIndex() == 0) {
                selectedAllItem();
            } else {
            	MyCheckBox jcb = (MyCheckBox) getSelectedItem();
                jcb.setBolValue((!jcb.isBolValue()));
                setSelectedIndex(getSelectedIndex());
            }
            SwingUtilities.invokeLater(
                    new Runnable() {
                public void run() {
                    /*ѡ�к���Ȼ���ֵ�ǰ����״̬*/
                    showPopup();
                }
            });
		}
	}
	
	private void selectedAllItem() {
        boolean bl = false;
        for (int i = 0; i < getItemCount(); i++) {
        	MyCheckBox jcb = (MyCheckBox) getItemAt(i);
            if (i == 0) {
                bl = !jcb.isBolValue();
            }
            jcb.setBolValue(bl);
        }
        setSelectedIndex(0);
    }
	
    /*��ȡѡȡ�Ķ���*/
    public Vector getComboVc() {
        Vector<String> vc = new Vector<String>();
        for (int i = 1; i < getItemCount(); i++) {
        	MyCheckBox jcb = (MyCheckBox) getItemAt(i);
            if (jcb.isBolValue()) {
                vc.add(jcb.getValue());
            }
        }
        return vc;
    }
}
