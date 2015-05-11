package org.corlist.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * ImageTextWriter is a helper class for JayList and
 * adjacent utilities to help write text on top of
 * Swing frame and taskbar icons.
 * 
 * @author Jacob Wesley Doetsch
 */
public class ImageTextWriter {
	
	private Font font;
	private Color color;
	private Point pt;
	
	/**
	 * Instantiates an ImageTextWriter that will write
	 * white, bold, 28-point, Arial-faced text at the
	 * location (23, 55).
	 */
	public ImageTextWriter () {
		font = new Font("Arial", Font.BOLD, 28);
		color = new Color(255, 255, 255);
		pt = new Point(0, 55);
	}
	
	/**
	 * Sets the font
	 * 
	 * @param font the Font to set
	 */
	public void setFont (Font font) {
		this.font = font;
	}
	
	/**
	 * Sets the text color
	 * 
	 * @param color the Color to set
	 */
	public void setColor (Color color) {
		this.color = color;
	}
	
	/**
	 * Sets the location to draw the text
	 * 
	 * @param pt the Point location
	 */
	public void setPoint (Point pt) {
		this.pt = pt;
	}
	
	/**
	 * Loads the given source image, writes the given text
	 * onto it, and returns the result.
	 * 
	 * @param imgSrc the source image location as a URL
	 * @param txt the text to write
	 * @return the resulting image
	 * @throws IOException
	 */
	public Image writeText (URL imgSrc, String txt) throws IOException {
	
		BufferedImage image = ImageIO.read(imgSrc);
		Graphics g = image.getGraphics();
		Graphics2D g2 = (Graphics2D) g;		
		
		g2.setRenderingHints(new RenderingHints(
	             RenderingHints.KEY_TEXT_ANTIALIASING,
	             RenderingHints.VALUE_TEXT_ANTIALIAS_ON));
		g2.setFont(font);
		g2.setColor(color);
		g2.drawString(txt, pt.x, pt.y);
		return image;
	}

}
