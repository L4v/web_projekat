package nomad.user;

import java.io.Serializable;

public class ParameterDTO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String username;
	private Sex sex;

	public ParameterDTO(String username, Sex sex)
	{
		this.username = username;
		this.sex = sex;
	}

	public String getUsername()
	{
		return this.username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public Sex getSex()
	{
		return this.sex;
	}

	public void setSex(Sex sex)
	{
		this.sex = sex;
	}
}
