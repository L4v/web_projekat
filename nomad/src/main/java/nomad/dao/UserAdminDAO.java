package nomad.dao;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nomad.beans.UserAdmin;

public class UserAdminDAO {

	private Map<String, UserAdmin> userAdmins = new HashMap<>();
	
	public UserAdminDAO() {
		
	}
	
	public UserAdminDAO(String contextPath) {
		loadUserAdmins(contextPath);
	}
	
	private void loadUserAdmins(String contextPath) {
		//TODO
	}
	
	public UserAdmin find(String username, String password) {
		if (!userAdmins.containsKey(username)) {
			return null;
		}
		UserAdmin userAdmin = userAdmins.get(username);
		if (!userAdmin.getPassword().equals(password)) {
			return null;
		}
		return userAdmin;
	}
	
	public Collection<UserAdmin> findAll(){
		return userAdmins.values();
	}
}
