package com.bancodebogota.gciades.ldap.otp;

import java.io.Serializable;

public class OtpParams implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String channel;

	private String message;

	private String refType;
	
	public OtpParams() {
		
	}
	
	public OtpParams(String channel, String message, String refType)
	{
		super();
		this.channel = channel;
		this.message = message;
		this.refType = refType;
	}

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}

	public String getRefType()
	{
		return refType;
	}

	public void setRefType(String refType)
	{
		this.refType = refType;
	}

}
