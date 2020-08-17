package nomad.beans;

import nomad.beans.enums.Sex;
import nomad.beans.enums.UserType;

public abstract class UserBase
{

	private String username;
	private String password;
	private String name;
	private String surname;
	private Sex sex;
	private UserType userType;

	
	public UserBase(String username, String password, String name, String surname, Sex sex, UserType userType) {

		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.sex = sex;
		this.userType = userType;
	}

	public String getUsername()
	{
		return username;
	}

	public void setUsername(String username)
	{
		this.username = username;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public Sex getSex()
	{
		return sex;
	}

	public void setSex(Sex sex)
	{
		this.sex = sex;
	}

	public UserType getUserType()
	{
		return userType;
	}

}
