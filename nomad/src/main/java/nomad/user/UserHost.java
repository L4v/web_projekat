package nomad.user;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

import nomad.apartment.Apartment;

public class UserHost extends UserBase implements Serializable
{
	private static final long serialVersionUID = 5032688984515531286L;
	private HashMap<String, Apartment> apartments;

	public UserHost(String username, String password, String name, String surname, Sex sex)
	{
		super(username, password, name, surname, sex, UserType.HOST);
	}

	public Collection<Apartment> getApartments()
	{
		return this.apartments.values();
	}

}
