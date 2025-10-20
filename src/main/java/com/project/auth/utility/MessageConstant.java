package com.project.auth.utility;

/**
 * @author Murat Saka
 * @created 19/10/2025 - 12:13
 * @project AuthServer
 */
public class MessageConstant {

    //user
    public static final String LOGIN_SUCCESSFULLY = "login.successfully";
    public static final String LOGIN_FAILED = "login.failed";
    public static final String REGISTER_SUCCESSFULLY = "register.successfully";
    public static final String REGISTER_FAILED = "register.failed";


    //auth
    public static final String INSUFFICIENT_AUTHENTICATION = "authentication.insufficient";
    public static final String INVALID_CREDENTIALS = "authentication.invalid_credentials";
    public static final String AUTHENTICATION_FAILED = "authentication.failed";
    public static final String ACCESS_DENIED = "authorization.access_denied";
    public static final String USER_IS_LOCKED = "user.is_locked";
    public static final String USER_NOT_FOUND = "user.not_found";
    public static final String TOKEN_IS_EXPIRED = "token.is_expired";

}
