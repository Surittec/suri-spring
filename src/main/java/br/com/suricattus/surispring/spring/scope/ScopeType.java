package br.com.suricattus.surispring.spring.scope;

import org.springframework.web.context.WebApplicationContext;

public interface ScopeType {
	
	String APPLICATION = WebApplicationContext.SCOPE_APPLICATION;
	
	String SESSION = WebApplicationContext.SCOPE_SESSION;
	
	String VIEW_ACCESS = "viewAccess";
	
	String VIEW = "view";
	
	String REQUEST = WebApplicationContext.SCOPE_REQUEST;
	
}
