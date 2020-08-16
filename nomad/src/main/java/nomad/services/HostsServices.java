package nomad.services;

import java.util.ArrayList;
import java.util.Collection;

import nomad.beans.Apartment;
import nomad.beans.UserHost;
import nomad.dao.UserHostDAO;

public class HostsServices {
	private UserHostDAO hostDAO;
	
	public HostsServices(UserHostDAO hostDAO)
	{
		this.hostDAO = hostDAO;
	}
	
	public Collection<Apartment> getApartments(String username)
	{
		UserHost host = hostDAO.get(username);
		if(host == null)
		{
			return new ArrayList<Apartment>();
		}
		return new ArrayList<Apartment>(host.getApartments());
	}
}
