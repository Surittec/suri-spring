package br.com.suricattus.surispring.framework.controller;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class ControllerSupport implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
     * Retorna a referencia do FacesContext.
     * @return HttpServletResponse
     */
    protected FacesContext getContext(){
        return FacesContext.getCurrentInstance();
    }
	
    /**
     * Retorna a referencia para o HttpServletResponse atual.
     * @return HttpServletResponse
     */
    protected HttpServletResponse getResponse(){
        return (HttpServletResponse)getContext().getExternalContext().getResponse();
    }

    /**
     * Retorna a referencia para o HttpServletRequest atual.
     * @return HttpServletRequest
     */
    protected HttpServletRequest getRequest(){
        return (HttpServletRequest)getContext().getExternalContext().getRequest();
    }
    
    /**
     * Retorna a instancia atual do contexto ServletContext.
     * @return ServletContext
     */
    protected ServletContext getServletContext(){
        return (ServletContext)getContext().getExternalContext().getContext();
    }
    
    /**
     * Retorna um parametro do Request HTTP (HttpServletRequest) sob a chave 'name'.
     * @param name - chave do parametro no Request HTTP
     * @return String - parametro do Request HTTP sob a chave 'name'
     */
    protected String getParameter(String name){
        return getRequest().getParameter(name);
    }

    /**
     * Retorna a referencia para o HttpSession atual.
     * @return HttpSession
     */
    protected HttpSession getSession(){
        return getRequest().getSession();
    }
    
    /**
     * Retorna o contexto de Flash
     * @return Flash
     */
    protected Flash getFlash(){
    	return getContext().getExternalContext().getFlash();
    }
    
    /**
     * Adiciona uma mensagem no facesMessages com severidade info.
     * @param msg
     * @param params
     */
    protected void addMsg(String msg, Object ... params) {
		addMsg(FacesMessage.SEVERITY_INFO, msg, params);
	}

	/**
     * Adiciona uma mensagem no facesMessages com severidade INFO, para o componente 
     * especificado
     * @param compoenentId
     * @param msg
     * @param params
     */
    protected void addMsgToComponent(String componenteId, String msg, Object... params) {
		addMsgToComponent(FacesMessage.SEVERITY_INFO, componenteId, msg, params);
	}
	
	/**
     * Adiciona uma mensagem no facesMessages com severidade warn.
     * @param msg
     * @param params
     */
    protected void addMsgWarn(String msg, Object... params) {
		addMsg(FacesMessage.SEVERITY_WARN, msg, params);
	}

	/**
     * Adiciona uma mensagem no facesMessages com severidade WARN, para o componente 
     * especificado.
     * @param compoenentId
     * @param msg
     * @param params
     */
    protected void addMsgWarnToComponent(String componenteId, String msg, Object... params) {
		addMsgToComponent(FacesMessage.SEVERITY_WARN,componenteId, msg, params);
	}
	
	/**
     * Adiciona uma mensagem no facesMessages com severidade error.
     * @param msg
     * @param params
     */
    protected void addMsgErro(String msg, Object... params) {
		addMsg(FacesMessage.SEVERITY_ERROR, msg, params);
	}

	/**
     * Adiciona uma mensagem do bundle no facesMessages com severidade error, para o componente 
     * especificado.
     * @param compoenentId
     * @param msg
     * @param params
     */
    protected void addMsgErroToComponent(String componenteId, String msg, Object... params) {
		addMsgToComponent(FacesMessage.SEVERITY_ERROR, componenteId, msg, params);
	}
	
	/**
	 * Lanca erro de validacao com a mensagem do bundle. 
	 * @param key
	 * @throws ValidatorException
	 */
    protected void throwValidationException(String key) throws ValidatorException {
		FacesMessage facesMessage = new FacesMessage(getMessageFromDefaultBundle(key));
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
    protected String getMessageFromBundle(String bundleName, String key, Object ... params){
		if(bundleName == null) bundleName = getContext().getApplication().getMessageBundle();
		ResourceBundle bundle = ResourceBundle.getBundle(bundleName, getContext().getViewRoot().getLocale());
		
		String msg = bundle.containsKey(key) ? bundle.getString(key) : key;

		if(params == null || params.length == 0) return msg;
		
		MessageFormat form = new MessageFormat(msg);
        return form.format(params);
	}
	
	/**
	 * Metodo para ser utilizado em acoes de cancelamento que exigem a limpeza de um form.
	 * Realiza, assim, a limpeza completa na arvore de componentes do JSF, do form que o botao se encontra.
	 * @param event
	 */
    protected void resetForm(ActionEvent event){
		UIComponent component = event.getComponent();
		while(!(component instanceof HtmlForm) && component.getParent() != null) component = component.getParent();
		if(component instanceof HtmlForm) cleanComponent(component);
	}
	
	/**
	 * Metodo para limpar o componente, na arvore jsf, cujo id informado. 
	 * @param componentClientId
	 */
    protected void resetComponent(String componentClientId){
		cleanComponent(getContext().getViewRoot().findComponent(componentClientId));
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PRIVATE METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private void cleanComponent(UIComponent component) {
		if(component == null) return;
		if (component instanceof UIInput) ((UIInput)component).resetValue();
		if (component.getChildCount() > 0) {
			for (UIComponent child : component.getChildren()) {
				cleanComponent(child);
			}
		}
	}
	
	private void addMsg(Severity severity, String msg, Object... params){
		getContext().getMessageList().add(new FacesMessage(severity, getMessageFromDefaultBundle(msg, params), null));
	}
	
	private void addMsgToComponent(Severity severity, String componentId, String msg, Object... params){
		getContext().getMessageList(componentId).add(new FacesMessage(severity, getMessageFromDefaultBundle(msg, params), null));
	}
	
}
