package org.doetsch.jaylist;

import java.awt.Color;
import java.awt.Font;
import java.net.URL;

import javax.swing.Icon;
import javax.swing.ImageIcon;

class Constants {

	static final int FRAME_WIDTH = 266;
	static final int FRAME_HEIGHT = 500;
	
	static final int ITEMPANEL_WIDTH = 256;
	static final int ITEMPANEL_HEIGHT = 32;
	static final int HEADER_HEIGHT = 32;
	static final int ITEMPANEL_HEIGHT_EXPANDED = 90;

	static final Color COLOR_BUTTON_BG = new Color(27, 125, 255);
	static final Color COLOR_BUTTON_FG = Color.WHITE;
	static final Color COLOR_HEADER_BG = new Color(248, 248, 248);
	static final Color COLOR_ITEM = new Color(254, 254, 254);
	static final Color COLOR_HIGHLIGHT = new Color(228, 241, 254);
	static final Color COLOR_HIGHLIGHT2 = new Color(228, 241, 254);
	static final Color COLOR_GRID = new Color(128, 128, 128);
	
	static final ImageIcon ICON_APP = new ImageIcon(
			ItemPanel.class.getResource("resources/icon_32.png"));
	
	static final int ICON_FLAG_COUNT = 2;
	static final ImageIcon ICON_CHECKED = new ImageIcon(
			ItemPanel.class.getResource("resources/set3/checked.png"));
	static final ImageIcon ICON_UNCHECKED = new ImageIcon(
			Constants.class.getResource("resources/set3/unchecked.png"));
	static final ImageIcon ICON_URGENT = new ImageIcon(
			Constants.class.getResource("resources/set2/urgent_32x32.png"));
	static final ImageIcon ICON_ISSUE = new ImageIcon(
			Constants.class.getResource("resources/set2/question_32x32.png"));
	static final ImageIcon ICON_EXPAND = new ImageIcon(
			Constants.class.getResource("resources/set2/expand.png"));
	static final ImageIcon ICON_COLLAPSE = new ImageIcon(
			Constants.class.getResource("resources/set2/collapse.png"));
	static final Icon ICON_BIRD = new ImageIcon(
			Constants.class.getResource("resources/set3/bird.png"));
	static final Icon ICON_SAVEAS = new ImageIcon(
			Constants.class.getResource("resources/set2/saveas.png"));
	static final Icon ICON_ADD = new ImageIcon(
			Constants.class.getResource("resources/set3/menu-add.png"));
	static final Icon ICON_REMOVE = new ImageIcon(
			Constants.class.getResource("resources/set3/menu-remove.png"));
	
	static final Font FONT_ITEM_TITLE = new Font("Arial", Font.PLAIN, 14);
	static final Font FONT_HEADER = new Font("Arial", Font.PLAIN, 16);
	static final Font FONT_ITEM_DESCRIPTIOn = new Font("Arial", Font.PLAIN, 12);
	static final Font LAUNCHER_FONT = new Font("Arial", Font.PLAIN, 16);
	static final Color LAUNCHER_COLOR_BUTTON = new Color(54, 138, 230);
	static final Color LAUNCHER_COLOR_BG = new Color(246, 251, 255);
	static final URL XMl_NEW_LIST =
			ListFrame.class.getResource("xml/new.xml");
	static final URL ICON_MENU_NEW = ListFrame.class.getResource(
			"/org/doetsch/jaylist/resources/set2/new_20x20.png");
	static final URL ICON_MENU_OPEN = ListFrame.class.getResource(
			"/org/doetsch/jaylist/resources/set2/open_20x20.png");
	static final URL ICON_MENU_SAVE = ListFrame.class.getResource(
			"/org/doetsch/jaylist/resources/set2/save_20x20.png");
	static final URL XML_LIST_SCHEMA = 
			ListMarshall.class.getResource("xml/list.xsd");
	

	

}
