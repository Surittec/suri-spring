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
package br.com.suricattus.surispring.spring.scope.holder;

import java.io.Serializable;
import java.util.Map;

/**
 * 
 * @author Lucas Lins
 *
 */
public class ViewAccessScopeHolder implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Map<String, Object> beans;
	private Map<String, Runnable> callbacks;
	
	public void register(Map<String, Object> beans, Map<String, Runnable> callbacks){
		this.beans = beans;
		this.callbacks = callbacks;
	}
	
	public Object removeBean(String name){
		if(beans != null){
			return beans.remove(name);
		}
		return null;
	}
	
	public Runnable removeCallback(String name){
		if(callbacks != null){
			return callbacks.remove(name);
		}
		return null;
	}
	
	public void destroy(){
		if(beans != null){
			beans.clear();
			beans = null;
		}
		
		if(callbacks != null){
			for(Runnable callback : callbacks.values()) callback.run();
			callbacks.clear();
			callbacks = null;
		}
	}
}
