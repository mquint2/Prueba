package com.bancodebogota.gciades.ldap.domain;

import java.io.Serializable;

public class UserAccountDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String sAMAccountName;

	private String userAccountControl;

	private String distinguishedName;
	
	private String pwdLastSet;
	

	public UserAccountDto() {
	}

	public UserAccountDto(String sAMAccountName, String userAccountControl, String distinguishedName, String pwdLastSet) {
		this.sAMAccountName = sAMAccountName;
		this.userAccountControl = userAccountControl;
		this.distinguishedName = distinguishedName;
		this.pwdLastSet = pwdLastSet;
	}

	public String getsAMAccountName() {
		return sAMAccountName;
	}

	public void setsAMAccountName(String sAMAccountName) {
		this.sAMAccountName = sAMAccountName;
	}

	public String getUserAccountControl() {
		return userAccountControl;
	}

	public void setUserAccountControl(String userAccountControl) {
		this.userAccountControl = userAccountControl;
	}

	public String getDistinguishedName() {
		return distinguishedName;
	}

	public void setDistinguishedName(String distinguishedName) {
		this.distinguishedName = distinguishedName;
	}

	public String getPwdLastSet() {
		return pwdLastSet;
	}

	public void setPwdLastSet(String pwdLastSet) {
		this.pwdLastSet = pwdLastSet;
	}	
	
}
