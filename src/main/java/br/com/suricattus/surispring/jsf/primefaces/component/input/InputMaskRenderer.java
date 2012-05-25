/*
 * SURICATTUS
 * Copyright 2011, SURICATTUS CONSULTORIA LTDA, 
 * and individual contributors as indicated by the @authors tag
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package br.com.suricattus.surispring.jsf.primefaces.component.input;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.component.inputmask.InputMask;

/**
 * 
 * 
 * @author Lucas Lins
 *
 */
public class InputMaskRenderer extends org.primefaces.component.inputmask.InputMaskRenderer{

	@Override
	protected void encodeScript(FacesContext context, InputMask inputMask) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = inputMask.getClientId(context);
        String mask = inputMask.getMask();
		
        startScript(writer, clientId);

        writer.write("PrimeFaces.cw('InputMask','" + inputMask.resolveWidgetVar() + "',{");
        writer.write("id:'" + clientId + "'");
        
        if(mask != null) writer.write(",mask: {" + inputMask.getMask() + "}");
		
        encodeClientBehaviors(context, inputMask);

        writer.write("});");
	
		endScript(writer);
	}
	
}
