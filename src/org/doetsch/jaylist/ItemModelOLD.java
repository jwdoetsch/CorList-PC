package org.doetsch.jaylist;

import java.util.HashMap;

class ItemModelOLD {
	
	private HashMap<String, Object> fields;
	
	/**
	 * Instantiates an ItemModel encapsulating the given title and flag
	 * 
	 * @param flag 0=unchecked, 1=checked, 2=alert, 3=question 
	 * @param title
	 * @return
	 */
	static ItemModelOLD create (int flag, String title, String desc) {
		ItemModelOLD itemModel = new ItemModelOLD();
		itemModel.setFlag(flag);
		itemModel.setTitle(title);
		itemModel.setDesc("DESCRIPTION");
		return itemModel;
	}
	
	static ItemModelOLD clone (ItemModelOLD itemModel) {
		return (new ItemModelOLD()).setFields(itemModel.getFields());
	}
	
	ItemModelOLD () {
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
	
	void setDesc (String description) {
		//description = "DESCRIPTION OVERRIDE";
		fields.put("desc", description);
	}
	
	String getDesc () {
		return (String)fields.get("desc");
	}
	
	HashMap<String, Object> getFields () {
		return fields;
	}
	
	ItemModelOLD setFields (HashMap<String, Object> fields) {
		this.fields = fields;
		return this;
	}

	public static void main (String[] args) {
		ItemModelOLD m = ItemModelOLD.create(0, "", "");
//		System.out.println(m.getDesc());
		//m.setDesc("foobar description");
		ItemPanelOLD p = new ItemPanelOLD(m, false, null, 0);
		System.out.println(m.getDesc());
	}
	
}
