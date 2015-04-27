package org.doetsch.jaylist;

import java.awt.Dimension;
import java.net.URL;
import java.util.ArrayList;

/**
 * ListFrameModel models the UI component field values
 * of ListFrame to facilitate list marshalling and
 * unmarshalling. ListFrameModel is mutable.
 * 
 * @author Jacob Wesley Doetsch
 */
class ListModel {
	
	private URL path;
	private String header;
	private ArrayList<ItemPanelModel> itemModels;
	private Dimension frameSize;
	
	ListModel (String header) {
		this.header = header;
		this.itemModels = new ArrayList<ItemPanelModel>();
	}
	
	ListModel () {
		this.itemModels = new ArrayList<ItemPanelModel>();
	}
	
	void addItemModels (ItemPanelModel model) {
		itemModels.add(model);
	}
	
	void setPath (URL path) {
		this.path = path;
	}
	
	URL getPath () {
		return this.path;
	}
	
	String getHeader () {
		return this.header;
	}
	
	void setHeader (String header) {
		this.header = header;
	}
	
	ArrayList<ItemPanelModel> getItemModels () {
		return this.itemModels;
	}
	
	void setFrameSize (Dimension frameSize) {
		this.frameSize = frameSize;
	}
	
	Dimension getFrameSize () {
		return this.frameSize;
	}

}
