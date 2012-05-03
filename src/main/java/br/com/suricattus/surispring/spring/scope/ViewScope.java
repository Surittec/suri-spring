package br.com.suricattus.surispring.spring.scope;

import java.util.Map;

import javax.faces.context.FacesContext;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.web.context.request.FacesRequestAttributes;

public class ViewScope implements Scope{

	public static final String CALLBACKS = ViewScope.class.getName() + ".callbacks";
	public static final String BEANS = ViewScope.class.getName() + ".beans";
	
	public Object get(String name, ObjectFactory<?> objectFactory) {
		Map<String, Object> beansMap = getBeansMap();
		Object instance = beansMap.get(name);
		if(instance == null) {
			instance = objectFactory.getObject();
			synchronized (beansMap) {
				beansMap.put(name,instance);
			}
		}
		return instance;
	}

	public Object remove(String name) {
		Object instance = getBeansMap().remove(name);
		if(instance != null) {
			Runnable callback = getCallbacksMap().remove(name);
			if(callback != null) {
				callback.run();
			}
		}
		return instance;
	}

	public void registerDestructionCallback(String name, Runnable runnable) {
		getCallbacksMap().put(name, runnable);
	}

	public Object resolveContextualObject(String name) {
		return new FacesRequestAttributes(FacesContext.getCurrentInstance()).resolveReference(name);
	}
	
	public String getConversationId() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		FacesRequestAttributes facesRequestAttributes = new FacesRequestAttributes(facesContext);
		return facesRequestAttributes.getSessionId() + "-" + facesContext.getViewRoot().getViewId();
	}
	
	/* **************************
	 * PROTECTED METHODS
	 * **************************/
	
	protected String getBeansIdentifier(){
		return BEANS;
	}
	
	protected String getCallbacksIdentifier(){
		return CALLBACKS;
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Object> getBeansMap() {
		return (Map<String, Object>) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get(getBeansIdentifier());
	}
	
	@SuppressWarnings("unchecked")
	protected Map<String,Runnable> getCallbacksMap() {
		return (Map<String, Runnable>) FacesContext.getCurrentInstance().getViewRoot().getViewMap().get(getCallbacksIdentifier());
	}
	
}
