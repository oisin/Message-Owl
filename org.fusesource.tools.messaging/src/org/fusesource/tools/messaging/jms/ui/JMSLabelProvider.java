/*******************************************************************************
 * Copyright (c) 2009, 2010 Progress Software Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package org.fusesource.tools.messaging.jms.ui;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.fusesource.tools.messaging.core.IDestination;
import org.fusesource.tools.messaging.core.IListener;
import org.fusesource.tools.messaging.core.ISender;
import org.fusesource.tools.messaging.jms.JMSConstants;
import org.fusesource.tools.messaging.jms.JMSListener;
import org.fusesource.tools.messaging.jms.JMSUtils;
import org.fusesource.tools.messaging.utils.ImagesUtil;

public abstract class JMSLabelProvider extends LabelProvider {

    @Override
    public String getText(Object element) {
        return getDisplayText(element);
    }

    protected abstract String getDisplayText(Object element);

    @Override
    public Image getImage(Object element) {
        if (element instanceof ISender) {
            ISender sender = (ISender) element;
            return getDefaultDestinationImage(sender.getDestination());
        } else if (element instanceof IListener) {
            IListener listener = (IListener) element;
            boolean isConActive = JMSUtils.hasActiveConnection(listener);
            boolean isDurSub = JMSUtils.isDurableSubscriber((JMSListener) listener);
            if (isConActive && listener.isStarted() && listener.canReceive()) {
                if (isDurSub) {
                    return ImagesUtil.getInstance().getImage(JMSImageConstants.DURABLE_TOPIC_IMAGE_ICON);
                }
                return getDefaultDestinationImage(listener.getDestination());
            } else {
                if (isDurSub) {
                    return ImagesUtil.getInstance().getImage(JMSImageConstants.DURABLE_TOPIC_STOPPED_IMAGE_ICON);
                }
                return getStoppedDestinationImage(listener.getDestination());
            }
        }
        return null;
    }

    private Image getDefaultDestinationImage(IDestination destination) {
        if (destination == null) {
            return null;
        }
        ImagesUtil instance = ImagesUtil.getInstance();
        String type = destination.getDestinationType().getType();
        if (JMSConstants.QUEUE_TYPE.equals(type)) {
            return instance.getImage(JMSImageConstants.QUEUE_IMAGE_ICON);
        } else if (JMSConstants.TOPIC_TYPE.equals(type)) {
            return instance.getImage(JMSImageConstants.TOPIC_IMAGE_ICON);
        }
        return null;
    }

    private Image getStoppedDestinationImage(IDestination destination) {
        if (destination == null) {
            return null;
        }
        ImagesUtil instance = ImagesUtil.getInstance();
        String type = destination.getDestinationType().getType();
        if (JMSConstants.QUEUE_TYPE.equals(type)) {
            return instance.getImage(JMSImageConstants.QUEUE_STOPPED_IMAGE_ICON);
        } else if (JMSConstants.TOPIC_TYPE.equals(type)) {
            return instance.getImage(JMSImageConstants.TOPIC_STOPPED_IMAGE_ICON);
        }
        return null;
    }

}
