package org.doetsch.jaylist;

public class ItemModel {
	
	final String title;
	final String desc;
	final int flag;
	final boolean expanded;
	
	ItemModel (String title, String desc, int flag, boolean expanded) {
		this.title = title;
		this.desc = desc;
		this.flag = flag;
		this.expanded = expanded;
	}

}
