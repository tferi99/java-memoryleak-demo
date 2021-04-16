package org.ftoth.javamemoryleakdemo.model;

import org.ftoth.javamemoryleakdemo.model.TestData;

import java.util.List;

public class MyTestData
{
	private String id;
	private List<String> testData;

	public MyTestData(String id, List<String> testData)
	{
		this.testData = testData;
	}

	@Override
	public String toString()
	{
		int count = testData != null ? testData.size() : 0;
		return this.getClass().getSimpleName() + "[" + id + "] data: " + count;
	}
}
