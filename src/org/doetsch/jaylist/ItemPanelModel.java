package org.doetsch.jaylist;

/**
 * ItemPanelModel encapsulates list table item
 * field values. ItemPanelModel is immutable.
 * 
 * @author Jacob Wesley Doetsch
 */
final class ItemPanelModel {
	
	//content
	final String title;
	final String desc;
	final int statusFlag;
	
	//ui behavior
	final boolean isExpanded;
	final boolean isHidden;
	
	ItemPanelModel (String title, String desc, int flag, boolean isExpanded, boolean isHidden) {
		this.title = title;
		this.desc = desc;
		this.statusFlag = flag;
		this.isExpanded = isExpanded;
		this.isHidden = isHidden;
	}

}
