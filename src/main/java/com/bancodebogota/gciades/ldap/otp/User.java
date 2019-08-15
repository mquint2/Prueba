package com.bancodebogota.gciades.ldap.otp;

import java.io.Serializable;

public class User implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String typeId;

	private String id;

	//CRM dato seguro = 1, no seguro = 0
	private String safePhone;

	//Si se incluye phone no busca en CRM, dato seguro va en 0
	private String phone;

	private String ipAddr;

	public User()
	{

	}

	public User(String typeId, String id, String safePhone, String phone, String ipAddr)
	{
		super();
		this.typeId = typeId;
		this.id = id;
		this.safePhone = safePhone;
		this.phone = phone;
		this.ipAddr = ipAddr;
	}

	public User(String typeId, String id)
	{
		super();
		this.typeId = typeId;
		this.id = id;
	}

	public String getTypeId()
	{
		return typeId;
	}

	public void setTypeId(String typeId)
	{
		this.typeId = typeId;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getSafePhone()
	{
		return safePhone;
	}

	public void setSafePhone(String safePhone)
	{
		this.safePhone = safePhone;
	}

	public String getPhone()
	{
		return phone;
	}

	public void setPhone(String phone)
	{
		this.phone = phone;
	}

	public String getIpAddr()
	{
		return ipAddr;
	}

	public void setIpAddr(String ipAddr)
	{
		this.ipAddr = ipAddr;
	}

}
