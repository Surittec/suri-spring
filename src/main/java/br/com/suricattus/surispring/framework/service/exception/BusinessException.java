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
package br.com.suricattus.surispring.framework.service.exception;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;

/**
 * Excecao de negocio. Atraves desta classe e possivel passar mensagens que serao exibidas 
 * diretamente na tela do usuario.
 * 
 * @author Lucas Lins
 *
 */
public class BusinessException extends RuntimeException{
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ATTRIBUTES
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static final long serialVersionUID = 1L;
	
	private List<Message> errors = new ArrayList<Message>();
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// CONSTRUCTORS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public BusinessException() {
		super();
	}
	
	public BusinessException(String message, Object ... params){
		addMessage(message, params);
	}
	
	public BusinessException(Throwable cause){
		super(cause);
	}
	
	public BusinessException(String message, Throwable cause){
		super(message, cause);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PUBLIC METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
	 * Adiciona uma nova mensagem para um component na lista de mensagens desta excecao.
	 * @param componentId
	 * @param message
	 * @param params
	 */
	public void addMessageToComponent(String componentId, String message, Object ... params){
		add(componentId, message, FacesMessage.SEVERITY_ERROR, params);
	}
	
	/**
	 * Adiciona uma nova mensagem na lista de mensagens desta excecao.
	 * @param message
	 * @param params
	 */
	public void addMessage(String message, Object ... params){
		add(null, message, FacesMessage.SEVERITY_ERROR, params);
	}
	
	public List<Message> getErrors(){ 
		return errors;
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PRIVATE METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private void add(String componentId, String message, FacesMessage.Severity severity, Object ... params){
		errors.add(new Message(componentId, message, severity, params));
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// MESSAGE CLASS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	public class Message{
		private String componentId;
		private String message;
		private FacesMessage.Severity severity;
		private Object[] params;
		
		public Message(String componentId, String message, Severity severity, Object[] params) {
			this.componentId = componentId;
			this.message = message;
			this.severity = severity;
			this.params = params;
		}
		
		public String getComponentId() {
			return componentId;
		}
		public void setComponentId(String componentId) {
			this.componentId = componentId;
		}
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public FacesMessage.Severity getSeverity() {
			return severity;
		}
		public void setSeverity(FacesMessage.Severity severity) {
			this.severity = severity;
		}
		public Object[] getParams() {
			return params;
		}
		public void setParams(Object[] params) {
			this.params = params;
		}
	}

}
