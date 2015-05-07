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
package br.com.surittec.surispring.security.controller;

import java.io.IOException;

import javax.faces.context.FacesContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.WebAttributes;
import org.springframework.util.StringUtils;

import br.com.surittec.surispring.faces.controller.Controller;

/**
 * 
 * @author Lucas Lins
 *
 */
public abstract class FormLoginController extends Controller {

	private static final long serialVersionUID = 1L;

	public void login() throws IOException, ServletException {

		if (!StringUtils.hasText(getParameter("j_username")) || !StringUtils.hasText(getParameter("j_password"))) {
			addMsgErro("javax.faces.loginFailed");
			return;
		}

		RequestDispatcher dispatcher = getRequest().getRequestDispatcher("/j_spring_security_check");
		dispatcher.forward(getRequest(), getResponse());
		FacesContext.getCurrentInstance().responseComplete();
	}

	// @URLAction(phaseId = PhaseId.RESTORE_VIEW)
	public void redirect() throws IOException {
		if (!(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)) {
			getExternalContext().redirect(getRequest().getContextPath());
		}
	}

	// @URLAction(phaseId = PhaseId.RENDER_RESPONSE)
	public void addError() {
		Exception ex = (Exception) getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
		if (ex instanceof AuthenticationException) {
			addMsgErro("javax.faces.loginFailed");
			getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, null);
		}
	}

}
