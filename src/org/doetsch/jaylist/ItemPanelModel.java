package org.doetsch.jaylist;

/**
 * ItemPanelModel encapsulates list table item
 * field values. ItemPanelModel is immutable.
 * 
 * @author Jacob Wesley Doetsch
 */
class ItemPanelModel {
	
	final String title;
	final String desc;
	final int flagStatus;
	final boolean flagExpand;
	
	ItemPanelModel (String title, String desc, int flag, boolean expanded) {
		this.title = title;
		this.desc = desc;
		this.flagStatus = flag;
		this.flagExpand = expanded;
	}

}
