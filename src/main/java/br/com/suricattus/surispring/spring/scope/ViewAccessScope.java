package br.com.suricattus.surispring.spring.scope;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import br.com.suricattus.surispring.spring.scope.holder.ViewAccessScopeHolder;

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
		ApplicationContext applicationContext = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
		return applicationContext.getBean(ViewAccessScopeHolder.class);
	}
}
