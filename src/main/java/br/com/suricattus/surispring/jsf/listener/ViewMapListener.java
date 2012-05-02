package br.com.suricattus.surispring.jsf.listener;

import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructViewMapEvent;
import javax.faces.event.PreDestroyViewMapEvent;
import javax.faces.event.SystemEvent;

import org.springframework.context.ApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

import br.com.suricattus.surispring.spring.scope.ViewAccessScope;
import br.com.suricattus.surispring.spring.scope.ViewScope;
import br.com.suricattus.surispring.spring.scope.holder.ViewAccessScopeHolder;

/**
 * 
 * 
 * @author lucas lins
 * 
 */
public class ViewMapListener implements javax.faces.event.ViewMapListener {
	
	public void processEvent(SystemEvent event) throws AbortProcessingException {
		
		if (event instanceof PostConstructViewMapEvent) {
			PostConstructViewMapEvent viewMapEvent = (PostConstructViewMapEvent) event;
			UIViewRoot viewRoot = (UIViewRoot) viewMapEvent.getComponent();
			
			initViewMaps(viewRoot);
			
		} else if (event instanceof PreDestroyViewMapEvent) {
			PreDestroyViewMapEvent viewMapEvent = (PreDestroyViewMapEvent) event;
			UIViewRoot viewRoot = (UIViewRoot) viewMapEvent.getComponent();
			
			destroyViewBeans(viewRoot);
			holdViewAccessBeans(viewRoot);
		}
	}
	
	public boolean isListenerForSource(Object source) {
		return source instanceof UIViewRoot;
	}
	
	/* ************************
	 * PRIVATE METHODS
	 * ************************/
	
	private void initViewMaps(UIViewRoot viewRoot){
		viewRoot.getViewMap().put(ViewScope.BEANS, new HashMap<String, Object>());
		viewRoot.getViewMap().put(ViewScope.CALLBACKS, new HashMap<String, Runnable>());
		
		viewRoot.getViewMap().put(ViewAccessScope.BEANS, new HashMap<String, Object>());
		viewRoot.getViewMap().put(ViewAccessScope.CALLBACKS, new HashMap<String, Runnable>());
	}
	
	@SuppressWarnings("unchecked")
	private void destroyViewBeans(UIViewRoot viewRoot){
		Map<String, Runnable> callbacks = (Map<String, Runnable>) viewRoot.getViewMap().get(ViewScope.CALLBACKS);
		if (callbacks != null) {
			for (Runnable c : callbacks.values()) c.run();
			callbacks.clear();
		}
	}
	
	@SuppressWarnings("unchecked")
	private void holdViewAccessBeans(UIViewRoot viewRoot){
		ApplicationContext applicationContext = FacesContextUtils.getWebApplicationContext(FacesContext.getCurrentInstance());
		ViewAccessScopeHolder holder = applicationContext.getBean(ViewAccessScopeHolder.class);
		holder.register((Map<String, Object>)viewRoot.getViewMap().get(ViewAccessScope.BEANS), (Map<String, Runnable>)viewRoot.getViewMap().get(ViewAccessScope.CALLBACKS));
	}
}
