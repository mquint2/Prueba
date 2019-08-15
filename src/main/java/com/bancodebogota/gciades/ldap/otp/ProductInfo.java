package com.bancodebogota.gciades.ldap.otp;

import java.io.Serializable;

public class ProductInfo implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String productId;

	private String productCode;

	private String productNum;

	private String OTP;

	public ProductInfo()
	{

	}

	public ProductInfo(String productId, String productCode, String productNum, String oTP)
	{
		super();
		this.productId = productId;
		this.productCode = productCode;
		this.productNum = productNum;
		OTP = oTP;
	}

	public String getProductId()
	{
		return productId;
	}

	public void setProductId(String productId)
	{
		this.productId = productId;
	}

	public String getProductCode()
	{
		return productCode;
	}

	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}

	public String getProductNum()
	{
		return productNum;
	}

	public void setProductNum(String productNum)
	{
		this.productNum = productNum;
	}

	public String getOTP()
	{
		return OTP;
	}

	public void setOTP(String oTP)
	{
		OTP = oTP;
	}

}
