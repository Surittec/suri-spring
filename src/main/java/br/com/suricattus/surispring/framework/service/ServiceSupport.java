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

import org.springframework.transaction.annotation.Transactional;

/**
 * Classe suporte aos Services transacionais do projeto. 
 * 
 * @author Lucas Lins
 * 
 */
@SuppressWarnings("unchecked")
@Transactional(rollbackFor = Exception.class)
public abstract class ServiceSupport extends GenericRetrieveService {

	private static final long serialVersionUID = 1L;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PUBLIC METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Metodo Responsavel por salvar um novo objeto na base de dados 
	 * 
	 * @param obj objeto a ser persistido
	 */
	public <T, PK extends Serializable> PK save(T novoObj){
		return (PK) getSession().save(novoObj);
	}
	
	/**
	 * Metodo Responsavel por salvar [ou alterar] um objeto na base de dados, a escolha a
	 * feita com base na verificacao se o objeto ja corresponde a um objeto persistente. 
	 * 
	 * @param obj objeto a ser persistido
	 */
	public <T> void saveOrUpdate(T ... objs){
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
	public <T> void saveOrUpdate(Collection<T>  objs){
		for(T t : objs){
			getSession().saveOrUpdate(t);
		}
	}
	
	/**
	 * Metodo responsavel por excluir uma lista de entidades da base de dados
	 * 
	 * @param ids Identificadores das entidades a serem removidas da base de dados
	 */
	public <T, PK extends Serializable> void delete(Class<T> classe, PK ... ids){
		if (ids != null && ids.length > 0){
			for (PK id : ids) {
				if (id != null){
					T obj = retrieve(classe, id);
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
	public <T> void delete(T ... objs){
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
	public <T> void delete(Collection<T> objs){
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
	public <T> void update(T ... objs){		
		saveOrUpdate(objs);
	}
	
	
	/**
	 * Metodo Responsavel por fazer o merge com um objeto na base de dados, a escolha 
	 * 
	 * @param obj objeto a ser sincronizado
	 * @return objeto sincronizado com a base
	 */
	public <T> T merge(T novoObj){
		T objMerged = (T)getSession().merge(novoObj);
		return objMerged;
	}
	
	/**
	 * Metodo responsavel por sincronizar um objeto da sessao com o atual no banco.
	 * 
	 * @param obj objeto para refresh
	 */
	public <T> void refresh(T obj){
		getSession().refresh(obj);
	}
	
	/**
	 * Metodo responsavel por remover um objeto da sessao.
	 * 
	 * @param obj objeto para remocao
	 */
	public <T> void evict(T obj){
		getSession().evict(obj);
	}
	
	/**
	 * Metodo responsavel por verificar se o objeto informado esta presente na sessao do hibernate.
	 * @param <T>
	 * @param obj
	 * @return boolean
	 */
	public <T> boolean contains(T obj){
		return getSession().contains(obj);
	}
	
}
