package nomad.services;

import nomad.beans.UserGuest;
import nomad.dao.UserGuestDAO;

public class UserRegistrationService {
	
	private UserGuestDAO guestDAO;
	
	public UserRegistrationService()
	{
		this.guestDAO = new UserGuestDAO();
	}
	
	public boolean RegisterGuest(UserGuest guest)
	{
		return guestDAO.add(guest);
	}
	
}
