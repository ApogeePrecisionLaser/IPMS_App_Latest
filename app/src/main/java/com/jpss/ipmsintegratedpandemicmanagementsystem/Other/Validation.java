package com.jpss.ipmsintegratedpandemicmanagementsystem.Other;

import android.widget.EditText;

import java.util.regex.Pattern;

public class Validation {
	
	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	// private static final String PHONE_REGEX = "\\d{3}-\\d{7}";
	private static final String PHONE_REGEX = "\\d{10}";
	private static final String NUMBER_REGEX = "\\d+";
	private static final String DECIMAL_REGEX = "(\\d+\\.)?\\d+";

	private static final String IP_REGEX = "((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
			+ "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
			+ "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}" + "|[1-9][0-9]|[0-9]))";


	// Error Messages
	private static final String REQUIRED_MSG = "required";
	private static final String EMAIL_MSG = "invalid email";
	// private static final String PHONE_MSG = "###-#######";
	private static final String PHONE_MSG = "##########";
	private static final String NUMBER_MSG = "must be numeric";
	private static final String IP_MSG = "Invalid Ip";

	// call this method when you need to check ip validation
	public static boolean isIpAddress(EditText editText, boolean required) {
		return isValid(editText, IP_REGEX, IP_MSG, required);
	}

	// call this method when you need to check email validation
	public static boolean isEmailAddress(EditText editText, boolean required) {
		return isValid(editText, EMAIL_REGEX, EMAIL_MSG, required);
	}

	// call this method when you need to check phone number validation
	public static boolean isPhoneNumber(EditText editText, boolean required) {
		return isValid(editText, PHONE_REGEX, PHONE_MSG, required);
	}

	// call this method when you need to check number validation
	public static boolean isNumeric(EditText editText, boolean required) {
		return isValid(editText, NUMBER_REGEX, NUMBER_MSG, required);
	}

	// call this method when you need to check number or decimal numbers
	// validation
	public static boolean isDouble(EditText editText, boolean required) {
		return isValid(editText, DECIMAL_REGEX, NUMBER_MSG, required);
	}

	// return true if the input field is valid, based on the parameter passed
	public static boolean isValid(EditText editText, String regex, String errMsg, boolean required) {

		String text = editText.getText().toString().trim();
		// clearing the error, if it was previously set by some other values
		editText.setError(null);

		// text required and editText is blank, so return false
		if (required && !hasText(editText))
			return false;

		// pattern doesn't match so returning false
		if (required && !Pattern.matches(regex, text)) {
			editText.setError(errMsg);
			return false;
		}
		;

		return true;
	}

	// check the input field has any text or not
	// return true if it contains text otherwise false
	public static boolean hasText(EditText editText) {

		String text = editText.getText().toString().trim();
		editText.setError(null);

		// length 0 means there is no text
		if (text.length() == 0) {
			editText.setError(REQUIRED_MSG);
			return false;
		}

		return true;
	}

	public static boolean isPasswordMatch(EditText editText_pass, EditText editText_confirmpass, boolean required){
		boolean hastext=hasText(editText_pass);
		String pass=editText_pass.getText().toString().trim();
		String pass1=editText_confirmpass.getText().toString().trim();
		if(hastext){
			if(!pass.equals(pass1)){
				editText_confirmpass.setError("Password mismatch");
				return false;
			}
		}else{
			editText_pass.setError(REQUIRED_MSG);
		}
        return true;
	}

}
