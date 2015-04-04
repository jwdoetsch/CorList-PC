package org.doetsch.jaylist;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

class PopupListener extends MouseAdapter {

	JPopupMenu popup;
	
	PopupListener (JPopupMenu popup) {
		this.popup = popup;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		tryPopup(e);
	}

	private void tryPopup(MouseEvent e) {
		if ((e.getButton() == 2) || (e.getButton() == 3)) {
			popup.show(e.getComponent(), e.getX() - 56, e.getY() - 12);		
		}
	}	
}
