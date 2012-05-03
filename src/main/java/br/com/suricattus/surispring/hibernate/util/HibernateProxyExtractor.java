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
package br.com.suricattus.surispring.hibernate.util;

import org.hibernate.proxy.HibernateProxy;

/**
 * Classe utilitaria para remocao do proxy do hibernate.
 * 
 * @author Lucas Lins
 *
 */
public class HibernateProxyExtractor {

	/**
	 * Metodo para remover o proxy da entidade e retornar a real classe da entidade.
	 * @param entity
	 * @return class
	 */
	public static Class<?> extractClass(Object entity){
		if (entity instanceof HibernateProxy) {
			return ((HibernateProxy) entity).getHibernateLazyInitializer().getPersistentClass();
		}else{
			return entity.getClass();
		}
	}
	
	/**
	 * Metodo para remover o proxy da entidade e retornar o real objeto da entidade.
	 * @param entity
	 * @return object
	 */
	public static Object extractEntity(Object entity){
		if (entity instanceof HibernateProxy) {
			return ((HibernateProxy) entity).getHibernateLazyInitializer().getImplementation();
		}else{
			return entity;
		}
	}
	
}
