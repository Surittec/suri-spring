package br.com.suricattus.surispring.javax.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.suricattus.surispring.framework.util.CpfUtil;
import br.com.suricattus.surispring.javax.validator.annotation.Cpf;

public class CpfValidator implements ConstraintValidator<Cpf, String>{

	private boolean nullable;
	
	public void initialize(Cpf config) {
		this.nullable = config.nullable();
	}

	public boolean isValid(String cpf, ConstraintValidatorContext constraintContext) {
		return CpfUtil.isValid(cpf, nullable);
	}
}
