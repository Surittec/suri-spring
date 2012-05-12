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
package br.com.suricattus.surispring.jsf.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.suricattus.surispring.framework.util.CnpjUtil;
import br.com.suricattus.surispring.jsf.util.FacesUtils;

/**
 * Validador de CNPJ
 * 
 * @author Lucas Lins
 * 
 */
public class CnpjValidator implements Validator{
	
	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(!CnpjUtil.isValid(value, true)){
			String label = (String)component.getAttributes().get("label");
			if(label != null && !label.trim().equals("")){
				throw new ValidatorException(FacesUtils.create(FacesMessage.SEVERITY_ERROR, "javax.faces.validator.CNPJ.detail", label));
			}else{
				throw new ValidatorException(FacesUtils.create(FacesMessage.SEVERITY_ERROR, "javax.faces.validator.CNPJ"));
			}
		}
	}

}
