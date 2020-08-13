package nomad.services;

import java.util.ArrayList;
import java.util.Collection;

import nomad.beans.UserBase;
import nomad.dao.UserAdminDAO;
import nomad.dao.UserGuestDAO;
import nomad.dao.UserHostDAO;

public class AdminServices {

	private UserGuestDAO guestDAO;
	private UserAdminDAO adminDAO;
	private UserHostDAO hostDAO;
	
	public AdminServices(UserGuestDAO guestDAO, UserAdminDAO adminDAO, UserHostDAO hostDAO)
	{
		this.guestDAO = guestDAO;
		this.adminDAO = adminDAO;
		this.hostDAO = hostDAO;
	}
	
	public Collection<UserBase> getAll()
	{
		Collection<UserBase> users = new ArrayList<UserBase>();
		
		users.addAll(guestDAO.getAll());
		users.addAll(adminDAO.getAll());
		users.addAll(hostDAO.getAll());
		
		return users;
	}
	
}
