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

import java.util.regex.Matcher;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

import br.com.suricattus.surispring.jsf.util.FacesUtils;

/**
 * Validador de E-mail
 * 
 * @author Lucas Lins
 *
 */
@FacesValidator("br.com.suricattus.surispring.jsf.validator.EmailValidator")
public class EmailValidator implements Validator{

	private static String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
	private static String DOMAIN = "(" + ATOM + "+(\\." + ATOM + "+)*";
	private static String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

	private static java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
			"^" + ATOM + "+(\\." + ATOM + "+)*@"
					+ DOMAIN
					+ "|"
					+ IP_DOMAIN
					+ ")$",
			java.util.regex.Pattern.CASE_INSENSITIVE
	);

	public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		if(!isValid((String)value)){
			String label = (String)component.getAttributes().get("label");
			if(label != null && !label.trim().equals("")){
				throw new ValidatorException(FacesUtils.createMessage(FacesMessage.SEVERITY_ERROR, "javax.faces.validator.Email.detail", label));
			}else{
				throw new ValidatorException(FacesUtils.createMessage(FacesMessage.SEVERITY_ERROR, "javax.faces.validator.Email"));
			}
		}
	}
	
	private boolean isValid(String value) {
		if ( value == null || value.length() == 0 ) {
			return true;
		}
		Matcher m = pattern.matcher( value );
		return m.matches();
	}
	
}
