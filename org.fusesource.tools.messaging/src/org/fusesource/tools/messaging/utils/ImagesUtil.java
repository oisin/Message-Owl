// Copyright © 2009 Progress Software Corporation. All Rights Reserved.
package org.fusesource.tools.messaging.utils;

import java.io.InputStream;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * Contains a common images registry shared by all the Controls. The key to the
 * common images registry is the path of the image in the jar. Provides utility
 * methods to load images from Jars.
 * 
 */
public class ImagesUtil {
	private static ImagesUtil instance = null;

	private ImageRegistry imageRegistry = new ImageRegistry();

	private ImagesUtil() {
	}

	public static ImagesUtil getInstance() {
		if(instance == null)
			instance = new ImagesUtil();
		return instance;
	}
	
	public ImageRegistry getImageRegistry() {
		return imageRegistry;
	}

    public static ImageDescriptor loadImageFromPlugin(String imagePath) {
    	ImageDescriptor  imageDescriptor = null;

        URL url = null;
        try {
        	imageDescriptor = ImageDescriptor.createFromFile(getInstance().getClass(),imagePath);            
        } catch (Exception e) {
            e.printStackTrace();
        } 
        return imageDescriptor;
    }
    
    public static Image loadImageFromJar(String imagePath) {
        Image image = null;

        try {
            InputStream is = getInstance().getClass().getClassLoader().getResourceAsStream(imagePath);
            image = new Image(Display.getCurrent(), is);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return image;
    }
    
    /**
     * Returns the image corresponding to the imagePath in the registry.
     * In case there is no image registred with that path then loads and stores the image
     * into the registry
     * 
     * @param imagePath
     * @return
     */
    public Image getImage(String imagePath) {
    	// this will create the ImageDescriptor corresponding to imagePath
    	// and then it will get the Image corresponding to it.
    	ImageDescriptor descriptor = getImageDescriptor(imagePath);
    	return imageRegistry.get(imagePath);
    }
    
    public ImageDescriptor getImageDescriptor(String imagePath) {
    	ImageDescriptor descriptor = imageRegistry.getDescriptor(imagePath);
    	if(descriptor == null) {
    		// create an ImageDescriptor with this location
    		descriptor = new JARImageDescriptor(imagePath);
    		imageRegistry.put(imagePath, descriptor);
    	}
    	
    	return imageRegistry.getDescriptor(imagePath);
    }
}