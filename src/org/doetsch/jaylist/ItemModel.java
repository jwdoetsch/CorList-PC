package org.doetsch.jaylist;

public class ItemModel {
	
	final String title;
	final String desc;
	final int flagStatus;
	final boolean flagExpand;
	
	ItemModel (String title, String desc, int flag, boolean expanded) {
		this.title = title;
		this.desc = desc;
		this.flagStatus = flag;
		this.flagExpand = expanded;
	}

}
