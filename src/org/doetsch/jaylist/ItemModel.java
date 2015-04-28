package org.doetsch.jaylist;

/**
 * ItemPanelModel encapsulates list table item
 * field values. ItemPanelModel is immutable.
 * 
 * @author Jacob Wesley Doetsch
 */
final class ItemModel {
	
	//content
	final String title;
	final String desc;
	final ItemStatus status;
	
	//ui behavior
	final boolean isExpanded;
	final boolean isHidden;
	
	ItemModel (String title, String desc, ItemStatus status, boolean isExpanded, boolean isHidden) {
		this.title = title;
		this.desc = desc;
		this.status = status;
		this.isExpanded = isExpanded;
		this.isHidden = isHidden;
	}

}
