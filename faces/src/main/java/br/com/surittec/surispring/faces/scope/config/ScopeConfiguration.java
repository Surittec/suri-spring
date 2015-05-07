package br.com.surittec.surispring.faces.scope.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.surittec.surispring.faces.scope.view.impl.ViewScope;

@Configuration
public class ScopeConfiguration {

	@Bean
	public CustomScopeConfigurer customScopeConfigurer() {
		Map<String, Object> scopes = new HashMap<String, Object>();
		scopes.put(ViewScope.SCOPE_VIEW, viewScope());

		CustomScopeConfigurer csc = new CustomScopeConfigurer();
		csc.setScopes(scopes);

		return csc;
	}

	@Bean
	public ViewScope viewScope() {
		return new ViewScope();
	}

}
