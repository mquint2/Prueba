package com.bancodebogota.gciades.ldap.utils;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.RSAPrivateKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DecryptUtil {
	
	@Value("#{systemEnvironment['LDAP_MSI_PUBLIC_KEY']}")
	private String keyPublic;
	@Value("#{systemEnvironment['LDAP_MSI_PRIVATE_KEY']}")
	private String keyPrivate;
	
	
	public String decryptMessage(String message) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey());
		byte[] byteString = cipher.doFinal(Base64.getDecoder().decode(message));
		return new String(byteString);
	}
	

	private PrivateKey getPrivateKey() throws GeneralSecurityException {
	
		BigInteger publicKeyInt = new BigInteger(this.keyPublic, 16);
    	BigInteger privKeyInt = new BigInteger(this.keyPrivate, 16);
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    	RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(publicKeyInt, privKeyInt);
    	PrivateKey prk = keyFactory.generatePrivate(privateKeySpec);
    	
    	return prk;
	}

}