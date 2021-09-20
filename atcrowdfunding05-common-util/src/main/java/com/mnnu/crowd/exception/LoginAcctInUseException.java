package com.mnnu.crowd.exception;

/**
 * 登陆账号已存在时抛出的异常，这个异常是为了代替框架主键重复异常 让异常点更清晰
 *
 * @author qiaoh
 */
public class LoginAcctInUseException extends RuntimeException {
    public LoginAcctInUseException() {
        super();
    }

    public LoginAcctInUseException(String message) {
        super(message);
    }

    public LoginAcctInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginAcctInUseException(Throwable cause) {
        super(cause);
    }

    protected LoginAcctInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
