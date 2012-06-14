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

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.SystemEvent;

import br.com.suricattus.surispring.spring.scope.ViewAccessScope;
import br.com.suricattus.surispring.spring.scope.ViewScope;

/**
 * 
 * @author Lucas Lins
 * 
 */
public class ViewMapListener implements javax.faces.event.ViewMapListener {
	
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		
		if (event instanceof PostConstructViewMapEvent) {
			PostConstructViewMapEvent viewMapEvent = (PostConstructViewMapEvent) event;
			UIViewRoot viewRoot = (UIViewRoot) viewMapEvent.getComponent();
			
			initViewMaps(viewRoot);
			
		} else if (event instanceof PreDestroyViewMapEvent) {
			PreDestroyViewMapEvent viewMapEvent = (PreDestroyViewMapEvent) event;
			UIViewRoot viewRoot = (UIViewRoot) viewMapEvent.getComponent();
			
			destroyBeans(viewRoot);
		}
	}
	
	public boolean isListenerForSource(Object source) {
		return source instanceof UIViewRoot;
	}
	
	/* ************************
	 * PRIVATE METHODS
	 * ************************/
	
	private void initViewMaps(UIViewRoot viewRoot){
		viewRoot.getViewMap().put(ViewScope.BEANS, new HashMap<String, Object>());
		viewRoot.getViewMap().put(ViewScope.CALLBACKS, new HashMap<String, Runnable>());
		
		viewRoot.getViewMap().put(ViewAccessScope.BEANS, new HashMap<String, Object>());
		viewRoot.getViewMap().put(ViewAccessScope.CALLBACKS, new HashMap<String, Runnable>());
	}
	
	@SuppressWarnings("unchecked")
	private void destroyBeans(UIViewRoot viewRoot){
		Map<String, Runnable> callbacks = (Map<String, Runnable>) viewRoot.getViewMap().get(ViewScope.CALLBACKS);
		if (callbacks != null) {
			for (Runnable c : callbacks.values()) c.run();
			callbacks.clear();
		}
		
		callbacks = (Map<String, Runnable>) viewRoot.getViewMap().get(ViewAccessScope.CALLBACKS);
		if (callbacks != null) {
			for (Runnable c : callbacks.values()) c.run();
			callbacks.clear();
		}
	}
}
