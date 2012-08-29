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
package br.com.suricattus.surispring.jsf.listener;

import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import br.com.suricattus.surispring.jsf.util.FacesUtils;
import br.com.suricattus.surispring.spring.scope.ViewAccessScope;

/**
 * 
 * @author Lucas Lins
 *
 */
public class ViewAccessScopeListener implements PhaseListener{
	
	private static final long serialVersionUID = 1L;
	
	public void afterPhase(PhaseEvent event) {
		if(event.getFacesContext().getViewRoot() != null && !FacesUtils.getFlash().containsKey(ViewAccessScope.BEANS)){
			UIViewRoot viewRoot = event.getFacesContext().getViewRoot();
			FacesUtils.getFlash().put(ViewAccessScope.BEANS, viewRoot.getViewMap().get(ViewAccessScope.BEANS));
			FacesUtils.getFlash().put(ViewAccessScope.CALLBACKS, viewRoot.getViewMap().get(ViewAccessScope.CALLBACKS));
		}
	}

	public void beforePhase(PhaseEvent event) {}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}
	
}
