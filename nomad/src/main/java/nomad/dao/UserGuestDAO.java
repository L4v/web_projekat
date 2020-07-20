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
	
	public UserGuest find(String username, String password) {
		if (!userGuests.containsKey(username)) {
			return null;
		}
		UserGuest userGuest = userGuests.get(username);
		if (!userGuest.getPassword().equals(password)) {
			return null;
		}
		return userGuest;
	}
	
	public Collection<UserGuest> findAll(){
		return userGuests.values();
	}
}
