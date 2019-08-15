package com.bancodebogota.gciades.ldap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bancodebogota.gciades.ldap.domain.UserAccountDto;
import com.bancodebogota.gciades.ldap.otp.OtpParams;
import com.bancodebogota.gciades.ldap.otp.ProductInfo;
import com.bancodebogota.gciades.ldap.otp.Request;
import com.bancodebogota.gciades.ldap.otp.SimParams;
import com.bancodebogota.gciades.ldap.otp.User;
import com.bancodebogota.gciades.ldap.repositories.LdapRepository;
import com.bancodebogota.gciades.ldap.response.RespuestaServicio;
import com.bancodebogota.gciades.ldap.utils.DecryptUtil;
import com.bancodebogota.gciades.ldap.utils.UserAccountControl;

@Service
public class EmployeeService
{

	@Autowired
	private LdapRepository ldapRepository;

	@Autowired
	private DecryptUtil decryptUtil;
	
	@Value("#{systemEnvironment['HOSTNAME_WS_OTP']}")
	private String endpointWsRestOTP;
	
	public ResponseEntity<RespuestaServicio> changePasswordWithOTP(String credentials, Request requestOTP, String otp)
	{
		RespuestaServicio respuesta = new RespuestaServicio();
		RestTemplate restTemplate = new RestTemplate();
		try {
			//creando nuevo request con parametros desencriptados
			Request decryptedRequest = new Request();
			decryptedRequest.setCompanyId(decryptUtil.decryptMessage(requestOTP.getCompanyId()));
			decryptedRequest.setCompanyName(decryptUtil.decryptMessage(requestOTP.getCompanyName()));
			decryptedRequest.setUser(new User(decryptUtil.decryptMessage(requestOTP.getUser().getTypeId()), decryptUtil.decryptMessage(requestOTP.getUser().getId()), decryptUtil.decryptMessage(requestOTP.getUser().getSafePhone()), decryptUtil.decryptMessage(requestOTP.getUser().getPhone()), decryptUtil.decryptMessage(requestOTP.getUser().getIpAddr())));
			decryptedRequest.setOtpParams(new OtpParams(decryptUtil.decryptMessage(requestOTP.getOtpParams().getChannel()), decryptUtil.decryptMessage(requestOTP.getOtpParams().getMessage()), decryptUtil.decryptMessage(requestOTP.getOtpParams().getRefType())));
			//atributos None para token virtual
			decryptedRequest.setProductoInfo(new ProductInfo("None","None","None",decryptUtil.decryptMessage(otp)));
			//validar OTP
			HttpEntity<Request> request = new HttpEntity<Request>(decryptedRequest);
			ResponseEntity<RespuestaServicio> respuestaServicioConsumo = restTemplate.exchange(endpointWsRestOTP + "/otp/validateOTP", HttpMethod.POST, request, RespuestaServicio.class);
			if (respuestaServicioConsumo.getStatusCode() != HttpStatus.OK){
				respuesta.setError(true);
				respuesta.setMessage("Error en validación de otp");
				return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			respuesta = respuestaServicioConsumo.getBody();
			if(respuesta.isError()) {
				return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.OK);
			}
			else {
				//si se valida la OTP se procede al cambio 
				return changePassword(credentials);
			}
		} catch(Exception ex) {
			respuesta.setError(true);
			respuesta.setMessage(ex.getMessage());
			return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	public ResponseEntity<RespuestaServicio> changePassword(String credentials) {

		RespuestaServicio respuesta = new RespuestaServicio();
		
		try {
			
			if( credentials == null || "".equals(credentials.trim()) || !credentials.contains(":")) 
				throw new IllegalArgumentException("Operación no permitida. Precondición Credentials no es válida");
			
			String userCredentials[] = credentials.split(":");
			String username = null;
			String password = null;
			
			try {
				username = userCredentials[0];
				password = userCredentials[1];
				//username = decryptUtil.decryptMessage(userCredentials[0]);
				//password = decryptUtil.decryptMessage(userCredentials[1]);
			} catch (Exception e) {
				 throw new IllegalStateException("Desencripción fallida", e);
			}
			
			UserAccountDto userAccount = this.ldapRepository.search(username);
			verifyUserAccount(userAccount);
			this.ldapRepository.changePassword(userAccount.getDistinguishedName(), password);
			
			respuesta.setError(false);
			respuesta.setMessage("Cambio de contraseña exitoso");;
			return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.OK);

		} catch( IllegalArgumentException | IllegalStateException e) {
			respuesta.setMessage(e.getMessage());
            respuesta.setError(true);
            return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.FORBIDDEN);
		} catch (Exception e) {
			respuesta.setMessage(e.getMessage());
            respuesta.setError(true);
            return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	private void verifyUserAccount( UserAccountDto userAccount ) {
		
		if( userAccount == null )
			throw new IllegalStateException("Cuenta de usuario no existe.");
		if( userAccount.getUserAccountControl() == null || "".equals( userAccount.getUserAccountControl().trim()))
			throw new IllegalStateException("La cuenta de usuario no cumple con las políticas de la entidad. UserAccountControl nulo o vacío");
		
		if( UserAccountControl.isDisabled(userAccount.getUserAccountControl()))
			throw new IllegalStateException("Operación no permitida. La cuenta de usuario está deshabilitada.");
		
		if( UserAccountControl.isDisableAndPasswordNotRequired(userAccount.getUserAccountControl()))
			return; //Si el estado de la cuenta de usuario es deshabilitada y no requiere contraseña dejarlo pasar, porque es un estado de creación por aplicación de terceros.
		
		if(!UserAccountControl.isOk(userAccount.getUserAccountControl()))
			throw new IllegalStateException("Operación no permitida. La cuenta de usuario no cumple con un estado válido definido por las políticas de seguridad de la entidad.");
		
		if( userAccount.getDistinguishedName() == null || "".equals( userAccount.getDistinguishedName().trim()))
			throw new IllegalStateException("La cuenta de usuario no cumple con las políticas de la entidad. DistinguishedName nulo o vacío");
	}

	
    public ResponseEntity<RespuestaServicio> sendOTP(Request requestOTP) {
		RespuestaServicio respuesta = new RespuestaServicio();
		RestTemplate restTemplate = new RestTemplate();
		try {
			//creando nuevo request con parametros desencriptados
			Request decryptedRequest = new Request();
			decryptedRequest.setCompanyId(decryptUtil.decryptMessage(requestOTP.getCompanyId()));
			decryptedRequest.setCompanyName(decryptUtil.decryptMessage(requestOTP.getCompanyName()));
			decryptedRequest.setUser(new User(decryptUtil.decryptMessage(requestOTP.getUser().getTypeId()), decryptUtil.decryptMessage(requestOTP.getUser().getId()), decryptUtil.decryptMessage(requestOTP.getUser().getSafePhone()), decryptUtil.decryptMessage(requestOTP.getUser().getPhone()), decryptUtil.decryptMessage(requestOTP.getUser().getIpAddr())));
			decryptedRequest.setOtpParams(new OtpParams(decryptUtil.decryptMessage(requestOTP.getOtpParams().getChannel()), decryptUtil.decryptMessage(requestOTP.getOtpParams().getMessage()), decryptUtil.decryptMessage(requestOTP.getOtpParams().getRefType())));
			decryptedRequest.setSimParams(new SimParams(decryptUtil.decryptMessage(requestOTP.getSimParams().getChannel()), decryptUtil.decryptMessage(requestOTP.getSimParams().getTrnType()), decryptUtil.decryptMessage(requestOTP.getSimParams().getTerminalId()), decryptUtil.decryptMessage(requestOTP.getSimParams().getServerStatusCodeValidate())));
			decryptedRequest.setValidateSim(decryptUtil.decryptMessage(requestOTP.getValidateSim()));
			//llamando servicio envio otp
			HttpEntity<Request> request = new HttpEntity<Request>(decryptedRequest);
			ResponseEntity<RespuestaServicio> respuestaServicioConsumo = restTemplate.exchange(endpointWsRestOTP + "/otp/sendOTP", HttpMethod.POST, request, RespuestaServicio.class);
			if (respuestaServicioConsumo.getStatusCode() != HttpStatus.OK){
				respuesta.setError(true);
				respuesta.setMessage("Error en envio de otp");
				return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			respuesta = respuestaServicioConsumo.getBody();
			int longitudTelefono = decryptedRequest.getUser().getPhone().length();
			if(longitudTelefono>4)
				respuesta.setMessage("******"+decryptedRequest.getUser().getPhone().substring(longitudTelefono-4, longitudTelefono));
		}catch (Exception e){
			e.printStackTrace();
			respuesta.setMessage(e.getMessage());
			respuesta.setError(true);
			return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<RespuestaServicio>(respuesta, HttpStatus.OK);		
	}
}
