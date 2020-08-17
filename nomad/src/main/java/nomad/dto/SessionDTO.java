package nomad.dto;

import java.io.Serializable;

import nomad.beans.enums.UserType;

public class SessionDTO implements Serializable
{
	private static final long serialVersionUID = 7249328060906163269L;

	private String username;
	private UserType type;

	public SessionDTO(String username, UserType type)
	{
		this.username = username;
		this.type = type;
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public UserType getType()
	{
		return this.type;
	}

	public void setType(UserType type)
	{
		this.type = type;
	}
}
