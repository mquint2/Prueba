package com.bancodebogota.gciades.ldap.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.bancodebogota.gciades.ldap.otp.Request;
import com.bancodebogota.gciades.ldap.response.RespuestaServicio;
import com.bancodebogota.gciades.ldap.services.EmployeeService;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.PUT, RequestMethod.GET, RequestMethod.POST})
@RestController
public class EmployeeController 
{

	@Autowired
	private EmployeeService employeeService;

	@PutMapping("/changePassword")
	public ResponseEntity<RespuestaServicio> changePassword(@RequestHeader("Credentials")String credentials)
	{
		return employeeService.changePassword(credentials);
	}

	@GetMapping("/healthCheck")
	public String threeScaleTestPath() {
		return "If you're seeing this message, it means that everything is alright ...";
	}
	
	@PutMapping("/changePasswordWithOTP")
	public ResponseEntity<RespuestaServicio> changePasswordWithOTP(@RequestHeader("Credentials")String credentials, @RequestHeader("otp") String otp, @RequestBody Request requestOTP)
	{
		return employeeService.changePasswordWithOTP(credentials, requestOTP, otp);
	}
	
	@PostMapping("/sendOTP")
	public ResponseEntity<RespuestaServicio> sendOTP(@RequestBody Request requestOTP) {
		return employeeService.sendOTP(requestOTP);
	}

}
