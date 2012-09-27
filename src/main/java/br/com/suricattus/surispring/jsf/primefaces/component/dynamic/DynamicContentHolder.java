package br.com.suricattus.surispring.jsf.primefaces.component.dynamic;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.primefaces.model.StreamedContent;
import org.springframework.stereotype.Component;

import br.com.suricattus.surispring.jsf.util.FacesUtils;
import br.com.suricattus.surispring.spring.scope.annotation.SessionScoped;

/**
 * Holder para dynamic content
 * 
 * @author Lucas Lins
 *
 */
@Component("dynamicContentHolder")
@SessionScoped
public class DynamicContentHolder implements Serializable{
	
	/* *************************
	 * ATTRIBUTES
	 * *************************/
	
	private static final long serialVersionUID = 1L;
	
	private static final int TIME_OUT = 5 * 60 * 1000;
	
	private Map<String, StreamedContent> streamedMap;
	private Map<Date, String> streamedMapLineTime;
	
	/* *************************
	 * INIT
	 * *************************/
	
	@PostConstruct
	public void postContruct(){
		streamedMap = new HashMap<String, StreamedContent>();
		streamedMapLineTime = new HashMap<Date, String>();
	}
	
	/* *************************
	 * PUBLIC METHODS
	 * *************************/
	
	public void put(String key, StreamedContent content){
		synchronized (streamedMap) {
			streamedMap.put(key, content);
			streamedMapLineTime.put(new Date(), key);
		}
	}
	
	public synchronized StreamedContent get(String key){
		synchronized (streamedMap) {
			StreamedContent content = streamedMap.get(key);
			cleanUp(key);
			return content;
		}
	}
	
	/* *************************
	 * PRIVATE METHODS
	 * *************************/
	
	private void cleanUp(String key){
		streamedMap.remove(key);
		
		long expirationTime = Calendar.getInstance().getTimeInMillis() - TIME_OUT;
		
		Map<String, Object> sessionMap = FacesUtils.getSessionMap();
		
		for(Date time : streamedMapLineTime.keySet()){
			if(streamedMapLineTime.get(time).equals(key)){
				streamedMapLineTime.remove(time);
			}else if(time.getTime() - expirationTime < 0){
				String expiredKey = streamedMapLineTime.get(time);
				streamedMap.remove(expiredKey);
				streamedMapLineTime.remove(time);
				sessionMap.remove(expiredKey);
			}
		}
	}
	
}
