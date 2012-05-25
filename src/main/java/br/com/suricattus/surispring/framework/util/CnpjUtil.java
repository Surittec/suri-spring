package br.com.suricattus.surispring.framework.util;

import java.util.Arrays;
import java.util.List;

public abstract class CnpjUtil {

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ATTIRBUTES
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static List<String> cnpjInvalidos = Arrays.asList(new String []{"00000000000000","11111111111111",
																			"22222222222222","33333333333333",
																			"44444444444444","55555555555555",
																			"66666666666666","77777777777777",
																			"88888888888888","99999999999999"});
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PUBLIC METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static boolean isValid(Object value) {
		return isValid(value, false);
	}
	
	public static boolean isValid(Object value, boolean nullable) {
		if(!nullable && value == null) return false;
		
		if(nullable && value == null) return true;
		
		if (value instanceof String) {
			String cnpj = (String) value;
			String regex = "\\d{14}";
			if (!cnpj.matches(regex)){
				return false;
			}
			return isValidCnpj(cnpj);
		}
		return false;
	}
	
	public static String format(String cnpj){
		if(cnpj == null) return null;
		
		if (cnpj.length() == 18 || cnpj.length() < 14) {
			return cnpj;
		} else {
			return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12, 14);
		}
	}
	
	public static String unformat(String value){
		if(value == null || value.trim().equals("")) return null;
		return value.replace(".", "").replace("-", "").replace("/", "");
	}

	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PRIVATE METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static boolean isValidCnpj(String cnpj) {
		
		if(cnpjInvalidos.contains(cnpj)) return false;
		
		char charsCNPJ[] = cnpj.toCharArray();
		Integer digito1 = calculoPrimeiroDigitoVerificador(charsCNPJ);
		Integer digito2 = calculoSegundoDigitoVerificador(charsCNPJ, digito1);

		String digitoIdentificadorCalculado = digito1.toString() + digito2.toString();
		String digitoIdentificadorInformado = charsCNPJ[charsCNPJ.length - 2] + "" + charsCNPJ[charsCNPJ.length - 1];

		return digitoIdentificadorCalculado.equals(digitoIdentificadorInformado);
	}

	private static int calculoPrimeiroDigitoVerificador(char[] charsCNPJ) {
		int fator = 5;
		int soma = 0;
		for (int i = 0; i < charsCNPJ.length - 2; i++) {
			char c = charsCNPJ[i];
			soma += Integer.parseInt(String.valueOf(c)) * fator;
			if (i == 3){
				fator = 9;
			}else{
				fator--;
			}
		}

		int valor = (soma) % 11;

		int digito1 = 11 - valor;
		if ((soma) % 11 < 2) {
			digito1 = 0;
		}
		return digito1;
	}

	private static int calculoSegundoDigitoVerificador(char[] charsCNPJ, int digito1) {
		int fator2 = 6;
		int soma2 = 0;
		for (int i = 0; i < charsCNPJ.length - 2; i++) {
			char c = charsCNPJ[i];
			soma2 += Integer.parseInt(String.valueOf(c)) * fator2;
			if (i == 4){
				fator2 = 9;
			}else{
				fator2--;
			}
		}
		soma2 += digito1 * 2;
		int valor2 = (soma2) % 11;

		int digito2 = 11 - valor2;
		if ((soma2) % 11 < 2)
			digito2 = 0;
		return digito2;
	}
	
}
