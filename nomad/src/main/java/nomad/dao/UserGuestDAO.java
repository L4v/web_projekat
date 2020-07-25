package nomad.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nomad.beans.UserGuest;


public class UserGuestDAO {

	private Map<String, UserGuest> userGuests = new HashMap<>();
	
	public UserGuestDAO() {
		
	}
	
	public UserGuestDAO(String contextPath) {
		loadUserGuests(contextPath);
	}
	
	private void loadUserGuests(String contextPath) {
		//TODO
	}
	
	public void update(UserGuest userGuest) {
		//TODO
	}
	
	public boolean add(UserGuest guest)
	{
		// TODO(Jovan): koristiti find?
		if(!userGuests.containsKey(guest.getName()))
		{
			userGuests.put(guest.getName(), guest);
			return true;
		}
		return false;
	}
	
	public UserGuest find(String username, String password) {
		if (!userGuests.containsKey(username)) {
			return null;
		}
		return userGuests.get(username);
	}
	
	public Collection<UserGuest> findAll(){
		return userGuests.values();
	}
}
