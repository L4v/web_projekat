package nomad.user;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import nomad.apartment.Apartment;

public class UserHost extends UserBase implements Serializable
{
	private static final long serialVersionUID = 5032688984515531286L;
	private ArrayList<Apartment> apartments;

	public UserHost(String username, String password, String name, String surname, Sex sex)
	{
		super(username, password, name, surname, sex, UserType.HOST);
		this.apartments = new ArrayList<Apartment>();
	}
	
	public boolean addApartment(Apartment apartment)
	{
		Apartment ap = this.apartments.stream().filter(a -> a.getId().equals(apartment.getId())).findAny().orElse(null);
		if(ap != null)
		{
			return false;
		}
		this.apartments.add(apartment);
		return true;
	}

	// TODO(Jovan): Not use direct get?
	public Collection<Apartment> getApartments()
	{
		if(this.apartments == null)
		{
			this.apartments = new ArrayList<Apartment>();
		}
		return this.apartments;
	}

}
