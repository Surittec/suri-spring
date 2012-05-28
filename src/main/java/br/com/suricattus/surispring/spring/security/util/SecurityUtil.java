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
package br.com.suricattus.surispring.spring.security.util;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.context.ApplicationContext;
import org.springframework.expression.Expression;
import org.springframework.security.access.expression.ExpressionUtils;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;

import br.com.suricattus.surispring.jsf.util.FacesUtils;

/**
 * Spring Security Utility Class
 * 
 * @author Lucas Lins
 * @author Dominik Dorn - http://www.dominikdorn.com/
 * @see http://code.google.com/p/spring-security-facelets-taglib/
 */
public abstract class SecurityUtil {
	
	/**
	 * Method that checks if the user has the given access expression.
	 * 
	 * @see Spring Security Expression-Based Access Control 
	 * @param access
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean isAuthorized(String access){
		ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext((ServletContext)FacesUtils.getExternalContext().getContext());
		Map<String, SecurityExpressionHandler> expressionHandlres = appContext.getBeansOfType(SecurityExpressionHandler.class);
		SecurityExpressionHandler handler = (SecurityExpressionHandler)expressionHandlres.values().toArray()[0];
		Expression accessExpression = handler.getExpressionParser().parseExpression(access);
		
		FilterInvocation f = new FilterInvocation(FacesUtils.getRequest(), FacesUtils.getResponse(), new FilterChain() {
			public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
				throw new UnsupportedOperationException();
			}
		});
		
		return ExpressionUtils.evaluateAsBoolean(accessExpression, handler.createEvaluationContext(SecurityContextHolder.getContext().getAuthentication(), f));
	}
	
	/**
	 * Method that checks if the user holds <b>any</b> of the given roles.
	 * Returns <code>true, when the first match is found, <code>false</code> if
	 * no match is found and also <code>false</code> if no roles are given
	 * 
	 * @param grantedRoles a comma seperated list of roles
	 * @return true if any of the given roles are granted to the current user, false otherwise
	 */
	public static boolean hasAnyRole(final String grantedRoles) {
		Set<String> parsedAuthorities = parseAuthorities(grantedRoles);
		if (parsedAuthorities.isEmpty()) return false;
		
		Set<String> userAuthorities = getUserAuthorities();
		
		for(String ga : parsedAuthorities) {
			if(userAuthorities.contains(ga)) return true;
		}
		return false;
	}
	
	/**
	 * Method that checks if the user holds <b>all</b> of the given roles.
	 * Returns <code>true</code>, iff the user holds all roles,
	 * <code>false</code> if no roles are given or the first non-matching role
	 * is found
	 * 
	 * @param requiredRoles a comma seperated list of roles
	 * @return true if all of the given roles are granted to the current user,
	 *         false otherwise or if no roles are specified at all.
	 */
	public static boolean hasAllRoles(final String requiredRoles) {
		Set<String> requiredAuthorities = parseAuthorities(requiredRoles);
		if (requiredAuthorities.isEmpty()) return false;
		
		Set<String> userAuthorities = getUserAuthorities();
		
		for (String requiredAuthority : requiredAuthorities) {
			if (!userAuthorities.contains(requiredAuthority)) return false;
		}
		return true;
	}

	/**
	 * Method that checks if <b>none</b> of the given roles is hold by the user.
	 * Returns <code>true</code> if no roles are given, or none of the given
	 * roles match the users roles. Returns <code>false</code> on the first
	 * matching role.
	 * 
	 * @param notGrantedRoles
	 *            a comma seperated list of roles
	 * @return true if none of the given roles is granted to the current user,
	 *         false otherwise
	 */
	public static boolean hasNoRole(final String notGrantedRoles) {
		Set<String> parsedAuthorities = parseAuthorities(notGrantedRoles);
		if (parsedAuthorities.isEmpty()) return true;

		Set<String> userAuthorities = getUserAuthorities();
		
		for(String notGrantedAuthority : parsedAuthorities){
			if(userAuthorities.contains(notGrantedAuthority)) return false;
		}
		return true;
	}

	/**
	 * Method checks if the user is authenticated. Returns <code>true</code> if
	 * the user is <b>not</b> anonymous. Returns <code>false</code> if the user
	 * <b>is</b> anonymous.
	 * 
	 * @return
	 */
	public static boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return false;
		}
		return authentication.isAuthenticated();
	}

	/**
	 * Method checks if the user is anonymous. Returns <code>true</code> if the
	 * user <b>is</b> anonymous. Returns <code>false</code> if the user is
	 * <b>not</b> anonymous.
	 * 
	 * @return
	 */
	public static boolean isAnonymous() {
		return !isAuthenticated();
	}
	
	/**
	 * List user authorities.
	 *  
	 * @return user authorities
	 */
	@SuppressWarnings("unchecked")
	public static Set<String> getUserAuthorities() {
		if (SecurityContextHolder.getContext() == null) return Collections.EMPTY_SET;
		
		Authentication currentUser = SecurityContextHolder.getContext().getAuthentication();
		if (currentUser == null) return Collections.EMPTY_SET;
		
		Collection<? extends GrantedAuthority> grantedAauthorities = currentUser.getAuthorities();
		if (grantedAauthorities == null || grantedAauthorities.isEmpty()) return Collections.EMPTY_SET;
		
		Set<String> authorities = new TreeSet<String>();
		for(GrantedAuthority ga : grantedAauthorities) authorities.add(ga.getAuthority());
		return authorities;
	}
	
	//------------------------------------------
	// PRIVATE METHODS
	//------------------------------------------
	
	private static Set<String> parseAuthorities(String grantedRoles) {
		Set<String> parsedAuthorities = new TreeSet<String>();
		if (!StringUtils.hasText(grantedRoles)) return parsedAuthorities;
		
		String[] roles = grantedRoles.split(",");
		for (String role : roles) parsedAuthorities.add(role.trim());
		return parsedAuthorities;
	}
}
