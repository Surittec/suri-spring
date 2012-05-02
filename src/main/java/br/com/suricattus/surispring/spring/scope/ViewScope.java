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

	@SuppressWarnings("unchecked")
	public Object remove(String name) {
		Object instance = getViewMap().remove(name);
		if(instance != null) {
			Map<String,Runnable> callbacks = (Map<String, Runnable>) getViewMap().get(getCallbacksIdentifier());
			if(callbacks != null) {
				Runnable callback = callbacks.remove(name);
				if(callback != null){
					callback.run();
				}
			}
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public void registerDestructionCallback(String name, Runnable runnable) {
		((Map<String, Runnable>) getViewMap().get(getCallbacksIdentifier())).put(name, runnable);
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
	
	protected Map<String,Object> getViewMap() {
		return FacesContext.getCurrentInstance().getViewRoot().getViewMap();
	}

	@SuppressWarnings("unchecked")
	protected Map<String,Object> getBeansMap() {
		return (Map<String, Object>) getViewMap().get(getBeansIdentifier());
	}
	
}
