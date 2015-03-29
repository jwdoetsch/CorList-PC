package org.doetsch.jaylist;

import java.util.HashMap;

class ItemModel {
	
	private HashMap<String, Object> fields;
	
	/**
	 * Instantiates an ItemModel encapsulating the given title and flag
	 * 
	 * @param flag 0=unchecked, 1=checked, 2=alert, 3=question 
	 * @param title
	 * @return
	 */
	static ItemModel create (int flag, String title) {
		ItemModel itemModel = new ItemModel();
		itemModel.setFlag(flag);
		itemModel.setTitle(title);
		return itemModel;
	}
	
	static ItemModel clone (ItemModel itemModel) {
		return (new ItemModel()).setFields(itemModel.getFields());
	}
	
	ItemModel () {
		fields = new HashMap<String, Object>();
	}
	
	void setFlag (int flag) {
		fields.put("flag", flag);
	}
	
	int getFlag () {
		return (int)fields.get("flag");
	}
	
	boolean isChecked () {
		return ((int)fields.get("flag") == 1);
	}
	
	void setTitle (String title) {
		fields.put("title", title);
	}
	
	String getTitle () {
		return (String)fields.get("title");
	}
	
	HashMap<String, Object> getFields () {
		return fields;
	}
	
	ItemModel setFields (HashMap<String, Object> fields) {
		this.fields = fields;
		return this;
	}

}
