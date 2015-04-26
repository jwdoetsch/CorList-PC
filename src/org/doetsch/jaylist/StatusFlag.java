package org.doetsch.jaylist;

import javax.swing.ImageIcon;

/**
 * The StatusFlag enumeration is the realization
 * of list item statuses. Each status is assigned
 * an integer status code, an ImageIcon icon, and a
 * String description of the status.
 * 
 * StatusFlag fields are immutable.
 * 
 * @author Jacob Wesley Doetsch
 */
enum StatusFlag {
	
	NONE (-1, null, "None"),
	INCOMPLETE (0, UI.ICON_UNCHECKED, "Incomplete"),
	COMPLETE (1, UI.ICON_CHECKED, "Complete"),
	ALERT (2, UI.ICON_URGENT, "Urgent"),
	QUESTION (3, UI.ICON_ISSUE, "Question");
	
	final int code;
	final ImageIcon icon;
	final String desc;
	
	StatusFlag (int flag, ImageIcon icon, String desc) {
		this.code = flag;
		this.icon = icon;
		this.desc = desc;
	}
	

	/**
	 * Returns the StatusFlag associated with the given
	 * integer code.
	 * 
	 * @param code
	 * @return
	 */
	static StatusFlag getStatusFlag (int code) {
		switch (code) {
			case -1 :
				return NONE;
			case 0:
				return INCOMPLETE;
			case 1:
				return COMPLETE;
			case 2:
				return ALERT;
			case 3:
				return QUESTION;
		}
		
		return NONE;
	};

}
