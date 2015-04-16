package org.doetsch.jaylist;

import java.awt.Color;

enum HighlightFlag {
	
	NONE (false, null),
	SELECTED (true, UI.COLOR_HIGHLIGHT2),
	EDITING (false, null),
	REMOVING (true, Color.RED);
	
	final boolean highlight;
	final Color color;
	
	HighlightFlag (boolean doHighlight, Color color) {
		this.highlight = doHighlight;
		this.color = color;
	}
	
}
