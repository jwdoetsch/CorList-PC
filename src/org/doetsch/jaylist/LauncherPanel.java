package org.doetsch.jaylist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

@SuppressWarnings("serial")
class LauncherPanel extends JPanel {
	
	private JButton btn;
	private ListFrame listFrame;
	private Launcher parentLauncher;
	
	LauncherPanel (ListFrame listFrame, Launcher parentLauncher) {
		this.listFrame = listFrame;
		this.parentLauncher = parentLauncher;
		initComponents();
	}
	
	private void initComponents () {
		
		//padding/border around the button
		int tilePadding = 2;

		setOpaque(false);
		setBorder(new EmptyBorder(tilePadding, tilePadding, tilePadding, tilePadding));
		setPreferredSize(new Dimension(104, 104));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.btn = new JButton();
		updateTileText();
		this.btn.setVerticalTextPosition(SwingConstants.TOP);
		this.btn.setBackground(UI.LAUNCHER_COLOR_BUTTON);
		this.btn.setForeground(Color.WHITE);
		this.btn.setFont(UI.LAUNCHER_FONT);
		this.btn.setBorder(new EmptyBorder(4, 4, 4, 4));
		this.btn.setMinimumSize(new Dimension(104 - tilePadding * 2, 104 - tilePadding * 2));
		this.btn.setMaximumSize(new Dimension(104 - tilePadding * 2, 104 - tilePadding * 2));
		
		this.btn.setPreferredSize(new Dimension(104 - tilePadding * 2, 104 - tilePadding * 2));
		this.btn.setMargin(new Insets(0, 0, 0, 0));
		this.btn.setFont(new Font("Arial", Font.PLAIN, 16));
		this.btn.setHorizontalAlignment(SwingConstants.LEADING);
		this.btn.setVerticalAlignment(SwingConstants.TOP);
		add(this.btn);
		
		this.btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (listFrame.isVisible()) {
					listFrame.requestFocus();
				} else {
					listFrame.setLocation(
							new Point(
									parentLauncher.getX() + 48,
									parentLauncher.getY() + 48));
					listFrame.setVisible(true);
				}
			}
		});

	}
	
	void updateTileText () {
		this.btn.setText("<html><p>" + listFrame.uiTextPane.getText() + "</p></html>");
	}

}
