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
import javax.swing.ImageIcon;

public class LauncherPanel extends JPanel {
	private JButton btn;
	private URL path;

	/**
	 * Create the panel.
	 */
	public LauncherPanel (LauncherModel model, boolean isBlank) {
		initComponents(model, isBlank);
	}
	private void initComponents (LauncherModel model, boolean isBlank) {
		int b = 2;
		setOpaque(false);
		setBorder(new EmptyBorder(b, b, b, b));

		setPreferredSize(new Dimension(104, 104));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.btn = new JButton("<html><p><dynamic></p></html>");
		this.btn.setVerticalTextPosition(SwingConstants.TOP);
		this.btn.setIcon(new ImageIcon(LauncherPanel.class.getResource("/org/doetsch/jaylist/resources/pin_16x16.png")));
		this.btn.setBackground(UI.LAUNCHER_COLOR_BUTTON);
		this.btn.setForeground(Color.WHITE);
		this.btn.setFont(UI.LAUNCHER_FONT);
		this.btn.setBorder(new EmptyBorder(6, 6, 6, 6));
		this.btn.setMinimumSize(new Dimension(104 - b * 2, 104 - b * 2));
		this.btn.setMaximumSize(new Dimension(104 - b * 2, 104 - b * 2));
		
		this.btn.setPreferredSize(new Dimension(104 - b * 2, 104 - b * 2));
		this.btn.setMargin(new Insets(0, 0, 0, 0));
		this.btn.setFont(new Font("Arial", Font.PLAIN, 16));
		this.btn.setHorizontalAlignment(SwingConstants.LEADING);
		this.btn.setVerticalAlignment(SwingConstants.TOP);
		add(this.btn);
		
		this.btn.setVisible(!isBlank);
		this.path = model.path;
	}

}
