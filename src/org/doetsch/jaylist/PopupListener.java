package org.doetsch.jaylist;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

class PopupListener extends MouseAdapter {

	private JPopupMenu popup;
	private Point offset;
	
	PopupListener (JPopupMenu popup, Point offset) {
		this.popup = popup;
		this.offset = offset;
	}
	
	@Override
	public void mouseClicked (MouseEvent e) {
		tryPopup(e);
	}

	private void tryPopup (MouseEvent e) {
		if ((e.getButton() == 2) || (e.getButton() == 3)) {
			//popup.show(e.getComponent(), e.getX() - 56, e.getY() - 12);
			popup.show(e.getComponent(), e.getX() + offset.x, e.getY() + offset.y);
		}
	}	
}
