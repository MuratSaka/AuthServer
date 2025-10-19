package com.project.auth.exception;

/**
 * @author Murat Saka
 * @created 19/10/2025 - 11:59
 * @project AuthServer
 */
public class ExpiredTokenException extends Exception{
    public ExpiredTokenException(String message){
        super(message);
    }
}
