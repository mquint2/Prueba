package co.com.bancodebogota.services;

import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

public interface IKeyGenerator {
	PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException;
	String getPublicKey();
}
