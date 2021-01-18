/**
 * 
 */
package com.revature.exceptions;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class InvalidUserParamsException extends Exception {

	InvalidUserParamsReason reason;

	/**
	 * @param reason
	 */
	public InvalidUserParamsException(InvalidUserParamsReason reason) {
		this.reason = reason;
	}

	/**
	 * @return the reason
	 */
	public InvalidUserParamsReason getReason() {
		return reason;
	}
	

}
