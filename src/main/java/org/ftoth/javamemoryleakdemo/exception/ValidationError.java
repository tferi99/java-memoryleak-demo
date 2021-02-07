package org.ftoth.javamemoryleakdemo.exception;

public class ValidationError extends Exception
{
	public ValidationError(String message)
	{
		super(message);
	}
}
