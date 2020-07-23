package nomad.controllers;

import nomad.dao.UserAdminDAO;
import nomad.dao.UserGuestDAO;
import nomad.dao.UserHostDAO;

public class UserRegistrationService {
	
	private UserAdminDAO adminDAO;
	private UserGuestDAO guestDAO;
	private UserHostDAO hostDAO;
	
	public UserRegistrationService()
	{
		this.adminDAO = new UserAdminDAO();
		this.guestDAO = new UserGuestDAO();
		this.hostDAO = new UserHostDAO();
	}
	
	public boolean RegisterGuest()
	{
		
		return false;
	}
	
	public boolean RegisterHost()
	{
		
		return false;
	}
	
}
