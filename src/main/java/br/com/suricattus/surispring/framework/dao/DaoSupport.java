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
package br.com.suricattus.surispring.framework.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import br.com.suricattus.surispring.framework.domain.BaseEntity;
import br.com.suricattus.surispring.framework.util.SearchSort;

/**
 * Classe suporte aos DAOs do projeto.
 * 
 * @author Lucas Lins
 * 
 * @param <T>
 * @param <PK>
 */
@SuppressWarnings("unchecked")
@Configurable(autowire = Autowire.BY_TYPE)
public abstract class DaoSupport <T extends BaseEntity, PK extends Serializable>{
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ATTRIBUTES
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	@Autowired
	private SessionFactory sessionFactory;
	
	protected Class<T> tipo;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// CONSTRUCTORS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Construtor que ja obtem de forma automatica o tipo do DAO.
	 */
	public DaoSupport(){
		Type superclass = getClass().getGenericSuperclass();
		if(superclass instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType)superclass;
			if(parameterizedType.getActualTypeArguments().length > 0){
				tipo = (Class<T>)parameterizedType.getActualTypeArguments()[0];
			}
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PROTECTED METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Hibernate session
	 * 
	 * @return hibernate session
	 */
	protected Session getSession(){
		return sessionFactory.getCurrentSession();
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PUBLIC METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Metodo Responsavel por salvar um novo objeto na base de dados 
	 * 
	 * @param obj objeto a ser persistido
	 */
	public PK save(T novoObj){
		return (PK) getSession().save(novoObj);
	}
	
	/**
	 * Metodo Responsavel por salvar [ou alterar] um objeto na base de dados, a escolha a
	 * feita com base na verificacao se o objeto ja corresponde a um objeto persistente. 
	 * 
	 * @param obj objeto a ser persistido
	 */
	public void saveOrUpdate(T ... objs){
		for(T t : objs){
			getSession().saveOrUpdate(t);
		}
	}
	
	/**
	 * Metodo Responsavel por salvar [ou alterar] um objeto na base de dados, a escolha e
	 * feita com base na verificacao se o objeto corresponde a um objeto persistente. 
	 * 
	 * @param obj objeto a ser persistido
	 */
	public void saveOrUpdate(Collection<T>  objs){
		for(T t : objs){
			getSession().saveOrUpdate(t);
		}
	}
	
	/**
	 * Metodo responsavel por excluir uma lista de entidades da base de dados
	 * 
	 * @param ids Identificadores das entidades a serem removidas da base de dados
	 */
	public void delete(PK ... ids){
		if (ids != null && ids.length > 0){
			for (PK id : ids) {
				if (id != null){
					T obj = retrieve(id);
					if (obj != null){
						Object objMerged = getSession().merge(obj);
						getSession().delete(objMerged);
					}
				}
			}
		}
	}
	
	/**
	 * Metodo Responsavel por excluir um conjunto de entidades persistentes da base de dados
	 * 
	 * @param objs Objetos a serem removidos da base de dados
	 */
	public void delete(T ... objs){
		for(T t : objs){
			Object objMerged = getSession().merge(t);
			getSession().delete(objMerged);
		}
	}
	
	/**
	 * Metodo Responsavel por excluir um conjunto de entidades persistentes da base de dados
	 * 
	 * @param objs Objetos a serem removidos da base de dados
	 */
	public void delete(Collection<T> objs){
		for(T t : objs){
			Object objMerged = getSession().merge(t);
			getSession().delete(objMerged);
		}
	}
	
	/**
	 * Metodo responsavel por alterar os dados de um objeto ja persistido no banco de dados
	 * 
	 * @param obj Objeto a ser alterado na base de dados
	 */
	public void update(T ... objs){		
		saveOrUpdate(objs);
	}
	
	/**
	 * Metodo responsavel por recuperar um objeto da base de dados
	 * 
	 * @param id Identificador do objeto a ser recuperado
	 * @return Objeto referente ao identificador informado
	 */
	public T retrieve(PK id){
		return (T)getSession().get(tipo, id);
	}
	
	/**
	 * Metodo responsavel por recuperar todos os objeto de uma tabela da base de dados
	 * 
	 * @return todos os objetos da classe
	 */
	public List<T> retrieveAll(){
		return getSession().createCriteria(tipo).setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * Metodo Responsavel por recuperar todos os objeto de uma tabela da base de dados de forma ordenada
	 * 
	 * @param sort asc - ascending or desc - descending
	 * @param propertyName property to be ordered
	 * @return todos os objetos da classe
	 */
	public List<T> retrieveAll(SearchSort sort, String ... propertiesName){
		Criteria criteria = getSession().createCriteria(tipo);
		if(SearchSort.ASCENDING.equals(sort)){
			for(String property : propertiesName){
				criteria.addOrder(Order.asc(property));
			}
		}
		else if(SearchSort.DESCENDING.equals(sort)){
			for(String property : propertiesName){
				criteria.addOrder(Order.desc(property));
			}
		}
		return criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();
	}
	
	/**
	 * Metodo responsavel por recuperar todos os objeto de uma tabela da base de dados
	 * 
	 * @param nome da propriedade, valor da propriedade(filtro), tipo de busca (comeca ou contem)
	 * @return todos os objetos da classe que se adequam aos filtros
	 */
	public  List<T> retrieveByProperty(String propertyName, Object value){
		return getSession().createCriteria(tipo).add(Restrictions.eq(propertyName, value)).list();
	}
	
	/**
	 * Metodo responsavel por recuperar todos os objeto de uma tabela da base de dados
	 * 
	 * @param nome da propriedade, valor da propriedade(filtro), tipo de busca (comeca ou contem)
	 * @return todos os objetos da classe que se adequam aos filtros
	 */
	public  List<T> retrieveByProperty(String propertyName, String value){
		return retrieveByProperty(propertyName, value, null);
	}
	
	/**
	 * Metodo responsavel por recuperar todos os objeto de uma tabela da base de dados
	 * 
	 * @param nome da propriedade, valor da propriedade(filtro), tipo de busca (comeca ou contem)
	 * @return todos os objetos da classe que se adequam aos filtros
	 */
	public  List<T> retrieveByProperty(String propertyName, String value, MatchMode matchMode){
		if (matchMode != null){
			return getSession().createCriteria(tipo).add(Restrictions.ilike(propertyName, value, matchMode)).list();
		}else{
			return getSession().createCriteria(tipo).add(Restrictions.ilike(propertyName, value, MatchMode.EXACT)).list();
		}
	}
	
	/**
	 * Metodo responsavel por recuperar todos os objetos de uma tabela da base de dados de acordo
	 * com o exemplo passado. 
	 * 
	 * @param filtro
	 * @return lista
	 */
	public List<T> retrieveByExample(T filtro){
		return retrieveByExample(filtro, null, false);
	}
	
	/**
	 * Metodo responsavel por recuperar todos os objetos de uma tabela da base de dados de acordo
	 * com o exemplo passado. 
	 * 
	 * @param filtro
	 * @param matchMode
	 * @param ignoreCase
	 * @return lista
	 */
	public List<T> retrieveByExample(T filtro, MatchMode matchMode, boolean ignoreCase){
		Example example = Example.create(filtro);
		
		if(matchMode != null){
			example = example.enableLike(matchMode);
		}
		
		if(ignoreCase){
			example = example.ignoreCase();
		}
		
		return getSession().createCriteria(tipo).add(example).list();
	}
	
	
	/**
	 * Metodo Responsavel por fazer o merge com um objeto na base de dados, a escolha 
	 * 
	 * @param obj objeto a ser sincronizado
	 * @return objeto sincronizado com a base
	 */
	public T merge(T novoObj){
		T objMerged = (T)getSession().merge(novoObj);
		return objMerged;
	}
	
	/**
	 * Metodo responsavel por sincronizar um objeto da sessao com o atual no banco.
	 * 
	 * @param obj objeto para refresh
	 */
	public void refresh(T obj){
		getSession().refresh(obj);
	}
	
	/**
	 * Metodo responsavel por remover um objeto da sessao.
	 * 
	 * @param obj objeto para remocao
	 */
	public void evict(T obj){
		getSession().evict(obj);
	}
	
}
