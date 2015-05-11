package org.corlist.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.corlist.Launcher;

/**
 * AdjacentResourceLoader assists in loading project
 * resources from paths both within or outside the
 * project or JAR. AdjacentLoader determines how 
 * the target resources are loaded with respect to how
 * the project is executed; as a project itself or as
 * a JAR.
 * 
 * AdjacentResourceLoader loads resources located 
 * in the project's root folder or folder containing the
 * executed JAR.
 * 
 * AdjacentResourceLoader makes the determination whether
 * or not execution is taking place within a JAR or not.
 * 
 * 
 * Use Cases:
 * 
 *     Project:
 *         /resources
 *         /src/<project source>
 *         /bin/<project classes>
 *    
 *     JAR:
 *         /<project name>
 *             /resources
 *             <project>.jar
 * 
 * AdjacentResourceLoader enables the loading of resources
 * from the folder /resources in each case.  
 * 
 * @author Jacob Wesley Doetsch
 */
public class AdjacentResourceLoader {

	private File file;
	
	public static AdjacentResourceLoader getLoader () {
		return new AdjacentResourceLoader();		
	}
	
	public URI getResource (String path) {
		
		if (Launcher.class.getResource("Launcher.class").toString().startsWith("jar")) {
			
			//get the resource relative to JAR location
			try {
//				file = new File(ClassLoader.getSystemClassLoader().
//						getResource("./lists/").toURI());
				file = new File(ClassLoader.getSystemClassLoader().
				getResource("./" + path).toURI());
				return file.toURI();
				
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
		} else {
			
			//get the resource relative to IDE execution environment
			try {
				String parentDir = (new File(
						Launcher.class.getResource("../..").toURI())).getParent();
//				file = (new File(parentDir + "/lists/"));
				file = (new File(parentDir + "/" + path));
				return file.toURI();

			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			
		}
		
		return null;
	}
	
}
