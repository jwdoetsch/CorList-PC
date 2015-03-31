package org.doetsch.jaylist;

import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.Box;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;

public class ItemPanel2 extends JPanel {
	private JPanel panel;
	private JScrollPane scrollPane;
	private JTextArea textArea;
	private JLabel labelSpacer;
	private JButton btnNewButton;
	private JTextField textField;
	private JButton btnNewButton_1;

	/**
	 * Create the panel.
	 */
	public ItemPanel2() {

		initComponents();
	}
	private void initComponents() {
		setLayout(new BorderLayout(0, 0));
		this.panel = new JPanel();
		add(this.panel, BorderLayout.NORTH);
		this.panel.setLayout(new BorderLayout(0, 0));
		this.btnNewButton = new JButton("New button");
		this.panel.add(this.btnNewButton, BorderLayout.WEST);
		this.textField = new JTextField();
		this.panel.add(this.textField, BorderLayout.CENTER);
		this.textField.setColumns(10);
		this.btnNewButton_1 = new JButton("New button");
		this.panel.add(this.btnNewButton_1, BorderLayout.EAST);
		this.scrollPane = new JScrollPane();
		add(this.scrollPane, BorderLayout.CENTER);
		this.textArea = new JTextArea();
		this.textArea.setWrapStyleWord(true);
		this.textArea.setLineWrap(true);
		this.scrollPane.setViewportView(this.textArea);
		this.labelSpacer = new JLabel("New label");
		add(this.labelSpacer, BorderLayout.WEST);
	}

}
