package nomad.services;

import nomad.beans.UserAdmin;
import nomad.beans.UserBase;
import nomad.beans.UserGuest;
import nomad.beans.UserHost;
import nomad.dao.UserAdminDAO;
import nomad.dao.UserGuestDAO;
import nomad.dao.UserHostDAO;
import nomad.dto.LoginDTO;

public class UserLoginService {

	private UserAdminDAO adminDAO;
	private UserGuestDAO guestDAO;
	private UserHostDAO hostDAO;
	
	public UserLoginService(UserAdminDAO adminDAO, UserGuestDAO guestDAO, UserHostDAO hostDAO)
	{
		this.adminDAO = adminDAO;
		this.guestDAO = guestDAO;
		this.hostDAO = hostDAO;
	}
	
	public UserBase validate(LoginDTO loginDTO)
	{
		for(UserAdmin u : adminDAO.findAll())
		{
			if(u.getUsername().equals(loginDTO.getUsername())
					&& u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}
	
		for(UserGuest u : guestDAO.getAll())
		{
			if(u.getUsername().equals(loginDTO.getUsername())
					&& u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}
		
		for(UserHost u : hostDAO.findAll())
		{
			if(u.getUsername().equals(loginDTO.getUsername())
					&& u.getPassword().equals(loginDTO.getPassword()))
			{
				return u;
			}
		}
		
		return null;
	}
	
}
