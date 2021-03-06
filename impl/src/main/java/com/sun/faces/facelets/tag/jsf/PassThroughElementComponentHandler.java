/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package com.sun.faces.facelets.tag.jsf;

import com.sun.faces.util.Util;
import java.util.Map;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagException;

public class PassThroughElementComponentHandler extends ComponentHandler {
    
    private final TagAttribute elementName;
    
    protected final TagAttribute getRequiredPassthroughAttribute(String localName)
            throws TagException {
        TagAttribute attr = this.tag.getAttributes().get(PassThroughAttributeLibrary.Namespace, localName);
        if (attr == null) {
            throw new TagException(this.tag, "Attribute '" + localName
                    + "' is required");
        }
        return attr;
    }
    
    

    public PassThroughElementComponentHandler(ComponentConfig config) {
        super(config);
        
        elementName = this.getRequiredPassthroughAttribute(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY);
    }
    
    @Override
    public UIComponent createComponent(FaceletContext ctx) {
        UIComponent result = null;
        try {
            Class clazz = Util.loadClass("com.sun.faces.component.PassthroughElement", this);
            result = (UIComponent)clazz.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException cnfe) {
            throw new FacesException(cnfe);
        }
        
        return result;
    }

    @Override
    public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {
        if (parent.getParent() == null) {
            Map<String,Object> passThroughAttrs = c.getPassThroughAttributes(true);
            Object attrValue;
            attrValue = (this.elementName.isLiteral()) ? this.elementName.getValue(ctx) : this.elementName.getValueExpression(ctx, Object.class);
            passThroughAttrs.put(Renderer.PASSTHROUGH_RENDERER_LOCALNAME_KEY, attrValue);
        }
        
    }
    
    
    
}
