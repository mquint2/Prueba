package com.bancodebogota.gciades.ldap.repositories;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.PartialResultException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.bancodebogota.gciades.ldap.domain.UserAccountDto;
import com.bancodebogota.gciades.ldap.utils.LdapError53Handler;

@Repository
public class LdapRepository {

	@Value("${spring.ldap.base}")
	private String ldap_base_dn;
	
	@Autowired
	private DirContext context;
	
	
	public UserAccountDto search(String sAMAccountName) throws Exception {

        String searchFilter = "(&(objectClass=user)(sAMAccountName=" + sAMAccountName + "))";
        String returnedAtts[] = { "sAMAccountName", "userAccountControl", "distinguishedName", "pwdLastSet" };

        SearchControls searchCtls = new SearchControls();
        searchCtls.setReturningAttributes(returnedAtts);        
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        
        NamingEnumeration<SearchResult> resultSearch;
        UserAccountDto dto  = null;
        try
        {
        	resultSearch = context.search(this.ldap_base_dn, searchFilter, searchCtls);
        	
            if(resultSearch.hasMore()) {
                SearchResult result = resultSearch.next();
                Attributes attrs = result.getAttributes();
                dto = new UserAccountDto();
                dto.setDistinguishedName( getAttr(attrs.get("distinguishedName")));
                dto.setPwdLastSet(getAttr(attrs.get("pwdLastSet")));
                dto.setsAMAccountName(getAttr(attrs.get("sAMAccountName")));
                dto.setUserAccountControl(getAttr(attrs.get("userAccountControl")));
            }

        } catch( PartialResultException e) {
        	//ignored. See https://docs.oracle.com/javase/8/docs/api/javax/naming/PartialResultException.html
        	//see description : https://docs.spring.io/spring-ldap/docs/current/apidocs/org/springframework/ldap/core/LdapTemplate.html
        } 
        catch(NamingException e) {
        	e.printStackTrace();//TODO: CORREGIR PARA ENVIAR A LOGS
        	throw new Exception("Ocurrió un problema no esperado consultando la cuenta de usuario " + sAMAccountName, e);
        }
        return dto;
	}
	
	public void changePassword(String distinguishedName, String password) throws Exception {

		try {
			String quotedPassword = "\"" + password + "\"";
			char unicodePwd[] = quotedPassword.toCharArray();
			byte pwdArray[] = new byte[unicodePwd.length * 2];
			for (int i = 0; i < unicodePwd.length; i++) {
				pwdArray[i * 2 + 1] = (byte) (unicodePwd[i] >>> 8);
				pwdArray[i * 2 + 0] = (byte) (unicodePwd[i] & 0xff);
			}
			ModificationItem[] mods = new ModificationItem[3];
			mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("UnicodePwd", pwdArray));
			mods[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl", "512"));
			mods[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("pwdLastSet", "-1"));
			context.modifyAttributes(distinguishedName, mods);
		} catch ( NamingException e) {
			e.printStackTrace(); //TODO: CORREGIR PARA ENVIAR A LOGS
			LdapError53Handler.handler(e);
			throw new Exception("Ocurrió un fallo no esperado cambiando contraseña.", e);
		}
	}
	
	private String getAttr( Attribute attr) {
		String value;
		try {
			value = attr != null ? String.valueOf(attr.get()) : null;
		} catch (NamingException e) {
			value = null;
		}
		return value;
	}
}
