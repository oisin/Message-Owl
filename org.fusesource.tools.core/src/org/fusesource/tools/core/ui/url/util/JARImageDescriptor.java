/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.core.ui.url.util;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.ImageData;

/**

 */
public class JARImageDescriptor extends ImageDescriptor {
    private String imagePathInJar = null;

    public JARImageDescriptor(String imagePathInJar) {
        if (imagePathInJar == null) {
            throw new IllegalArgumentException("Image Path cannoth be null");
        }
        this.imagePathInJar = imagePathInJar;
    }

    // TODO: implement equals() method
    @Override
    public boolean equals(Object obj) {
        boolean isEqual = false;

        if (obj instanceof JARImageDescriptor) {
            JARImageDescriptor other = (JARImageDescriptor) obj;
            isEqual = imagePathInJar.equals(other.getImagePath());
        }

        return isEqual;
    }

    /**
     * Creates ImageData object by reading the Image information from the Classpath
     */
    @Override
    public ImageData getImageData() {
        InputStream in = getClass().getClassLoader().getResourceAsStream(imagePathInJar);
        ImageData result = null;
        if (in != null) {
            try {
                result = new ImageData(in);
            } catch (SWTException e) {
                if (e.code != SWT.ERROR_INVALID_IMAGE) {
                    throw e;
                }
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    System.err.println(getClass().getName() + ".getImageData(): "
                            + "Exception while closing InputStream : " + e);
                }
            }
        }
        return result;
    }

    /**
     * Returns the path of the image (passed to the constructor)
     * 
     * @return the path of the image (passed to the constructor)
     */
    public String getImagePath() {
        return imagePathInJar;
    }
}
