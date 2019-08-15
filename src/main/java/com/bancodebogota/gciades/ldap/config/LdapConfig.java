package com.bancodebogota.gciades.ldap.config;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.ldap.InitialLdapContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//<import org.springframework.data.ldap.repository.config.EnableLdapRepositories;

@Configuration
//@EnableLdapRepositories(basePackages = "com.bancodebogota.gciades.ldap.**")
public class LdapConfig
{

	@Value("${spring.ldap.urls}")
	private String ldapUrls;

	@Value("${spring.ldap.username}")
	private String ldapUsername;

	@Value("${spring.ldap.password}")
	private String ldapPassword;

	
	@Bean
	public DirContext buildContextLdap() throws Exception {
		
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put("com.sun.jndi.ldap.read.timeout", "5000");
		env.put("com.sun.jndi.ldap.connect.timeout", "3000");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, ldapUsername);
		env.put(Context.SECURITY_CREDENTIALS, ldapPassword);
		env.put(Context.REFERRAL, "ignore");//propiedad que indica que ignore los resultados parciales no completados
		env.put(Context.PROVIDER_URL, ldapUrls);

		DirContext ctx = null;

		try {
			ctx = new InitialLdapContext(env, null);
		} catch (NamingException error) {
			throw new Exception("Problema al crear la conexión con el directorio activo: " + error.getMessage(), error);
		}

		return ctx;
	}

}