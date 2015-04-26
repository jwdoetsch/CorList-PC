package org.doetsch.jaylist;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
public class LauncherPanel extends JPanel {
	private JButton btn;
	private URL path;
	private Launcher parentLauncher;
	
	
	/**
	 * Create the panel.
	 */
	public LauncherPanel (Launcher parentLauncher, LauncherModel model, boolean isBlank) {
		this.parentLauncher = parentLauncher;
		initComponents(model, isBlank);
	}
	private void initComponents (LauncherModel model, boolean isBlank) {
		int b = 2;
		setOpaque(false);
		setBorder(new EmptyBorder(b, b, b, b));

		setPreferredSize(new Dimension(104, 104));
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		this.btn = new JButton("<html><p>" + model.title + "</p></html>");
		this.btn.setVerticalTextPosition(SwingConstants.TOP);
		this.btn.setBackground(UI.LAUNCHER_COLOR_BUTTON);
		this.btn.setForeground(Color.WHITE);
		this.btn.setFont(UI.LAUNCHER_FONT);
		this.btn.setBorder(new EmptyBorder(4, 4, 4, 4));
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
		
		this.btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ListMarshall marshall = new ListMarshall();
				
				try {
					ListFrame newList;
					newList = new ListFrame(parentLauncher, marshall.unmarshall(path), true);
					newList.setVisible(true);
					
				/*
				 * catch XML decode and file IO exceptions and notify the
				 * user via dialog
				 */
				} catch (IOException | SAXException
						| ParserConfigurationException e1) {
					JOptionPane.showMessageDialog(parentLauncher, "Can't open the selected file.");
				}
			}
			
		});
	}

}
