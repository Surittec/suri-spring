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
package br.com.suricattus.surispring.framework.domain;

import br.com.suricattus.surispring.hibernate.util.HibernateProxyExtractor;

/**
 * Classe base de todas as entidades.
 * 
 * @author Lucas Lins
 *
 */
public abstract class BaseEntity implements Entity{

	private static final long serialVersionUID = 1L;

	@Override
	public String toString() {
		return String.format("%s[id = %s]", getClass().getName(), getId());
	}

	@Override
	public int hashCode() {
		Object id = getId();
		final int prime = 37;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		
		Class<?> objClass = HibernateProxyExtractor.extractClass(obj);
		if (getClass() != objClass) return false;
		
		final BaseEntity other = (BaseEntity) obj;
		Object id = getId();
		if (id == null) {
			if (other.getId() != null) return false;
		}else if (!id.equals(other.getId())) {
			return false;
		}
		return true;
	}
	
}
