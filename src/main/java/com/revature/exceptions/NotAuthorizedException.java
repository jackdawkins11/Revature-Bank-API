/**
 * 
 */
package com.revature.exceptions;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class NotAuthorizedException extends Exception {

	/**
	 * 
	 */
	public NotAuthorizedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NotAuthorizedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NotAuthorizedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NotAuthorizedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NotAuthorizedException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
