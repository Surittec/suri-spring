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
package br.com.suricattus.surispring.framework.controller;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;

import javax.faces.application.FacesMessage;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import br.com.suricattus.surispring.framework.controller.annotation.AppendBusinessMessages;
import br.com.suricattus.surispring.jsf.util.FacesUtils;

import com.ocpsoft.pretty.PrettyContext;
import com.ocpsoft.pretty.faces.config.mapping.PathParameter;
import com.ocpsoft.pretty.faces.config.mapping.UrlMapping;

/**
 * 
 * @author Lucas Lins
 *
 */
@AppendBusinessMessages
public abstract class ControllerSupport{

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ATTRIBUTES
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	protected String idParam;
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PUBLIC METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public String getIdParam() {
		return idParam;
	}

	public void setIdParam(String idParam) {
		this.idParam = idParam;
	}
	
	 /**
     * Comparacao de strings com ignore case, util para dataTables com sort
     * @param obj1
     * @param obj2
     * @return
     */
    public int sortIgnoreCase(Object obj1, Object obj2){
		return ((String)obj1).compareToIgnoreCase((String)obj2);
	}
    
    /**
	 * Metodo para ser utilizado em acoes de cancelamento que exigem a limpeza de um form.
	 * Realiza, assim, a limpeza completa na arvore de componentes do JSF, do form que o botao se encontra.
	 * @param event
	 */
    public void resetForm(ActionEvent event){
		UIComponent component = event.getComponent();
		while(!(component instanceof HtmlForm) && component.getParent() != null) component = component.getParent();
		if(component instanceof HtmlForm) cleanComponent(component);
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PROTECTED METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	/**
     * Retorna a referencia do FacesContext.
     * @return FacesContext
     */
    protected FacesContext getContext(){
        return FacesUtils.getContext();
    }

	/**
     * Retorna a referencia do FacesContext.
     * @return ExternalContext
     */
    protected ExternalContext getExternalContext(){
        return FacesUtils.getExternalContext();
    }
    
    /**
     * Retorna a referencia para o HttpServletResponse atual.
     * @return HttpServletResponse
     */
    protected HttpServletResponse getResponse(){
        return FacesUtils.getResponse();
    }

    /**
     * Retorna a referencia para o HttpServletRequest atual.
     * @return HttpServletRequest
     */
    protected HttpServletRequest getRequest(){
        return FacesUtils.getRequest();
    }
    
    /**
     * Retorna a instancia atual do contexto ServletContext.
     * @return ServletContext
     */
    protected ServletContext getServletContext(){
        return FacesUtils.getServletContext();
    }
    
    /**
     * Retorna um parametro do Request HTTP (HttpServletRequest) sob a chave 'name'.
     * @param name - chave do parametro no Request HTTP
     * @return String - parametro do Request HTTP sob a chave 'name'
     */
    protected String getParameter(String name){
        return FacesUtils.getParameter(name);
    }

    /**
     * Retorna a referencia para o HttpSession atual.
     * @return HttpSession
     */
    protected HttpSession getSession(){
        return FacesUtils.getSession();
    }
    
    /**
     * Retorna o contexto de Flash
     * @return Flash
     */
    protected Flash getFlash(){
    	return FacesUtils.getFlash();
    }
    
    /**
     * Adiciona uma mensagem no facesMessages com severidade info.
     * @param msg
     * @param params
     */
    protected void addMsg(String msg, Object ... params) {
    	FacesUtils.addMsg(FacesMessage.SEVERITY_INFO, msg, params);
	}

	/**
     * Adiciona uma mensagem no facesMessages com severidade INFO, para o componente 
     * especificado
     * @param compoenentId
     * @param msg
     * @param params
     */
    protected void addMsgToComponent(String componenteId, String msg, Object... params) {
    	FacesUtils.addMsgToComponent(componenteId, FacesMessage.SEVERITY_INFO, msg, params);
	}
	
	/**
     * Adiciona uma mensagem no facesMessages com severidade warn.
     * @param msg
     * @param params
     */
    protected void addMsgWarn(String msg, Object... params) {
    	FacesUtils.addMsg(FacesMessage.SEVERITY_WARN, msg, params);
	}

	/**
     * Adiciona uma mensagem no facesMessages com severidade WARN, para o componente 
     * especificado.
     * @param compoenentId
     * @param msg
     * @param params
     */
    protected void addMsgWarnToComponent(String componenteId, String msg, Object... params) {
    	FacesUtils.addMsgToComponent(componenteId, FacesMessage.SEVERITY_WARN, msg, params);
	}
	
	/**
     * Adiciona uma mensagem no facesMessages com severidade error.
     * @param msg
     * @param params
     */
    protected void addMsgErro(String msg, Object... params) {
    	FacesUtils.addMsg(FacesMessage.SEVERITY_ERROR, msg, params);
	}

	/**
     * Adiciona uma mensagem do bundle no facesMessages com severidade error, para o componente 
     * especificado.
     * @param compoenentId
     * @param msg
     * @param params
     */
    protected void addMsgErroToComponent(String componenteId, String msg, Object... params) {
    	FacesUtils.addMsgToComponent(componenteId, FacesMessage.SEVERITY_ERROR, msg, params);
	}
	
	/**
	 * Lanca erro de validacao com a mensagem do bundle. 
	 * @param key
	 * @throws ValidatorException
	 */
    protected void throwValidationException(String key) throws ValidatorException {
		FacesMessage facesMessage = new FacesMessage(FacesUtils.getMessageFromDefaultBundle(key));
		facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
		throw new ValidatorException(facesMessage);
	}
	
	/**
	 * Retorna a mensagem do arquivo properties default ("messages") formatada com os parametros passados.
	 * 
	 * @param key
	 * @param params
	 * @return mensagem formatada
	 */
    protected String getMessageFromDefaultBundle(String key, Object ... params){
        return FacesUtils.getMessageFromDefaultBundle(key, params);
	}
	
	/**
	 * Retorna a mensagem do arquivo properties, que possui o nome passado como argumento, formatada com os parametros passados.
	 * 
	 * @param bundleName
	 * @param key
	 * @param params
	 * @return mensagem formatada
	 */
    protected String getMessageFromBundle(String bundleName, String key, Object ... params){
		return FacesUtils.getMessageFromBundle(bundleName, key, params);
	}
	
	/**
	 * Metodo para limpar o componente, na arvore jsf, cujo id informado. 
	 * @param componentClientId
	 */
    protected void resetComponent(String componentClientId){
		cleanComponent(getContext().getViewRoot().findComponent(componentClientId));
	}
    
    /**
     * Redirecionamento com o pretty-faces
     * @param mappingId
     * @param params
     */
    protected String redirect(String mappingId, Object ... params){
    	PrettyContext context = PrettyContext.getCurrentInstance(getRequest());
		UrlMapping mapping = context.getConfig().getMappingById("pretty:"+mappingId);
		if(mapping != null){
			
			if(mapping.getPatternParser().getPathParameters().size() > params.length) return null;
			
			StringBuilder url = new StringBuilder(mapping.getViewId());
			url.append("?faces-redirect=true");
			
			Iterator<Object> paramsIt = Arrays.asList(params).iterator();
			for(PathParameter pp : mapping.getPatternParser().getPathParameters()) url.append("&").append(pp.getName()).append("=").append(paramsIt.next());
			while(paramsIt.hasNext()) url.append("&").append(paramsIt.next());
			
			return url.toString();
			
		}else{
			return null;
		}
    }
    
    protected boolean filterIgnoreCase(Object value, Object filter, Locale locale) {
	    String filterText = (filter == null) ? null : filter.toString().trim();
	    if (filterText == null || filterText.equals("")) return true;
	    
	    if (value == null) return false;

	    String objectText = value.toString().toUpperCase();
	    filterText = filterText.toUpperCase();

	    if (objectText.contains(filterText)) return true;
	    else return false;
	}
    
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PRIVATE METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private void cleanComponent(UIComponent component) {
		if(component == null) return;
		if (component instanceof EditableValueHolder) ((EditableValueHolder)component).resetValue();
		if(UIComponent.isCompositeComponent(component)){
			Iterator<UIComponent> it = component.getFacetsAndChildren();
			while(it.hasNext()) cleanComponent(it.next());
		}
		if (component.getChildCount() > 0) {
			for (UIComponent child : component.getChildren()) {
				cleanComponent(child);
			}
		}
	}
	
}
