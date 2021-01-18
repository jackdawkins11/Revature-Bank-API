/**
 * 
 */
package com.revature.exceptions;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class NoSuchUserException extends Exception {

	/**
	 * 
	 */
	public NoSuchUserException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NoSuchUserException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoSuchUserException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoSuchUserException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NoSuchUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
