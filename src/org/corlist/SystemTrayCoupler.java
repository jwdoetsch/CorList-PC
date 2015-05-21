package org.corlist;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Menu;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;

import javax.swing.AbstractAction;

/**
 * SystemTrayCoupler is a helper class that implements
 * boiler-plate system tray code.
 * 
 * @author Jacob Wesley Doetsch
 */
class SystemTrayCoupler {

	private String title;
	private PopupMenu menu;
	private Image trayIconImage;
	private AbstractAction leftClickAction;
	
	private TrayIcon trayIcon;
	private SystemTray sysTray;
	
	/**
	 * 
	 * @param title 
	 * @param menu
	 * @param trayIconImage
	 * @param leftClickAction
	 * @throws AWTException
	 */
	SystemTrayCoupler (String title, PopupMenu menu,
			Image trayIconImage, AbstractAction leftClickAction)
					throws AWTException {
		
		this.menu = menu;
		this.trayIconImage = trayIconImage;
		this.leftClickAction = leftClickAction;
		
		initialize();
	}
	
	/*
	 * Attempts to add the tray icon to the system tray
	 */
	private void initialize () throws AWTException {
		if (!SystemTray.isSupported()) {
			return;
		}
		
		trayIcon = new TrayIcon(trayIconImage, title);
		sysTray = SystemTray.getSystemTray();
		trayIcon.setPopupMenu(menu);
		trayIcon.addActionListener(leftClickAction);
		sysTray.add(trayIcon);
				
	}
	
	/**
	 * Removes the icon from the system tray
	 */
	void dispose () {
		SystemTray.getSystemTray().remove(trayIcon);
	}
	
}
