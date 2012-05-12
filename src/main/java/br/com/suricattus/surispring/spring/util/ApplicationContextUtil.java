package br.com.suricattus.surispring.spring.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component("br.com.suricattus.surispring.spring.util.ApplicationContextUtil")
public class ApplicationContextUtil implements ApplicationContextAware{
	
	private static ApplicationContext applicationContext;
	
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ApplicationContextUtil.applicationContext = applicationContext;
	}
	
	public static ApplicationContext getContext(){
		return applicationContext;
	}
	
}
