package br.com.suricattus.surispring.framework.util;

import java.util.Arrays;
import java.util.List;

public abstract class CnpjUtil {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ATTIRBUTES
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static List<String> cpfInvalidos = Arrays.asList(new String []{"00000000000", "11111111111",
																		   "22222222222", "33333333333",
																		   "44444444444", "55555555555",
																		   "66666666666", "77777777777",
																		   "88888888888", "99999999999"});
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PUBLIC METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static boolean isValid(Object value) {
		return isValid(value, false);
	}
	
	public static boolean isValid(Object value, boolean nullable) {
		
		if(!nullable && value == null) return false;

		if(nullable && value == null) return true;
		
		if (value != null && !isValidCnpj(value.toString())) {
			return false;
		}else{
			return true;
		}
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PRIVATE METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static boolean isValidCnpj(String cnpj) {
		
		if(cpfInvalidos.contains(cnpj)) return false;
		
		if (cnpj.trim().length() != 11) return false;

		String cpf1 = cnpj.substring(0, 9); // rcpf1
		String cpf2 = cnpj.substring(9); // rcpf2
		int i;

		int d1 = 0;
		for (i = 0; i < 9; i++) d1 += Integer.parseInt(cpf1.substring(i, i + 1)) * (10 - i);

		d1 = 11 - (d1 % 11);
		if (d1 > 9) d1 = 0;

		if (Integer.parseInt(cpf2.substring(0, 1)) != d1) return false;

		d1 *= 2;
		for (i = 0; i < 9; i++) d1 += Integer.parseInt(cpf1.substring(i, i + 1)) * (11 - i);

		d1 = 11 - (d1 % 11);
		if (d1 > 9) d1 = 0;

		if (Integer.parseInt(cpf2.substring(1, 2)) != d1) return false;

		return true;
	}
	
}
