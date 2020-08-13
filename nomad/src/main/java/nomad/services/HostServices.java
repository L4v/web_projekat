package nomad.services;

import java.util.ArrayList;
import java.util.Collection;

import nomad.beans.Reservation;
import nomad.beans.UserGuest;
import nomad.dao.UserGuestDAO;
import nomad.dao.UserHostDAO;

public class HostServices {

	private UserGuestDAO guestDAO;
	private UserHostDAO hostDAO;
	
	public HostServices(UserGuestDAO guestDAO, UserHostDAO hostDAO)
	{
		this.guestDAO = guestDAO;
		this.hostDAO = hostDAO;
	}
	
	public Collection<UserGuest> getMyGuests()
	{
		Collection<UserGuest> users = new ArrayList<UserGuest>();
		
		for (UserGuest userGuest : guestDAO.getAll()) 
		{
			for(Reservation reservation : userGuest.getReservations()) 
			{
				//if(reservation.getApartment().getHost().equals()) 
				{
					users.add(userGuest);
					break;
				}
			}
		}
		
		return users;
	}
}
