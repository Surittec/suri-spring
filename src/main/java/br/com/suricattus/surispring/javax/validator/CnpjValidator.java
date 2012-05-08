package br.com.suricattus.surispring.javax.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.suricattus.surispring.framework.util.CnpjUtil;
import br.com.suricattus.surispring.javax.validator.annotation.Cnpj;

public class CnpjValidator implements ConstraintValidator<Cnpj, String>{

	private boolean nullable;
	
	public void initialize(Cnpj config) {
		this.nullable = config.nullable();
	}

	public boolean isValid(String cnpj, ConstraintValidatorContext constraintContext) {
		return CnpjUtil.isValid(cnpj, nullable);
	}

}
