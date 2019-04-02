package com.littlefox.storybook.lib.object;

public class ItemResult
{
	public String	code			= "";
	public String	message			= "";
	public String	device_id		= "";
	
	@Override
	public String toString()
	{
		return String.format("{\"code\":\"%s\", \"message\":\"%s\", \"device_id\":\"%s\"}",  code, message, device_id);
	}
}