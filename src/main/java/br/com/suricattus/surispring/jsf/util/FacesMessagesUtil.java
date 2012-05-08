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
package br.com.suricattus.surispring.jsf.util;

import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

/**
 * Classe utilitaria de mensagens do JSF
 * 
 * @author Lucas Lins
 *
 */
public abstract class FacesMessagesUtil {

	/**
     * Adiciona uma mensagem no facesMessages com severidade info.
     * @param msg
     * @param params
     */
    public static void addMsg(String msg, Object ... params) {
		addMsg(FacesMessage.SEVERITY_INFO, msg, params);
	}

	/**
     * Adiciona uma mensagem no facesMessages com severidade INFO, para o componente 
     * especificado
     * @param compoenentId
     * @param msg
     * @param params
     */
    public static void addMsgToComponent(String componenteId, String msg, Object... params) {
		addMsgToComponent(FacesMessage.SEVERITY_INFO, componenteId, msg, params);
	}
	
	/**
     * Adiciona uma mensagem no facesMessages com severidade warn.
     * @param msg
     * @param params
     */
    public static void addMsgWarn(String msg, Object... params) {
		addMsg(FacesMessage.SEVERITY_WARN, msg, params);
	}

	/**
     * Adiciona uma mensagem no facesMessages com severidade WARN, para o componente 
     * especificado.
     * @param compoenentId
     * @param msg
     * @param params
     */
    public static void addMsgWarnToComponent(String componenteId, String msg, Object... params) {
		addMsgToComponent(FacesMessage.SEVERITY_WARN,componenteId, msg, params);
	}
	
	/**
     * Adiciona uma mensagem no facesMessages com severidade error.
     * @param msg
     * @param params
     */
    public static void addMsgErro(String msg, Object... params) {
		addMsg(FacesMessage.SEVERITY_ERROR, msg, params);
	}

	/**
     * Adiciona uma mensagem do bundle no facesMessages com severidade error, para o componente 
     * especificado.
     * @param compoenentId
     * @param msg
     * @param params
     */
    public static void addMsgErroToComponent(String componenteId, String msg, Object... params) {
		addMsgToComponent(FacesMessage.SEVERITY_ERROR, componenteId, msg, params);
	}
	
    /**
     * Adiciona uma mensagem do bundle no facesMessages
     * @param severity
     * @param msg
     * @param params
     */
	public static void addMsg(Severity severity, String msg, Object... params){
		FacesContext.getCurrentInstance().addMessage(null, create(severity, msg, params));
	}
	
	/**
	 * Adiciona uma mensagem do bundle no facesMessages, para o componente especificado
	 * @param severity
	 * @param componentId
	 * @param msg
	 * @param params
	 */
	public static void addMsgToComponent(Severity severity, String componentId, String msg, Object... params){
		FacesContext.getCurrentInstance().addMessage(componentId, create(severity, msg, params));
	}
	
	/**
	 * Cria uma mensagem com os parametros informados
	 * @param severity
	 * @param msg
	 * @param params
	 * @return
	 */
	public static FacesMessage create(Severity severity, String msg, Object... params){
		return new FacesMessage(severity, getMessageFromDefaultBundle(msg, params), null);
	}
	
	/**
	 * Retorna a mensagem do arquivo properties default ("messages") formatada com os parametros passados.
	 * 
	 * @param key
	 * @param params
	 * @return mensagem formatada
	 */
    public static String getMessageFromDefaultBundle(String key, Object ... params){
        return getMessageFromBundle(null, key, params);
	}
	
	/**
	 * Retorna a mensagem do arquivo properties, que possui o nome passado como argumento, formatada com os parametros passados.
	 * 
	 * @param bundleName
	 * @param key
	 * @param params
	 * @return mensagem formatada
	 */
    public static String getMessageFromBundle(String bundleName, String key, Object ... params){
    	FacesContext context = FacesContext.getCurrentInstance();
    	
		if(bundleName == null) bundleName = context.getApplication().getMessageBundle();
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, context.getViewRoot().getLocale());
		
		String msg = bundle.containsKey(key) ? bundle.getString(key) : key;

		if(params == null || params.length == 0) return msg;
		
		MessageFormat form = new MessageFormat(msg);
        return form.format(params);
	}
	
}
