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
package br.com.suricattus.surispring.jsf.exception;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.OptimisticLockException;

import org.apache.taglibs.standard.lang.jstl.ELException;

import br.com.suricattus.surispring.jsf.util.FacesUtils;

import com.ocpsoft.pretty.PrettyException;
import com.ocpsoft.pretty.faces.util.FacesMessagesUtils;
import com.sun.faces.context.FacesFileNotFoundException;

/**
 * Pretty Exceptions Handler
 * 
 * @author Lucas Lins
 *
 */
public class PrettyExceptionHandler extends ExceptionHandlerWrapper {
	
	private final FacesMessagesUtils messagesUtils = new FacesMessagesUtils();
	private final javax.faces.context.ExceptionHandler wrapped;
	
	public PrettyExceptionHandler(final javax.faces.context.ExceptionHandler wrapped) {
		this.wrapped = wrapped;
	}
	
	@Override
	public javax.faces.context.ExceptionHandler getWrapped() {
		return this.wrapped;
	}
	
	@Override
	public void handle() throws FacesException {
		for (final Iterator<ExceptionQueuedEvent> it = getUnhandledExceptionQueuedEvents().iterator(); it.hasNext();) {
			Throwable t = it.next().getContext().getException();
			while ((t instanceof FacesException || t instanceof ELException) && t.getCause() != null) {
				t = t.getCause();
			}
			
			if(handleException(it, t, FacesFileNotFoundException.class, "com.sun.faces.context.FacesFileNotFoundException.message")) continue;
			if(handleException(it, t, PrettyException.class, "com.ocpsoft.pretty.PrettyException.message")) continue;
			if(handleException(it, t, EntityNotFoundException.class, "javax.persistence.EntityNotFoundException.message")) continue;
			if(handleException(it, t, EntityExistsException.class, "javax.persistence.EntityExistsException.message")) continue;
			if(handleException(it, t, OptimisticLockException.class, "javax.persistence.OptimisticLockException.message")) continue;
			if(handleException(it, t, ViewExpiredException.class, "javax.faces.application.ViewExpiredException.message")) continue;
			if(handleException(it, t, Exception.class, "java.lang.Exception.message")) continue;
			
		}
		getWrapped().handle();
	}
	
	protected boolean handleException(Iterator<ExceptionQueuedEvent> it, Throwable t, Class<? extends Throwable> type, String message){
		if(type.isAssignableFrom(t.getClass())){
			try{
				FacesContext facesContext = FacesUtils.getContext();
				
				FacesUtils.addMsgErro(message);
				messagesUtils.saveMessages(facesContext, facesContext.getExternalContext().getSessionMap());
				
				facesContext.getApplication().getNavigationHandler().handleNavigation(facesContext, null, "pretty:error");
				facesContext.renderResponse();
				
				return true;
			}finally{
				it.remove();
				
				while(it.hasNext()){
					it.next();
					it.remove();
				}
			}
		}
		return false;
	}
}
