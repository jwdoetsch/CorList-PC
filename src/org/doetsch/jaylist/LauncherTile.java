package org.doetsch.jaylist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * LauncherTile is a custom stylized JButton utilized
 * by Launcher. 
 * 
 * @author Jacob Wesley Doetsch
 */
@SuppressWarnings("serial")
class LauncherTile extends JButton {
	
	boolean isHidden;
	
	LauncherTile (String text, boolean isHidden) {
		super();
		this.isHidden = isHidden;
		init();
	}
	
	/*
	 * Stylizes the LauncherButton and defines behavior
	 */
	private void init () {
		
		this.setVisible(!isHidden);
		this.setBackground(UI.LAUNCHER_COLOR_BUTTON);
		
		//don't draw the icon/text border
		this.setFocusPainted(false);
		this.setPreferredSize(new Dimension(100, 100));
		this.setBorder(new EmptyBorder(6, 6, 6, 6));
		this.setMinimumSize(new Dimension(100, 100));
		this.setMaximumSize(new Dimension(100, 100));
		
		//set up font and text alignment
		this.setForeground(Color.WHITE);
		this.setFont(new Font("Arial", Font.BOLD, 14));
		this.setVerticalTextPosition(SwingConstants.TOP);
		this.setHorizontalTextPosition(SwingConstants.LEFT);
	}
	
	/*
	 * Enables the button text to wrap by utilizing
	 * JButton's HTML paragraph support  
	 */
	public void setText (String text) {
		super.setText("<html><p>" + text + "</html></p>");		
	}

}
