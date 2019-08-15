package com.bancodebogota.gciades.ldap.utils;


/**
 * Esta clase maneja la excepción capturada que contiene el error estándar LDAP: error code 53 o excepción LDAP_UNWILLING_TO_PERFORM.
 * Indica que el servidor LDAp no puede procesar la petición porque no cumple conalgunas restricciones definidas.
 * 
 * Algunas razones pueden ser:
 * 
 * Modificación de atributos que no pueden ser modificados.
 * Restricciones de password.
 * Restricciones de conexión 
 * 
 * Por ejemplo, detalle del problema
 * 
 * problem 5003 (WILL_NOT_PERFORM), data 0 --> ERROR_PASSWORD_RESTRICTION (https://ldapwiki.com/wiki/WILL_NOT_PERFORM)

 * 
 * @See https://ldapwiki.com/wiki/LDAP_UNWILLING_TO_PERFORM 
 * Para ver más estados: https://ldapwiki.com/wiki/LDAP%20Result%20Codes
 * 
 * @author WRIVER1
 *
 */
public class LdapError53Handler {
	
	/**
	 * Inidica que no cumple con las políticas de complejidad. https://ldapwiki.com/wiki/ERROR_PASSWORD_RESTRICTION 
	 */
	private static final String ERROR_LDAP_53_PASSWORD_RESTRICTION ="problem 5003 (WILL_NOT_PERFORM), data 0";
	
	/**
	 * El método maneja por ahora una de las posibles razones de este tipo de error
	 * Si en el futuro obtienen una descripción diferente agregar el condicional para manejarlo que permita identificar la causa real.
	 * @param e
	 * @throws Exception
	 */
	public static void handler(Throwable e) {
		String message = e.getMessage() != null && !"".equals(e.getMessage().trim()) ? e.getMessage(): "MESSAGE NOT FOUND";
		if(message.contains(ERROR_LDAP_53_PASSWORD_RESTRICTION))
			throw new IllegalStateException("La contraseña no cumple con la política de complejidad de seguridad de las contraseñas. Por favor consulte con la entidad para conocerla e intente de nuevamente.");
	}
}
