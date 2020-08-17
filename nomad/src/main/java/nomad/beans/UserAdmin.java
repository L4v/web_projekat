package nomad.beans;

import java.io.Serializable;

import nomad.beans.enums.Sex;
import nomad.beans.enums.UserType;

public class UserAdmin extends UserBase implements Serializable
{
	private static final long serialVersionUID = 7912416340864282937L;

	public UserAdmin(String username, String password, String name, String surname, Sex sex)
	{
		super(username, password, name, surname, sex, UserType.ADMIN);
	}
}
