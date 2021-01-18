/**
 * 
 */
package com.revature.exceptions;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class NoSuchAccountException extends Exception {

	/**
	 * 
	 */
	public NoSuchAccountException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public NoSuchAccountException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public NoSuchAccountException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NoSuchAccountException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NoSuchAccountException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
