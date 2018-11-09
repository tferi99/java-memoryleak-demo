package org.ftoth.javamemoryleakdemo.util;

public class SystemUtil
{
	public static int getOSBits()
	{
		String arch = System.getProperty("os.arch");
		if (arch.equals("x86")) {
			return 32;
		}
		return 64;
	}
}
