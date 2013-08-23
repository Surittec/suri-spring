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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import br.com.suricattus.surispring.framework.domain.BaseEntity;
import br.com.suricattus.surispring.framework.util.SearchSort;

/**
 * Servico generico que auxilia na busca de entidades persistentes.
 * Pode ser adicionado no applicationContext de sua aplicacao.
 * 
 * @author Lucas Lins
 * 
 */
public class GenericRetrieveService implements Serializable{
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ATTRIBUTES
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static final long serialVersionUID = 1L;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// DEFAULT METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	Session getSession(){
		return (Session)entityManager.getDelegate();
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
	@SuppressWarnings("unchecked")
    public <T extends BaseEntity> T retrieve(Class<T> classe, Serializable id){
		return (T)getSession().get(classe, id);
	}
	
	/**
	 * Retorna a lista de entidades do tipo <T>
	 * @param <T>
	 * @param classe
	 * @return lista de entidades
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> retrieveByProperty(Class<T> classe, String propertyName, String value, MatchMode matchMode){
		if (matchMode != null){
			return getSession().createCriteria(classe).add(Restrictions.ilike(propertyName, value, matchMode)).list();
		}else{
			return getSession().createCriteria(classe).add(Restrictions.ilike(propertyName, value, MatchMode.EXACT)).list();
		}
	}
	
	/**
	 * Retorna a entidade do tipo <T> filtrada pelos parametros informados
	 * @param <T>
	 * @param classe
	 * @param propertyName
	 * @param value
	 * @return lista de entidades
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T retrieveUniqueByProperty(Class<T> classe, String propertyName, Object value){
		return (T) getSession().createCriteria(classe).add(Restrictions.eq(propertyName, value)).uniqueResult();
	}
	
	/**
	 * Retorna a entidade do tipo <T> filtrada pelos parametros informados. Difere do metodo
	 * retrieveUniqueByProperty(Class<T> classe, String propertyName, Object value) pelo ultimo parametro definido ja como 
	 * String e utiliza o {@link MatchMode} exato para a busca. 
	 * 
	 * @param <T>
	 * @param classe
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public <T extends BaseEntity> T retrieveUniqueByProperty(Class<T> classe, String propertyName, String value){
		return retrieveUniqueByProperty(classe, propertyName, value, null);
	}
	
	/**
	 * Retorna a entidade filtrada pelos parametros informadas.
	 * 
	 * @param <T>
	 * @param classe
	 * @param propertyName
	 * @param value
	 * @param matchMode
	 * @return lista de entidades.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T retrieveUniqueByProperty(Class<T> classe, String propertyName, String value, MatchMode matchMode){
		if (matchMode != null){
			return (T) getSession().createCriteria(classe).add(Restrictions.ilike(propertyName, value, matchMode)).uniqueResult();
		}else{
			return (T) getSession().createCriteria(classe).add(Restrictions.ilike(propertyName, value, MatchMode.EXACT)).uniqueResult();
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
	@SuppressWarnings("unchecked")
    public <T extends BaseEntity> List<T> retrieveByExample(Class<T> classe, BaseEntity filtro, MatchMode matchMode, boolean ignoreCase){
		Example example = Example.create(filtro);
		if(matchMode != null) example = example.enableLike(matchMode);
		if(ignoreCase) example = example.ignoreCase();
		return getSession().createCriteria(classe).add(example).list();
	}
	
	/**
	 * Retorna a entidade filtradas pelo exemplo passado.
	 * @param <T>
	 * @param classe
	 * @param filtro
	 * @return lista de entidades
	 */
	public <T extends BaseEntity> T retrieveUniqueByExample(Class<T> classe, BaseEntity filtro){
		return retrieveUniqueByExample(classe, filtro, null, false);
	}
	
	/**
	 * Retorna a entidade filtrada pelos parametros informados
	 * @param <T>
	 * @param classe
	 * @param filtro
	 * @param matchMode
	 * @param ignoreCase
	 * @return lista de entidades
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T retrieveUniqueByExample(Class<T> classe, BaseEntity filtro, MatchMode matchMode, boolean ignoreCase){
		Example example = Example.create(filtro);
		if(matchMode != null) example = example.enableLike(matchMode);
		if(ignoreCase) example = example.ignoreCase();
		return (T) getSession().createCriteria(classe).add(example).uniqueResult();
	}
	
	/**
	 * Retorna a lista de entidades de acordo com a namedQuery informada.
	 * @param <T>
	 * @param classe
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> List<T>  retrieveByNamedQuery(Class<T> classe, String namedQuery, Object ... params){
		Query query = getSession().getNamedQuery(namedQuery);
		if(params != null) for(int i = 1; i <= params.length; i++) query.setParameter(i, params[i - 1]);
		return query.list();
	}
	
	/**
	 * Retorna a lista de entidades de acordo com a namedQuery informada.
	 * @param <T>
	 * @param classe
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends BaseEntity> List<T>  retrieveByNamedQuery(Class<T> classe, String namedQuery, Map<String, Object> params){
		Query query = getSession().getNamedQuery(namedQuery);
		if(params != null){
			for(String paramName : params.keySet()){
				Object param = params.get(paramName);
				if(param instanceof Collection){
					query.setParameterList(paramName, (Collection)params.get(paramName));
				}else if(param instanceof Object[]){
					query.setParameterList(paramName, (Object[])params.get(paramName));
				}else{
					query.setParameter(paramName, params.get(paramName));
				}
			}
		}
		return query.list();
	}
	
	/**
	 * Retorna a entidade de acordo com a namedQuery informada.
	 * @param <T>
	 * @param classe
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends BaseEntity> T  retrieveUniqueByNamedQuery(Class<T> classe, String namedQuery, Object ... params){
		Query query = getSession().getNamedQuery(namedQuery);
		if(params != null){
			for(int i = 0; i < params.length; i++){ 
				query.setParameter(i, params[i]);
			}
		}
		return (T) query.uniqueResult();
	}
	
	/**
	 * Retorna a entidade de acordo com a namedQuery informada.
	 * @param <T>
	 * @param classe
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T extends BaseEntity> T  retrieveUniqueByNamedQuery(Class<T> classe, String namedQuery, Map<String, Object> params){
		Query query = getSession().getNamedQuery(namedQuery);
		if(params != null){
			for(String paramName : params.keySet()){
				Object param = params.get(paramName);
				if(param instanceof Collection){
					query.setParameterList(paramName, (Collection)params.get(paramName));
				}else if(param instanceof Object[]){
					query.setParameterList(paramName, (Object[])params.get(paramName));
				}else{
					query.setParameter(paramName, params.get(paramName));
				}
			}
		}
		return (T) query.uniqueResult();
	}
	
	/**
	 * Retorna o resultado de acordo com a namedQuery informada.
	 * @param classe
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	public List<?> retrieveByNamedQuery(String namedQuery, Object ... params){
		Query query = getSession().getNamedQuery(namedQuery);
		if(params != null) for(int i = 1; i <= params.length; i++) query.setParameter(i, params[i - 1]);
		return query.list();
	}
	
	/**
	 * Retorna o resultado de acordo com a namedQuery informada.
	 * @param classe
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<?> retrieveByNamedQuery(String namedQuery, Map<String, Object> params){
		Query query = getSession().getNamedQuery(namedQuery);
		if(params != null){
			for(String paramName : params.keySet()){
				Object param = params.get(paramName);
				if(param instanceof Collection){
					query.setParameterList(paramName, (Collection)params.get(paramName));
				}else if(param instanceof Object[]){
					query.setParameterList(paramName, (Object[])params.get(paramName));
				}else{
					query.setParameter(paramName, params.get(paramName));
				}
			}
		}
		return query.list();
	}
	
	/**
	 * Retorna o resultado unico de acordo com a namedQuery informada.
	 * @param classe
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	public Object retrieveUniqueByNamedQuery(String namedQuery, Object ... params){
		Query query = getSession().getNamedQuery(namedQuery);
		if(params != null) for(int i = 0; i < params.length; i++) query.setParameter(i, params[i]);
		return query.uniqueResult();
	}
	
	/**
	 * Retorna o resultado unico de acordo com a namedQuery informada.
	 * @param classe
	 * @param namedQuery
	 * @param params
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Object retrieveUniqueByNamedQuery(String namedQuery, Map<String, Object> params){
		Query query = getSession().getNamedQuery(namedQuery);
		if(params != null){
			for(String paramName : params.keySet()){
				Object param = params.get(paramName);
				if(param instanceof Collection){
					query.setParameterList(paramName, (Collection)params.get(paramName));
				}else if(param instanceof Object[]){
					query.setParameterList(paramName, (Object[])params.get(paramName));
				}else{
					query.setParameter(paramName, params.get(paramName));
				}
			}
		}
		return query.uniqueResult();
	}
}
