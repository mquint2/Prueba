package com.bancodebogota.gciades.ldap.otp;

import java.io.Serializable;

public class SimParams implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String channel;

	private String trnType;

	private String terminalId;

	public SimParams() {
		
	}
	
	public SimParams(String channel, String trnType, String terminalId, String serverStatusCodeValidate)
	{
		super();
		this.channel = channel;
		this.trnType = trnType;
		this.terminalId = terminalId;
		this.serverStatusCodeValidate = serverStatusCodeValidate;
	}

	//indica el codigo para sim validada
	private String serverStatusCodeValidate;

	public String getChannel()
	{
		return channel;
	}

	public void setChannel(String channel)
	{
		this.channel = channel;
	}

	public String getTrnType()
	{
		return trnType;
	}

	public void setTrnType(String trnType)
	{
		this.trnType = trnType;
	}

	public String getTerminalId()
	{
		return terminalId;
	}

	public void setTerminalId(String terminalId)
	{
		this.terminalId = terminalId;
	}

	public String getServerStatusCodeValidate()
	{
		return serverStatusCodeValidate;
	}

	public void setServerStatusCodeValidate(String serverStatusCodeValidate)
	{
		this.serverStatusCodeValidate = serverStatusCodeValidate;
	}

}
