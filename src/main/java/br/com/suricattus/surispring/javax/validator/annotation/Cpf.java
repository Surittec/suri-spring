
package br.com.suricattus.surispring.javax.validator.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;

import br.com.suricattus.surispring.javax.validator.CpfValidator;

/**
 * Annotation de definicao do validator de cpf.
 * 
 * @author lucas lins
 *
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfValidator.class)
@Documented
public @interface Cpf {

	boolean nullable() default false;
	String message() default "{javax.validation.constraints.Cpf.message}";
	
}
