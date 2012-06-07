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
package br.com.suricattus.surispring.framework.controller.interceptor;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import br.com.suricattus.surispring.framework.service.exception.BusinessException;
import br.com.suricattus.surispring.framework.service.exception.BusinessException.Message;
import br.com.suricattus.surispring.jsf.util.FacesUtils;

/**
 * Interceptor que captura excecoes de negocio e adiciona as mensagens no FacesMessages
 * 
 * @author Lucas Lins
 *
 */
@Aspect
@Component("br.com.suricattus.surispring.framework.controller.interceptor.BusinessExceptionInterceptor")
public class BusinessExceptionInterceptor {
	
	@Around("@within(br.com.suricattus.surispring.framework.controller.annotation.AppendBusinessMessages)")
	public Object appendMessages(ProceedingJoinPoint pjp) throws Throwable{
		try{
			return pjp.proceed();
		}catch (BusinessException be) {
			for(Message error : be.getErrors()) {
				if(error.getComponentId() != null){
					FacesUtils.addMsgToComponent(error.getComponentId(), error.getSeverity(), error.getMessage(), error.getParams());
				}else{
					FacesUtils.addMsg(error.getSeverity(), error.getMessage(), error.getParams());
				}
			}
			return null;
		}
	}

}
