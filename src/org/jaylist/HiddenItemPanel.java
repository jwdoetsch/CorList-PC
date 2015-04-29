package org.jaylist;

import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class HiddenItemPanel extends JPanel {
	private JLabel label;

	private JTable parentTable;
	private int rowIndex;
	
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
		setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
		setBackground(UI.COLOR_ITEM);
		this.label = new JLabel("");
		this.label.setIcon(UI.ICON_HIDDEN);
		this.label.setPreferredSize(new Dimension(32, 8));
		add(this.label);
	}
	
	private void syncRowHeight () {
	
		int height = UI.ICON_HIDDEN.getIconHeight() + 1;
		
		if (parentTable.getRowHeight(this.rowIndex) != height) {
			parentTable.setRowHeight(this.rowIndex, height);
		}
	}

}
