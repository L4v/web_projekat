package nomad.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import nomad.apartment.Apartment;

public class UserHost extends UserBase implements Serializable
{
	private static final long serialVersionUID = 5032688984515531286L;
	private ArrayList<String> apartments;

	public UserHost(String username, String password, String name, String surname, Sex sex)
	{
		super(username, password, name, surname, sex, UserType.HOST);
		this.apartments = new ArrayList<String>();
	}
	
	public boolean addApartment(String apartmentId)
	{
		return apartments.add(apartmentId);
	}

	// TODO(Jovan): Not use direct get?
	public Collection<String> getApartments()
	{
		if(this.apartments == null)
		{
			this.apartments = new ArrayList<String>();
		}
		return this.apartments;
	}

}
