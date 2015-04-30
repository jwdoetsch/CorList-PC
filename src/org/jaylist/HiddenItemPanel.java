package org.jaylist;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import java.awt.Component;
import javax.swing.Box;
import java.awt.BorderLayout;

@SuppressWarnings("serial")
public class HiddenItemPanel extends JPanel {
	private JLabel lblChk;

	private JTable parentTable;
	private int rowIndex;
	private JLabel lblMore;
	
	/**
	 * Create the panel.
	 */
	public HiddenItemPanel (JTable parentTable, int rowIndex) {
		this.parentTable = parentTable;	
		this.rowIndex = rowIndex;
		initComponents();
		syncRowHeight();
	}
	
	private void initComponents () {
		setBackground(UI.COLOR_ITEM);
		setLayout(new BorderLayout(0, 0));
		this.lblChk = new JLabel("");
		this.lblChk.setIcon(UI.ICON_HIDDEN);
		this.lblChk.setPreferredSize(new Dimension(32, 8));
		add(this.lblChk, BorderLayout.WEST);
		
		this.lblMore = new JLabel("");
		this.lblMore.setIcon(UI.ICON_HIDDEN2);
		this.lblMore.setPreferredSize(new Dimension(32, 8));
		add(this.lblMore, BorderLayout.EAST);
	}
	
	private void syncRowHeight () {
	
		int height = UI.ICON_HIDDEN.getIconHeight() + 1;
		
		if (parentTable.getRowHeight(this.rowIndex) != height) {
			parentTable.setRowHeight(this.rowIndex, height);
		}
	}

}
