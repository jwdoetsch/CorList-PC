package org.corlist;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.JPopupMenu;

/**
 * PopupAction is a helper class that encapsulates a
 * JPopupMenu and a location offset so that the 
 * JPopupMenu can be invoked and places at a location
 * relative to the invoking mouse-click position.
 * 
 * @author Jacob Wesley Doetsch
 */
class UIOffsetPopupAction extends MouseAdapter {

	private JPopupMenu popup;
	private Point offset;
	
	/**
	 * Instantiates a new PopupAction encapsulating the 
	 * given JPopupMenu and that will place the JPopupMenu
	 * at the given offset relative to the invoking mouse-
	 * click.
	 * 
	 * @param popupMenu the JPopupMenu
	 * @param offset the menu's location relative to the
	 * invoking mouse-click
	 */
	UIOffsetPopupAction (JPopupMenu popupMenu, Point offset) {
		this.popup = popupMenu;
		this.offset = offset;
	}
	
	@Override
	public void mouseClicked (MouseEvent e) {
		showPopup(e);
	}
	
	/*
	 * Attempts to show the popup menu and place it at the
	 * location of the mouse-click and offset by the offset
	 * given at construction
	 */
	private void showPopup (MouseEvent e) {
		if ((e.getButton() == 2) || (e.getButton() == 3)) {
			popup.show(e.getComponent(), e.getX() + offset.x, e.getY() + offset.y);
			popup.requestFocus();
		}
	}	
}
