package br.com.suricattus.surispring.spring.scope.holder;

import java.io.Serializable;
import java.util.Map;

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
