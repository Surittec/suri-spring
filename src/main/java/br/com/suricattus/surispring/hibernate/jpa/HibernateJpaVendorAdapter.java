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
package br.com.suricattus.surispring.hibernate.jpa;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.cfg.Environment;

/**
 * Hibernate JPA Adapter
 * 
 * @author Lucas Lins
 *
 */
public class HibernateJpaVendorAdapter extends org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter{

	private String dialect;
	private String hbm2ddl = "validate";
	private String cache_provider_class;
	private boolean use_second_level_cache = false;
	private boolean show_sql = false;
	private boolean format_sql = false;
	private String transaction_manager_lookup_class;
	
	
	@Override
	public Map<String, Object> getJpaPropertyMap() {
		Map<String, Object> jpaProperties = new HashMap<String, Object>();
		
		if(getDialect() != null) jpaProperties.put(Environment.DIALECT, getDialect());
		
		if(getHbm2ddl() != null) jpaProperties.put(Environment.HBM2DDL_AUTO, getHbm2ddl());
		
		if(getCache_provider_class() != null) jpaProperties.put(Environment.CACHE_PROVIDER, getCache_provider_class());
		
		if(isUse_second_level_cache()) jpaProperties.put(Environment.USE_SECOND_LEVEL_CACHE, "true");
		
		if(isShow_sql()) jpaProperties.put(Environment.SHOW_SQL, "true");
		
		if(isFormat_sql()) jpaProperties.put(Environment.FORMAT_SQL, "true");

		if(getTransaction_manager_lookup_class() != null) jpaProperties.put(Environment.TRANSACTION_MANAGER_STRATEGY, getTransaction_manager_lookup_class());
		
		return jpaProperties;
	}


	/* *****************************
	 * GETTERS/SETTERS
	 * *****************************/
	
	public String getDialect() {
		return dialect;
	}
	public void setDialect(String dialect) {
		this.dialect = dialect;
	}
	public String getCache_provider_class() {
		return cache_provider_class;
	}
	public void setCache_provider_class(String cache_provider_class) {
		this.cache_provider_class = cache_provider_class;
	}
	public boolean isShow_sql() {
		return show_sql;
	}
	public void setShow_sql(boolean show_sql) {
		this.show_sql = show_sql;
	}
	public boolean isFormat_sql() {
		return format_sql;
	}
	public void setFormat_sql(boolean format_sql) {
		this.format_sql = format_sql;
	}
	public String getTransaction_manager_lookup_class() {
		return transaction_manager_lookup_class;
	}
	public void setTransaction_manager_lookup_class(
			String transaction_manager_lookup_class) {
		this.transaction_manager_lookup_class = transaction_manager_lookup_class;
	}
	public String getHbm2ddl() {
		return hbm2ddl;
	}
	public void setHbm2ddl(String hbm2ddl) {
		this.hbm2ddl = hbm2ddl;
	}
	public boolean isUse_second_level_cache() {
		return use_second_level_cache;
	}
	public void setUse_second_level_cache(boolean use_second_level_cache) {
		this.use_second_level_cache = use_second_level_cache;
	}
	
}
