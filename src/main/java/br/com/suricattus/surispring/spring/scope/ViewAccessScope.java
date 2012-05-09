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
package br.com.suricattus.surispring.spring.scope;

import java.util.Map;

import org.springframework.beans.factory.ObjectFactory;

import br.com.suricattus.surispring.spring.scope.holder.ViewAccessScopeHolder;
import br.com.suricattus.surispring.spring.util.ApplicationContextUtil;

/**
 * 
 * @author Lucas Lins
 *
 */
public class ViewAccessScope extends ViewScope{

	public static final String CALLBACKS = ViewAccessScope.class.getName() + ".callbacks";
	public static final String BEANS = ViewAccessScope.class.getName() +  ".beans";
	
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Map<String, Object> beansMap = getBeansMap();
		Object instance = beansMap.get(name);
		if(instance == null) {
			
			ViewAccessScopeHolder holder = getScopeHolder();
			instance = holder.removeBean(name);
			if(instance != null){
				Runnable callback = holder.removeCallback(name);
				if(callback != null){
					registerDestructionCallback(name, callback);
				}
			}else{
				instance = objectFactory.getObject();
			}
			
			synchronized (beansMap) {
				beansMap.put(name,instance);
			}
		}
		return instance;
	}

	
	/* **************************
	 * PROTECTED METHODS
	 * **************************/
	
	@Override
	protected String getBeansIdentifier() {
		return BEANS;
	}
	
	@Override
	protected String getCallbacksIdentifier() {
		return CALLBACKS;
	}
	
	protected ViewAccessScopeHolder getScopeHolder(){
		return ApplicationContextUtil.getContext().getBean(ViewAccessScopeHolder.class);
	}
}
