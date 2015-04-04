package org.doetsch.jaylist;

import javax.swing.JPanel;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

import java.awt.BorderLayout;

import javax.swing.SwingConstants;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.net.URL;
import javax.swing.border.EtchedBorder;

public class LauncherPanel extends JPanel {
	private JButton btnNewButton;
	private URL path;

	/**
	 * Create the panel.
	 */
	public LauncherPanel (LauncherModel model, boolean isBlank) {
		initComponents(model, isBlank);
	}
	private void initComponents (LauncherModel model, boolean isBlank) {
		setOpaque(false);
		setBorder(new EmptyBorder(4, 4, 4, 4));
		setSize(new Dimension(104, 104));
		setMaximumSize(new Dimension(104, 104));
		setMinimumSize(new Dimension(104, 104));
		setPreferredSize(new Dimension(104, 104));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		this.btnNewButton = new JButton("<html><p>" + model.title + "</p></html>");
		this.btnNewButton.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.btnNewButton.setBackground(JLConstants.LAUNCHER_COLOR_BUTTON);
		this.btnNewButton.setForeground(Color.WHITE);
		this.btnNewButton.setFont(JLConstants.LAUNCHER_FONT);
		
		this.btnNewButton.setMaximumSize(new Dimension(96, 96));
		this.btnNewButton.setMinimumSize(new Dimension(96, 96));
		this.btnNewButton.setPreferredSize(new Dimension(96, 96));
		this.btnNewButton.setMargin(new Insets(6, 6, 6, 6));
		this.btnNewButton.setFont(new Font("Arial", Font.PLAIN, 16));
		this.btnNewButton.setHorizontalAlignment(SwingConstants.LEFT);
		this.btnNewButton.setVerticalAlignment(SwingConstants.TOP);
		add(this.btnNewButton);
		
		this.btnNewButton.setVisible(!isBlank);
		this.path = model.path;
	}

}
