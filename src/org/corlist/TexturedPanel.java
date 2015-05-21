package org.corlist;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


/**
 * TexturedPanel is a simple extension of JPanel that
 * adds a tiled background, the texture, to the drawing
 * of the JPanel.
 * 
 * TexturedPanel is implemented by Launcher and ListFrame.
 * 
 * @author Jacob Wesley Doetsch
 */
@SuppressWarnings("serial")
class TexturedPanel extends JPanel {
	
	private BufferedImage texture;
	private TexturePaint paint;
	
	/**
	 * Creates a new TexturedPanel utilizing the
	 * given texture
	 * 
	 * @param texturePath the URL of the image to use
	 * as the texture
	 * @throws IOException
	 */
	TexturedPanel (URL texturePath) throws IOException {
		super();
		texture = ImageIO.read(texturePath);
		paint = new TexturePaint(texture,
				new Rectangle(0, 0, texture.getWidth(), texture.getHeight()));
	}
	
	/*
	 * Paints the texture over whatever opaque background.
	 */
	@Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
		
        g2d.setPaint(paint);
        g2d.fillRect(0, 0, getWidth(), getHeight());
    }

}
