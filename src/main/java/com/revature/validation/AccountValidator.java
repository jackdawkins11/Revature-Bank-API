/**
 * 
 */
package com.revature.validation;
import com.revature.exceptions.InvalidAccountParamsException;

/**
 * @author John Dawkins jackdawkins1298@outlook.com
 *
 */
public class AccountValidator {

	public static void validateAccountParams(double amount) throws InvalidAccountParamsException {
		//The amuount cannot be negative
		if( amount < 0 ) {
			throw new InvalidAccountParamsException();
		}
	}
}
