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
	//final int statusFlag;
	final StatusFlag status;
	
	//ui behavior
	final boolean isExpanded;
	final boolean isHidden;
	
	ItemPanelModel (String title, String desc, StatusFlag status, boolean isExpanded, boolean isHidden) {
		this.title = title;
		this.desc = desc;
		//this.statusFlag = flag;
		this.status = status;
		this.isExpanded = isExpanded;
		this.isHidden = isHidden;
	}

}
