/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.ui;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.fusesource.tools.messaging.utils.ImagesUtil;

public abstract class AbstractDecorator extends LabelProvider implements ILabelDecorator {

    protected Image getOverlayImage(String id, Image baseImage, Image decoratorImage) {
        Image decoratorImageDescriptor = ImagesUtil.getInstance().getImageRegistry().get(id);
        if (decoratorImageDescriptor == null) {
            ImagesUtil.getInstance().getImageRegistry().put(id, getOverlayImageDescriptor(baseImage, decoratorImage));
            decoratorImageDescriptor = ImagesUtil.getInstance().getImageRegistry().get(id);
        }
        return decoratorImageDescriptor;
    }

    protected abstract ImageDescriptor getOverlayImageDescriptor(Image baseImage, Image decoratorImage);

}
