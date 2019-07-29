/**
 * Crea (como objeto) la llave privada, dado un par de llaves
 * @author mquint2
 */
package co.com.bancodebogota.services;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KeyGenerator implements IKeyGenerator{
	
	//TODO Definir una ubicacion y forma de generacion de las llaves que actualmente estan en el archivo application.porperties
	@Value("${key.public}")
	private String publicKey;
	
	@Value("${key.private}")
	private String privateKey;
	
	/**
	 * @return retorna la llave privada como objeto 
	 */
	@Override
	public PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		BigInteger publicKeyInt = new BigInteger(this.publicKey, 16);
    	BigInteger privKeyInt = new BigInteger(this.privateKey, 16);
    	BigInteger exponentInt = new BigInteger("10001", 16);
        
    	KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    	
    	RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(publicKeyInt, exponentInt);
    	PublicKey puk = keyFactory.generatePublic(publicKeySpec);
    	
    	RSAPrivateKeySpec privateKeySpec = new RSAPrivateKeySpec(publicKeyInt, privKeyInt);
    	PrivateKey prk = keyFactory.generatePrivate(privateKeySpec);
    	
    	return prk;
	}

	@Override
	public String getPublicKey() {
		return this.publicKey;
	}

}
