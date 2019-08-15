package com.bancodebogota.gciades.ldap.otp;

import java.io.Serializable;

public class Request implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private User user;

	private OtpParams otpParams;

	private SimParams simParams;

	private ProductInfo productoInfo;

	private String companyId;

	private String companyName;

	private String validateSim;

	public User getUser()
	{
		return user;
	}

	public void setUser(User user)
	{
		this.user = user;
	}

	public OtpParams getOtpParams()
	{
		return otpParams;
	}

	public void setOtpParams(OtpParams otp)
	{
		this.otpParams = otp;
	}

	public SimParams getSimParams()
	{
		return simParams;
	}

	public void setSimParams(SimParams sim)
	{
		this.simParams = sim;
	}

	public ProductInfo getProductoInfo()
	{
		return productoInfo;
	}

	public void setProductoInfo(ProductInfo productoInfo)
	{
		this.productoInfo = productoInfo;
	}

	public String getCompanyId()
	{
		return companyId;
	}

	public void setCompanyId(String companyId)
	{
		this.companyId = companyId;
	}

	public String getCompanyName()
	{
		return companyName;
	}

	public void setCompanyName(String companyName)
	{
		this.companyName = companyName;
	}

	public String getValidateSim()
	{
		return validateSim;
	}

	public void setValidateSim(String validateSim)
	{
		this.validateSim = validateSim;
	}

}
