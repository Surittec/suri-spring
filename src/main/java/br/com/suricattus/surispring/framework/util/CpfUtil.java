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
package br.com.suricattus.surispring.framework.util;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Lucas Lins
 *
 */
public abstract class CpfUtil {
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// ATTIRBUTES
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	private static List<String> cpfInvalidos = Arrays.asList(new String []{	"00000000000","11111111111",
																			"22222222222","33333333333",
																			"44444444444","55555555555",
																			"66666666666","77777777777",
																			"88888888888","99999999999"});
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PUBLIC METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	
	public static boolean isValid(Object value) {
		return isValid(value, false);
	}
	
	public static boolean isValid(Object value, boolean nullable) {
		if(!nullable && value == null) return false;

		if(nullable && value == null) return true;
		
		if (value != null && !isValidCpf(value.toString())) {
			return false;
		}else{
			return true;
		}
	}
	
	public static String format(String cpf){
		if (cpf == null) return null;

		if (cpf.length() == 13) {
			return cpf;
		} else if (cpf.length() < 11) {
			return cpf;
		} else {
			return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9, 11);
		}
	}
	
	public static String unformat(String value){
		if(value == null || value.trim().equals("")) return null;
		return value.replace(".", "").replace("-", "").replace("/", "");
	}
	
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
	// PRIVATE METHODS
	// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

	private static boolean isValidCpf(String cpf) {
		
		if(cpfInvalidos.contains(cpf)) return false;
		
		if (cpf.trim().length() != 11) return false;

		String cpf1 = cpf.substring(0, 9); // rcpf1
		String cpf2 = cpf.substring(9); // rcpf2
		int i;

		int d1 = 0;
		for (i = 0; i < 9; i++) {
			d1 += Integer.parseInt(cpf1.substring(i, i + 1)) * (10 - i);
		}

		d1 = 11 - (d1 % 11);
		if (d1 > 9) {
			d1 = 0;
		}

		if (Integer.parseInt(cpf2.substring(0, 1)) != d1) {
			return false;
		}

		d1 *= 2;
		for (i = 0; i < 9; i++) {
			d1 += Integer.parseInt(cpf1.substring(i, i + 1)) * (11 - i);
		}

		d1 = 11 - (d1 % 11);
		if (d1 > 9) {
			d1 = 0;
		}

		if (Integer.parseInt(cpf2.substring(1, 2)) != d1) {
			return false;
		}

		return true;
	}

}
