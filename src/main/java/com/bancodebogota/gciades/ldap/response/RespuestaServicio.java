package com.bancodebogota.gciades.ldap.response;

public class RespuestaServicio
{

    private boolean error;

    private String message;

    public RespuestaServicio()
    {
        super();
    }

    public boolean isError()
    {
        return error;
    }

    public void setError(boolean error)
    {
        this.error = error;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String mensaje)
    {
        this.message = mensaje;
    }
    
}
