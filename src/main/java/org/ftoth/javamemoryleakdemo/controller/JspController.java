package org.ftoth.javamemoryleakdemo.controller;

import org.ftoth.javamemoryleakdemo.exception.ValidationError;

import java.util.Map;

public class JspController
{
	public static final String CHECKBOX_CHECKED = "checked";

	protected String getRequiredStringParam(String paramName, String value)
		throws ValidationError
	{
		if (value == null || value.trim().equals("")) {
			throw new ValidationError("'" + paramName + "' required");
		}
		return value;
	}

	protected int getIntParam(String paramName, String value)
		throws ValidationError
	{
		int val;
		try {
			return Integer.parseInt(value);
		}
		catch (NumberFormatException e) {
			throw new ValidationError("'" + paramName + "': bad format");
		}
		catch (Exception e) {
			throw new ValidationError("'" + paramName + "': " + e.getMessage());
		}
	}

	protected int getRequiredIntParam(String paramName, String value)
		throws ValidationError
	{
		String val = getRequiredStringParam(paramName, value);
		return getIntParam(paramName, val);
	}


}
