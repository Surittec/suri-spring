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
package br.com.suricattus.surispring.framework.service;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.stereotype.Service;

import br.com.suricattus.surispring.framework.domain.BaseEntity;
import br.com.suricattus.surispring.framework.util.SearchSort;
import br.com.suricattus.surispring.spring.util.ApplicationContextUtil;

/**
 * Servico generico que auxilia na busca de entidades persistentes.
 * 
 * @author Lucas Lins
 * 
 */
@Service
public class GenericRetrieveService implements Serializable{
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ATTRIBUTES
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static final long serialVersionUID = 1L;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// DEFAULT METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	Session getSession(){
		try{
			return ApplicationContextUtil.getContext().getBean(SessionFactory.class).getCurrentSession();
		}catch (NoSuchBeanDefinitionException e) {
			return (Session)ApplicationContextUtil.getContext().getBean(EntityManager.class).getDelegate();
		}
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PUBLIC METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	/**
	 * Retorna a entidade do tipo <T>, cujo id foi informado.
	 * @param <T>
	 * @param classe
	 * @param id
	 * @return entidade
	 */
	public <T extends BaseEntity> T retrieve(Class<T> classe, Serializable id){
		return (T)getSession().get(classe, id);
	}
	
	/**
	 * Retorna a lista de entidades do tipo <T>
	 * @param <T>
	 * @param classe
	 * @return lista de entidades
	 */
	public <T extends BaseEntity> List<T> retrieveAll(Class<T> classe){
		return getSession().createCriteria(classe).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * Retorna a lista de entidades do tipo <T> ordenada pelas propriedades informadas, nesta ordem.
	 * @param <T>
	 * @param classe
	 * @param sort
	 * @param propertiesName
	 * @return lista de entidades ordadenadas
	 */
	public <T extends BaseEntity> List<T> retrieveAll(Class<T> classe, SearchSort sort, String ... propertiesName){
		Criteria criteria = getSession().createCriteria(classe);
		if(SearchSort.ASCENDING.equals(sort)){
			for(String property : propertiesName) criteria.addOrder(Order.asc(property));
		}
		else if(SearchSort.DESCENDING.equals(sort)){
			for(String property : propertiesName) criteria.addOrder(Order.desc(property));
		}
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * Retorna a lista de entidades do tipo <T> filtradas pelos parametros informados
	 * @param <T>
	 * @param classe
	 * @param propertyName
	 * @param value
	 * @return lista de entidades
	 */
	public <T extends BaseEntity> List<T> retrieveByProperty(Class<T> classe, String propertyName, Object value){
		return getSession().createCriteria(classe).add(Restrictions.eq(propertyName, value)).list();
	}
	
	/**
	 * Retorna a lista de entidades do tipo <T> filtradas pelos parametros informados. Difere do metodo
	 * retrieveByProperty(Class<T> classe, String propertyName, Object value) pelo ultimo parametro definido ja como 
	 * String e utiliza o {@link MatchMode} exato para a busca. 
	 * 
	 * @param <T>
	 * @param classe
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T extends BaseEntity> List<T> retrieveByProperty(Class<T> classe, String propertyName, String value){
		return retrieveByProperty(classe, propertyName, value, null);
	}
	
	/**
	 * Retorna a lista de entidades filtradas pelos parametros informadas.
	 * 
	 * @param <T>
	 * @param classe
	 * @param propertyName
	 * @param value
	 * @param matchMode
	 * @return lista de entidades.
	 */
	public <T extends BaseEntity> List<T> retrieveByProperty(Class<T> classe, String propertyName, String value, MatchMode matchMode){
		if (matchMode != null){
			return getSession().createCriteria(classe).add(Restrictions.ilike(propertyName, value, matchMode)).list();
		}else{
			return getSession().createCriteria(classe).add(Restrictions.ilike(propertyName, value, MatchMode.EXACT)).list();
		}
	}
	
	/**
	 * Retorna a lista de entidades filtradas pelo exemplo passado.
	 * @param <T>
	 * @param classe
	 * @param filtro
	 * @return lista de entidades
	 */
	public <T extends BaseEntity> List<T> retrieveByExample(Class<T> classe, BaseEntity filtro){
		return retrieveByExample(classe, filtro, null, false);
	}
	
	/**
	 * Retorna a lista de entidades filtradas pelos parametros informados
	 * @param <T>
	 * @param classe
	 * @param filtro
	 * @param matchMode
	 * @param ignoreCase
	 * @return lista de entidades
	 */
	public <T extends BaseEntity> List<T> retrieveByExample(Class<T> classe, BaseEntity filtro, MatchMode matchMode, boolean ignoreCase){
		Example example = Example.create(filtro);
		if(matchMode != null) example = example.enableLike(matchMode);
		if(ignoreCase) example = example.ignoreCase();
		return getSession().createCriteria(classe).add(example).list();
	}
}
