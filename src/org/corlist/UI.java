package org.corlist;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;


class UI {

	static final int FRAME_WIDTH = 266;
	static final int FRAME_HEIGHT = 500;
	
	static final int ITEMPANEL_WIDTH = 256;
	static final int ITEMPANEL_HEIGHT = 32;
	static final int HEADER_HEIGHT = 32;
	static final int ITEMPANEL_HEIGHT_EXPANDED = 90;

	static final Color COLOR_BUTTON_BG = new Color(27, 125, 255);
	static final Color COLOR_BUTTON_FG = Color.WHITE;
	static final Color COLOR_HEADER_BG = new Color(251, 251, 251);
	static final Color COLOR_ITEM = new Color(254, 254, 254);
	static final Color COLOR_HIGHLIGHT = new Color(228, 241, 254);
	static final Color COLOR_HIGHLIGHT2 = new Color(228, 241, 254);
	static final Color COLOR_GRID = new Color(239, 239, 239);
	
	static final int ICON_FLAG_COUNT = 2;
	
	static final ImageIcon ICON_APP = new ImageIcon(
			UI.class.getResource("assets/gfx/corlist_icon.png"));
	

	static final ImageIcon ICON_APP_BLUE = new ImageIcon(
			UI.class.getResource("assets/gfx/app_blue.png"));
	static final ImageIcon ICON_APP_GREEN = new ImageIcon(
			UI.class.getResource("assets/gfx/app_green.png"));
	static final ImageIcon ICON_APP_PINK = new ImageIcon(
			UI.class.getResource("assets/gfx/app_pink.png"));
	static final ImageIcon ICON_APP_ORANGE = new ImageIcon(
			UI.class.getResource("assets/gfx/app_orange.png"));

	
	static final ImageIcon ICON_JAYAPP = new ImageIcon(
			UI.class.getResource("assets/gfx/app_blue_60x60.png"));
	
	static final URL URL_ICON_APP = 
			UI.class.getResource("assets/gfx/corlist_icon.png");
	static final URL URL_ICON_JAYAPP = 
			UI.class.getResource("assets/gfx/apps_50x50.png");

	static final ImageIcon ICON_CHECKED = new ImageIcon(
			UI.class.getResource("assets/gfx/checked.png"));
	static final ImageIcon ICON_UNCHECKED = new ImageIcon(
			UI.class.getResource("assets/gfx/unchecked.png"));
	static final ImageIcon ICON_URGENT = new ImageIcon(
			UI.class.getResource("assets/gfx/urgent_32x32.png"));
	static final ImageIcon ICON_ISSUE = new ImageIcon(
			UI.class.getResource("assets/gfx/question_32x32.png"));
	static final ImageIcon ICON_EXPAND = new ImageIcon(
			UI.class.getResource("assets/gfx/expand.png"));
	static final ImageIcon ICON_COLLAPSE = new ImageIcon(
			UI.class.getResource("assets/gfx/collapse.png"));
	static final ImageIcon ICON_BIRD = new ImageIcon(
			UI.class.getResource("assets/gfx/bird.png"));
	static final ImageIcon ICON_SAVEAS = new ImageIcon(
			UI.class.getResource("assets/gfx/saveas.png"));
	static final ImageIcon ICON_ADD = new ImageIcon(
			UI.class.getResource("assets/gfx/menu-add.png"));
	static final ImageIcon ICON_REMOVE = new ImageIcon(
			UI.class.getResource("assets/gfx/menu-remove.png"));
	static final ImageIcon ICON_HIDDEN = new ImageIcon(
			UI.class.getResource("assets/gfx/checked_32x8.png"));
	static final ImageIcon ICON_HIDDEN2 = new ImageIcon(
			UI.class.getResource("assets/gfx/hidden_32x8.png"));
	
	
	static final ImageIcon ICON_MENU_NEW = new ImageIcon(
			UI.class.getResource("assets/gfx/new_20x20.png"));
	static final ImageIcon ICON_MENU_OPEN = new ImageIcon(
			UI.class.getResource("assets/gfx/open_20x20.png"));
	static final ImageIcon ICON_MENU_SAVE = new ImageIcon(
			UI.class.getResource("assets/gfx/save_20x20.png"));
	
	static final Icon ICON_LAUNCHER_NEW = new ImageIcon(
			UI.class.getResource("assets/gfx/new_16x16.png"));

	static final Font FONT_ITEM_TITLE = new Font("Arial", Font.PLAIN, 14);
	static final Font FONT_HEADER = new Font("Arial", Font.PLAIN, 16);
	static final Font FONT_ITEM_DESCRIPTIOn = new Font("Arial", Font.PLAIN, 12);
	static final Font LAUNCHER_FONT = new Font("Arial", Font.PLAIN, 16);
	static final Color LAUNCHER_COLOR_BUTTON = new Color(54, 138, 230);
	static final Color LAUNCHER_COLOR_BG = new Color(246, 251, 255);
	static final URL XMl_NEW_LIST =
			UI.class.getResource("assets/xml/new.xml");

	static final URL XML_LIST_SCHEMA = 
			UI.class.getResource("assets/xml/list.xsd");
	static final ImageIcon ICON_TRAY = new ImageIcon(
			UI.class.getResource("assets/gfx/systray_16x16.png"));
	
	
}
