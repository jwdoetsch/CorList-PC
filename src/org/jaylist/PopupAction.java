package org.jaylist;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPopupMenu;

class PopupAction extends MouseAdapter {

	private JPopupMenu popup;
	private Point offset;
	
	PopupAction (JPopupMenu popupMenu, Point offset) {
		this.popup = popupMenu;
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
