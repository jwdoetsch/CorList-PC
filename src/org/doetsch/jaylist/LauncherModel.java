package org.doetsch.jaylist;

import java.net.URL;

/**
 * The immutable helper class LauncherModel encapsulates
 * LauncherPanel field values. 
 * 
 * @author Jacob Wesley Doetsch
 */
public class LauncherModel {

	final String title;
	final URL path;
	
	LauncherModel (String title, URL path) {
		this.title = title;
		this.path = path;
	}
	
}
