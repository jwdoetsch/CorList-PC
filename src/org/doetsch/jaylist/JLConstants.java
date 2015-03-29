package org.doetsch.jaylist;

import java.awt.Color;
import javax.swing.ImageIcon;

public class JLConstants {


	
	public static final Color HIGHLIGHT_COLOR = new Color(210, 225, 255);
	public static final Color HIGHLIGHT_COLOR2 = new Color(210, 225, 255);

	public static final int DEFAULT_FRAME_WIDTH = 271;
	public static final int DEFAULT_FRAME_HEIGHT = 512;
	
	public static final ImageIcon ICON_UNCHECKED =
			new ImageIcon(JLConstants.class.getResource("resources/unchecked.png"));
	public static final ImageIcon ICON_CHECKED =
			new ImageIcon(JLConstants.class.getResource("resources/checked2.png"));
	public static final ImageIcon ICON_URGENT =
			new ImageIcon(JLConstants.class.getResource("resources/urgent2.png"));
	public static final ImageIcon ICON_QUESTION =
			new ImageIcon(JLConstants.class.getResource("resources/question.png"));
	
	public static final ImageIcon ICON_APP =
			new ImageIcon(JLConstants.class.getResource("resources/icon_32.png"));
	public static final ImageIcon ICON_ADD =
			new ImageIcon(JLConstants.class.getResource(
					"/org/doetsch/jaylist/resources/add.png"));
	public static final ImageIcon ICON_REMOVE =
			new ImageIcon(JLConstants.class.getResource(
					"/org/doetsch/jaylist/resources/remove.png"));
}
