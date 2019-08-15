package com.bancodebogota.gciades.ldap.utils;

/**
 * Clase que registra los códigos más relevantes que puede tomar el atributo useraccountcontrol 
 * en una cuenta de usuario en un Directorio Activo. Los valores son acumulativos teniendo en cuenta
 * que 512 o NORMAL ACCOUNT es el valor predeterminado de una cuenta, por lo que una cuenta deshabilitado 
 * sería ACCOUNTDISABLED + NORMAL_ACCOUNT (2 + 512) = 514
 * 
 * @see https://support.microsoft.com/es-co/help/305144/how-to-use-useraccountcontrol-to-manipulate-user-account-properties
 * @author WRIVER1
 */
public class UserAccountControl {

	//Valor de cuenta predeterminado
		private static final int NORMAL_ACCOUNT = 512;
		
		//Valores complementarios de una cuenta de usuario
		private static final int ACCOUNT_DISABLED = 2;
		
		//Valores asociados a la constraseña de una cuenta
		private static final int DONT_EXPIRE_PASSWORD = 65536;
		private static final int PASSWORD_NOT_REQUIRED = 32;

		
		public static boolean isDisabled( String userAccountControl ) {
			boolean result = false;
			switch( castToIntegerUserAccountControlValue(userAccountControl) ) {
				case NORMAL_ACCOUNT + ACCOUNT_DISABLED: result =  true; break;
				case DONT_EXPIRE_PASSWORD + NORMAL_ACCOUNT + ACCOUNT_DISABLED: result =  true; break;
				default: break;
			}
			return result;
		}
		
		public static boolean isDisableAndPasswordNotRequired( String userAccountControl ) {
			return NORMAL_ACCOUNT + ACCOUNT_DISABLED + PASSWORD_NOT_REQUIRED == castToIntegerUserAccountControlValue(userAccountControl);
		}
		
		public static boolean isOk(String userAccountControl) {
			boolean result = false;
			switch( castToIntegerUserAccountControlValue(userAccountControl) ) {
				case NORMAL_ACCOUNT: result =  true; break;
				case DONT_EXPIRE_PASSWORD + NORMAL_ACCOUNT: result =  true; break;
				default: break;
			}
			return result;
		}
		
		private static int castToIntegerUserAccountControlValue( String userAccountControl) {
			int userAccountControlDecimal = 0;
			try {
				userAccountControlDecimal = Integer.valueOf(userAccountControl);
			} catch(NumberFormatException e) {
				throw new IllegalStateException("Imposible transformar el código useraccountcontrol " + userAccountControl + " en un número decimal");
			}
			return userAccountControlDecimal;
		}
}
