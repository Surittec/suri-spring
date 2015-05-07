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
package br.com.surittec.surispring.faces.scope.view.impl;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

/**
 * 
 * @author Lucas Lins
 *
 */
public class ViewScope implements Scope {

	public static final String SCOPE_VIEW = "view";

	public static final String CALLBACKS = ViewScope.class.getName() + ".callbacks";
	public static final String BEANS = ViewScope.class.getName() + ".beans";

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Map<String, Object> beansMap = getBeansMap();
		Object instance = beansMap.get(name);
		if (instance == null) {
			instance = objectFactory.getObject();
			synchronized (beansMap) {
				beansMap.put(name, instance);
			}
		}
		return instance;
	}

	@Override
	public Object remove(String name) {
		Object instance = getBeansMap().remove(name);
		if (instance != null) {
			Runnable callback = getCallbacksMap().remove(name);
			if (callback != null) {
				callback.run();
			}
		}
		return instance;
	}

	@Override
	public void registerDestructionCallback(String name, Runnable runnable) {
		getCallbacksMap().put(name, runnable);
	}

	@Override
	public Object resolveContextualObject(String name) {
		return new FacesRequestAttributes(FacesContext.getCurrentInstance()).resolveReference(name);
	}

	@Override
	public String getConversationId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();
	}

	/* **************************
	 * PROTECTED METHODS *************************
	 */

	protected String getBeansIdentifier() {
		return BEANS;
	}

	protected String getCallbacksIdentifier() {
		return CALLBACKS;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> getBeansMap() {
		return (Map<String, Object>) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get(getBeansIdentifier());
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Runnable> getCallbacksMap() {
		return (Map<String, Runnable>) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get(getCallbacksIdentifier());
	}

}
