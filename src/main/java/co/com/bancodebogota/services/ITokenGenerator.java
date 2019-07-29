package co.com.bancodebogota.services;

public interface ITokenGenerator {
	String getToken(String username, String groups);
}
