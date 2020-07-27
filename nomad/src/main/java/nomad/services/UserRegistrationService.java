package nomad.services;

import nomad.beans.UserGuest;
import nomad.dao.UserGuestDAO;

public class UserRegistrationService {
	
	private UserGuestDAO guestDAO;
	
	public UserRegistrationService(UserGuestDAO guestDAO)
	{
		this.guestDAO = guestDAO;
	}
	
	public boolean registerGuest(UserGuest guest)
	{
		return guestDAO.add(guest);
	}
	
}
